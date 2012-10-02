package kvhc.gui;

import kvhc.adrumdrum.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
	
	
	/*
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
	
	
	
	@Override 
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		
		if(m_Active) {
			if (m_Playing){
				canvas.drawBitmap(GUIStepButton.m_buttonOn_play, 0, 0, null);
			} else {
				canvas.drawBitmap(GUIStepButton.m_buttonOn, 0, 0, null);
			}
		} else {
			if (m_Playing){
				canvas.drawBitmap(GUIStepButton.m_buttonOff_play, 0, 0, null);
			} else {
				canvas.drawBitmap(GUIStepButton.m_buttonOff, 0, 0, null);
			}
		}
	}
	
}
