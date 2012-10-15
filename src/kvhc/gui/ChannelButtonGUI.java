package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Channel;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Class to manage a ChannelButton.
 * When clicked it opens a dialog with settings for the Channel.
 * 
 * @author kvhc
 */
public class ChannelButtonGUI extends Button {

	private Channel channel;
	private Activity mainActivity;
	private int id;
	private GUIController guic;

	/**
	 * Constructor
	 * @param context the main activity
	 * @param channel the Channel to control
	 * @param id The id of the channel
	 * @param A GUI controller to send to the Channel Dialog
	 */
	public ChannelButtonGUI(Context context, Channel channel, int id, GUIController guic) {
		super(context);
		mainActivity = (Activity) context;
		this.channel = channel;
		this.id = id;
		this.guic = guic;
		setOnClickListener(onClick);
		setGravity(20);
		setBackgroundResource(R.drawable.wrench);
	}
	
	/**
	 * Button Listener
	 */
	private OnClickListener onClick = new OnClickListener() {
		public void onClick(View v) {
			ChannelDialog cd = new ChannelDialog(mainActivity, channel, id, guic);
			cd.show();
		}
	};
	
}
