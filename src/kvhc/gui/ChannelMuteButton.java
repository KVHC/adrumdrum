package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Button to mute or unmute a Channel.
 * 
 * The .png files that are used by this class are under the creative common licens and 
 * were created by Interactivemania - http://www.interactivemania.com and
 * Downloaded from http://www.iconfinder.com/
 *
 * @author kvhc
 */
public class ChannelMuteButton extends Button {

	private boolean isMuted;
	private Channel channel;

	/**
	 * Constructor.
	 * @param context the context
	 * @param channel the Channel to control
	 */
	public ChannelMuteButton(Context context, Channel channel) {
		super(context);
		this.channel = channel;
		this.isMuted = channel.isMuted();
		setOnClickListener(onClick);
		setGravity(20);
		
		if (isMuted) {
			setBackgroundResource(R.drawable.muted);
		} else {
			setBackgroundResource(R.drawable.unmuted);
		}
	}
	
	/**
	 * Button Listener.
	 */
	private OnClickListener onClick = new OnClickListener() {
		
		public void onClick(View v) {
			if (isMuted) {
				channel.setMute(false);
				setBackgroundResource(R.drawable.unmuted);
			} else {
				channel.setMute(true);
				setBackgroundResource(R.drawable.muted);
			}
			isMuted = !isMuted;
		}
	};
}
