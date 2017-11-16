package com.example.mariliavgama.medialist.data.source;

import android.support.annotation.NonNull;

import com.example.mariliavgama.medialist.data.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Concrete implementation to load items from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */

public class ItemsRepository implements ItemsDataSource {

    private static ItemsRepository INSTANCE = null;

    private final ItemsDataSource mItemsRemoteDataSource;

    private final ItemsDataSource mItemsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Item> mCachedItems;

    /**
     * Marks the cache as invalid to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ItemsRepository(@NonNull ItemsDataSource itemsRemoteDataSource,
                            @NonNull ItemsDataSource itemsLocalDataSource) {
        mItemsRemoteDataSource = itemsRemoteDataSource;
        mItemsLocalDataSource = itemsLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param itemsRemoteDataSource the backend data source
     * @param itemsLocalDataSource  the device storage data source
     * @return the {@link ItemsRepository} instance
     */
    public static ItemsRepository getInstance(ItemsDataSource itemsRemoteDataSource,
                                              ItemsDataSource itemsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ItemsRepository(itemsRemoteDataSource, itemsLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ItemsDataSource, ItemsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets items from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadItemsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getItems(@NonNull final LoadItemsCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCachedItems != null && !mCacheIsDirty) {
            callback.onItemsLoaded(new ArrayList<>(mCachedItems.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getItemsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mItemsLocalDataSource.getItems(new LoadItemsCallback() {
                @Override
                public void onItemsLoaded(List<Item> items) {
                    refreshCache(items);
                    callback.onItemsLoaded(new ArrayList<>(mCachedItems.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getItemsFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void refreshItems() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllItems() {
        mItemsRemoteDataSource.deleteAllItems();
        mItemsLocalDataSource.deleteAllItems();

        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }
        mCachedItems.clear();
    }

    private void getItemsFromRemoteDataSource(@NonNull final LoadItemsCallback callback) {
        mItemsRemoteDataSource.getItems(new LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> items) {
                refreshCache(items);
                refreshLocalDataSource(items);
                callback.onItemsLoaded(new ArrayList<>(mCachedItems.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Item> items) {
        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }
        mCachedItems.clear();
        for (Item item : items) {
            mCachedItems.put(item.getDisplayTitle(), item);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Item> items) {
        mItemsLocalDataSource.deleteAllItems();
        for (Item item : items) {
            mItemsLocalDataSource.saveItem(item);
        }
    }

    @Override
    public void saveItem(@NonNull Item item) {
        mItemsRemoteDataSource.saveItem(item);
        mItemsLocalDataSource.saveItem(item);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }
        mCachedItems.put(item.getDisplayTitle(), item);
    }
}