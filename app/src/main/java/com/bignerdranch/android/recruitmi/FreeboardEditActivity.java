package com.bignerdranch.android.recruitmi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by 이예린 on 2018-06-08.
 */

public class FreeboardEditActivity extends AppCompatActivity{


    public EditText textTitle;
    public EditText textContent;
    public Button insertButton;
    public ImageButton imgPlus;
    public ImageView photo;
    private ImageButton getImgBtn;
    Intent intent;


    String title;
    String content;
    String today;
    String imgsrc;
    EditText titleLar;
    EditText contentLar;
    Calendar cal;
    String urlpath;


    String str_input1, str_input2, str_input3;
    String name ="yerin";
    MainActivity ma = new MainActivity();
    //private static final PICK_FROM_CAMERA = 0;
    //private static final PICK_FROM_ALBUM = 1;

    FileInputStream mFileInputStream;
    String file_name;
    String file_path;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board);
        intent = getIntent();
        urlpath = intent.getStringExtra("database");
        textTitle = (EditText) findViewById(R.id.board_title);
        textContent = (EditText) findViewById(R.id.board_content);
        imgPlus = (ImageButton) findViewById(R.id.imageButton);
        photo = (ImageView) findViewById(R.id.photo_view);
        insertButton = (Button) findViewById(R.id.board_insert);
        photo.setVisibility(View.INVISIBLE);
        //db에 넣는 버튼
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = textTitle.getText().toString();
                content = textContent.getText().toString();

                //uploadFile(getImgURL, getImgName);
                //HttpFileUpload("http://10.251.7.143:3000/process/photo",file_name);
                new JSONTask().execute();
                finish();
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    System.out.println("getData 출력했을 때");
                    System.out.println(data.getData());


                    // 이미지 표시
                    photo.setImageBitmap(img);
                    photo.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    if (uri.getScheme().toString().compareTo("content")==0)
                    {
                        Cursor cursor =getContentResolver().query(uri, null, null, null, null);
                        if (cursor.moveToFirst())
                        {
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                            Uri filePathUri = Uri.parse(cursor.getString(column_index));
                            file_name = filePathUri.getLastPathSegment().toString();
                            file_path=filePathUri.getPath();
                            Toast.makeText(this,"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();
                            System.out.println("파일이름 :"+file_name+" 경로이름 : "+file_path);

                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public class JSONTask extends AsyncTask<String, String,String> {




        @Override
        protected String doInBackground(String... urls) {


            String boundary = "****";
            String LINE_FEED = "\r\n";
            String charset = "UTF-8";
            OutputStream outputStream;
            PrintWriter writer;
            File targetFile = null;
            if(file_path != null) {
                targetFile = new File(file_path);
            }

            JSONObject result = null;
            try{

                URL url = new URL("http://172.20.10.4:3000/process/"+urlpath);
                //URL url = new URL("http://172.30.1.25:3000/process/freeboardPriv");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(15000);

                outputStream = connection.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

                /** Body에 데이터를 넣어줘야 할경우 없으면 Pass **/
                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"key_title\"").append(LINE_FEED);
                writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.append(title).append(LINE_FEED);
                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"key_content\"").append(LINE_FEED);
                writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.append(content).append(LINE_FEED);
                writer.flush();

                /** 파일 데이터를 넣는 부분**/
                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file_name + "\"").append(LINE_FEED);
                writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file_name)).append(LINE_FEED);
                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();

                FileInputStream inputStream = new FileInputStream(targetFile);
                byte[] buffer = new byte[(int)targetFile.length()];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                inputStream.close();
                writer.append(LINE_FEED);
                writer.flush();

                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    try {
                        result = new JSONObject(response.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    result = new JSONObject(response.toString());
                }

            } catch (ConnectException e) {
                //Log.e(TAG, "ConnectException");
                e.printStackTrace();


            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(result);
            return result.toString();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Intent i = new Intent(, HomeActivity.class);
            //getIntent().putExtra("userresponse", result);
            //Log.i("받아온 값",result);
            // startActivity(i);
        }
    }
}
