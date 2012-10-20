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

package kvhc.util;

import java.util.Observable;

import kvhc.models.Song;
import kvhc.util.AndroidTimer;
import kvhc.util.ISongRenderer;
import kvhc.util.SoundPoolRenderer;
import android.content.Context;

/**
 * Class representing a player. 
 * It knows the current step and if it is playing. 
 * Notifies observers when it changes step.
 * 
 * @author kvhc
 */
public class Player extends Observable {
	
	// Default variables
	private static final int DEFAULT_WAITTIME = 500;
	
	// Channel Renderer
	private ISongRenderer mSongRender;
	
	// Timing
	private AndroidTimer mTimer;
	private long waitTime; // in milliseconds
	
	// Sounds
	private Song song = null;
	
	// State
	private int currentStep;
	private boolean isPlaying;
	
	/**
	 * The constructor for creating a player
	 * @param context from witch to play the sounds in.
	 */
	public Player(Context context) {
		mSongRender = new SoundPoolRenderer(context);
		waitTime = DEFAULT_WAITTIME;
		
		currentStep = 0;
		isPlaying = false;
        
		Runnable r = new Runnable(){
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
		mSongRender.loadSounds(song.getSounds());
	}
	
	/**
	 * Play the current step of the song and moves forward to the next.
	 * Only changes to the next step if the Player is playing (CAPTAIN OBVIOUS).
	 * Notifies observers (for example Views in the GUI) about the step change.
	 */
	private void nextStep() {
		if (isPlaying && song != null) {
			
			setChanged();
			notifyObservers(currentStep);
			
			mSongRender.renderSongAtStep(song, currentStep);
			
			currentStep++;

			if (currentStep >= song.getNumberOfSteps()) {
				currentStep = 0;
			}
		}
	}
	
	/**
	 * Get the number of the current step.
	 */
	public int getActiveStep() {
		return currentStep; 
	}

	/**
	 * Sets the bpm (Beats Per Minute) of the player.
	 * This calculates and sets the waitTime (time between two sounds play) of the timer.
	 * @param bpm the bpm to set
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
	 * Sets the bpm in a given range (p). This method is called from progress bars in the GUI
	 * whos range is between 0 and 100.
	 * The bpm starts as 30 and each increase in the parameter increase the bpm by 3.
	 * @param p The bpm are calculated with 30 + 3 * p
	 * @return the bpm set to the player
	 */
	public int setBPMInRange(int p) {
		int bpm = 30 + 3 * p;
		setWaitTimeByBPM(bpm);
		return(bpm);
	}
	
	/**
	 * Starts the player and its timer.
	 */
	public void play() {
		isPlaying = true;
		mTimer.start();
	}

	/**
	 * Stops the player.
	 * Also notifies all observers with (-1).
	 */
	public void stop() {
		if (isPlaying){
			isPlaying = false;
			currentStep = 0;
			mTimer.stop();
			setChanged();
			notifyObservers(-1);
		}
	}
	
	/**
	 * Method for seeing if the player is running or not.
	 * @return true if the player is running, else false
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
}
