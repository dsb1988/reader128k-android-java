package net.reader128k.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
    public static String convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm");
            Date postDate = format.parse(date);
            Calendar postCalendar = new GregorianCalendar();
            postCalendar.setTime(postDate);

            Calendar nowCalendar = new GregorianCalendar();
            if (nowCalendar.get(Calendar.YEAR) == postCalendar.get(Calendar.YEAR))
                if (nowCalendar.get(Calendar.MONTH) == postCalendar.get(Calendar.MONTH)) {
                    int nowWeek = nowCalendar.get(Calendar.WEEK_OF_MONTH);
                    int postWeek = postCalendar.get(Calendar.WEEK_OF_MONTH);
                    if (nowWeek == postWeek) {
                        int postHour = postCalendar.get(Calendar.HOUR_OF_DAY);
                        int postMinute = postCalendar.get(Calendar.MINUTE);
                        int nowDay = nowCalendar.get(Calendar.DAY_OF_WEEK);
                        int postDay = postCalendar.get(Calendar.DAY_OF_WEEK);
                        if (nowDay == postDay) {
                            int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
                            if (nowHour == postHour) {
                                int nowMinute = nowCalendar.get(Calendar.MINUTE);
                                if (nowMinute == postMinute)
                                    return DateHelper.formatSeconds(nowCalendar.get(Calendar.SECOND) - postCalendar.get(Calendar.SECOND));
                                else
                                    return DateHelper.formatMinutes(nowMinute - postMinute);
                            }
                            else {
                                if (nowHour - postHour < 12)
                                    return DateHelper.formatHours(nowHour - postHour);
                            }
                        }
                        else if (nowDay - postDay == 1)
                            return "Yesterday at " + String.format("%02d:%02d", postHour, postMinute);
                        else
                            return DateHelper.formatDays(nowDay - postDay, postHour, postMinute);
                    }
                }
        } catch (ParseException e) {
            return date;
        }

        return date;
    }

    public static String formatSeconds(int value) {
        return value % 10 == 1 ? String.format("%d second ago", value) : String.format("%d seconds ago", value);
    }

    public static String formatMinutes(int value) {
        return value % 10 == 1 ? String.format("%d minute ago", value) : String.format("%d minutes ago", value);
    }

    public static String formatHours(int value) {
        return value % 10 == 1 ? String.format("%d hour ago", value) : String.format("%d hours ago", value);
    }

    public static String formatDays(int value, int hour, int minute) {
        return (value % 10 == 1 ? value + " day ago at " : value  + " days ago at ") + String.format("%02d:%02d", hour, minute);
    }
}
