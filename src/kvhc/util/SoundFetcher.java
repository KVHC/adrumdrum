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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import kvhc.adrumdrum.R;
import kvhc.player.Sound;

public class SoundFetcher extends Activity {

	
	
	private static HashMap<Integer,String> idAndName;
	private static HashMap<String,Integer> nameAndId;
	private static HashMap<Integer,Integer> idAndSound;
	
	
	
	private SoundFetcher(){
			
	}
	
	
	private static void initHashMaps(){
		
		idAndName =  new HashMap<Integer,String>();
		nameAndId = new HashMap<String,Integer>();
		idAndSound = new HashMap<Integer,Integer>();
		
		
		Integer i = 1;
		
		idAndName.put(i,"Bassdrum");
		nameAndId.put("Bassdrum",i);
		idAndSound.put(i,R.raw.jazzfunkkitbd_01);
		i++;
		
		idAndName.put(i,"Bell Ride Cymbal");
		nameAndId.put("Bell Ride Cymbal",i);
		idAndSound.put(i,R.raw.jazzfunkkitbellridecym_01);
		i++;
		
		idAndName.put(i,"Crash Cymbal 01");
		nameAndId.put("Crash Cymbal 01",i);
		idAndSound.put(i,R.raw.jazzfunkkitcrashcym_01);
		i++;
		
		idAndName.put(i,"Crash Cymbal 02");
		nameAndId.put("Crash Cymbal 02",i);
		idAndSound.put(i,R.raw.jazzfunkkitcrashcym_02);
		i++;

		idAndName.put(i,"Hihat Closed");
		nameAndId.put("Hihat Closed",i);
		idAndSound.put(i,R.raw.jazzfunkkitclosedhh_01);
		i++;
		
		idAndName.put(i,"Hihat Open");
		nameAndId.put("Hihat Open",i);
		idAndSound.put(i,R.raw.jazzfunkkitopenhh_01);
		i++;
		
		idAndName.put(i,"Ride Cymbal");
		nameAndId.put("Ride Cymbal",i);
		idAndSound.put(i,R.raw.jazzfunkkitridecym_01);
		i++;
		
		idAndName.put(i,"Snare 01");
		nameAndId.put("Snare 01",i);
		idAndSound.put(i,R.raw.jazzfunkkitsn_01);
		i++;
		
		idAndName.put(i,"Snare 02");
		nameAndId.put("Snare 02",i);
		idAndSound.put(i,R.raw.jazzfunkkitsn_02);
		i++;
				
		idAndName.put(i,"Snare 03");
		nameAndId.put("Snare 03",i);
		idAndSound.put(i,R.raw.jazzfunkkitsn_03);
		i++;

		idAndName.put(i,"Splash Cymbal 01");
		nameAndId.put("Splash Cymbal 01",i);
		idAndSound.put(i,R.raw.jazzfunkkitsplashcym_01);
		i++;
		
		idAndName.put(i,"Splash Cymbal 02");
		nameAndId.put("Splash Cymbal 02",i);
		idAndSound.put(i,R.raw.jazzfunkkitsplashcym_02);
		i++;
		
		idAndName.put(i,"Tomtom 01");
		nameAndId.put("Tomtom 01",i);
		idAndSound.put(i,R.raw.jazzfunkkittom_01);
		i++;

		idAndName.put(i,"Tomtom 02");
		nameAndId.put("Tomtom 02",i);
		idAndSound.put(i,R.raw.jazzfunkkittom_02);
		i++;
		
		idAndName.put(i,"Tomtom 03");
		nameAndId.put("Tomtom 03",i);
		idAndSound.put(i,R.raw.jazzfunkkittom_03);
		
	}
	
	public static ArrayList<String> getNameInOrder(){
		ArrayList<String> retList = new ArrayList<String>();
		int i = 1;
		
		while (getName(i) != null){
			retList.add(getName(i));
			i++;
		}
		
		return retList;
		
	}
	
	public static String getName(int id){
		if (idAndName == null){
			initHashMaps();
		}
		
		return  idAndName.get(id);
	}
	
	
	public static Integer getSound(int id){
		if (idAndSound == null){
			initHashMaps();
		}
		
		return  idAndSound.get(id);
	}

	
	public static Integer getId(String name){
		if (nameAndId == null){
			initHashMaps();
		}
		
		return  nameAndId.get(name);
	}
	
	
	

	public static Sound GetSoundFromString(String s) {
	
		Integer id = getId(s);
		if (id == null){
			return null;
		}
		Integer soundId = getSound(id);
		
		return new Sound(id, soundId, s);
	}
}
