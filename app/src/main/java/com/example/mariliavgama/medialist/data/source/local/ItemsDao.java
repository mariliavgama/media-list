package com.example.mariliavgama.medialist.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.mariliavgama.medialist.data.Item;

import java.util.List;

/**
 * Data Access Object for the items table.
 */
@Dao
public interface ItemsDao {

    /**
     * Select all items from the items table.
     *
     * @return all items.
     */
    @Query("SELECT * FROM Items")
    List<Item> getItems();

    /**
     * Insert a item in the database. If the item already exists, replace it.
     *
     * @param item the item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(Item item);

    /**
     * Delete all items.
     */
    @Query("DELETE FROM Items")
    void deleteItems();
}