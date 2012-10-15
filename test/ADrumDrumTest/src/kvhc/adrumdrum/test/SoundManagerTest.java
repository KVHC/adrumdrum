package kvhc.adrumdrum.test;

import android.test.AndroidTestCase;
import kvhc.player.SoundManager;
import junit.framework.Assert;

/**
 * Test class for kvhc.player.SoundManager.
 * @author kvhc
 */
public class SoundManagerTest extends AndroidTestCase {
	/**
	 * Tests the initiation of a SoundManager.
	 */
	public void testInitSounds() {
		SoundManager testSM = new SoundManager();		
		testSM.initSounds(this.getContext());
		Assert.assertNotNull(testSM);
	}
}
