package com.dominikkrajcer.numbers.ui.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dominikkrajcer.numbers.HttpAsyncTaskLoader;
import com.dominikkrajcer.numbers.InternetUtils;
import com.dominikkrajcer.numbers.R;
import com.dominikkrajcer.numbers.data.Number;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private static final String BASE_URL = "http://dev.tapptic.com/test/json.php?name=";
    private static final int LOADER_ID = 0;

    private TextView numberTV;
    private ImageView numberIV;
    private String numberName;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initSubViews(rootView);
        return rootView;
    }

    private void initSubViews(View rootView) {
        numberIV = rootView.findViewById(R.id.numberIV);
        numberTV = rootView.findViewById(R.id.numberTV);
    }

    public void initData(String numberName) {
        this.numberName = numberName;
        if (InternetUtils.isOnline(getContext())) {
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String numberName) {
        this.numberName = numberName;
        if (InternetUtils.isOnline(getContext())) {
            getLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
        } else {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new HttpAsyncTaskLoader(getContext(), BASE_URL + numberName);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        displayData(data);
    }

    private void displayData(String data) {
        try {
            Number number = Number.fromJSON(new JSONObject(data));
            numberTV.setText(number.getText());
            Picasso.with(getContext()).load(number.getImageUrl()).into(numberIV);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
