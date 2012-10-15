package kvhc.util.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.player.Step;
import kvhc.util.ISongRenderer;


/**
 * A SQL implementation of the ISongRenderer interface, for saving songs to 
 * the database. 
 * 
 * @author kvhc
 *
 */
public class SQLRenderer implements ISongRenderer {
	
	
	private ArrayList<Sound> mSounds;
	private DatabaseHandler mDB;
	private SongDataSource dbSongHelper;
	private SoundDataSource dbSoundHelper;
	
	/**
	 * constructor
	 * @param context Android context for some activity or view, parent...
	 */
	public SQLRenderer(Context context) {
		mDB = new DatabaseHandler(context);
		dbSongHelper = new SongDataSource(context);
		dbSoundHelper  = new SoundDataSource(context);
	}

	/*  ISongRenderer implementation  */
	public void LoadSounds(ArrayList<Sound> soundList) {
		mSounds = soundList;
		
		for(Sound sound : mSounds) {
			dbSoundHelper.save(sound);
		}
	}
	
	public void RenderSong(Song song) {
		song = dbSongHelper.save(song);
	}
	
	public void RenderSongAtStep(Song song, int step) {
		//TODO: Add implementation?  Will we use this? Seems unessesary.	
	}
}
