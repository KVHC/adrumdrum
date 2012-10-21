package kvhc.util;

import java.util.HashMap;
import java.util.Map;

import kvhc.models.Song;
import kvhc.models.Sound;

/**
 * A Class for the management of sounds. Singleton
 * 
 * @author kvhc
 */
public final class AssetManagerModel<T> {
	
	private Map<String, T> mSoundMap;
	
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
		// TODO: might not be neccessary?
		if(mInstance == null) {
			mInstance = new AssetManagerModel<T>();
			mInstance.mSoundMap = mSoundMap = new HashMap<String, T>(16);
		}
		
		return mInstance;
	}
	
	/**
	 * Gets the singleton Sound Manager
	 * @return 
	 */
	public static AssetManagerModel<Sound> getSoundManager() {
		if(mSoundManager == null) {
			mSoundManager = new AssetManagerModel<Sound>().getInstance();
		}
		return mSoundManager; 
	}
	
	/**
	 * Gets the singleton Song Manager
	 * @return
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
	 * @param key
	 * @param value
	 */
	public void setValue(String key, T value) {
		mSoundMap.put(key, value);
	}
}
