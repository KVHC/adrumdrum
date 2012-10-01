package kvhc.util;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.SoundManager;

public class SoundPoolRenderer implements ISongRenderer {
	
	private SoundManager mSoundManager;
	
	public SoundPoolRenderer(Context context) {
		mSoundManager = new SoundManager();
        mSoundManager.initSounds(context);
	}
	
	/**
	 * Loads the sounds into the SoundManager. Other stuff might use sound differently but its in the interface for now
	 * 
	 * */
	public void LoadSounds(ArrayList<Sound> sounds) {
		for(Sound sound : sounds) {
			mSoundManager.addSound(sound.GetId(), sound.GetSoundValue());
		}
	}
	

	/**
	 * Renders the entire song to the SoundPool output. 
	 * Might not be a good idea.
	 */
	public void RenderSong(Song song) {
		int numsteps = song.GetNumberOfSteps();
		
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
		for(Channel c : song.GetChannels()) {
			if(c.IsStepActive(step)&& !c.isMuted()) {
				// 
				mSoundManager.playSound(c.GetSound().GetId(), 1.0f, 1.0f);
			}
		}
	}

	
}
