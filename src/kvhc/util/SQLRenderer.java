package kvhc.util;

import java.util.ArrayList;

import android.content.Context;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;


/**
 * A SQL implementation of the ISongRenderer interface, for saving songs to 
 * the database. 
 * 
 * @author kvhc
 *
 */
public class SQLRenderer implements ISongRenderer {
	
	
	private ArrayList<Sound> mSounds;
	private DatabaseHandler mDB;
	
	public SQLRenderer(Context context) {
		mDB = new DatabaseHandler(context);
	}
	

	/**
	 * 
	 * @param channelId
	 * @param channel
	 */
	private void SaveSteps(int channelId, Channel channel) {
		
		// set step table name 
		
		// set up insert query
		
		// set up update query
		
		// for each step in channel
			// set step values
			// if step exists (TODO or has id value?)
				// update step
			// else
				// insert step
	}
	
	/**
	 * 
	 * @param songId
	 * @param channel
	 */
	private void SaveChannel(int songId, Channel channel) {
		// set channel table name
		
		// set up insert query
		
		// set up update query
		
		// if channel is already in the database
			// update channel (or delete then insert? update is better.)
		// else
			// insert channel 
		
		// save all steps
	}
	
	/**
	 * Saves a song to the database, does all the dirty set up 
	 * 
	 * @param song Song to save to the database
	 * @return true if success 
	 */
	private boolean SaveSong(Song song) {
		
		// does the song have a name?
			// no? ehm. fail the save. cannot save noname (woundn't know how to load it and it would be forever lost in the dark abyss that is SQLite
		
		// set song table name
		
		// set up insert query
		
		// set up update query
		
		// if song exists
			// update song
		// else 
			// insert song
		
		// save all channels
		
		// returns true if all went well? -_- wat
		return false;
	}
	
	private boolean SaveSounds() {

		if(mSounds != null && mSounds.size() > 0) {
			// if the sounds are loaded and there are sounds in the list
			
			// set up insert query
			
			// set up update query
			
			// for all sounds
				// if sound is in database
					// update sound
				// else
					// insert sound
			
			
		}
		
		return false;
	}
	
	/*  ISongRenderer implementation  */
	public void LoadSounds(ArrayList<Sound> soundList) {
		mSounds = soundList;
	}
	
	public void RenderSong(Song song) {
		SaveSong(song);
	}
	
	public void RenderSongAtStep(Song song, int step) {
		//TODO: Add implementation?  Will we use this? Seems unessesary.	
	}
}
