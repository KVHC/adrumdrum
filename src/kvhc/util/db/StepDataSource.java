package kvhc.util.db;

import java.util.ArrayList;
import java.util.List;

import kvhc.player.Channel;
import kvhc.player.Sound;
import kvhc.player.Step;
import android.app.admin.DeviceAdminInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StepDataSource {
	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { DatabaseHandler.KEY_ID, 
			DatabaseHandler.KEY_VELOCITY, 
			DatabaseHandler.KEY_ACTIVE, 
			DatabaseHandler.KEY_NUMBER,
			DatabaseHandler.FKEY_CHANNELID};
	
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
		values.put(DatabaseHandler.KEY_VELOCITY, velocity);
		values.put(DatabaseHandler.KEY_ACTIVE, active ? 1 : 0);
		values.put(DatabaseHandler.KEY_NUMBER, stepNumber);
		values.put(DatabaseHandler.FKEY_CHANNELID, channel.getId());
		long insertId = database.insert(DatabaseHandler.TABLE_STEP, null, values);
		Cursor cursor = database.query(DatabaseHandler.TABLE_STEP, allColumns, DatabaseHandler.KEY_ID + " = " 
						+ insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Step newStep = cursorToStep(cursor);
		cursor.close();
		return newStep;
	}
	
	public void deleteStep(Step step) {
		long id = step.getId();
		
		// Delete sound
		database.delete(DatabaseHandler.TABLE_STEP, DatabaseHandler.KEY_ID + " = " + id, null);
	}
	
	public List<Step> getAllStepsForChannel(Channel channel) {
		List<Step> steps = new ArrayList<Step>();
		
		String where = DatabaseHandler.FKEY_CHANNELID + " = ? ";
		String[] whereArgs = new String[] { String.valueOf(channel.getId()) };
		Cursor cursor = database.query(DatabaseHandler.TABLE_STEP, allColumns, where, whereArgs,null,null,null);
		
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
