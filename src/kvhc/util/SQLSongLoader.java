package kvhc.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.Step;

/**
 * Used for loading a song from SQL
 * @author srejv
 *
 */
public class SQLSongLoader implements ISongLoader {

	private DatabaseHandler mDB;
	
	private ArrayList<Sound> mSoundList;
	
	private static String SONG_TABLE_NAME = "songs";
	
	public SQLSongLoader(Context context) {
		mDB = new DatabaseHandler(context);
	}
	
	/**
	 * Loads a song from SQL, Object source should be a String which should be the 
	 * name of the song 
	 */
	public Song loadSong(Object[] args) {
		if(args.length != 1) {
			return null;
		}
		
		
		
		if(args[0].getClass() != String.class) {
			// if source is not a string,
			// we cant use it
			return null;
		}
		
		String name = (String)args[0];
		
		Song loadedSong = mDB.getSongByName(name);
		
		// Maybe some post loading processing (like loading the song in here instead of just calling the db implementation.)
		
		return loadedSong;
	}
	
	/**
	 * Gets a list of all the sounds in the database
	 * @return
	 */
	private ArrayList<Sound> getSoundList() {
		
		if(mSoundList == null) {
			
			Cursor soundCursor = mDB.getCursorForTable(DatabaseHandler.TABLE_CHANNEL
					, new String[] { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_SOUNDVALUE, DatabaseHandler.KEY_NAME, 
					DatabaseHandler.KEY_RIGHTPAN, DatabaseHandler.KEY_LEFTPAN },
					"",
					new String[] {});
			
			if(soundCursor.moveToFirst()) {
				
				Sound sound = null;
				
				mSoundList = new ArrayList<Sound>(soundCursor.getCount());
				
				do {
					int id = soundCursor.getInt(0);
					int soundValue = soundCursor.getInt(1);
					String name = soundCursor.getString(2);
					
					sound = new Sound(id, soundValue, name);
					
					mSoundList.add(id-1, sound);
					
				} while(soundCursor.moveToNext());
			}
			
			if(mSoundList == null) 
				mSoundList = new ArrayList<Sound>(0); 
		}
		
		return mSoundList;
	}
	
	private ArrayList<Step> getStepsForChannel(int channelId) {
		
		
		ArrayList<Step> steps = null;
		
		Cursor stepCursor = mDB.getCursorForTable(DatabaseHandler.TABLE_STEP,
				new String[] { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_NUMBER, DatabaseHandler.KEY_VELOCITY, DatabaseHandler.KEY_ACTIVE },
				DatabaseHandler.FKEY_CHANNELID + "=?",
				new String[] { String.valueOf(channelId)});
		
		if(stepCursor.moveToFirst()) {
			
			Step step = null;
			
			steps = new ArrayList<Step>(stepCursor.getCount());
			
			do {
				// Load the parameters
				int id = stepCursor.getInt(0);
				int number = stepCursor.getInt(1);
				double velocity = stepCursor.getDouble(2);
				boolean active = stepCursor.getInt(3) != 0;
				
				// Create the step
				step = new Step(active);
				step.setVelolcity((float)velocity);
				step.setStepNumber(number);
				
				// Add it to the list
				steps.add(number, step);
				
			} while(stepCursor.moveToNext());
		}
		
		if(steps == null) {
			// In case it's not initialized, just return an empty list
			return new ArrayList<Step>(8);
		}
		
		return steps;
	}

	private ArrayList<Channel> getChannelsForSongId(int songId) {
		
		ArrayList<Channel> channels = null;
		
		Cursor channelCursor = mDB.getCursorForTable(DatabaseHandler.TABLE_CHANNEL
				, new String[] { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_VOLUME, 
				DatabaseHandler.KEY_RIGHTPAN, DatabaseHandler.KEY_LEFTPAN, DatabaseHandler.FKEY_SOUNDID },
				DatabaseHandler.FKEY_SONGID + "=?", 
				new String[] { String.valueOf(songId) });
		
		if(channelCursor.moveToFirst()) {
			
			Channel channel = null;
			
			channels = new ArrayList<Channel>(channelCursor.getCount());
			
			do {
				int id = channelCursor.getInt(0);
				double volume = channelCursor.getDouble(1);
				double rightPan = channelCursor.getDouble(2);
				double leftPan = channelCursor.getDouble(3);
				int soundId = channelCursor.getInt(4);
				
				Sound sound = getSoundList().get(soundId-1);
				
				ArrayList<Step> steps = getStepsForChannel(id);
				
				channel = new Channel(sound, steps.size());
				for(Step step : steps) {
					channel.setStep(step.getStepNumber(), step.isActive(), step.getVelocity());
				}
				
				channel.setVolume((float)volume);
				channel.setPanning((float)rightPan, (float)leftPan);
				
				channels.add(channel);
			} while(channelCursor.moveToNext());
		} else {
			// There are no channels
			channels = new ArrayList<Channel>(0);
		}
		
		
		return channels;
		
	}
	
	/**
	 * Returns a list of songs
	 */
	public ArrayList<Song> getSongList(Object[] args) {
		
		ArrayList<Song> songs;
		
		Cursor songCursor = mDB.getCursorForTable(DatabaseHandler.TABLE_SONG, new String[] {DatabaseHandler.KEY_ID , DatabaseHandler.KEY_NAME }, "", null );
		
		
		if(songCursor.moveToFirst()) {
			// If the cursor isn't empty it points to the first row
			Song song = null;
			
			songs = new ArrayList<Song>(songCursor.getCount());
			
			do {
				
				int songId = songCursor.getInt(0);
				String songName = songCursor.getString(1);
				
				// Load the channels
				
				
				
				
				songs.add(song);
				
			} while(songCursor.moveToNext());
		}
		
		return songs;
	}

}
