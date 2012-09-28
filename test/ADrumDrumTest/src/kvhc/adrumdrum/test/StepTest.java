package kvhc.adrumdrum.test;


import junit.framework.Assert;
import android.test.AndroidTestCase;

import kvhc.player.Step;

public class StepTest extends AndroidTestCase {

	
	public void testStep() {
		Assert.assertNotNull(new Step());
	}

	public void testStepBoolean() {
		Step step = new Step(true);
		
		Assert.assertTrue(step.IsActive());
	}

	public void testSetActive() {
		Step step = new Step();
		Assert.assertFalse(step.IsActive());
		
		step.SetActive(true);
		Assert.assertTrue(step.IsActive());
	}

	public void testSetVel() {
		Step step = new Step();
		
		float testVel = 0.333f; //new Random().nextFloat();
		step.SetVel(testVel);
		
		Assert.assertEquals("Current Velocity", testVel, step.GetVel());
	}
}
