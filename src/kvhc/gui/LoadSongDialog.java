package kvhc.gui;

import kvhc.adrumdrum.R;
import kvhc.player.Song;
import kvhc.util.ISongLoader;
import kvhc.util.SQLSongLoader;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
		ListView songList = (ListView)findViewById(R.id.savedSongsList);
		
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				  "Linux", "OS/2" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				  R.layout.song_list_item, R.id.savedSongsList, values);

		songList.setAdapter(adapter);
		
		songList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getContext(),
					      "Click ListItem Number " + position, Toast.LENGTH_LONG)
					      .show();
				
			}
		});
		
		// Set up load button
		Button b = (Button)findViewById(R.id.btnLoadSong);
		b.setOnClickListener(btnLoadListener);
	}
	
	public Song getSong() {
		return mSong;
	}
	
	
	

}
