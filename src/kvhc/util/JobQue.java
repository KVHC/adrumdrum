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

package kvhc.util;

import java.util.HashMap;
import java.util.HashSet;
import android.os.Handler;

/**
 * 
 * @author Niclas
 * A class that works lika a scheduler for Runnable objects
 */
public class JobQue {
	
	/**
	 * Instance Variables
	 * - A HashSet for storing the id of the current running jobs
	 * - A HashMap for storing jobs and having their key id as key
	 * - A Handeler for setting up jobs
	 * - A Integer that holds the id for the next job
	 */
	private HashSet<Integer> running;
	private HashMap<Integer, Job> jobs;
	private Handler handler;
	private static Integer id;
	
	/**
	 * The Constructor, just initialize the variables
	 */
	public JobQue(){
		running = new HashSet<Integer>();
		jobs = new HashMap<Integer,Job>();
		handler = new Handler();
		id = 0;
	}
	
	/**
	 * Add a new job to the scheduler
	 * @param job: The Runnable object thats run method should be invoked 
	 * @param time: How often the job should be done. The time are in milliseconds
	 * @return The id of the job.
	 */
	public Integer addJob(Runnable job, long time){
		
		Job j = new Job(id,job,time);
		running.add(id);
		jobs.put(id,j);
		id++;
		handler.postDelayed(j,time);
		return id -1;
	}
	
	/**
	 * Pauses a job.
	 * @param id: The id of the job
	 */
	public void pauseJob(Integer id){
		if (running.contains(id))
			running.remove(id);
	}
	
	/**
	 * Resume a job if it have been paused. The job will be executed first after the time paired with the job
	 * @param id: The id of the job
	 */
	public void resumeJob(Integer id){
		if(jobs.get(id) != null && !running.contains(id) && !jobs.get(id).isDying()){
			running.add(id);
			handler.postDelayed(jobs.get(id),jobs.get(id).getTime());
		}
	}
	
	/**
	 * Completely removes the job
	 * @param id The id of the job
	 */
	public void removeJob(Integer id){
		running.remove(id);
		jobs.get(id).kill();
	}
	
	/**
	 * Change the time of the job. The change will occur after the next executon of the job
	 * @param id: The id of the job
	 * @param time: How many milliseconds it should be between jobs 
	 */
	public void changeTime(Integer id, long time){
		if(jobs.get(id) != null){
			jobs.get(id).setTime(time);
		}
	}

	/**
	 * Run a job once after the given time
	 * @param job: The job that should be executed
	 * @param time: After how many milliseconds the job should  be executed
	 */
	public void runOnce(Runnable job,long time){
		handler.postDelayed(job,time);
	}
	
	/**
	 * Runs the job immediately
	 * @param job: The job to run
	 */
	public void runImmediately(Runnable job){
		handler.post(job);
	}
	
	/**
	 * Test if a job is running
	 * @param id: The id of the job
	 * @return True if the job with the given id are running else false
	 */
	public boolean isRunning(Integer id){
		return running.contains(id);
	}
	
	/**
	 * Test if there exist a job with the given id
	 * @param id: The id of the job
	 * @return True if the job exists and aren't dying
	 */
	public boolean jobExists(Integer id){
		return (jobs.get(id) == null || jobs.get(id).isDying());
	}
	
	
	/**
	 * A inner class to represent a job
	 * @author Niclas
	 *
	 */
	private class Job implements Runnable{

		private Integer id;
		private Runnable job;
		private long time;
		private boolean dying;
		
		/**
		 * The constructor
		 * @param id: The id that the job are assigned
		 * @param job: The Runnable that should be executed
		 * @param time: The time between the execution of the job. Given in milliseconds
		 */
		public Job(Integer id, Runnable job, long time){
			this.id = id;
			this.job = job;
			this.time = time;
			dying = false;
		}
		
		/**
		 * Kill this job
		 */
		public void kill(){
			dying = true;
		}
		
		/**
		 * Getter for the time
		 * @return How much time to wait between the jobs, as milliseconds
		 */
		public long getTime(){
			return time;
		}
		
		/**
		 * Setter for the time
		 * @param id: How much time to wait between the jobs, as milliseconds
		 */
		public void setTime(long time){
			this.time = time;
		}
		
		/**
		 * Tells if the job are dying
		 * @return True if the job are dying else false
		 */
		public boolean isDying(){
			return dying;
		}
		
		/**
		 * Executes the given runnable if this job are running 
		 */
		public void run() {
			if(running.contains(id)){
				job.run();
				handler.postDelayed(this,time);
			}else if(dying){
				jobs.remove(id);
			}
		}
	}
}
