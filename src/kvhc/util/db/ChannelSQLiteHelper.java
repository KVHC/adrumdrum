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
 * Helper class to save a Channel to database.
 * 
 * @author kvhc
 */
public class ChannelSQLiteHelper extends SQLiteOpenHelper {

	// Strings for Database
	public static final String TABLE_CHANNEL = "channels";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_VOLUME = "volume";
	public static final String COLUMN_LEFTPAN = "leftpan";
	public static final String COLUMN_RIGHTPAN = "rightpan";
	public static final String FKEY_SONGID= "song_id";
	public static final String FKEY_SOUNDID = "sound_id";
	
	private static final String DATABASE_NAME = "adrumdrum.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CHANNEL + "(" + COLUMN_ID 
			+ " integer primary key autoincrement, " + COLUMN_NUMBER
			+ " integer not null, " + COLUMN_LEFTPAN 
			+ " double not null, " + COLUMN_RIGHTPAN
			+ " double not null, " + COLUMN_VOLUME
			+ " double not null," + FKEY_SONGID
			+ " integer," + FKEY_SOUNDID
			+ " integer"
			+"); commit;";
    
	/**
	 * Constructor.
	 * @param context
	 */
	public ChannelSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SoundSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
		onCreate(db);
	}
}
