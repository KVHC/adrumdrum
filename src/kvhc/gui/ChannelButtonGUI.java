package kvhc.gui;

import kvhc.player.Channel;
import kvhc.player.ChannelDialog;
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
 */
public class ChannelButtonGUI extends Button {

	private boolean isActive;
	private Channel channel;
	private Activity mainActivity;

	/**
	 * Constructor
	 * @param context the main activity
	 * @param channel the Channel to control
	 */
	public ChannelButtonGUI(Context context, Channel channel) {
		super(context);
		this.channel = channel;
		mainActivity = (Activity) context;
		setOnClickListener(onClick);
		setOnLongClickListener(onLongClick);
		this.isActive = false;
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00FF00));

	}
	
	/**
	 * Activates the Channel and sets the button color to green.
	 */
	public void activate(){
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00FF00));
		channel.SetMute(false);
	}
	
	/**
	 * Deactivates the Channel and sets the button color to red.
	 */
	public void deactivate(){
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		channel.SetMute(true);
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
			else
				deactivate();
			isActive = !isActive;
		}
	};
	
	/**
	 * Button Listener for long click
	 */
	private OnLongClickListener onLongClick = new OnLongClickListener() {
		public boolean onLongClick(View v) {
			
			ChannelDialog cd = new ChannelDialog(mainActivity, channel);
			cd.show();
			
			//Intent intent = new Intent(mainActivity, GUIChannelSettings.class);
			
			//Försök att skicka med Channel. Vet inte om det är så här man gör:
            //intent.putExtra("Channel", channel);  
	    	//mainActivity.startActivity(intent);
			return true;
		}
	};
	


}
