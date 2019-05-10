package com.bignerdranch.android.recruitmi;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.IdRes;

import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Timer;

public class HomeActivity extends FragmentActivity {


    private MainBoardFragment mb;
    private AlarmFragment ab;
    private TimerFragment tb;
    private DiaryFragment db;
    private HomeFragment hb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String sessionId = intent.getStringExtra("userresponse");

        mb = new  MainBoardFragment();
        tb = new TimerFragment();
        db = new DiaryFragment();
        hb = new HomeFragment();
        initFragment();



        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(tabId ==R.id.tab_home) {
                    transaction.replace(R.id.contentContainer,hb);
                    transaction.commit();
                }else if(tabId == R.id.tab_diary) {
                    transaction.replace(R.id.contentContainer, db);
                    transaction.commit();
                }else if(tabId == R.id.tab_board) {
                    transaction.replace(R.id.contentContainer, mb);
                    transaction.commit();
                }else if(tabId ==R.id.stop_watch) {
                    transaction.replace(R.id.contentContainer, tb);
                    transaction.commit();
                }

            }
        });



    }

    public void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, mb);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
