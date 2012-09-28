package kvhc.adrumdrum.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import kvhc.player.SoundManager;

public class SoundManagerTest extends AndroidTestCase {

	public void testSoundManager() {
		SoundManager manager = new SoundManager();
		
		Assert.assertNotNull(manager);
	}
}
