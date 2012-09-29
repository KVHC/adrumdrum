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
	
	public Sound(int id, int soundValue, String name) {
		mId = id;
		mSoundValue = soundValue;
		mName = name;
	}

	public int GetId() {
		return mId;
	}

	public void SetId(int id) {
		this.mId = id;
	}

	public String GetName() {
		return mName;
	}

	public void SetName(String name) {
		this.mName = name;
	}

	public int GetSoundValue() {
		return mSoundValue;
	}
	
	public void SetSoundValue(int soundValue) {
		mSoundValue = soundValue;
	}
	
	
}
