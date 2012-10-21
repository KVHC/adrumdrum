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
import kvhc.models.Step;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.CheckBox;


/**
 * GUIStepButton is a View representing a step.
 * 
 * @author Oscar/kvhc
 *
 */
@SuppressLint("ViewConstructor")
public class GUIStepButton extends CheckBox {
	
	// Default variables
	private static final int DISTANCE_FROM_TOP = 12;
	private static final int TEXT_SIZE = 20;
	
	// Identification
	private int mChannelId;
	private int mStepId;
	private Step step;
	
	// State
	private boolean mActive; 
	private boolean mPlaying;
	
	// Icons and Paint
	private static Bitmap mButtonOff; 
	private static Bitmap mButtonOn;
	private static Bitmap mButtonOffPlaying; 
	private static Bitmap mButtonOnPlaying;
	private Paint textPaint; 
	private Paint velocityShade;

	/**
	 * Constructor for the GUIStepButton.
	 * @param context the context
	 * @param channelId the id of the channel this step is part of
	 * @param step The step this button represents
	 */
	public GUIStepButton(Context context, int channelId, Step step) {
		super(context);
		
		initImages();
		initPaint();
		
		this.step = step;
		mChannelId = channelId;
		mStepId = step.getStepNumber();
		mActive = false;
	}
	
	/**
	 * Constructor who also takes an boolean to decide if the button should be shown
	 * as active or inactive.
	 * @param context the context
	 * @param channelId the id of the channel this step is part of
	 * @param step The step this button represents
	 * @param isActive whether the button should be shown as active or not
	 */
	public GUIStepButton(Context context, int channelId, Step step, boolean isActive) {
		super(context);
		
		initImages();
		initPaint();
		
		this.step = step;
		mChannelId = channelId;
		mStepId = step.getStepNumber();
		mActive = isActive;
	}
	
	/**
	 * This method initialize the images that represent different phases of a step.
	 */
	private void initImages(){
		if(GUIStepButton.mButtonOff == null) {
			mButtonOff = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonoff);
		}
		
		if(GUIStepButton.mButtonOn == null) {
			mButtonOn = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonon);
		}
		
		if(GUIStepButton.mButtonOffPlaying == null) {
			mButtonOffPlaying = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonoff_active);
		}
		
		if(GUIStepButton.mButtonOnPlaying == null) {
			mButtonOnPlaying = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonon_active);
		}	
	}

	/**
	 * Initialize paint to paint digits and shade boxes with.
	 */
	private void initPaint(){
		// Skapar en färg för att måla texten
		textPaint = new Paint();
		textPaint.setTextSize(TEXT_SIZE);
		// Initierar en färg för att shadea knappen
		velocityShade = new Paint();
	}
	
	
	/**
	 * Returns the channel number this View represents.
	 * @return the channel number this View represents
	 */
	public int getChannelId() {
		return mChannelId;
	}
	
	/**
	 * Returns the step number this View represents.
	 * @return the step number this View represents
	 */
	public int getStepId() {
		return mStepId;
	}
	
	/**
	 * Returns the Step this button represents.
	 * @return the step this button represents.
	 */
	public Step getStep() {
		return step;
	}
	
	/**
	 * Sets if the StepButton is to be shown as active or not.
	 * @param active
	 */
	public void setActive(boolean active) {
		mActive = active;
	}
	
	/**
	 * Change the active state of the button to the opposite state.
	 */
	public void reverse() {
		mActive = !mActive;
	}
	
	/**
	 * Setter for playing variable.
	 * @param playing Boolean that represents if the steps being played
	 */
	public void setPlaying(boolean playing){
		mPlaying = playing;
	}
	
	/**
	 * A method that specifies how the step button should be drawn.
	 * @param canvas. A canvas to draw the button on
	 */
	protected void onDraw(Canvas canvas) {
		
		// Flyttar siffrorna åt vänster om de är större än tio så att de fortfarande är centrerade
		float textPosX = 19;
		float textPosY = 32 + DISTANCE_FROM_TOP;
		if (mStepId > 8 && mStepId < 100){
			textPosX = 13;
		}	
		
		float velocity = step.getVelocity();
		if(velocity < 0.1){
			velocity = 0.1F;
		}
		
		// Sätter alpha till ett värde mellan 0-255, beroende på velocity.
		velocityShade.setAlpha((int) (velocity*255));
		
		if (mPlaying){
			textPaint.setColor(Color.RED); // Om steget spelas så ska färgen på siffrorna också vara röda
			if(mActive) {
				canvas.drawBitmap(GUIStepButton.mButtonOnPlaying, 0, DISTANCE_FROM_TOP, velocityShade);
			} else {
				canvas.drawBitmap(GUIStepButton.mButtonOffPlaying, 0, DISTANCE_FROM_TOP, velocityShade);
			}
		} else {
			textPaint.setColor(Color.BLACK);
			if (mActive){
				canvas.drawBitmap(GUIStepButton.mButtonOn, 0, DISTANCE_FROM_TOP, velocityShade);
			} else {
				canvas.drawBitmap(GUIStepButton.mButtonOff, 0, DISTANCE_FROM_TOP, velocityShade);
			}
		}
		canvas.drawText(String.valueOf(mStepId +1),textPosX,textPosY, textPaint);
	}
	
}
