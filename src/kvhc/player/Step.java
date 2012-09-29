package kvhc.player;

public class Step {
	
	private boolean isActive;
	private float vel; // For future use, 
	
	public Step() 
	{
		isActive = false;
		vel = 0.7f;
	}
	
	public Step(boolean SetActive) {
		isActive = SetActive;
	}
	
	public void SetActive(boolean Active) {
		isActive = Active;
	}
	
	public boolean IsActive(){
		return isActive;
	}
	
	public void SetVelolcity(float velocity) {
		vel = velocity;
	}
	
	public float GetVelocity() {
		return vel;
	}
}
