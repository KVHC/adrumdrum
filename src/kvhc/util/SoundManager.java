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
 * @author Martin Breuer
 *
 */
@SuppressLint("UseSparseArrays")
public class SoundManager {
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
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
	public void playSound(int index, float right, float left, float volume)
	{
		if(mSoundPoolMap.get(index) != null) {
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			mSoundPool.play(mSoundPoolMap.get(index), volume*left*streamVolume, volume*right*streamVolume, 1, 0, 1f);
		}
	}
}
