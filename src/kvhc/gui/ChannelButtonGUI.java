package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import kvhc.util.AndroidTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Class to manage a ChannelButton.
 * When clicked it sets its Channel to active or inactive and sets 
 * the button color to red (inactive) or green (active).
 * 
 * The .png files that are used by this class are under the creative common licens and 
 * were created by Interactivemania - http://www.interactivemania.com and
 * Downloaded from http://www.iconfinder.com/
 * 
 * 
 */
public class ChannelButtonGUI extends Button {

	private boolean isActive;
	private Channel channel;
	private Activity mainActivity;
	private int id;
	private GUIController controll;

	/**
	 * Constructor
	 * @param context the main activity
	 * @param channel the Channel to control
	 * @param id The id of the channel
	 * @param A GUI controller to send to the options dialog
	 */
	public ChannelButtonGUI(Context context, Channel channel,int id, GUIController controll) {
		super(context);
		this.channel = channel;
		this.id = id;
		this.controll = controll;
		mainActivity = (Activity) context;
		setOnClickListener(onClick);
		setOnLongClickListener(onLongClick);
		this.isActive = channel.isMuted();
		setGravity(20);
		
		if (isActive) {
			setBackgroundResource(R.drawable.muted);
		}
		else {
			setBackgroundResource(R.drawable.unmuted);
		}
	}
	
	/**
	 * Activates the Channel and sets the button color to green.
	 */
	public void activate(){
		channel.setMute(false);
		setBackgroundResource(R.drawable.unmuted);
		
	}
	
	/**
	 * Deactivates the Channel and sets the button color to red.
	 */
	public void deactivate(){
		channel.setMute(true);
		setBackgroundResource(R.drawable.muted);
	}
	
	/**
	 * Returns the Channel associated with this ChannelButtonGUI
	 */
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * Button Listener
	 */
	private OnClickListener onClick = new OnClickListener() {
		
		public void onClick(View v) {
			if (isActive)
				activate();
			else {
				deactivate();
			}
			isActive = !isActive;
		}
	};
	
	/**
	 * Button Listener for long click
	 */
	private OnLongClickListener onLongClick = new OnLongClickListener() {
		public boolean onLongClick(View v) {		
			ChannelDialog cd = new ChannelDialog(mainActivity, channel,id,controll);
			cd.show();
			return true;
		}
	};
	


}
