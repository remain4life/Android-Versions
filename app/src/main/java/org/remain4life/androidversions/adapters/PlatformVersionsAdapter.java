package org.remain4life.androidversions.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.remain4life.androidversions.BuildConfig;
import org.remain4life.androidversions.ItemListActivity;
import org.remain4life.androidversions.base.IListAdapter;
import org.remain4life.androidversions.databinding.VersionItemBinding;
import org.remain4life.androidversions.db.PlatformVersionEntity;

import java.util.List;

import org.remain4life.androidversions.BR;

import static org.remain4life.androidversions.helpers.Helper.APP_TAG;

public class PlatformVersionsAdapter
        extends RecyclerView.Adapter<PlatformVersionsAdapter.VersionViewHolder>
        implements IListAdapter<PlatformVersionEntity> {

    private ItemListActivity activity;
    private List<PlatformVersionEntity> data;
    private final ObservableList.OnListChangedCallback<ObservableList<PlatformVersionEntity>> onListChangedCallback = new AdapterOnListChangedCallback<>(this);

    public PlatformVersionsAdapter(ItemListActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public VersionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        VersionItemBinding binding = VersionItemBinding.inflate(
                layoutInflater, viewGroup, false);
        return new VersionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionViewHolder versionViewHolder, int i) {
        versionViewHolder.setItem(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class VersionViewHolder extends RecyclerView.ViewHolder implements Observable {
        private final VersionItemBinding versionItemBinding;
        private PlatformVersionEntity item;
        private final PropertyChangeRegistry mCallbacks = new PropertyChangeRegistry();

        public VersionViewHolder(VersionItemBinding binding) {
            super(binding.getRoot());
            versionItemBinding = binding;
            versionItemBinding.setActivity(activity);
        }

        /**
         * Called on image clicked to open activity with regular image size
         */
        @SuppressLint("CheckResult")
        public void onImage() {
            if (BuildConfig.DEBUG) {
                Log.d(APP_TAG, "-> Clicked on  " + item.name);
            }



        }

        @Bindable
        public PlatformVersionEntity getItem() {
            return item;
        }

        public void setItem(PlatformVersionEntity item) {
            this.item = item;
            versionItemBinding.setEntity(item);
            notifyPropertyChanged(BR.item);
            versionItemBinding.executePendingBindings();
        }

        // observable implementation
        @Override
        public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
            mCallbacks.add(callback);
        }

        @Override
        public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
            mCallbacks.remove(callback);
        }

        public void notifyChange() {
            mCallbacks.notifyCallbacks(this, 0, null);
        }

        public void notifyPropertyChanged(int fieldId) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }

    @Override
    public void setData(List<PlatformVersionEntity> data) {
        if (BuildConfig.DEBUG) {
            Log.d(APP_TAG, "Set data to adapter: " + data);
        }

        if (this.data == data) {
            return;
        }

        if (this.data instanceof ObservableList) {
            ((ObservableList<PlatformVersionEntity>) this.data).removeOnListChangedCallback(onListChangedCallback);
        }
        this.data = data;
        if (this.data instanceof ObservableList) {
            ((ObservableList<PlatformVersionEntity>) this.data).addOnListChangedCallback(onListChangedCallback);
        }
        notifyDataSetChanged();
    }
}

