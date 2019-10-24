package org.remain4life.androidversions.helpers;

import org.remain4life.androidversions.db.AppDatabase;

public class Application extends android.app.Application {

    private static Application app;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // init DB
        database = AppDatabase.getInstance(this);
    }

    public static Application getApplication(){
        return app;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
