package com.floridakeys.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description     DateTime Util
 *
 * @author          Adrian
 */
public class DateTimeUtil {
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }

    /**
     * Make Time String
     *
     * @param timeString
     * @return
     */
    public static String makeTime(String timeString) {
        String time = "00:00";

        try {
            int timeInt = Integer.parseInt(timeString);
            time = String.format("%02d:%02d", timeInt / 60, timeInt % 60);
        } catch (Exception e) {}

        return time;
    }

    /**
     * Convert Date String
     * ex: 2015-11-15 -> NOV 15
     *
     * @param dateString
     * @return
     */
    public static String convertDate(String dateString, boolean includeYear) {
        String dateStr = dateString;

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(dateString);

            if (date != null) {
                String month = makeStringForMonth(date.getMonth() + 1, false);
                if (includeYear)
                    dateStr = String.format("%s %02d %04d", month, date.getDate(), date.getYear() + 1900);
                else
                    dateStr = String.format("%s %02d", month, date.getDate());
            }
        } catch (Exception e) {}

        return dateStr;
    }

    /**
     * Convert Year/Month String
     * ex: 2015-11-15 -> November 2015
     *
     * @param dateString
     * @return
     */
    public static String convertYearMonthString(String dateString, boolean includeYear) {
        String dateStr = dateString;

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(dateString);

            if (date != null) {
                String month = makeStringForMonth(date.getMonth() + 1, true);
                if (includeYear)
                    dateStr = String.format("%s %04d", month, date.getYear() + 1900);
                else
                    dateStr = String.format("%s", month);
            }
        } catch (Exception e) {}

        return dateStr;
    }

    /**
     * Convert Time String
     * ex: 15:30:00 -> 03:30 PM
     *
     * @param timeString
     * @return
     */
    public static String convertTime(String timeString, boolean includeSecond) {
        String timeStr = timeString;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            Date date = dateFormat.parse(timeStr);
            int hour = (date.getHours() > 12) ? date.getHours() - 12 : date.getHours();
            String pm = (date.getHours() > 12) ? "PM" : "AM";

            if (includeSecond)
                timeStr = String.format("%02d:%02d:%02d %s", hour, date.getMinutes(), date.getSeconds(), pm);
            else
                timeStr = String.format("%02d:%02d %s", hour, date.getMinutes(), pm);

        } catch (ParseException e) {
            int a;
            a = 1;
        }

        return timeStr;
    }

    public static String makeStringForMonth(int month, boolean full) {
        String monthString = "";

        switch (month) {
            case 1: monthString = (full) ? "January" : "Jan"; break;
            case 2: monthString = (full) ? "February" : "Feb"; break;
            case 3: monthString = (full) ? "March" : "Mar"; break;
            case 4: monthString = (full) ? "April" : "Apr"; break;
            case 5: monthString = (full) ? "May" : "May"; break;
            case 6: monthString = (full) ? "June" : "June"; break;
            case 7: monthString = (full) ? "July" : "July"; break;
            case 8: monthString = (full) ? "August" : "Aug"; break;
            case 9: monthString = (full) ? "September" : "Sept"; break;
            case 10: monthString = (full) ? "October" : "Oct"; break;
            case 11: monthString = (full) ? "November" : "Nov"; break;
            case 12: monthString = (full) ? "December" : "Dec"; break;
        }
        return monthString;
    }

    /**
     * Increase one month
     *
     * @param dateString
     * @return
     */
    public static String increaseOneMonth(String dateString)
    {
        String newDateStr = dateString;

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)df.parse(dateString);

            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, 1);
                newDateStr = df.format(cal.getTime());
            }
        } catch (Exception e) {}

        return newDateStr;
    }

    /**
     * Decrease one month
     *
     * @param dateString
     * @return
     */
    public static String decreaseOneMonth(String dateString)
    {
        String newDateStr = dateString;

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)df.parse(dateString);

            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, -1);
                newDateStr = df.format(cal.getTime());
            }
        } catch (Exception e) {}

        return newDateStr;
    }
}
