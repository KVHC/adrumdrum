package kvhc.adrumdrum.test;

import android.test.AndroidTestCase;
import kvhc.player.Sound;
import junit.framework.Assert;

/**
 * Test class for kvhc.player.Sound .
 * @author kvhc
 */
public class SoundTest extends AndroidTestCase {
    /**
     * Tests that the creation of a Sound object works properly.
     */
	public void testConstructor() {
		Sound testSound = new Sound(1, 1, "test");
		Assert.assertNotNull(testSound);
	}
}
