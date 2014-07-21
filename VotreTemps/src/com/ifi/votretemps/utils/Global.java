package com.ifi.votretemps.utils;

import java.util.ArrayList;
import java.util.List;

import com.ifi.votretemps.dal.DataGateway;
import com.ifi.votretemps.dal.Database;
import com.ifi.votretemps.model.bo.BOTask;
import com.ifi.votretemps.model.bo.BOUser;

public class Global {
	public static List<BOUser> users;
	public static List<BOTask> tasks;

	public static DataGateway dataGateway;
	public static Database database;

	public static int tempsItem = 99999;
	public static int userIDNow = 99999;

	public static int language = 0;
	public static int alarm = 0;
	public static int limit = 0;
	public static int temps = 0;

	// public static long shit = 0;

	public static ArrayList<String> LIMITS;
	public static ArrayList<String> LANGUAGES;

	public static void setupGlobal() {
		// LIMIT
		LIMITS = new ArrayList<String>();
		LIMITS.add("2");
		LIMITS.add("3");
		LIMITS.add("4");

		LANGUAGES = new ArrayList<String>();
		LANGUAGES.add("English");
		LANGUAGES.add("Français");
	}
}
