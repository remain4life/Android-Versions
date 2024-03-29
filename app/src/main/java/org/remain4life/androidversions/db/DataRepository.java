package org.remain4life.androidversions.db;

import android.annotation.SuppressLint;
import android.support.annotation.StringRes;
import android.util.Log;

import org.remain4life.androidversions.BuildConfig;
import org.remain4life.androidversions.R;
import org.remain4life.androidversions.base.IFavouritesObserver;
import org.remain4life.androidversions.base.IEntitySubject;
import org.remain4life.androidversions.base.IVersionItemsContainer;
import org.remain4life.androidversions.helpers.Application;
import org.remain4life.androidversions.helpers.Helper;
import org.remain4life.androidversions.helpers.rx.RxAndroidPlugins;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.remain4life.androidversions.helpers.Helper.DB_TAG;

public class DataRepository implements IEntitySubject {
    private static DataRepository instance;

    private AppDatabase db;

    // observers to get favourite updates
    private List<IFavouritesObserver> favouritesObservers;

    private DataRepository(AppDatabase db) {
        this.db = db;
        favouritesObservers = new ArrayList<>();
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository(Application.getApplication().getDatabase());
        }
        return instance;
    }

    public enum Filter {
        ALL(R.string.menu_all),
        FAVOURITE(R.string.menu_favourites),
        LOW_DISTRIBUTION(R.string.menu_distribution);

        @StringRes
        public final int source;

        Filter(@StringRes int source) {
            this.source = source;
        }
    }


    @SuppressLint("CheckResult")
    private Observable<Long[]> populateDb() {
        // create list with records for DB
        List<PlatformVersionEntity> entityList = createPlatformVersionsList();

        return Observable.fromCallable(() ->
                db.entitiesDao().insert(entityList)
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
     * @param filter    enum value to choose what to load
     * @param container IVersionItemsContainer implementation to load items
     */
    @SuppressLint("CheckResult")
    public void loadVersionsFromDB(Filter filter, IVersionItemsContainer container) {
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

        // check is DB empty
        Observable.fromCallable(() ->
                db.entitiesDao().rowsCount()
        ).subscribeOn(Schedulers.io())
                .map(count -> {
                    if (BuildConfig.DEBUG) {
                        Log.d(DB_TAG, "-> DB rows count: " +  count);
                    }
                    if (count == 0) {
                        // fill DB
                        if (BuildConfig.DEBUG) {
                            Log.d(DB_TAG, "-> DB is empty, populating... ");
                        }
                        return populateDb()
                                .ignoreElements()
                                .andThen(load);
                    }
                    return load;
                })
                .subscribe(listSingle -> listSingle
                                .observeOn(RxAndroidPlugins.onMainThreadScheduler())
                                .subscribe(entities -> {
                                            container.setVersionItems(entities);

                                            if (BuildConfig.DEBUG) {
                                                Log.d(DB_TAG, "-> " +  entities.size() + " entities successfully loaded from DB");
                                            }
                                        },
                                        throwable -> container.onError(throwable.toString())
                                ),

                        throwable -> container.onError(throwable.toString())
                );
    }

    /**
     * Updates favourites flag in DB
     *
     * @param entity PlatformVersionEntity with changed favourites flag
     */
    @SuppressLint("CheckResult")
    public void updateFavourite(PlatformVersionEntity entity) {
        Observable.just(db)
                .subscribeOn(Schedulers.io())
                .map(db -> db.entitiesDao()
                        .setVersionFavourite(entity.version, entity.isFavourite))
                .observeOn(RxAndroidPlugins.onMainThreadScheduler())
                .subscribe(result -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> Entity " + entity.version + ", "
                                        + entity.name + " favourite cached: " + entity.isFavourite);
                            }
                            notifyObservers(entity);
                        },
                        throwable -> Log.e(Helper.ERROR_TAG, throwable.toString())
                );
    }

    /**
     * Deletes needed entity from DB
     *
     * @param entity PlatformVersionEntity we want to delete
     */
    @SuppressLint("CheckResult")
    public void deleteEntity(PlatformVersionEntity entity) {
        Observable.just(db)
                .subscribeOn(Schedulers.io())
                .map(db -> db.entitiesDao()
                        .deleteVersion(entity.version))
                .observeOn(RxAndroidPlugins.onMainThreadScheduler())
                .subscribe(result -> {
                            if (BuildConfig.DEBUG) {
                                Log.d(DB_TAG, "-> Deleted " + result + ", "
                                        + entity.name);
                            }
                        },
                        throwable -> Log.e(Helper.ERROR_TAG, throwable.toString())
                );
    }


    @Override
    public void registerObserver(IFavouritesObserver favouritesObserver) {
        if(!favouritesObservers.contains(favouritesObserver)) {
            favouritesObservers.add(favouritesObserver);
        }
    }

    @Override
    public void removeObserver(IFavouritesObserver repositoryObserver) {
        favouritesObservers.remove(repositoryObserver);
    }

    @Override
    public void notifyObservers(PlatformVersionEntity entity) {
        for (IFavouritesObserver observer: favouritesObservers) {
            observer.onUserDataChanged(entity);
        }
    }
}
