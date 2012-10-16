/**
 * aDrumDrum is a stepsequencer for Android.
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
import kvhc.util.AndroidTimer;

/**
 * Test class for kvhc.util.AndroidTimer.
 * @author kvhc
 *
 */
public class AndroidTimerTest extends AndroidTestCase {
	int count=0;//used to test observable/observer-pattern
	/**
	 * Tests the constructor.
	 */
	public void testConstructor() {
		Runnable testJob = new Runnable() {
			public void run() {
				count++;
			}
		};
		AndroidTimer testAT = new AndroidTimer(testJob, (long) 1);
		Assert.assertNotNull(testAT);
	}
	/**
	 * Tests starting and stopping a timer.
	 */
	public void testStartStop() {
		Runnable testJob = new Runnable() {
			public void run() {
				count++;
			}
		};
		AndroidTimer testAT = new AndroidTimer(testJob, (long) 1);
		testAT.start();
		Assert.assertTrue(testAT.running());
		testAT.stop();
		Assert.assertFalse(testAT.running());
	}
	/**
	 * Tests changing the job while running.
	 * Also tests start/stop functionality afterwards.
	 */
	public void testChangingJobWhileRunning() {
		//first runnable to construct with
		Runnable testJob = new Runnable() {
			public void run() {
				count++;
			}
		};
		AndroidTimer testAT = new AndroidTimer(testJob, (long) 1);
		testAT.start();
		//new Runnable to change to
		Runnable testJob2 = new Runnable() {
			public void run() {
				count--;
			}
		};
		//changing Job shouldn't break anything
		try {
			testAT.changeJob(testJob2);
		} catch (Exception e) {
			Assert.fail("changeJob threw "
				+ "exception in AndroidTimerTest");
		}
		//after changing Job it should still be running
		Assert.assertTrue(testAT.running());
		testAT.stop();
		//here it should be stopped
		Assert.assertFalse(testAT.running());
	}
}
