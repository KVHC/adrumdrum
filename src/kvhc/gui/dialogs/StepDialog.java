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

package kvhc.gui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import kvhc.adrumdrum.R;
import kvhc.gui.GUIController;
import kvhc.models.Step;

/**
 * StepDialog appears if you do LongClick on a GUIStepButton.
 * Shows some step settings and actions.
 */
public class StepDialog extends Dialog {

	private static final int MAX_PERCENT = 100;
	private static final float ONE_PERCENT = 0.01f;
	
	private Step step;
	private GUIController guic;
	private int channel, stepnr;
	
	/**
	 * The constructor
	 * @param parrentActivity The parent Activity of this dialog
	 * @param step The step to display settings for
	 * @param guic The GUIController
	 */
	public StepDialog(Activity parrentActivity, Step step, GUIController guic, int channel, int stepnr) {
		super(parrentActivity);
		this.step = step;
		this.guic = guic;
		this.channel = channel;
		this.stepnr = stepnr;
	}

	/**
	 * What to do when a new StepDialog are created.
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.velocity_settings);
        setTitle("Step Settings");
        initButtons();
        initBars();
	}
	
	/**
	 * Initialize the buttons.
	 */
	private void initButtons(){
		Button back = (Button)this.findViewById(R.id.buttonBack);
		back.setOnClickListener(backClick);
		
		Button spike = (Button)this.findViewById(R.id.buttonSpike);
	    spike.setOnClickListener(spikeClick);
	}
	
	/**
	 * Initialize the progresspar.
	 */
	private void initBars(){
		SeekBar velocityBar = (SeekBar)this.findViewById(R.id.seekbarVelocity);
		velocityBar.setProgress((int)(step.getVelocity() * MAX_PERCENT));
		velocityBar.setOnSeekBarChangeListener(velocityListener);
	}
	
	
	/**
	 * A Listener for what to do when the velocity bar are changed.
	 */
	private OnSeekBarChangeListener velocityListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			
			step.setVelolcity(progress * ONE_PERCENT);
		}

		public void onStartTrackingTouch(SeekBar arg0) {
		}

		public void onStopTrackingTouch(SeekBar arg0) {
		}
    };

    
    // Different on click listeners 
    
	/**
	 * An on click listener that close this dialog.
	 */
	private View.OnClickListener backClick = new View.OnClickListener(){
		public void onClick(View v) {
			dismiss();
		}
	};	
	
    /**
     * An on click listener that SPIKES the step.
     */
    private View.OnClickListener spikeClick = new View.OnClickListener(){
        public void onClick(View v) {
            guic.setSpike(channel,stepnr);
            SeekBar velocityBar = (SeekBar)findViewById(R.id.seekbarVelocity);
            velocityBar.setProgress(MAX_PERCENT);
            dismiss();
            guic.redrawChannels();
        }
    };
	
}
