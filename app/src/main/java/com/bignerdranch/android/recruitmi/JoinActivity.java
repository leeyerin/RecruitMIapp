package com.bignerdranch.android.recruitmi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * Created by 이예린 on 2018-06-05.
 * 회원가입 화면
 */

public class JoinActivity extends AppCompatActivity{

    Button join;
    Button joinCancel;
    EditText signEmail;
    EditText signPassword;
    EditText signPasswordConfirm;
    public String sign_email;
    public String sign_password;
    public String sign_password_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        join = findViewById(R.id.signButton);
        signEmail = findViewById(R.id.signUpId);
        signPassword = findViewById(R.id.signUpPassword);
        signPasswordConfirm = findViewById(R.id.signUp_login_Password_confirm);
        joinCancel = findViewById(R.id.signCancelButton);

        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sign_email = signEmail.getText().toString();
                sign_password = signPassword.getText().toString();
                sign_password_confirm = signPasswordConfirm.getText().toString();

                new JSONTask().execute("http://172.20.10.4:3000/signup") ;
            }
        });

        joinCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_sign_id",sign_email);
                jsonObject.accumulate("user_sign_password",sign_password);
                jsonObject.accumulate("user_sign_password_confirm",sign_password_confirm);

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

            Intent i = new Intent(JoinActivity.this, LoginActivity.class);
            //getIntent().putExtra("userresponse", result);
            startActivity(i);
        }
    }
}
