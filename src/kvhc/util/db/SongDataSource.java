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
import android.util.Log;

/**
 * DataSource object for Song model
 * CRUD operations
 *  
 * @author kvhc
 *
 */
public class SongDataSource {
	private SQLiteDatabase database;
	private SongSQLiteHelper dbHelper;
	private String[] allColumns = { SongSQLiteHelper.COLUMN_ID, SongSQLiteHelper.COLUMN_NAME };
	
	private ChannelDataSource dbChannelHelper;
	
	public SongDataSource(Context context) {
		dbHelper = new SongSQLiteHelper(context);
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
		values.put(SongSQLiteHelper.COLUMN_NAME, "'" + name + "'");
		Song newSong = null;
		try {
			long insertId = database.insert(SongSQLiteHelper.TABLE_SONG, null, values);
			
			if(insertId >= 0) {
				Cursor cursor = database.query(SongSQLiteHelper.TABLE_SONG, allColumns, SongSQLiteHelper.COLUMN_ID + " = " 
							+ insertId, null, null, null, null);
			
				cursor.moveToFirst();
				newSong = cursorToSong(cursor);
				cursor.close();
				
			}
		} catch(SQLException e) {
			Log.e(getClass().toString(), e.toString());
		}
		
		return newSong;
	}
	
	public Song save(Song song) {
		ContentValues values = new ContentValues();
		values.put(SongSQLiteHelper.COLUMN_NAME, song.getName());
	
		if(song.getId() > 0) {
			// Has id
			String where = SongSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(song.getId() )};
			database.update(SongSQLiteHelper.TABLE_SONG, values, where, whereArgs);
		} else {
			// doesn't have id
			song = createSong(song.getName());
		}
		
		if(song == null) {
			// Error in creation of song
			// Abort and return null
			return null;
		}
		
		dbChannelHelper.open();
		for(Channel channel : song.getChannels()) {
			dbChannelHelper.save(song, channel);
		}
		dbChannelHelper.close();
		
		return song;
	}
	
	public void deleteSong(Song song) {
		long id = song.getId();
		
		// Delete sound
		database.delete(SongSQLiteHelper.TABLE_SONG, SongSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Song> getAllSongs() {
		List<Song> songs = new ArrayList<Song>();
		
		Cursor cursor = database.query(SongSQLiteHelper.TABLE_SONG, allColumns, null,null,null,null,null);
		
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
		
		dbChannelHelper.open();
		List<Channel> channels = dbChannelHelper.getAllChannelsForSong(song);
		song.setChannels(channels);
		dbChannelHelper.close();
		
		return song;
	}
}
