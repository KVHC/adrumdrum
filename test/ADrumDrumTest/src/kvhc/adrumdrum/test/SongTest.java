/**
 * aDrumDrum is a step sequencer for Android.
 * Copyright (C) 2012  Daniel Fallstrand, Niclas Ståhl, Oscar Dragén and Viktor Nilsson.
 *
 * This file is part of aDrumDrum.
 *
 * aDrumDrum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aDrumDrum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aDrumDrum.  If not, see <http://www.gnu.org/licenses/>.
 */
package kvhc.adrumdrum.test;

import junit.framework.Assert;
import kvhc.models.Song;
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


