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
import kvhc.models.Step;
import java.util.Random;

/**
 * Test class for kvhc.models.Step.
 * @author kvhc
 */
public class StepTest extends AndroidTestCase {
	
	// Constants for this class
	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final float TEST_VELOCITY = 0.4f;
	
	/**
	 * Tests the constructor that takes an Int.
	 */
	public void testStep() {
		Assert.assertNotNull(new Step(1));
	}
	
	/**
	 * Tests empty constructor.
	 */
	public void testEmptyConstructor() {
		Assert.assertNotNull(new Step());
	}
	
	/**
	 * Tests the constructor which takes Boolean and Int,
	 * and sets the step active if the Boolean is true.
	 */
	public void testStepBoolean() {
		Step step = new Step(true, 1);
		Assert.assertTrue(step.isActive());
	}
	
	/**
	 * Tests the setActive method.
	 */
	public void testSetActive() {
		Step step = new Step(1);
		Assert.assertFalse(step.isActive());
		
		step.setActive(true);
		Assert.assertTrue(step.isActive());
	}
	
	/**
	 * Tests the setVelocity/getVelocity methods
	 */
	public void testSetVel() {
		Step step = new Step(1);
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
	 */
	public void testClone() {
		// Init a step with a channel and stepid
		int randomStepId = new Random().nextInt()%DEFAULT_NUMBER_OF_STEPS;
		Step s1 = new Step(randomStepId);
		Step s2 = null;
		// Make some changes to s1
		s1.setVelolcity(TEST_VELOCITY);
		s1.setActive(true);
		
		// Clone it to a new Step
		try {
			s2 = s1.clone();
		} catch(CloneNotSupportedException e) {
			Assert.fail("Couldn't clone a step!");
		}
		
		// Check if it looks alike
		Assert.assertEquals(s1.getVelocity(), s2.getVelocity());
		Assert.assertEquals(s1.isActive(), s2.isActive());
		Assert.assertEquals(s1.getId(), s2.getId());
	}
	
}
