/**
 * aDrumDrum is a stepsequencer for Android.
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

import kvhc.player.*;

public class StepTest extends AndroidTestCase {
	
	Channel testChannel = new Channel();
	
	public void testStep() {
		Assert.assertNotNull(new Step(testChannel,1));
	}

	public void testStepBoolean() {
		Step step = new Step(true,testChannel,1);
		
		Assert.assertTrue(step.isActive());
	}

	public void testSetActive() {
		Step step = new Step(testChannel,1);
		Assert.assertFalse(step.isActive());
		
		step.setActive(true);
		Assert.assertTrue(step.isActive());
	}

	public void testSetVel() {
		Step step = new Step(testChannel,1);
		
		float testVel = 0.333f; //new Random().nextFloat();
		step.setVelolcity(testVel);
		
		Assert.assertEquals("Current Velocity", testVel, step.getVelocity());
	}
}
