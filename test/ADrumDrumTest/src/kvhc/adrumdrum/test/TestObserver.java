package kvhc.adrumdrum.test;

import java.util.Observable;
import java.util.Observer;


/**
 * Test class used for observable classes
 * @author kvhc
 *
 */
public class TestObserver implements Observer {
	
	int count;
	
	public TestObserver(){
		//super();
		count = 0;
	}
	
	public void update(Observable observable, Object data) {
		count++;
	}
	
	public int getCount(){
		return count;
	}

}
