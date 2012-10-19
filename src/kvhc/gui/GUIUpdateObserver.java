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
	
	private static final int START_OF_STEPINDEX = 3;
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
	 * A (-1) as parameter marks a stop in the player
	 */
	public void update(Observable observable, Object data) {
		
		int step = Integer.parseInt(data.toString());
		
		TableLayout channelContainer = (TableLayout)parentActivity.findViewById(R.id.ChannelContainer);
		
		if (step >= 0){
			
			for (int i = 0; i < channelContainer.getChildCount(); i++) {
				TableRow row = (TableRow) channelContainer.getChildAt(i);
				
				if (row.getChildCount() <= START_OF_STEPINDEX) {
					break;
				}
				int totalSteps = row.getChildCount() - START_OF_STEPINDEX; 
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
					if (row.getChildCount() <= START_OF_STEPINDEX) {
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
