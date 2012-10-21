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
import android.test.AndroidTestCase;
import kvhc.models.Sound;
import kvhc.models.Step;
import kvhc.models.Channel;
import java.util.Random;

/**
 * Test class for kvhc.models.Step.
 * @author kvhc
 */
public class StepTest extends AndroidTestCase {

	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final float TEST_VELOCITY = 0.4f;
	private static final String TEST_STRING = "Test";
	
	/**
	 * Tests the constructor that takes a Channel and an Int.
	 */
	public void testStep() {
		Channel testChannel = new Channel();
		Assert.assertNotNull(new Step(testChannel, 1));
	}
	
	/**
	 * Tests empty constructor.
	 */
	public void testEmptyConstructor() {
		Assert.assertNotNull(new Step());
	}
	
	/**
	 * Tests the constructor which takes Boolean, Channel, Int,
	 * and sets the step active if the Boolean is true.
	 */
	public void testStepBoolean() {
		Channel testChannel = new Channel();
		Step step = new Step(true, testChannel, 1);
		Assert.assertTrue(step.isActive());
	}
	
	/**
	 * Tests the setActive method.
	 */
	public void testSetActive() {
		Channel testChannel = new Channel();
		Step step = new Step(testChannel,1);
		Assert.assertFalse(step.isActive());
		
		step.setActive(true);
		Assert.assertTrue(step.isActive());
	}
	
	/**
	 * Tests the setVelocity/getVelocity methods
	 */
	public void testSetVel() {
		Channel testChannel = new Channel();
		Step step = new Step(testChannel,1);
		float testVel = new Random().nextFloat();
		step.setVelolcity(testVel);
		
		Assert.assertEquals("Current Velocity", testVel, step.getVelocity());
	}
	
	/**
	 * Test set and get step number.
	 */
	public void testGetAndSetNumber() {
		Step s = new Step();
		int random = new Random().nextInt();
		s.setStepNumber(random);
		Assert.assertEquals(random, s.getStepNumber());
	}
	
	/**
	 * Tests setId() and getId()
	 */
	public void testGetAndSetID() {
		Step s = new Step();
		long random = new Random().nextLong();
		s.setId(random);
		Assert.assertEquals(random, s.getId());
	}
	
	/**
	 * Tests clone()
	 * Channel unfortunately doesn't implement equals/hashcode
	 */
	public void testClone() {
		// Init a step with a channel and stepid
		int randomStepId = new Random().nextInt()%DEFAULT_NUMBER_OF_STEPS;
		Step s1 = new Step(new Channel(), randomStepId);
		
		// Make some changes to s1
		s1.setVelolcity(TEST_VELOCITY);
		s1.setActive(true);
		
		// Clone it to a new Step
		Step s2 = s1.clone();
		
		// Check if it looks alike
		Assert.assertEquals(s1.getVelocity(), s2.getVelocity());
		Assert.assertEquals(s1.isActive(), s2.isActive());
		Assert.assertEquals(s1.getId(), s2.getId());
	}
	
}
