package com.bignerdranch.android.recruitmi;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.ref.WeakReference;

/**
 * Created by 이예린 on 2018-05-14.
 *
 *
 * 공부시간을 체크하는 스톱워치 기능을 제공하는 Timer Fragment
 */

public class TimerFragment extends Fragment {

    TextView myOutput;
    TextView myrecord;
    ImageView status;
    Button myBtnRec;
    Button myBtnStart;

    final static int Init =0;
    final static int Run =1;
    final static int Pause =2;
    static String str ="";

    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    int myCount=1;
    long myBaseTime;
    long myPauseTime;

    public TimerFragment () {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.timertest, container, false );
        myOutput = (TextView)v.findViewById(R.id.time_out);
        myBtnRec = (Button) v.findViewById(R.id.recordButton);
        myBtnStart = (Button) v.findViewById(R.id.startButton);
        myrecord = (TextView) v.findViewById(R.id.recording);
        status = (ImageView) v.findViewById(R.id.btn_status);

        //스톱워치로 시간 측정 시작
        myBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(cur_Status){
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                        myTimer.sendEmptyMessage(0);
                        myBtnStart.setText("Stop");
                        myBtnRec.setEnabled(true);
                        cur_Status = Run;
                        break;

                    case Run:
                        status.setBackgroundResource(R.drawable.stopwatchstop);
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        myPauseTime = SystemClock.elapsedRealtime();
                        myBtnStart.setText("Start");
                        myBtnRec.setText("Reset");
                        cur_Status = Pause;
                        break;


                    case Pause:
                        status.setBackgroundResource(R.drawable.stopwatchstart);
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now- myPauseTime);
                        myBtnStart.setText("Stop");
                        myBtnRec.setText("Record");

                        cur_Status = Run;
                        break;

                }

            }
        });

        //스톱워치 시간 기록
        myBtnRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch(cur_Status){
                    case Run:
                        str = myrecord.getText().toString();
                        str +=  String.format("%d. %s\n",myCount,getTimeOut());
                        myrecord.setText(str);
                        myCount++; //카운트 증가
                        break;
                    case Pause:

                        //핸들러를 멈춤
                        myTimer.removeMessages(0);
                        myBtnStart.setText("시작");
                        myBtnRec.setText("기록");
                        myOutput.setText("00:00:00");
                        cur_Status = Init;
                        myCount = 1;
                        myrecord.setText("");
                        myBtnRec.setEnabled(false);
                        status.setBackgroundResource(R.drawable.stopwatchstop);
                        break;
                }
            }
        });

        return v;
    }





    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            myOutput.setText(getTimeOut());
            //sendEmptyMessage : 비어있는 메세지를 Handler 에게 전송
            myTimer.sendEmptyMessage(0);
        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        return easy_outTime;
    }
}
