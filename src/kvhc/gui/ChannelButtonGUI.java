package kvhc.gui;

import kvhc.player.Channel;
import kvhc.util.AndroidTimer;
import android.content.Context;
import android.graphics.LightingColorFilter;
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

	/**
	 * Constructor
	 * @param context the main activity
	 * @param channel the Channel to control
	 */
	public ChannelButtonGUI(Context context, Channel channel) {
		super(context);
		this.channel = channel;
		setOnClickListener(onClick);
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


}
