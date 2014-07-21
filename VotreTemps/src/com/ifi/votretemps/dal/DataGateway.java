package com.ifi.votretemps.dal;

import android.content.Context;

public class DataGateway {
	private static DataGateway _instanceConnectionSingleton = null;
	private Database database;

	public static synchronized DataGateway getInstanceConnection() {
		if (_instanceConnectionSingleton == null) {
			synchronized (DataGateway.class) {
				if (_instanceConnectionSingleton == null) {
					_instanceConnectionSingleton = new DataGateway();
				}
			}
		}
		return _instanceConnectionSingleton;
	}

	public Database connectToDatabase(Context context) {
		setDatabase(new Database(context));

		return database;
	}

	// Set get stuff
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
}
