package kvhc.adrumdrum;

/**
 * The Channel class manages a channel containing a number
 * of steps and a sound to play for each active step
 * 
 * @author kvhc
 *
 */
public class Channel {
	
	private int m_soundId;
	private int m_numSteps;
	private Step[] m_Steps;
	
	public Channel() {
		this(0, 16); // 0 = inget ljud? 
	}
	
	public Channel(int soundId) {
		this(soundId, 16); // Ni vet
	}
	
	public Channel(int soundId, int steps) {
		m_soundId = soundId;
		m_numSteps = steps;
		m_Steps = new Step[m_numSteps];
		
		for(int i = 0; i < m_numSteps; i++) {
			m_Steps[i] = new Step();
		}
	}
	
	public boolean PlayStep(int i) {
		return m_Steps[i].IsActive();
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
		m_Steps[step].SetActive(
				!m_Steps[step].IsActive()
			);
	}
	
	public void SetStep(int step, boolean active) {
		m_Steps[step].SetActive(active);
	}
	
}
