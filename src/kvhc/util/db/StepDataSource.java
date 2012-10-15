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

public class StepDataSource {
	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { StepSQLiteHelper.COLUMN_ID, 
			StepSQLiteHelper.COLUMN_VELOCITY, 
			StepSQLiteHelper.COLUMN_ACTIVE, 
			StepSQLiteHelper.COLUMN_NUMBER,
			StepSQLiteHelper.FKEY_CHANNELID};
	
	public StepDataSource(Context context) {
		dbHelper = new DatabaseHandler(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Step createStep(float velocity, boolean active, int stepNumber, Channel channel) {
		ContentValues values = new ContentValues();
		values.put(StepSQLiteHelper.COLUMN_VELOCITY, velocity);
		values.put(StepSQLiteHelper.COLUMN_ACTIVE, active ? 1 : 0);
		values.put(StepSQLiteHelper.COLUMN_NUMBER, stepNumber);
		values.put(StepSQLiteHelper.FKEY_CHANNELID, channel.getId());
		long insertId = database.insert(StepSQLiteHelper.TABLE_STEP, null, values);
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
		
		String where = StepSQLiteHelper.FKEY_CHANNELID + " = ? ";
		String[] whereArgs = new String[] { String.valueOf(channel.getId()) };
		Cursor cursor = database.query(StepSQLiteHelper.TABLE_STEP, allColumns, where, whereArgs,null,null,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Step step = cursorToStep(cursor);
			steps.add(step);
			cursor.moveToNext();
		}
		
		cursor.close();
		return steps;
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
