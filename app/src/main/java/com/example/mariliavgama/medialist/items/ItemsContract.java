package com.example.mariliavgama.medialist.items;

import com.example.mariliavgama.medialist.BasePresenter;
import com.example.mariliavgama.medialist.BaseView;
import com.example.mariliavgama.medialist.data.Item;

import java.util.List;

/**
 * Architecture style based on: https://github.com/googlesamples/android-architecture
 * This is a contract class which defines the connection between the view and the presenter.
 */

public interface ItemsContract {

    interface View extends BaseView<Presenter> {
        void showItems(List<Item> items);

        void showLoadingItemsError();
    }

    interface Presenter extends BasePresenter {
        void scheduleRefreshItems();

        void refreshItems();

        void takeView(ItemsContract.View view);
    }
}