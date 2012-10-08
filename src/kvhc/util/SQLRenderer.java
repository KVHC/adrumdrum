package kvhc.util;

import java.util.ArrayList;

import kvhc.player.Song;
import kvhc.player.Sound;



public class SQLRenderer implements ISongRenderer {
	
	
	ArrayList<Sound> mSounds;
	
	
	
	public void LoadSounds(ArrayList<Sound> soundList) {
		mSounds = soundList;
	}
	
	
	public void RenderSong(Song song) {
		// TODO Auto-generated method stub
		
	}
	
	public void RenderSongAtStep(Song song, int step) {
		// TODO Auto-generated method stub
		
	}
}
