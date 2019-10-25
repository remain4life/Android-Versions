package org.remain4life.androidversions;

import android.app.Activity;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import org.remain4life.androidversions.base.BaseFragment;
import org.remain4life.androidversions.databinding.ItemDetailBinding;
import org.remain4life.androidversions.db.PlatformVersionEntity;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends BaseFragment<ItemDetailBinding> {
    // fragment argument representing Parcelable DB entity
    public static final String ARG_ENTITY = "entity";

    // content to present
    private PlatformVersionEntity entity;

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
    }

    private void setTitle() {
        Activity activity = this.getActivity();
        if (activity != null) {
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(entity.name);
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
}
