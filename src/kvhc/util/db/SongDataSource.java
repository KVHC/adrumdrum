package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.player.Channel;
import kvhc.player.Song;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DataSource object for Song model
 * CRUD operations
 *  
 * @author kvhc
 *
 */
public class SongDataSource {
	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_NAME };
	
	private ChannelDataSource dbChannelHelper;
	
	public SongDataSource(Context context) {
		dbHelper = new DatabaseHandler(context);
		dbChannelHelper = new ChannelDataSource(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Song createSong(String name) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, name);
		long insertId = database.insert(DatabaseHandler.TABLE_SONG, null, values);
		Cursor cursor = database.query(DatabaseHandler.TABLE_SONG, allColumns, DatabaseHandler.KEY_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Song newSong = cursorToSong(cursor);
		cursor.close();
		return newSong;
	}
	
	public Song save(Song song) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_NAME, song.getName());
	
		if(song.getId() > 0) {
			// Has id
			String where = DatabaseHandler.KEY_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(song.getId() )};
			database.update(DatabaseHandler.TABLE_SONG, values, where, whereArgs);
		} else {
			// doesn't have id
			Song tmp = createSong(song.getName());
			song.setId(tmp.getId());
			tmp = null;
		}
		
		for(Channel channel : song.getChannels()) {
			dbChannelHelper.save(song, channel);
		}
		
		return song;
	}
	
	public void deleteSong(Song song) {
		long id = song.getId();
		
		// Delete sound
		database.delete(DatabaseHandler.TABLE_SONG, DatabaseHandler.KEY_ID + " = " + id, null);
	}
	
	public List<Song> getAllSongs() {
		List<Song> songs = new ArrayList<Song>();
		
		Cursor cursor = database.query(DatabaseHandler.TABLE_SONG, allColumns, null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Song song = cursorToSong(cursor);
			songs.add(song);
			cursor.moveToNext();
		}
		
		cursor.close();
		return songs;
	}
	
	private Song cursorToSong(Cursor cursor) {
		
		Song song = new Song(4);
		song.setId((int)cursor.getLong(0));
		song.setName(cursor.getString(1));
		
		List<Channel> channels = dbChannelHelper.getAllChannelsForSong(song);
		song.setChannels(channels);
		
		return song;
	}
}
