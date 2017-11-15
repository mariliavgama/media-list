package com.example.mariliavgama.medialist.items;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ItemsAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, refresh items
        // Only retain a weak reference to the presenter
        if (ItemsPresenter.wrPresenter == null) {
            return;
        }

        ItemsPresenter presenter = ItemsPresenter.wrPresenter.get();

        if (presenter == null) {
            return;
        }
        presenter.refreshItems();
    }
}