/**
 * aDrumDrum is a stepsequencer for Android.
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

import java.util.List;

import kvhc.gui.GUIController;
import kvhc.player.Song;
import kvhc.player.Sound;
import kvhc.util.db.SQLSongLoader;
import kvhc.util.db.SoundDataSource;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	
	private GUIController guic;
	
	SoundDataSource mSoundLoader;
	
    /**
     * Everything that the app have to do then created
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Adrumdrum: Started onCreate()");

        
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
        	mSoundLoader.createSound(R.raw.jazzfunkkitbd_01, "Bassdrum");
        	mSoundLoader.createSound(R.raw.jazzfunkkitbellridecym_01, "Bell Ride");
        	mSoundLoader.createSound(R.raw.jazzfunkkitclosedhh_01, "Closed hihat");
        	mSoundLoader.createSound(R.raw.jazzfunkkitcrashcym_01, "Crash 01");
        	mSoundLoader.createSound(R.raw.jazzfunkkitcrashcym_02, "Crash 02");
        	mSoundLoader.createSound(R.raw.jazzfunkkitopenhh_01, "Open hihat");
        	mSoundLoader.createSound(R.raw.jazzfunkkitridecym_01, "Ride");
        	mSoundLoader.createSound(R.raw.jazzfunkkitsn_01, "Snare 01");
        	mSoundLoader.createSound(R.raw.jazzfunkkitsn_02, "Snare 02");
        	mSoundLoader.createSound(R.raw.jazzfunkkitsn_03, "Snare 03");
        	mSoundLoader.createSound(R.raw.jazzfunkkitsplashcym_01, "Splash 01");
        	mSoundLoader.createSound(R.raw.jazzfunkkitsplashcym_02, "Splash 02");
        	mSoundLoader.createSound(R.raw.jazzfunkkittom_01, "Tomtom 01");
        	mSoundLoader.createSound(R.raw.jazzfunkkittom_02, "Tomtom 02");
        	mSoundLoader.createSound(R.raw.jazzfunkkittom_03, "Tomtom 03");
        }
        
        mSoundLoader.close();
        guic = new GUIController(this);
    }

    /**
     * The things that should be done then the app are stopped
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
