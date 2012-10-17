package kvhc.util.db;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

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
	private SongDataSource dbSongHelper;
	private ChannelDataSource dbChannelHelper;
	private StepDataSource dbStepHelper;
	private SoundDataSource dbSoundHelper;
	
	/**
	 * constructor
	 * @param context Android context for some activity or view, parent...
	 */
	public SQLRenderer(Context context) {
		dbSongHelper = new SongDataSource(context);
		dbSoundHelper  = new SoundDataSource(context);
		dbChannelHelper = new ChannelDataSource(context);
		dbStepHelper = new StepDataSource(context);
	}

	/*  ISongRenderer implementation  */
	public void LoadSounds(ArrayList<Sound> soundList) {
		mSounds = soundList;
		
		for(Sound sound : mSounds) {
			dbSoundHelper.save(sound);
		}
	}
	
	public void RenderSong(Song song) {
		
		dbSongHelper.open();
		Song tmp = dbSongHelper.save(song);
		dbSongHelper.close();
		
		song.setId(tmp.getId());
		
		dbChannelHelper.open();
		dbStepHelper.open();
		for(Channel channel : song.getChannels()) {
			dbChannelHelper.save(song, channel);
			
			Log.w("SQLRender", "Save steps: " + channel.getSteps().size());
			for(Step step : channel.getSteps()) {
				dbStepHelper.save(step, channel);
			}
		}
		dbStepHelper.close();
		dbChannelHelper.close();
		
		
		
	}
	
	public void RenderSongAtStep(Song song, int step) {
		//TODO: Add implementation?  Will we use this? Seems unessesary.	
	}
}
