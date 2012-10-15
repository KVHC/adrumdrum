/**
 * aDrumDrum is a stepsequencer for Android.
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
