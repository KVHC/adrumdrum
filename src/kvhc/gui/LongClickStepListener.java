package kvhc.gui;

import kvhc.player.Step;
import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;

public class LongClickStepListener implements OnLongClickListener{

	Step step;
	Activity parentActivity;
	
	public LongClickStepListener(Step step, Activity parentActivity){
		this.step = step;
		this.parentActivity = parentActivity;
	}
	
	
	public boolean onLongClick(View v) {
		StepDialog vd = new StepDialog(parentActivity, step);
		vd.show();
		
		return true;
	}

}
