package org.remain4life.androidversions;

import android.content.DialogInterface;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.remain4life.androidversions.adapters.PlatformVersionsAdapter;
import org.remain4life.androidversions.base.BaseActivity;
import org.remain4life.androidversions.base.IFavouritesObserver;
import org.remain4life.androidversions.base.IVersionItemsContainer;
import org.remain4life.androidversions.databinding.ActivityItemListBinding;
import org.remain4life.androidversions.db.DataRepository;
import org.remain4life.androidversions.db.PlatformVersionEntity;

import java.util.List;

import static org.remain4life.androidversions.helpers.Helper.APP_TAG;
import static org.remain4life.androidversions.helpers.Helper.DB_TAG;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends BaseActivity<ActivityItemListBinding>  implements IVersionItemsContainer, IFavouritesObserver {

    // activity two-pane mode flag, i.e. running on a tablet device or not
    private boolean twoPane;

    // items from DB to display
    private List<PlatformVersionEntity> versionItems;

    PlatformVersionsAdapter adapter;

    // repository subject to observe
    DataRepository repo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) {
            // detail container view will be present only in the
            // large-screen layouts (res/values-w900dp)
            twoPane = true;
        }

        setupRecyclerView();
        repo = DataRepository.getInstance();
        // observe favourite changes
        repo.registerObserver(this);

        // load
        repo.loadVersionsFromDB(DataRepository.Filter.ALL, this);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = twoPane ?
                binding.containerTablet.itemListTablet : binding.container.itemList;
        adapter = new PlatformVersionsAdapter(this, twoPane);
        recyclerView.setAdapter(adapter);
        notifyPropertyChanged(BR.items);
    }

    @Override
    public int getVariable() {
        return BR.activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_item_list;
    }

    @Bindable
    public List<PlatformVersionEntity> getVersionItems() {
        return versionItems;
    }

    @Override
    public void setVersionItems(List<PlatformVersionEntity> versionItems) {
        this.versionItems = versionItems;
        notifyPropertyChanged(BR.activity);
        notifyPropertyChanged(BR.items);
        adapter.setData(versionItems);
        adapter.notifyDataSetChanged();
        binding.executePendingBindings();
    }

    /**
     * Shows delete item dialog
     *
     * @param deleteListener listener for yes action
     * @param entity PlatformVersionEntity to display info about
     */
    public void showDeleteDialog(DialogInterface.OnClickListener deleteListener, PlatformVersionEntity entity) {
        showDialog(
                String.format(getString(R.string.dialog_delete_title), entity.version, entity.name),
                getString(R.string.dialog_delete_message),
                deleteListener,
                null);
    }

    /**
     * Creates and adds fragment with detail entity info
     *
     * @param entity PlatformVersionEntity to display details
     */
    public void addEntityFragment(PlatformVersionEntity entity) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ItemDetailFragment.ARG_ENTITY, entity);
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        // tag is unique field of entity
        String fragmentTag = entity.version;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment, fragmentTag)
                .commit();
    }

    /**
     * Removes details fragment of deleted entity
     *
     * @param entity deleted PlatformVersionEntity
     */
    public void removeEntityFragment(PlatformVersionEntity entity) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(entity.version);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    public void onUserDataChanged(PlatformVersionEntity entity) {
        if (BuildConfig.DEBUG) {
            Log.d(DB_TAG, "ItemListActivity -> onUserDataChanged called: " + entity.version + ", "
                    + entity.name + ", favourite - " + entity.isFavourite);
        }
        for (PlatformVersionEntity item : versionItems) {
            if (item.version.equals(entity.version)) {
                item.isFavourite = entity.isFavourite;
            }
        }
        setVersionItems(versionItems);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repo.removeObserver(this);
    }
}
