package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.player.Sound;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DAO for Sound model
 * CRUD interface
 * 
 * @author kvhc
 *
 */
public class SoundDataSource {

	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { SoundSQLiteHelper.COLUMN_ID, SoundSQLiteHelper.COLUMN_SOUNDVALUE,
			SoundSQLiteHelper.COLUMN_NAME };
	
	public SoundDataSource(Context context) {
		dbHelper = new DatabaseHandler(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Sound createSound(int soundValue, String name) {
		ContentValues values = new ContentValues();
		values.put(SoundSQLiteHelper.COLUMN_SOUNDVALUE, soundValue);
		values.put(SoundSQLiteHelper.COLUMN_NAME, soundValue);
		long insertId = database.insert(SoundSQLiteHelper.TABLE_SOUND, null, values);
		Cursor cursor = database.query(SoundSQLiteHelper.TABLE_SOUND, allColumns, SoundSQLiteHelper.COLUMN_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Sound newSound = cursorToSound(cursor);
		cursor.close();
		return newSound;
	}
	
	public void deleteSound(Sound sound) {
		long id = sound.getId();
		
		// Delete sound
		database.delete(SoundSQLiteHelper.TABLE_SOUND, SoundSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Sound> getAllSounds() {
		List<Sound> sounds = new ArrayList<Sound>();
		
		Cursor cursor = database.query(SoundSQLiteHelper.TABLE_SOUND, allColumns, null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Sound sound = cursorToSound(cursor);
			sounds.add(sound);
			cursor.moveToNext();
		}
		
		cursor.close();
		return sounds;
	}
	
	private Sound cursorToSound(Cursor cursor) {
		Sound sound = new Sound(cursor.getInt(1),cursor.getString(2));
		sound.setId((int)cursor.getLong(0));
		
		return sound;
	}

	public Sound getSoundFromKey(long soundId) {
		
		String where = SoundSQLiteHelper.COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(soundId) };
		Cursor cursor = database.query(SoundSQLiteHelper.TABLE_SOUND, allColumns, where, whereArgs, null,null,null);
		
		cursor.moveToFirst();
		Sound sound = cursorToSound(cursor);
		
		cursor.close();
		return sound;
	}

	/**
	 * Saves a sound to the db, if the sound doesn't have an id, it will create 
	 * and id for it and set it. 
	 * @param sound Sound to save to the database
	 */
	public void save(Sound sound) {
		
		ContentValues values = new ContentValues();
		values.put(SoundSQLiteHelper.COLUMN_SOUNDVALUE, sound.getSoundValue());
		values.put(SoundSQLiteHelper.COLUMN_NAME, sound.getName());
		
		
		if(sound.getId() > 0) {
			String where = SoundSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(sound.getId()) };
			database.update(SoundSQLiteHelper.TABLE_SOUND, values, where, whereArgs);
		} else {
			sound.setId(createSound(sound.getSoundValue(), sound.getName()).getId());
		}
		
		
	}
}
