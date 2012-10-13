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

}
