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
	
	/**
	 * The constructor
	 * @param parrentActivity The parent Activity of this dialog
	 * @param step The step to display settings for
	 */
	public StepDialog(Activity parrentActivity, Step step) {
		super(parrentActivity);
		this.step = step;
		
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
	}
	
	/**
	 * Init the progresspar in the StepDialog
	 */
	private void initBars(){
		SeekBar velocityBar = (SeekBar)this.findViewById(R.id.seekbarVelocity);
		velocityBar.setProgress((int)(step.GetVelocity() * 100));
		velocityBar.setOnSeekBarChangeListener(velocityListener);
	}
	
	
	/**
	 * A Listener for what to do then the velocity bar are changed
	 */
	private OnSeekBarChangeListener velocityListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
			
			step.SetVelolcity(progress * 0.01F);
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
	
	
}