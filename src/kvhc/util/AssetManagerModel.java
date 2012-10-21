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

import java.util.HashMap;

import kvhc.models.Song;
import kvhc.models.Sound;

/**
 * A Class for the management of sounds. Singleton
 * 
 * @author kvhc
 */
public final class AssetManagerModel<T> {
	
	private HashMap<String, T> mSoundMap;
	
	private AssetManagerModel<T> mInstance;
	
	// Two inner implementations of the asset manager model class. This might be stupid.
	private static AssetManagerModel<Sound> mSoundManager;
	private static AssetManagerModel<Song> mSongManager;
	
	/**
	 * Empty Constructor.
	 */
	private AssetManagerModel() {
	}
	
	/**
	 * Singleton pattern instance
	 * @return A reference to the singleton SoundManager object
	 */
	private AssetManagerModel<T> getInstance() {
		if(mInstance == null) {
			mInstance = new AssetManagerModel<T>();
			mInstance.mSoundMap = mSoundMap = new HashMap<String, T>(16);
		}
		return mInstance;
	}
	
	/**
	 * Gets the singleton Sound Manager.
	 * @return an instance of the Sound manager.
	 */
	public static AssetManagerModel<Sound> getSoundManager() {
		if(mSoundManager == null) {
			mSoundManager = new AssetManagerModel<Sound>().getInstance();
		}
		return mSoundManager; 
	}
	
	/**
	 * Gets the singleton Song Manager.
	 * @return an instance of the Song manager.
	 */
	public static AssetManagerModel<Song> getSongManager() {
		if(mSongManager == null) {
			mSongManager = new AssetManagerModel<Song>().getInstance();
		}
		return mSongManager;
	}
	
	/**
	 * Returns the value found by the key.
	 * @param key the key to success
	 * @return null if there exists no key
	 */
	public T getValue(String key) {
		if(mSoundMap.containsKey(key)) {
			return mSoundMap.get(key);
		}
		
		return null;
	}

	/**
	 * Set a value for a key, if the key exists, updates its reference.
	 * @param key the key to use
	 * @param value a Sound or a Song
	 */
	public void setValue(String key, T value) {
		mSoundMap.put(key, value);
	}
}
