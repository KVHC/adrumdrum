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

import android.content.ContentValues;
import android.content.Context;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.Step;


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
	
	/**
	 * constructor
	 * @param context Android context for some activity or view, parent...
	 */
	public SQLRenderer(Context context) {
		mDB = new DatabaseHandler(context);
	}
	

	/**
	 * Saves the steps to the database 
	 * @param channelId the id of the channel 
	 * @param channel the channel model to save
	 */
	private void SaveSteps(int channelId, Channel channel) {
		
		// set step table name 
		String stepTable = DatabaseHandler.TABLE_STEP;
				
		// set up insert query
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.FKEY_CHANNELID, channelId);
		
		// set up update query
		String whereClause = DatabaseHandler.FKEY_CHANNELID + "=? AND " +
				DatabaseHandler.KEY_NUMBER + "=?";
		String[] whereArgs = new String[] { "channel_id", "stepnumber" };
		whereArgs[0] = String.valueOf(channelId);
		
		int i = 0; // TODO dubbelkolla att den verkligen går igenom alla i rätt ordning, fast det borde den ju göra
		for(Step step : channel.getSteps()) {
			// for each step in channel
						
			// set step values
			values.put(DatabaseHandler.KEY_NUMBER, step.getStepNumber());
			values.put(DatabaseHandler.KEY_VELOCITY, step.getVelocity());

			// set up where args
			whereArgs[1] = String.valueOf(i++);
			
			// if step exists (TODO or has id value?)
			// update step
			boolean updateSuccess = mDB.updateRowInTable(stepTable, values, whereClause, whereArgs);
			
			if(!updateSuccess) {
				// else
				// insert step
				mDB.insertRowIntoTable(stepTable , values);
			}
		}
	}
	
	/**
	 * 
	 * @param songId
	 * @param channel
	 */
	private void SaveChannels(int songId, Song song) {
		// set channel table name
		String channelTable = DatabaseHandler.TABLE_CHANNEL;
				
		// set up insert query
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.FKEY_SONGID, songId);
		
		// set up update query
		String whereClause = DatabaseHandler.FKEY_SONGID + "=? AND " +
				DatabaseHandler.KEY_NUMBER + "=?";
		String[] whereArgs = new String[] { "song_id", "channelnumber" };
		whereArgs[0] = String.valueOf(songId);
		
		int channelId = 0;
		for(Channel channel : song.getChannels()) {
			// set up where args
			whereArgs[1] = String.valueOf(channelId++);
			
			//  if channel exists (TODO or has id value?)
			// update step
			boolean updateSuccess = mDB.updateRowInTable(channelTable, values, whereClause, whereArgs);
		
		
			if(!updateSuccess) {
				// else
				// insert channel
				channelId = (int)mDB.insertRowIntoTable(channelTable , values);
			}
		
		
			// save all steps
			SaveSteps(channelId, channel);
		}
	}
	
	/**
	 * Saves a song to the database, does all the dirty set up 
	 * 
	 * @param song Song to save to the database
	 * @return true if success 
	 */
	private boolean SaveSong(Song song) {
		
		// does the song have a name?
		if(song.getName() == null || song.getName().length() == 0) {
			// no? ehm. fail the save. cannot save noname (woundn't know how to load it and it would be forever lost in the dark abyss that is SQLite
			return false;
		}
	
		// set song table name
		String songTableName = DatabaseHandler.TABLE_SONG; 
		
		// set up insert query
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, song.getName());
		
		// set up update query
		int songId = 0;
	
		String whereClause = DatabaseHandler.KEY_NAME + "=?";
		String[] whereArgs = new String[] { song.getName() };
		
		// if song exists, update song
		boolean updateSuccess = mDB.updateRowInTable(songTableName, values, whereClause, whereArgs); 


		if(!updateSuccess) {
			// else insert song
			songId = (int)mDB.insertRowIntoTable(songTableName, values);
		}
		
		// save all channels
		SaveChannels(songId, song);
		
		// returns true if all went well? -_- wat
		return true;
	}
	
	private boolean SaveSounds() {

		if(mSounds != null && mSounds.size() > 0) {
			// if the sounds are loaded and there are sounds in the list
			
			// set up the sound table name
			String soundTable = DatabaseHandler.TABLE_SOUND;
			
			// set up insert query
			ContentValues values = new ContentValues();
			
			// set up update query TODO fix string wheres
			String whereClause = DatabaseHandler.KEY_NAME + "='?'";
			String[] whereArgs =  new String[1];
			
			int soundId = 0;
			for(Sound sound : mSounds) {
				// for all sounds
				int id = sound.GetId();
				String name = sound.GetName();
				int soundValue = sound.GetSoundValue();

				values.put(DatabaseHandler.KEY_ID, id);
				values.put(DatabaseHandler.KEY_NAME, name);
				values.put(DatabaseHandler.KEY_SOUNDVALUE, soundValue);
				
				whereArgs[0] = name;
				
				// if sound is in database, update sound
				boolean updatedSuccess = mDB.updateRowInTable(soundTable, values, whereClause, whereArgs);

				if(!updatedSuccess) {
					// else insert sound
					soundId = (int)mDB.insertRowIntoTable(soundTable, values);
					
					sound.SetId(soundId);
				}
				
				// do something with sound id..?
			}
			
			
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
