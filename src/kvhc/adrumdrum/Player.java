package kvhc.adrumdrum;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

public class Player {
	
	// SoundPool
	private SoundManager mSoundManager;
	
	// Timing
	private Timer m_Timer;
	private long waitTime; // Milliseconds
	private TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			NextStep();
	}};
	
	
	// Sounds
	private int m_numChannels;
	private ArrayList<Channel> m_Channels;
	
	
	// State
	private int m_currentStep;
	private int m_numSteps;
	boolean m_isPlaying;
	
	
	
	public Player(Context context) {
		waitTime = 500;
		
		m_numChannels = 4;
		m_Channels = new ArrayList<Channel>(m_numChannels);
		m_currentStep = 0;
		m_isPlaying = false;
		
		mSoundManager = new SoundManager();
        mSoundManager.initSounds(context);
        mSoundManager.addSound(1, R.raw.ljud1);
        mSoundManager.addSound(2, R.raw.bd);
        mSoundManager.addSound(3, R.raw.hat);
        mSoundManager.addSound(4, R.raw.snare);
        
        for(int i = 0; i < m_numChannels; i++) {
        	m_Channels.set(i, new Channel(i+1));
        }
        
        m_Timer = new Timer();
        m_Timer.schedule(timerTask, waitTime);
	}
	
	private void NextStep() {
		if(m_isPlaying) {
			m_currentStep++;
			if(m_currentStep > m_numSteps) {
				m_currentStep = 0;
			}
			
			for(int i = 0; i < m_numChannels; i++) {
				if(m_Channels.get(i).PlayStep(m_currentStep)) {
					mSoundManager.playSound(m_Channels.get(i).GetSound());
				}
			}
		}
		
		m_Timer.schedule(timerTask, waitTime);
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
	}
	
	public void Stop() {
		m_isPlaying = false;
		m_currentStep = 0;
	}
	
	public boolean IsPlaying() {
		return m_isPlaying;
	}
	
	
	public void SetSound(int channel, int sound) {
		m_Channels.get(channel).SetSound(sound);
	}
	
	public void SetStep(int channel, int step, boolean active) {
		m_Channels.get(channel).SetStep(step, active);
	}
}
