package com.ifi.votretemps.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.ifi.votretemps.model.bo.BOTask;

public class UtilityMethod {
	public static int randomID() {
		Random random = new Random();
		int randomID = random.nextInt();

		if (randomID < 0) {
			randomID = randomID * (-1);
		}

		return randomID;
	}

	public static List<BOTask> separateTaskKanban(int column) {
		List<BOTask> results = new ArrayList<BOTask>();

		for (int i = 0; i < Global.tasks.size(); i++) {
			if (Global.tasks.get(i).getState() == column) {
				results.add(Global.tasks.get(i));
			}
		}

		return results;
	}

	public static int getYearFromDatepicker(String date) {
		String[] parts = date.split("-");
		String part0 = parts[0];
		int year = Integer.parseInt(part0);
		return year;
	}

	public static int getMonthFromDatepicker(String date) {
		String[] parts = date.split("-");
		String part1 = parts[1];
		int month = Integer.parseInt(part1);
		return month;
	}

	public static int getDayFromDatepicker(String date) {
		String[] parts = date.split("-");
		String part2 = parts[2];
		int day = Integer.parseInt(part2);
		return day;
	}

	public static boolean checkAlarme() {
		long todayTime = System.currentTimeMillis();

		for (int i = 0; i < Global.tasks.size(); i++) {
			Calendar dateEnd = Calendar.getInstance();
			dateEnd.set(
					getYearFromDatepicker(Global.tasks.get(i).getEndDate()),
					getMonthFromDatepicker(Global.tasks.get(i).getEndDate()) - 1,
					getDayFromDatepicker(Global.tasks.get(i).getEndDate()));
			long endTime = dateEnd.getTimeInMillis();
			long betweenTime = endTime - todayTime;
			int betweenDay = (int) (betweenTime / (1000 * 60 * 60 * 24));

			if (betweenTime < 0) {
				// If today is more than date, task false
			} else {
				if (betweenDay < 3) {
					return true;
				}
			}
		}

		return false;
	}

	public static long betweenTime(Calendar dateStart, Calendar dateEnd) {
		long start = dateStart.getTimeInMillis();
		long end = dateEnd.getTimeInMillis();

		long result = end - start;
		return result;
	}

	public static int changeTimeToIntGantt(int pos, int parentTimeBar) {
		int timeBar = 0;

		timeBar = (int) parentTimeBar * getWorkDay(pos) / getAllDayInt(pos);

		return timeBar;
	}

	public static int getAllDayInt(int pos) {
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.set(getYearFromDatepicker(Global.tasks.get(pos).getEndDate()),
				getMonthFromDatepicker(Global.tasks.get(pos).getEndDate()) - 1,
				getDayFromDatepicker(Global.tasks.get(pos).getEndDate()));

		Calendar dateStart = Calendar.getInstance();
		dateStart.set(getYearFromDatepicker(Global.tasks.get(pos)
				.getStartDate()), getMonthFromDatepicker(Global.tasks.get(pos)
				.getStartDate()) - 1, getDayFromDatepicker(Global.tasks
				.get(pos).getStartDate()));

		long start = dateStart.getTimeInMillis();
		long end = dateEnd.getTimeInMillis();
		int startInt = (int) start / (1000 * 60 * 60 * 24);
		int endInt = (int) end / (1000 * 60 * 60 * 24);
		// int allDay = (int) ((end - start) / (1000 * 60 * 60 * 24));
		int allDay = endInt - startInt;
		allDay = allDay + 1;

		return allDay;
	}

	public static int getWorkDay(int pos) {
		Calendar dateStart = Calendar.getInstance();
		dateStart.set(getYearFromDatepicker(Global.tasks.get(pos)
				.getStartDate()), getMonthFromDatepicker(Global.tasks.get(pos)
				.getStartDate()) - 1, getDayFromDatepicker(Global.tasks
				.get(pos).getStartDate()));

		long start = dateStart.getTimeInMillis();
		long todayTime = System.currentTimeMillis();

		int todayDay = (int) ((todayTime - start) / (1000 * 60 * 60 * 24));
		todayDay = todayDay + 1;

		return todayDay;
	}

	public static int getLeftDay(int pos) {
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.set(getYearFromDatepicker(Global.tasks.get(pos).getEndDate()),
				getMonthFromDatepicker(Global.tasks.get(pos).getEndDate()) - 1,
				getDayFromDatepicker(Global.tasks.get(pos).getEndDate()));

		long end = dateEnd.getTimeInMillis();
		long todayTime = System.currentTimeMillis();

		int leftDay = (int) ((end - todayTime) / (1000 * 60 * 60 * 24));

		return leftDay;
	}
}
