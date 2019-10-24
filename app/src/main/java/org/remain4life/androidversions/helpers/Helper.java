package org.remain4life.androidversions.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class
 */
public final class Helper {
    private Helper() {}

    // tags for logging
    public static final String DB_TAG = "dbTag";
    public static final String ERROR_TAG = "errorTag";
    public static final String APP_TAG = "appTag";

    // release date pattern
    private static final String DATE_FORMAT = "MMMM d, y";


    /**
     * Converts Date to String representation using specified format
     *
     * @param date Date to convert
     * @return String representation using specified format
     */
    public static String getFormattedDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(date);
    }

    /**
     * Converts String of specified format to Date representation
     *
     * @param dateString String of specified format to convert
     * @return Date object
     */
    public static Date getDateFromString (String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(ERROR_TAG, "ParseException in getDateFromString: " + e.getLocalizedMessage());
        }
        return null;
    }
}
