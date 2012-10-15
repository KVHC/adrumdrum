package kvhc.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Sound;
import kvhc.util.AssetManagerModel;

public class GUIChannelSettings extends Activity {

	private Channel mChannel;
	
	private SeekBar mSeekbarVolume;
	private SeekBar mSeekbarPanning;
	private Spinner mSampleSinner;
	private Button mButton;
	
	private AssetManagerModel<Sound> soundManager = AssetManagerModel.getSoundManager();
	
	private MainActivity ma;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_settings);
        
        //initGUI();
        
    }
	
    public GUIChannelSettings() {
    	//getChannel();
    }
	
	public GUIChannelSettings(MainActivity mainActivity, Channel c) {
		//mChannel = c;
		//ma = mainActivity;
		
		//initGUI();
	}

	
	private void initGUI() {
		
		mSeekbarVolume = (SeekBar) ma.findViewById(R.id.seekbarChannelVolume);
		mSeekbarVolume.setOnSeekBarChangeListener(VolumeListener);
		
		mSeekbarPanning = (SeekBar) ma.findViewById(R.id.seekbarChannelPanning);
		mSeekbarPanning.setOnSeekBarChangeListener(PanningListener);
		
		mSampleSinner = (Spinner) ma.findViewById(R.id.spinner_sample);
		
		mButton = (Button) ma.findViewById(R.id.buttonBack);
		
		mButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				mChannel.setSound(getSoundFromString(String.valueOf(mSampleSinner.getSelectedItem())));
			}
		});
		
	}
	
	public Sound getSoundFromString(String s) {
		
		// Load the sound from the soundManager
		String name = s;
		return soundManager.getValue(name);
		
		// Are sounds even loaded? :|
	}
	
	/**
	 * getChannel ska plocka ut en Channel som skickas med Intent när man
	 * skapar en ny aktivitet (denna). FUNKAR EJ.
	 */
	/*private void getChannel() {
		mChannel = (Channel) getIntent().getSerializableExtra("Channel");
	}*/
	
	private OnSeekBarChangeListener VolumeListener = new OnSeekBarChangeListener() {
		
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if(fromUser) {
				mChannel.setVolume(progress / 100.0f);
			}
		}
	};

	private OnSeekBarChangeListener PanningListener = new OnSeekBarChangeListener() {
		
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if(progress >= 100){					
				mChannel.setPanning(1.0f, 1.0f - ((progress - 100) * 0.01f));
			}
			else if (progress < 100){
				mChannel.setPanning(1.0f - (100 - progress) * 0.01f, 1.0f);
			}
		}
	};

	
	private OnClickListener LoadSample = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Load sample to channel (how? open a view-list?)
		}
	};
}
