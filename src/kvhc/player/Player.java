/**
 * aDrumDrum is a stepsequencer for Android.
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

package kvhc.player;

import java.util.Observable;

import kvhc.util.AndroidTimer;
import kvhc.util.ISongRenderer;
import kvhc.util.SoundPoolRenderer;
import android.content.Context;

public class Player extends Observable {
	
	// Channel Renderer
	ISongRenderer mSongRender;
	
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
		
		mSongRender = new SoundPoolRenderer(context);
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
		mSongRender.LoadSounds(song.GetSounds());
	}
	
	/**
	 * Play the next step of the song and moves forward to the next
	 */
	private void nextStep() {
		if (isPlaying && song != null) {
			
			setChanged();
			notifyObservers(currentStep);
			
			mSongRender.RenderSongAtStep(song, currentStep);
			
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
		waitTime /= 4;
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
	 * Method for seeing if the player is running or not
	 * @return true if the player is running, else false
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
}
