package kvhc.util;

import java.util.ArrayList;

import kvhc.player.Song;

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
	ArrayList<Song> getSongList(Object[] args);
}
