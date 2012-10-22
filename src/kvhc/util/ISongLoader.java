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

import java.util.List;

import kvhc.models.Song;

/**
 * An interface for loading a song, or an "importer". 
 * 
 * It leaves the implementation details to the class. 
 * 
 * @author kvhc
 *
 */
public interface ISongLoader {
	
	/**
	 * Loads a song depending on the implementation from a list of arguments
	 * @param args A list of self chosen arguments
	 * @return Null if no song is found by the argument, otherwise a loaded song object
	 */
	Song loadSong(Object[] args);
	
	/**
	 * Returns a list of songs given some arguments
	 * @param args Implementation-based arguments
	 * @return An ArrayList of loaded songs
	 */
	List<Song> getSongList(Object[] args);
}
