package kvhc.util;

import java.util.ArrayList;

import kvhc.player.Song;
import kvhc.player.Sound;

public interface ISongRenderer {
	
	void RenderSong(Song song);
	void RenderSongAtStep(Song song, int step);
	
	void LoadSounds(ArrayList<Sound> soundList);
	
}
