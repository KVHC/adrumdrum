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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper database class for the Song model.
 * Contains the Song table structure and creation of the 
 * table in the database.
 * 
 * @author kvhc
 *
 */
public class SongSQLiteHelper extends SQLiteOpenHelper {

	// Table name.
	public static final String TABLE_SONG = "songs";
	
	// Table columns.
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_BPM = "bpm";
	
	// Database info.
	private static final String DATABASE_NAME = "adrumdrumsongs.db";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * Column enumerator which contains the column index corresponding to if you select * songs (hopefully).
	 * 
	 * @author kvhc
	 */
	public static enum Columns {
		ID  (0),
		Name (1),
		BPM (2);
		
		private final int index;
		
	    /**
	     * Enumerator column index constructor.
	     * @param index The position of the column in the database table songs.
	     */
		Columns(int index) {
	        this.index = index;
	    }
		
		/**
		 * Returns the enumerator index.
		 * @return The The position of the column in the database table songs.
		 */
	    public int index() { 
	        return index; 
	    }
	};
	
	// Table creation string.
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SONG + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "  
			+ COLUMN_NAME + " text, "
			+ COLUMN_BPM + " integer not null"
			+ ");";
	
	/**
	 * Constructor.
	 * @param context Context needed for the database usage.
	 */
	public SongSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SongSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
		onCreate(db);
	}
}
