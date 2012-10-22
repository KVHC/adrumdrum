package kvhc.util;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 *
 * The SoundManager class is used to manage and play sounds.
 *
 * Code based on blog entry by Martin Breuer:
 * http://www.droidnova.com/creating-sound-effects-in-android-part-1,570.html
 * 
 * @author Martin Breuer/kvhc
 *
 */
@SuppressLint("UseSparseArrays")
public class SoundManager {
	
	private SoundPool mSoundPool;
	private Map<Integer, Integer> mSoundPoolMap;
	private AudioManager  mAudioManager;
	private Context mContext;
	
	/**
	 * Initiate a SoundManager.
	 * 
	 * @param context Context for the audio service.
	 */
	public void initSounds(Context context) {
	    mContext = context;
	    mSoundPool = new SoundPool(24, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer, Integer>(16);
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	/**
	 * Add a sound to a SoundManager.
	 * 
	 * @param index index of the sound added.
	 * @param soundID resource ID.
	 */
	public void addSound(int index, int soundID) {
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, soundID, 1));
	}
	
	/**
	 * Play a sound.
	 * All volume values are floats between 0 and 1.
	 * Volume for right/left speaker are multiplied by the "global" volume.
	 * 
	 * @param index index of the sound to play
	 * @param right right volume
	 * @param left left volume
	 * @param volume "global" volume
	 */
	public void playSound(int index, float right, float left, float volume)	{
		if(mSoundPoolMap.get(index) != null) {
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSoundPoolMap.get(index), volume*left*streamVolume, volume*right*streamVolume, 1, 0, 1f);
		}
	}
}
