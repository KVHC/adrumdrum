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

import java.util.List;

import android.content.Context;
import android.util.Log;
import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.models.Sound;
import kvhc.util.AssetManagerModel;
import kvhc.util.ISongLoader;

/**
 * Used for loading a song from SQL.
 * @author kvhc
 */
public class SQLSongLoader implements ISongLoader {

	private List<Song> mSongs;
	
	private SongDataSource mDBSongHelper;
	private SoundDataSource mDBSoundHelper;
	private ChannelDataSource mDBChannelHelper;
	private StepDataSource mDBStepHelper;
	
	/**
	 * Constructor.
	 * @param context the Context of the activity.
	 */
	public SQLSongLoader(Context context) {
		
		// Set up the DAO
		mDBSongHelper = new SongDataSource(context);
		mDBSoundHelper = new SoundDataSource(context);
		mDBChannelHelper = new ChannelDataSource(context);
		mDBStepHelper = new StepDataSource(context);
		
		mDBSongHelper.open();
		mDBSoundHelper.open();
		
		// Set up sound manager
		AssetManagerModel<Sound> soundManager = AssetManagerModel.getSoundManager();
		for(Sound sound : mDBSoundHelper.getAllSounds()) {
			soundManager.setValue(sound.getName(), sound);
		}
		
		mDBSongHelper.close();
		mDBSoundHelper.close();
		
	}
	
	/**
	 * Loads a song from SQL, Object source should be a String which should be the 
	 * name of the song 
	 */
	public Song loadSong(Object[] args) {

		// Set up variables
		String name;
		
		getSongList(null);

		
		// Get arguments
		switch(args.length) {
		case 1:
			if(args[0].getClass() != String.class) {
				return null;
			}
			name = (String)args[0];
			break;
		default:
				return null;
		}
		
		// Check if the song is loaded
		Song loadedSong = null;
		for(Song song : mSongs) {
			if(song.getName().equals(name)) {
				loadedSong = song;
				
				Log.w("SQLSongLoader", "Id: " + song.getId());
				Log.w("SQLSongLoader", "Name: " + song.getName());
				Log.w("SQLSongLoader", "NumChannels: " + song.getNumberOfChannels());
				Log.w("SQLSongLoader", "NumSteps: " + song.getNumberOfSteps());
				break;
			}
		}

		// ADrumDrumWars - Return of the Song
		return loadedSong;
	}
	
	/**
	 * Gets a list of all the sounds in the database.
	 * @return List of all the sounds found in the databse.
	 */
	public List<Sound> getSoundList() {
		
		mDBSongHelper.open();
		List<Sound> sounds = mDBSoundHelper.getAllSounds();
		mDBSongHelper.close();
		
		return sounds;
	}
	
	
	/**
	 * Returns a list of songs.
	 */
	public List<Song> getSongList(Object[] args) {
		
		// Get list of all songs.
		mDBSongHelper.open();
		List<Song> songs = mDBSongHelper.getAllSongs();
		mDBSongHelper.close();
		
		// Fill all the songs with data.
		for(Song song : songs) {
			if(mSongs.contains(song)) {
				continue;
			}
			
			// Loading channels.
			mDBChannelHelper.open();
			List<Channel> channels = mDBChannelHelper.getAllChannelsForSong(song);
			mDBChannelHelper.close();
			
			// Loading steps and sound.
			mDBStepHelper.open();
			mDBSoundHelper.open();
			for(Channel channel : channels) {
				// Sound has ID but no content. Get the sound from the database.
				channel.setSound(mDBSoundHelper.getSoundFromKey(channel.getSound().getId()));
				
				// Get all the steps from the database.
				channel.setSteps(mDBStepHelper.getAllStepsForChannel(channel));
			}
			mDBSoundHelper.close();
			mDBStepHelper.close();
			
			song.setChannels(channels);
		}
		
		// Set song list. 
		mSongs = songs;
		
		return songs;
	}

}
