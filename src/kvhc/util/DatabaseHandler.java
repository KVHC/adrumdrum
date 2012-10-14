package kvhc.util;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.Step;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	
	 // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
 // Database Name
    public static final String DATABASE_NAME = "ADrumDrumDatabase";
 
    // Song table name
    public static final String TABLE_SONG = "songs";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    
    
    // Channel table name
    public static final String TABLE_CHANNEL = "channels";
    public static final String FKEY_SONGID = "song_id";
    public static final String FKEY_SOUNDID = "sound_id";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_LEFTPAN = "left_panning";
    public static final String KEY_RIGHTPAN = "right_panning";
    
    
    // Sound table name
    public static final String TABLE_SOUND = "sounds";
    public static final String KEY_SOUNDVALUE = "sound_value";
    
    
    // Step table name
    public static final String TABLE_STEP = "steps";
    public static final String FKEY_CHANNELID = "channel_id";
    public static final String KEY_VELOCITY = "velocity";
    public static final String KEY_ACTIVE = "is_active";

	public static final String KEY_NUMBER = "number";
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
 // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        
    	/**
    	 * Song
    	 *  int id,
    	 *  string name
    	 */
    	String CREATE_SONG_TABLE = "CREATE TABLE " + TABLE_SONG + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY," 
    			+ KEY_NAME + " TEXT)";
        
    	/**
    	 * Channel
    	 *   
    	 *   int id
    	 *   int number (channel number in song)
    	 *   float volume
    	 *   float leftPan
    	 *   float rightPan
    	 *   fkey int soundId
    	 *   fkey int songId
    	 */
        String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_CHANNEL + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NUMBER + " INTEGER,"
                + KEY_VOLUME + " DOUBLE,"
                + KEY_LEFTPAN + " DOUBLE,"
                + KEY_RIGHTPAN + " DOUBLE,"
        		+ FKEY_SONGID + " INTEGER,"
        		+ FKEY_SOUNDID + " INTEGER,"
        		+ KEY_NAME + " TEXT)";
        
        /**
         * Sound
         *   
         *   pk int id
         *   int soundValue (or string?)
         *   string name
         * 
         */
        String CREATE_SOUND_TABLE = "CREATE TABLE " + TABLE_SOUND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ FKEY_SONGID + " INTEGER,"
        		+ KEY_SOUNDVALUE + " INTEGER,"
        		+ KEY_NAME + " TEXT)";
        
        /**
         * Step
         * 	int id; 
         *  float velocity;
         *  int active - 0 false, else true
         *  int number
         *  int fkey channelId
         */
        String CREATE_STEP_TABLE =  "CREATE TABLE " + TABLE_STEP + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VELOCITY + " DOUBLE," 
                + KEY_ACTIVE + " INTEGER,"
                + KEY_NUMBER + " INTEGER"
                + FKEY_CHANNELID + " INTEGER)";
        
        
        db.execSQL(CREATE_SONG_TABLE);
        db.execSQL(CREATE_CHANNEL_TABLE);
        db.execSQL(CREATE_SOUND_TABLE);
        db.execSQL(CREATE_STEP_TABLE);
        
    }
    
 // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Updates a row in a specified table, depenind on the where claus and where args
     * @param tableName 
     * @param updatedValues
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean updateRowInTable(String tableName, ContentValues updatedValues, String whereClause, String[] whereArgs) {
    	
    	// Gets a database 
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// Update the table
    	int rowsAffected = db.update(tableName, updatedValues, whereClause, whereArgs);
    	
    	if(rowsAffected != 1) {
    		// We mostly want to update a row at a time when we update so 
    		// if it's not one returned then something 
    		// might be wrong
    		
    		// TODO test this, it might be stupid and lead to bugs
    		
    		// we do nothing about it
    		
    		// return the function as unsuccessful 
    		return false;
    	}
    	
    	// returns great success
    	return true;
    }
    
    /**
     * Inserts a row into a table, doesn't check for valid keys
     * @param tableName the table name
     * @param values The key value pairs for the row
     * @return the returned id or row number (not sure)
     */
    public long insertRowIntoTable(String tableName, ContentValues values) {
    	// Get the database
    	SQLiteDatabase db = this.getWritableDatabase();
   	 
        // Inserting Row and returning id
        return db.insert(tableName, null, values);
    }
    
	/**
	 * Returns the rows from a table with the args... args. 
	 * @param tableName 
	 * @param args
	 */

	public Cursor getCursorForTable(String tableName, String[] columns, String where, String[] values) {
		// Get a database to load from 
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Gets cursor for arguments 
		Cursor tableCursor = db.query(tableName, columns, where, values, null,null,null,null);
		
		// Return it 
		return tableCursor;
	}

}
