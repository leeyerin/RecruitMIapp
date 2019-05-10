package com.bignerdranch.android.recruitmi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

public class BoardMenuActivity extends AppCompatActivity {

    private static final String TAG = "public_freeboard_search";
    private String PER_PAGE = "&per_page=50";
    public static final int LOAD_SUCCESS = 101;
    private static ProgressDialog progressDialog;

    public EditText textTitle;
    public EditText textContent;
    public Button insertButton;

    String str_input1, str_input2, str_input3;
    String name ="yerin";


    TextView testtext;
    Button check;
    ListView listView;
    MyListAdapter myListAdapter;

    ArrayList<list_recruit_item> list_itemArrayList;
    private SimpleAdapter adapter = null;
    private List<HashMap<String, String>> infoList = null;

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
                //Intent intent1 = new Intent(BoardMenuActivity.this,BoardMenuEditActivity.class);
                //startActivity(intent1);
                getJSON("http://172.20.10.4:3000/select/crawl/manager");
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
        list_itemArrayList = new ArrayList<list_recruit_item>();
        infoList = new ArrayList<HashMap<String, String>>();

        String[] from = new String[]{"title"};
        int[] to = new int[]{R.id.recruittitle};
        HashMap<String, String> map;

        progressDialog = new ProgressDialog(BoardMenuActivity.this);
        progressDialog.setMessage("plz wait");
        progressDialog.show();

        getJSON("http://172.20.10.4:3000/select/crawl");
        adapter = new SimpleAdapter(this, infoList, R.layout.recruititem, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //ArrayList에 position에 해당하는 아이템과 리스트뷰에  각 Item의 Position에 있는 것이
                //같기 때문에 이 Position 파라미터를 이용하면 된다.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoList.get(i).get("url")));
//                Intent intent = new Intent(BoardActivity_public_board.this, FreeboardDetailActivity.class);
//                intent.putExtra("nickname", pushList.get(i).get("nickname"));
//                intent.putExtra("title", pushList.get(i).get("title"));
//                intent.putExtra("detail", pushList.get(i).get("detail"));
//                intent.putExtra("date", pushList.get(i).get("date"));
                //intent.putExtra("nickImage" , list_itemArrayList.get(i).getProfile_image());

                startActivity(intent);

            }
        });
    }

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<BoardMenuActivity> weakReference;

        public MyHandler(BoardMenuActivity activity) {
            weakReference = new WeakReference<BoardMenuActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BoardMenuActivity activity = weakReference.get();

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

    public void getJSON(final String sdd) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                try {


                    URL url = new URL(sdd);
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
                System.out.println(result+";;;;");
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
        String url = null;

        //String [] arraysum = new String[3];

        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("data");
            for (int a = 0; a < jarray.length(); a++) {
                //HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(a);

                title = jObject.optString("title");
                url = jObject.optString("url");

                HashMap<String, String> infoMap = new HashMap<String, String>();

                infoMap.put("title", title);
                infoMap.put("url", url);

                infoList.add(infoMap);
                System.out.println(infoMap);

            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return false;

    }
}
