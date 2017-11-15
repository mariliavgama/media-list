package com.example.mariliavgama.medialist.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mariliavgama.medialist.data.Item;

/**
 * The Room Database that contains the Item table.
 */
@Database(entities = {Item.class}, version = 1)
public abstract class ListAppDatabase extends RoomDatabase {

    private static ListAppDatabase INSTANCE;

    public abstract ItemsDao itemDao();

    private static final Object sLock = new Object();

    public static ListAppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ListAppDatabase.class, "Items.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}