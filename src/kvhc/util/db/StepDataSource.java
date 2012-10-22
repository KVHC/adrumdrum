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

package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.models.Channel;
import kvhc.models.Step;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DataSource object for Step model.
 * @author kvhc
 */
public class StepDataSource {
	
	private SQLiteDatabase database;
	private StepSQLiteHelper dbHelper;
	private boolean mIsOpened = false;
	
	private String[] allColumns = { StepSQLiteHelper.COLUMN_ID, 
			StepSQLiteHelper.COLUMN_VELOCITY, 
			StepSQLiteHelper.COLUMN_ACTIVE, 
			StepSQLiteHelper.COLUMN_NUMBER,
			StepSQLiteHelper.FKEY_CHANNELID};
	
	/**
	 * Constructor.
	 * @param context Current context, needed for database usage.
	 */
	public StepDataSource(Context context) {
		dbHelper = new StepSQLiteHelper(context);
		mIsOpened = false;
	}
	
	/**
	 * Opens the database for usage.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		mIsOpened = true;
	}
	
	/**
	 * Closes the database.
	 */
	public void close() {
		dbHelper.close();
		mIsOpened = false;
	}
	
	/**
	 * Creates a step from the parameters and adds it to the database.
	 * Returns the newly added Step with ID from the database.
	 * 
	 * @throws SQLException if the database connection is not open.
	 * @param velocity Step velocity
	 * @param active Is the step active?
	 * @param stepNumber The number of the step.
	 * @param channel Channel the step belongs to.
	 * @return The step id from the database.
	 */
	private long createStep(float velocity, boolean active, int stepNumber, Channel channel)throws SQLException {
		if(!mIsOpened) {
			throw new SQLException("No database connection.");
		}
		
		// Is the channel in the database?
		if(channel.getId() == 0) {
			// Nope. It's a loose step. Don't add.
			return 0;
		}
		
		// Create a value table.
		ContentValues values = new ContentValues();
		values.put(StepSQLiteHelper.COLUMN_VELOCITY, velocity);
		values.put(StepSQLiteHelper.COLUMN_ACTIVE, active ? 1 : 0);
		values.put(StepSQLiteHelper.COLUMN_NUMBER, stepNumber);
		values.put(StepSQLiteHelper.FKEY_CHANNELID, channel.getId());
		
		// Insert into the database.
		long insertId = database.insert(StepSQLiteHelper.TABLE_STEP, null, values);
		
		// Return step id.
		return insertId;
	}
	
	/**
	 * Deletes a step from the database.
	 * 
	 * Returns the amount of steps deleted.
	 * @throws SQLException if the database connection is not open.
	 * @param step The step to be deleted.
	 * @return The amount of steps deleted.
	 */
	public int deleteStep(Step step) throws SQLException {
		if(!mIsOpened) {
			throw new SQLException("No database connection.");
		}
		// Get the id
		long id = step.getId();
		step.setId(0);
		// Delete step
		return database.delete(StepSQLiteHelper.TABLE_STEP, StepSQLiteHelper.COLUMN_ID + StepSQLiteHelper.EQUALS_TO + id, null);
	}
	
	/**
	 * Creates and returns a list of all the steps that are owned in the 
	 * specified channel. 
	 * 
	 * If the Channel object doesn't have an id (not loaded from the db) 
	 * it will return an empty list of steps.
	 * 
	 * @throws SQLException if the database connection is not open.
	 * @param channel The Channel to load steps from.
	 * @return A list of steps.
	 */
	public List<Step> getAllStepsForChannel(Channel channel) throws SQLException {
		if(!mIsOpened) {
			throw new SQLException("No database connection.");
		}
		
		// Create a list for the steps.
		List<Step> steps = new ArrayList<Step>();

		
		
		// Does the channel exist? 
		if(channel == null || channel.getId() == 0) {
			// Nope, return an empty list of steps.
			return steps;
		}
		
		// Set up the selection query
		String where = StepSQLiteHelper.FKEY_CHANNELID + StepSQLiteHelper.EQUALS_TO_PARAMETER;
		String[] whereArgs = new String[] { String.valueOf(channel.getId()) };
		String orderBy = StepSQLiteHelper.COLUMN_NUMBER + " ASC";
		
		// Execute the query
		Cursor cursor = database.query(StepSQLiteHelper.TABLE_STEP, allColumns, where, whereArgs, null, null, orderBy);
		
		// Load all the steps that are found
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Step step = cursorToStep(cursor);
			steps.add(step);
			cursor.moveToNext();
		}
		cursor.close();
		
		// Return the steps.
		return steps;
	}
	
	/**
	 * Updates a step with the parameters from the Step object and
	 * Channel object.
	 * @throws SQLException if the database connection is not open.
	 * @param step The Step to update.
	 * @param channel The Channel the Step belongs to.
	 */
	private void updateStep(Step step, Channel channel)	throws SQLException {
		if(!mIsOpened) {
			throw new SQLException("No database connection.");
		}
		// Does the step exist?
		if(step == null || step.getId() == 0) {
			// The step doesn't exist. Don't update.
			return;
		}
		
		// Does the channel exist?
		if(channel == null || channel.getId() == 0) {
			// The channel doesn't exist. Don't update.
			return;
		}
		
		// Set up the update values.
		ContentValues values = new ContentValues();
		values.put(StepSQLiteHelper.COLUMN_VELOCITY, step.getVelocity());
		values.put(StepSQLiteHelper.COLUMN_ACTIVE, step.isActive());
		values.put(StepSQLiteHelper.COLUMN_NUMBER, step.getStepNumber());
		values.put(StepSQLiteHelper.FKEY_CHANNELID, channel.getId());
		
		// Set up the update query.
		String where = StepSQLiteHelper.COLUMN_ID + StepSQLiteHelper.EQUALS_TO_PARAMETER;
		String[] whereArgs = new String[] { String.valueOf(step.getId()) };
		
		// Run update query.
		database.update(StepSQLiteHelper.TABLE_STEP, values, where, whereArgs);
	}
	
	/**
	 * Saves a step to the database. If the step exists (read has an ID), it updates
	 * the step. If it doesn't exist it is created and an ID is set on the step.
	 * @param step The Step to save.
	 * @param channel The Channel the Step belongs to.
	 * @return Returns -1 if the channel isn't initiated in the database, 1 if updated and 2 if created.
	 */
	public int save(Step step, Channel channel) throws SQLException {
		
		if(!mIsOpened) {
			throw new SQLException("No database connection.");
		}
		
		// Does the channel exist in the database?
		if(channel.getId() == 0) {
			// Channel doesn't have an ID, not in database.
			return -1;
		}
		
		// Does the step already exist?
		if(step.getId() > 0) {
			// Update the step.
			updateStep(step, channel);
			return 1;
		} else {
			// The step doesn't exist, create it.
			long newStepId = createStep(step.getVelocity(), step.isActive(), step.getStepNumber(), channel);
			step.setId(newStepId);
			return 2;
		}
	}
	
	/**
	 * Creates a step from a Cursor object.
	 * 
	 * @param cursor The Cursor object pointing to a row in the Step table.
	 * @return A new step.
	 */
	private Step cursorToStep(Cursor cursor) {
		
		// Create new step.
		Step step = new Step();
		
		Log.d(getClass().toString(), "Creating step id: " + cursor.getLong(StepSQLiteHelper.Columns.ID.index()));
		Log.d(getClass().toString(), "Is Active: " + cursor.getInt(StepSQLiteHelper.Columns.Active.index()));
		Log.d(getClass().toString(), "Step number: " + cursor.getInt(StepSQLiteHelper.Columns.Number.index()));
		Log.d(getClass().toString(), "Velolcity: " + cursor.getDouble(StepSQLiteHelper.Columns.Velocity.index()));
		
		// Set up properties.
		step.setId(cursor.getLong(StepSQLiteHelper.Columns.ID.index()));
		step.setActive(cursor.getInt(StepSQLiteHelper.Columns.Active.index()) > 0);
		step.setStepNumber(cursor.getInt(StepSQLiteHelper.Columns.Number.index()));
		step.setVelolcity((float)cursor.getDouble(StepSQLiteHelper.Columns.Velocity.index()));
		
		// Return the new step.
		return step;
	}

	/**
	 * Returns true if the data source has a connection to the database.
	 * @return True if the object is connected to the database.
	 */
	public boolean isOpened() {
		// TODO Auto-generated method stub
		return mIsOpened;
	}
}
