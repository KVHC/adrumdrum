package kvhc.player;

import java.util.ArrayList;


/**
 * The Channel class manages a channel containing a number
 * of steps and a sound to play for each active step
 * 
 * @author kvhc
 *
 */
public class Channel {
	
	//private int m_soundId;
	private int m_numSteps;
	private ArrayList<Step> m_Steps;
	private boolean mute;
	
	private float m_volume;
	private float m_leftPan;
	private float m_rightPan;
	
	private Sound mSound;
	
	/**
	 * Constructor for an empty channel
	 * Default number of steps: 16
	 */
	public Channel() {
		this(null, 16); // 0 = inget ljud? 
	}
	
	/**
	 * Constructor for a channel with a sound
	 * Default number of steps: 16
	 * @param soundId
	 */
	public Channel(Sound sound) {
		this(sound, 16); // Ni vet
	}
	
	/**
	 * Constructor for a channel with a sound and a specific number of steps
	 * @param soundId
	 * @param steps
	 */
	public Channel(Sound sound, int steps) {
		mSound = sound;
		m_numSteps = steps;
		m_Steps = new ArrayList<Step>(m_numSteps);
		
		for(int i = 0; i < m_numSteps; i++) {
			m_Steps.add(new Step());
		}
		
		m_volume = 1.0f;
		mute = false;
	}
	
	public boolean IsStepActive(int i) {
		return m_Steps.get(i).IsActive();
	}
	
	public void SetSound(Sound sound) {
		mSound = sound;
	}
	
	public Sound GetSound() {
		return mSound;
	}
	
	public void ToggleStep(int step) {
		m_Steps.get(step).SetActive(
				!m_Steps.get(step).IsActive()
			);
	}
	
	public void SetStep(int step, boolean active) {
		m_Steps.get(step).SetActive(active);
	}
	
	public void SetStep(int step, boolean active, float velocity) {
		m_Steps.get(step).SetActive(active);
		m_Steps.get(step).SetVelolcity(velocity);
	}
	
	public float GetVolumeLeft(int step) {
		if(step >= m_Steps.size()|| step < 0) {
			return 0.0f;
		}
		
		return (m_volume * m_Steps.get(step).GetVelocity());
	}
	
	public float GetVolumeRight(int step) {
		if(step >= m_Steps.size() || step < 0) {
			return 0.0f; 
		}
		
		return (m_volume * m_Steps.get(step).GetVelocity());  
	}
	
	public float GetVolume() {
		return m_volume;
	}
	
	public void SetVolume(float volume) {
		m_volume = volume;
	}
	
	public void SetMute(boolean mute){
		this.mute = mute;
	}
	
	public boolean isMuted(){
		return mute;
	}
	
	public int GetNumberOfSteps() {
		return m_Steps.size();
	}

	public void SetPanning(float rightLevel, float leftLevel) {
		// TODO Auto-generated method stub
		m_leftPan = leftLevel;
		m_rightPan = rightLevel;
	}
	
	public float GetLeftPanning() {
		return m_leftPan;
	}
	
	public float GetRightPanning() {
		return m_rightPan;
	}

	public void ResizeBy(int resizeByAmount) {
		
		m_numSteps += resizeByAmount;
		
		if(resizeByAmount < 0) {
			for(int i = 0; i < (-resizeByAmount); i++) {
				m_Steps.remove(m_Steps.size()-1);
			}
		} else {
			for(int i = 0; i < resizeByAmount; i++) {
				m_Steps.add(new Step());
			}
		}
	}
	
	public ArrayList<Step> GetSteps() {
		return m_Steps;
	}

}
