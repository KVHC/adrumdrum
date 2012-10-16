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

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Sound;
import kvhc.util.SoundFetcher;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A dialog about channel settings
 */
public class ChannelDialog extends Dialog{

	private Channel channel;
	private TextView tv1;
	private int id;
	private GUIController guic;
	private Button solo;
	
	/**
	 * The Constructor
	 * @param parrentActivity The activity that started this dialog
	 * @param channel The channel to change the settings for
	 */
	public ChannelDialog(Activity parrentActivity, Channel channel,	int id, GUIController guic) {
		super(parrentActivity);
		tv1 = (TextView)parrentActivity.findViewById(R.id.textView1); //DEBUG
		this.id = id;
		this.guic = guic;
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
        initSpinners();
	}
	
	/**
	 * Init the buttons in this dialog
	 */
	private void initButtons(){
		Button back = (Button)this.findViewById(R.id.buttonBack);
		back.setOnClickListener(backClick);

		Button clearSteps = (Button)this.findViewById(R.id.clear_steps_button);
		clearSteps.setOnClickListener(clearStepsClick);
		
		Button remove = (Button)this.findViewById(R.id.buttonRemove);
		remove.setOnClickListener(removeClick);
		
		solo = (Button)this.findViewById(R.id.buttonSolo);
		setSoloButtonText();
		solo.setOnClickListener(soloClick);
		
		
	}
	
	/**
	 * Init the progressbars in this dialog
	 */
	private void initBars(){
		SeekBar panningBar = (SeekBar)this.findViewById(R.id.seekbarChannelPanning);
		
		float right = channel.getRightPanning();
		float left = channel.getLeftPanning();	
		
		if (right > left){
			panningBar.setProgress(200 - (int)(left * 100));
		} else if(right < left) {
			panningBar.setProgress((int)(right * 100));
		} else {
			panningBar.setProgress(100);
		}
		
		panningBar.setOnSeekBarChangeListener(panningListener);		
		
		SeekBar volumeBar = (SeekBar)this.findViewById(R.id.seekbarChannelVolume);
		volumeBar.setProgress((int)(channel.getChannelVolume() * 100));
		volumeBar.setOnSeekBarChangeListener(volumeListener);
		
	}
	
	/**
	 * Init the spinners in this dialog
	 */
	private void initSpinners(){
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
									(this.getContext(), android.R.layout.simple_spinner_dropdown_item, 
											SoundFetcher.getNameInOrder());
		Spinner soundSpinner = (Spinner) this.findViewById(R.id.spinner_sample);
		soundSpinner.setAdapter(arrayAdapter);
		soundSpinner.setSelection(channel.getSound().getId() -1 );
		soundSpinner.setOnItemSelectedListener(sampleSpinnerListener);
		
		
		
	}
	
	/**
	 * Set the text on the soloButton 
	 */
	private void setSoloButtonText(){
		if (guic.getSoloChannel() == id){
			solo.setText("End Solo");
		} else {
			solo.setText("Start Solo");
		}
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
			
			channel.setPanning(right, left);
			tv1.setText("Panned " + channel.getLeftPanning() + "L " + channel.getRightPanning() + "R");
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
				
			channel.setVolume(volume);
			
			guic.invalidateChannelGUI();
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
	
	/**
	 * Listener to the clearSteps button
	 */
	private View.OnClickListener clearStepsClick = new View.OnClickListener(){
		public void onClick(View v) {
			channel.clearAllSteps();
			guic.redrawChannels();
			dismiss();
		}
	};
	
	/**
	 * Listener to the removeChannel button
	 */
	private View.OnClickListener removeClick = new View.OnClickListener(){
		public void onClick(View v) {
			guic.removeChannel(id);
			dismiss();
		}
	};

	
	/**
	 * Listener to the soloChannel button
	 */
	private View.OnClickListener soloClick = new View.OnClickListener(){
		public void onClick(View v) {
			guic.toggleSolo(id);
			setSoloButtonText();
			guic.invalidateChannelGUI();
		}
	};
	
	
	/**
	 * Listener that sets the sound of the channel to the one selected in the spinner
	 */
	private OnItemSelectedListener sampleSpinnerListener= new OnItemSelectedListener() {
	    @Override
	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	    	
	    	String name = parentView.getItemAtPosition(position).toString();
	        
	    	Sound s = SoundFetcher.GetSoundFromString(name);
	        
	        channel.setSound(s);
	        guic.uppdateButtons();
	        
	        tv1.setText(name +" and " +  s.getName()); //DEBUG //TODO
	        
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> parentView) {
	        // Nothing to do here
	    }


	};
	
	
}
