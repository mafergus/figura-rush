package com.addressunknowngames.shapeninja.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by matthewferguson on 2/23/18.
 */

public class TimestampUtils {

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date
     *            Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    /**
     * Private constructor: class cannot be instantiated
     */
    private TimestampUtils() {
    }

    public static String timeToString(long totalRunTimeMs) {
        int hours = (int) (totalRunTimeMs / (1000 * 60 * 60));
        totalRunTimeMs -= (hours * 1000 * 60 * 60);
        int minutes = (int) (totalRunTimeMs / (1000 * 60));
        totalRunTimeMs -= (minutes * 1000 * 60);
        int seconds = (int) (totalRunTimeMs / 1000);
        totalRunTimeMs -= (seconds * 1000);
        int miliseconds = (int) (totalRunTimeMs);

        String hoursStr = "" + (hours == 0 ? "" : hours);
        String minutesStr = "" + (minutes == 0 ? "" : String.format("%02d", minutes));
        String secondsStr = String.format("%02d", seconds);
        String milisecondsStr = String.format("%2d", miliseconds / 10);

        String text = hoursStr + (hours != 0 ? ":" : "") + minutesStr + (minutes != 0 ? ":" : "") + secondsStr + "." + milisecondsStr;

        return text;
    }
}