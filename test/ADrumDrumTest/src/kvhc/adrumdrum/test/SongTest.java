package kvhc.adrumdrum.test;

import junit.framework.Assert;
import kvhc.player.Song;
import android.test.AndroidTestCase;

/**
 * Test class for kvhc.player.Song
 * 
 * @author kvhc
 *
 */
public class SongTest extends AndroidTestCase {

	/**
	 * Tests constructor with numChannels-parameter
	 */
	public void testFirstConstructor() {
		Song s = new Song(4);
		
		Assert.assertEquals(4, s.getNumberOfChannels());
		Assert.assertNotNull(s);
		Assert.assertNull(s.getChannel(0).getSound());
	}
	
	/**
	 * Tests removeChannel(int channelIndex)
	 * @throws Exception 
	 */
	public void testRemoveChannel() throws Exception {
		Song s = new Song(4);
		
		Assert.assertEquals(4, s.getNumberOfChannels());
		s.removeChannel(s.getNumberOfChannels()-1);
		s.removeChannel(s.getNumberOfChannels()-1);
		s.removeChannel(s.getNumberOfChannels()-1);
		Assert.assertEquals(1, s.getNumberOfChannels());
	}
	
	/**
	 * Tests addChannel()
	 */
	public void testAddChannel() {
		Song s = new Song(4);
		
		Assert.assertEquals(4, s.getNumberOfChannels());
		s.addChannel();
		s.addChannel();
		Assert.assertEquals(6, s.getNumberOfChannels());
	}
	
}


