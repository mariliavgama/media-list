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
    @ColumnInfo(name = "headline")
    private final String mHeadline;

    @Nullable
    @ColumnInfo(name = "summary_short")
    private final String mSummaryShort;

    @Nullable
    @ColumnInfo(name = "byline")
    private final String mByLine;

    @Nullable
    @ColumnInfo(name = "publication_date")
    private final String mPublicationDate;


    @Nullable
    @ColumnInfo(name = "multimedia_src")
    private final String mMultimediaSrc;

    @Nullable
    @ColumnInfo(name = "display_title")
    private final String mDisplayTitle;

    @Nullable
    @ColumnInfo(name = "mpaa_rating")
    private final String mMppaRating;

    /**
     * Use this constructor to create a new Item.
     *
     * @param headline          headline of the item
     * @param summaryShort      short summary of the item
     * @param byLine            by line of the item
     * @param publicationDate   publication date of the item
     * @param multimediaSrc     multimedia photo src of the item
     * @param displayTitle      display title of the item
     * @param mppaRating        mppa rating of the item

     */
    @Ignore
    public Item(@Nullable String headline, @Nullable String summaryShort,
                @Nullable String byLine, @Nullable String publicationDate,
                @Nullable String multimediaSrc, @Nullable String displayTitle,
                @Nullable String mppaRating) {
        this(headline, summaryShort, byLine, publicationDate, multimediaSrc, displayTitle,
                mppaRating, UUID.randomUUID().toString());
    }


    /**
     * Use this constructor to specify an Item if the item already has an id
     *
     * @param headline          headline of the item
     * @param summaryShort      short summary of the item
     * @param byLine            by line of the item
     * @param publicationDate   publication date of the item
     * @param multimediaSrc     multimedia photo src of the item
     * @param displayTitle      display title of the item
     * @param mppaRating        mppa rating of the item
     * @param id                id of the item
     */
    public Item(@Nullable String headline, @Nullable String summaryShort,
                @Nullable String byLine, @Nullable String publicationDate,
                @Nullable String multimediaSrc, @Nullable String displayTitle,
                @Nullable String mppaRating, @NonNull String id) {
        mId = id;
        mHeadline = headline;
        mSummaryShort = summaryShort;
        mByLine = byLine;
        mPublicationDate = publicationDate;
        mMultimediaSrc = multimediaSrc;
        mDisplayTitle = displayTitle;
        mMppaRating = mppaRating;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getHeadline() {
        return mHeadline;
    }

    @Nullable
    public String getSummaryShort() {
        return mSummaryShort;
    }

    @Nullable
    public String getByLine() {
        return mByLine;
    }

    @Nullable
    public String getPublicationDate() {
        return mPublicationDate;
    }

    @Nullable
    public String getMultimediaSrc() {
        return mMultimediaSrc;
    }

    @Nullable
    public String getDisplayTitle() {
        return mDisplayTitle;
    }

    @Nullable
    public String getMppaRating() {
        return mMppaRating;
    }

    @Override
    public String toString() {
        return "Item with title " + mDisplayTitle;
    }
}