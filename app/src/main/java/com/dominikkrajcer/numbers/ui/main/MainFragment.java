package com.dominikkrajcer.numbers.ui.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dominikkrajcer.numbers.HttpAsyncTaskLoader;
import com.dominikkrajcer.numbers.InternetUtils;
import com.dominikkrajcer.numbers.R;
import com.dominikkrajcer.numbers.data.Number;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, MainRecyclerViewAdapter.OnItemClickListener {

    private static final String BASE_URL = "http://dev.tapptic.com/test/json.php";
    private static final String SELECTED_ITEM = "SELECTED_ITEM";
    private static final String SCROLL_STATE = "SCROLL_STATE";
    private static final int LOADER_ID = 0;


    private MainFragmentCallback mainFragmentCallback;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    public void setOnItemInFragmentClickListener(MainFragmentCallback mainFragmentCallback) {
        this.mainFragmentCallback = mainFragmentCallback;
    }

    public MainFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mainRecyclerViewAdapter != null) {
            outState.putInt(SELECTED_ITEM, mainRecyclerViewAdapter.getSelectedItem());
            outState.putInt(SCROLL_STATE, linearLayoutManager.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mainRecyclerViewAdapter.notifyItemChanged(mainRecyclerViewAdapter.getSelectedItem());
            mainRecyclerViewAdapter.setSelectedItem(savedInstanceState.getInt(SELECTED_ITEM, 0));
            scrollListToSavedPosition(savedInstanceState.getInt(SCROLL_STATE, 0));
        }
    }

    private void scrollListToSavedPosition(final int position) {
        if(position != 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    linearLayoutManager.scrollToPosition(position);
                }
            }, 200);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromTheInternet();
    }

    private void getDataFromTheInternet() {
        if (InternetUtils.isOnline(getContext())) {
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initRecyclerView(rootView);

        return rootView;
    }

    private void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter();
        mainRecyclerViewAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mainRecyclerViewAdapter);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new HttpAsyncTaskLoader(getContext(), BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        List<Number> numberList = parseData(data);

        if (!numberList.isEmpty()) {
            mainFragmentCallback.onFinishLoadingData(numberList.get(mainRecyclerViewAdapter.getSelectedItem()));
            mainRecyclerViewAdapter.setNumberList(numberList);
        }
    }

    private List<Number> parseData(String data) {
        List<Number> numberList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                Number number = Number.fromJSON(jsonArray.getJSONObject(i));
                numberList.add(number);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return numberList;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onItemClick(Number number) {
        mainFragmentCallback.onItemInFragmentClick(number);
    }

    public interface MainFragmentCallback {

        void onItemInFragmentClick(Number number);

        void onFinishLoadingData(Number firstNumber);
    }
}
