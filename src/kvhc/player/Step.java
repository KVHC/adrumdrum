package kvhc.player;

/**
 * A class representing a step (e.g. a single "beat" on a channel)
 * @author kvhc
 *
 */
public class Step {
	
	private boolean mIsActive;
	private float mVelocity; // For future use, 
	
	/**
	 * Constructor, sets isActive to false by default
	 */
	public Step() 
	{
		mIsActive = false;
		mVelocity = 0.7f;
	}
	
	/**
	 * Constructor
	 * @param SetActive True to enable the step, false otherwise
	 */
	public Step(boolean SetActive) {
		mIsActive = SetActive;
	}
	
	/**
	 * Enables or disables the step
	 * @param Active True to enable the step, false to disable
	 */
	public void SetActive(boolean Active) {
		mIsActive = Active;
	}
	
	/**
	 * Tells whether or not the step is enabled (audible)
	 * @return true if the step is enabled, false if disabled
	 */
	public boolean IsActive(){
		return mIsActive;
	}
	
  /**
	 * Sets the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume
	 * @param velocity a float greater than or equal to 0.0.
	 */
	public void SetVelolcity(float velocity) {
		mVelocity = velocity;
	}
	
  /**
	 * Return the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume
	 * @return the velocity of the step.
	 */
	public float GetVelocity() {
		return mVelocity;
	}
}
