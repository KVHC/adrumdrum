package kvhc.adrumdrum.test;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import kvhc.models.Channel;
import kvhc.models.Step;
import kvhc.util.db.SongDataSource;
import kvhc.util.db.StepDataSource;
import android.database.SQLException;
import android.test.AndroidTestCase;

/**
 * Unit test class for StepDataSource.
 * 
 * Tests all the important functions. 
 * 
 * @author srejv
 *
 */
public class StepDataSourceTest extends AndroidTestCase {

	/**
	 * Tests the constructor for something.
	 */
	public void testConstructor() {
		// Test null input
		StepDataSource songs = new StepDataSource(null);
		Assert.assertNotNull(songs);
		// Test member context (androidTestCase, I think this is the one
		// we want to use for the rest of the tests?
		songs = new StepDataSource(mContext);
		Assert.assertNotNull(songs);
		// Tests getContext() as input.
		songs = new StepDataSource(getContext());
		Assert.assertNotNull(songs);
	}
	
	/**
	 * Tests if its possible to connect to the database.
	 */
	public void testOpen() {
		// Set up.
		StepDataSource steps = new StepDataSource(getContext());
		
		// Test.
		steps.open();
		Assert.assertTrue(steps.isOpened());
		
		// Tear down.
		steps.close();
	}
	
	/**
	 * Tests if it's possible to close the database connection.
	 */
	public void testClose() {
		// Set up.
		StepDataSource steps = new StepDataSource(getContext());
		steps.open();
		
		// Test.
		steps.close();
		Assert.assertFalse(steps.isOpened());
		
		// Tear down.
	}
	
	/**
	 * Tests the input and ouput of the deleteStep(Step step) method.
	 */
	public void testDeleteStep() {
		// Set up
		Random random = new Random();
		Channel channel = new Channel();
		channel.setId(random.nextInt());
		Step step;
		StepDataSource steps = new StepDataSource(getContext());
		// Test exception when database not opened
		boolean didThrow = false;
		try {
			steps.deleteStep(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Now open the database for the rest of the test.
		steps.open();
		
		// Test delete null input.
		int expected = 0; // Exptect to delete zero since we don't send a step in.
		int numberOfStepsDeleted = steps.deleteStep(null);
		Assert.assertEquals(expected, numberOfStepsDeleted);
		
		// Test delete empty step input.
		step = new Step();
		numberOfStepsDeleted = steps.deleteStep(step);
		Assert.assertEquals(expected, numberOfStepsDeleted);
		
		// Test delete saved step.
		step = new Step();
		steps.save(step, channel);
		numberOfStepsDeleted = steps.deleteStep(step);
		Assert.assertEquals(1, numberOfStepsDeleted);
		
		// Test delete loaded step.
		// Test for exceptions?
		int numberOfSteps = random.nextInt(128);
		for(int i = 0; i < numberOfSteps; i++) {
			step = new Step();
			steps.save(step, channel);
		}
		List<Step> loadedSteps = steps.getAllStepsForChannel(channel);
		int deletedSum = 0; 
		for(Step s : loadedSteps) {
			deletedSum += steps.deleteStep(s);
		}
		Assert.assertEquals(numberOfSteps, deletedSum);
		
		// Tear down.
		steps.close();
	}
	
	/**
	 * Tests the input and ouput of the getAllStepsForChannel(Channel channel) method.
	 */
	public void testGetAllStepsForChannel() {
		// Set up 
		Random random = new Random();
		Channel channel = new Channel();
		channel.setId(random.nextInt());
		int expectedNumberOfSteps = random.nextInt();
		StepDataSource steps = new StepDataSource(getContext());
		for(int i = 0; i < expectedNumberOfSteps; i++) {
			steps.save(new Step(), channel);
		}
		
		// Test exception when database not opened
		boolean didThrow = false;
		try {
			steps.getAllStepsForChannel(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Open steps for the rest of the test
		steps.open();
		
		// Test with null
		List<Step> testList = steps.getAllStepsForChannel(null);
		Assert.assertEquals(0, testList.size());
		
		// Test with no id channel
		testList = steps.getAllStepsForChannel(new Channel());
		Assert.assertEquals(0, testList.size());
		
		// Test with real channel
		testList = steps.getAllStepsForChannel(channel);
		Assert.assertEquals(expectedNumberOfSteps, testList.size());
		
		// Tear down
		for(Step step : testList) {
			steps.deleteStep(step);
		}
		steps.close();
	}
	
	/**
	 * Tests the input and ouput of the save(Step step, Channel channel) method.
	 */
	public void testSave() {
		// Set up 
		Random random = new Random();
		Channel channel = new Channel();
		channel.setId(random.nextInt());
		Step step = new Step();
		StepDataSource steps = new StepDataSource(getContext());
		long errorCodeNumber = -1;
		long updateNumber = 1;
		long createNumber = 2;
		
		// Test exception when database not opened
		boolean didThrow = false;
		try {
			steps.save(null, null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Open steps for the rest of the test.
		steps.open();
		
		// Test with null.
		long successCode = steps.save(null, null);
		assertEquals(errorCodeNumber, successCode);
		
		// Test with no id channel.
		successCode = steps.save(null, new Channel());
		Assert.assertEquals(errorCodeNumber, successCode);
		
		// Test with real channel.
		successCode = steps.save(null, channel);
		Assert.assertEquals(errorCodeNumber, successCode);

		// Test with real step, new channel.
		successCode = steps.save(step, new Channel());
		Assert.assertEquals(createNumber, successCode);
		
		// Test with unsaved step for creation.
		step = new Step();
		successCode = steps.save(step, channel);
		Assert.assertEquals(updateNumber, successCode);
		steps.deleteStep(step);
		
		// Test with real channel saved step thing.
		step = new Step();
		steps.save(step, channel);
		successCode = steps.save(step, channel);
		Assert.assertEquals(updateNumber, successCode);
		steps.deleteStep(step);
		
		// Test if step gets the ID from the saving.
		step = new Step();
		step.setActive(true);
		step.setStepNumber(random.nextInt());
		step.setVelolcity(random.nextFloat());
		Assert.assertEquals(0, step.getId());
		steps.save(step, channel);
		Assert.assertTrue(step.getId() > 0);
		
		// Test that  the step keeps it's ID after saving again.
		long previousId = step.getId();
		steps.save(step, channel);
		Assert.assertEquals(previousId, step.getId());
		steps.deleteStep(step);
		
		// Tear down
		steps.close();
	}
}
