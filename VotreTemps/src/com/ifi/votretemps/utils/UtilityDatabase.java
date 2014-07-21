package com.ifi.votretemps.utils;

import java.util.List;

import com.ifi.votretemps.model.bo.BOUser;

public class UtilityDatabase {	
	public static int checkIndentifyUser(String username, String password,
			List<BOUser> users) {
		for (int i=0; i<users.size(); i++) {
			if (username.compareTo(users.get(i).getUserName()) == 0) {
				if (password.compareTo(users.get(i).getPassword()) == 0) {
					return users.get(i).getUserId();
				}
				break;
			}
		}
		
		return 0;
	}
}
