package com.bignerdranch.android.recruitmi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BoardActivity_freeboard_2 extends AppCompatActivity {

    //String imgTablename ="img";
    private static final String TAG = "freeboard_search";
    private String PER_PAGE = "&per_page=50";
    public static final int LOAD_SUCCESS = 101;
    private static ProgressDialog progressDialog;
    private EditText textTitle;
    private EditText textContent;

    String str_input1, str_input2, str_input3;
    String userId = "yerin";
//
    ListView listView;

    ArrayList<list_item> list_itemArrayList;
    private SimpleAdapter adapter = null;
    private List<HashMap<String,String>> infoList = null;
    private List<HashMap<String,String>> pushList = null;
    TextView testtext;
    Button check;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.board_list), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.go_write_page :
                //글 작성 페이지로 이동 항목이 선택되었음
                //Intent intent1 = new Intent(BoardActivity_freeboard_2.this, Freeboard2EditActivity.class);
                Intent intent1 = new Intent(BoardActivity_freeboard_2.this, FreeboardEditActivity.class);
                intent1.putExtra("database", "freeboardPub");
                startActivity(intent1);
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView) findViewById(R.id.my_listview);
        list_itemArrayList = new ArrayList<list_item>();
        infoList = new ArrayList<HashMap<String, String>>();
        pushList = new ArrayList<HashMap<String, String>>();

        String [] from = new String[]{"nickname","title","date"};
        int [] to = new int[]{R.id.nickname_textView, R.id.title_textView, R.id.date_textView};
        HashMap<String,String> map;

        progressDialog = new ProgressDialog(BoardActivity_freeboard_2.this);
        progressDialog.setMessage("plz wait");
        progressDialog.show();

        getJSON();
        adapter = new SimpleAdapter(this,infoList,R.layout.item,from,to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BoardActivity_freeboard_2.this,FreeboardDetailActivity.class);
                intent.putExtra("nickname", pushList.get(i).get("nickname"));
                intent.putExtra("title", pushList.get(i).get("title"));
                intent.putExtra("detail", pushList.get(i).get("detail"));
                intent.putExtra("date",pushList.get(i).get("date"));
                intent.putExtra("img", pushList.get(i).get("img"));
                startActivity(intent);
            }
        });



        /*
        //글 작성 페이지에서 String 값 받아오기
        list_itemArrayList.add(new list_item(R.mipmap.ic_launcher, "user01", "제목입니다.", new Date(System.currentTimeMillis()), "내용2"));

        myListAdapter = new MyListAdapter(BoardActivity_freeboard_2.this, list_itemArrayList);
        listView.setAdapter(myListAdapter);*/
    }

    private final MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<BoardActivity_freeboard_2> weakReference;

        public MyHandler(BoardActivity_freeboard_2 activity) {
            weakReference = new WeakReference<BoardActivity_freeboard_2>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BoardActivity_freeboard_2 activity = weakReference.get();
            if(activity != null) {
                switch(msg.what) {
                    case LOAD_SUCCESS:
                        activity.progressDialog.dismiss();
                        activity.adapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    public void getJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    URL url = new URL("http://172.20.10.4:3000/select/freeboardPub");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();
                    InputStream inputStream;
                    if (responseStatusCode == httpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader br = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();
                } catch (Exception e) {
                    result = e.toString();
                }

                if (jsonParser(result)) {
                    System.out.println(result);
                    Message message = mHandler.obtainMessage(LOAD_SUCCESS);
                    mHandler.sendMessage(message);
                }
            }
        });
        thread.start();
    }

    public boolean jsonParser(String jsonString) {
        String title = null;
        String nickname = null;
        String date = null;
        String content = null;
        String imgPath = null;


        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("data");
            for (int a = 0; a < jarray.length(); a++) {
                //HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(a);

                title = jObject.optString("title");
                nickname = jObject.optString("nickname");
                date = jObject.optString("date");
                content = jObject.optString("detail");
                imgPath = jObject.optString("img");
                HashMap<String, String> infoMap = new HashMap<String, String>();
                HashMap<String, String> pushMap = new HashMap<String, String>();

                infoMap.put("nickname", nickname);
                infoMap.put("title", title);
                infoMap.put("date", date);

                pushMap.put("nickname", nickname);
                pushMap.put("title", title);
                pushMap.put("date", date);
                pushMap.put("detail", content);
                pushMap.put("img", imgPath);

                infoList.add(infoMap);
                System.out.println(infoMap);
                System.out.println(pushMap);
                pushList.add(pushMap);

            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return false;

    }
}
