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


/**
 * Model of a sound (used by Channel).
 * @author srejv
 *
 */
public class Sound {

	
	private long mId; // Id in the other system
	private int mSoundValue; // The location of the soundfile in an integer (from raw atm, probably should be object)
	private String mName;
	
	/**
	 * The constructor of a Sound.
	 * @param id The id of this sound
	 * @param soundValue The value of the sound in the R class
	 * @param name
	 */
	public Sound(int soundValue, String name) {
		mId = -1;
		mSoundValue = soundValue;
		mName = name;
	}
	
	public Sound(long id) {
		mId = id;
		mSoundValue = 0;
		mName = "";
	}
	
	/**
	 * Method for geting the id of this sound.
	 * @return the id of this sound
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Change the id of this sound.
	 * @param id The new id of the sound
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Method for getting the name of this sound.
	 * @return The name of this sound
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Change the name of this sound.
	 * @param name The new name of the channel
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Method for getting the id that this sound have in the R class.
	 * @return the id that this sound have in the R class
	 */
	public int getSoundValue() {
		return mSoundValue;
	}
	
	/**
	 * Method for setting the id that this sound have in the R class.
	 * @param soundValue the new value of the sound id in the R class
	 */
	public void setSoundValue(int soundValue) {
		mSoundValue = soundValue;
	}
	
	/**
	 * Returns a String version of the Sound
	 */
	public String toString() {
		// TODO Auto-generated method stub
		return this.mName;
	}
	
	
}
