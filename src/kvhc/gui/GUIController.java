package kvhc.gui;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.player.Player;
import kvhc.player.Song;
import kvhc.player.Sound;

public class GUIController {

	
	private Player player;
	private Song song;
	
	private MainActivity ma;
	private TextView tv1;
	
	public GUIController(Player player, MainActivity ma){
		this.player = player;
		this.ma = ma;
		
		song = new Song(4);
		
		ArrayList<Sound> sounds = new ArrayList<Sound>(4);
		sounds.add(new Sound(1, R.raw.jazzfunkkitbd_01, "Bassdrum"));
		sounds.add(new Sound(2, R.raw.jazzfunkkitclosedhh_01, "Hihat closed"));
		sounds.add(new Sound(3, R.raw.jazzfunkkitsn_01, "Snare"));
		sounds.add(new Sound(4, R.raw.jazzfunkkitridecym_01, "Ride"));
		
		// Nu initierar spelaren en sån här... fuck.
		for(int i = 0; i < song.GetNumberOfChannels(); i++)  {
			song.GetChannel(i).SetSound(sounds.get(i));
		}
		
		this.player.LoadSong(song);
		
		
		init();
	}
	
	private void init(){
		initText();
		initButtons();
		initBars();
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
				tv1.setText("It's not the song that is supposed to be panned... is it?");
				//if(progress >= 50){					
					//song.panning(1.0, 1.0 - ((progress - 50) * 0.02));
					//tv1.setText("Panned: R=1.0 L=" + (1.0 -(progress - 50) * 0.02));
					//tv1.setText("It's not the song that is supposed to be panned... is it?");
				//}
				//else if (progress < 50){
					//song.panning(1.0 - (50 - progress) * 0.02, 1.0);
					//tv1.setText("Panned: R=" +((1.0 - (50 - progress) * 0.02)) + "L=1.0");
					
				//}
				 	
				
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
    
	
}
