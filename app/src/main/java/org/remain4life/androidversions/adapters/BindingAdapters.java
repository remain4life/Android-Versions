package org.remain4life.androidversions.adapters;

import android.databinding.BindingAdapter;
import android.view.View;

import org.remain4life.androidversions.base.BaseRecyclerView;

import java.util.List;

public class BindingAdapters {
    private BindingAdapters() { }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("android:onClick")
    public static void setOnClickListener(View view, Runnable listener) {
        view.setOnClickListener(v -> listener.run());
    }

    @BindingAdapter("data")
    public static <T> void setAdapterData(BaseRecyclerView recyclerView, List<T> data) {
        recyclerView.setData(data);
    }


}
