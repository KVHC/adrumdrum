package kvhc.adrumdrum.test;

import junit.framework.Assert;
import android.test.*;

import kvhc.player.Channel;
import kvhc.player.Sound;

/**
 * Test class for kvhc.player.Channel
 * 
 * @author kvhc
 *
 */
public class ChannelTest extends AndroidTestCase {
    /**
     * Test the parameterless constructor
     */
    public void testFirstConstructor(){
        Channel c = new Channel();
        
        Assert.assertNotNull(c);
        Assert.assertEquals(16, c.getNumberOfSteps());
        Assert.assertNull(c.getSound());
    }
    
    /**
     * Test the constructor which gets a sound object 
     */
    public void testSecondConstructor(){
        Sound testSound = new Sound(1,1, "test");
        Channel testChannel = new Channel(testSound);
        
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(16, testChannel.getNumberOfSteps());
        Assert.assertNotNull(testChannel.getSound());
        Assert.assertEquals("test", testChannel.getSound().getName());
    }
    
    /**
     * Test the counstructor which gets a sound object and a number of steps
     */
    public void testThirdConstructor(){
        Sound testSound = new Sound(1,1, "test");
        Channel testChannel = new Channel(testSound,8);
        
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(8, testChannel.getNumberOfSteps());
        Assert.assertNotNull(testChannel.getSound());
        Assert.assertEquals("test", testChannel.getSound().getName());
    }
    
    /**
     * Test the IsStepActive method
     */
    public void testIsStepActive(){
        Channel testChannel = new Channel();
        testChannel.clearAllSteps();
        
        for (int i = 0; i<16;i++){
            Assert.assertEquals(testChannel.isStepActive(i), testChannel.getSteps().get(i).isActive());
        }   
    }
    
    /**
     * Test functionality related to panning
     */
    public void testPanning(){
        Channel testChannel = new Channel();
        float RIGHTTESTLEVEL = (float)0.20;//SHOULD THIS BE RANDOM?
        float LEFTTESTLEVEL = (float)0.30;
        testChannel.setPanning(RIGHTTESTLEVEL, LEFTTESTLEVEL);
        
        Assert.assertEquals(RIGHTTESTLEVEL, testChannel.getRightPanning());
        Assert.assertEquals(LEFTTESTLEVEL, testChannel.getLeftPanning());
    }
    
    /**
     * Test resizing
     */
    public void testResize(){
        //Test adding a step
        Channel testChannel = new Channel();
        int before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(1);
        int after = testChannel.getNumberOfSteps();
        
        Assert.assertEquals(before+1,after);
        
        //Test removing a step
        before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(-1);
        after = testChannel.getNumberOfSteps();
        
        Assert.assertEquals(before-1,after);
        
        //Test removing to more steps than the Channels current number of steps
        before = testChannel.getNumberOfSteps();
        testChannel.resizeBy(0-(before+1));
        after = testChannel.getNumberOfSteps();
        
        Assert.assertEquals(before, after);
    }
    
}

