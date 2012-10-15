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
