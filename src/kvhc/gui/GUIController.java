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
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		for(int y = 0; y < song.GetNumberOfChannels(); y++) {
			TableRow row = new TableRow(channelContainer.getContext());
			for(int x = 0; x < song.GetNumberOfSteps(); x++) {
				GUIStepButton box = new GUIStepButton(row.getContext(), y, x);
				box.setOnClickListener(stepClickListener);
				row.addView(box);
			}
			channelContainer.addView(row);
		}

		channelContainer.setVisibility(View.VISIBLE);
	}
	
	private void addChannel(Channel c) {
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		TableRow row = new TableRow(channelContainer.getContext());
		for(int x = 0; x < song.GetNumberOfSteps(); x++) {
			GUIStepButton box = new GUIStepButton(row.getContext(), song.GetNumberOfChannels()-1, x);
			box.setOnClickListener(stepClickListener);
			row.addView(box);
		}
		channelContainer.addView(row);
		//channelContainer.invalidate();
	}
	
    private void initText() {
    	tv1 = (TextView)parentActivity.findViewById(R.id.textView1);
		
	}

	private void initButtons() {
    	   Button btn1 = (Button)parentActivity.findViewById(R.id.button1);
           btn1.setOnClickListener(btnListener);
           
           Button addChnl = (Button)parentActivity.findViewById(R.id.buttonAddChannel);
           addChnl.setOnClickListener(addChannelListener);
	}

	private void initBars(){
		SeekBar bpmBar = (SeekBar)parentActivity.findViewById(R.id.bpmbar);
    	//SeekBar panningBar = (SeekBar)parentActivity.findViewById(R.id.panningbar);
    	TextView bpmtext = (TextView)parentActivity.findViewById(R.id.bpmtext);
    	
    	//panningBar.setProgress(50);
    	
    	bpmBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			//@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				player.setBPMInRange(progress);
				tv1.setText("BPM is: " + (60 + progress*6));
				
			}

			//@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			//@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				//TODO
				
			}
        });
    	
    	/*panningBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			//@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				/*if(progress >= 50){					
					song.panning(1.0, 1.0 - ((progress - 50) * 0.02));
					tv1.setText("Panned: R=1.0 L=" + (1.0 -(progress - 50) * 0.02));
				}
				else if (progress < 50){
					player.panning(1.0 - (50 - progress) * 0.02, 1.0);
					tv1.setText("Panned: R=" +((1.0 - (50 - progress) * 0.02)) + "L=1.0");
				}
				
				// Ska inte ske här längre. 

				
			}

			//@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			//@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				//TODO
				
			}
        });*/
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
			
			final EditText input = new EditText(parentActivity); // This could also come from an xml resource, in which case you would use findViewById() to access the input
			
			
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
    
	public void setActiveStep(int stepid) {
		//var nån bugg i rg.clearCheck(), nån får gärna fixa detta
		//rg.check(stepid);
	}
}
