package kvhc.adrumdrum.test;

import android.test.AndroidTestCase;
import kvhc.player.*;
import junit.framework.*;

/**
 * 
 * Test class for kvhc.player.SoundManager
 * 
 * @author kvhc
 *
 */
public class SoundManagerTest extends AndroidTestCase {
	
	/**
	 * Tests the initiation of a SoundManager
	 */
	public void testInitSounds() {
		SoundManager testSM = new SoundManager();
		Assert.assertNotNull(testSM);		
		testSM.initSounds(this.getContext());
	}
	
}
