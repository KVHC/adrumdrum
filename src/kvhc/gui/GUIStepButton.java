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
	
	private static Bitmap m_buttonOff; 
	private static Bitmap m_buttonOn;
	
	/**
	 * Constructor for the GUIStepButton
	 * @param context
	 * @param channelId
	 * @param stepId
	 */
	public GUIStepButton(Context context, int channelId, int stepId) {
		super(context);
		
		if(GUIStepButton.m_buttonOff == null) {
			GUIStepButton.m_buttonOff = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonoff);
		}
		
		if(GUIStepButton.m_buttonOn == null) {
			m_buttonOn = BitmapFactory.decodeResource(getResources(), R.drawable.stepbuttonon);
		}
		
		m_ChannelId = channelId;
		m_StepId = stepId;
		m_Active = false;
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
	
	@Override 
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		
		if(m_Active) {
			canvas.drawBitmap(GUIStepButton.m_buttonOn, 0, 0, null);
		} else {
			canvas.drawBitmap(GUIStepButton.m_buttonOff, 0, 0, null);
		}
	}
	
}
