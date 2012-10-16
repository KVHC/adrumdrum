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

// PGA inte länkat in projektet än i git så ser det just nu ut såhär. VSMG. Antar att LoadSounds blir att ladda en patch.
//import org.puredata.android.service.PdService;
//import org.puredata.android.utils.PdUiDispatcher;

import kvhc.player.Channel;
import kvhc.player.Song;
import kvhc.player.Sound;

public class PdRenderer implements ISongRenderer {

	//private PdUiDispatcher dispatcher;
	//private PdService pdService = null;
	
	public void LoadSounds(ArrayList<Sound> soundList) {
		
		
	}
	
	public void RenderSong(Song song) {
		// Vill inte spela upp hela samtidigt.
		
		//for(Channel c : song.GetChannels()) {
			
		//}
	}
	
	public void RenderSongAtStep(Song song, int step) {
		
		for(Channel c: song.getChannels()) {
			if(c.isStepActive(step)) {
				// Något sånt här
				//String soundName = c.GetSound().GetName();
				/*PdBase.SendFloat(soundName + "-volume", c.GetVolume());
				PdBase.SendFloat(soundName +"-leftpan", c.GetLeftPanning());
				PdBase.SendFloat(soundName +"-rightpan", c.GetRightPanning());
				PdBase.SendTrigger(soundName);*/
			}
		}
	}
}
