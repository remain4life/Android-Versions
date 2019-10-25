package org.remain4life.androidversions.base;

import org.remain4life.androidversions.db.PlatformVersionEntity;

import java.util.List;

public interface IVersionItemsContainer {
    void setVersionItems(List<PlatformVersionEntity> entities);
    void onError(String errorMassage);
}
