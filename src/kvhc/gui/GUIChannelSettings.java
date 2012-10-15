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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Sound;

public class GUIChannelSettings extends Activity {

	private Channel mChannel;
	
	private SeekBar mSeekbarVolume;
	private SeekBar mSeekbarPanning;
	private Spinner mSampleSinner;
	private Button mButton;
	
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
				mChannel.setSound(GUIChannelSettings.GetSoundFromString(String.valueOf(mSampleSinner.getSelectedItem())));
			}
		});
		
	}
	
	public static Sound GetSoundFromString(String s) {
		
		//String name = String.valueOf(s.getSelectedItem());
		String name = s;
		if("Bassdrum".equals(name)) {
			return new Sound(1, R.raw.jazzfunkkitbd_01, name);
		}
		
		if("Bell Ride Cymbal".equals(name)) {
			return new Sound(2, R.raw.jazzfunkkitbellridecym_01, name);
		} 
		
		if("Crash Cymbal 01".equals(name)) {
			return new Sound(3, R.raw.jazzfunkkitcrashcym_01, name);
		}
		
		if("Crash Cymbal 02".equals(name)) {
			return new Sound(4, R.raw.jazzfunkkitcrashcym_02, name);
		}
		
        if("Hihat Closed".equals(name)) {
        	return new Sound(5, R.raw.jazzfunkkitclosedhh_01, name);
        }
        
        if("Hihat Open".equals(name)) {
        	return new Sound(6, R.raw.jazzfunkkitopenhh_01, name);
        }
        
		if("Ride Cymbal".equals(name)) {
			return new Sound(7, R.raw.jazzfunkkitridecym_01, name);
		}
		
		if("Snare 01".equals(name)) {
			return new Sound(8, R.raw.jazzfunkkitsn_01, name);
		}
		
		if("Snare 02".equals(name)) {
			return new Sound(9, R.raw.jazzfunkkitsn_02, name);
		}
		
		if("Snare 03".equals(name)) {
			return new Sound(10, R.raw.jazzfunkkitsn_03, name);
		}
		
		if("Splash Cymbal 01".equals(name)) {
			return new Sound(11, R.raw.jazzfunkkitsplashcym_01, name);
		}
		
		if("Splash Cymbal 02".equals(name)) {
			return new Sound(12, R.raw.jazzfunkkitsplashcym_02, name);
		}
		
		if("Tomtom 01".equals(name)) {
			return new Sound(13, R.raw.jazzfunkkittom_01, name);
		}
		
		if("Tomtom 02".equals(name)) {
			return new Sound(14, R.raw.jazzfunkkittom_02, name);
		}
		
		if("Tomtom 03".equals(name)) {
			return new Sound(15, R.raw.jazzfunkkittom_03, name);
		}
		
		return null;
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
