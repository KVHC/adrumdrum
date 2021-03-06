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

/**
 * A class representing a step (e.g. a single "beat" on a channel).
 * @author kvhc
 *
 */
public class Step implements Cloneable {
	
	// Default variables
	private static final float DEFAULT_VELOCITY = 0.7f;
	// Members
	private boolean mIsActive;
	private float mVelocity;
	private int mNumber; // The step number
	private long id;
	
	/**
	 * Constructor, sets isActive to false by default.
	 * @param stepid the id/column of the step
	 */
	public Step(int stepid) {
		mIsActive = false;
		mVelocity = DEFAULT_VELOCITY;
		this.mNumber = stepid;
	}
	
	/**
	 * Empty Constructor, set everything to default values.
	 */
	public Step() {
		this.mIsActive = false;
		this.mVelocity = DEFAULT_VELOCITY;
		this.mNumber = 0;
		this.id = 0;
	}
	
	/**
	 * Constructor.
	 * @param setActive True to enable the step, false otherwise.
	 * @param stepid the id/column of the step
	 */
	public Step(boolean setActive, int stepid) {
		mIsActive = setActive;
		this.mNumber = stepid;
	}
	
	/**
	 * Enables or disables the step.
	 * @param active True to enable the step, false to disable
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
     * Resets the step to default values (Velocity=70%, step inactive).
     */
    public void reset() {
    	mIsActive = false;
		mVelocity = DEFAULT_VELOCITY;
    }

	/**
	 * Get the step number (for saving and loading).
	 * @return the number of the step in the song. 
	 */
	public int getStepNumber() {
		return mNumber;
	}
	
	/**
	 * Set the step number (for saving and loading).
	 * @param stepNumber position number
	 */
	public void setStepNumber(int stepNumber) {
		mNumber = stepNumber;
	}

	/**
	 * Clones a step.
	 * @see java.lang.Object#clone()
	 */
	public Step clone() throws CloneNotSupportedException {
		Step clone = new Step();
		clone.mIsActive = this.mIsActive;
		clone.mVelocity = this.mVelocity;
		return clone;
	}
	
	/**
	 * Gets the ID of the step (for uniqueness).
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the ID of the step (for uniqueness).
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
}
