package kvhc.util;

import java.util.ArrayList;
import java.util.HashMap;

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
		
		for(Channel c: song.GetChannels()) {
			if(c.IsStepActive(step)) {
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