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

import kvhc.player.Channel;
import kvhc.player.Step;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StepDataSource {
	
	private SQLiteDatabase database;
	private StepSQLiteHelper dbHelper;
	
	private String[] allColumns = { StepSQLiteHelper.COLUMN_ID, 
			StepSQLiteHelper.COLUMN_VELOCITY, 
			StepSQLiteHelper.COLUMN_ACTIVE, 
			StepSQLiteHelper.COLUMN_NUMBER,
			StepSQLiteHelper.FKEY_CHANNELID};
	
	public StepDataSource(Context context) {
		dbHelper = new StepSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Step createStep(float velocity, boolean active, int stepNumber, Channel channel) {
		
		if(channel.getId() == 0) {
			return null;
		}
		
		ContentValues values = new ContentValues();
		values.put(StepSQLiteHelper.COLUMN_VELOCITY, velocity);
		values.put(StepSQLiteHelper.COLUMN_ACTIVE, active ? 1 : 0);
		values.put(StepSQLiteHelper.COLUMN_NUMBER, stepNumber);
		values.put(StepSQLiteHelper.FKEY_CHANNELID, channel.getId());
		
		
		long insertId = database.insert(StepSQLiteHelper.TABLE_STEP, null, values);
		Log.w("StepData", "Insert step: " + insertId);
		Cursor cursor = database.query(StepSQLiteHelper.TABLE_STEP, allColumns, StepSQLiteHelper.COLUMN_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Step newStep = cursorToStep(cursor);
		cursor.close();
		return newStep;
	}
	
	public void deleteStep(Step step) {
		long id = step.getId();
		
		// Delete sound
		database.delete(StepSQLiteHelper.TABLE_STEP, StepSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
		
	public List<Step> getAllStepsForChannel(Channel channel) {
		
		List<Step> steps = new ArrayList<Step>();
		
		if(channel == null || channel.getId() == 0) {
			return steps;
		}
		
		String where = StepSQLiteHelper.FKEY_CHANNELID + "=?";
		String[] whereArgs = new String[] { String.valueOf(channel.getId()) };
		String orderBy = StepSQLiteHelper.COLUMN_NUMBER + " ASC";
		Cursor cursor = database.query(StepSQLiteHelper.TABLE_STEP, allColumns, where, whereArgs, null, null, orderBy);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Step step = cursorToStep(cursor);
			steps.add(step);
			cursor.moveToNext();
		}
		
		cursor.close();
		Log.w("StepData", "Number of steps: " + steps.size() + " for channel id: " + channel.getId());
		return steps;
	}
	
	private void updateStep(Step step, Channel channel) {
		ContentValues values = new ContentValues();
		values.put(StepSQLiteHelper.COLUMN_VELOCITY, step.getVelocity());
		values.put(StepSQLiteHelper.COLUMN_ACTIVE, step.isActive());
		values.put(StepSQLiteHelper.COLUMN_NUMBER, step.getStepNumber());
		values.put(StepSQLiteHelper.FKEY_CHANNELID, channel.getId());
		
		String where = StepSQLiteHelper.COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(step.getId()) }; 
		database.update(StepSQLiteHelper.TABLE_STEP, values, where, whereArgs);
	}
	
	
	public void save(Step step, Channel channel) {
		
		if(channel.getId() == 0) {
			// Channel doesn't have any ID, not worth saving.
			return;
		}
		
		if(step.getId() > 0) {
			updateStep(step, channel);
		} else {
			Step tmp = createStep(step.getVelocity(), step.isActive(), step.getStepNumber(), channel);
			step.setId(tmp.getId());
		}
	}
	
	private Step cursorToStep(Cursor cursor) {
		Step step = new Step();
		step.setId(cursor.getLong(0));
		step.setActive(cursor.getInt(2) > 0);
		step.setStepNumber(cursor.getInt(3));
		step.setVelolcity((float)cursor.getDouble(1));
		
		return step;
	}
}
