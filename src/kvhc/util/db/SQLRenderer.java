/**
 * aDrumDrum is a step sequencer for Android.
 * Copyright (C) 2012  Daniel Fallstrand, Niclas Ståhl, Oscar Dragén and Viktor Nilsson.
 *
 * This file is part of aDrumDrum.
 *
 * aDrumDrum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aDrumDrum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aDrumDrum.  If not, see <http://www.gnu.org/licenses/>.
 */

package kvhc.util.db;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.models.Sound;
import kvhc.models.Step;
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
