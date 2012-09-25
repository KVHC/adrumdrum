package kvhc.player;

public class Step {
	
	private boolean isActive;
	private float vel; // For future use, 
	
	public Step() 
	{
		isActive = false;
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
	
	public void SetVel(float velocity) {
		vel = velocity;
	}
	
	public float GetVel() {
		return vel;
	}
}
