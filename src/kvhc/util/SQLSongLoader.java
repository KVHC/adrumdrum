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
	 * Loads a song from SQL, Object source should be a String (name) 
	 */
	public Song loadSong(Object source) {
		// TODO Auto-generated method stub
		
		if(source.getClass() != String.class) {
			// source is not a string, dunno how to use it. 
			return null;
		}
		
		return null;
	}

}
