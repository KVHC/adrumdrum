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
		Assert.assertFalse(testPlayer.isPlaying());
	}
	
	/**
	 * Asserts that when the play-method is called the song plays
	 * and also checks that stopping notifies observers.
	 */
	public void testPlayAndStop(){
		//init
		count=0;
		Player testPlayer = new Player(getContext());
		ArrayList<Channel> testList = new ArrayList<Channel>();
		testList.add(new Channel(new Sound(1,1,"test"),1));
		testPlayer.addObserver(this);
		
		testPlayer.play();
		Assert.assertTrue(testPlayer.isPlaying());
		//stopping the player should notify observers
		testPlayer.stop();
		//if update has been called this should be true
		Assert.assertTrue(count>0);
		Assert.assertFalse(testPlayer.isPlaying());
		//after stopping, current step should be 0
		Assert.assertEquals(0, testPlayer.getActiveStep());
	}

	
	/**
	 * Implementation of the update function from the Observable interface
	 * used to assert that Player correctly notifies its observers.
	 */
	public void update(Observable observable, Object data) {
		count++;
		
	}
	
	
	
}
