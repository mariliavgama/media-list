package com.example.mariliavgama.medialist.items;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariliavgama.medialist.R;
import com.example.mariliavgama.medialist.data.Item;
import com.example.mariliavgama.medialist.util.DateTimeUtils;
import com.example.mariliavgama.medialist.util.LayoutUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Architecture style based on: https://github.com/googlesamples/android-architecture
 * This is a Fragment which implements the view interface.
 */

public class ItemsFragment extends Fragment implements ItemsContract.View {

    private ItemsContract.Presenter mPresenter;
    private ItemsAdapter mListAdapter;

    public ItemsFragment() {
        // Requires empty public constructor
    }
    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ItemsAdapter(new ArrayList<Item>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.items_frag, container, false);

        // Set up items view
        RecyclerView recyclerView = root.findViewById(R.id.items_list);
        // only retain a weak reference to the activity
        ItemsActivity activity = ItemsActivity.wrActivity.get();
        if (activity == null) {
            return null;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab = activity.findViewById(R.id.fab_refresh);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.scheduleRefreshItems();
            }
        });
        return root;
    }

    @Override
    public void showItems(List<Item> items) {
        mListAdapter.replaceData(items);
        Context context = getContext();
        if (context == null) {
            return;
        }
        Toast.makeText(context, R.string.items_show_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingItemsError() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        Toast.makeText(context, R.string.items_show_error, Toast.LENGTH_SHORT).show();
    }

    static class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Item> mItems;

        ItemsAdapter(List<Item> items) {
            setList(items);
        }

        void replaceData(List<Item> items) {
            setList(items);
            notifyDataSetChanged();
        }

        private void setList(@NonNull List<Item> items) {
            mItems = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(view);
        }

        //
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ItemViewHolder) holder).bindData(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public int getItemViewType(final int position) {
            return R.layout.item;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView headline;
        private ImageView multimedia;
        private TextView summaryShort;
        private TextView byLineDate;
        private TextView titleRating;

        ItemViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            headline = itemView.findViewById(R.id.item_headline);
            multimedia = itemView.findViewById(R.id.item_multimedia);
            summaryShort = itemView.findViewById(R.id.item_summary_short);
            byLineDate = itemView.findViewById(R.id.item_byline_date);
            titleRating = itemView.findViewById(R.id.item_title_rating);
        }

        void bindData(final Item item) {
            headline.setText(item.getHeadline());

            multimedia.setContentDescription(item.getHeadline());
            // Default options set on Activity onCreate will be used in all calls to displayImage
            ImageLoader.getInstance().displayImage(item.getMultimediaSrc(), multimedia);

            summaryShort.setText(item.getSummaryShort());
            if (context == null) {
                return;
            }
            byLineDate.setText(String.format(context.getString(R.string.byline_date),
                    item.getByLine(), DateTimeUtils.getDate(item.getPublicationDate())));
            titleRating.setText(getTitleRating(item));
        }

        String getTitleRating(Item item) {
            String rating = "".equals(item.getMppaRating()) ? "" :
                    String.format(context.getString(R.string.rating), item.getMppaRating());
            return item.getDisplayTitle() + rating;
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int SPACE = LayoutUtils.DPToPixels(5);

        SpacesItemDecoration() {}

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = SPACE;
            outRect.right = SPACE;
            outRect.bottom = SPACE;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildAdapterPosition(view) == 0) {
                outRect.top = SPACE;
            }
        }
    }
}
