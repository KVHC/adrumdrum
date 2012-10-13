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
	
	//private int soundId;
	private int numSteps;
	private ArrayList<Step> mSteps;
	private boolean mute;
	
	private float volume;
	private float leftPan;
	private float rightPan;
	
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
		numSteps = steps;
		mSteps = new ArrayList<Step>(numSteps);
		
		int stepIdToAdd = 0;
		
		for (int i = 0; i < numSteps; i++) {
			mSteps.add(new Step(this, stepIdToAdd++));
		}
		
		volume = 1.0f;
		leftPan = 1.0f;
		rightPan = 1.0f;
		
		mute = false;
	}
	
	/**
	 * Returns true if the step is active
	 * @param i
	 * @return
	 */
	public boolean isStepActive(int i) {
		if( i >= numSteps || i < 0) {
			return false;
		}
		return mSteps.get(i).isActive();
	}
	
	/**
	 * Sets all steps to inactive
	 */
	public void clearAllSteps() {
		for (Step step : mSteps) {
			step.reset();
		}
	}
	
	/**
	 * Sets the Sound of the Channel
	 * @param sound the sound to be used
	 */
	public void setSound(Sound sound) {
		mSound = sound;
	}
	
	/**
	 * Method that returns the sound of the step.
	 * @return the sound of the step
	 */
	public Sound getSound() {
		return mSound;
	}
	
	/**
	 * Toggles a step between active or inactive
	 * If it's active, it is set to inactive and vice versa
	 * @param step what step
	 * @return whether it's active or not after the operation
	 */
	public boolean toggleStep(int step) {
		mSteps.get(step).setActive(
				!mSteps.get(step).isActive()
			);
		
		return isStepActive(step);
	}
	
	/**
	 * Sets a specific step to active or not
	 * @param step what step
	 * @param active boolean whether the step should be active or not
	 */
	public void setStep(int step, boolean active) {
		mSteps.get(step).setActive(active);
	}
	
	/**
	 * Sets a specific step to active or not active and with a specific velocity ("volume")
	 * @param step what step
	 * @param active boolean whether the step should be active or not
	 * @param velocity velocity ("volume") of the step
	 */
	public void setStep(int step, boolean active, float velocity) {
		mSteps.get(step).setActive(active);
		mSteps.get(step).setVelolcity(velocity);
	}
	
	/**
	 * Get the left speakers volume of a separate step in the channel
	 * Is computed by multiplying the channel volume by the steps velocity ("volume")
	 * 
	 * @param step what step
	 * @return a float between 0 and 1
	 */
	public float getVolumeLeft(int step) {
		if (mute) {
			return 0.0f;
		}
		
		if (step >= mSteps.size()|| step < 0) {
			return 0.0f;
		}
		
		return (volume * leftPan * mSteps.get(step).getVelocity());
	}
	
	/**
	 * This method returns the volume set to this channel
	 * @return The raw volume of this channel without caring about velocity 
	 * 		   of the next step or if the channel is muted
	 */
	public float getChannelVolume(){
		return volume;
	}
	
	/**
	 * Get the right speakers volume of a separate step in the channel
	 * Is computed by multiplying the channel volume by the steps velocity ("volume")
	 * 
	 * @param step what step
	 * @return a float between 0 and 1
	 */
	public float getVolumeRight(int step) {
		if(mute) return 0.0f;
		
		if(step >= mSteps.size() || step < 0) {
			return 0.0f; 
		}
		
		return (volume * rightPan * mSteps.get(step).getVelocity());  
	}
	
	/**
	 * Returns the volume of the Channel.
	 * If the Channel is muted it returns zero
	 * 
	 * @return a float between 0 and 1
	 */
	public float getVolume() {
		if(mute) return 0.0f;
		
		return volume;
	}
	
	/**
	 * Sets the volume of the channel.
	 * 
	 * @param volume A float between 0 and 1
	 */
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	/**
	 * Sets mute to true or false
	 * @param mute
	 */
	public void setMute(boolean mute){
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
	public int getNumberOfSteps() {
		return mSteps.size();
	}
	
	public Step getStepAt(int i){
		if (i>=0 && i < mSteps.size()){
			return mSteps.get(i);
		}else {
			return null;
		}
	}

	/**
	 * Sets the panning of the right and left speaker
	 * @param rightLevel
	 * @param leftLevel
	 */
	public void setPanning(float rightLevel, float leftLevel) {
		leftPan = leftLevel;
		rightPan = rightLevel;
	}
	
	/**
	 * Method for getting the Left Panning of the Channel
	 * @return the Left Panning of the Channel
	 */
	public float getLeftPanning() {
		return leftPan;
	}
	
	/**
	 * Method for getting the Right Panning of the Channel
	 * @return the Right Panning of the Channel
	 */
	public float getRightPanning() {
		return rightPan;
	}

	/**
	 * Resizes the number of Steps in the Channel
	 * resizeByAmount may be a positive or negative number
	 * 
	 * @param resizeByAmount the number of steps to be added or removed
	 */
	public void resizeBy(int resizeByAmount) {
		
		numSteps += resizeByAmount;
		
		if (numSteps < 0) {
			numSteps -= resizeByAmount;
			return;
		}
		
		if (resizeByAmount < 0) {
			for(int i = 0; i < (-resizeByAmount); i++) {
				mSteps.remove(mSteps.size()-1);
			}
		} else {
			for(int i = 0; i < resizeByAmount; i++) {
				mSteps.add(new Step(this, mSteps.size()));
			}
		}
	}
	
	/**
	 * Method for getting an arraylist with all the Channels Steps
	 * @return an arraylist with all the Channels Steps
	 */
	public ArrayList<Step> getSteps() {
		return mSteps;
	}
	
    /**
     * Sets the step with given stepids velocity to 100%, the two neighbours to 70% and
     * the neighbours neighbours to 30%. Activates the mentioned steps.
     * @param stepid the center step of the spike
     */
    public void multiStepVelocitySpike(int stepid) {
    	
        mSteps.get(stepid).setVelolcity(1.0f);
        mSteps.get(stepid).setActive(true);
        if (stepid-1 >= 0 && stepid-1 < numSteps) {
            mSteps.get(stepid-1).setActive(true);
            mSteps.get(stepid-1).setVelolcity(0.7f);
            if (stepid-2 >= 0 && stepid-2 < numSteps) {
                mSteps.get(stepid-2).setActive(true);
                mSteps.get(stepid-2).setVelolcity(0.3f);
            }
        }
        if (stepid+1 >= 0 && stepid+1 < numSteps) {
            mSteps.get(stepid+1).setActive(true);
            mSteps.get(stepid+1).setVelolcity(0.7f);
            if (stepid+2 >= 0 && stepid+2 < numSteps) {
                mSteps.get(stepid+2).setActive(true);
                mSteps.get(stepid+2).setVelolcity(0.3f);
            }
        }
    }


}