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

package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import kvhc.models.Sound;
import kvhc.util.db.ChannelSQLiteHelper;
import kvhc.util.db.SongSQLiteHelper;
import kvhc.util.db.SoundDataSource;
import kvhc.util.db.StepSQLiteHelper;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main class.
 * @author kvhc
 */
public class MainActivity extends Activity {
	
	private GUIController guic;
	
	private SoundDataSource mSoundLoader;
	
    /**
     * Everything that the app have to do when created
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Always change media volume and not ringtone volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mSoundLoader = new SoundDataSource(this);
        try {
        	mSoundLoader.open();
        } catch(Exception e) {
        	// Already created?
        	Log.e("Main", e.toString());
        }
        
        if(mSoundLoader.getAllSounds().size() == 0) {
        	
        	Sound s = new Sound(R.raw.jazzfunkkitbd_01, "Bassdrum");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitbellridecym_01, "Bell Ride");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitclosedhh_01, "Closed hihat");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitcrashcym_01, "Crash 01");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitcrashcym_02, "Crash 02");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitopenhh_01, "Open hihat");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitridecym_01, "Ride");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitsn_01, "Snare 01");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitsn_02, "Snare 02");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitsn_03, "Snare 03");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitsplashcym_01, "Splash 01");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkitsplashcym_02, "Splash 02");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkittom_01, "Tomtom 01");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkittom_01, "Tomtom 02");
        	mSoundLoader.save(s);
        	s = new Sound(R.raw.jazzfunkkittom_01, "Tomtom 03");
        	mSoundLoader.save(s);
        	
        	ChannelSQLiteHelper channelHelper = new ChannelSQLiteHelper(this);
            channelHelper.onUpgrade(channelHelper.getWritableDatabase(), 1, 3);
            SongSQLiteHelper songHelper = new SongSQLiteHelper(this);
            songHelper.onUpgrade(songHelper.getWritableDatabase(), 1, 3);
            StepSQLiteHelper stepHelper = new StepSQLiteHelper(this);
            stepHelper.onUpgrade(stepHelper.getWritableDatabase(), 1, 3);
        }
        
        mSoundLoader.close();
        guic = new GUIController(this);
    }

    /**
     * The things that should be done when the app are stopped
     */
    public void onStop() {
    	guic.onStop();
    	
    	super.onStop();
    }
    
    /**
     * The things that should be done then the app are destroyed
     */
    public void onDestroy() {
    	guic.onDestroy();
    	
    	super.onDestroy();
    }

    /**
     * The things that should be done then the menu are created
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	/**
	 * Listener to the menu
	 */
	public boolean onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()) {
			case R.id.licenses: 
				guic.createAndShowLicensesDialog();
				return true;
			case R.id.clear_steps:
				guic.clearAllSteps();
				return true;
			case R.id.save_song:
				guic.createAndShowSaveSongDialog();
				return true;
			case R.id.load_song:
				guic.createAndShowLoadSongDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
}
