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

package kvhc.util;
import android.os.Handler;

/***
 * A class representing a timer
 * @author Niclas 
 */
public class AndroidTimer {

	private Handler handler;
	private long time;
	private Runnable job;
	private boolean enabled;
	
	/**
	 * Constructor. The timer are not started until the start() method are called
	 * 
	 * @param handler: A handler that executes the current task
	 * @param job: A Runnable object. This objects run method will be executed 
	 * every time the timer ticks
	 * @param time: How long time between every timer tick, the time are in milliseconds
	 */
	public AndroidTimer(Handler handler, Runnable job, long time) {
		this.handler = handler;
		this.job = job;
		this.time = time;
	}
	
	/**
	 * Constructor. The timer are not started until the start() method are called
	 * 
	 * @param job: A Runnable object. This objects run method will be executed 
	 * every time the timer ticks
	 * @param time: How long time between every timer tick, the time are in milliseconds
	 */
	public AndroidTimer(Runnable job, long time) {
		this(new Handler(), job, time);
	}
	
	/**
	 * Stops the timer
	 */
	public void stop(){
		enabled = false;
	}
	
	/**
	 * Starts the timer
	 */
	public void start() {
		
		if (enabled) {
			return;
		}
		enabled = true;
		handler.postDelayed(timer,time);
	}
	
	/**
	 * Checks if the timer are running
	 * @return True if the timer are running, else false
	 */
	public boolean running() {
		return enabled;
	}
	
	/**
	 * Change the Runnable object that are used every timer tick
	 * @param job: A Runnable object that should be used eery timer tick
	 */
	public void changeJob(Runnable job) {
		this.job = job;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * A private class used too loop the the job
	 */
	private Runnable timer = new Runnable() {
		public void run() {
			if (enabled) {
				handler.post(job);
				handler.postDelayed(this,time);
			}
		}
	};
}
