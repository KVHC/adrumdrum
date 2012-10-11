package kvhc.adrumdrum.test;

import android.test.AndroidTestCase;
import kvhc.player.*;
import junit.framework.Assert;
import java.util.*;


/**
 * Test class for kvhc.player.Player
 * 
 * 
 * @author kvhc
 *
 */
public class PlayerTest extends AndroidTestCase {
	
	
	/**
	 * Tests the constructor 
	 */
	public void testConstructor (){
		Player testPlayer = new Player(getContext());
		Assert.assertNotNull(testPlayer);
	}
	
	/**
	 * Asserts that when the play-method is called the song plays
	 * 
	 */
	public void testPlay(){
		Player testPlayer = new Player(getContext());
		TestObserver testObserver = new TestObserver();
		testPlayer.addObserver(testObserver);
		testPlayer.Play();
		// I don't know why this keeps failing,
		// but I think the test is broken, perhaps
		// I'm not initiating the class correctly
		try{
			Thread.sleep(10000);
		}catch(Exception e){
			Assert.fail("Thread.sleep() threw exception");
		}
		Assert.assertTrue(testObserver.getCount()>0);
		
		Assert.assertEquals(testPlayer.IsPlaying(),true);
	}
	
	
	
}
