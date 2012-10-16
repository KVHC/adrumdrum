package kvhc.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.SoundManager;

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
	HashMap<Integer, Integer> mSoundMap;
	
	public SoundPoolRenderer(Context context) {
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(context);
		mSoundMap = new HashMap<Integer, Integer>(16);
        
	}
	
	/**
	 * Loads the sounds into the SoundManager. Other stuff might use sound differently but its in the interface for now
	 * 
	 * */
	public void LoadSounds(ArrayList<Sound> sounds) {
		mSoundMap = new HashMap<Integer, Integer>(sounds.size());
		int i = 1;
		for(Sound sound : sounds) {
			if(sound != null) {
				mSoundMap.put(sound.getId(), i);
				mSoundManager.addSound(i, sound.getSoundValue());
				i++;
			}
		}
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
			if (c.isStepActive(step)&& !c.isMuted()) {
				if(c.getSound() != null) {
					int soundManagerId = mSoundMap.get(c.getSound().getId());
					mSoundManager.playSound(soundManagerId, c.getVolumeRight(step), c.getVolumeLeft(step), c.getVolume());
				}
			}
		}
	}

	
}
