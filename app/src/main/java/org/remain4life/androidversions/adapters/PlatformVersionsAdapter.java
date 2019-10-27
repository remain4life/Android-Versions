package org.remain4life.androidversions.adapters;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import org.remain4life.androidversions.BR;
import org.remain4life.androidversions.BuildConfig;
import org.remain4life.androidversions.ItemDetailActivity;
import org.remain4life.androidversions.ItemDetailFragment;
import org.remain4life.androidversions.ItemListActivity;
import org.remain4life.androidversions.R;
import org.remain4life.androidversions.base.IListAdapter;
import org.remain4life.androidversions.databinding.VersionItemBinding;
import org.remain4life.androidversions.db.DataRepository;
import org.remain4life.androidversions.db.PlatformVersionEntity;

import java.util.List;

import static org.remain4life.androidversions.helpers.Helper.APP_TAG;

public class PlatformVersionsAdapter
        extends RecyclerView.Adapter<PlatformVersionsAdapter.VersionViewHolder>
        implements IListAdapter<PlatformVersionEntity> {

    private ItemListActivity activity;
    private boolean twoPane;
    private List<PlatformVersionEntity> data;
    private final ObservableList.OnListChangedCallback<ObservableList<PlatformVersionEntity>> onListChangedCallback = new AdapterOnListChangedCallback<>(this);

    public PlatformVersionsAdapter(ItemListActivity activity, boolean twoPane) {
        this.activity = activity;
        this.twoPane = twoPane;
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
        versionViewHolder.setEntity(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class VersionViewHolder extends RecyclerView.ViewHolder implements Observable {
        private final VersionItemBinding versionItemBinding;
        private PlatformVersionEntity entity;
        private final PropertyChangeRegistry mCallbacks = new PropertyChangeRegistry();

        public VersionViewHolder(VersionItemBinding binding) {
            super(binding.getRoot());
            versionItemBinding = binding;
            versionItemBinding.setActivity(activity);
            versionItemBinding.setViewHolder(this);
        }

        /**
         * Called on item clicked to open details
         */
        public void onVersion() {
            if (BuildConfig.DEBUG) {
                Log.d(APP_TAG, "-> Clicked on " + entity.name);
            }

            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ItemDetailFragment.ARG_ENTITY, entity);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(activity, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ENTITY, entity);
                activity.startActivity(intent);
            }
        }

        public void onFavourite(){
            Toast.makeText(activity, "onFavourite clicked", Toast.LENGTH_LONG).show();
            entity.isFavourite = !entity.isFavourite;
            setEntity(entity);
            DataRepository.getInstance()
                    .updateFavourite(entity);
        }

        @Bindable
        public PlatformVersionEntity getEntity() {
            return entity;
        }

        public void setEntity(PlatformVersionEntity entity) {
            this.entity = entity;
            versionItemBinding.setEntity(entity);
            notifyPropertyChanged(BR.entity);
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

