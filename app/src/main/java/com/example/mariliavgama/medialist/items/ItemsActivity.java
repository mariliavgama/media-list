package com.example.mariliavgama.medialist.items;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mariliavgama.medialist.R;
import com.example.mariliavgama.medialist.data.source.ItemsRepository;
import com.example.mariliavgama.medialist.data.source.local.ItemsLocalDataSource;
import com.example.mariliavgama.medialist.data.source.local.ListAppDatabase;
import com.example.mariliavgama.medialist.data.source.remote.ItemsRemoteDataSource;
import com.example.mariliavgama.medialist.util.AppExecutors;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;

/**
 * Architecture style based on: https://github.com/googlesamples/android-architecture
 * This is an Activity which creates fragments and presenters.
 */

public class ItemsActivity extends AppCompatActivity {

    static WeakReference<ItemsActivity> wrActivity;
    private ItemsPresenter mItemsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_act);
        wrActivity = new WeakReference<>(this);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                // Cache is not available by default, so set it here.
                .cacheInMemory(true)
                // Add this if would like to cache on SD Card:
                ///.cacheOnDisk(true)
                .build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config); // Has to be done on Application start

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ItemsFragment itemsFragment =
                (ItemsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (itemsFragment == null) {
            // Create the fragment
            itemsFragment = ItemsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, itemsFragment);
            transaction.commit();
        }

        // TODO: Use dependency injection instead of creating a new instance of repository
        ListAppDatabase database = ListAppDatabase.getInstance(getApplicationContext());

        ItemsRepository repository = ItemsRepository.getInstance(ItemsRemoteDataSource.getInstance(),
                ItemsLocalDataSource.getInstance(new AppExecutors(),
                        database.itemDao()));

        // Create the presenter
        mItemsPresenter = new ItemsPresenter(repository, itemsFragment);

        setAlarmToLoadItems();
    }

    /*
    * Set alarm to load items from time to time.
    * For this prototype, new items are being loaded every 60 seconds but that can be set to be less
    * frequent.
     */
    private void setAlarmToLoadItems() {
        Intent alarmIntent = new Intent(this, ItemsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int delayToLoadItems = 60000;
        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), delayToLoadItems, pendingIntent);
        }
    }
}
