package kvhc.adrumdrum.test;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import kvhc.models.Sound;
import kvhc.util.db.SoundDataSource;
import android.database.SQLException;
import android.test.AndroidTestCase;

public class SoundDataSourceTest extends AndroidTestCase {
	
	private static final String TEST_NAME_STRING = "TestSound";
	private static final int BIG_NUMBER = 100;
	
	public void setUp() {
		
	}
	
	public void tearDown() {
		
	}
	
	public void testConstructor() {
		// Test null input
		SoundDataSource sounds = new SoundDataSource(null);
		Assert.assertNotNull(sounds);
		// Test member context (androidTestCase, I think this is the one
		// we want to use for the rest of the tests?
		sounds = new SoundDataSource(mContext);
		Assert.assertNotNull(sounds);
		// Tests getContext() as input.
		sounds = new SoundDataSource(getContext());
		Assert.assertNotNull(sounds);
	}
	
	/**
	 * Tests if its possible to connect to the database.
	 */
	public void testOpen() {
		// Set up.
		SoundDataSource sounds = new SoundDataSource(getContext());
		// Test.
		sounds.open();
		Assert.assertTrue(sounds.isOpened());
		// Tear down.
		sounds.close();
	}
	
	/**
	 * Tests if it's possible to close the database connection.
	 */
	public void testClose() {
		// Set up.
		SoundDataSource sounds = new SoundDataSource(getContext());
		sounds.open();
		// Test.
		sounds.close();
		Assert.assertFalse(sounds.isOpened());
		// Tear down.
	}
	
	public void testDeleteSound() {
		// Set up.
		Random random = new Random();
		SoundDataSource sounds = new SoundDataSource(getContext());
		int randomSoundValue = random.nextInt();
		Sound sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			sounds.deleteSound(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open sounds for the rest of the tests.
		sounds.open();
		
		// Test id
		sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		Assert.assertEquals(-1, sound.getId());
		sounds.save(sound);
		Assert.assertTrue(0 < sound.getId());
		sounds.deleteSound(sound);
		
		// Test properties;
		sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		sounds.save(sound);
		sounds.deleteSound(sound);
		Assert.assertEquals(randomSoundValue, sound.getSoundValue());
		Assert.assertEquals(TEST_NAME_STRING, sound.getName());
		// Tear down.
		sounds.close();
	}
	
	public void testGetAllSounds() {
		// Set up.
		Random random = new Random();
		SoundDataSource sounds = new SoundDataSource(getContext());
		int randomSoundValue = random.nextInt();
		int randomNumberOfTimes = random.nextInt(BIG_NUMBER);
		Sound sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		List<Sound> testList = null;
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			sounds.deleteSound(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Now open the database for the rest of the test.
		sounds.open();
		
		int sizeBefore = sounds.getAllSounds().size();
		
		// Test with data added.
		for(int i = 0; i < randomNumberOfTimes; i++) {
			sound = new Sound(i, TEST_NAME_STRING);
			sounds.save(sound);
		}
		testList = sounds.getAllSounds();
		Assert.assertEquals(randomNumberOfTimes+sizeBefore, testList.size());

		// Tear down.
		sounds.close();
	}
	
	public void testGetSoundFromKey() {
		// Set up.
		Random random = new Random();
		SoundDataSource sounds = new SoundDataSource(getContext());
		int randomSoundValue = random.nextInt();
		long randomSoundId = random.nextLong();
		Sound sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			sounds.deleteSound(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open sounds for the rest of the tests.
		sounds.open();

		// Test random id input save thing.
		sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		sound.setId(randomSoundId);
		sounds.save(sound);
		Sound selectedSound = sounds.getSoundFromKey(sound.getId());
		Assert.assertEquals(sound.getId(), selectedSound.getId());

		// Tear down.
		sounds.close();
	}
	
	public void testSave() {
		// Set up.
		Random random = new Random();
		SoundDataSource sounds = new SoundDataSource(getContext());
		int randomSoundValue = random.nextInt();
		long randomId = random.nextLong();
		Sound sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			sounds.deleteSound(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open the database connection for the rest of the tests.
		sounds.open();

		// Test no id
		sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		Assert.assertTrue(sound.getId() == -1);
		sounds.save(sound);
		Assert.assertTrue(sound.getId() > 0);
		// Test id
		sound = new Sound(randomSoundValue, TEST_NAME_STRING);
		sound.setId(randomId);
		Assert.assertEquals(randomId, sound.getId());
		sounds.save(sound);
		Assert.assertEquals(randomId, sound.getId());
		// Tear down.
		sounds.close();
	}
}
