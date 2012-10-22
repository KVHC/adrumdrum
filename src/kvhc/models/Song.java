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

	// Constant variables
	private static final int DEFAULT_NUMBER_OF_STEPS = 16;
	private static final int DEFAULT_NUMBER_OF_CHANNELS = 4;
	private static final int DEFAULT_BPM = 120;
	
	// Class variables
	private long id;
	private List<Channel> mChannels;
	private int mNumsteps;
	private String mName;
	private int mBpm;
	
	/**
	 * Creates a song with a set number of channels.
	 * @param numChannels number of Channels in the Song
	 */
	public Song(int numChannels) {
		
		mNumsteps = DEFAULT_NUMBER_OF_STEPS;
		mChannels = new ArrayList<Channel>(numChannels);
		mBpm = DEFAULT_BPM;
		
		// For all channels
		for (int i = 0; i < numChannels; i++) {
			mChannels.add(new Channel(null, mNumsteps));
		}
	}
	
	/**
	 * Creates a song based on an array of channels.
	 * @param channels the Channels the Song should consist of
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
		mBpm = DEFAULT_BPM;
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
	 * @param channelIndex the index of the channel to remove
	 * @return true if a channel was successfully removed
	 */
	public boolean removeChannel(int channelIndex) {
		if (channelIndex >= mChannels.size() ||  mChannels.size() <= 1) {
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
	 * @param numberOfStepsToRemove the number of steps to remove
	 */
	public void removeSteps(int numberOfStepsToRemove) {
		// If the channel will have more than one step left after removal
		if (!mChannels.isEmpty() && numberOfStepsToRemove <= mChannels.get(0).getNumberOfSteps()-1) {
			for (int i = 0; i < mChannels.size(); i++) {
				mChannels.get(i).resizeBy(-numberOfStepsToRemove);
			}
		}
	}
	
	/**
	 * Resets all steps in all Channels.
	 */
	public void clearAllSteps() {
		// For all channels in song
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
	 * Returns all the sounds loaded for all the channels.
	 * @return a list of the Sounds from the Channels
	 */
	public List<Sound> getSounds() {
		List<Sound> sounds = new ArrayList<Sound>(mChannels.size());
		
		for (Channel c : mChannels) {
			sounds.add(c.getSound());
		}
		
		return sounds;
	}
	
	/**
	 * Gives the song a name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		mName = name;
	}
		
	/**
	 * Returns the name of the song.
	 * @return the name of the song
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Mutes all channels except the specified one.
	 * @param channel the index of the channel to go SOLO 
	 */
	public void muteAllChannelsExcept(int channel) {
		// If the channel index doesn't represent a channel, do nothing
		if (channel>=mChannels.size()) {
			return;
		}
		// Mute ALL channels
		for (int i=0;i<mChannels.size();i++) {
			mChannels.get(i).setMute(true);
		}
		// Unmute the specified channel
		mChannels.get(channel).setMute(false);
	}
	
	/**
	 * Unmutes all Channels in the Song.
	 */
	public void playAll(){
		// For all channels
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
	 * @return the set BPM of the song
	 */
	public int getBpm() {
		return mBpm;
	}

	/**
	 * Sets the BPM of the song.
	 * @param mBpm the bpm to set
	 */
	public void setBpm(int mBpm) {
		this.mBpm = mBpm;
	}
	
	/**
	 * Compare a song for equality.
	 * @param o the Object to compare to
	 * @return boolean whether it is an equal Song
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Song) {
			Song song = (Song)o;
			return (mName.equals(song.getName()) && (this.id == song.id));
		}
		return false;
	}
	
	/**
	 * Returns the hashcode of the Song.
	 * This is just the ID.
	 * @return the hashcode of the Song
	 */
	@Override
	public int hashCode() {
		return((int)id + Integer.parseInt(mName));
	}
}
