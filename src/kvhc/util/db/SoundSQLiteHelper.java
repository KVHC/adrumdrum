package kvhc.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SoundSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SOUND = "sounds";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SOUNDVALUE = "sound_value";
	public static final String COLUMN_NAME = "name";
	
	private static final String DATABASE_NAME = "adrumdrum.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SOUND + "(" + COLUMN_ID 
			+ " integer primary key autoincrement, " + COLUMN_SOUNDVALUE
			+ " integer not null, " + COLUMN_NAME 
			+ " text not null);";
	
	
	public SoundSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SoundSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND);
		onCreate(db);
	}
}
