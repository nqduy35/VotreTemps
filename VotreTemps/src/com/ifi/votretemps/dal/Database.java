package com.ifi.votretemps.dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ifi.votretemps.model.bo.BOTask;
import com.ifi.votretemps.model.bo.BOUser;
import com.ifi.votretemps.utils.KeyDatabase;

public class Database extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "GTemps";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_USERGT = "CREATE TABLE "
				+ KeyDatabase._nameOfTableUser + "(" + KeyDatabase._keyUserID
				+ " INTEGER PRIMARY KEY NOT NULL," + KeyDatabase._keyUserName
				+ " TEXT," + KeyDatabase._keyPassword + " TEXT);";

		String CREATE_TABLE_TASKGT = "CREATE TABLE "
				+ KeyDatabase._nameOfTableTask + "(" + KeyDatabase._keyTaskID
				+ " INTEGER PRIMARY KEY NOT NULL," + KeyDatabase._keyTaskName
				+ " TEXT," + KeyDatabase._keyInfo + " TEXT,"
				+ KeyDatabase._keyStart + " DATETIME," + KeyDatabase._keyEnd
				+ " DATETIME," + KeyDatabase._keyColor + " INTEGER,"
				+ KeyDatabase._keyState + " INTEGER," + KeyDatabase._keyUserID
				+ " INTEGER" + ");";

		db.execSQL(CREATE_TABLE_USERGT);
		db.execSQL(CREATE_TABLE_TASKGT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS '" + KeyDatabase._nameOfTableUser
				+ "';");
		db.execSQL("DROP TABLE IF EXISTS '" + KeyDatabase._nameOfTableTask
				+ "';");
		onCreate(db);
	}

	public void deleteTable(SQLiteDatabase db, String nameOfTable) {
		db.execSQL("DROP TABLE IF EXISTS " + nameOfTable);
	}

	// Add Object
	public void addUser(BOUser newUser) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KeyDatabase._keyUserID, newUser.getUserId());
		values.put(KeyDatabase._keyUserName, newUser.getUserName());
		values.put(KeyDatabase._keyPassword, newUser.getPassword());

		db.insert(KeyDatabase._nameOfTableUser, null, values);
		db.close();
	}

	public void addTask(BOTask newTask) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KeyDatabase._keyTaskID, newTask.getTaskId());
		values.put(KeyDatabase._keyTaskName, newTask.getTaskName());
		values.put(KeyDatabase._keyInfo, newTask.getInfo());
		values.put(KeyDatabase._keyStart, newTask.getStartDate());
		values.put(KeyDatabase._keyEnd, newTask.getEndDate());
		values.put(KeyDatabase._keyColor, newTask.getColor());
		values.put(KeyDatabase._keyState, newTask.getState());
		values.put(KeyDatabase._keyUserID, newTask.getUserId());

		db.insert(KeyDatabase._nameOfTableTask, null, values);
		db.close();
	}

	// Delete Object
	public void deleteUser(int userID) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(KeyDatabase._nameOfTableUser,
				KeyDatabase._keyUserID + " = ?",
				new String[] { String.valueOf(userID) });
		db.close();
	}

	public void deleteTask(int taskID) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(KeyDatabase._nameOfTableTask,
				KeyDatabase._keyTaskID + " = ?",
				new String[] { String.valueOf(taskID) });
		db.close();
	}

	// Update
	public int updateUser(BOUser updatedUser) {
		int result = 0;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KeyDatabase._keyPassword, updatedUser.getPassword());

		result = db.update(KeyDatabase._nameOfTableUser, values,
				KeyDatabase._keyUserID + " = ?",
				new String[] { String.valueOf(updatedUser.getUserId()) });

		db.close();
		return result;
	}

	public int updateTask(BOTask updatedTask) {
		int result = 0;

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KeyDatabase._keyTaskName, updatedTask.getTaskName());
		values.put(KeyDatabase._keyInfo, updatedTask.getInfo());
		values.put(KeyDatabase._keyStart, updatedTask.getStartDate());
		values.put(KeyDatabase._keyEnd, updatedTask.getEndDate());
		values.put(KeyDatabase._keyColor, updatedTask.getColor());
		values.put(KeyDatabase._keyState, updatedTask.getState());
		values.put(KeyDatabase._keyUserID, updatedTask.getUserId());

		result = db.update(KeyDatabase._nameOfTableTask, values,
				KeyDatabase._keyTaskID + " = ?",
				new String[] { String.valueOf(updatedTask.getTaskId()) });

		db.close();
		return result;
	}

	// Get all
	public List<BOUser> getAllUsers() {
		ArrayList<BOUser> users = new ArrayList<BOUser>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + KeyDatabase._nameOfTableUser;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				BOUser newUser = new BOUser();
				newUser.setUserId(cursor.getInt(0));
				newUser.setUserName(cursor.getString(1));
				newUser.setPassword(cursor.getString(2));

				// Adding contact to list
				users.add(newUser);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return users;
	}

	public List<BOTask> getAllTasks() {
		ArrayList<BOTask> tasks = new ArrayList<BOTask>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + KeyDatabase._nameOfTableTask;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				BOTask newTask = new BOTask();
				newTask.setTaskId(cursor.getInt(0));
				newTask.setTaskName(cursor.getString(1));
				newTask.setInfo(cursor.getString(2));
				newTask.setStartDate(cursor.getString(3));
				newTask.setEndDate(cursor.getString(4));
				newTask.setColor(cursor.getInt(5));
				newTask.setState(cursor.getInt(6));
				newTask.setUserId(cursor.getInt(7));

				// Adding contact to list
				tasks.add(newTask);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return tasks;
	}

	// Get specific
	public BOUser getUser(int userId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db
				.query(KeyDatabase._nameOfTableUser, new String[] {
						KeyDatabase._keyUserID, KeyDatabase._keyUserName,
						KeyDatabase._keyPassword }, KeyDatabase._keyUserID
						+ "=?", new String[] { String.valueOf(userId) }, null,
						null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		BOUser newUser = new BOUser(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2));

		return newUser;
	}

	public List<BOTask> getTask(int userId) {
		ArrayList<BOTask> tasks = new ArrayList<BOTask>();
		ArrayList<BOTask> results = new ArrayList<BOTask>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + KeyDatabase._nameOfTableTask;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				BOTask newTask = new BOTask();
				newTask.setTaskId(cursor.getInt(0));
				newTask.setTaskName(cursor.getString(1));
				newTask.setInfo(cursor.getString(2));
				newTask.setStartDate(cursor.getString(3));
				newTask.setEndDate(cursor.getString(4));
				newTask.setColor(cursor.getInt(5));
				newTask.setState(cursor.getInt(6));
				newTask.setUserId(cursor.getInt(7));

				// Adding contact to list
				tasks.add(newTask);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getUserId() == userId) {
				results.add(tasks.get(i));
			}
		}

		return results;
	}

	public BOTask getATask(int taskId) {
		ArrayList<BOTask> tasks = new ArrayList<BOTask>();
		BOTask result = new BOTask();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + KeyDatabase._nameOfTableTask;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				BOTask newTask = new BOTask();
				newTask.setTaskId(cursor.getInt(0));
				newTask.setTaskName(cursor.getString(1));
				newTask.setInfo(cursor.getString(2));
				newTask.setStartDate(cursor.getString(3));
				newTask.setEndDate(cursor.getString(4));
				newTask.setColor(cursor.getInt(5));
				newTask.setState(cursor.getInt(6));
				newTask.setUserId(cursor.getInt(7));

				// Adding contact to list
				tasks.add(newTask);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getTaskId() == taskId) {
				result = tasks.get(i);
			}
		}

		return result;
	}
}
