package kvhc.gui;

import java.util.Observable;
import java.util.Observer;

import kvhc.adrumdrum.R;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;




public class GUIUpdateObserver implements Observer {
	
	private Activity parentActivity;
	private int current;
	
	public GUIUpdateObserver(Activity pa){
		parentActivity = pa;
	}
	
	
	/*
	 * This method should be called every time the player go to the next step
	 * It marks the current step with a red line in the GUI
	 * A -1 as parameter marks a stop in the player
	 */
	public void update(Observable observable, Object data) {
		int step = Integer.parseInt(data.toString());
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		if (step >= 0){
			
			// Börjar på noll, radiobuttons/annat bör inte ligga i channelContainer
			for(int i = 0; i < channelContainer.getChildCount(); i++) {
				TableRow row = (TableRow) channelContainer.getChildAt(i);
				// Den magiska 2:an är kommer från att det finns en knapp och något annat 
				// förutom steps i raden
				int totalSteps = row.getChildCount() - 2; 
				int previousStep = (step - 1);
				if (previousStep < 0)
					previousStep = totalSteps;
				// +1 för att första elementet är en knapp
				((GUIStepButton)row.getChildAt(previousStep + 1)).setPlaying(false);
				((GUIStepButton)row.getChildAt(step + 1)).setPlaying(true);
			}
			current = step + 1;
		}else {
			for(int i = 0; i < channelContainer.getChildCount(); i++){
				TableRow row = (TableRow) channelContainer.getChildAt(i);
				((GUIStepButton)row.getChildAt(current)).setPlaying(false);
			}
		}
		channelContainer.invalidate();
	}
}
