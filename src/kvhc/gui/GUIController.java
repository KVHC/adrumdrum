/**
 * aDrumDrum is a step sequencer for Android.
 * Copyright (C) 2012  Daniel Fallstrand, Niclas Ståhl, Oscar Dragén and Viktor Nilsson.
 *
 * This file is part of aDrumDrum.
 *
 * aDrumDrum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aDrumDrum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aDrumDrum.  If not, see <http://www.gnu.org/licenses/>.
 */

package kvhc.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import kvhc.adrumdrum.R;
import kvhc.gui.dialogs.LoadSongDialog;
import kvhc.gui.dialogs.SaveSongDialog;
import kvhc.gui.dialogs.StepDialog;
import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.models.Sound;
import kvhc.models.Step;
import kvhc.util.Player;
import kvhc.util.db.SoundDataSource;

/**
 * Master class of the GUI.
 */
public class GUIController {

	// Default variables
	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final int DEFAULT_NUMBER_OF_CHANNELS = 5;
	private static final int DEFAULT_BPM_PERCENTAGE_OF_MAX = 40;
	private static final String NAME_OF_CLASS = "GUIController";
	
	// Default variables
	private Player player;
	private Song song;
	private TextView tv1;
	private int solo;
	
	private Activity parentActivity;
	private Map<String, Sound> mSoundManager;
	
	private static String SOUND_NAME_NO_SOUND = "No Sound";
	
    /**
     * An array of Strings containing the names of the different sounds.
     * This should be done in a different way (?)
     */
	private SoundDataSource mDBsoundHelper; 
	
	/**
	 * Constructor.
	 * @param activity the main activity
	 */
	public GUIController(Activity activity) {
		parentActivity = activity;
		
		this.player = new Player(parentActivity.getBaseContext());
		player.addObserver(new GUIUpdateObserver(parentActivity));
		
		mDBsoundHelper = new SoundDataSource(parentActivity);
		
		init();
	}
	
	
	//----------------------------------------------------------------------
	// Methods that initialize the GUIcontroller
	
	/**
	 * Initialize all the init functions.
	 */
	private final void init(){
		initSong();
		initText();
		initButtons();
		initBars();
		initChannels();
		// -1 in the solo variable means that no channel play solos
		solo = -1;
	}
	
	/**
	 * Initialize sample song.
	 */
	private final void initSong() {
		// Okay, make a song
		
		mDBsoundHelper.open();
		mSoundManager = new HashMap<String, Sound>();

		for(Sound sound : mDBsoundHelper.getAllSounds()) {
			mSoundManager.put(sound.getName(), sound);
		}
		mDBsoundHelper.close();
		
		// Creates list of sounds for channel.
		List<Sound> sounds = new ArrayList<Sound>(DEFAULT_NUMBER_OF_CHANNELS);
		
		sounds.add(mSoundManager.get("Bassdrum"));
		sounds.add(mSoundManager.get("Closed hihat"));
		sounds.add(mSoundManager.get("Snare 02"));
		sounds.add(mSoundManager.get("Crash 01"));
		sounds.add(mSoundManager.get("Tomtom 02"));
		
		// Create channels
		List<Channel> channels = new ArrayList<Channel>(sounds.size());
		
		// No saved song yet so this is how we roll.
		for (int i=0; i < sounds.size(); i++)  {
			// Adds a new channel
			channels.add(new Channel(sounds.get(i), DEFAULT_NUMBER_OF_STEPS));
		}
		
		song = new Song(channels);
		
		player.loadSong(song);
	}
	
	/**
	 * Initialize the default channel rows.
	 */
	private final void initChannels() {
		redrawChannels();
	}
		
	/**
	 * Initialize a TextView showing status messages.
	 */
    private final void initText() {
    	tv1 = (TextView)parentActivity.findViewById(R.id.textView1);
	}

    /**
     * Initialize the necessary GUI-buttons (Play/Stop, Add Channel, Remove Step etc).
     */
	private final void initButtons() {
    	   Button btn1 = (Button)parentActivity.findViewById(R.id.button1);
           btn1.setOnClickListener(playOrStopClickListener);
           
           Button addChnl = (Button)parentActivity.findViewById(R.id.buttonAddChannel);
           addChnl.setOnClickListener(addChannelListener);
           
           Button addStep = (Button)parentActivity.findViewById(R.id.buttonAddStep);
           addStep.setOnClickListener(addStepListener);
           
           Button remStep = (Button)parentActivity.findViewById(R.id.buttonRemoveStep);
           remStep.setOnClickListener(removeStepListener);
	}
	
	/**
	 * Initialize the BPM-bar.
	 */
	private final void initBars(){

		SeekBar bpmBar = (SeekBar)parentActivity.findViewById(R.id.bpmbar);
		player.setBPMInRange(DEFAULT_BPM_PERCENTAGE_OF_MAX);
		bpmBar.setProgress(DEFAULT_BPM_PERCENTAGE_OF_MAX);
		song.setBpm(DEFAULT_BPM_PERCENTAGE_OF_MAX);
		
    	bpmBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				int bpm = player.setBPMInRange(progress);
				song.setBpm(progress);
				tv1.setText("BPM is: " + bpm);
				
			}

			public void onStartTrackingTouch(SeekBar arg0) {}
			public void onStopTrackingTouch(SeekBar arg0) {}
        });
    	
    }
	
	
	//-------------------------------------------------------------------
	// Methods for modifying the song
	
	
	/**
	 * Adds a new channel to the GUI.
	 * Checks the number of steps of the song and adds that many steps,
	 * adds and links a ChannelButtonGUI and a ChannelMuteButton to it. 
	 * @param c the Channel to be added
	 */
	private void addChannel(Channel c) {
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		TableRow row = new TableRow(channelContainer.getContext());
		
		// Add ChannelButtonGUI and set its name label
		ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,song.getNumberOfChannels()-1,this);
		if(c.getSound() != null) {
			name.setText(c.getSound().getName());
		} else {
			name.setText(SOUND_NAME_NO_SOUND);
		}
		row.addView(name);
		
		//Add ChannelMuteButton
		ChannelMuteButton mute = new ChannelMuteButton(parentActivity,c,this);
		row.addView(mute);
		
		// Add all the steps respective GUIStepButton and set their Listeners
		for(int x = 0; x < song.getNumberOfSteps(); x++) {
			GUIStepButton box = new GUIStepButton(row.getContext(), song.getNumberOfChannels()-1, c.getStepAt(x));
			box.setOnClickListener(stepClickListener);
			box.setOnLongClickListener(stepLongClickListener);
			row.addView(box);
		}
		channelContainer.addView(row);
		
		// Load the extended Song
		player.loadSong(song);
	}
	
	/**
	 * Removes a channel with the specified index.
	 * @param channelIndex
	 */
    public void removeChannel(int channelIndex) {
    	if (song.removeChannel(channelIndex)) {
    		player.loadSong(song);
    		redrawChannels();
    	}
    }   
    	
	
    /**
     * If the channel already is playing solo this method ends the solo and unmute all other channels.
     * If the channel don't play solo this method mute all channels except the given.
     * @param channel the id of the channel to toggle solo on
     */
    public void toggleSolo(int channel){
    	if (channel == solo){
    		song.playAll();
    		solo = -1;
    	} else {
    		song.muteAllChannelsExcept(channel);
    		solo = channel;
    	}
    }
    
    /**
     * A method for getting the id of the channel that playes solo.
     * Returns -1 if no channels are playing solo.
     * @return The id of the channel that plays solo or zero if no Channel is
     */
    public int getSoloChannel(){
    	return solo;
    }

    
    /**
     * If solo-mode has been enabled previously, this resets the solo-variable.
     * solo represents a channel number if it is higher than or equal to 0.
     */
    public void stopSolo(){
    	solo = -1;
    }
  
    
	/**
	 * Reloads a given song. Calls numerateSteps so all the steps isn't step number one.
	 * @param song the Song to reload
	 */
	public void reloadSong(Song song) {
		// Do we want to continue playing? Yes? No?
		boolean isPlaying = player.isPlaying();
		player.stop();
		
		Log.d(NAME_OF_CLASS, "Song to be loaded: " + song.getId());
		Log.d(NAME_OF_CLASS, "Song bpm: " + song.getBpm());
		Log.d(NAME_OF_CLASS, "Song name: " + song.getName());
		Log.d(NAME_OF_CLASS, "Number of channels: " + song.getNumberOfChannels());
		Log.d(NAME_OF_CLASS, "Get number of steps: " + song.getNumberOfSteps());
		
		// Add the song to the GUI 
		this.song = song;
		numerateSteps();
		redrawChannels();
		SeekBar bpmBar = (SeekBar)parentActivity.findViewById(R.id.bpmbar);
		bpmBar.setProgress(song.getBpm());
		
		// Load the newly loaded song into the player

		player.loadSong(song);
		
		// Start playing again if it way playing before. New sneaky feature.
		// This will soooo be deleted soon!
		if(isPlaying) {
			player.play();
		}
	}
	
	
	/**
     * Sets the steps velocity to 100%, the two neighbors to 70% and
     * the neighbors neighbors to 30%. Activates the mentioned steps.
     * @param channelId on which channel to set the spike
     * @param stepid on which step to set the spike
     */
    public void setSpike(int channelId, int stepid){
    	song.getChannel(channelId).multiStepVelocitySpike(stepid);
    }
    
    /**
     * Stops Playback and clears all steps. Redraws all Channels.
     */
    public void clearAllSteps() {
    	player.stop();
    	initSong();
		redrawChannels();
    }
 
	
	//-----------------------------------------------------------------------
	// Methods for updating the GUI
	
    /**
     * Numerate the steps. Used when Loading a song from the database.
     */
    private void numerateSteps(){
    	for (Channel c: song.getChannels()){
    		int number = 0;
    		for (Step s : c.getSteps()){
    			s.setStepNumber(number);
    			number++;
    		}
    	}
    }
	   
    /**
     * Redraws all the Channel and their steps and their ChannelButtons.
     * This is done when adding or removing steps.
     * 
     * It redraws all the channels and steps, this is not really necessary.
     * THIS IS VERY OPTIMIZABLE
     */
    public void redrawChannels() {
		// Get the channel container.
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		channelContainer.removeAllViewsInLayout();
		int y = 0;
		// For each channel in song.
		for(Channel c: song.getChannels()) {
			// Create a new row.
			TableRow row = new TableRow(channelContainer.getContext());
			// Create a ChannelButton and set the name tag.
			ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,y,this);
			// Create a ChannelMuteButton.
			ChannelMuteButton mute = new ChannelMuteButton(parentActivity,c,this);
			if(c.getSound() != null) {
				name.setText(c.getSound().getName());
			} else {
				name.setText(SOUND_NAME_NO_SOUND);
			}
			// Add views to row.
			row.addView(name);
			row.addView(mute);
			// Create and add all the steps.
			for(int x = 0; x < song.getNumberOfSteps(); x++) {
				GUIStepButton box = new GUIStepButton(row.getContext(), y, c.getStepAt(x), c.isStepActive(x));
				box.setOnClickListener(stepClickListener);
				box.setOnLongClickListener(stepLongClickListener);
				row.addView(box);
			}
			// Add the row to the table.
			channelContainer.addView(row);
			y++;
		}
		// Show table.
		channelContainer.setVisibility(View.VISIBLE);
		channelContainer.invalidate();
	}
    
    
    /**
	 * Update the name on the GUI-button
	 * @param channelId 
	 */
	public void updateButtonGUI(int channelId){
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
  
    	if (channelId <  channelContainer.getChildCount()){
    		TableRow row = (TableRow)channelContainer.getChildAt(channelId);
    		ChannelButtonGUI guib = (ChannelButtonGUI) row.getChildAt(0);
    		guib.updateName();
    	}
	}
    
    
    /**
     * Method for invalidating all elements in the channel container.
     * This fixes some trouble we had on non-Samsung phones.
     */
    public void invalidateAll(){
    	TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
    	
    	for (int i = 0; i < channelContainer.getChildCount(); i ++){
    		TableRow row = (TableRow)channelContainer.getChildAt(i);
    		for (int j = 0; j < row.getChildCount(); j ++){
    			row.getChildAt(j).invalidate();
    		}
    	}
    }
    
    
    //-------------------------------------------------------------------------
    // Methods for what to do then the GUIControler are destroyed
    
	/**
	 * Call onStop.
	 */
	public void onStop() {
    	player.stop();
    }
    
	/**
	 * Call onDestroy.
	 */
    public void onDestroy() {
    	player.stop();
    }
    
   
    //------------------------------------------------------------------------------------
    // Listeners:    
    
	/**
	 * Listener to the step buttons.
	 */
	private OnClickListener stepClickListener = new OnClickListener() {

        public void onClick(View v) {
        	// Retrieves the checkbox and inverts its value
            GUIStepButton box = (GUIStepButton) v;
            box.reverse();
            
            // Updates the corresponding channel
            box.setActive(song.getChannel(box.getChannelId()).toggleStep(box.getStepId()));
        }
    };
    
    /**
     * LongClick Listener to the step button. Creates and shows a StepDialog.
     */
	private OnLongClickListener stepLongClickListener = new OnLongClickListener() {

		public boolean onLongClick(View v) {
			GUIStepButton gsb = (GUIStepButton) v;
			StepDialog vd = new StepDialog(parentActivity, gsb.getStep(), 
								GUIController.this, gsb.getChannelId(), gsb.getStepId());
			vd.show();

			return true;
		}
	};
	
    
	/**
	 * Listener to the Play/Stop-button.
	 */
    private OnClickListener playOrStopClickListener = new OnClickListener()
    {
		public void onClick(View v) {
			if (player.isPlaying()) {
    			player.stop();
    			tv1.setText("Stopped");
    			
    		} else {
    			player.play();
    			tv1.setText("Playing");
    		}
		}
    }; 
    
    /**
     * Listener to the add-new-channel-button. Doesn't redraw the old channels.
     */
    private OnClickListener addChannelListener = new OnClickListener() {
		public void onClick(View v) {
			// Spinner for sound sample selection.
			final Spinner input = new Spinner(parentActivity);
			mDBsoundHelper.open();
			ArrayAdapter<Sound> spinnerArrayAdapter = new ArrayAdapter<Sound>(parentActivity, 
									android.R.layout.simple_spinner_dropdown_item, 
										mDBsoundHelper.getAllSounds());
			mDBsoundHelper.close();
			input.setAdapter(spinnerArrayAdapter);
			// Build the dialog.
			AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
			builder.setView(input);	
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	// Set up 
			    	String name = String.valueOf(input.getSelectedItem());
			        Sound s = mSoundManager.get(name);
			        Channel c = new Channel(s, song.getNumberOfSteps());
			        song.addChannel(c);
			        addChannel(c);
			        player.loadSong(song); // Should just be like... add sound? 
			        dialog.dismiss();
			    }
			});
			builder.create();
			builder.show();
		}
	};

	/**
	 * Listener to the add-new-step-button. Redraws all the channels.
	 */
	private OnClickListener addStepListener = new OnClickListener() {
		
		public void onClick(View v) {
			// Add one step to the Song and GUI
			player.stop();
			tv1.setText("Stopped");
			song.addSteps(1);
			player.loadSong(song);
			redrawChannels();
		}
	};
	
	/**
	 * Listener to the remove-step-button. Redraws all the channels.
	 */
	private OnClickListener removeStepListener = new OnClickListener() {
		
		public void onClick(View v) {
			player.stop();
			tv1.setText("Stopped");
			song.removeSteps(1);
			player.loadSong(song);
			redrawChannels();
		}
	};

	
	//-----------------------------------------------------------------------------------
	// Methods for showing dialogs
	
	/**
	 * Shows the license(s) in a Dialog.
	 * Reads a textfile from res/raw, creates a simple dialog and puts the read text in the dialog.
	 */
	public void createAndShowLicensesDialog(){	
		InputStream stream = parentActivity.getResources().openRawResource(R.raw.licenses);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line;
		String licenses;

		// Read the text file line by line and add a line break
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			stream.close();
			br.close();
		} catch (IOException e) {
			Log.e(getClass().toString(), e.getMessage());
		}
        licenses = sb.toString();
		
        // Create and show the dialog
		AlertDialog dialog = new AlertDialog.Builder(parentActivity).create();
		dialog.setTitle(R.string.licenses);
		dialog.setMessage(licenses);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * Creates a SaveSongDialog and shows it.
	 */
	public void createAndShowSaveSongDialog() {
		SaveSongDialog saveDialog = new SaveSongDialog(parentActivity, song);
		saveDialog.show();
	}
	
	/**
	 * Creates a LoadSongDialog and shows it.
	 */
	public void createAndShowLoadSongDialog() {
		final LoadSongDialog loadDialog = new LoadSongDialog(parentActivity);
		loadDialog.setOnDismissListener(new OnDismissListener() {			
			// Set what the dialog should do then dismissed
			public void onDismiss(DialogInterface dialog) {
				Song lSong = loadDialog.getSong();
				if(lSong != null) {
					reloadSong(lSong);
				}
			}
		});
		loadDialog.show();
	}

}
