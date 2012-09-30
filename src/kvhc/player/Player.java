package kvhc.player;

import java.util.ArrayList;

import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.adrumdrum.R.raw;
import kvhc.util.AndroidTimer;
import kvhc.util.ISongRenderer;
import kvhc.util.SoundPoolRenderer;
import android.content.Context;

public class Player {
	
	// Channel Renderer ;)
	ISongRenderer mChannelRender;
	
	// Timing
	Runnable r;
	AndroidTimer mTimer;
	private long waitTime; // Milliseconds
	
	
	// Sounds
	Song song = null;
	float rightSound;
	float leftSound;
	
	// State
	private int m_currentStep;
	boolean m_isPlaying;
	
	// Save a reference to MainActivity, 
	// used to tell the MA which step is active
	//MainActivity act;
	
	/**
	 * Wow
	 * @param context
	 */
	public Player(Context context) {
		
		mChannelRender = new SoundPoolRenderer(context);
		
		//Saves the reference to MA
		//this.act = act;
		
		waitTime = 500;
		
		m_currentStep = 0;
		m_isPlaying = false;
		rightSound = (float) 1.0;
		leftSound = (float) 1.0;
        
        r = new Runnable(){
    		public void run() {
    			NextStep();
    		}
    	};
    	mTimer = new AndroidTimer(r,waitTime);
	}
	
	public void LoadSong(Song newSong) {
		song = newSong;
		mChannelRender.LoadSounds(song.GetSounds());
	}
	
	private void NextStep() {
		if(m_isPlaying && song != null) {

			//act.setRadioButtonToActiveStep(m_currentStep);
			
			mChannelRender.RenderSongAtStep(song, m_currentStep);
			
			m_currentStep++;

			if(m_currentStep >= song.GetNumberOfSteps()) {
				m_currentStep = 0;
			}
			
			mTimer.setTime(waitTime);
		}
	}
	
	public int GetActiveStep() {
		return m_currentStep; 
	}
	
	public void SetWaitTimeByBPM(int bpm) {
		
		if(bpm <= 0){
			// S�kert dumt
			Stop();
			return;
		}
		
		// Kan man g�ra s�? :S
		// 1 / (bpm / 60s) f�r att f� fram tid mellan beats/step (4/4 iaf?)
		waitTime = (long)((60.0 / bpm)*1000);
	}
	
	public void setBPMInRange(int p){
		SetWaitTimeByBPM(60 + p*6);
	}
	
	public void Play() {
		m_isPlaying = true;
		mTimer.start();
	}
	
	public void Stop() {
		m_isPlaying = false;
		m_currentStep = 0;
		mTimer.stop();
	}
	
	public boolean IsPlaying() {
		return m_isPlaying;
	}
}
