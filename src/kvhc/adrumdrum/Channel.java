package kvhc.adrumdrum;

import java.util.ArrayList;

public class Channel {
	
	private int m_soundId;
	private int m_numSteps;
	private ArrayList<Step> m_Steps;
	
	public Channel() {
		this(0, 16); // 0 = inget ljud? 
	}
	
	public Channel(int soundId) {
		this(soundId, 16); // Ni vet
	}
	
	public Channel(int soundId, int steps) {
		m_soundId = soundId;
		m_numSteps = steps;
		m_Steps = new ArrayList<Step>(m_numSteps);
		
		for(int i = 0; i < m_numSteps; i++) {
			m_Steps.set(i, new Step());
		}
	}
	
	public boolean PlayStep(int i) {
		return m_Steps.get(i).IsActive();
	}
	
	public void SetSound(int soundId) {
		m_soundId = soundId;
	}
	
	public int GetSound() {
		return m_soundId;
	}
	
	// Togglar en Step.....
	public void ToggleStep(int step) {
		// LOL Vilken toggle.
		m_Steps.get(step).SetActive(
				!m_Steps.get(step).IsActive()
			);
	}
	
	public void SetStep(int step, boolean active) {
		m_Steps.get(step).SetActive(active);
	}
	
}
