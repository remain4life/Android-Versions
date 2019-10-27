package org.remain4life.androidversions.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface EntitiesDao {
    // get all records
    @Query("SELECT * FROM platform_versions ORDER BY id")
    Single<List<PlatformVersionEntity>> loadAllVersions();

    // load records from DB for Favourites filter
    @Query("SELECT * FROM platform_versions WHERE isFavourite = 1 ORDER BY id")
    Single<List<PlatformVersionEntity>> loadFavourites();

    // load records from DB with <3% filter
    @Query("SELECT * FROM platform_versions WHERE distribution < 3 ORDER BY id")
    Single<List<PlatformVersionEntity>> loadLowDistributionVersions();

    // select one record by version
    @Query("SELECT * FROM platform_versions WHERE version = :version LIMIT 1")
    Single<PlatformVersionEntity> loadVersionById(String version);

    // update record as favourite or not by version
    @Query("UPDATE platform_versions SET isFavourite = :isFavourite WHERE version = :version")
    int setVersionFavourite(String version, boolean isFavourite);

    // insert new PlatformVersionEntities with replace strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(List<PlatformVersionEntity> entities);

    // to know is DB empty
    @Query("SELECT COUNT (*) FROM platform_versions")
    int rowsCount();

    // delete row from DB by version
    @Query("DELETE FROM platform_versions WHERE version = :version")
    int deleteVersion(String version);
}
