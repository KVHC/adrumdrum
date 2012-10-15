package kvhc.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import kvhc.util.AssetManagerModel;
import kvhc.util.ISongLoader;
import kvhc.util.ISongRenderer;
import kvhc.util.db.SQLRenderer;
import kvhc.util.db.SQLSongLoader;
import kvhc.util.db.SoundDataSource;

/**
 * Master class of the GUI
 *
 */
public class GUIController {

	private Player player;
	private Song song;
	
	private TextView tv1;
	
	private Activity parentActivity;
	private AssetManagerModel<Sound> mSoundManager = AssetManagerModel.getSoundManager();
	
	ISongRenderer sqlWriter;
	ISongLoader sqlLoader;
	
	SoundDataSource mDBsoundHelper; 
	/**
	 * Constructor
	 * @param player the sound controller
	 * @param ma the main activity
	 */
	public GUIController(Activity activity) {
		parentActivity = activity;
		
		this.player = new Player(parentActivity.getBaseContext());
		
		player.addObserver(new GUIUpdateObserver(parentActivity));
		
		sqlWriter = new SQLRenderer(parentActivity);
		sqlLoader = new SQLSongLoader(parentActivity);
		
		mDBsoundHelper = new SoundDataSource(parentActivity);
		
		init();
	}
	
	/**
	 * Init all the init functions
	 */
	private void init(){
		initSong();
		initText();
		initButtons();
		initBars();
		initChannels();
	}
	
	
	/**
	 * Init sample song 
	 */
	private void initSong() {
		// Okay, make a song
		
		// Adding sounds to the sound manager because we might now have them or something		
		Sound s = new Sound(R.raw.jazzfunkkitbd_01, "Bassdrum");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("BassDrum", s);
		
		s = new Sound(R.raw.jazzfunkkitbellridecym_01, "Ride");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Ride", s);
		
		s = new Sound(R.raw.jazzfunkkitclosedhh_01, "Closed hihat");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Closed hihat", s);
		
		s = new Sound(R.raw.jazzfunkkitcrashcym_01, "Crash 01");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Crash 01", s);
		
		s = new Sound(R.raw.jazzfunkkitcrashcym_02, "Crash 02");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Crash 02", s);
		
		s = new Sound(R.raw.jazzfunkkitsn_01, "Snare 01");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Snare 01", s);
		
		s = new Sound(R.raw.jazzfunkkitsn_02, "Snare 02");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Snare 02", s);
		
		s = new Sound(R.raw.jazzfunkkitsn_03, "Snare 03");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Snare 03", s);
		
		s = new Sound(R.raw.jazzfunkkitsplashcym_01, "Splash 01");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Splash 01", s);
		
		s = new Sound(R.raw.jazzfunkkitsplashcym_02, "Splash 02");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Splash 02", s);
		
		s = new Sound(R.raw.jazzfunkkittom_01, "Tomtom 01");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Tomtom 01", s);
		
		s = new Sound(R.raw.jazzfunkkittom_02, "Tomtom 02");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Tomtom 02", s);
		
		s = new Sound(R.raw.jazzfunkkittom_03, "Tomtom 03");
		mDBsoundHelper.save(s);
		mSoundManager.setValue("Tomtom 03", s);
		
		
		// Creates list of sounds for channel.
		ArrayList<Sound> sounds = new ArrayList<Sound>(4);
		
		sounds.add(mSoundManager.getValue("Bassdrum"));
		sounds.add(mSoundManager.getValue("Hihat closed"));
		sounds.add(mSoundManager.getValue("Snare"));
		sounds.add(mSoundManager.getValue("Ride"));
		
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
	 * Init the default channel rows
	 */
	private void initChannels() {
		redrawChannels();
	}
	
	
	/**
	 * Inits a TextView. For testing purposes only atm
	 */
    private void initText() {
    	tv1 = (TextView)parentActivity.findViewById(R.id.textView1);
		
	}

    /**
     * Inits the necessary GUI-buttons (Play/Stop, Add Channel, Remove Step etc)
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
	 * Inits the BPM-bar
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
	 * adds and links a ChannelButtonGUI to it.
	 * 
	 * @param c the channel to be added
	 */
	private void addChannel(Channel c) {
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		TableRow row = new TableRow(channelContainer.getContext());
		
		// Name label

		ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,song.getNumberOfChannels()-1,this);
		name.setText(c.getSound().getName());
		row.addView(name);
		
		for(int x = 0; x < song.getNumberOfSteps(); x++) {
			GUIStepButton box = new GUIStepButton(row.getContext(), song.getNumberOfChannels()-1, x);
			box.setOnClickListener(stepClickListener);
			box.setOnLongClickListener(new LongClickStepListener(c.getStepAt(x), parentActivity));
			row.addView(box);
		}
		channelContainer.addView(row);
	}
	
   
    /**
     * An array of Strings containing the names of the different sounds.
     * This should be done in a different way (?)
     */
    private ArrayList<String> sampleArray = null;
    
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
    private void redrawChannels() {
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		channelContainer.removeAllViewsInLayout();
		
		int y = 0;
		for(Channel c: song.getChannels()) {
			
			TableRow row = new TableRow(channelContainer.getContext());
			
			// Name label/ mute button
			ChannelButtonGUI name = new ChannelButtonGUI(parentActivity,c,y,this);
			name.setText(c.getSound().getName());
			//name.setOnLongClickListener(channelSettingsListener);
			row.addView(name);
			
			// All the steps
			for(int x = 0; x < song.getNumberOfSteps(); x++) {
				
				GUIStepButton box = new GUIStepButton(row.getContext(), y, x, c.isStepActive(x));	// Construction
				box.setOnClickListener(stepClickListener);						// Listener
				box.setOnLongClickListener(new LongClickStepListener(c.getStepAt(x), parentActivity));
				row.addView(box);
				
			}
			channelContainer.addView(row);
			y++;
		}


		channelContainer.setVisibility(View.VISIBLE);
		channelContainer.invalidate();
	}
    
    /**
     * Stops Playback and clears all steps
     * Redraws all Channels
     */
    public void clearAllSteps() {
    	player.stop();
		song.clearAllSteps();
		redrawChannels();
    }
	
    public void removeChannel(int channel) {
    	if (song.removeChannel(channel)) {
    		player.loadSong(song);
    		redrawChannels();
    	}
    }
    
	/**
	 * Call onStop (might need some special handling here?)
	 */
	public void onStop() {
    	player.stop();
    }
    
	/**
	 * Call onDestroy (might need some special handling here?)
	 */
    public void onDestroy() {
    	player.stop();
    }
    
    
    //
    // Listeners:
    //
    
	/**
	 * Listener to the step buttons
	 */
	private OnClickListener stepClickListener = new OnClickListener() {

        public void onClick(View v) {
        	
        	// Retrieves the checkbox and inverts its value
            GUIStepButton box = (GUIStepButton) v;
            box.reverse();
            
            // Updates the corresponding channel
            box.SetActive(song.getChannel(box.GetChannel()).toggleStep(box.GetStep()));
        }
    };
	
    
	/**
	 * Listener to the Play/Stop-button
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
			Button b = (Button) v;
			
			// Spinner for sound sample selection
			createSampleList();
			final Spinner input2 = new Spinner(parentActivity);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parentActivity, android.R.layout.simple_spinner_dropdown_item, sampleArray);
			input2.setAdapter(spinnerArrayAdapter);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
			builder.setView(input2);	
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			        
			    	String name = String.valueOf(input2.getSelectedItem());
			        Sound s = mSoundManager.getValue(name);
			        
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
	 * Listener to the add-new-step-button. Redraws all the channels
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
	 * Listener to the remove-step-button. Redraws all the channels
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
	 * Show a Licenses Dialog!
	 */
	public void createAndShowLicensesDialog(){
		AlertDialog dialog = new AlertDialog.Builder(parentActivity).create();
		dialog.setTitle(R.string.licenses);
		dialog.setMessage("LICENSES HERE");
		dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	
	public void createAndShowSaveSongDialog() {
		final SaveSongDialog saveDialog = new SaveSongDialog(parentActivity, song);
		
		saveDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
				sqlWriter.RenderSong(song);
			}
		});
		saveDialog.show();
	}
	
	public void createAndShowLoadSongDialog() {
		final LoadSongDialog loadDialog = new LoadSongDialog(parentActivity);
		
		loadDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
				player.stop();
				song = loadDialog.getSong();
				player.loadSong(song);
				
			}
		});
		loadDialog.show();
	}
}
