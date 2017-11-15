package com.example.mariliavgama.medialist.items;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariliavgama.medialist.R;
import com.example.mariliavgama.medialist.data.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        ListView listView = (ListView) root.findViewById(R.id.items_list);
        listView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_refresh);

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
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Toast.makeText(activity, R.string.items_show_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingItemsError() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Toast.makeText(activity, R.string.items_show_error, Toast.LENGTH_SHORT).show();
    }

    static class ItemsAdapter extends BaseAdapter {
        private List<Item> mItems;

        public ItemsAdapter(List<Item> items) {
            setList(items);
        }

        public void replaceData(List<Item> items) {
            setList(items);
            notifyDataSetChanged();
        }

        private void setList(@NonNull List<Item> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Item getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            ViewHolder holder;

            if (rowView != null) {
                holder = (ViewHolder) rowView.getTag();
            } else {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item, viewGroup, false);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            }

            final Item item = getItem(i);
            holder.title.setText(item.getTitle());
            holder.byLine.setText(item.getByLine());

            return rowView;
        }

        static class ViewHolder {
            @BindView(R.id.item_title) TextView title;
            @BindView(R.id.item_byline) TextView byLine;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
