package com.bignerdranch.android.recruitmi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 이예린 on 2018-05-14.
 */

public class BoardFragment extends Fragment {
    private Button writeButton;
    public  BoardFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_board, container, false );
        return v;
/*
        writeButton = (Button) v.findViewById(R.id.button4);
        writeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, boardfrag );
                fragmentTransaction.commit();

            }*/
    }
}

