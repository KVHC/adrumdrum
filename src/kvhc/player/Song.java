package kvhc.player;

import java.util.ArrayList;


public class Song {
	
	private ArrayList<Channel> m_Channels;
	
	private int bpm; // Unsure if this should be here
	private int numsteps;
	
	private String mName;
	
	/**
	 * Creates a song with a set number of channels
	 * @param numChannels
	 */
	public Song(int numChannels) {
		
		bpm = 120;
		numsteps = 16;
		
		m_Channels = new ArrayList<Channel>(numChannels);
		
		for(int i = 0; i < numChannels; i++) {
			m_Channels.add(new Channel(null, numsteps));
		}
	}
	
	/**
	 * Creates a song based on an array of channels
	 * @param channels
	 */
	public Song(ArrayList<Channel> channels) {
		m_Channels = channels;
		numsteps = m_Channels.get(0).GetNumberOfSteps();
		bpm = 120;
	}
	
	/**
	 * Add an empty channel to the song
	 */
	public void AddChannel() {
		Channel c = new Channel(null, numsteps);
		m_Channels.add(c);
	}
	
	/**
	 * Add a created channel to the song
	 * @param c
	 */
	public void AddChannel(Channel c) {
		m_Channels.add(c);
	}
	
	/**
	 * Remove a channel at the specified index
	 * @param channelIndex
	 * @throws Exception
	 */
	public void RemoveChannel(int channelIndex) throws Exception {
		if( 0 > channelIndex || m_Channels.size() >= channelIndex)
			return;
		
		m_Channels.remove(channelIndex);
	}
	
	/**
	 * Removed channel based on the channel object. 
	 * 
	 * TODO: Implement equals on channel?
	 * @param c
	 * @throws Exception
	 */
	public void RemoveChannel(Channel c) throws Exception {
		for(int i = 0; i < m_Channels.size(); i++) {
			if(m_Channels.get(i).equals(c)) {
				m_Channels.remove(i);
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
	public void AddSteps(int numberOfStepsToAdd) {
		
		for(Channel c : m_Channels) {
			c.ResizeBy(numberOfStepsToAdd);
		}
		
	}
	
	/**
	 * Remove a number of steps from all channels
	 * @param numberOfStepsToRemove
	 */
	public void RemoveSteps(int numberOfStepsToRemove) {
		for(int i = 0; i < m_Channels.size(); i++) {
			m_Channels.get(i).ResizeBy(-numberOfStepsToRemove);
		}
	}
	
	/**
	 * Returns a specified channel
	 * @param channelNumber
	 * @return
	 */
	public Channel GetChannel(int channelNumber) {
		return m_Channels.get(channelNumber);
	}
	
	/**
	 * Returns all the channels that the song contains
	 * @return
	 */
	public ArrayList<Channel> GetChannels() {
		return m_Channels;
	}

	/**
	 * Returns the number of channels in the song
	 * @return
	 */
	public int GetNumberOfChannels() {
		return m_Channels.size();
	}

	/**
	 * Returns the number of steps in this song
 	 * @return
	 */
	public int GetNumberOfSteps() {
		
		if(m_Channels.size() <= 0) {
			return 0;
		}
		
		return m_Channels.get(0).GetNumberOfSteps();
	}
	
	/**
	 * Returns all the sounds loaded for all the channels
	 * @return
	 */
	public ArrayList<Sound> GetSounds() {
		ArrayList<Sound> sounds = new ArrayList<Sound>(m_Channels.size());
		
		for(Channel c : m_Channels) {
			sounds.add(c.GetSound());
		}
		
		return sounds;
	}
	
	
	/**
	 * Gives the song a name
	 * @param name
	 */
	public void SetName(String name) {
		mName = name;
	}
	
	/**
	 * Returns the name of the song 
	 * @return
	 */
	public String GetName() {
		return mName;
	}
	
	/**
	 * Mutes all channels except the specified one
	 * @param channel the channel to go SOLO 
	 */
	public void muteAllChannelsExcept(int channel) {
		if (channel>=m_Channels.size()) {
			return;
		}
		for(int i=0;i<m_Channels.size();i++) {
			m_Channels.get(i).SetMute(true);
		}
		m_Channels.get(channel).SetMute(false);
	}
}
