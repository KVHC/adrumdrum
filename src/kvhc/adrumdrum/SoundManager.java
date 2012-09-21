package kvhc.adrumdrum;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @author !!!!N�N ANNAN!!!!!
 *
 * KOD TAGEN FR�N 
 * http://www.droidnova.com/creating-sound-effects-in-android-part-1,570.html
 * 
 * Fast nu �ndrade jag till Arraylist f�r det �r ju fan s� han anv�nnde den, det j�vla n�tet.
 *
 */
public class SoundManager {
	private  SoundPool mSoundPool;
	private  HashMap<Integer, Integer> mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  Context mContext;
	
	public void initSounds(Context theContext) {
	    mContext = theContext;
	    mSoundPool = new SoundPool(24, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer, Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	public void playSound(int index,float right, float left)
	{
		if(mSoundPoolMap.get(index) != null) {
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			mSoundPool.play(mSoundPoolMap.get(index), left * streamVolume, right * streamVolume, 1, 0, 1f);
		}
	}
}