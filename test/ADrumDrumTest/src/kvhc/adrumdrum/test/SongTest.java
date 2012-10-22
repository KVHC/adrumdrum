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

import java.util.ArrayList;
import java.util.Random;

import junit.framework.Assert;
import kvhc.models.Channel;
import kvhc.models.Song;
import android.test.AndroidTestCase;

/**
 * Test class for kvhc.player.Song
 * 
 * @author kvhc
 *
 */
public class SongTest extends AndroidTestCase {

	private static final int NUMBER_OF_CHANNELS = 4;
	
	/**
	 * Tests constructor with numChannels-parameter
	 */
	public void testFirstConstructor() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		Assert.assertEquals(NUMBER_OF_CHANNELS, s.getNumberOfChannels());
		Assert.assertNotNull(s);
		Assert.assertNull(s.getChannel(0).getSound());
	}
	
	/**
	 * Tests constructor that takes a list of Channels.
	 */
	public void testSecondConstructor() {
		// If you send an invalid value, a Song should be created anyway
		Song s = new Song(null);
		Assert.assertNotNull(s);
		
		//Init a random number
		int randomNumberOfChannels = new Random().nextInt(NUMBER_OF_CHANNELS*2);
		
		//Make a list of Channels
		ArrayList<Channel> listOfChannels = new ArrayList<Channel>(randomNumberOfChannels);
		for (int i=0;i<randomNumberOfChannels;i++) {
			Channel c = new Channel();
			listOfChannels.add(c);
		}
		//Make a new song with the list of channels
		s = new Song(listOfChannels);
		
		//Test if it is the right number of Channels in the Song
		Assert.assertEquals(randomNumberOfChannels, s.getNumberOfChannels());
		
	}
	
	/**
	 * Tests setChannels(List<Channel> channels)
	 */
	public void testsetChannels() {
		//Init a Song.
		Song s = new Song(NUMBER_OF_CHANNELS);
		//Init a random number
		int randomNumberOfChannels = new Random().nextInt(NUMBER_OF_CHANNELS*2);
		
		//Make a list of Channels
		ArrayList<Channel> listOfChannels = new ArrayList<Channel>(randomNumberOfChannels);
		for (int i=0;i<randomNumberOfChannels;i++) {
			Channel c = new Channel();
			listOfChannels.add(c);
		}
		//Set the Song to use these channels
		s.setChannels(listOfChannels);
		
		//Test if it is the right number of Channels in the Song
		Assert.assertEquals(randomNumberOfChannels, s.getNumberOfChannels());
	}
	
	/**
	 * Tests removeChannel(int channelIndex)
	 * @throws Exception 
	 */
	public void testRemoveChannel() throws Exception {
		Song s = new Song(NUMBER_OF_CHANNELS);
		Assert.assertEquals(NUMBER_OF_CHANNELS, s.getNumberOfChannels());
		s.removeChannel(s.getNumberOfChannels()-1);
		s.removeChannel(s.getNumberOfChannels()-1);
		s.removeChannel(s.getNumberOfChannels()-1);
		Assert.assertEquals(1, s.getNumberOfChannels());
		
		//Test to remove a Channel that doesn't exist
		boolean didItRemoveAChannel = s.removeChannel(-1);
		
		//Nothing should have happen
		Assert.assertEquals(1, s.getNumberOfChannels());
		
		//Test if it returns false as expected
		Assert.assertEquals(false, didItRemoveAChannel);
	}
	
	/**
	 * Tests addChannel()
	 */
	public void testAddChannel() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		Assert.assertEquals(NUMBER_OF_CHANNELS, s.getNumberOfChannels());
		s.addChannel();
		s.addChannel();
		Assert.assertEquals(NUMBER_OF_CHANNELS + 2, s.getNumberOfChannels());
	}
	
	/**
	 * Tests addChannel(Channel)
	 */
	public void testAddChannel2() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		s.addChannel(new Channel());
		Assert.assertEquals(NUMBER_OF_CHANNELS+1, s.getNumberOfChannels());
	}
	
	/**
	 * Tests addSteps(int)
	 */
	public void testAddSteps() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		int defaultNumberOfSteps = s.getChannel(0).getNumberOfSteps();
		s.addSteps(1);
		s.addSteps(2);
		Assert.assertEquals(defaultNumberOfSteps+3, s.getNumberOfSteps());	
	}
	
	/**
	 * Tests clearAllSteps()
	 */
	public void testClearAllSteps() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		s.getChannel(0).setStep(0, true);
		s.getChannel(0).setStep(1, true);
		s.clearAllSteps();
		
		Assert.assertEquals(false, s.getChannel(0).getStepAt(0).isActive());
		Assert.assertEquals(false, s.getChannel(0).getStepAt(1).isActive());
	}
	
	/**
	 * Tests setName & getName
	 */
	public void testSetAndGetName() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		String name = "doobeedoo";
		s.setName(name);
		
		Assert.assertEquals(name, s.getName());
	}
	
	/**
	 * Tests setId & getId
	 */
	public void testSetAndGetId() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		long id = 123456789;
		s.setId(id);
		
		Assert.assertEquals(id, s.getId());
	}
	
	/**
	 * Tests setBPM & getBPM
	 */
	public void testSetAndGetBpm() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		int bpm = 70;
		s.setBpm(bpm);
		
		Assert.assertEquals(bpm, s.getBpm());
	}
	
	/**
	 * Tests playAll()
	 */
	public void testPlayAll() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		s.getChannel(1).setMute(true);
		s.getChannel(2).setMute(true);
		
		s.playAll();
		
		Assert.assertEquals(false, s.getChannel(1).isMuted());
		Assert.assertEquals(false, s.getChannel(2).isMuted());
	}
	
	/**
	 * Tests removeSteps(int)
	 */
	public void testRemoveSteps() {
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		int defaultNumberOfSteps = s.getNumberOfSteps();
		s.removeSteps(1);
		s.removeSteps(2);
		
		Assert.assertEquals(defaultNumberOfSteps-3, s.getNumberOfSteps());
	}
	
	/**
	 * Tests equals(Object)
	 */
	public void testEquals() {
		String testString = "Test String";
		
		//Make three new Songs. 
		// s1==s2, s1!=s3, s2!=s3
		Song s1 = new Song(NUMBER_OF_CHANNELS);
		Song s2 = new Song(NUMBER_OF_CHANNELS);
		Song s3 = new Song(NUMBER_OF_CHANNELS+2);
		
		// Set some values
		s1.setName(testString);
		s2.setName(testString);
		s3.setName(testString+"test");
		s1.setId(1);
		s2.setId(1);
		s3.setId(2);

		// Test equals on them
		Assert.assertEquals(true, s1.equals(s2));
		Assert.assertEquals(true, s2.equals(s1));
		Assert.assertEquals(false, s1.equals(s3));
		Assert.assertEquals(false, s3.equals(s1));
		Assert.assertEquals(false, s2.equals(s3));
		Assert.assertEquals(false, s3.equals(s2));
		
		//Test against some random other objects
		Assert.assertEquals(false, s1.equals(testString));
		Assert.assertEquals(false, s3.equals(new Channel()));
	}
	
	/**
	 * Test muteAllChannelsExcept(channelindex) 
	 */
	public void testmuteAllChannelsExcept() {
		//Make a new song
		Song s = new Song(NUMBER_OF_CHANNELS);
		
		//These shouldn't be muted
		Assert.assertEquals(false, s.getChannel(0).isMuted());
		Assert.assertEquals(false, s.getChannel(1).isMuted());
		
		//Mute all except this one
		s.muteAllChannelsExcept(NUMBER_OF_CHANNELS-2);
		
		//Check some
		Assert.assertEquals(true, s.getChannel(0).isMuted());
		Assert.assertEquals(false, s.getChannel(NUMBER_OF_CHANNELS-2).isMuted());
		
		//This should be 1 when we check later
		int numberOfMutedChannels = 0;
		
		for(Channel c : s.getChannels()) {
			if (!c.isMuted()) {
				numberOfMutedChannels++;
			}
		}
		// Check if just one was muted
		Assert.assertEquals(1, numberOfMutedChannels);
	}

}
