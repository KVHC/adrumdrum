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
 * Helper database class for the Channel.
 * Contains the Channel table structure and creation of the 
 * table in the database.
 * 
 * @author kvhc
 */
public class ChannelSQLiteHelper extends SQLiteOpenHelper {

	// Table name
	public static final String TABLE_CHANNEL = "channels";
	
	// Table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_VOLUME = "volume";
	public static final String COLUMN_LEFTPAN = "leftpan";
	public static final String COLUMN_RIGHTPAN = "rightpan";
	public static final String COLUMN_MUTE = "mute";
	
	// Foreign keys
	public static final String FKEY_SONGID= "song_id";
	public static final String FKEY_SOUNDID = "sound_id";
	
	/**
	 * Column enumerator which contains the column index corresponding to if you select * channels (hopefully).
	 * 
	 * @author kvhc
	 */
	public static enum Columns {
		ID  (0),
		Number (1),
		Volume (2),
		LeftPanning (3),
		RightPanning (4),
		Mute (5),
		SongId (6),
		SoundId (7);
		
		private final int index;
		
	    /**
	     * Enumerator column index constructor.
	     * @param index The position of the Column in the database.
	     */
		Columns(int index) {
	        this.index = index;
	    }
		
		/**
		 * Returns the enumerator index.
		 * @return The The position of the Column in the database.
		 */
	    public int index() { 
	        return index; 
	    }
	};
	
	// Database properties
	private static final String DATABASE_NAME = "adrumdrum.db";
	private static final int DATABASE_VERSION = 1;
	
	// Table creation string
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CHANNEL + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_NUMBER + " integer not null, " 
			+ COLUMN_LEFTPAN + " double not null, " 
			+ COLUMN_RIGHTPAN + " double not null, " 
			+ COLUMN_VOLUME + " double not null, " 
			+ COLUMN_MUTE + " integer not null, "
			+ FKEY_SONGID + " integer, " 
			+ FKEY_SOUNDID + " integer"
			+"); commit;";
    
	/**
	 * Constructor.
	 * @param context
	 */
	public ChannelSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	/**
	 * What to do then an instance are created
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/**
	 * What to do then the database are uppgraded
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ChannelSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
		onCreate(db);
	}
}
