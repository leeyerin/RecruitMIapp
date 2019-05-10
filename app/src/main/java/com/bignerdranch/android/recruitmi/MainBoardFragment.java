package com.bignerdranch.android.recruitmi;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by 이예린 on 2018-05-14.
 * 각각의 게시판으로 이동할 수 있게 도와주는 게시판 홈화면
 */

public class MainBoardFragment extends Fragment {

    private  final int REQUEST_CODE = 0;
    private BoardFragment  boardfrag;
    private ImageButton enterprise_info_button;
    private ImageButton enterprise_public_info_button;
    private ImageButton calendar;
    private ImageButton free_board;
    private ImageButton free_board_2;
    private ImageButton share;
    public MainBoardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardfrag = new BoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View v=  inflater.inflate(R.layout.mainboard, container, false);


        enterprise_info_button = (ImageButton) v.findViewById(R.id.button4); //사기업 채용정보 게시판 버튼
        calendar = (ImageButton) v.findViewById(R.id.button5); //시험일정 게시판 버튼
        free_board = (ImageButton) v.findViewById(R.id.button7); //자유게시판 | 취준생 버튼
        free_board_2 = (ImageButton) v.findViewById(R.id.button3); //자유게시판 | 공시생 버튼
        share = (ImageButton) v.findViewById(R.id.button9); //후기 공유 게시판

        enterprise_info_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BoardMenuActivity.class);
                startActivity(i);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BoardActivity_calendar.class);
                startActivity(i);
            }
        });

        free_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        free_board_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BoardActivity_freeboard_2.class);
                startActivity(i);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BoardActivity_share.class);
                startActivity(i);
            }
        });
        return v;
    }
}
