package com.example.mariliavgama.medialist.data;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Immutable model class for an Item.
 */
@Entity(tableName = "items")
public final class Item {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "byline")
    private final String mByLine;


    /**
     * Use this constructor to create a new Item.
     *
     * @param title     title of the item
     * @param byLine    byLine of the item
     */
    @Ignore
    public Item(@Nullable String title, @Nullable String byLine) {
        this(title, byLine, UUID.randomUUID().toString());
    }


    /**
     * Use this constructor to specify an Item if the item already has an id
     *
     * @param title       title of the item
     * @param byLine      byLine of the item
     * @param id          id of the item
     */
    public Item(@Nullable String title, @Nullable String byLine,
                @NonNull String id) {
        mId = id;
        mTitle = title;
        mByLine = byLine;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getByLine() {
        return mByLine;
    }

    @Override
    public String toString() {
        return "Item with title " + mTitle;
    }
}