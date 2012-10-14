package kvhc.player;


/**
 * Model of a sound (used by Channel)
 * @author srejv
 *
 */
public class Sound {

	
	private int mId; // Id in the other system
	private int mSoundValue; // The location of the soundfile in an integer (from raw atm, probably should be object)
	private String mName;
	
	/**
	 * The constructor of a Sound
	 * @param id The id of this sound
	 * @param soundValue The value of the sound in the R class
	 * @param name
	 */
	public Sound(int soundValue, String name) {
		mId = -1;
		mSoundValue = soundValue;
		mName = name;
	}

	/**
	 * Method for geting the id of this sound
	 * @return the id of this sound
	 */
	public int GetId() {
		return mId;
	}

	/**
	 * Change the id of this sound
	 * @param id The new id of the sound
	 */
	public void SetId(int id) {
		this.mId = id;
	}

	/**
	 * Method for getting the name of this sound
	 * @return The name of this sound
	 */
	public String GetName() {
		return mName;
	}

	/**
	 * Change the name of this sound
	 * @param name The new name of the channel
	 */
	public void SetName(String name) {
		this.mName = name;
	}

	/**
	 * Method for getting the id that this sound have in the R class
	 * @return the id that this sound have in the R class
	 */
	public int GetSoundValue() {
		return mSoundValue;
	}
	
	/**
	 * Method for setting the id that this sound have in the R class
	 * @param soundValue the new value of the sound id in the R class
	 */
	public void SetSoundValue(int soundValue) {
		mSoundValue = soundValue;
	}
	
	
}
