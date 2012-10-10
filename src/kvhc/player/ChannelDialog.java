package kvhc.player;

import kvhc.adrumdrum.R;
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


public class ChannelDialog extends Dialog{

	private Channel channel;
	private Button back;
	private TextView tv1; // DEBUG
	
	
	public ChannelDialog(Activity parrentActivity, Channel channel) {
		super(parrentActivity);
		tv1 = (TextView)parrentActivity.findViewById(R.id.textView1); //DEBUG
		this.channel = channel;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_settings);
        setTitle("Channel Controlls");
        initButtons();
        initBars();
	}
	
	
	private void initButtons(){
		back = (Button)this.findViewById(R.id.buttonBack);
		back.setOnClickListener(backClick);
	}
	
	
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
		volumeBar.setProgress((int)(channel.getChannelVolume() * 200));
		volumeBar.setOnSeekBarChangeListener(volumeListener);
		
	}
	

	
	
	
	
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
    
	OnSeekBarChangeListener volumeListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			
			float volume =  progress * 0.005F;
				
			channel.SetVolume(volume);
		}

		public void onStartTrackingTouch(SeekBar arg0) {
		}

		public void onStopTrackingTouch(SeekBar arg0) {
		}
    };
	
	
	
	private View.OnClickListener backClick = new View.OnClickListener(){
		public void onClick(View v) {
			dismiss();
		}
	};	
	
	
}
