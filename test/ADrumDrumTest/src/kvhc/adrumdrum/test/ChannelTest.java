package kvhc.adrumdrum.test;

import junit.framework.Assert;
import kvhc.player.Channel;
import android.test.AndroidTestCase;

public class ChannelTest extends AndroidTestCase {
	
	
	public void testChannel() {
		Channel c = new Channel();
		int soundId = 5;
		int stepNumber = 16;
		
		
		Assert.assertNotNull(c);
		
		c = new Channel(soundId);
		
		Assert.assertNotNull(c);
		Assert.assertEquals(soundId, c.GetSound());
		
		c = new Channel(1, 16);
		
		Assert.assertNotNull(c);
		Assert.assertEquals(soundId, c.GetSound());
		Assert.assertEquals(stepNumber, c.GetNumberOfSteps());
	}
}
