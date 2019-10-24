package org.remain4life.androidversions.db;

import android.annotation.SuppressLint;
import android.util.Log;

import org.remain4life.androidversions.BuildConfig;
import org.remain4life.androidversions.ItemListActivity;
import org.remain4life.androidversions.R;
import org.remain4life.androidversions.helpers.Application;
import org.remain4life.androidversions.helpers.Helper;
import org.remain4life.androidversions.helpers.rx.RxAndroidPlugins;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.remain4life.androidversions.helpers.Helper.DB_TAG;

public class DataRepository {
    private static DataRepository instance;

    private AppDatabase db;

    private DataRepository(AppDatabase db) {
        this.db = db;
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository(Application.getApplication().getDatabase());
        }
        return instance;
    }

    public enum Filter {
        ALL,
        FAVOURITE,
        LOW_DISTRIBUTION
    }


    @SuppressLint("CheckResult")
    void populateDb() {
        // create list with records for DB
        List<PlatformVersionEntity> entityList = createPlatformVersionsList();

        Observable.fromCallable(() ->
                db.entitiesDao().insert(entityList)
        ).subscribeOn(Schedulers.io())
                .observeOn(RxAndroidPlugins.onMainThreadScheduler())
                .subscribe(insertedNumber -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "Records inserted: " + insertedNumber.length);
                            }
                        }
                );
    }

    /**
     * Creates list with data about Android platform versions.
     * Sources:
     * <a href="https://developer.android.com/about/dashboards">https://developer.android.com/about/dashboards</a>
     * <a href="https://en.wikipedia.org/wiki/Android_version_history">https://en.wikipedia.org/wiki/Android_version_history</a>
     *
     * @return List<PlatformVersionEntity> to insert
     */
    private List<PlatformVersionEntity> createPlatformVersionsList() {
        List<PlatformVersionEntity> entities = new ArrayList<>();
        entities.add(new PlatformVersionEntity(
                "9", "Pie", Helper.getDateFromString("August 6, 2018"),
                28, 10.4, false,
                Application.getApplication().getString(R.string.android_pie_desc)
        ));
        entities.add(new PlatformVersionEntity(
                "8.1", "Oreo", Helper.getDateFromString("December 5, 2017"),
                27, 15.4, false,
                Application.getApplication().getString(R.string.android_oreo_desc_update)
        ));

        entities.add(new PlatformVersionEntity(
                "8.0", "Oreo", Helper.getDateFromString("August 21, 2017"),
                26, 12.9, false,
                Application.getApplication().getString(R.string.android_oreo_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "7.1", "Nougat", Helper.getDateFromString("October 4, 2016"),
                25, 7.8, false,
                Application.getApplication().getString(R.string.android_nougat_desc_update)
        ));

        entities.add(new PlatformVersionEntity(
                "7.0", "Nougat", Helper.getDateFromString("August 22, 2016"),
                24, 11.4, false,
                Application.getApplication().getString(R.string.android_nougat_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "6.0", "Marshmallow", Helper.getDateFromString("October 5, 2015"),
                23, 16.9, false,
                Application.getApplication().getString(R.string.android_marshmallow_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "5.1", "Lollipop", Helper.getDateFromString("March 9, 2015"),
                22, 11.5, false,
                Application.getApplication().getString(R.string.android_lollipop_desc_update)
        ));

        entities.add(new PlatformVersionEntity(
                "5.0", "Lollipop", Helper.getDateFromString("November 12, 2014"),
                21, 3.0, false,
                Application.getApplication().getString(R.string.android_lollipop_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "4.4", "KitKat", Helper.getDateFromString("October 31, 2013"),
                19, 6.9, false,
                Application.getApplication().getString(R.string.android_kitkat_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "4.3", "Jelly Bean", Helper.getDateFromString("July 24, 2013"),
                18, 0.5, false,
                Application.getApplication().getString(R.string.android_jellybean_desc_update_2)
        ));

        entities.add(new PlatformVersionEntity(
                "4.2.x", "Jelly Bean", Helper.getDateFromString("November 13, 2012"),
                17, 1.5, false,
                Application.getApplication().getString(R.string.android_jellybean_desc_update)
        ));

        entities.add(new PlatformVersionEntity(
                "4.1.x", "Jelly Bean", Helper.getDateFromString("July 9, 2012"),
                16, 1.2, false,
                Application.getApplication().getString(R.string.android_jellybean_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "4.0.3 - 4.0.4", "Ice Cream Sandwich", Helper.getDateFromString("December 16, 2011"),
                15, 0.3, false,
                Application.getApplication().getString(R.string.android_sandwich_desc)
        ));

        entities.add(new PlatformVersionEntity(
                "2.3.3 - 2.3.7", "Gingerbread", Helper.getDateFromString("February 9, 2011"),
                10, 0.3, false,
                Application.getApplication().getString(R.string.android_gingerbread_desc)
        ));

        return entities;
    }


    /**
     * Loads Android platform version list
     *
     * @param filter enum value to choose what to load
     * @param activity ItemListActivity to load items
     */
    @SuppressLint("CheckResult")
    public void loadVersionsFromDB(Filter filter, ItemListActivity activity) {

        Single<List<PlatformVersionEntity>> load;
        switch (filter) {
            case ALL:
            default:
                load = db.entitiesDao().loadAllVersions();
                break;
            case FAVOURITE:
                load = db.entitiesDao().loadFavourites();
                break;
            case LOW_DISTRIBUTION:
                load = db.entitiesDao().loadLowDistributionVersions();
                break;

        }

        load
                .subscribeOn(Schedulers.io())
                .observeOn(RxAndroidPlugins.onMainThreadScheduler())
                .subscribe(entities ->  {

                            activity.setVersionItems(entities);

                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> " + entities.size() + " entities successfully loaded from DB");
                            }
                        },
                        throwable -> activity.onError(throwable.toString())
                );
    }
}
