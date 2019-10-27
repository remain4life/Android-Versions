package org.remain4life.androidversions.base;

import org.remain4life.androidversions.db.PlatformVersionEntity;

public interface IEntitySubject {
    void registerObserver(IFavouritesObserver repositoryObserver);
    void removeObserver(IFavouritesObserver repositoryObserver);
    void notifyObservers(PlatformVersionEntity entity);
}
