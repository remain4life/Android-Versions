package org.remain4life.androidversions.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.remain4life.androidversions.BuildConfig;
import org.remain4life.androidversions.db.converters.DateConverter;

import static org.remain4life.androidversions.helpers.Helper.DB_TAG;

@Database(entities = PlatformVersionEntity.class,
        version = 1,
        exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "androidVersionsDb";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (BuildConfig.DEBUG) {
                    Log.d(DB_TAG, "Creating new database instance");
                }
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        //only for TEST
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d(DB_TAG, "Getting the database instance");
        }
        return instance;
    }

    //return all methods from DAO interface
    public abstract EntitiesDao entitiesDao();
}

