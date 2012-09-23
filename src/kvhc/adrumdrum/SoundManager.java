package kvhc.adrumdrum;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @author !!!!NÅN ANNAN!!!!!
 *
 * Grundar klassen på http://www.droidnova.com/creating-sound-effects-in-android-part-1,570.html
 * 
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
	
	public void playSound(int index)
	{
		if(mSoundPoolMap.get(index) != null) {
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
		}
	}
}