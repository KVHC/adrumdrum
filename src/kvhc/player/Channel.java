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
	
	/**
	 * Returns true if the step is active
	 * @param i
	 * @return
	 */
	public boolean IsStepActive(int i) {
		if( i >= m_numSteps || i < 0) {
			return false;
		}
		return m_Steps.get(i).IsActive();
	}
	
	/**
	 * Sets all steps to inactive
	 */
	public void clearAllSteps() {
		for (Step step : m_Steps) {
			step.SetActive(false);
		}
	}
	
	/**
	 * Sets the Sound of the Channel
	 * @param sound the sound to be used
	 */
	public void SetSound(Sound sound) {
		mSound = sound;
	}
	
	/**
	 * Method that returns the sound of the step.
	 * @return the sound of the step
	 */
	public Sound GetSound() {
		return mSound;
	}
	
	/**
	 * Toggles a step between active or inactive
	 * If it's active, it is set to inactive and vice versa
	 * @param step what step
	 * @return whether it's active or not after the operation
	 */
	public boolean ToggleStep(int step) {
		m_Steps.get(step).SetActive(
				!m_Steps.get(step).IsActive()
			);
		
		return IsStepActive(step);
	}
	
	/**
	 * Sets a specific step to active or not
	 * @param step what step
	 * @param active boolean whether the step should be active or not
	 */
	public void SetStep(int step, boolean active) {
		m_Steps.get(step).SetActive(active);
	}
	
	/**
	 * Sets a specific step to active or not active and with a specific velocity ("volume")
	 * @param step what step
	 * @param active boolean whether the step should be active or not
	 * @param velocity velocity ("volume") of the step
	 */
	public void SetStep(int step, boolean active, float velocity) {
		m_Steps.get(step).SetActive(active);
		m_Steps.get(step).SetVelolcity(velocity);
	}
	
	/**
	 * Get the left speakers volume of a separate step in the channel
	 * Is computed by multiplying the channel volume by the steps velocity ("volume")
	 * 
	 * @param step what step
	 * @return a float between 0 and 1
	 */
	public float GetVolumeLeft(int step) {
		if(mute) return 0.0f;
		
		if(step >= m_Steps.size()|| step < 0) {
			return 0.0f;
		}
		
		return (m_volume * m_Steps.get(step).GetVelocity());
	}
	
	/**
	 * This method returns the volume set to this channel
	 * @return The raw volume of this channel without caring about velocity 
	 * 		   of the next step or if the channel is muted
	 */
	public float getChannelVolume(){
		return m_volume;
	}
	
	/**
	 * Get the right speakers volume of a separate step in the channel
	 * Is computed by multiplying the channel volume by the steps velocity ("volume")
	 * 
	 * @param step what step
	 * @return a float between 0 and 1
	 */
	public float GetVolumeRight(int step) {
		if(mute) return 0.0f;
		
		if(step >= m_Steps.size() || step < 0) {
			return 0.0f; 
		}
		
		return (m_volume * m_Steps.get(step).GetVelocity());  
	}
	
	/**
	 * Returns the volume of the Channel.
	 * If the Channel is muted it returns zero
	 * 
	 * @return a float between 0 and 1
	 */
	public float GetVolume() {
		if(mute) return 0.0f;
		
		return m_volume;
	}
	
	/**
	 * Sets the volume of the channel.
	 * 
	 * @param volume A float between 0 and 1
	 */
	public void SetVolume(float volume) {
		m_volume = volume;
	}
	
	/**
	 * Sets mute to true or false
	 * @param mute
	 */
	public void SetMute(boolean mute){
		this.mute = mute;
	}
	
	/**
	 * Method for seeing if the channel is muted or not
	 * @return a boolean whether the channel is muted or not
	 */
	public boolean isMuted(){
		return mute;
	}
	
	/**
	 * Method for getting the number of steps in the channel
	 * @return the number of steps in the channel
	 */
	public int GetNumberOfSteps() {
		return m_Steps.size();
	}

	/**
	 * Sets the panning of the right and left speaker
	 * @param rightLevel
	 * @param leftLevel
	 */
	public void SetPanning(float rightLevel, float leftLevel) {
		m_leftPan = leftLevel;
		m_rightPan = rightLevel;
	}
	
	/**
	 * Method for getting the Left Panning of the Channel
	 * @return the Left Panning of the Channel
	 */
	public float GetLeftPanning() {
		return m_leftPan;
	}
	
	/**
	 * Method for getting the Right Panning of the Channel
	 * @return the Right Panning of the Channel
	 */
	public float GetRightPanning() {
		return m_rightPan;
	}

	/**
	 * Resizes the number of Steps in the Channel
	 * resizeByAmount may be a positive or negative number
	 * 
	 * @param resizeByAmount the number of steps to be added or removed
	 */
	public void ResizeBy(int resizeByAmount) {
		
		m_numSteps += resizeByAmount;
		
		if(m_numSteps < 0) {
			m_numSteps -= resizeByAmount;
			return;
		}
		
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
	
	/**
	 * Method for getting an arraylist with all the Channels Steps
	 * @return an arraylist with all the Channels Steps
	 */
	public ArrayList<Step> GetSteps() {
		return m_Steps;
	}

	
}