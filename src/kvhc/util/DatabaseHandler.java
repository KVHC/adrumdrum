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
    private static final String DATABASE_NAME = "contactsManager";
 
    // Song table name
    private static final String TABLE_SONG = "songs";
    
    // Song Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    
    
    // Channel table name
    private static final String TABLE_CHANNEL = "channels";
    private static final String FKEY_SONGID = "song_id";
    private static final String FKEY_SOUNDID = "sound_id";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_LEFTPAN = "left_panning";
    private static final String KEY_RIGHTPAN = "right_panning";
    
    
    // Sound table name
    private static final String TABLE_SOUND = "sounds";
    private static final String KEY_SOUNDVALUE = "sound_value";
    
    // Step table name
    private static final String TABLE_STEP = "steps";
    private static final String KEY_VELOCITY = "velocity";
    private static final String FKEY_CHANNELID = "channel_id";
    private static final String KEY_ACTIVE = "is_active";
    
    
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
    	 *   int id, // NOT PRIMARY
    	 *   float volume,
    	 *   float leftPan,
    	 *   float rightPan,
    	 *   fkey int soundId
    	 *   fkey int songId
    	 */
        String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_CHANNEL + "("
                + KEY_ID + " INTEGER,"
                + KEY_VOLUME + " DOUBLE,"
                + KEY_LEFTPAN + " DOUBLE,"
                + KEY_RIGHTPAN + " DOUBLE,"
        		+ FKEY_SONGID + " INTEGER,"
        		+ FKEY_SOUNDID + " INTEGER,"
        		+ KEY_NAME + " TEXT)";
        
        /**
         * Sound
         *   
         *   int id, // NOT PRIMARY
         *   int soundValue (or string?)
         *   string name
         *   fkey int songId
         * 
         */
        String CREATE_SOUND_TABLE = "CREATE TABLE " + TABLE_SOUND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ FKEY_SONGID + " INTEGER,"
        		+ KEY_SOUNDVALUE + " INTEGER,"
        		+ KEY_NAME + " TEXT)";
        
        /**
         * Step
         * 	int id; // NOT PRIMARY!
         *  float velocity;
         *  int active - 0 false, else true
         *  int fkey songId
         *  int fkey channelId
         */
        String CREATE_STEP_TABLE =  "CREATE TABLE " + TABLE_STEP + "("
                + KEY_ID + " INTEGER,"
                + KEY_ACTIVE + " INTEGER," 
                + FKEY_SONGID + " INTEGER,"
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
        values.put(KEY_NAME, song.GetName()); 
     
        // Inserting Row
        long songId = db.insert(TABLE_SONG, null, values);
        
        values.clear();
        int channelId = 0;
        for(Channel c : song.GetChannels()) {
        	
        	int soundId = c.GetSound().GetId();
        	// Add channel sound
        	values.put(KEY_ID, soundId);
        	values.put(KEY_NAME, c.GetSound().GetName());
        	values.put(FKEY_SONGID, songId);
        	values.put(KEY_SOUNDVALUE, c.GetSound().GetSoundValue());
        	
        	db.insert(TABLE_SOUND, null, values);
        	values.clear();
        	        	
        	// Add channel
        	values.put(FKEY_SONGID, songId);
        	values.put(KEY_ID, channelId);
        	values.put(KEY_VOLUME, c.GetVolume());
        	values.put(KEY_LEFTPAN, c.GetLeftPanning());
        	values.put(KEY_RIGHTPAN, c.GetRightPanning());
        	
        	db.insert(TABLE_CHANNEL, null, values);
        	values.clear();
        	
        	int i = 0;
        	for(Step step : c.GetSteps()) {
        		values.put(KEY_ID, i++);
        		values.put(FKEY_SONGID, songId);
                values.put(KEY_VELOCITY, step.GetVelocity());
                values.put(KEY_ACTIVE, step.IsActive());
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
    public Song getSongById(int id) {
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
        	
        	c.SetPanning((float)channelCursor.getDouble(5), (float)channelCursor.getDouble(4));
        	c.SetVolume((float)channelCursor.getDouble(3));
        	
        	while(stepCursor.moveToNext()) {
        		c.SetStep(stepCursor.getInt(0), stepCursor.getInt(2) > 0, (float)stepCursor.getDouble(1));
        	}
        	
        	song.AddChannel(c);
        }
        
        
        // return contact
        return song;
    	
    }

}
