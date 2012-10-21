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
 * Helper class to save a Step to database.
 * Contains the information about the Step table structure and 
 * creates the table if it doesn't exist upon initialization. 
 * 
 * @author kvhc
 */
public class StepSQLiteHelper extends SQLiteOpenHelper {

	// Table name
	public static final String TABLE_STEP = "songs";
	
	// Table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_VELOCITY = "velocity";
	
	// Foreign keys
	public static final String FKEY_CHANNELID = "channel_id";

	// Database operations
	public static final String EQUALS_TO_PARAMETER = "=?";
	public static final String EQUALS_TO = "=";
	
	
	// Database info
	private static final String DATABASE_NAME = "adrumdrum.db";
	private static final int DATABASE_VERSION = 1;
	

	
	/**
	 * Column enumerator which contains the column index corresponding to if you select * channels (hopefully).
	 * 
	 * @author kvhc
	 */
	public static enum Columns {
		ID  (0),
		Velocity (1),
		Active (2),
		Number(3), 
		ChannelId (4);
		
		private final int index;
		
	    /**
	     * Enumerator column index constructor.
	     * @param index The position of the column in the database table channels.
	     */
		Columns(int index) {
	        this.index = index;
	    }
		
		/**
		 * Returns the enumerator index.
		 * @return The The position of the column in the database table channels.
		 */
	    public int index() { 
	        return index; 
	    }
	};
	
	// Table creation string.
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_STEP + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "  
			+ COLUMN_VELOCITY + " double not null,"
			+ COLUMN_ACTIVE + " integer not null,"
			+ COLUMN_NUMBER + " integer not null,"
			+ FKEY_CHANNELID + " integer not null"
			+ "); commit;";
    /**
     * Step
     * 	int id; 
     *  float velocity;
     *  int active - 0 false, else true
     *  int number
     *  int fkey channelId
     */
    
	/**
	 * Constructor.
	 * @param context Needs context for use of database.
	 */
	public StepSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(StepSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
		onCreate(db);
	}
}
