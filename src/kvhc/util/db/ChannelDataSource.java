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

package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.models.Sound;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Channel object DAO.
 * 
 * @author kvhc
 */
public class ChannelDataSource {
	private SQLiteDatabase database;
	private ChannelSQLiteHelper dbHelper;
	
	private String[] allColumns = { ChannelSQLiteHelper.COLUMN_ID , 
			ChannelSQLiteHelper.COLUMN_NUMBER, 
			ChannelSQLiteHelper.COLUMN_VOLUME, 
			ChannelSQLiteHelper.COLUMN_LEFTPAN, 
			ChannelSQLiteHelper.COLUMN_RIGHTPAN, 
			ChannelSQLiteHelper.COLUMN_MUTE, 
			ChannelSQLiteHelper.FKEY_SONGID, 
			ChannelSQLiteHelper.FKEY_SOUNDID };
	private boolean mIsOpen;	

	/**
	 * The only constructor available, needs context for using the database.
	 * @param context Current context, needed for database usage.
	 */
	public ChannelDataSource(Context context) {
		dbHelper = new ChannelSQLiteHelper(context);
		mIsOpen = false;
	}
	
	/**
	 * Opens the DB for usage.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		mIsOpen = true;
	}
	
	/**
	 * Closes the database.
	 */
	public void close() {
		dbHelper.close();
		mIsOpen = false;
	}
	
	/**
	 * Adds a Channel to the database based on the parameters.  
	 * 
	 * It's not created if the input song is null or doesn't have a database id.
	 * If the sound is null or not in the database the sound is just set to null and it 
	 * will be added. 
	 * 
	 * @param song - Song the Channel belongs to.
	 * @param sound - Sound that belongs to the channel.
	 * @param volume - The Channel volume.
	 * @param rightPan - Amount of panning to the right.
	 * @param leftPan - Amount of panning to the left.
	 * @param number - The position of the step in the sequence.
	 * @param mute = If the channel is muted or not. 
	 * @return
	 */
	private long createChannel(Song song, Sound sound, float volume, float rightPan, float leftPan, int number, boolean mute) { 
		// Does the song exist?
		if(song == null || song.getId() == 0) {
			// Nope, don't add to the database.
			return 0;
		}
		
		// Set up the value table.
		ContentValues values = new ContentValues();
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, leftPan);
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, rightPan);
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, number);
		values.put(ChannelSQLiteHelper.COLUMN_MUTE, mute ? 1 : 0);
		
		// Add foreign keys.
		values.put(ChannelSQLiteHelper.FKEY_SONGID, song.getId());
		
		// Does the sound exist?
		if(sound != null && sound.getId() != 0) {
			// Yes, add it's ID to the value table.
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, sound.getId());
		} else {
			// No, set the sound id to 0.
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, -1);
		}
		
		// Insert into database.
		long insertId = database.insert(ChannelSQLiteHelper.TABLE_CHANNEL, null, values);
		
		// Return the id.
		return insertId;
	}
	
	/**
	 * Deletes a channel from the database.
	 * @param channel The channel to delete.
	 * @throws SQLException when there is no connectino to the database.
	 * @return the number of rows deleted.
	 */
	public int deleteChannel(Channel channel) throws SQLException {
		if(!mIsOpen) {
			throw new SQLException("No database connection.");
		}
		// Get the id for the channel.
		long id = channel.getId();
		channel.setId(0);
		// Delete channel.
		return database.delete(ChannelSQLiteHelper.TABLE_CHANNEL, ChannelSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Returns a list of channels for the specified channel (the song needs to be in the 
	 * database).
	 * 
	 * If the song is null or not in the database, the method will return an empty list
	 * of channels. 
	 * 
	 * @param song Song to which the Channels belongs. 
	 * @return A list of the specified Song's channels.
	 * @throws 
	 */
	public List<Channel> getAllChannelsForSong(Song song) throws SQLException {
		if(!mIsOpen) {
			throw new SQLException("No database connection.");
		}
		// Create a list of channels
		List<Channel> channels = new ArrayList<Channel>();
		// Set up the query.
		String where = ChannelSQLiteHelper.FKEY_SONGID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(song.getId()) };
		String orderBy = ChannelSQLiteHelper.COLUMN_NUMBER + " ASC";
		// Run query.
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, where, whereArgs, 
				null,null, orderBy);
		Log.w("ChannelDataSource", "Number of Channels found: " + cursor.getCount());
		// Fill the list.
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Channel channel = cursorToChannel(cursor);
			channels.add(channel);
			cursor.moveToNext();
		}
		cursor.close();
		// Return channel list.
		return channels;
	}
	
	/**
	 * Turns the row the cursor is pointing to into a new Channel object.
	 * 
	 * Does not add the steps.
	 * Adds a sound, but only with the ID (nothing else).
	 *  
	 * @param cursor Database cursor that points to a row in the Channel table.
	 * @return A created channel.
	 */
	private Channel cursorToChannel(Cursor cursor) {
		// Create a new channel.
		Channel channel = new Channel();
		// Get the foreign keys.
		long channelId = cursor.getLong(ChannelSQLiteHelper.Columns.ID.index());
		long soundId = cursor.getLong(ChannelSQLiteHelper.Columns.SoundId.index());
		// Set the properties.
		channel.setId(channelId);
		channel.setPanning(
				(float)cursor.getDouble(ChannelSQLiteHelper.Columns.RightPanning.index()), 
				(float)cursor.getDouble(ChannelSQLiteHelper.Columns.LeftPanning.index()));
		channel.setVolume((float)cursor.getDouble(ChannelSQLiteHelper.Columns.Volume.index())); 
		channel.setChannelNumber(cursor.getInt(ChannelSQLiteHelper.Columns.Number.index()));
		channel.setMute(cursor.getInt(ChannelSQLiteHelper.Columns.Mute.index()) == 1);
		// Set up foreign key objects.
		channel.setSteps(null);
		channel.setSound(new Sound(soundId));
		// Return new channel.
		return channel;
	}

	/**
	 * Saves a channel to a song, if the channel doesn't exists it is created.
	 * The Channel needs an for ID for the Channel to update.
	 * 
	 * Also, if the song is null or not in the database the Channel
	 * won't be saved.
	 * 
	 * @param song Song the channel belongs to.
	 * @param channel The Channel to save.
	 */
	public void save(Song song, Channel channel) {
		// Does the song exist? Does it exist in the database?
		if(song == null || song.getId() == 0) {
			// Nope. Don't save anything.
			return;
		}
		// Does the channel exist?
		if(channel.getId() > 0) {
			// Yes, update.
			// Set up value table.
			ContentValues values = new ContentValues();
			values.put(ChannelSQLiteHelper.COLUMN_NUMBER, channel.getChannelNumber());
			values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, channel.getRightPanning());
			values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, channel.getLeftPanning());
			values.put(ChannelSQLiteHelper.COLUMN_VOLUME, channel.getChannelVolume());
			values.put(ChannelSQLiteHelper.COLUMN_MUTE, channel.isMuted() ? 1 : 0);
			// Add foreign keys to the value table.
			values.put(ChannelSQLiteHelper.FKEY_SONGID, song.getId());
			if(channel.getSound() != null) {
				values.put(ChannelSQLiteHelper.FKEY_SOUNDID, channel.getSound().getId());
			} else {
				values.put(ChannelSQLiteHelper.FKEY_SOUNDID, 0);
			}
			// Set up query.
			String where = ChannelSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(channel.getId() )};
			// Run query.
			database.update(ChannelSQLiteHelper.TABLE_CHANNEL, values, where, whereArgs);
		} else {
			// The Channel doesn't exist, create it. 
			long newChannelId = createChannel(song, 
					channel.getSound(), 
					channel.getVolume(), 
					channel.getRightPanning(), 
					channel.getLeftPanning(), 
					channel.getChannelNumber(), 
					channel.isMuted());
			// Set the newly obtained id to the channel.
			channel.setId(newChannelId);
		}
	}

	/**
	 * Returns true if the database is open for transactions.
	 * @return True, then you can use the data source without error.
	 */
	public boolean isOpened() {
		return mIsOpen;
	}
}
