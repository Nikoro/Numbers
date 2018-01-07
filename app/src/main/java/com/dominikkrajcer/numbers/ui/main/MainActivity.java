package com.dominikkrajcer.numbers.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.dominikkrajcer.numbers.R;
import com.dominikkrajcer.numbers.data.Number;
import com.dominikkrajcer.numbers.ui.detail.DetailFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentCallback {

    private DetailFragment detailFragment;
    private static final String WAS_DETAIL_VIEW_NOT__VISIBLE = "WAS_DETAIL_VIEW_NOT__VISIBLE";

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(WAS_DETAIL_VIEW_NOT__VISIBLE, detailFragment.isHidden());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainFragment();
        initDetailFragment();
    }

    private void initMainFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        mainFragment.setOnItemInFragmentClickListener(this);
    }

    private void initDetailFragment() {
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);

        if (isPortraitMode()) {
            hideDetailView();
        } else {
            showDetailView();
        }
    }

    private void showDetailView() {
        getSupportFragmentManager().beginTransaction().show(detailFragment).commit();
    }

    private void hideDetailView() {
        getSupportFragmentManager().beginTransaction().hide(detailFragment).commit();
    }

    @Override
    public void onItemInFragmentClick(Number number) {
        showDetailView();
        detailFragment.updateData(number.getName());
    }

    @Override
    public void onFinishLoadingData(Number selectedNumber) {
        detailFragment.initData(selectedNumber.getName());
    }

    private boolean isPortraitMode() {
        return getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        if (isPortraitMode() && !detailFragment.isHidden()) {
            hideDetailView();
        } else {
            super.onBackPressed();
        }
    }
}
