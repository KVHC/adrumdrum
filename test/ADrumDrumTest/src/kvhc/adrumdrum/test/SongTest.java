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
		
		Assert.assertEquals(4, s.GetNumberOfChannels());
		Assert.assertNotNull(s);
		Assert.assertNull(s.GetChannel(0).GetSound());
	}
	
	/**
	 * Tests removeChannel(int channelIndex)
	 * @throws Exception 
	 */
	public void testRemoveChannel() throws Exception {
		Song s = new Song(4);
		
		Assert.assertEquals(4, s.GetNumberOfChannels());
		s.RemoveChannel(s.GetNumberOfChannels()-1);
		s.RemoveChannel(s.GetNumberOfChannels()-1);
		s.RemoveChannel(s.GetNumberOfChannels()-1);
		Assert.assertEquals(1, s.GetNumberOfChannels());
	}
	
	/**
	 * Tests addChannel()
	 */
	public void testAddChannel() {
		Song s = new Song(4);
		
		Assert.assertEquals(4, s.GetNumberOfChannels());
		s.AddChannel();
		s.AddChannel();
		Assert.assertEquals(6, s.GetNumberOfChannels());
	}
	
}


