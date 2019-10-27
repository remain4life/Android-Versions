package org.remain4life.androidversions.base;

import org.remain4life.androidversions.db.PlatformVersionEntity;

public interface IFavouritesObserver {
    void onUserDataChanged(PlatformVersionEntity entity);
}
