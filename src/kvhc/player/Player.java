package kvhc.player;

import java.util.Observable;

import kvhc.util.AndroidTimer;
import kvhc.util.ISongRenderer;
import kvhc.util.SoundPoolRenderer;
import android.content.Context;

public class Player extends Observable {
	
	// Channel Renderer ;)
	ISongRenderer mChannelRender;
	
	// Timing
	Runnable r;
	AndroidTimer mTimer;
	private long waitTime; // Milliseconds
	
	
	// Sounds
	Song song = null;
	float rightSound;
	float leftSound;
	
	// State
	private int m_currentStep;
	boolean m_isPlaying;
	
	/**
	 * The constructor for creating a player
	 * @param context from witch to play the sounds in.
	 */
	public Player(Context context) {
		
		mChannelRender = new SoundPoolRenderer(context);
		
		//Saves the reference to MA
		//this.act = act;
		
		waitTime = 500;
		
		m_currentStep = 0;
		m_isPlaying = false;
		rightSound = (float) 1.0;
		leftSound = (float) 1.0;
        
        r = new Runnable(){
    		public void run() {
    			NextStep();
    		}
    	};
    	mTimer = new AndroidTimer(r,waitTime);
	}
	
	/**
	 * Loads a song into the player.
	 * The old song is discarded.
	 * @param newSong The song to load into the player
	 */
	public void LoadSong(Song newSong) {
		song = newSong;
		mChannelRender.LoadSounds(song.GetSounds());
	}
	
	/**
	 * Play the next step of the song and moves forward to the next
	 */
	private void NextStep() {
		if(m_isPlaying && song != null) {

			//act.setRadioButtonToActiveStep(m_currentStep);
			
			setChanged();
			notifyObservers(m_currentStep);
			
			mChannelRender.RenderSongAtStep(song, m_currentStep);
			
			m_currentStep++;

			if(m_currentStep >= song.GetNumberOfSteps()) {
				m_currentStep = 0;
			}
			
			
		}
	}
	
	/**
	 * Get the number of the current step
	 */
	public int GetActiveStep() {
		return m_currentStep; 
	}

	
	/**
	 * Sets the btm of the player
	 * @param bpm 
	 */
	public void SetWaitTimeByBPM(int bpm) {
		
		if(bpm <= 0){
			// S�kert dumt
			Stop();
			return;
		}
		
		// Kan man g�ra s�? :S
		// 1 / (bpm / 60s) f�r att f� fram tid mellan beats/step (4/4 iaf?)
		waitTime = (long)((60.0 / bpm)*1000);
		mTimer.setTime(waitTime);
	}
	
	/**
	 * Sets the bpm in a given range (used by progress bars)
	 * The bpm starts as 60 and each increase in the parameter increase the bpm by 6
	 * @param p. The bpm are calculated with 60 + 6 * p
	 */
	public void setBPMInRange(int p){
		SetWaitTimeByBPM(60 + p*6);
	}
	
	/**
	 * Starts the player
	 */
	public void Play() {
		m_isPlaying = true;
		mTimer.start();
	}

	/**
	 * Stops the player
	 * Also notifies all observers with a -1
	 */
	public void Stop() {
		m_isPlaying = false;
		m_currentStep = 0;
		mTimer.stop();
		setChanged();
		notifyObservers(-1);
	}
	
	/**
	 * Method for seeing if the player is runing or not
	 * @return true if the player is running, else false
	 */
	public boolean IsPlaying() {
		return m_isPlaying;
	}
}
