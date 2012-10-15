package kvhc.player;

import java.util.Observable;

import kvhc.util.AndroidTimer;
import kvhc.util.ISongRenderer;
import kvhc.util.SoundPoolRenderer;
import android.content.Context;

public class Player extends Observable {
	
	// Channel Renderer
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
	private int currentStep;
	boolean isPlaying;
	
	/**
	 * The constructor for creating a player
	 * @param context from witch to play the sounds in.
	 */
	public Player(Context context) {
		
		mChannelRender = new SoundPoolRenderer(context);
		waitTime = 500;
		
		currentStep = 0;
		isPlaying = false;
		rightSound = (float) 1.0;
		leftSound = (float) 1.0;
        
        r = new Runnable(){
    		public void run() {
    			nextStep();
    		}
    	};
    	mTimer = new AndroidTimer(r,waitTime);
	}
	
	/**
	 * Loads a song into the player.
	 * The old song is discarded.
	 * @param newSong The song to load into the player
	 */
	public void loadSong(Song newSong) {
		song = newSong;
		mChannelRender.LoadSounds(song.GetSounds());
	}
	
	/**
	 * Play the next step of the song and moves forward to the next
	 */
	private void nextStep() {
		if (isPlaying && song != null) {
			
			setChanged();
			notifyObservers(currentStep);
			
			mChannelRender.RenderSongAtStep(song, currentStep);
			
			currentStep++;

			if (currentStep >= song.getNumberOfSteps()) {
				currentStep = 0;
			}
		}
	}
	
	/**
	 * Get the number of the current step
	 */
	public int getActiveStep() {
		return currentStep; 
	}

	
	/**
	 * Sets the bpm of the player
	 * @param bpm 
	 */
	public void setWaitTimeByBPM(int bpm) {
		
		if (bpm <= 0){
			stop();
			return;
		}

		waitTime = (long)((60.0 / bpm)*1000);
		mTimer.setTime(waitTime);
	}
	
	/**
	 * Sets the bpm in a given range (used by progress bars)
	 * The bpm starts as 60 and each increase in the parameter increase the bpm by 6
	 * @param p. The bpm are calculated with 60 + 6 * p
	 */
	public void setBPMInRange(int p){
		setWaitTimeByBPM(60 + p*6);
	}
	
	/**
	 * Starts the player
	 */
	public void play() {
		isPlaying = true;
		mTimer.start();
	}

	/**
	 * Stops the player
	 * Also notifies all observers with a -1
	 */
	public void stop() {
		if(isPlaying){
			isPlaying = false;
			currentStep = 0;
			mTimer.stop();
			setChanged();
			notifyObservers(-1);
		}
	}
	
	/**
	 * Method for seeing if the player is runing or not
	 * @return true if the player is running, else false
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
}
