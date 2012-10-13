/**
 * aDrumDrum is a stepsequencer for Android.
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

package kvhc.player;

/**
 * A class representing a step (e.g. a single "beat" on a channel).
 * @author kvhc
 *
 */
public class Step {
	
	private boolean mIsActive;
	private float mVelocity;
	private int stepid;
	private Channel channel; 
	private int mNumber; // The step number
	
	/**
	 * Constructor, sets isActive to false by default.
	 * @param channel the channel of the step
	 * @param stepid the id/column of the step
	 */
	public Step(Channel channel, int stepid) {
		mIsActive = false;
		mVelocity = 0.7f;
		this.channel = channel;
		this.stepid = stepid;
	}
	
	/**
	 * Constructor.
	 * @param SetActive True to enable the step, false otherwise.
	 * @param Channel the channel of the step
	 * @param stepid the id/column of the step
	 */
	public Step(boolean setActive, Channel channel, int stepid) {
		mIsActive = setActive;
		this.channel = channel;
		this.stepid = stepid;
	}
	
	/**
	 * Enables or disables the step.
	 * @param Active True to enable the step, false to disable
	 */
	public void setActive(boolean active) {
		mIsActive = active;

	}
	
	/**
	 * Tells whether or not the step is enabled (audible).
	 * @return true if the step is enabled, false if disabled
	 */
	public boolean isActive(){
		return mIsActive;
	}
	
	/**
	 * Sets the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume.
	 * @param velocity a float greater than or equal to 0.0.
	 */
	public void setVelolcity(float velocity) {
		mVelocity = velocity;
	}
	
	/**
	 * Return the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume.
	 * @return the velocity of the step.
	 */
	public float getVelocity() {
		return mVelocity;
	}
	
    /**
     * Returns the channel the step is on.
     * @return the parent channel.
     */
    public Channel getChannel() {
        return channel;
    }
    
    /**
     * Returns the id of the step.
     * @return the id of the step
     */
    public int getStepId() {
    	return stepid;
    }
	
    /**
     * Sets the steps velocity to 100%, the two neighbours to 70% and
     * the neighbours neighbours to 30%. Activates the mentioned steps.
     */
    public void multiStepVelocitySpike() {
        channel.multiStepVelocitySpike(stepid);
    }
    
    /**
     * Resets the step to default values (Velocity=70%, step inactive).
     */
    public void reset() {
    	mIsActive = false;
		mVelocity = 0.7f;
    }

	/**
	 * Get the step number (for saving and loading)
	 * @return the number of the step in the song. 
	 */
	public int getStepNumber() {
		return mNumber;
	}
	
	/**
	 * Set the step number (for saving and loading)
	 * @param step position number
	 */
	public void setStepNumber(int stepNumber) {
		mNumber = stepNumber;
	}
}
