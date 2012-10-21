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

import kvhc.models.Sound;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DAO for Sound model.
 * CRUD interface.
 * 
 * @author kvhc
 *
 */
public class SoundDataSource {

	// Members.
	private SQLiteDatabase database;
	private SoundSQLiteHelper dbHelper;
	private String[] allColumns = { 
			SoundSQLiteHelper.COLUMN_ID, 
			SoundSQLiteHelper.COLUMN_SOUNDVALUE,
			SoundSQLiteHelper.COLUMN_NAME };
	private boolean mIsOpen;
	
	/**
	 * Default Constructor, needs the context to be able to use the database. 
	 * @param context Current context, needed for database usage.
	 */
	public SoundDataSource(Context context) {
		dbHelper = new SoundSQLiteHelper(context);
		mIsOpen = false;
	}
	
	/**
	 * Opens the database for transactions.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		mIsOpen = true;
	}
	
	/**
	 * Closes the database connection, which means that no database transactions
	 * can take place. 
	 */
	public void close() {
		dbHelper.close();
		mIsOpen = false;
	}
	
	/**
	 * Creates a sound in the database and returns the id for the sound.
	 * 
	 * @param soundValue The sound "value". A member of Sound.
	 * @param name The name of the sound.
	 * @return The id resulting from the insertion into the database.
	 */
	private long createSound(int soundValue, String name) {
		// Create value table.
		ContentValues values = new ContentValues();
		// Fill value table.
		values.put(SoundSQLiteHelper.COLUMN_SOUNDVALUE, soundValue);
		values.put(SoundSQLiteHelper.COLUMN_NAME, name);
		// Run insertion query.
		long insertId = database.insert(SoundSQLiteHelper.TABLE_SOUND, null, values);
		// Return sound id from insertion query.
		return insertId;
	}
	
	/**
	 * Deletes a sound from the database.
	 * @param sound Sound to be deleted.
	 * @throws SQLException if database connection not opened.
	 */
	public void deleteSound(Sound sound) throws SQLException {
		if(!isOpened()) {
			throw new SQLException("Database connection not open.");
		}
		// Get the sound id.
		long id = sound.getId();
		// Delete sound.
		database.delete(SoundSQLiteHelper.TABLE_SOUND, SoundSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Creates a list of all the sounds in the database.
	 * @return A list of all the sounds in the database.
	 * @throws SQLException if database connection not opened.
	 */
	public List<Sound> getAllSounds() throws SQLException {
		if(!isOpened()) {
			throw new SQLException("Database connection not open.");
		}
		// Create sound list.
		List<Sound> sounds = new ArrayList<Sound>();
		// Run query (Nothing to set up).
		Cursor cursor = database.query(SoundSQLiteHelper.TABLE_SOUND, allColumns, null, null, null, null, null);
		// Fill list with sounds.
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Sound sound = cursorToSound(cursor);
			sounds.add(sound);
			cursor.moveToNext();
		}
		cursor.close();
		// Return sounds.
		return sounds;
	}
	
	/**
	 * Converts the row the cursor is pointing to into a Sound.  
	 * @param cursor Database cursor pointing to a row in the Sound table. 
	 * @return The Sound the cursor was pointing to.
	 */
	private Sound cursorToSound(Cursor cursor) {
		// Create sound.
		Sound sound = new Sound(cursor.getInt(SoundSQLiteHelper.Columns.SoundValue.index()),cursor.getString(SoundSQLiteHelper.Columns.Name.index()));
		// Set properties.
		sound.setId((int)cursor.getLong(SoundSQLiteHelper.Columns.ID.index()));
		// Return sound.
		return sound;
	}

	/**
	 * Get a Sound based on the Sound ID.
	 * @param soundId The ID of the sound.
	 * @return The Sound with the id soundId.
	 * @throws SQLException if database connection not opened.
	 */
	public Sound getSoundFromKey(long soundId) throws SQLException {
		if(!isOpened()) {
			throw new SQLException("Database connection not open.");
		}
		// Set up selection query.
		String where = SoundSQLiteHelper.COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(soundId) };
		// Run query.
		Cursor cursor = database.query(SoundSQLiteHelper.TABLE_SOUND, allColumns, where, whereArgs, null,null,null);
		// Get the sound.
		cursor.moveToFirst();
		Sound sound = cursorToSound(cursor);
		cursor.close();
		// Return the sound.
		return sound;
	}

	/**
	 * Saves a sound to the database, if the sound doesn't have an id, it will create 
	 * and id for it and set it. 
	 * @param sound Sound to save to the database
	 * @throws SQLException if database connection not opened.
	 */
	public void save(Sound sound) throws SQLException {
		if(!isOpened()) {
			throw new SQLException("Database connection not open.");
		}
		// Does the sound exist?
		if (sound.getId() > 0) {
			// Yes, update!
			// Create value table.
			ContentValues values = new ContentValues();
			// Fill value table.
			values.put(SoundSQLiteHelper.COLUMN_SOUNDVALUE, sound.getSoundValue());
			values.put(SoundSQLiteHelper.COLUMN_NAME, sound.getName());
			// Set up the query.
			String where = SoundSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(sound.getId()) };
			// Run query.
			database.update(SoundSQLiteHelper.TABLE_SOUND, values, where, whereArgs);
		} else {
			// No, create it!
			long newSoundId = createSound(sound.getSoundValue(), sound.getName()); 
			sound.setId(newSoundId);
		}
	}

	/**
	 * Returns true if the database connection is open.
	 * @return true if the database connection is open.
	 */
	public boolean isOpened() {
		return mIsOpen;
	}
}
