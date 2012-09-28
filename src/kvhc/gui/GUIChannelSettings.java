package kvhc.gui;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.player.Channel;

public class GUIChannelSettings {

	private Channel m_Channel;
	
	private SeekBar m_SeekbarVolume;
	private SeekBar m_SeekbarPanning;
	
	private MainActivity ma;
	
	
	public GUIChannelSettings(MainActivity mainActivity, Channel c) {
		m_Channel = c;
		ma = mainActivity;
		
		initGUI();
	}
	
	private void initGUI() {
		m_SeekbarVolume = (SeekBar) ma.findViewById(R.id.seekbarChannelVolume);
		m_SeekbarVolume.setOnSeekBarChangeListener(VolumeListener);
		
		m_SeekbarPanning = (SeekBar) ma.findViewById(R.id.seekbarChannelPanning);
		m_SeekbarPanning.setOnSeekBarChangeListener(PanningListener);
	}
	
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
				m_Channel.SetVolume(progress / 100.0f);
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
				m_Channel.SetPanning(1.0f, 1.0f - ((progress - 100) * 0.01f));
			}
			else if (progress < 100){
				m_Channel.SetPanning(1.0f - (100 - progress) * 0.01f, 1.0f);
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
