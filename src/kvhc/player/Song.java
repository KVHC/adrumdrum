package kvhc.player;

import java.util.ArrayList;
import java.util.List;

public class Song {
	
	private long id;
	private List<Channel> mChannels;
	private int numsteps;
	private String mName;
	
	/**
	 * Creates a song with a set number of channels
	 * @param numChannels
	 */
	public Song(int numChannels) {
		
		numsteps = 16;
		mChannels = new ArrayList<Channel>(numChannels);
		
		for (int i = 0; i < numChannels; i++) {
			mChannels.add(new Channel(null, numsteps));
		}
	}
	
	/**
	 * Creates a song based on an array of channels
	 * @param channels
	 */
	public Song(ArrayList<Channel> channels) {
		mChannels = channels;
		numsteps = mChannels.get(0).getNumberOfSteps();
	}
	
	/**
	 * Add an empty channel to the song
	 */
	public void addChannel() {
		Channel c = new Channel(null, numsteps);
		mChannels.add(c);
	}
	
	/**
	 * Add a created channel to the song
	 * @param c. The channel that should be added
	 */
	public void addChannel(Channel c) {
		mChannels.add(c);
	}
	
	
	/**
	 * Removed channel based on the channel object. 
	 * 
	 * TODO: Implement equals on channel?
	 * @param c
	 * @throws Exception
	 */
	public void removeChannel(Channel c) throws Exception {
		for (int i = 0; i < mChannels.size(); i++) {
			if (mChannels.get(i).equals(c)) {
				mChannels.remove(i);
				// Vet inte om detta ändrar på ordningen i arraylist? Vill ha länkad så det inte gör något. 
				// Kanske om man fixar med en iterator? Detta är bara ett exempel
			}
		}
		throw new Exception("Not implemented yet.");
	}
	

	/**
	 * Append a number of steps to all channels
	 * @param numberOfStepsToAdd
	 */
	public void addSteps(int numberOfStepsToAdd) {
		for(Channel c : mChannels) {
			c.resizeBy(numberOfStepsToAdd);
		}
	}
	
	/**
	 * Remove a number of steps from all channels
	 * @param numberOfStepsToRemove
	 */
	public void removeSteps(int numberOfStepsToRemove) {
		if (!mChannels.isEmpty() && numberOfStepsToRemove <= mChannels.get(0).getNumberOfSteps()) {
			for (int i = 0; i < mChannels.size(); i++) {
				mChannels.get(i).resizeBy(-numberOfStepsToRemove);
			}
		}
	}
	
	public void clearAllSteps() {
		for (Channel channel : mChannels) {
			channel.clearAllSteps();
		}
	}
	
	/**
	 * Returns a specified channel
	 * @param channelNumber
	 * @return
	 */
	public Channel getChannel(int channelNumber) {
		return mChannels.get(channelNumber);
	}
	
	/**
	 * Returns all the channels that the song contains
	 * @return
	 */
	public List<Channel> getChannels() {
		return mChannels;
	}

	/**
	 * Returns the number of channels in the song
	 * @return
	 */
	public int getNumberOfChannels() {
		return mChannels.size();
	}

	/**
	 * Returns the number of steps in this song
 	 * @return
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
	public ArrayList<Sound> GetSounds() {
		ArrayList<Sound> sounds = new ArrayList<Sound>(mChannels.size());
		
		for (Channel c : mChannels) {
			sounds.add(c.getSound());
		}
		
		return sounds;
	}
		
	/**
	 * Removes a channel on the given number
	 * @param channel The number of the channel to remove
	 */
	public boolean removeChannel(int channel) {
		if (channel >= mChannels.size() ||  mChannels.size() <= 1 ){
			return false;
		} else {
			mChannels.remove(channel);
		}
		return true;
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
	 * ID getter (TODO what is ID?)
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * ID setter (TODO what is ID?)
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * SETS ALL THE CHANNELS HURRS
	 * @param channels
	 */
	public void setChannels(List<Channel> channels) {
		mChannels = channels;
		if(channels.size() > 0)  {
			numsteps = channels.get(0).getNumberOfSteps();
		}
	}
}