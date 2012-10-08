package kvhc.gui;

import android.content.Context;
import android.widget.RadioButton;

public class GUIActiveStepButton extends RadioButton {

	int stepid;
	
	public GUIActiveStepButton(Context context, int stepid) {
		super(context);
		
		this.stepid = stepid;
	}

}
