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

import android.test.AndroidTestCase;
import kvhc.models.Sound;
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
		Sound testSound = new Sound(1, "test");
		Assert.assertNotNull(testSound);
	}
	
	/**
	 * Tests to set a name and then get it. If it is the same - success!
	 * Also tests toString.
	 */
	public void testSetAndGetName() {
		String testName1 = "Test1";
		String testName2 = "Test2";
		Sound s = new Sound(1, testName1);
		
		// Test just getName()
		Assert.assertEquals(testName1, s.getName());
		// Set the name with setName
		s.setName(testName2);
		// Test if the change has been made
		Assert.assertEquals(testName2, s.getName());
		
		// Also test toString, which is basically the same as getName()
		Assert.assertEquals(testName2, s.toString());
	}
	
	/**
	 * Tests setSoundValue() & getSoundValue().
	 */
	public void testSetAndGetSoundValue() {
		Sound s = new Sound(1, "Test");
		Assert.assertEquals(1, s.getSoundValue());
		s.setSoundValue(0);
		Assert.assertEquals(0, s.getSoundValue());
	}
	
	/**
	 * Tests setId() & getId().
	 */
	public void testSetAndGetId() {
		Sound s = new Sound(1);
		Assert.assertEquals(1, s.getId());
		s.setId(0);
		Assert.assertEquals(0, s.getId());
	}
}
