package kvhc.gui;

import java.util.Observable;
import java.util.Observer;

import kvhc.adrumdrum.R;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Observer, listens to Player and updates active step when something happens.
 * 
 * @author kvhc
 */
public class GUIUpdateObserver implements Observer {
	
	private Activity parentActivity;
	private int current;
	
	/**
	 * Constructor.
	 * @param pa the parent activity
	 */
	public GUIUpdateObserver(Activity pa){
		parentActivity = pa;
	}
	
	/**
	 * This method should be called every time the player go to the next step
	 * It marks the current step with a red line in the GUI
	 * A -1 as parameter marks a stop in the player
	 * 
	 * TODO(update): This method have to be optimized
	 */
	public void update(Observable observable, Object data) {
		
		int step = Integer.parseInt(data.toString());
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		if (step >= 0){
			
			for (int i = 0; i < channelContainer.getChildCount(); i++) {
				TableRow row = (TableRow) channelContainer.getChildAt(i);
				// Den magiska 3:an är kommer från att det finns mer än steps i raden
				// (ett okänt och channelbutton + mutebutton)
				if (row.getChildCount() <= 3) {
					break;
				}
				int totalSteps = row.getChildCount() - 3; 
				int previousStep = (step - 1);
				if (previousStep < 0) {
					previousStep = totalSteps;
				}
				// +2 för att första två elementen är knappar
				((GUIStepButton)row.getChildAt(previousStep + 2)).setPlaying(false);
				((GUIStepButton)row.getChildAt(previousStep + 2)).invalidate();
				((GUIStepButton)row.getChildAt(step + 2)).setPlaying(true);
				((GUIStepButton)row.getChildAt(step + 2)).invalidate();
			}
			current = step + 2;
		}else {
			if (current > 0) {
				for (int i = 0; i < channelContainer.getChildCount(); i++){
					TableRow row = (TableRow) channelContainer.getChildAt(i);
					if (row.getChildCount() <= 3) {
						break;
					}
					((GUIStepButton)row.getChildAt(current)).setPlaying(false);
					((GUIStepButton)row.getChildAt(current)).invalidate();
				}
			}
		}
		channelContainer.invalidate();
	}
	
}
