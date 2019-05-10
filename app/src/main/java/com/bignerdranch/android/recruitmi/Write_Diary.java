package com.bignerdranch.android.recruitmi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by 이예린 on 2018-06-11.
 */

public class Write_Diary extends AppCompatActivity{

    private DiaryFragment myFragment;
    SQLiteDatabase db;
    String dbname = "myDB";
    String tablename = "diary";
    String sql;
    String title;
    String content;
    String today;
    EditText titleLar;
    EditText contentLar;
    Button jaksung;




    private static final String DIALOG_DATE = "DialogDate";
    Calendar calendar;
    int year;
    int month;
    int dayOfMonth;
    ImageButton mDateButton;
    DatePickerDialog datePickerDialog;
    TextView date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        myFragment = new DiaryFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary);

        titleLar = (EditText) findViewById(R.id.diary_date2);
        contentLar = (EditText)findViewById(R.id.diary_date3);
        mDateButton = (ImageButton) findViewById(R.id.imageButton8);
        jaksung = (Button) findViewById(R.id.insert_diary_sql);
        date = findViewById(R.id.diary_date);



        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Write_Diary.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();


            }
        });


        jaksung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print("ff");
               title = titleLar.getText().toString();
               content = contentLar.getText().toString();
               today = date.getText().toString();
                new JSONTask().execute("http://172.20.10.4:3000/diary_insert") ;
                finish();

            }
        });
    }

    public class JSONTask extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("diary_title",title);
                jsonObject.accumulate("diary_content",content);
                jsonObject.accumulate("diary_date",today);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결하는 부분
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-Cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept","text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                    //서버로부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine())!=null) {
                        buffer.append(line);
                    }
                    return buffer.toString();

                }  catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con!=null) {
                        con.disconnect();
                    }
                    try{
                        if(reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

}
