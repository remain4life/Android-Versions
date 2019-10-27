package org.remain4life.androidversions;

import android.app.Activity;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;

import org.remain4life.androidversions.base.BaseFragment;
import org.remain4life.androidversions.base.IFavouritesObserver;
import org.remain4life.androidversions.databinding.ItemDetailBinding;
import org.remain4life.androidversions.db.DataRepository;
import org.remain4life.androidversions.db.PlatformVersionEntity;

import static org.remain4life.androidversions.helpers.Helper.APP_TAG;
import static org.remain4life.androidversions.helpers.Helper.DB_TAG;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends BaseFragment<ItemDetailBinding> implements IFavouritesObserver {
    // fragment argument representing Parcelable DB entity
    public static final String ARG_ENTITY = "entity";

    // content to present
    private PlatformVersionEntity entity;

    // repository subject to observe
    DataRepository repo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_ENTITY)) {
            entity = args.getParcelable(ARG_ENTITY);
            setTitle();
        }

        repo = DataRepository.getInstance();
        // observe favourite changes
        repo.registerObserver(this);
    }



    private void setTitle() {
        Activity activity = this.getActivity();
        if (activity != null) {
            Log.d(APP_TAG, "Activity is not null");
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                Log.d(APP_TAG, "appBarLayout is not null");
                appBarLayout.setTitle(entity.name);
            } else {
                // if device was rotated to landscape with this fragment
                activity.setTitle(entity.name);
            }
        }
    }

    @Override
    public int getVariable() {
        return BR.fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_detail;
    }

    @Bindable
    public PlatformVersionEntity getEntity() {
        return entity;
    }

    /**
     * Changes favourite flag, caches it to DB
     */
    public void onFavourite(){
        //Toast.makeText(getActivity(), "onFavourite clicked", Toast.LENGTH_LONG).show();
        entity.isFavourite = !entity.isFavourite;
        setEntity(entity);
        DataRepository.getInstance()
                .updateFavourite(entity);

    }

    public void setEntity(PlatformVersionEntity entity) {
        this.entity = entity;
        notifyPropertyChanged(BR.entity);
        binding.executePendingBindings();
    }

    @Override
    public void onUserDataChanged(PlatformVersionEntity entity) {
        if (BuildConfig.DEBUG) {
            Log.d(DB_TAG, "ItemDetailFragment -> onUserDataChanged called: " + entity.version + ", "
                    + entity.name + ", favourite - " + entity.isFavourite);
        }
        setEntity(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repo.removeObserver(this);
    }
}
