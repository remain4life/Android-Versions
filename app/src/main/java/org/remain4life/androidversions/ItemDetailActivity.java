package org.remain4life.androidversions;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.remain4life.androidversions.base.BaseActivity;
import org.remain4life.androidversions.databinding.ActivityItemDetailBinding;
import org.remain4life.androidversions.db.PlatformVersionEntity;

import static org.remain4life.androidversions.ItemDetailFragment.ARG_ENTITY;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends BaseActivity<ActivityItemDetailBinding> {

    private PlatformVersionEntity entity;

    public static Intent createIntent(Context context, PlatformVersionEntity entity) {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ENTITY, entity);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(binding.detailToolbar);
        setTitle(getTitle());

        // show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // loading entity from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(ARG_ENTITY)) {
            entity = extras.getParcelable(ARG_ENTITY);
        }

        // savedInstanceState is non-null when rotating the screen,
        // the fragment will automatically be re-added in this case
        if (savedInstanceState == null) {
            // create the detail fragment and add it to the activity
            ItemDetailFragment fragment = new ItemDetailFragment();
            if (entity != null) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ARG_ENTITY, entity);
                fragment.setArguments(arguments);
            }
            setContentFragment(fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Up button
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getVariable() {
        return BR.activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_item_detail;
    }

    public void setContentFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.item_detail_container, fragment);
        transaction.commit();
    }

    @Bindable
    public PlatformVersionEntity getEntity() {
        return entity;
    }
}
