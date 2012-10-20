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

import kvhc.models.Song;
import kvhc.models.Sound;

/**
 * Interface to render a song.
 * Classes implements this to ensure they have all
 * the necessary functions to render a Song.
 * 
 * @author kvhc
 *
 */
public interface ISongRenderer {
	
	/**
	 * Renders a whole song
	 * @param song Song the render.
	 */
	void renderSong(Song song);
	
	/**
	 * Render song at a specific step.
	 * @param song Song to use
	 * @param step the specific step
	 */
	void renderSongAtStep(Song song, int step);
	
	/**
	 * Load sound from an ArrayList of Sound.
	 * @param soundList a list of Sounds
	 */
	void loadSounds(ArrayList<Sound> soundList);
	
}
