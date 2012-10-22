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

import java.util.ArrayList;
import java.util.Random;
import kvhc.models.Channel;
import kvhc.models.Sound;
import kvhc.models.Step;

/**
 * Test class for kvhc.models.Channel.
 * Tests a number of different things, for example changing the number
 * of steps, the constructors and the panning functionality of Channel.
 * @author kvhc
 *
 */
public class ChannelTest extends AndroidTestCase {
    
	private String testString = "test";
	private static final int DEFAULT_NUMBER_OF_STEPS=16;
	private static final int TEST_NUMBER_OF_STEPS=8;
	private static final float PERCENT_100 = 1.0f;
	private static final float PERCENT_70 = 0.7f;
	private static final float PERCENT_30 = 0.3f;
	private static final float TEST_VELOCITY = 0.4f;
	private static final float DEFAULT_VELOCITY = 0.7f;
	private static final float RIGHT_PANNING = 0.2f;
	private static final float LEFT_PANNING = 0.8f;
	private static final int MAX_NUMBER_OF_TRIES = 1000;
	private static final int BIG_NUMBER = 1000000;
	
	/**
     * Test the parameterless constructor.
     */
    public void testFirstConstructor() {
        Channel c = new Channel();
        Assert.assertNotNull(c);
        Assert.assertEquals(DEFAULT_NUMBER_OF_STEPS, c.getNumberOfSteps());
        Assert.assertNull(c.getSound());
    }
    /**
     * Test the constructor which takes only a sound object.
     */
    public void testSecondConstructor() {
        //Init
    	Sound testSound = new Sound(1, testString);
        Channel testChannel = new Channel(testSound);
        //Assert
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(DEFAULT_NUMBER_OF_STEPS, testChannel.getNumberOfSteps());
        Assert.assertNotNull(testChannel.getSound());
        Assert.assertEquals(testString, testChannel.getSound().getName());
    }
    /**
     * Test the constructor which takes a sound object and a number of steps.
     */
    public void testThirdConstructor() {
        Sound testSound = new Sound(1, testString);
        Channel testChannel = new Channel(testSound,TEST_NUMBER_OF_STEPS);
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(TEST_NUMBER_OF_STEPS, testChannel.getNumberOfSteps());
        Assert.assertNotNull(testChannel.getSound());
        Assert.assertEquals(testString, testChannel.getSound().getName());
    }
    /**
     * Test the IsStepActive method for all steps.
     */
    public void testIsStepActive() {
        Channel testChannel = new Channel();
        testChannel.clearAllSteps();
        //test for all steps
        for (int i = 0; i<DEFAULT_NUMBER_OF_STEPS; i++) {
            Assert.assertEquals(testChannel.isStepActive(i),
            		testChannel.getSteps().get(i).isActive());
        }
        // test if a step that doesn't exist isn't active. 
        Assert.assertEquals(false, testChannel.isStepActive(DEFAULT_NUMBER_OF_STEPS+1));
    }
    /**
     * Test functionality related to panning, by using the setPanning method
     * of Channel.
     */
    public void testPanning() {
        Channel testChannel = new Channel();
        float rightTestLevel = new Random().nextFloat();
        float leftTestLevel = new Random().nextFloat();
        testChannel.setPanning(rightTestLevel, leftTestLevel); 
        Assert.assertEquals(rightTestLevel, testChannel.getRightPanning());
        Assert.assertEquals(leftTestLevel, testChannel.getLeftPanning());
    }
    /**
     * Tests the resizeBy method in 3 different ways.
     * First it tries to add and then remove a step from Channel
     * with the default number of steps, and then it tries to
     * remove more steps than the Channel currently has.
     */
    public void testResize() {
        //Test adding a step
        Channel testChannel = new Channel();
        int before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(1);
        int after = testChannel.getNumberOfSteps();
        Assert.assertEquals(before + 1, after);
        //Test removing a step
        before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(-1);
        after = testChannel.getNumberOfSteps();
        Assert.assertEquals(before - 1, after);
        //Test removing to more steps than the Channels current number of steps
        before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(0 - (before + 1));
        after = testChannel.getNumberOfSteps();
        Assert.assertEquals(before, after);
    }
    
	/**
	 * Tests multiStepVelocitySpike()
	 */
	public void testMultiStepVelocitySpike() {
		Channel c = new Channel();
		
		int middleStep = DEFAULT_NUMBER_OF_STEPS/2; 
		c.multiStepVelocitySpike(middleStep);
		
		
		Assert.assertEquals(PERCENT_100, c.getStepAt(middleStep).getVelocity());
		Assert.assertEquals(PERCENT_70, c.getStepAt(middleStep-1).getVelocity());
		Assert.assertEquals(PERCENT_70, c.getStepAt(middleStep+1).getVelocity());
		Assert.assertEquals(PERCENT_30, c.getStepAt(middleStep-2).getVelocity());
		Assert.assertEquals(PERCENT_30, c.getStepAt(middleStep+2).getVelocity());	
	}
	
	/**
	 * Tests toggleStep(int)
	 */
	public void testToggleStep() {
		Channel c = new Channel();
		c.setStep(0, true);
		c.toggleStep(0);
		c.toggleStep(1);
		
		Assert.assertEquals(false, c.isStepActive(0));
		Assert.assertEquals(true, c.isStepActive(1));
	}
	
	/**
	 * Tests setVolume & getVolume()
	 */
	public void testSetAndGetVolume() {
		Channel c = new Channel();
		Random random = new Random();
		int randomAmountOfTries = random.nextInt(MAX_NUMBER_OF_TRIES);
		for(int i = 0; i < randomAmountOfTries; i++) {
			float testInput = random.nextFloat();
			c.setVolume(testInput);
			Assert.assertEquals(testInput, c.getChannelVolume());
		}
		
		// Set channel to mute and see if it returns 0.0f volume
		c.setMute(true);
		Assert.assertEquals(0.0f, c.getVolume());
	}
	
	/**
	 * Tests setSound(Sound)
	 */
	public void testSetAndGetSound() {
		Channel c = new Channel();
		Sound s = new Sound(1);
		c.setSound(s);
		Assert.assertEquals(s, c.getSound());
	}
	
	/**
	 * Tests setStep(int step, boolean active, float velocity)
	 */
	public void testSetStep() {
		Channel c = new Channel();
		int step = c.getNumberOfSteps()/2;
		c.setStep(step, true, TEST_VELOCITY);
		Assert.assertEquals(true, c.isStepActive(step));
		Assert.assertEquals(TEST_VELOCITY, c.getStepAt(step).getVelocity());
		
		Assert.assertEquals(null, c.getStepAt(-1));
	}
	
	/**
	 * Tests getVolumeRight(step)
	 */
	public void testGetVolumeRight() {
		// Init a test Channel and set panning
		Channel c = new Channel();
		c.setPanning(RIGHT_PANNING, LEFT_PANNING);
		// Expected values
		float expectedVolumeRight = c.getVolume() * RIGHT_PANNING * TEST_VELOCITY;
		float expectedVolumeDefault = c.getVolume() * c.getRightPanning() * DEFAULT_VELOCITY;
		int step = c.getNumberOfSteps()/2;
		c.setStep(step, true, TEST_VELOCITY);
		Assert.assertEquals(expectedVolumeRight, c.getVolumeRight(step));
		Assert.assertEquals(expectedVolumeDefault, c.getVolumeRight(step+1));
		
		//Set channel to mute and check it it returns 0.0f as volume
		c.setMute(true);
		Assert.assertEquals(0.0f, c.getVolumeRight(step));
		Assert.assertEquals(0.0f, c.getVolumeRight(step+1));
		c.setMute(false);
		
		//Check if it return 0.0f in volume if the step doesn't exists
		Assert.assertEquals(0.0f, c.getVolumeLeft(-1));
	}
	
	/**
	 * Tests getVolumeLeft(step)
	 */
	public void testGetVolumeLeft() {
		// Init a test Channel and set panning
		Channel c = new Channel();
		c.setPanning(RIGHT_PANNING, LEFT_PANNING);
		// Expected values
		float expectedVolumeLeft = c.getVolume() * LEFT_PANNING * TEST_VELOCITY;
		float expectedVolumeDefault = c.getVolume() * LEFT_PANNING * DEFAULT_VELOCITY;
		int step = c.getNumberOfSteps()/2;
		c.setStep(step, true, TEST_VELOCITY);
		Assert.assertEquals(expectedVolumeLeft, c.getVolumeLeft(step));
		Assert.assertEquals(expectedVolumeDefault, c.getVolumeLeft(step+1));
		
		//Set channel to mute and check it it returns 0.0f as volume
		c.setMute(true);
		Assert.assertEquals(0.0f, c.getVolumeLeft(step));
		Assert.assertEquals(0.0f, c.getVolumeLeft(step+1));
		c.setMute(false);
		
		//Check if it return 0.0f in volume if the step doesn't exists
		Assert.assertEquals(0.0f, c.getVolumeLeft(-1));
	}
	
	/**
	 * Test getChannelNumber() and setChannelNumber
	 */
	public void testChannelNumber() {
		Channel c = new Channel();
		int random = new Random().nextInt(BIG_NUMBER);
		c.setChannelNumber(random);
		Assert.assertEquals(random, c.getChannelNumber());
	}
	
	/**
	 * Tests setSteps(List<Step> steps)
	 */
	public void testSetSteps() {
		//Init a random number of steps
		int randomNumberOfSteps = new Random().nextInt(DEFAULT_NUMBER_OF_STEPS);
		//Init a new Channel
		Channel c = new Channel();
		//Make a list of steps
		ArrayList<Step> listOfSteps = new ArrayList<Step>(randomNumberOfSteps);
		for (int i=0;i<randomNumberOfSteps;i++) {
			Step s = new Step(i);
			listOfSteps.add(s);
		}
		//Set the channel to use these steps
		c.setSteps(listOfSteps);
		
		//Test if it is the right number of steps
		Assert.assertEquals(randomNumberOfSteps, c.getNumberOfSteps());
	}
}

