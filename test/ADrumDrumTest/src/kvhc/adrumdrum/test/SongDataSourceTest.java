package kvhc.adrumdrum.test;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import kvhc.models.Song;
import kvhc.util.db.SongDataSource;
import android.database.SQLException;
import android.test.AndroidTestCase;

/**
 * Unit test class for SongDataSource.
 * Tests all the important functions.
 * @author srejv
 *
 */
public class SongDataSourceTest extends AndroidTestCase {

	private static final int MAX_RANDOM_TEST_NUMBER_OF_CHANNELS = 16;
	private static final int BIG_NUMBER = 100;
	
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
		SongDataSource songs = new SongDataSource(null);
		Assert.assertNotNull(songs);
		// Test member context (androidTestCase, I think this is the one
		// we want to use for the rest of the tests?
		songs = new SongDataSource(mContext);
		Assert.assertNotNull(songs);
		// Tests getContext() as input.
		songs = new SongDataSource(getContext());
		Assert.assertNotNull(songs);
	}
	
	/**
	 * Tests if its possible to connect to the database.
	 */
	public void testOpen() {
		// Set up.
		SongDataSource songs = new SongDataSource(getContext());
		
		// Test.
		songs.open();
		Assert.assertTrue(songs.isOpened());
		
		// Tear down.
		songs.close();
	}
	
	/**
	 * Tests if it's possible to close the database connection.
	 */
	public void testClose() {
		// Set up.
		SongDataSource songs = new SongDataSource(getContext());
		songs.open();
		
		// Test.
		songs.close();
		Assert.assertFalse(songs.isOpened());
		
		// Tear down.
	}
	
	/**
	 * Test the deleteSong functionality in SongDataSource.
	 */
	public void testDeleteSong() {
		// Set up.
		Random random = new Random();
		SongDataSource songs = new SongDataSource(mContext);
		int randomChannel = random.nextInt(MAX_RANDOM_TEST_NUMBER_OF_CHANNELS);
		// Test exception when database not opened
		boolean didThrow = false;
		try {
			songs.deleteSong(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Open connection for the rest of the tests.
		songs.open();
		// Test new Song input.
		songs.deleteSong(new Song(randomChannel));
		// Test reference song input.
		Song song = new Song(randomChannel);
		songs.deleteSong(song);
		// Test saved song.
		song = new Song(randomChannel);
		songs.save(song);
		songs.deleteSong(song);
		// Test loaded song.
		song = new Song(randomChannel);
		song.setName("test");
		songs.save(song);
		List<Song> testList = songs.getAllSongs();
		Assert.assertTrue(testList.contains(song));
		songs.deleteSong(song);
		testList = songs.getAllSongs();
		Assert.assertFalse(testList.contains(song));
		// Tear down.
		
		songs.close();
	}
	
	/**
	 * Test the GetAllSongs functionality in SongDataSource.
	 */
	public void testGetAllSongs() {
		// Set up.
		Random random = new Random();
		SongDataSource songs = new SongDataSource(mContext);
		int randomChannel = random.nextInt(MAX_RANDOM_TEST_NUMBER_OF_CHANNELS);
		int amountOfSongs = random.nextInt(BIG_NUMBER);

		// Test exception when database not opened
		boolean didThrow = false;
		try {
			songs.getAllSongs();
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		
		// Open connection for the rest of the tests.
		songs.open();
		
		// Check number of old songs
		int amountOfOldSongs = songs.getAllSongs().size();
		
		// Test new Song input.
		for(int i = 0; i < amountOfSongs; i++) {
			songs.save(new Song(randomChannel));
		}
		int songsAdded = songs.getAllSongs().size()-amountOfOldSongs;
		Assert.assertEquals(amountOfSongs, songsAdded);

		// Tear down.
		songs.close();
	}
	
	/**
	 * Test the saving functionality of SongDataSource.
	 */
	public void testSave() {
		// Set up.
		Random random = new Random();
		SongDataSource songs = new SongDataSource(mContext);
		int randomChannel = random.nextInt(MAX_RANDOM_TEST_NUMBER_OF_CHANNELS);
		// Test exception when database not opened
		boolean didThrow = false;
		try {
			songs.save(null);
		} catch(SQLException exeption) {
			didThrow = true;
		}
		Assert.assertTrue("Since there was no database connection open it should throw an exception.", didThrow);
		// Open connection for the rest of the tests.
		songs.open();

		// Test empty song
		songs.save(new Song(randomChannel));
		// Test song with parameters
		Song song = new Song(randomChannel);
		song.setBpm(80);
		song.setName("DebugSong");
		Assert.assertTrue(song.getId() == 0);
		songs.save(song);
		Assert.assertTrue(song.getId() > 0);
		songs.deleteSong(song);
		Assert.assertTrue(song.getId() == 0);
		// Tear down.
		songs.close();
		
	}
}
