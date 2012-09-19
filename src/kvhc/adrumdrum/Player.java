package kvhc.adrumdrum;


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
	
	
	// State
	private int m_currentStep;
	private int m_numSteps;
	boolean m_isPlaying;
	
	
	/**
	 * Wow
	 * @param context
	 */
	public Player(Context context) {
		
		waitTime = 500;
		
		m_numChannels = 4;
		m_Channels = new Channel[m_numChannels];
		m_currentStep = 0;
		m_isPlaying = false;
		
		
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
				if(m_Channels[i].PlayStep(m_currentStep)) {
					mSoundManager.playSound(m_Channels[i].GetSound());
				}
			}
			
			mTimer.setTime(waitTime);
		}
	}
	
	public void SetWaitTimeByBPM(int bpm) {
		
		if(bpm <= 0){
			// Säkert dumt
			Stop();
			return;
		}
		
		// Kan man göra så? :S
		// 1 / (bpm / 60s) för att få fram tid mellan beats/step (4/4 iaf?)
		waitTime = (long)(60.0 / bpm)*1000;
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
}
