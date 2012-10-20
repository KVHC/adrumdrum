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

package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.gui.dialogs.ChannelDialog;
import kvhc.models.Channel;
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

	private static final int MARGIN = 20;
	private Channel channel;
	private Activity mainActivity;
	private int id;
	private GUIController guic;

	/**
	 * Constructor
	 * @param context the main activity
	 * @param channel the Channel to control
	 * @param id The id of the channel
	 * @param guic A GUIController to send to the ChannelDialog
	 */
	public ChannelButtonGUI(Context context, Channel channel, int id, GUIController guic) {
		super(context);
		mainActivity = (Activity) context;
		this.channel = channel;
		this.id = id;
		this.guic = guic;
		setOnClickListener(onClick);
		setGravity(MARGIN);
		setBackgroundResource(R.drawable.wrench);
	}
	
	/**
	 * Updates the Channel name, usually the name of the Sound in the Channel.
	 */
	public void updateName(){
		setText(channel.getSound().getName());
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
