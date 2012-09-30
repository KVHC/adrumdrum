package kvhc.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Player;
import kvhc.player.Song;
import kvhc.player.Sound;

public class GUIController {

	
	private Player player;
	private Song song;
	
	private TextView tv1;
	private RadioGroup rg;
	
	private Activity parentActivity; // Lite onödigt egentligt...
	
	/**
	 * Constructor
	 * @param player the sound controller
	 * @param ma the main activity
	 */
	//public GUIController(Player player, MainActivity ma){
	public GUIController(Activity activity) {
		parentActivity = activity;
		
		this.player = new Player(parentActivity.getBaseContext());
		
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
		initShowActiveSteps();
	}
	
	/**
	 * Init sample song 
	 */
	private void initSong() {
		// Okay, make a song
		ArrayList<Sound> sounds = new ArrayList<Sound>(4);
		sounds.add(new Sound(1, R.raw.jazzfunkkitbd_01, "Bassdrum"));
		sounds.add(new Sound(2, R.raw.jazzfunkkitclosedhh_01, "Hihat closed"));
		sounds.add(new Sound(3, R.raw.jazzfunkkitsn_01, "Snare"));
		sounds.add(new Sound(4, R.raw.jazzfunkkitridecym_01, "Ride"));
		
		ArrayList<Channel> channels = new ArrayList<Channel>(sounds.size());
		
		// Har ingen sparad låt än, så vi får göra såhär.
		for(int i = 0; i < sounds.size(); i++)  {
			// Adds a new channel
			channels.add(new Channel(sounds.get(i), 8));
		}
		
		song = new Song(channels);
		
		/*song.GetChannel(0).SetStep(0, true);
		song.GetChannel(0).SetStep(4, true);
		
		song.GetChannel(1).SetStep(0, true);
		song.GetChannel(1).SetStep(2, true);
		song.GetChannel(1).SetStep(4, true);
		song.GetChannel(1).SetStep(6, true);*/
		
		player.LoadSong(song);
	}
	
	private void initShowActiveSteps() {
		
		TableLayout activeStepsContainer = (TableLayout)parentActivity.findViewById(R.id.ActiveStepsContainer);
		rg = new RadioGroup(parentActivity.getBaseContext());
		
		for(int i = 0; i < song.GetNumberOfSteps(); i++) {
			RadioButton btn = new RadioButton(rg.getContext());
			rg.addView(btn);
		}
		
		rg.setOrientation(LinearLayout.HORIZONTAL);
		rg.setEnabled(true);
		activeStepsContainer.addView(rg);
		activeStepsContainer.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Init the default channel rows
	 */
	private void initChannels() {
		
		RedrawChannels();
		
		/*TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		for(int y = 0; y < song.GetNumberOfChannels(); y++) {
			TableRow row = new TableRow(channelContainer.getContext());
			for(int x = 0; x < song.GetNumberOfSteps(); x++) {
				GUIStepButton box = new GUIStepButton(row.getContext(), y, x);
				box.setOnClickListener(stepClickListener);
				row.addView(box);
			}
			channelContainer.addView(row);
		}

		channelContainer.setVisibility(View.VISIBLE);*/
	}
	
	private void addChannel(Channel c) {
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		TableRow row = new TableRow(channelContainer.getContext());
		
		// Name label
		TextView name = new TextView(parentActivity);
		name.setText(c.GetSound().GetName());
		row.addView(name);
		
		for(int x = 0; x < song.GetNumberOfSteps(); x++) {
			GUIStepButton box = new GUIStepButton(row.getContext(), song.GetNumberOfChannels()-1, x);
			box.setOnClickListener(stepClickListener);
			row.addView(box);
		}
		channelContainer.addView(row);
	}
	
    private void initText() {
    	tv1 = (TextView)parentActivity.findViewById(R.id.textView1);
		
	}

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
	
	
	private OnClickListener stepClickListener = new OnClickListener() {

        public void onClick(View v) {
        	
        	// Retrieves the checkbox and inverts its value
            GUIStepButton box = (GUIStepButton) v;
            box.reverse();
            
            // Updates the corresponding channel
            song.GetChannel(box.GetChannel()).SetStep(box.GetStep(), box.isChecked());
        }
    };
	
    private OnClickListener btnListener = new OnClickListener()
    {
		public void onClick(View v) {
			if(player.IsPlaying()) {
    			player.Stop();
    			tv1.setText("Not playing anymore");
    			
    		} else {
    			player.Play();
    			tv1.setText("Playing");
    		}
		}
    };
    
    private ArrayList<String> sampleArray = null;
    
    private void CreateSampleList() {
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
    
    private OnClickListener addChannelListener = new OnClickListener() {
		
		public void onClick(View v) {
			Button b = (Button) v;
			
			Log.e("LOL", "OKEJ LÄGG TILL DÅ");
			
			// Spinner for sound sample selection
			CreateSampleList();
			final Spinner input2 = new Spinner(parentActivity);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parentActivity, android.R.layout.simple_spinner_dropdown_item, sampleArray);
			input2.setAdapter(spinnerArrayAdapter);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
			builder.setView(input2);	
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			        
			    	String name = String.valueOf(input2.getSelectedItem());
			        Sound s = GUIChannelSettings.GetSoundFromString(name);
			        
			        Channel c = new Channel(s, song.GetNumberOfSteps());
			        song.AddChannel(c);
			        addChannel(c);
			        
			        player.LoadSong(song);
			        
			        dialog.dismiss();
			    }
			});
			
			builder.create();
			builder.show();
			
		}
	};
    
	
	private OnClickListener addStepListener = new OnClickListener() {
		
		public void onClick(View v) {
			// Add one step to the song and gui
			
			song.AddSteps(1);
			player.LoadSong(song);
			RedrawChannels();
		}
	};
	
	private OnClickListener removeStepListener = new OnClickListener() {
		
		public void onClick(View v) {
			song.RemoveSteps(1);
			player.LoadSong(song);
			RedrawChannels();
		}
	};
	
	private void RedrawChannels() {
		// Now it redraws all the channels and steps, this is not really neccessary 
		// this is VERY optimizable.
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		channelContainer.removeAllViewsInLayout();
		
		int y = 0;
		for(Channel c: song.GetChannels()) {
			
			TableRow row = new TableRow(channelContainer.getContext());
			
			// Name label
			TextView name = new TextView(parentActivity);
			name.setText(c.GetSound().GetName());
			row.addView(name);
			
			// All the steps
			for(int x = 0; x < song.GetNumberOfSteps(); x++) {
				
				GUIStepButton box = new GUIStepButton(row.getContext(), y, x, c.IsStepActive(x));	// Construction
				box.setOnClickListener(stepClickListener);						// Listener
				row.addView(box);
				
			}
			channelContainer.addView(row);
			y++;
		}


		channelContainer.setVisibility(View.VISIBLE);
		channelContainer.invalidate();
	}
	
	
	public void setActiveStep(int stepid) {
		//var nån bugg i rg.clearCheck(), nån får gärna fixa detta
		//rg.check(stepid);
	}
}
