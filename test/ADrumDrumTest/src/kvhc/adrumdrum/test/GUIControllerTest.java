package kvhc.adrumdrum.test;

import junit.framework.Assert;
import kvhc.gui.GUIController;
import android.test.AndroidTestCase;

public class GUIControllerTest extends AndroidTestCase {

	public void testGUIController() {
		GUIController controller = new GUIController(null, null);
		
		Assert.assertNotNull(controller);
	}
}
