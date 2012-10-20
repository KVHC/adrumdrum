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
import android.util.Log;

/**
 * DataSource object for Song model
 * CRUD operations
 *  
 * @author kvhc
 */
public class SongDataSource {
	private SQLiteDatabase database;
	private SongSQLiteHelper dbHelper;
	private String[] allColumns = { SongSQLiteHelper.COLUMN_ID, SongSQLiteHelper.COLUMN_NAME };
	
	/**
	 * Default Constructor, needs the context to be able to use the db. 
	 * @param context
	 */
	public SongDataSource(Context context) {
		dbHelper = new SongSQLiteHelper(context);
	}
	
	/**
	 * Opens the db for transactions.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the db connection.
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Creates a new Song in the db.
	 * @param name The name of the Song.
	 * @return
	 */
	public Song createSong(String name) {
		ContentValues values = new ContentValues();
		values.put(SongSQLiteHelper.COLUMN_NAME, name);
		Song newSong = null;
		try {
			open();
			long insertId = database.insert(SongSQLiteHelper.TABLE_SONG, null, values);
			Log.w("DERP", "insertid: " + insertId);
			if (insertId >= 0) {
				Cursor cursor = database.query(SongSQLiteHelper.TABLE_SONG, allColumns, SongSQLiteHelper.COLUMN_ID + " = " 
							+ insertId, null, null, null, null);
			
				cursor.moveToFirst();
				newSong = cursorToSong(cursor);
				cursor.close();
				
			}
		} catch(SQLException e) {
			Log.e(getClass().toString(), e.toString());
		} finally {
			close();
		}
		
		return newSong;
	}
	
	/**
	 * Saves a Song to the db, if it doesn't exist it's created. 
	 * 
	 * @param song The song to save. 
	 * @return
	 */
	public Song save(Song song) {
		ContentValues values = new ContentValues();
		values.put(SongSQLiteHelper.COLUMN_NAME, song.getName());
	
		if (song.getId() > 0) {
			// Has id
			String where = SongSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(song.getId() )};
			
			open();
			database.update(SongSQLiteHelper.TABLE_SONG, values, where, whereArgs);
			close();
		} else {
			// doesn't have id
			song = createSong(song.getName());
		}
		
		if (song == null) {
			Log.e("SongDataSource", "NULL SONG");
			return null;
		}
		
		return song;
	}
	
	/**
	 * Deletes a song from database.
	 * @param song song to be deleted
	 */
	public void deleteSong(Song song) {
		long id = song.getId();
		
		// Delete sound
		database.delete(SongSQLiteHelper.TABLE_SONG, SongSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Returns a list of all songs.
	 * @return a list of all songs.
	 */
	public List<Song> getAllSongs() {
		List<Song> songs = new ArrayList<Song>();
		
		Cursor cursor = database.query(SongSQLiteHelper.TABLE_SONG, allColumns, null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Song song = cursorToSong(cursor);
			songs.add(song);
			cursor.moveToNext();
		}
		
		cursor.close();
		return songs;
	}
	
	/**
	 * Get the Song pointed to by the Cursor.
	 * @param cursor
	 * @return Song pointed to by the Cursor
	 */
	private Song cursorToSong(Cursor cursor) {
		
		Song song = new Song(4);
		song.setId(cursor.getLong(0));
		song.setName(cursor.getString(1));
		
		return song;
	}
}
