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
	
	private String[] allColumns = { ChannelSQLiteHelper.COLUMN_ID
			, ChannelSQLiteHelper.COLUMN_NUMBER 
			, ChannelSQLiteHelper.COLUMN_VOLUME 
			, ChannelSQLiteHelper.COLUMN_LEFTPAN
			, ChannelSQLiteHelper.COLUMN_RIGHTPAN
			, ChannelSQLiteHelper.FKEY_SONGID 
			, ChannelSQLiteHelper.FKEY_SOUNDID };
	

	/**
	 * The only constructor available, needs context for using the DB.
	 * @param context
	 */
	public ChannelDataSource(Context context) {
		dbHelper = new ChannelSQLiteHelper(context);
	}
	
	/**
	 * Opens the DB for usage
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the db
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Creates a channel in the db with all the parameters needed.
	 * @param song - Song the Channel belongs to 
	 * @param sound - Sound that belongs to the channel
	 * @param volume - The Channel volume
	 * @param rightPan - Amount of panning to the right
	 * @param leftPan - Amount of panning to the left
	 * @param number - The position of the step in the sequence
	 * @return
	 */
	public Channel createChannel(Song song, Sound sound, float volume, float rightPan, float leftPan, int number) {
		ContentValues values = new ContentValues();
		values.put(ChannelSQLiteHelper.FKEY_SONGID, song.getId());
		
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, leftPan);
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, rightPan);
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, number);
		
		if(sound != null) {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, sound.getId());
		} else {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, -1);
		}
		
		long insertId = database.insert(ChannelSQLiteHelper.TABLE_CHANNEL, null, values);
		
		Log.w("ChannelData", "Insert channel id: " + insertId);
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, ChannelSQLiteHelper.COLUMN_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Channel newChannel = cursorToChannel(cursor);
		cursor.close();
		return newChannel;
	}
	
	/**
	 * Deletes a channel from the db.
	 * @param channel
	 */
	public void deleteChannel(Channel channel) {
		long id = channel.getId();
		
		// Delete sound
		database.delete(ChannelSQLiteHelper.TABLE_CHANNEL, ChannelSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Returns a list of channels for the specified channel (the song needs to be in the db already).
	 * @param song A song
	 * @return
	 */
	public List<Channel> getAllChannelsForSong(Song song) {
		List<Channel> channels = new ArrayList<Channel>();
		
		String where = ChannelSQLiteHelper.FKEY_SONGID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(song.getId()) };
		String orderBy = ChannelSQLiteHelper.COLUMN_NUMBER + " ASC";
		
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, where, whereArgs, 
				null,null, orderBy);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Channel channel = cursorToChannel(cursor);
			channels.add(channel);
			cursor.moveToNext();
		}
		cursor.close();
		
		Log.w("ChannelData", "Antal: " + channels.size());
		return channels;
	}
	
	/**
	 * Turns the row the cursor is pointing to into a new Channel object
	 * @param cursor
	 * @return
	 */
	private Channel cursorToChannel(Cursor cursor) {
		if(cursor.getCount() == 0) return null;
		
		Channel channel = new Channel();
		
		long channelId = cursor.getLong(0);
		long soundId = cursor.getLong(6);
		
		channel.setId(channelId);
		channel.setPanning((float)cursor.getDouble(4), (float)cursor.getDouble(5));
		channel.setVolume((float)cursor.getDouble(2)); 
		channel.setChannelNumber(cursor.getInt(1));
		
		channel.setSteps(null);
		channel.setSound(new Sound(soundId));
		
		return channel;
	}

	/**
	 * Saves a channel to a song, if the channel doesn't exists it is created.
	 * The Channel needs an for ID for the Channel to update.
	 * 
	 * @param song
	 * @param channel
	 */
	public void save(Song song, Channel channel) {
		
		if(song.getId() == 0) return;
		
		if(channel.getId() > 0) {
			// The Channel exists, update
			ContentValues values = new ContentValues();
			values.put(ChannelSQLiteHelper.COLUMN_NUMBER, channel.getChannelNumber());
			values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, channel.getRightPanning());
			values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, channel.getLeftPanning());
			values.put(ChannelSQLiteHelper.COLUMN_VOLUME, channel.getVolume());
		
			if(channel.getSound() != null) {
				values.put(ChannelSQLiteHelper.FKEY_SOUNDID, channel.getSound().getId());
			} else {
				values.put(ChannelSQLiteHelper.FKEY_SOUNDID, 0);
			}
		
			String where = ChannelSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(channel.getId() )};
		
			database.update(ChannelSQLiteHelper.TABLE_CHANNEL, values, where, whereArgs);
		} else {
			// The Channel doesn't exist, create it. It creates a new one, but we only want the ID since we already have the object. 
			Channel tmp = createChannel(song, channel.getSound(), channel.getVolume(), channel.getRightPanning(), channel.getLeftPanning(), channel.getChannelNumber());
			channel.setId(tmp.getId());
		}
	}
}
