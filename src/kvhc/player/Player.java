package kvhc.player;

import kvhc.adrumdrum.MainActivity;
import kvhc.adrumdrum.R;
import kvhc.util.AndroidTimer;
import android.content.Context;

public class Player {
	
	// SoundPool
	private SoundManager mSoundManager;
	
	// Timing
	Runnable r;
	AndroidTimer mTimer;
	private long waitTime; // Milliseconds
	
	
	// Sounds
	private int m_numChannels;
	private Channel[] m_Channels;
	float rightSound;
	float leftSound;
	
	// State
	private int m_currentStep;
	private int m_numSteps = 7;
	boolean m_isPlaying;
	
	// Save a reference to MainActivity, 
	// used to tell the MA which step is active
	MainActivity act;
	
	/**
	 * Wow
	 * @param context
	 */
	public Player(Context context, MainActivity act) {
		
		//Saves the reference to MA
		this.act = act;
		
		waitTime = 500;
		
		m_numChannels = 4;
		m_Channels = new Channel[m_numChannels];
		m_currentStep = 0;
		m_isPlaying = false;
		rightSound = (float) 1.0;
		leftSound = (float) 1.0;
		
		
		mSoundManager = new SoundManager();
        mSoundManager.initSounds(context);
        mSoundManager.addSound(1, R.raw.ljud1);
        mSoundManager.addSound(2, R.raw.bd);
        mSoundManager.addSound(3, R.raw.hat);
        mSoundManager.addSound(4, R.raw.snare);
        
        for(int i = 0; i < m_numChannels; i++) {
        	m_Channels[i] = new Channel(i+1);
        }
        
        r = new Runnable(){
    		public void run() {
    			NextStep();
    		}
    	};
    	mTimer = new AndroidTimer(r,waitTime);
	}
	
	private void NextStep() {
		if(m_isPlaying) {
			m_currentStep++;
			
			if(m_currentStep > m_numSteps) {
				m_currentStep = 0;
			}
			
			for(int i = 0; i < m_numChannels; i++) {
				if(m_Channels[i].PlayStep(m_currentStep) && !m_Channels[i].isMuted()) {
					mSoundManager.playSound(m_Channels[i].GetSound(),rightSound,leftSound,m_Channels[i].getVolume());
				}
			}
			
			act.setRadioButtonToActiveStep(m_currentStep);
			
			mTimer.setTime(waitTime);
		}
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
	
	public void panning(double r, double l){
		rightSound = (float)r;
		leftSound = (float)l;
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
	
	
	public void SetSound(int channel, int sound) {
		m_Channels[channel].SetSound(sound);
	}
	
	public void SetStep(int channel, int step, boolean active) {
		m_Channels[channel].SetStep(step, active);
	}
	
	public void muteAllChannelsExcept(int channel) {
		if (channel>m_numChannels) {
			return;
		}
		for(int i=0;i<m_numChannels;i++) {
			m_Channels[i].setMute(true);
		}
		m_Channels[channel].setMute(false);
	}
}
