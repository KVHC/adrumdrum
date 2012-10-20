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

import kvhc.models.Song;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DataSource object for Song model.
 * CRUD operations.
 * 
 * @author kvhc
 */
public class SongDataSource {
	
	// Properties
	private SQLiteDatabase database;
	private SongSQLiteHelper dbHelper;
	private String[] allColumns = { SongSQLiteHelper.COLUMN_ID, SongSQLiteHelper.COLUMN_NAME };
	
	/**
	 * Default Constructor, needs the context to be able to use the database. 
	 * @param context Current context, needed for database usage.
	 */
	public SongDataSource(Context context) {
		dbHelper = new SongSQLiteHelper(context);
	}
	
	/**
	 * Opens the database for transactions.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the database connection.
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Creates a new Song in the database.
	 * 
	 * @param name The name of the Song.
	 * @return The ID of the song in the database.
	 */
	private long createSong(String name, int bpm) {
		
		// Create value table.
		ContentValues values = new ContentValues();
		
		// Fill value table.
		values.put(SongSQLiteHelper.COLUMN_NAME, name);
		values.put(SongSQLiteHelper.COLUMN_BPM, name);
		
		// Run query
		long insertId = database.insert(SongSQLiteHelper.TABLE_SONG, null, values);
		
		// Return the newly inserted song's id.
		return insertId;
	}
	
	/**
	 * Saves a Song to the database, if it doesn't exist an id is set on the
	 * object.  
	 * 
	 * @param song The song to save. 
	 * @return Nothing.
	 */
	public void save(Song song) {
		
		// Does the song have an id?	
		if (song.getId() > 0) {
			// Yes, which means it's in the database. Update.
			
			// Create value table.
			ContentValues values = new ContentValues();
			values.put(SongSQLiteHelper.COLUMN_NAME, song.getName());
			values.put(SongSQLiteHelper.COLUMN_BPM, song.getBpm());
			
			// Set up update query.
			String where = SongSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(song.getId() )};
			
			// Run query.
			database.update(SongSQLiteHelper.TABLE_SONG, values, where, whereArgs);
		} else {
			// No, it's not the database.
			long newSongId = createSong(song.getName(), song.getBpm());
			song.setId(newSongId);
		}
	}
	
	/**
	 * Deletes a song from database.
	 * @param song Song to be deleted.
	 */
	public void deleteSong(Song song) {
		// Get song id.
		long id = song.getId();
		
		// Delete song.
		database.delete(SongSQLiteHelper.TABLE_SONG, SongSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Returns a list of all songs in the database.
	 * @return a list of all songs.
	 */
	public List<Song> getAllSongs() {
		
		// Create list for the songs.
		List<Song> songs = new ArrayList<Song>();
		
		// Run selection query.
		Cursor cursor = database.query(SongSQLiteHelper.TABLE_SONG, allColumns, null,null,null,null,null);
		
		// Fill list with songs.
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Song song = cursorToSong(cursor);
			songs.add(song);
			cursor.moveToNext();
		}
		cursor.close();
		
		// Return the song list.
		return songs;
	}
	
	/**
	 * Creates a Song based on the row the database cursor is pointing to.
	 * @param cursor Cursor pointing at a row in the Songs table. 
	 * @return Song pointed to by the Cursor.
	 */
	private Song cursorToSong(Cursor cursor) {
		
		// Create song.
		Song song = new Song(4);
		
		// Set up properties.
		song.setId(cursor.getLong(0));
		song.setName(cursor.getString(1));
		song.setBpm(cursor.getInt(2));
		
		// Return song object.
		return song;
	}
}
