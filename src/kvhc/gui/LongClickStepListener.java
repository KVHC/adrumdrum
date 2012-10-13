package kvhc.gui;

import kvhc.player.Step;
import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;

public class LongClickStepListener implements OnLongClickListener{

	Step step;
	Activity parentActivity;
	GUIController guic;
	
	public LongClickStepListener(Step step, Activity parentActivity, GUIController guic){
		this.step = step;
		this.parentActivity = parentActivity;
		this.guic = guic;
	}
	
	
	public boolean onLongClick(View v) {
		StepDialog vd = new StepDialog(parentActivity, step, guic);
		vd.show();
		
		return true;
	}

}
