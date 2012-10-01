package kvhc.gui;

import kvhc.player.Channel;
import kvhc.util.AndroidTimer;
import android.content.Context;
import android.graphics.LightingColorFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChannelButtonGUI extends Button {

	private boolean isActive;
	private Channel channel;
	private GUIController controller;

	
	public ChannelButtonGUI(Context context, Channel channel, GUIController controller) {
		super(context);
		this.channel = channel;
		this.controller = controller;
		setOnClickListener(onClick);
		this.isActive = false;
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));

	}
	
	public void activate(){
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		channel.SetMute(false);
	}
	
	public void deactivate(){
		getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAABC00));
		channel.SetMute(true);
	}
	
	
	
	
	
	
	
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
