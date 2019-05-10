package com.bignerdranch.android.recruitmi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BoardActivity_public_board extends AppCompatActivity {

    private static final String TAG = "public_freeboard_search";
    private String PER_PAGE = "&per_page=50";
    public static final int LOAD_SUCCESS = 101;
    private static ProgressDialog progressDialog;
    //String dbname = "myDB";
    //String tablename = "publicboard";
    //String sql;
    //SQLiteDatabase db;
    //Cursor resultset;
    String userId = "yerin";

    String str_input1, str_input2, str_input3;
    String name = "yerin";

    TextView testtext;
    Button check;
    ListView listView;
    MyListAdapter myListAdapter;

    ArrayList<list_item> list_itemArrayList;
    private SimpleAdapter adapter = null;
    private List<HashMap<String, String>> infoList = null;
    private List<HashMap<String, String>> pushList = null;
    //ArrayAdapter<String> adapter;
    //ArrayList<list_item> list_itemArrayList;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.board_list), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_write_page:
                //글 작성 페이지로 이동 항목이 선택되었음
                Intent intent1 = new Intent(BoardActivity_public_board.this, BoardActivity_public_boardEditActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        //setContentView(R.layout.test);
        //testtext = (TextView) findViewById(R.id.textView14);

        //new JSONTask().execute("http://172.30.1.57:3000/select/publicboard");


        listView = (ListView) findViewById(R.id.my_listview);
        list_itemArrayList = new ArrayList<list_item>();
        infoList = new ArrayList<HashMap<String, String>>();
        pushList = new ArrayList<HashMap<String, String>>();

        String[] from = new String[]{"nickname", "title", "date"};
        int[] to = new int[]{R.id.nickname_textView, R.id.title_textView, R.id.date_textView};
        HashMap<String, String> map;
        //myListAdapter = new MyListAdapter(BoardActivity_public_board.this, list_itemArrayList);
        //myListAdapter = new ArrayAdapter<String>(this,list_itemArrayList);


        //new JSONTask().execute("http://172.16.32.92:3000/select/publicboard");

        System.out.println(infoList.size() + "dfdfd");
        //infoList.add(new HashMap<String, String>("nickname","fdd"),new HashMap<String, String>())


        progressDialog = new ProgressDialog(BoardActivity_public_board.this);
        progressDialog.setMessage("plz wait");
        progressDialog.show();

        getJSON();
        adapter = new SimpleAdapter(this, infoList, R.layout.item, from, to);
        listView.setAdapter(adapter);
        //myListAdapter = new MyListAdapter(BoardActivity_public_board.this, list_itemArrayList);
        //listView.setAdapter(myListAdapter);
        //progressDialog.hide();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //ArrayList에 position에 해당하는 아이템과 리스트뷰에  각 Item의 Position에 있는 것이
                //같기 때문에 이 Position 파라미터를 이용하면 된다.
                Intent intent = new Intent(BoardActivity_public_board.this, FreeboardDetailActivity.class);
                intent.putExtra("nickname", pushList.get(i).get("nickname"));
                intent.putExtra("title", pushList.get(i).get("title"));
                intent.putExtra("detail", pushList.get(i).get("detail"));
                intent.putExtra("date", pushList.get(i).get("date"));
                //intent.putExtra("nickImage" , list_itemArrayList.get(i).getProfile_image());

                startActivity(intent);

            }
        });
        System.out.println("완성!!");
//        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
//
//        sql = "create table if not exists "+tablename+" (name varchar(20), title varchar(40), content varchar(40) , datetime varchar(50));";
//        db.execSQL(sql);
////
//        listView = (ListView) findViewById(R.id.my_listview);
//
//        list_itemArrayList = new ArrayList<list_item>();
//
//
//
//        //db 검색하기
//        try {
//            sql ="select * from " + tablename;
//            resultset = db.rawQuery(sql, null);
//
//            int count = resultset.getCount();
//
//            for(int i=0; i<count; i++) {
//                resultset.moveToNext();
//                String str_name = resultset.getString(0);
//                String str_title = resultset.getString(1);
//                Date date = new Date(System.currentTimeMillis());
//                String str_content = resultset.getString(2);
//
//                list_itemArrayList.add(new list_item(R.mipmap.ic_launcher,str_name, str_title,date, str_content));
//            }
//
//            myListAdapter = new MyListAdapter(BoardActivity_public_board.this, list_itemArrayList);
//            listView.setAdapter(myListAdapter);
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    //ArrayList에 position에 해당하는 아이템과 리스트뷰에  각 Item의 Position에 있는 것이
//                    //같기 때문에 이 Position 파라미터를 이용하면 된다.
//                    Intent intent = new Intent(BoardActivity_public_board.this, FreeboardDetailActivity.class);
//                    intent.putExtra("nickname",list_itemArrayList.get(i).getNickname());
//                    intent.putExtra("title", list_itemArrayList.get(i).getTitle());
//                    intent.putExtra("content", list_itemArrayList.get(i).getContent());
//                    intent.putExtra("date", list_itemArrayList.get(i).getWrite_date());
//                    intent.putExtra("nickImage" , list_itemArrayList.get(i).getProfile_image());
//
//                    startActivity(intent);
//
//                }
//            });
//            System.out.println("완성!!");
//        } catch(Exception e) {
//            System.out.println("select Error" + e);
//        }
    }


    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<BoardActivity_public_board> weakReference;

        public MyHandler(BoardActivity_public_board activity) {
            weakReference = new WeakReference<BoardActivity_public_board>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BoardActivity_public_board activity = weakReference.get();

            if (activity != null) {
                switch (msg.what) {
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
                    URL url = new URL("http://172.16.14.235:3000/select/publicboard");
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

        //String [] arraysum = new String[3];

        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("data");
            for (int a = 0; a < jarray.length(); a++) {
                //HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(a);

                title = jObject.optString("title");
                nickname = jObject.optString("nickname");
                date = jObject.optString("date");
                content = jObject.optString("detail");

                HashMap<String, String> infoMap = new HashMap<String, String>();
                HashMap<String, String> pushMap = new HashMap<String, String>();

                infoMap.put("nickname", nickname);
                infoMap.put("title", title);
                infoMap.put("date", date);

                pushMap.put("nickname", nickname);
                pushMap.put("title", title);
                pushMap.put("date", date);
                pushMap.put("detail", content);

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