package kvhc.adrumdrum.test;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import kvhc.models.Channel;
import kvhc.models.Song;
import kvhc.util.db.ChannelDataSource;
import android.database.SQLException;
import android.test.AndroidTestCase;

/**
 * Unit test class for SongDataSource.
 * Tests all the important functions.
 * @author srejv
 *
 */
public class ChannelDataSourceTest extends AndroidTestCase {

	private static final int MAX_RANDOM_TEST_VAR = 16;
	
	// Maybe set up
	public void setUp() {
		// make backup of database.
		// drop the current database, create a new empty one.
	}
	
	public void tearDown() {
		// drop the database created for the tests.
		// insert the backed up database.
	}

	/**
	 * Tests the constructor.
	 */
	public void testConstructor() {
		// Test null input
		ChannelDataSource channels = new ChannelDataSource(null);
		Assert.assertNotNull(channels);
		// Test member context (androidTestCase, I think this is the one
		// we want to use for the rest of the tests?
		channels = new ChannelDataSource(mContext);
		Assert.assertNotNull(channels);
		// Tests getContext() as input.
		channels = new ChannelDataSource(getContext());
		Assert.assertNotNull(channels);
	}
	

	/**
	 * Tests if its possible to connect to the database.
	 */
	public void testOpen() {
		// Set up.
		ChannelDataSource channels = new ChannelDataSource(mContext);
		
		// Test.
		channels.open();
		Assert.assertTrue(channels.isOpened());
		
		// Tear down.
		channels.close();
	}
	
	/**
	 * Tests if it's possible to close the database connection.
	 */
	public void testClose() {
		// Set up.
		ChannelDataSource channels = new ChannelDataSource(mContext);
		channels.open();
		
		// Test.
		channels.close();
		Assert.assertFalse(channels.isOpened());
		
		// Tear down.
	}
	
	public void testDeleteChannel() {
		// Set up.
		Random random = new Random();
		ChannelDataSource channels = new ChannelDataSource(mContext);
		Channel channel = null;
		int insertChannelNumber = random.nextInt(MAX_RANDOM_TEST_VAR);
		boolean insertMute = random.nextInt() % 2 == 0;
		float insertRightPan = random.nextFloat();
		float insertLeftPan = random.nextFloat();
		float insertVolume = random.nextFloat();
		Song song = new Song(0);
		song.setId(random.nextLong());
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			channels.deleteChannel(channel);
		} catch(SQLException exception) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open the Data Source for the rest of the tests.
		channels.open();
		// Test null channel
		channels.deleteChannel(channel);
		// Test unsaved channel
		channel = new Channel();
		channels.deleteChannel(channel);
		// Test saved channel
		channel = new Channel();
		Assert.assertTrue(channel.getId() == 0);
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() > 0);
		channels.deleteChannel(channel);
		Assert.assertTrue(channel.getId() == 0);
		
		// Test saved channel properties
		channel = new Channel();
		channel.setChannelNumber(insertChannelNumber);
		channel.setMute(insertMute);
		channel.setPanning(insertRightPan, insertLeftPan);
		channel.setVolume(insertVolume);
		Assert.assertTrue(channel.getId() == 0);
		Assert.assertEquals(insertChannelNumber, channel.getChannelNumber());
		Assert.assertEquals(insertMute, channel.isMuted());
		Assert.assertEquals(insertRightPan, channel.getRightPanning());
		Assert.assertEquals(insertLeftPan, channel.getLeftPanning());
		Assert.assertEquals(insertVolume, channel.getVolume());
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() > 0);
		Assert.assertEquals(insertChannelNumber, channel.getChannelNumber());
		Assert.assertEquals(insertMute, channel.isMuted());
		Assert.assertEquals(insertRightPan, channel.getRightPanning());
		Assert.assertEquals(insertLeftPan, channel.getLeftPanning());
		Assert.assertEquals(insertVolume, channel.getVolume());
		channels.deleteChannel(channel);
		Assert.assertTrue(channel.getId() == 0);
		Assert.assertEquals(insertChannelNumber, channel.getChannelNumber());
		Assert.assertEquals(insertMute, channel.isMuted());
		Assert.assertEquals(insertRightPan, channel.getRightPanning());
		Assert.assertEquals(insertLeftPan, channel.getLeftPanning());
		Assert.assertEquals(insertVolume, channel.getVolume());
		// Tear down.
		channels.close();
	}
	
	
	public void testGetAllChannelsForSong() {
		// Set up.
		Random random = new Random();
		ChannelDataSource channels = new ChannelDataSource(mContext);
		int numberOfChannelsToAdd = random.nextInt(MAX_RANDOM_TEST_VAR);
		Channel channel = null;
		List<Channel> testList = null;
		Song song = new Song(0);
		song.setId(random.nextLong());
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			channels.getAllChannelsForSong(null);
		} catch(SQLException exception) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Set as opened for the rest of the tests.
		channels.open();
		// Test null
		testList = channels.getAllChannelsForSong(null);
		Assert.assertEquals(0, testList.size());
		// Test new no id input
		testList = channels.getAllChannelsForSong(new Song(numberOfChannelsToAdd));
		Assert.assertEquals(0, testList.size());
		// Test with channels.
		for(int i = 0; i < numberOfChannelsToAdd; i++) {
			channel = new Channel();
			channels.save(song, channel);
		}
		testList = channels.getAllChannelsForSong(song);
		Assert.assertEquals(numberOfChannelsToAdd, testList.size());
		// Tear down.
		channels.close();
	}
	
	public void testSave() {
		// Set up.
		Random random = new Random();
		ChannelDataSource channels = new ChannelDataSource(mContext);
		Channel channel = null;
		int insertChannelNumber = random.nextInt(MAX_RANDOM_TEST_VAR);
		int numberOfChannelsToAdd = random.nextInt(MAX_RANDOM_TEST_VAR);
		boolean insertMute = random.nextInt() % 2 == 0;
		float insertRightPan = random.nextFloat();
		float insertLeftPan = random.nextFloat();
		float insertVolume = random.nextFloat();
		Song song = new Song(0);
		song.setId(random.nextLong());
		// Test exception when database not opened.
		boolean didThrow = false;
		try {
			channels.save(null, null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open database for transactions for the rest of the tests.
		channels.open();
		// Test song null, channel null
		channels.save(null, null);
		Assert.assertTrue(true); // Hopefully no crash.
		// Test song no id, channel null
		
		channels.save(new Song(numberOfChannelsToAdd), null);
		Assert.assertTrue(true); // Hopefully no crash.
		// Test song id, channel null
		song = new Song(numberOfChannelsToAdd );
		song.setId(random.nextLong());
		channels.save(song, null);
		// Test song null, channel no id
		channel = new Channel();
		Assert.assertTrue(channel.getId() == 0);
		channels.save(new Song(numberOfChannelsToAdd), channel);
		Assert.assertTrue(channel.getId() == 0);
		// Test song no id, channel no id
		channel = new Channel();
		Assert.assertTrue(channel.getId() == 0);
		channels.save(new Song(numberOfChannelsToAdd ), channel);
		Assert.assertTrue(channel.getId() > 0);
		// Test song id, channel no id
		channel = new Channel();
		song = new Song(numberOfChannelsToAdd );
		song.setId(random.nextLong());
		Assert.assertTrue(channel.getId() == 0);
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() > 0);
		// Test song null, channel id
		channel = new Channel();
		Assert.assertTrue(channel.getId() == 0);
		channels.save(null, channel);
		Assert.assertTrue(channel.getId() == 0);
		// Test song no id, channel id
		channel = new Channel();
		song = new Song(numberOfChannelsToAdd);
		Assert.assertTrue(channel.getId() == 0);
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() == 0);
		// Test song id, channel id
		channel = new Channel();
		channel.setId(random.nextLong());
		song = new Song(numberOfChannelsToAdd );
		song.setId(random.nextLong());
		Assert.assertTrue(channel.getId() > 0);
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() > 0);
		// Test saved channel properties
		channel = new Channel();
		channel.setChannelNumber(insertChannelNumber);
		channel.setMute(insertMute);
		channel.setPanning(insertRightPan, insertLeftPan);
		channel.setVolume(insertVolume);
		Assert.assertTrue(channel.getId() == 0);
		Assert.assertEquals(insertChannelNumber, channel.getChannelNumber());
		Assert.assertEquals(insertMute, channel.isMuted());
		Assert.assertEquals(insertRightPan, channel.getRightPanning());
		Assert.assertEquals(insertLeftPan, channel.getLeftPanning());
		Assert.assertEquals(insertVolume, channel.getVolume());
		channels.save(song, channel);
		Assert.assertTrue(channel.getId() > 0);
		Assert.assertEquals(insertChannelNumber, channel.getChannelNumber());
		Assert.assertEquals(insertMute, channel.isMuted());
		Assert.assertEquals(insertRightPan, channel.getRightPanning());
		Assert.assertEquals(insertLeftPan, channel.getLeftPanning());
		Assert.assertEquals(insertVolume, channel.getVolume());
		channels.deleteChannel(channel);
		// Tear down.
		channels.close();
	}
	
	
}
