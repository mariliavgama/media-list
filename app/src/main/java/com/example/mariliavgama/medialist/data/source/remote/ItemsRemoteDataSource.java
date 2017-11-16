package com.example.mariliavgama.medialist.data.source.remote;

import android.support.annotation.NonNull;

import com.example.mariliavgama.medialist.data.Item;
import com.example.mariliavgama.medialist.data.source.ItemsDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class ItemsRemoteDataSource implements ItemsDataSource {

    private static ItemsRemoteDataSource INSTANCE;

    private final static Map<String, Item> ITEMS_SERVICE_DATA;

    static {
        ITEMS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static ItemsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private ItemsRemoteDataSource() {}

    private static void addItem(String headline, String summaryShort,
                                String byLine, String publicationDate,
                                String multimediaSrc, String displayTitle,
                                String mppaRating) {
        Item newItem = new Item(headline, summaryShort, byLine, publicationDate, multimediaSrc,
                displayTitle, mppaRating);
        ITEMS_SERVICE_DATA.put(newItem.getDisplayTitle(), newItem);
    }

    @Override
    public void getItems(final @NonNull LoadItemsCallback callback) {
        ItemsService service = ItemsService.retrofit.create(ItemsService.class);
        Call<Results> call = service.repoItems();
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (!response.isSuccessful()) {
                    callback.onDataNotAvailable();
                    return;
                }

                List<Result> results = response.body().getResults();
                List<Item> items = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    Result r = results.get(i);
                    addItem(r.getHeadline(), r.getSummaryShort(), r.getByline(), r.getPublicationDate(),
                            r.getMultimedia().getSrc(), r.getDisplayTitle(), r.getMpaaRating());
                }
                items.addAll(ITEMS_SERVICE_DATA.values());
                callback.onItemsLoaded(items);
            }

            @Override
            public void onFailure (Call<Results> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveItem(@NonNull Item item) {
        ITEMS_SERVICE_DATA.put(item.getDisplayTitle(), item);
    }

    @Override
    public void refreshItems() {

    }

    @Override
    public void deleteAllItems() {
        ITEMS_SERVICE_DATA.clear();
    }

    public interface ItemsService {
        @GET("svc/movies/v2/reviews/dvd-picks.json?order=by-date&api-key=b75da00e12d54774a2d362adddcc9bef")
        Call<Results> repoItems();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nytimes.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}