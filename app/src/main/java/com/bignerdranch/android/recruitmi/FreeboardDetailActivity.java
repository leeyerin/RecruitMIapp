package com.bignerdranch.android.recruitmi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 이예린 on 2018-06-09.
 */

public class FreeboardDetailActivity extends AppCompatActivity {



    Intent intent;
    public TextView textTitle;
    public TextView textContent;
    public TextView textDate;
    public TextView textNick;
    public ImageView photoview;
    String title;
    String content;
    String nickname;
    String imgpath;
    Bitmap bmImag;
    String imgUrl = "http://172.20.10.4:3000/";
    //String date;
    //Date from = new Date();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_board);
        intent = getIntent();


        textTitle = findViewById(R.id.detail_title_view);
        textContent = findViewById(R.id.detail_content_view);
        textDate = findViewById(R.id.detail_date_view);
        textNick = findViewById(R.id.detailViewId);
        photoview = findViewById(R.id.detail_photo);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("detail");
        nickname = intent.getStringExtra("nickname");
        imgpath = intent.getStringExtra("img");
        // System.out.print(intent.getStringExtra("date"));
//        Date today = (Date) intent.getSerializableExtra("date");
//        String date = transFormat.format(today);
        String date = intent.getStringExtra("date");

        new back().execute(imgUrl+imgpath);
        textTitle.setText(title);
        textContent.setText(content);
        textDate.setText(date);
        textNick.setText(nickname);

    }

    private class back extends AsyncTask<String,Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImag = BitmapFactory.decodeStream(is);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return bmImag;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //super.onPostExecute(bitmap);
            photoview.setImageBitmap(bmImag);
            //photoview.setVisibility(View.VISIBLE);
        }
    }
}