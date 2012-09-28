package kvhc.adrumdrum.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import kvhc.gui.GUIStepButton;

public class GUIStepTest extends AndroidTestCase {
	
	public void testGUIStep() {
		int channelId = 2;
		int stepId = 7;
		GUIStepButton button = new GUIStepButton(null, channelId, stepId);
		
		Assert.assertNotNull(button);
	}
	
	
}
