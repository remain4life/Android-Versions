package org.remain4life.androidversions.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import org.remain4life.androidversions.db.DataRepository;

/*
 * Class to cache user filter
 */
public class PreferencesCache {
    private static final String PREFERENCES_NAME = "cache";
    private static final String FILTER = "filter";

    private static SharedPreferences sharedPreferences() {
        return Application.getApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor sharedPreferencesEditor() {
        return sharedPreferences().edit();
    }

    public static void setFilter(DataRepository.Filter filter) {
        sharedPreferencesEditor().putInt(FILTER, filter.ordinal()).apply();
    }

    public static DataRepository.Filter getFilter() {
        int ordinal = sharedPreferences().getInt(FILTER, DataRepository.Filter.ALL.ordinal());
        return DataRepository.Filter.values()[ordinal];
    }
}
