package kvhc.gui.dialogs;

import kvhc.adrumdrum.R;
import kvhc.models.Song;
import kvhc.util.ISongRenderer;
import kvhc.util.db.SQLRenderer;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A Dialog for saving songs.
 * 
 * Uses the ISongRenderer interface.
 * 
 * @author kvhc
 *
 */
public class SaveSongDialog extends Dialog {

	private Song mSong;
	private ISongRenderer mSongRenderer;
	private EditText mEditSongName;
	
	public SaveSongDialog(Context context, Song song) {
		super(context);
		
		mSong = song;
		
	}
	
	/**
	 * A standard onCreate method for setting up the View
	 */
	public void onCreate(Bundle savedInstanceState){
		// Set up the dialog
		super.onCreate(savedInstanceState);
        setContentView(R.layout.save_song);
        setTitle("Save Song");
        
        if(mSongRenderer == null) {
        	mSongRenderer = new SQLRenderer(getContext());
        }
        
        // Set up the EditText widget
        mEditSongName = (EditText)findViewById(R.id.editSongName);
        mEditSongName.setText(mSong.getName());
        
        // Set up the save button 
        Button saveBtn = (Button)findViewById(R.id.buttonSaveSong);
        saveBtn.setOnClickListener(saveButtonListener);
	}
	
	private View.OnClickListener saveButtonListener = new View.OnClickListener() {
		public void onClick(View v) {
			// Set the name
			mSong.setName(mEditSongName.getText().toString());
			
			// Render the song through the interface
			mSongRenderer.RenderSong(mSong);
			
			dismiss();
		}
	};
}
