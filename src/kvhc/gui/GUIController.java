/**
 * aDrumDrum is a stepsequencer for Android.
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Player;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.util.SoundFetcher;
import kvhc.player.Step;
import kvhc.util.AssetManagerModel;
import kvhc.util.ISongLoader;
import kvhc.util.ISongRenderer;
import kvhc.util.db.SQLRenderer;
import kvhc.util.db.SQLSongLoader;


/**
 * Master class of the GUI.
 */
public class GUIController {

	private Player player;
	private Song song;
	private TextView tv1;
	private int solo;
	
	private Activity parentActivity;
	//private AssetManagerModel<Sound> mSoundManager = AssetManagerModel.getSoundManager();
	private HashMap<String, Sound> mSoundManager;
	
	ISongRenderer sqlWriter;
	ISongLoader sqlLoader;
	
    /**
     * An array of Strings containing the names of the different sounds.
     * This should be done in a different way (?)
     */
    private ArrayList<String> sampleArray = null;
	SoundDataSource mDBsoundHelper; 
	
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
	
	/**
	 * Init all the init functions.
	 */
	private void init(){
		initSong();
		initText();
		initButtons();
		initBars();
		initChannels();
		solo = -1;
	}
	
	/**
	 * Init sample song.
	 */
	private void initSong() {
		// Okay, make a song
		
		// Adding sounds to the sound manager because we might now have them or something
		sqlWriter = new SQLRenderer(parentActivity);
		sqlLoader = new SQLSongLoader(parentActivity);
		
		mDBsoundHelper.open();
		mSoundManager = new HashMap<String, Sound>();
		//List<Sound> soundss = mDBsoundHelper.getAllSounds();
		for(Sound sound : mDBsoundHelper.getAllSounds()) {
			mSoundManager.put(sound.getName(), sound);
		}
		mDBsoundHelper.close();
		// Creates list of sounds for channel.
		ArrayList<Sound> sounds = new ArrayList<Sound>(4);
		
		sounds.add(mSoundManager.get("Bassdrum"));
		sounds.add(mSoundManager.get("Closed hihat"));
		sounds.add(mSoundManager.get("Snare 01"));
		sounds.add(mSoundManager.get("Ride"));
		
		// Create channels
		ArrayList<Channel> channels = new ArrayList<Channel>(sounds.size());
		
		// No saved song yet so this is how we roll.
		for (int i=0; i < sounds.size(); i++)  {
			// Adds a new channel
			channels.add(new Channel(sounds.get(i), 8));
		}
		
		song = new Song(channels);
		
		player.loadSong(song);
	}
	
	/**
	 * Init the default channel rows.
	 */
	private void initChannels() {
		redrawChannels();
	}
		
	/**
	 * Inits a TextView showing status messages.
	 */
    private void initText() {
    	tv1 = (TextView)parentActivity.findViewById(R.id.textView1);
		
	}

    /**
     * Inits the necessary GUI-buttons (Play/Stop, Add Channel, Remove Step etc).
     */
	private void initButtons() {
    	   Button btn1 = (Button)parentActivity.findViewById(R.id.button1);
           btn1.setOnClickListener(btnListener);
           
           Button addChnl = (Button)parentActivity.findViewById(R.id.buttonAddChannel);
           addChnl.setOnClickListener(addChannelListener);
           
           Button addStep = (Button)parentActivity.findViewById(R.id.buttonAddStep);
           addStep.setOnClickListener(addStepListener);
           
           Button remStep = (Button)parentActivity.findViewById(R.id.buttonRemoveStep);
           remStep.setOnClickListener(removeStepListener);
	}
	
	/**
	 * Inits the BPM-bar.
	 */
	private void initBars(){
		SeekBar bpmBar = (SeekBar)parentActivity.findViewById(R.id.bpmbar);
    	
    	bpmBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				player.setBPMInRange(progress);
				tv1.setText("BPM is: " + (60 + progress*6));
				
			}

			public void onStartTrackingTouch(SeekBar arg0) {}
			public void onStopTrackingTouch(SeekBar arg0) {}
        });
    }
	
	
	/**
	 * Adds a new channel.
	 * Checks the number of steps of the song and adds that many steps,
	 * adds and links a ChannelButtonGUI and a ChannelMuteButton to it.
	 * 
	 * @param c the Channel to be added
	 */
	private void addChannel(Channel c) {
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		TableRow row = new TableRow(channelContainer.getContext());
		
		// Name label
		ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,song.getNumberOfChannels()-1,this);
		if(c.getSound() != null) {
			name.setText(c.getSound().getName());
		} else {
			name.setText("No Sound");
		}
		row.addView(name);
		ChannelMuteButton mute = new ChannelMuteButton(parentActivity,c);
		row.addView(mute);
		
		for(int x = 0; x < song.getNumberOfSteps(); x++) {
			GUIStepButton box = new GUIStepButton(row.getContext(), song.getNumberOfChannels()-1, c.getStepAt(x));
			box.setOnClickListener(stepClickListener);
			box.setOnLongClickListener(stepLongClickListener);
			row.addView(box);
		}
		channelContainer.addView(row);
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
     * A method for getting the id of the channel that playes solo
     * returns -1 if no channels are playing solo
     * @return The id of the channel that plays solo
     */
    public int getSoloChannel(){
    	return solo;
    }

    
    /**
     * Create a sample list. Used when adding a new channel or when changing sound sample
     * of an existing channel. This should be done in a different way.
     */
    private void createSampleList() {
    	if(sampleArray == null) {
    		sampleArray = new ArrayList<String>(16);
    		sampleArray.add("Bassdrum");
    		sampleArray.add("Bell Ride Cymbal");
    		sampleArray.add("Crash Cymbal 01");
    		sampleArray.add("Crash Cymbal 02");
    		sampleArray.add("Hihat Closed");
    		sampleArray.add("Hihat Open");
    		sampleArray.add("Ride Cymbal");
    		sampleArray.add("Snare 01");
    		sampleArray.add("Snare 02");
    		sampleArray.add("Snare 03");
    		sampleArray.add("Splash Cymbal 01");
    		sampleArray.add("Splash Cymbal 02");
    		sampleArray.add("Tomtom 01");
    		sampleArray.add("Tomtom 02");
    		sampleArray.add("Tomtom 03");
    	}
    }

    
    /**
     * Redraws all the Channel and their steps and their ChannelButtons.
     * This is done when adding or removing steps (and would be done if
     * we implemented removeChannel()). 
     * 
     * Now it redraws all the channels and steps, this is not really neccessary
     * THIS IS VERY OPTIMIZABLE
     */
    public void redrawChannels() {
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		channelContainer.removeAllViewsInLayout();
		
		int y = 0;
		for(Channel c: song.getChannels()) {
			
			TableRow row = new TableRow(channelContainer.getContext());
			
			// Create a ChannelButton and set the name tag
			ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,y,this);

			name.setText(c.getSound().getName());
			// Create a ChannelMuteButton
			ChannelMuteButton mute = new ChannelMuteButton(parentActivity,c);

			if(c.getSound() != null) {
				name.setText(c.getSound().getName());
			} else {
				name.setText("No Sound");
			}
			//name.setOnLongClickListener(channelSettingsListener);

			row.addView(name);
			row.addView(mute);
			
			// All the steps
			for(int x = 0; x < song.getNumberOfSteps(); x++) {
				
				GUIStepButton box = new GUIStepButton(row.getContext(), y, c.getStepAt(x), c.isStepActive(x));	// Construction
				box.setOnClickListener(stepClickListener);						// Listener
				box.setOnLongClickListener(stepLongClickListener);
				row.addView(box);
				
			}
			channelContainer.addView(row);
			y++;
		}


		channelContainer.setVisibility(View.VISIBLE);
		channelContainer.invalidate();
	}
    
    /**
     * Stops Playback and clears all steps. Redraws all Channels.
     */
    public void clearAllSteps() {
    	player.stop();
		song.clearAllSteps();
		redrawChannels();
    }
    
    /**
     * Method for invalidating all elements in the channel container.
     * We had troubles with this on non-Samsung phones.
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
    
    
    //
    // Listeners:
    //
    
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
			StepDialog vd = new StepDialog(parentActivity, gsb.getStep(), GUIController.this);
			vd.show();
			
			return true;
		}
	};
    
	/**
	 * Listener to the Play/Stop-button.
	 */
    private OnClickListener btnListener = new OnClickListener()
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
			
			// Spinner for sound sample selection
			final Spinner input2 = new Spinner(parentActivity);
			
			mDBsoundHelper.open();
			ArrayAdapter<Sound> spinnerArrayAdapter = new ArrayAdapter<Sound>(parentActivity, android.R.layout.simple_spinner_dropdown_item, mDBsoundHelper.getAllSounds());
			mDBsoundHelper.close();
			
			input2.setAdapter(spinnerArrayAdapter);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
			builder.setView(input2);	
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			        
			    	String name = String.valueOf(input2.getSelectedItem());
			        Sound s = SoundFetcher.GetSoundFromString(name);
			        Sound s2 = mSoundManager.getValue(name);
			        
			        Channel c = new Channel(s, song.getNumberOfSteps());
			        song.addChannel(c);
			        addChannel(c);
			        
			        player.loadSong(song);
			        
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
			// Add one step to the song and gui
			player.stop();
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
			song.removeSteps(1);
			player.loadSong(song);
			redrawChannels();
		}
	};

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
			Log.e("derp", e.getMessage());
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
	
	
	public void createAndShowSaveSongDialog() {
		
		SaveSongDialog saveDialog = new SaveSongDialog(parentActivity, song);

		saveDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
				sqlWriter.RenderSong(song);
			}
		});
		saveDialog.show();
	}
	
	public void createAndShowLoadSongDialog() {
		LoadSongDialog loadDialog = new LoadSongDialog(parentActivity.getBaseContext());
		loadDialog.setPlayer(player);
		loadDialog.show();
	}
}
