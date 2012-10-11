package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * A dialog about channel settings
 */
public class ChannelDialog extends Dialog{

	private Channel channel;
	private Button back;
	private TextView tv1; // DEBUG
	
	/**
	 * The Constructor
	 * @param parrentActivity The activity that started this dialog
	 * @param channel The channel to change the settings for
	 */
	public ChannelDialog(Activity parrentActivity, Channel channel) {
		super(parrentActivity);
		tv1 = (TextView)parrentActivity.findViewById(R.id.textView1); //DEBUG
		this.channel = channel;
	}
	
	/**
	 * What should happen then a new dialog is created
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_settings);
        setTitle("Channel Controlls");
        initButtons();
        initBars();
	}
	
	/**
	 * Init the buttons in this dialog
	 */
	private void initButtons(){
		back = (Button)this.findViewById(R.id.buttonBack);
		back.setOnClickListener(backClick);
	}
	
	/**
	 * Init the progressbars in this dialog
	 */
	private void initBars(){
		SeekBar panningBar = (SeekBar)this.findViewById(R.id.seekbarChannelPanning);
		
		float right = channel.GetRightPanning();
		float left = channel.GetLeftPanning();	
		
		if (right > left){
			panningBar.setProgress(200 - (int)(left * 100));
		}else if(right < left){
			panningBar.setProgress((int)(right * 100));
		}else{
			panningBar.setProgress(100);
		}
		
		panningBar.setOnSeekBarChangeListener(panningListener);
		
		
		SeekBar volumeBar = (SeekBar)this.findViewById(R.id.seekbarChannelVolume);
		volumeBar.setProgress((int)(channel.getChannelVolume() * 100));
		volumeBar.setOnSeekBarChangeListener(volumeListener);
		
	}
	

	
	
	
	/**
	 * A listener that changes the panning on the channel
	 */
	OnSeekBarChangeListener panningListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			float right; 
			float left;
			
			if (progress >= 100){
				right = 1.0F;
				left = 1.0F - 0.01F * (progress - 100);   
			}else{
				right = 0.01F * progress;
				left = 1.0F;
			}
			
			channel.SetPanning(right, left);
			tv1.setText("Panned " + channel.GetLeftPanning() + "L " + channel.GetRightPanning() + "R");
		}


		public void onStartTrackingTouch(SeekBar arg0) {
		}

		public void onStopTrackingTouch(SeekBar arg0) {
		}
    };
    
    
    /**
     * A listener that change the volume of the channel
     */
	OnSeekBarChangeListener volumeListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			
			float volume =  progress * 0.01F;
				
			channel.SetVolume(volume);
		}

		public void onStartTrackingTouch(SeekBar arg0) {
		}

		public void onStopTrackingTouch(SeekBar arg0) {
		}
    };
	
	
	/**
	 * A on click listener that close this dialog
	 */
	private View.OnClickListener backClick = new View.OnClickListener(){
		public void onClick(View v) {
			dismiss();
		}
	};	
	
	
}
