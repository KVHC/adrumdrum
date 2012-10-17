package kvhc.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	
	 // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    public static final String DATABASE_NAME = "adrumdrum.db";
    
    // Database Helpers
    SoundSQLiteHelper soundHelper;
    SongSQLiteHelper songHelper;
    ChannelSQLiteHelper channelHelper;
    StepSQLiteHelper stepHelper;
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        soundHelper = new SoundSQLiteHelper(context);
        songHelper  = new SongSQLiteHelper(context);
        channelHelper = new ChannelSQLiteHelper(context);
        stepHelper = new StepSQLiteHelper(context);        
    }
    
    
    
 // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
		soundHelper.onCreate(db);
		songHelper.onCreate(db);
        channelHelper.onCreate(db);
        stepHelper.onCreate(db);
    }
    
 // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
    	soundHelper.onUpgrade(db, oldVersion, newVersion);
		songHelper.onUpgrade(db, oldVersion, newVersion);
        channelHelper.onUpgrade(db, oldVersion, newVersion);
        stepHelper.onUpgrade(db, oldVersion, newVersion);
    }
}
