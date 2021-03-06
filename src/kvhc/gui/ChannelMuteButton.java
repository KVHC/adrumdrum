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
import kvhc.models.Channel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Button;

/**
 * Button to mute or unmute a Channel.
 * 
 * @author kvhc
 */
@SuppressLint("ViewConstructor")
public class ChannelMuteButton extends Button {

	private static final float VOLUME_LIMIT_FULL = 0.75f;
	private static final float VOLUME_LIMIT_MIDDLE = 0.50f;
	private static final float VOLUME_LIMIT_LOW = 0.25f;

	private Channel channel;
	private GUIController guic;
	
	/**
	 * Constructor.
	 * @param context the context
	 * @param channel the Channel to control
	 * @param guic GUIController to tell if solo mode is no more
	 */
	public ChannelMuteButton(Context context, Channel channel, GUIController guic) {
		super(context);
		this.guic = guic;
		this.channel = channel;
		setOnClickListener(onClick);
		
		drawChannels();
	}
	
	/**
	 * Button Listener.
	 * Toggle between muted and unmuted on click
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
	
	/**
	 * Draws different symbols on the button depending on the volume of the channel.
	 */
	private void drawChannels(){
		if (channel.isMuted()) {
			setBackgroundResource(R.drawable.muted);			
		} else if(channel.getChannelVolume() > VOLUME_LIMIT_FULL)  {
			setBackgroundResource(R.drawable.volume_full);
		} else if(channel.getChannelVolume() > VOLUME_LIMIT_MIDDLE)  {
			setBackgroundResource(R.drawable.volume_middle);
		} else if(channel.getChannelVolume() > VOLUME_LIMIT_LOW)  {
			setBackgroundResource(R.drawable.volume_low);
		} else {
			setBackgroundResource(R.drawable.volume_nothing);
		}
	}
	
	/**
	 * This method are called every time the gui are invalidated. 
	 * Draws sound symbols on the mute-button
	 */
	protected void onDraw(Canvas canvas) {
		drawChannels();
	}
}
