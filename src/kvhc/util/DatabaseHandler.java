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
     * Enters a song into the database
     * @param song
     */
    public void addSong(Song song) {
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, song.getName()); 
     
        // Inserting Row
        long songId = db.insert(TABLE_SONG, null, values);
        
        values.clear();
        int channelId = 0;
        for(Channel c : song.getChannels()) {
        	
        	int soundId = c.getSound().GetId();
        	// Add channel sound
        	values.put(KEY_ID, soundId);
        	values.put(KEY_NAME, c.getSound().GetName());
        	values.put(FKEY_SONGID, songId);
        	values.put(KEY_SOUNDVALUE, c.getSound().GetSoundValue());
        	
        	db.insert(TABLE_SOUND, null, values);
        	values.clear();
        	        	
        	// Add channel
        	values.put(FKEY_SONGID, songId);
        	values.put(KEY_ID, channelId);
        	values.put(KEY_VOLUME, c.getVolume());
        	values.put(KEY_LEFTPAN, c.getLeftPanning());
        	values.put(KEY_RIGHTPAN, c.getRightPanning());
        	
        	db.insert(TABLE_CHANNEL, null, values);
        	values.clear();
        	
        	int i = 0;
        	for(Step step : c.getSteps()) {
        		values.put(KEY_ID, i++);
        		values.put(FKEY_SONGID, songId);
                values.put(KEY_VELOCITY, step.getVelocity());
                values.put(KEY_ACTIVE, step.isActive());
                values.put(FKEY_CHANNELID, channelId);
                
                db.insert(TABLE_STEP, null, values);
        	}
        }
       
        db.close(); // Closing database connection
    	
    }
    
    public void updateSong(Song song) {
    	
    }
    
    public void deleteSong(Song song) {
    	
    }
    
    /**
     * Returns song by it's ID, loaded from db.
     * @param id
     * @return
     */
    private Song getSongById(int id) {
    	//Song song = new Song();
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor songCursor = db.query(TABLE_SONG, new String[] { KEY_ID,
                KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (songCursor == null)
        	return null;
        
        songCursor.moveToFirst();
     
        Cursor channelCursor = db.query(TABLE_CHANNEL, new String[] {
        		KEY_ID, FKEY_SONGID, FKEY_SOUNDID, KEY_VOLUME, KEY_LEFTPAN, KEY_RIGHTPAN }, 
        		FKEY_SONGID + "=?", new String[] { String.valueOf(id) }, 
        		null, null, null, null);
        
        
        if(channelCursor == null)
        	return null;
        
        channelCursor.moveToFirst();
        
        Song song = new Song(channelCursor.getCount());
        
        for(int i = 0; i < channelCursor.getCount(); i++) {
        	
        	// Create a stepCursor
        	Cursor stepCursor = db.query(TABLE_STEP, new String[] {
        			KEY_ID, KEY_VELOCITY, KEY_ACTIVE
        	}, FKEY_SONGID + "=? AND " + FKEY_CHANNELID + "=?", 
        	new String[] { String.valueOf(id), channelCursor.getString(0) },
        	null,null,null,null); 
        	
        	if(stepCursor  == null) {
        		continue;
        	}
        	
        	stepCursor.moveToFirst();
        	
        	Cursor soundCursor = db.query(TABLE_SOUND, new String[] {
        			KEY_NAME, KEY_SOUNDVALUE
        	}, FKEY_SONGID + "? AND " + KEY_ID + "=?", new String[] { String.valueOf(id), channelCursor.getString(3) },
        	null,null,null,null);
        	
        	if(soundCursor == null) {
        		continue;
        	}
        	
        	soundCursor.moveToFirst();
        	
        	Sound s = new Sound(channelCursor.getInt(3), soundCursor.getInt(1), soundCursor.getString(0));
        	
        	Channel c = new Channel(s, stepCursor.getCount());
        	
        	c.setPanning((float)channelCursor.getDouble(5), (float)channelCursor.getDouble(4));
        	c.setVolume((float)channelCursor.getDouble(3));
        	
        	while(stepCursor.moveToNext()) {
        		c.setStep(stepCursor.getInt(0), stepCursor.getInt(2) > 0, (float)stepCursor.getDouble(1));
        	}
        	
        	song.addChannel(c);
        }
        
        
        // return contact
        return song;
    	
    }

    /**
     * Checks the database for a Song with the name of the parameter name,
     * if it exists it is loaded and returned!
     * @param name Name of the song in the db
     * @return Null if no song exists by that name, otherwise the song
     */
	public Song getSongByName(String name) {
		// TODO Comment to know what's going on
		SQLiteDatabase db = this.getReadableDatabase();
   	 
        Cursor songCursor = db.query(TABLE_SONG, new String[] { KEY_ID,
                KEY_NAME }, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);
        if (songCursor == null)
        	return null;
        
        songCursor.moveToFirst();
        
        int songId = songCursor.getInt(0);
		
		return getSongById(songId);
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
