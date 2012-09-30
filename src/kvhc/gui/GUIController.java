package kvhc.gui;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
	
	private MainActivity ma;
	private TextView tv1;
	private RadioGroup rg;
	
	/**
	 * Constructor
	 * @param player the sound controller
	 * @param ma the main activity
	 */
	public GUIController(Player player, MainActivity ma){
		this.player = player;
		this.ma = ma;
		
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
			channels.add(new Channel(sounds.get(i), 16));
		}
		
		song = new Song(channels);
		
		player.LoadSong(song);
	}
	
	private void initShowActiveSteps() {
		
		TableLayout activeStepsContainer = (TableLayout)ma.getFromR(R.id.ActiveStepsContainer);
		rg = new RadioGroup(ma);
		
		for(int i=0;i<8;i++) {
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
		
		TableLayout channelContainer = (TableLayout)ma.getFromR(R.id.ChannelContainer);
		
		for(int y=0;y<4;y++) {
			TableRow row = new TableRow(channelContainer.getContext());
			for(int x=0;x<8;x++) {
				GUIStepButton box = new GUIStepButton(row.getContext(), y, x);
				box.setOnClickListener(stepClickListener);
				row.addView(box);
			}
			channelContainer.addView(row);
		}

		channelContainer.setVisibility(View.VISIBLE);
	}
	
    private void initText() {
    	tv1 = (TextView)ma.getFromR(R.id.textView1);
		
	}

	private void initButtons() {
    	   Button btn1 = (Button)ma.getFromR(R.id.button1);
           btn1.setOnClickListener(btnListener);
		
	}

	private void initBars(){
		SeekBar bpmBar = (SeekBar)ma.getFromR(R.id.bpmbar);
    	SeekBar panningBar = (SeekBar)ma.getFromR(R.id.panningbar);
    	TextView bpmtext = (TextView) ma.getFromR(R.id.bpmtext);
    	
    	panningBar.setProgress(50);
    	
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
    	
    	panningBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

			//@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				/*if(progress >= 50){					
					song.panning(1.0, 1.0 - ((progress - 50) * 0.02));
					tv1.setText("Panned: R=1.0 L=" + (1.0 -(progress - 50) * 0.02));
				}
				else if (progress < 50){
					player.panning(1.0 - (50 - progress) * 0.02, 1.0);
					tv1.setText("Panned: R=" +((1.0 - (50 - progress) * 0.02)) + "L=1.0");
				}*/
				
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
        });
    }
	
	
	private OnClickListener stepClickListener = new OnClickListener() {

        public void onClick(View v) {
            // Så jävla snyggt. 
            GUIStepButton box = (GUIStepButton) v;
            box.reverse();
            
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
    
	public void setActiveStep(int stepid) {
		//var nån bugg i rg.clearCheck(), nån får gärna fixa detta
		
	}
    
	
}
