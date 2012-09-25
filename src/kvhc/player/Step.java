package kvhc.player;

/**
 * A class representing a step (e.g. a single "beat" on a channel)
 * @author kvhc
 *
 */
public class Step {
	
	
	private boolean isActive; //Whether or not a sound should play when the step is called.
	private float velocity; //The "volume" of the step.  
	
	/**
	 * Constructor, sets isActive to false by default
	 */
	public Step() 
	{
		isActive = false;
	}
	
	/**
	 * Constructor
	 * @param SetActive True to enable the step, false otherwise
	 */
	public Step(boolean SetActive) {
		isActive = SetActive;
	}
	
	/**
	 * Enables or disables the step
	 * @param Active True to enable the step, false to disable
	 */
	public void SetActive(boolean active) {
		isActive = active;
	}
	
	/**
	 * Tells whether or not the step is enabled (audible)
	 * @return true if the step is enabled, false if disabled
	 */
	public boolean IsActive(){
		return isActive;
	}
	
	/**
	 * Sets the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume
	 * @param velocity a float greater than or equal to 0.0.
	 */
	public void SetVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Return the velocity ("volume") of the step. 0.0 is inaudible, 1.0 is normal volume
	 * @return the velocity of the step.
	 */
	public float GetVelocity() {
		return velocity;
	}
}
