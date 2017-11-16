package com.example.mariliavgama.medialist.items;

import com.example.mariliavgama.medialist.data.Item;
import com.example.mariliavgama.medialist.data.source.ItemsDataSource;
import com.example.mariliavgama.medialist.data.source.ItemsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link ItemsPresenterTest}
 */
public class ItemsPresenterTest {

    private static List<Item> ITEMS;

    @Mock
    private ItemsRepository mItemsRepository;

    @Mock
    private ItemsContract.View mItemsView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<ItemsDataSource.LoadItemsCallback> mLoadItemsCallbackCaptor;

    private ItemsPresenter mItemsPresenter;

    @Before
    public void setupItemsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mItemsPresenter = new ItemsPresenter(mItemsRepository, mItemsView);
        mItemsPresenter.takeView(mItemsView);

        // Set the items
        ITEMS = new ArrayList<Item>();
        String[] index = {"1", "2", "3"};
        for (int i = 0; i < 3; i++) {
            String n = index[i];
            ITEMS.add(new Item("headline" + n , "summaryShort" + n, "byLine" + n,
                    "publicationDate" + n, "multimediaSrc" + n, "displayTitle" + n,
                    "mppaRating" + n));
        }
    }

    @Test
    public void loadAllItemsFromRepositoryAndLoadIntoView() {
        // Given an initialized ItemsPresenter with initialized items
        // When loading of Items is requested
        mItemsPresenter.loadItems();

        // Callback is captured and invoked with stubbed items twice
        //First time is when the fragment is bound to the view and a second time when we force another load
        verify(mItemsRepository,times(2)).getItems(mLoadItemsCallbackCaptor.capture());
        mLoadItemsCallbackCaptor.getValue().onItemsLoaded(ITEMS);

        ArgumentCaptor<List> showItemsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mItemsView).showItems(showItemsArgumentCaptor.capture());
        assertTrue(showItemsArgumentCaptor.getValue().size() == 3);
        assertEquals(((Item) showItemsArgumentCaptor.getValue().get(0)).getHeadline(), "headline1");
        assertEquals(((Item) showItemsArgumentCaptor.getValue().get(0)).getSummaryShort(), "summaryShort1");
    }
}