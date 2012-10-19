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

package kvhc.gui.dialogs;

import java.util.List;

import kvhc.adrumdrum.R;
import kvhc.models.Song;
import kvhc.util.ISongLoader;
import kvhc.util.db.SQLSongLoader;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The Load Song dialog shows a list of saved songs (currently from the db)
 * and lets the user choose one which then is returned to the callee. 
 * 
 * @author kvhc
 *
 */
public class LoadSongDialog extends Dialog {

	// Song stuff 
	private ISongLoader mSongLoader = null;
	private Song mSong = null;
	private List<Song> mSongList = null;
	private String[] songNames = null;
	
	/**
	 * Constructor
	 * @param context the context
	 */
	public LoadSongDialog(Context context) {
		super(context);
	}
	
	/**
	 * onCreate
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// Set up the view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_song);
		setTitle("Load Song");
		
		if(mSongLoader == null) {
			mSongLoader = new SQLSongLoader(getContext());
		}
		
		// Set up the list
		ListView mList = (ListView)findViewById(R.id.savedSongsList);
		songNames = new String[getSongs().size()];
		int i = 0;
		for(Song song : getSongs()) {
			songNames[i++] = song.getName();
		}
		
		if(i > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), 
					R.layout.song_list_item, R.id.textViewSongName, songNames);
			
			mList.setAdapter(adapter);
		
			mList.setOnItemClickListener(new OnItemClickListener() {
	
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					
					String name = songNames[position];
					String[] args = new String[] { name };
					mSong = mSongLoader.loadSong(args);
					dismiss();
					
				}
			});
		}
	}
	
	/**
	 * Creates a list of the song names.
	 * @return an array of song names list
	 */
	private List<Song> getSongs() {
		if(mSongList == null) {
			mSongList = mSongLoader.getSongList(null);
		}
		return mSongList;
	}
	
	/**
	 * Returns the Song created.
	 * @return the Song created
	 */
	public Song getSong() {
		return mSong;
	}	
}
