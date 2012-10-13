package kvhc.util;

import kvhc.player.Song;

/**
 * An interface for loading a song, or an "importer". 
 * 
 * It leaves the implementation details to the class. 
 * 
 * @author srejv
 *
 */
public interface ISongLoader {
	Song loadSong(Object source);
}
