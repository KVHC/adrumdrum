package kvhc.util;

import android.content.Context;
import kvhc.player.Song;

/**
 * Used for loading a song from SQL
 * @author srejv
 *
 */
public class SQLSongLoader implements ISongLoader {

	DatabaseHandler mDB;
	
	public SQLSongLoader(Context context) {
		mDB = new DatabaseHandler(context);
	}
	
	/**
	 * Loads a song from SQL, Object source should be a String which should be the 
	 * name of the song 
	 */
	public Song loadSong(Object source) {
		// TODO Auto-generated method stub
		
		if(source.getClass() != String.class) {
			// if source is not a string,
			// we cant use it
			return null;
		}
		
		String name = (String)source;
		
		Song loadedSong = mDB.getSongByName(name);
		
		// After processing?
		
		return loadedSong;
	}

}
