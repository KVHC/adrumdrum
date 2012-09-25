package kvhc.player;

import java.util.HashMap;
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
 * @author Martin Breuer
 *
 */
public class SoundManager {
	private  SoundPool mSoundPool;
	private  HashMap<Integer, Integer> mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  Context mContext;
	
	
	/**
	 * Initiate a SoundManager
	 * 
	 * @param theContext Context for the audo service
	 */
	
	public void initSounds(Context theContext) {
	    mContext = theContext;
	    mSoundPool = new SoundPool(24, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer, Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	/**
	 * Add a sound to a SoundManager
	 * 
	 * @param index index of the sound added
	 * @param SoundID resource ID
	 */
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/**
	 * Play a sound
	 * 
	 * @param index a sound to play
	 * @param right right volume
	 * @param left left volume
	 */
	public void playSound(int index,float right, float left, float volume)
	{
		if(mSoundPoolMap.get(index) != null) {
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			mSoundPool.play(mSoundPoolMap.get(index), volume*left*streamVolume, volume*right*streamVolume, 1, 0, 1f);
		}
	}
}