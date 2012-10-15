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

package kvhc.gui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import kvhc.adrumdrum.R;
import kvhc.player.Step;

public class StepDialog extends Dialog {

	private Step step;
	private Button back;
	private Button spike;
	private GUIController guic;
	
	/**
	 * The constructor
	 * @param parrentActivity The parent Activity of this dialog
	 * @param step The step to display settings for
	 * @param guic The GUIController
	 */
	public StepDialog(Activity parrentActivity, Step step, GUIController guic) {
		super(parrentActivity);
		this.step = step;
		this.guic = guic;
		
	}

	/**
	 * What to do then a new StepDialog are created
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.velocity_settings);
        setTitle("Step Settings");
        initButtons();
        initBars();
	}
	
	/**
	 * Init the buttons in the StepDialog
	 */
	private void initButtons(){
		back = (Button)this.findViewById(R.id.buttonBack);
		back.setOnClickListener(backClick);
		
		spike = (Button)this.findViewById(R.id.buttonSpike);
	    spike.setOnClickListener(spikeClick);
	}
	
	/**
	 * Init the progresspar in the StepDialog
	 */
	private void initBars(){
		SeekBar velocityBar = (SeekBar)this.findViewById(R.id.seekbarVelocity);
		velocityBar.setProgress((int)(step.getVelocity() * 100));
		velocityBar.setOnSeekBarChangeListener(velocityListener);
	}
	
	
	/**
	 * A Listener for what to do then the velocity bar are changed
	 */
	private OnSeekBarChangeListener velocityListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			
			step.setVelolcity(progress * 0.01F);
		}

		public void onStartTrackingTouch(SeekBar arg0) {
		}

		public void onStopTrackingTouch(SeekBar arg0) {
		}
    };

    
	/**
	 * A on click listener that close this dialog
	 */
	private View.OnClickListener backClick = new View.OnClickListener(){
		public void onClick(View v) {
			dismiss();
		}
	};	
	
    /**
     * A on click listener that SPIKES the step
     */
    private View.OnClickListener spikeClick = new View.OnClickListener(){
        public void onClick(View v) {
            step.multiStepVelocitySpike();
            SeekBar velocityBar = (SeekBar)findViewById(R.id.seekbarVelocity);
            velocityBar.setProgress(100);
            guic.redrawChannels();
        }
    };
	
	
}
