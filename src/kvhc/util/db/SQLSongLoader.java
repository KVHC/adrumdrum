package kvhc.util.db;

import java.util.List;

import android.content.Context;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.util.AssetManagerModel;
import kvhc.util.ISongLoader;

/**
 * Used for loading a song from SQL
 * @author kvhc
 *
 */
public class SQLSongLoader implements ISongLoader {

	private List<Song> mSongs;
	
	private AssetManagerModel<Sound> soundManager;
	
	private SongDataSource mDBSongHelper;
	private SoundDataSource mDBSoundHelper;
	
	public SQLSongLoader(Context context) {
		
		// Set up the DAO
		mDBSongHelper = new SongDataSource(context);
		mDBSoundHelper = new SoundDataSource(context);
		
		// Set up sound manager
		soundManager = AssetManagerModel.getSoundManager();
		for(Sound sound : mDBSoundHelper.getAllSounds()) {
			soundManager.setValue(sound.getName(), sound);
		}
	}
	
	/**
	 * Loads a song from SQL, Object source should be a String which should be the 
	 * name of the song 
	 */
	public Song loadSong(Object[] args) {

		// Set up variables
		String name;
		mSongs = getSongList(null);
		
		
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
				loadedSong  = song;
				break;
			}
		}
		
		// ADrumDrumWars - Return of the Song 
		return loadedSong;
	}
	
	/**
	 * Gets a list of all the sounds in the database
	 * @return
	 */
	public List<Sound> getSoundList() {
		return mDBSoundHelper.getAllSounds();
	}
	
	
	/**
	 * Returns a list of songs
	 */
	public List<Song> getSongList(Object[] args) {
		return mDBSongHelper.getAllSongs();
	}

}
