package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import kvhc.util.db.SoundDataSource;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
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
        
        System.out.println("Adrumdrum: Done onCreate()");
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
