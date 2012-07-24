package sample.application.memopad;

import java.util.Calendar;

class Jp_String {
	static String[] week_name = {"“ú—j“ú","Œ—j“ú","‰Î—j“ú","…—j“ú","–Ø—j“ú","‹à—j“ú","“y—j“ú"};
	static String jpString () {
		Calendar calendar = Calendar.getInstance();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	
		String ts = new String( year + "”N" + month + "Œ" + day + "“ú" + week_name[week] + "\n" + hour + "" + minute + "•ª" + second + "•b");
		
		return ts;
	}
}
