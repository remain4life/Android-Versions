package org.remain4life.androidversions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.remain4life.androidversions.base.BaseRecyclerView;

public class LinearRecyclerView extends BaseRecyclerView {

    public LinearRecyclerView(Context context) {
        super(context);
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        return new LinearLayoutManager(context, VERTICAL, false);
    }
}
