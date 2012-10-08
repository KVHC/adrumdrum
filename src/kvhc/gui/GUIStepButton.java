package kvhc.gui;

import kvhc.adrumdrum.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;


/**
 * GUIStepButton
 * 
 * A View for the steps 
 * 
 * @author Oscar/kvhc
 *
 */
public class GUIStepButton extends CheckBox {
	
	private int m_ChannelId;
	private int m_StepId;
	
	private boolean m_Active; 
	private boolean m_Playing;
	
	private static Bitmap m_buttonOff; 
	private static Bitmap m_buttonOn;
	private static Bitmap m_buttonOff_play; 
	private static Bitmap m_buttonOn_play;
	private static int distanceFromTop = 12;  // så att stepsen ritas i mitten av raden
	
	
	
	
	/**
	 * Constructor for the GUIStepButton
	 * @param context
	 * @param channelId
	 * @param stepId
	 */
	public GUIStepButton(Context context, int channelId, int stepId) {
		super(context);
		
		initImages();
		
		m_ChannelId = channelId;
		m_StepId = stepId;
		m_Active = false;
	}
	
	public GUIStepButton(Context context, int channelId, int stepId, boolean isActive) {
		super(context);
		
		initImages();
		
		m_ChannelId = channelId;
		m_StepId = stepId;
		m_Active = isActive;
	}
	
	
	/**
	 * This method initialize the images that represent different phases of a step
	 */
	private void initImages(){
		if(GUIStepButton.m_buttonOff == null) {
			m_buttonOff = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonoff);
		}
		
		if(GUIStepButton.m_buttonOn == null) {
			m_buttonOn = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonon);
		}
		
		if(GUIStepButton.m_buttonOff_play == null) {
			m_buttonOff_play = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonoff_active);
		}
		
		if(GUIStepButton.m_buttonOn_play == null) {
			m_buttonOn_play = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonon_active);
		}
		
	}
	
	
	/**
	 * Returns the channel number this View represents 
	 * @return
	 */
	public int GetChannel() {
		return m_ChannelId;
	}
	
	/**
	 * Returns the step number this View represents (might be a stupid idea)
	 * @return
	 */
	public int GetStep() {
		return m_StepId;
	}
	
	/**
	 * Sets if the StepButton is to be shown as active or not 
	 * @param active
	 */
	public void SetActive(boolean active) {
		m_Active = active;
	}
	
	/**
	 * Change the avtive state of the button to the opposite state
	 */
	public void reverse() {
		m_Active = !m_Active;
	}
	
	/**
	 * Setter for playing variable
	 * @param playing Boolean that represents if the steps being played
	 */
	public void setPlaying(boolean playing){
		m_Playing = playing;
	}
	
	
	
	/**
	 * A method that specifies how the step button should be drawn 
	 */
	protected void onDraw(Canvas canvas) {
		
		// Skapar en färg för att måla texten
		Paint paint = new Paint();
		paint.setTextSize(20);

		// Flyttar siffrorna åt vänster om de är större än tio så att de fortfarande är centrerade
		float textPosX = 19;
		float textPosY = 32 + distanceFromTop;
		if (m_StepId > 8 && m_StepId < 100){
			textPosX = 13;
		}	
		
		
		if (m_Playing){
			paint.setColor(Color.RED); // Om steget spelas så ska färgen på siffrorna också vara röda
			if(m_Active) {
				canvas.drawBitmap(GUIStepButton.m_buttonOn_play, 0, distanceFromTop, null);
			} else {
				canvas.drawBitmap(GUIStepButton.m_buttonOff_play, 0, distanceFromTop, null);
			}
		} else {
			if (m_Active){
				canvas.drawBitmap(GUIStepButton.m_buttonOn, 0, distanceFromTop, null);
			} else {
				canvas.drawBitmap(GUIStepButton.m_buttonOff, 0, distanceFromTop, null);
			}
		}
		canvas.drawText(String.valueOf(m_StepId +1),textPosX,textPosY, paint);
		
	}
	
}
