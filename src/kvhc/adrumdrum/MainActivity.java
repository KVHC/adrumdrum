/**
 * aDrumDrum is a stepsequencer for Android.
Copyright (C) 2012  Daniel Fallstrand, Niclas Ståhl, Oscar Dragén and Viktor Nilsson.

This file is part of aDrumDrum.

aDrumDrum is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

aDrumDrum is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with aDrumDrum.  If not, see <http://www.gnu.org/licenses/>.
 */

package kvhc.adrumdrum;

import kvhc.gui.GUIController;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private GUIController guic;
	
    /**
     * Everything that the app have to do then created
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        guic = new GUIController(this);
        // Always change media volume and not ringtone volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC); 
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
}
