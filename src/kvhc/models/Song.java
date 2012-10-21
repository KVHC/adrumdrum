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

package kvhc.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Song class manages a collection of Channels. 
 * @author kvhc
 *
 */
public class Song {
	
	// Default variables
	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final int DEFAULT_NUMBER_OF_CHANNELS = 4;
	
	private long id;
	private List<Channel> mChannels;
	private int mNumsteps;
	private String mName;
	private int mBpm;
	
	/**
	 * Creates a song with a set number of channels
	 * @param numChannels
	 */
	public Song(int numChannels) {
		
		mNumsteps = DEFAULT_NUMBER_OF_STEPS;
		mChannels = new ArrayList<Channel>(numChannels);
		mBpm = 120;
		
		for (int i = 0; i < numChannels; i++) {
			mChannels.add(new Channel(null, mNumsteps));
		}
	}
	
	/**
	 * Creates a song based on an array of channels.
	 * @param channels
	 */
	public Song(List<Channel> channels) {
		if (channels == null) {		
			mNumsteps = DEFAULT_NUMBER_OF_STEPS;
			mChannels = new ArrayList<Channel>(DEFAULT_NUMBER_OF_CHANNELS);	
		} else {
			mChannels = channels;
			if(channels.size() > 0) {
				mNumsteps = mChannels.get(0).getNumberOfSteps();
			} else {
				mNumsteps = DEFAULT_NUMBER_OF_STEPS;
			}
		}
		mBpm = 120;
	}
	
	/**
	 * Add an empty channel to the song.
	 */
	public void addChannel() {
		Channel c = new Channel(null, mNumsteps);
		mChannels.add(c);
	}
	
	/**
	 * Add a created channel to the song.
	 * @param c The channel that should be added
	 */
	public void addChannel(Channel c) {
		mChannels.add(c);
	}
	
	/**
	 * Removes a channel on the given index.
	 * @param channel the index of the channel to remove
	 * @return true if a channel was successfully removed
	 */
	public boolean removeChannel(int channelIndex) {
		if (channelIndex >= mChannels.size() ||  mChannels.size() <= 1 ) {
			return false;
		} else {
			mChannels.remove(channelIndex);
		}
		return true;
	}

	/**
	 * Append a number of steps to all channels.
	 * @param numberOfStepsToAdd the number of steps to add to each channel
	 */
	public void addSteps(int numberOfStepsToAdd) {
		for(Channel c : mChannels) {
			c.resizeBy(numberOfStepsToAdd);
		}
	}
	
	/**
	 * Remove a number of steps from all channels.
	 * @param numberOfStepsToRemove
	 */
	public void removeSteps(int numberOfStepsToRemove) {
		if (!mChannels.isEmpty() && numberOfStepsToRemove <= mChannels.get(0).getNumberOfSteps() - 1) {
			for (int i = 0; i < mChannels.size(); i++) {
				mChannels.get(i).resizeBy(-numberOfStepsToRemove);
			}
		}
	}
	
	/**
	 * Resets all steps in all Channels.
	 */
	public void clearAllSteps() {
		for (Channel channel : mChannels) {
			channel.clearAllSteps();
		}
	}
	
	/**
	 * Returns a specified channel by index.
	 * @param channelIndex index of the channel to return
	 * @return the channel asked for
	 */
	public Channel getChannel(int channelIndex) {
		return mChannels.get(channelIndex);
	}
	
	/**
	 * Returns all the channels that the song contains.
	 * @return the List of Channels the Song contains
	 */
	public List<Channel> getChannels() {
		return mChannels;
	}

	/**
	 * Returns the number of channels in the song.
	 * @returnthe number of channels in the song
	 */
	public int getNumberOfChannels() {
		return mChannels.size();
	}

	/**
	 * Returns the number of steps in this song.
 	 * @return the number of steps in this song
	 */
	public int getNumberOfSteps() {
		if (mChannels.size() <= 0) {
			return 0;
		}
		return mChannels.get(0).getNumberOfSteps();
	}
	
	/**
	 * Returns all the sounds loaded for all the channels
	 * @return
	 */
	public List<Sound> getSounds() {
		List<Sound> sounds = new ArrayList<Sound>(mChannels.size());
		
		for (Channel c : mChannels) {
			sounds.add(c.getSound());
		}
		
		return sounds;
	}
	
	/**
	 * Gives the song a name
	 * @param name
	 */
	public void setName(String name) {
		mName = name;
	}
		
	/**
	 * Returns the name of the song 
	 * @return
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Mutes all channels except the specified one
	 * @param channel the channel to go SOLO 
	 */
	public void muteAllChannelsExcept(int channel) {
		if (channel>=mChannels.size()) {
			return;
		}
		for (int i=0;i<mChannels.size();i++) {
			mChannels.get(i).setMute(true);
		}
		mChannels.get(channel).setMute(false);
	}
	
	/**
	 * Unmutes all Channels in the Song.
	 */
	public void playAll(){
		for (int i=0;i<mChannels.size();i++) {
			mChannels.get(i).setMute(false);
		}
	}

	/**
	 * Returns the ID of the Song.
	 * @return ID of the Song
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the ID of the Song.
	 * @param id ID to be set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets a given List of Channels to used by the Song.
	 * @param channels list of channels to be used
	 */
	public void setChannels(List<Channel> channels) {
		mChannels = channels;
		if (channels.size() > 0) {
			mNumsteps = channels.get(0).getNumberOfSteps();
		}
	}

	/**
	 * Gets the set BPM of the song.
	 * @return
	 */
	public int getBpm() {
		return mBpm;
	}

	/**
	 * Sets the BPM of the song.
	 * @param mBpm
	 */
	public void setBpm(int mBpm) {
		this.mBpm = mBpm;
	}
	
	/**
	 * Compare a song for equality
	 * @param Song to compare to
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Song)
		{
			Song song = (Song)o;
			return mName.equals(song.getName());
		}
		return false;
	}
	/**
	 * Returns the hashcode of the Song
	 */
	@Override
	public int hashCode() {
		return((int)id);
	}
}
