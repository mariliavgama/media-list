package com.example.mariliavgama.medialist.data.source.local;

import android.support.annotation.NonNull;

import com.example.mariliavgama.medialist.data.Item;
import com.example.mariliavgama.medialist.data.source.ItemsDataSource;
import com.example.mariliavgama.medialist.util.AppExecutors;

import java.util.List;

/**
 * Concrete implementation of a data source as a db.
 */
public class ItemsLocalDataSource implements ItemsDataSource {

    private static volatile ItemsLocalDataSource INSTANCE;

    private ItemsDao mItemsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private ItemsLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull ItemsDao itemsDao) {
        mAppExecutors = appExecutors;
        mItemsDao = itemsDao;
    }

    public static ItemsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull ItemsDao itemsDao) {
        if (INSTANCE == null) {
            synchronized (ItemsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemsLocalDataSource(appExecutors, itemsDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadItemsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getItems(@NonNull final LoadItemsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Item> items = mItemsDao.getItems();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (items == null || items.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onItemsLoaded(items);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveItem(@NonNull final Item item) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mItemsDao.insertItem(item);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshItems() {
        // Not required because the {@link ItemsRepository} handles the logic of refreshing the
        // items from all the available data sources.
    }

    @Override
    public void deleteAllItems() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mItemsDao.deleteItems();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}