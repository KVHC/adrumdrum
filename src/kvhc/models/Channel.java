/**
 * aDrumDrum is a step sequencer for Android.
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

package kvhc.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Channel class manages a channel containing a number of steps 
 * and a sound to play whenever one of its step is played.
 * 
 * @author kvhc
 *
 */
public class Channel {
	
	// Default variables
	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final float SPIKE_VELOCITY_CENTER = 1.0f;
	private static final float SPIKE_VELOCITY_INNER = 0.7f;
	private static final float SPIKE_VELOCITY_OUTER = 0.3f;

	// Identification
	private long mId;
	private int mChannelNumber;
	
	// Steps
	private int numSteps;
	private List<Step> mSteps;
	
	// Volume variables
	private float volume;
	private float leftPan;
	private float rightPan;
	private boolean mute;
	
	// The sound to play
	private Sound mSound;
	
	/**
	 * Constructor for an empty channel.
	 * Sets the Sound of the channel to null.
	 */
	public Channel() {
		this(null, DEFAULT_NUMBER_OF_STEPS); // 0 = inget ljud? 
	}
	
	/**
	 * Constructor for a channel with a sound.
	 * @param sound the sound of the channel
	 */
	public Channel(Sound sound) {
		this(sound, DEFAULT_NUMBER_OF_STEPS); // Ni vet
	}
	
	/**
	 * Constructor for a channel with a sound and a specific number of steps.
	 * @param sound the sound of the Channel
	 * @param steps the number of steps the channel should consist of
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
	 * Returns true if the step is active.
	 * @param step the step to check
	 * @return true if the step is active, false otherwise
	 */
	public boolean isStepActive(int step) {
		if (step >= numSteps || step < 0) {
			return false;
		}
		return mSteps.get(step).isActive();
	}
	
	/**
	 * Resets all steps. Sets them inactive and with default volume. 
	 */
	public void clearAllSteps() {
		for (Step step : mSteps) {
			step.reset();
		}
	}
	
	/**
	 * Sets the Sound of the Channel.
	 * @param sound the sound to be used
	 */
	public void setSound(Sound sound) {
		mSound = sound;
	}
	
	/**
	 * Method that returns the sound of the step.
	 * @return the Sound of the step
	 */
	public Sound getSound() {
		return mSound;
	}
	
	/**
	 * Toggles a step between active or inactive.
	 * If it's active, it is set to inactive and vice versa.
	 * @param step what step to toggle
	 * @return whether it's active or not after the operation
	 */
	public boolean toggleStep(int step) {
		mSteps.get(step).setActive(
				!mSteps.get(step).isActive()
			);
		
		return isStepActive(step);
	}
	
	/**
	 * Sets a specific step to active or not.
	 * @param step what step to set
	 * @param active boolean whether the step should be active or not
	 */
	public void setStep(int step, boolean active) {
		mSteps.get(step).setActive(active);
	}
	
	/**
	 * Sets a specific step to active or not active and with a specific velocity ("volume")
	 * @param step what step
	 * @param active boolean whether the step should be active or not
	 * @param velocity velocity ("volume") of the step, between 0 and 1
	 */
	public void setStep(int step, boolean active, float velocity) {
		mSteps.get(step).setActive(active);
		mSteps.get(step).setVelolcity(velocity);
	}
	
	/**
	 * Get the left speakers volume of a separate step in the channel
	 * Is computed by multiplying the channel volume by the steps velocity and the left panning.
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
		if (mute) {
			return 0.0f;
		}
		if (step >= mSteps.size() || step < 0) {
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
		if (mute) {
			return 0.0f;
		}
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
	 * Sets mute to true or false.
	 * @param mute
	 */
	public void setMute(boolean mute){
		this.mute = mute;
	}
	
	/**
	 * Method for seeing if the channel is muted or not.
	 * @return a boolean whether the channel is muted or not
	 */
	public boolean isMuted(){
		return mute;
	}
	
	/**
	 * Method for getting the number of steps in the channel.
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
	 * Sets the panning of the right and left speaker.
	 * @param rightLevel
	 * @param leftLevel
	 */
	public void setPanning(float rightLevel, float leftLevel) {
		leftPan = leftLevel;
		rightPan = rightLevel;
	}
	
	/**
	 * Method for getting the Left Panning of the Channel.
	 * @return the Left Panning of the Channel
	 */
	public float getLeftPanning() {
		return leftPan;
	}
	
	/**
	 * Method for getting the Right Panning of the Channel.
	 * @return the Right Panning of the Channel
	 */
	public float getRightPanning() {
		return rightPan;
	}

	/**
	 * Resizes the number of Steps in the Channel.
	 * resizeByAmount may be a positive or negative number.
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
	 * Method for getting n list with all the Channels Steps
	 * @return a list with all the Channels Steps
	 */
	public List<Step> getSteps() {
		return mSteps;
	}
	
    /**
     * Sets the step with given stepids velocity to 100%, the two neighbours to 70% and
     * the neighbours neighbours to 30%. Activates the mentioned steps.
     * @param stepid the center step of the spike
     */
    public void multiStepVelocitySpike(int stepid) {
    	
        mSteps.get(stepid).setVelolcity(SPIKE_VELOCITY_CENTER);
        mSteps.get(stepid).setActive(true);
        if (stepid-1 >= 0 && stepid-1 < numSteps) {
            mSteps.get(stepid-1).setActive(true);
            mSteps.get(stepid-1).setVelolcity(SPIKE_VELOCITY_INNER);
            if (stepid-2 >= 0 && stepid-2 < numSteps) {
                mSteps.get(stepid-2).setActive(true);
                mSteps.get(stepid-2).setVelolcity(SPIKE_VELOCITY_OUTER);
            }
        }
        if (stepid+1 >= 0 && stepid+1 < numSteps) {
            mSteps.get(stepid+1).setActive(true);
            mSteps.get(stepid+1).setVelolcity(SPIKE_VELOCITY_INNER);
            if (stepid+2 >= 0 && stepid+2 < numSteps) {
                mSteps.get(stepid+2).setActive(true);
                mSteps.get(stepid+2).setVelolcity(SPIKE_VELOCITY_OUTER);
            }
        }
    }
    
	/**
	 * Returns the ID of the channel.
	 * @return
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Sets the ID for the Channel.
	 * @param id the ID of the channel
	 */
	public void setId(long id) {
		mId = id;
	}

	/**
	 * Set all the steps at once.
	 * @param steps a list of step to use in the Channel
	 */
	public void setSteps(List<Step> steps) {
		if(steps == null) {
			return;
		}
		
		mSteps = steps;
		numSteps = steps.size();
	}

	/**
	 * Position of channel in song in database.
	 */
	public int getChannelNumber() {
		return mChannelNumber;
	}
	
	/**
	 * Sets the number of the Channel.
	 * @param channelNumber
	 */
	public void setChannelNumber(int channelNumber) {
		mChannelNumber = channelNumber;
	}

}
