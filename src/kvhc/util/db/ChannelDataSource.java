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
import android.util.Log;

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
		
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, leftPan);
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, rightPan);
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, volume);
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, number);
		
		if(sound != null) {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, sound.getId());
		} else {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, -1);
		}
		
		long insertId = database.insert(ChannelSQLiteHelper.TABLE_CHANNEL, null, values);
		
		Log.w("ChannelData", "Insert channel id: " + insertId);
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
		
		String where = ChannelSQLiteHelper.FKEY_SONGID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(song.getId()) };
		Cursor cursor = database.query(ChannelSQLiteHelper.TABLE_CHANNEL, allColumns, where, whereArgs, null,null, ChannelSQLiteHelper.COLUMN_NUMBER + " ASC");
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Channel channel = cursorToChannel(cursor);
			channels.add(channel);
			cursor.moveToNext();
		}
		cursor.close();
		
		Log.w("ChannelData", "Antal: " + channels.size());
		return channels;
	}
	
	private Channel cursorToChannel(Cursor cursor) {
		if(cursor.getCount() == 0) return null;
		
		Channel channel = new Channel();
		
		long channelId = cursor.getLong(0);
		long soundId = cursor.getLong(6);
		
		channel.setId(channelId);
		channel.setPanning((float)cursor.getDouble(4), (float)cursor.getDouble(5));
		channel.setVolume((float)cursor.getDouble(2)); 
		channel.setChannelNumber(cursor.getInt(1));
		
		if(channelId >= 0) {
			dbStepHelper.open();
			List<Step> steps = dbStepHelper.getAllStepsForChannel(channel);
			dbStepHelper.close();
			Log.w("Channel ds", "Loading steps for channel: " + channelId + " with steps: " + steps.size());
			channel.setSteps(steps); // GET STEPS
		}
		
		if(soundId >= 0) {
			dbSoundHelper.open();
			Sound sound = dbSoundHelper.getSoundFromKey(soundId);
			dbSoundHelper.close();
			channel.setSound(sound); // GET SOUND
		} else {
			channel.setSound(null);
		}
		
		return channel;
	}

	public void save(Song song, Channel channel) {
		
		if(song.getId() == 0) return;
		
		ContentValues values = new ContentValues();
		values.put(ChannelSQLiteHelper.COLUMN_NUMBER, channel.getChannelNumber());
		values.put(ChannelSQLiteHelper.COLUMN_RIGHTPAN, channel.getRightPanning());
		values.put(ChannelSQLiteHelper.COLUMN_LEFTPAN, channel.getLeftPanning());
		values.put(ChannelSQLiteHelper.COLUMN_VOLUME, channel.getVolume());
		
		if(channel.getSound() != null) {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, channel.getSound().getId());
		} else {
			values.put(ChannelSQLiteHelper.FKEY_SOUNDID, 0);
		}
		
		if(channel.getId() > 0) {
			String where = ChannelSQLiteHelper.COLUMN_ID + " = ?";
			String[] whereArgs = new String[] { String.valueOf(channel.getId() )};
		
			database.update(ChannelSQLiteHelper.TABLE_CHANNEL, values, where, whereArgs);
		} else {
			Channel tmp = createChannel(song, channel.getSound(), channel.getVolume(), channel.getRightPanning(), channel.getLeftPanning(), channel.getChannelNumber());
			
			channel.setId(tmp.getId());
		}
	}
}
