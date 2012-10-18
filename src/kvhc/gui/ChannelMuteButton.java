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

package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.models.Channel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
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
@SuppressLint("ViewConstructor")
public class ChannelMuteButton extends Button {

	private Channel channel;
	private GUIController guic;
	
	/**
	 * Constructor.
	 * @param context the context
	 * @param channel the Channel to control
	 */
	public ChannelMuteButton(Context context, Channel channel,GUIController guic) {
		super(context);
		this.guic = guic;
		this.channel = channel;
		setOnClickListener(onClick);
		setGravity(20);
		
		if (channel.isMuted()) {
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
			if (channel.isMuted()) {
				channel.setMute(false);
			} else {
				channel.setMute(true);
			}
			guic.stopSolo();
			invalidate();
		}
	};
	
	
	protected void onDraw(Canvas canvas) {
		if (channel.isMuted()) {
			setBackgroundResource(R.drawable.muted);			
		} else if(channel.getChannelVolume() > 0.75)  {
			setBackgroundResource(R.drawable.unmuted);
		} else if(channel.getChannelVolume() > 0.50)  {
			setBackgroundResource(R.drawable.unmuted);
		} else if(channel.getChannelVolume() > 0.25)  {
			setBackgroundResource(R.drawable.muted);
		} else {
			setBackgroundResource(R.drawable.muted);
		}
	}
}
