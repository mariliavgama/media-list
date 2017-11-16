package com.example.mariliavgama.medialist.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mariliavgama.medialist.data.Item;
import com.example.mariliavgama.medialist.data.source.ItemsDataSource;
import com.example.mariliavgama.medialist.data.source.ItemsRepository;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Architecture style based on: https://github.com/googlesamples/android-architecture
 * This is a presenter which implements the presenter interface in the corresponding contract.
 * A presenter typically hosts business logic associated with a particular feature, and the
 * corresponding view handles the Android UI work. The view contains almost no logic; it converts
 * the presenter's commands to UI actions, and listens for user actions, which are then passed to
 * the presenter.
 */

public class ItemsPresenter implements ItemsContract.Presenter {

    private final ItemsRepository mItemsRepository;
    @Nullable
    private ItemsContract.View mItemsView;
    static WeakReference<ItemsPresenter> wrPresenter;

    ItemsPresenter(@NonNull ItemsRepository itemsRepository, @NonNull ItemsContract.View itemsView) {
        mItemsRepository = itemsRepository;
        mItemsView = itemsView;
        mItemsView.setPresenter(this);
        wrPresenter = new WeakReference<>(this);
    }

    @Override
    public void start() {
        loadItems();
    }

    void loadItems() {
        mItemsRepository.getItems(new ItemsDataSource.LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> items) {
                List<Item> itemsToShow = items == null ? new ArrayList<Item>() : items;
                if (mItemsView == null) {
                    return;
                }
                processItems(itemsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (mItemsView == null) {
                    return;
                }
                mItemsView.showLoadingItemsError();
            }
        });
    }

    private void processItems(List<Item> items) {
        if (mItemsView == null) {
            return;
        }
        if (items.isEmpty()) {
            mItemsView.showLoadingItemsError();
        } else {
            // Show the list of items
            mItemsView.showItems(items);
        }
    }

    @Override
    public void refreshItems() {
        // Invalidate cache and load items from server
        mItemsRepository.refreshItems();
        loadItems();

    }

    @Override
    public void takeView(ItemsContract.View view) {
        this.mItemsView = view;
        loadItems();
    }

    @Override
    public void scheduleRefreshItems() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<? extends Long> getObservable() {
        int secondsToLoadItems = 1;
        return Observable.timer(secondsToLoadItems, TimeUnit.SECONDS);
    }

    private Observer<Long> getObserver() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Long value) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                refreshItems();
            }
        };
    }
}
