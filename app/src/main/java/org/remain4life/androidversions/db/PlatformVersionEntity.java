package org.remain4life.androidversions.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import org.remain4life.androidversions.helpers.Helper;

import java.util.Date;

@Entity(tableName = "platform_versions")
public class PlatformVersionEntity implements Parcelable {
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

    @Ignore
    protected PlatformVersionEntity(Parcel in) {
        version = in.readString();
        name = in.readString();
        released = (Date)in.readSerializable();
        if (in.readByte() == 0) {
            api = null;
        } else {
            api = in.readInt();
        }
        if (in.readByte() == 0) {
            distribution = null;
        } else {
            distribution = in.readDouble();
        }
        byte tmpIsFavourite = in.readByte();
        isFavourite = tmpIsFavourite == 0 ? null : tmpIsFavourite == 1;
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeString(name);
        dest.writeSerializable(released);
        if (api == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(api);
        }
        if (distribution == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(distribution);
        }
        dest.writeByte((byte) (isFavourite == null ? 0 : isFavourite ? 1 : 2));
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlatformVersionEntity> CREATOR = new Creator<PlatformVersionEntity>() {
        @Override
        public PlatformVersionEntity createFromParcel(Parcel in) {
            return new PlatformVersionEntity(in);
        }

        @Override
        public PlatformVersionEntity[] newArray(int size) {
            return new PlatformVersionEntity[size];
        }
    };

    public String getReleasedString(){
        return Helper.getFormattedDate(released);
    }
}
