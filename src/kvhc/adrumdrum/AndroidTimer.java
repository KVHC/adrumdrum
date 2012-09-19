package kvhc.adrumdrum;
import android.os.Handler;

/***
 * 
 * @author Niclas
 * A class representing a timer
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
	public AndroidTimer(Handler handler, Runnable job, long time){
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
	public AndroidTimer(Runnable job, long time){
		this(new Handler() , job, time);
		
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
	public void start(){
		
		if(enabled)
			return;
		
		enabled = true;
		handler.postDelayed(timer,time);
	}
	
	/**
	 * Checks if the timer are running
	 * @return True if the timer are running, else false
	 */
	public boolean running(){
		return enabled;
	}
	
	/**
	 * Change the Runnable object that are used every timer tick
	 * @param job: A Runnable object that should be used eery timer tick
	 */
	public void changeJob(Runnable job){
		this.job = job;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * A private class used too loop the the job
	 */
	private Runnable timer = new Runnable(){
		//@Override
		public void run() {
			if(enabled){
				handler.post(job);
				handler.postDelayed(this,time);
			}
		}
	};
}
