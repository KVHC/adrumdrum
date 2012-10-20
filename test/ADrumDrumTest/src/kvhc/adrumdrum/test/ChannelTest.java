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
import java.util.Random;
import kvhc.models.Channel;
import kvhc.models.Sound;

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
}

