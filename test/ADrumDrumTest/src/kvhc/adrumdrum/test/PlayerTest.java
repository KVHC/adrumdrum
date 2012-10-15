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
public class PlayerTest extends AndroidTestCase implements Observer {
	
	int count;
	
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
		count=0;
		Player testPlayer = new Player(getContext());
		ArrayList<Channel> testList = new ArrayList<Channel>();
		testList.add(new Channel(new Sound(1,1,"test"),1));
		testPlayer.addObserver(this);
		testPlayer.play();
		
		// I don't know why this keeps failing,
		// but I think the test is broken, perhaps
		// I'm not initiating the class correctly
		try{
			Thread.sleep(3000);
		} catch(Exception e){
			Assert.fail("Thread.sleep() threw exception in PlayerTest");
		}
		Assert.assertTrue(count>0);
		
		Assert.assertEquals(testPlayer.isPlaying(),true);
	}

	public void update(Observable observable, Object data) {
		count++;
		
	}
	
	
	
}
