/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: generator
 */

package search.generator;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ToolBox {
	private static final int dayInMs = 86400000;

	public static long formatStringDateToLong(String stringDate) {
		String[] a = stringDate.split("-");
		int year = Integer.parseInt(a[0]);
		int month = Integer.parseInt(a[1]) - 1;
		int day = Integer.parseInt(a[2]);

		Calendar cal = new GregorianCalendar(year, month, day);
		return cal.getTime().getTime();
	}

	public static String formatLongDateToString(Long longDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date(longDate));
		List<String> yyyymmdd = new ArrayList<>();
		yyyymmdd.add("" + cal.YEAR);
		yyyymmdd.add("" + (cal.MONTH + 1));
		yyyymmdd.add("" + cal.DAY_OF_MONTH);
		String date = String.join("-", yyyymmdd);
		return date;
	}

	public static long formatToMidnight(long date) { return (date / dayInMs) * dayInMs; }
}
