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
        Assert.assertEquals(16, c.GetNumberOfSteps());
        Assert.assertNull(c.GetSound());
    }
    
    /**
     * Test the constructor which gets a sound object 
     */
    public void testSecondConstructor(){
        Sound testSound = new Sound(1,1, "test");
        Channel testChannel = new Channel(testSound);
        
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(16, testChannel.GetNumberOfSteps());
        Assert.assertNotNull(testChannel.GetSound());
        Assert.assertEquals("test", testChannel.GetSound().GetName());
    }
    
    /**
     * Test the counstructor which gets a sound object and a number of steps
     */
    public void testThirdConstructor(){
        Sound testSound = new Sound(1,1, "test");
        Channel testChannel = new Channel(testSound,8);
        
        Assert.assertNotNull(testChannel);
        Assert.assertEquals(8, testChannel.GetNumberOfSteps());
        Assert.assertNotNull(testChannel.GetSound());
        Assert.assertEquals("test", testChannel.GetSound().GetName());
    }
    
    /**
     * Test the IsStepActive method
     */
    public void testIsStepActive(){
        Channel testChannel = new Channel();
        testChannel.clearAllSteps();
        
        for (int i = 0; i<16;i++){
            Assert.assertEquals(testChannel.IsStepActive(i), testChannel.GetSteps().get(i).IsActive());
        }   
    }
    
    /**
     * Test functionality related to panning
     */
    public void testPanning(){
        Channel testChannel = new Channel();
        float RIGHTTESTLEVEL = (float)0.20;//SHOULD THIS BE RANDOM?
        float LEFTTESTLEVEL = (float)0.30;
        testChannel.SetPanning(RIGHTTESTLEVEL, LEFTTESTLEVEL);
        
        Assert.assertEquals(RIGHTTESTLEVEL, testChannel.GetRightPanning());
        Assert.assertEquals(LEFTTESTLEVEL, testChannel.GetLeftPanning());
    }
    
    /**
     * Test resizing
     */
    public void testResize(){
        //Test adding a step
        Channel testChannel = new Channel();
        int before = testChannel.GetNumberOfSteps();
        testChannel.ResizeBy(1);
        int after = testChannel.GetNumberOfSteps();
        
        Assert.assertEquals(before+1,after);
        
        //Test removing a step
        before = testChannel.GetNumberOfSteps();
        testChannel.ResizeBy(-1);
        after = testChannel.GetNumberOfSteps();
        
        Assert.assertEquals(before-1,after);
        
        //Test removing to more steps than the Channels current number of steps
        before = testChannel.GetNumberOfSteps();
        testChannel.ResizeBy(0-(before+1));
        after = testChannel.GetNumberOfSteps();
        
        Assert.assertEquals(before, after);
    }
    
}

