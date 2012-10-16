package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.Step;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Channel object DAO 
 * @author kvhc
 *
 */
public class ChannelDataSource {
	private SQLiteDatabase database;
	private ChannelSQLiteHelper dbHelper;
	
	private StepDataSource dbStepHelper;
	private SoundDataSource dbSoundHelper;
	
	private String[] allColumns = { ChannelSQLiteHelper.COLUMN_ID
			, ChannelSQLiteHelper.COLUMN_NUMBER 
			, ChannelSQLiteHelper.COLUMN_VOLUME 
			, ChannelSQLiteHelper.COLUMN_LEFTPAN
			, ChannelSQLiteHelper.COLUMN_RIGHTPAN
			, ChannelSQLiteHelper.FKEY_SONGID 
			, ChannelSQLiteHelper.FKEY_SOUNDID };
	

	public ChannelDataSource(Context context) {
		dbHelper = new ChannelSQLiteHelper(context);
		dbStepHelper = new StepDataSource(context);
		dbSoundHelper = new SoundDataSource(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Channel createChannel(Song song, Sound sound, float volume, float rightPan, float leftPan, int number) {
		ContentValues values = new ContentValues();
		values.put(ChannelSQLiteHelper.FKEY_SONGID, song.getId());
		values.put(ChannelSQLiteHelper.FKEY_SOUNDID, sound.getId());
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, leftPan);
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, rightPan);
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, number);
		
		long insertId = database.insert(ChannelSQLiteHelper.TABLE_CHANNEL, null, values);
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, ChannelSQLiteHelper.COLUMN_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Channel newChannel = cursorToChannel(cursor);
		cursor.close();
		return newChannel;
	}
	
	public void deleteChannel(Channel channel) {
		long id = channel.getId();
		
		// Delete sound
		database.delete(ChannelSQLiteHelper.TABLE_CHANNEL, ChannelSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Channel> getAllChannelsForSong(Song song) {
		List<Channel> channels = new ArrayList<Channel>();
		
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, null,null,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Channel channel = cursorToChannel(cursor);
			channels.add(channel);
			cursor.moveToNext();
		}
		
		cursor.close();
		return channels;
	}
	
	private Channel cursorToChannel(Cursor cursor) {
		
		Channel channel = new Channel();
		
		channel.setId(cursor.getInt(0));
		channel.setPanning((float)cursor.getDouble(4), (float)cursor.getDouble(5));
		channel.setVolume((float)cursor.getDouble(2)); 
		
		List<Step> steps = dbStepHelper.getAllStepsForChannel(channel);
		channel.setSteps(steps); // GET STEPS
		
		Sound sound = dbSoundHelper.getSoundFromKey(cursor.getLong(6));
		channel.setSound(sound); // GET SOUND
		
		return channel;
	}

	public void save(Song song, Channel channel) {
		
		ContentValues values = new ContentValues();
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, channel.getLeftPanning());
		
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, channel.getChannelNumber());
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, channel.getRightPanning());
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, channel.getVolume());
		
		if(channel.getSound() != null) {
			values.put(ChannelSQLiteHelper.COLUMN_NAME, channel.getSound().getName());
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, channel.getSound().getId());
		} else {
			values.put(ChannelSQLiteHelper.COLUMN_NAME, "");
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, 0);
		}
		
		String where = ChannelSQLiteHelper.COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(channel.getId() )};
		
		long numRows = database.update(ChannelSQLiteHelper.TABLE_CHANNEL, values, where, whereArgs);
		
		if(numRows == 0) {
			channel = createChannel(song, 
					channel.getSound(), 
					channel.getVolume(), 
					channel.getRightPanning(), 
					channel.getLeftPanning(), 
					channel.getChannelNumber());
		}
	}
}
