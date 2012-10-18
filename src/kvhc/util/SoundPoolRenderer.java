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

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.models.Sound;
import kvhc.util.db.SoundDataSource;

/**
 * Uses SoundPool for android as output for RenderSong and RenderSongAtStep 
 * 
 * 
 * @author kvhc
 *
 */
@SuppressLint("UseSparseArrays")
public class SoundPoolRenderer implements ISongRenderer {
	
	private SoundManager mSoundManager;
	HashMap<Long, Integer> mSoundMap;
	
	private SoundDataSource mDBSoundHelper;
	
	public SoundPoolRenderer(Context context) {
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(context);
		mSoundMap = new HashMap<Long, Integer>(16);
        
		mDBSoundHelper = new SoundDataSource(context);
	}
	
	/**
	 * Loads the sounds into the SoundManager. Other stuff might use sound differently but its in the interface for now
	 * 
	 * */
	public void LoadSounds(ArrayList<Sound> sounds) {
		mSoundMap = new HashMap<Long, Integer>(sounds.size());
		
		int i = 1;
		
		mDBSoundHelper.open();
		for(Sound sound : mDBSoundHelper.getAllSounds()) {
			if(sound != null) {
				mSoundMap.put(sound.getId(), i);
				mSoundManager.addSound(i, sound.getSoundValue());
				i++;
			}
		}
		mDBSoundHelper.close();
	}
	

	/**
	 * Renders the entire song to the SoundPool output. 
	 * Might not be a good idea.
	 */
	public void RenderSong(Song song) {
		int numsteps = song.getNumberOfSteps();
		
		for(int i = 0; i < numsteps; i++) {
			RenderSongAtStep(song, i);
		}
	}

	/**
	 * Renders all the channels at the input step
	 * 
	 * IF sounds not loaded, cannot playback.
	 */
	public void RenderSongAtStep(Song song, int step) {
		for (Channel c : song.getChannels()) {
			if (c.isStepActive(step) && !c.isMuted()) {
				if(c.getSound() != null) {
					int soundManagerId = mSoundMap.get(c.getSound().getId());
					mSoundManager.playSound(soundManagerId, c.getVolumeRight(step), c.getVolumeLeft(step), c.getVolume());
				}
			}
		}
	}

	
}
