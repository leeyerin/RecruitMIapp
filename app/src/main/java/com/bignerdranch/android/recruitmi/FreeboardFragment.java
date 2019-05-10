package com.bignerdranch.android.recruitmi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by 이예린 on 2018-05-28.
 * 모델 및 뷰 객체와 상호동작하는 컨트롤러
 * 특정 게시글의 상세 내역을 보여주고 사용자가 수정한 상세내역을 변경하는 것이 역할
 * 프래그먼트가 자신의 뷰를 화면에 보여주기 위해서는 FreeboardFragment를 BoardActivity_freeboardList에 추가해야 한다.
 */


public class FreeboardFragment extends Fragment {

    //private static final String ARG_FREEBOARD_ID = "freeboard_id";

    private Freeboard mFreeboard;
    private EditText mTitleField;
    private Button mDateButton;

    /*
        호스팅 액티비티가 프래그먼트의 인스턴스를 필요로 할 때 그 프래그먼트의 생성자를 직접 호출하는 대신
        newInstance() 의 메서드를 호출하면 된다.
     */
/*
    public static FreeboardFragment newInstance(UUID freeboardId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FREEBOARD_ID, freeboardId);

        FreeboardFragment fragment = new FreeboardFragment();
        fragment.setArguments(args);
        return fragment;
    }
    */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //자신의 상태를 저장하거나 읽는 번들 객체를 가짐
        mFreeboard = new Freeboard();
        //UUID freeboardId = (UUID) getArguments().getSerializable(ARG_FREEBOARD_ID);
        //mFreeboard = BoardLab.get(getActivity()).getFreeboard(freeboardId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board, container, false);
        // 프래그먼트의 뷰를 명시적으로 인플레이트
        mTitleField = (EditText) v.findViewById(R.id.board_title);
        //mTitleField.setText(mFreeboard.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //사용자가 입력한 데이터 값을 갖고 있는 CharSequence 객체의 toString() 메소드를 호출한다.
                mFreeboard.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}
