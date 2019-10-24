package org.remain4life.androidversions.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

public abstract class BaseRecyclerView extends RecyclerView {
    public BaseRecyclerView(Context context) {
        super(context);

        setClipToPadding(false);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(createLayoutManager(context, attrs, defStyle));

        setClipToPadding(false);
    }

    public <T> void setData(List<T> data) {
        if (data != null) {
            //noinspection unchecked
            IListAdapter<T> adapter = (IListAdapter<T>) getAdapter();
            if (adapter != null) {
                adapter.setData(data);
            }
        }

    }

    protected abstract LayoutManager createLayoutManager(Context context, @Nullable AttributeSet attrs, int defStyle);
}
