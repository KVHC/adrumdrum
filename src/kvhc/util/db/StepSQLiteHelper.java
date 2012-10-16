package kvhc.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StepSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_STEP = "songs";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_VELOCITY = "velocity";
	public static final String FKEY_CHANNELID = "channel_id";
	
	private static final String DATABASE_NAME = "adrumdrum.db";
	private static final int DATABASE_VERSION = 1;
	
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
    
	public StepSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SoundSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion +  ", which will destroy all data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
		onCreate(db);
	}
}
