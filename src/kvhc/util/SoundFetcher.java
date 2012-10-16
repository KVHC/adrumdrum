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

package kvhc.util;

import android.app.Activity;
import kvhc.adrumdrum.R;
import kvhc.player.Sound;

public class SoundFetcher extends Activity {


	public static Sound GetSoundFromString(String s) {
		
		//String name = String.valueOf(s.getSelectedItem());
		String name = s;
		if("Bassdrum".equals(name)) {
			return new Sound(1, R.raw.jazzfunkkitbd_01, name);
		}
		
		if("Bell Ride Cymbal".equals(name)) {
			return new Sound(2, R.raw.jazzfunkkitbellridecym_01, name);
		} 
		
		if("Crash Cymbal 01".equals(name)) {
			return new Sound(3, R.raw.jazzfunkkitcrashcym_01, name);
		}
		
		if("Crash Cymbal 02".equals(name)) {
			return new Sound(4, R.raw.jazzfunkkitcrashcym_02, name);
		}
		
        if("Hihat Closed".equals(name)) {
        	return new Sound(5, R.raw.jazzfunkkitclosedhh_01, name);
        }
        
        if("Hihat Open".equals(name)) {
        	return new Sound(6, R.raw.jazzfunkkitopenhh_01, name);
        }
        
		if("Ride Cymbal".equals(name)) {
			return new Sound(7, R.raw.jazzfunkkitridecym_01, name);
		}
		
		if("Snare 01".equals(name)) {
			return new Sound(8, R.raw.jazzfunkkitsn_01, name);
		}
		
		if("Snare 02".equals(name)) {
			return new Sound(9, R.raw.jazzfunkkitsn_02, name);
		}
		
		if("Snare 03".equals(name)) {
			return new Sound(10, R.raw.jazzfunkkitsn_03, name);
		}
		
		if("Splash Cymbal 01".equals(name)) {
			return new Sound(11, R.raw.jazzfunkkitsplashcym_01, name);
		}
		
		if("Splash Cymbal 02".equals(name)) {
			return new Sound(12, R.raw.jazzfunkkitsplashcym_02, name);
		}
		
		if("Tomtom 01".equals(name)) {
			return new Sound(13, R.raw.jazzfunkkittom_01, name);
		}
		
		if("Tomtom 02".equals(name)) {
			return new Sound(14, R.raw.jazzfunkkittom_02, name);
		}
		
		if("Tomtom 03".equals(name)) {
			return new Sound(15, R.raw.jazzfunkkittom_03, name);
		}
		
		return null;
	}
	
	
}
