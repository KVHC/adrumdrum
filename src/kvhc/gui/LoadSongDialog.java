package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Song;
import kvhc.util.ISongLoader;
import kvhc.util.SQLSongLoader;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The Load Song dialog shows a list of saved songs (currently from the db)
 * and lets the user choose one which then is returned to the callee. 
 * 
 * @author kvhc
 *
 */
public class LoadSongDialog extends Dialog {

	
	private ISongLoader mSongLoader;
	private Song mSong;
	
	private View.OnClickListener btnLoadListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			mSong = mSongLoader.loadSong("");
			
			dismiss();
		}
	}; 
	
	public LoadSongDialog(Context context) {
		super(context);
		
		mSongLoader = new SQLSongLoader(context);
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// Set up the view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_song);
		setTitle("Load Song");
		
		// Set up the list
		
		
		// Set up load button
		Button b = (Button)findViewById(R.id.btnLoadSong);
		b.setOnClickListener(btnLoadListener);
	}
	
	public Song getSong() {
		return mSong;
	}
	
	
	

}
