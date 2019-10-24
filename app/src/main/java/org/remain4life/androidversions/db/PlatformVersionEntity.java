package org.remain4life.androidversions.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "platform_versions")
public class PlatformVersionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String version;
    public String name;
    public Date released;
    public Integer api;
    public Double distribution;
    public Boolean isFavourite;
    public String description;

    public PlatformVersionEntity(int id, String version, String name, Date released, Integer api, Double distribution, Boolean isFavourite, String description) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.released = released;
        this.api = api;
        this.distribution = distribution;
        this.isFavourite = isFavourite;
        this.description = description;
    }

    @Ignore
    public PlatformVersionEntity(String version, String name, Date released, Integer api, Double distribution, Boolean isFavourite, String description) {
        this.version = version;
        this.name = name;
        this.released = released;
        this.api = api;
        this.distribution = distribution;
        this.isFavourite = isFavourite;
        this.description = description;
    }
}
