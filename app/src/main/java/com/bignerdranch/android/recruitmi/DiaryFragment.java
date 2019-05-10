package com.bignerdranch.android.recruitmi;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 이예린 on 2018-05-14.
 */

public class DiaryFragment extends Fragment{

    private static final String TAG = "public_freeboard_search";
    private String PER_PAGE = "&per_page=50";
    public static final int LOAD_SUCCESS = 101;
    private static ProgressDialog progressDialog;
    TextView titleView;
    TextView contentView;
    TextView dateView;

    ImageButton write;

    ListView listView;
    MyListAdapter myListAdapter;

    ArrayList<list_diary_item> list_itemArrayList;
    private SimpleAdapter adapter = null;
    private List<HashMap<String, String>> infoList = null;
    public DiaryFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        //View v = inflater.inflate(R.layout.activity_list_view, container, false);

        listView = (ListView) v.findViewById(R.id.my_diary_listview);
        //list_itemArrayList = new ArrayList<list_item>();
        list_itemArrayList = new ArrayList<list_diary_item>();
        infoList = new ArrayList<HashMap<String, String>>();

        String[] from = new String[]{"date", "title", "detail"};
        int[] to = new int[]{R.id.textView19, R.id.textView20, R.id.textView21};

                progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("plz wait");
        progressDialog.show();

        getJSON();
        System.out.println("들어옴!");
        adapter = new SimpleAdapter(getActivity(), infoList, R.layout.diaryitem, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //ArrayList에 position에 해당하는 아이템과 리스트뷰에  각 Item의 Position에 있는 것이
                //같기 때문에 이 Position 파라미터를 이용하면 된다.
                Intent intent = new Intent(getActivity(), FreeboardDetailActivity.class);
                //.intent.putExtra("nickname", infoList.get(i).get("nickname"));
                intent.putExtra("title", infoList.get(i).get("title"));
                intent.putExtra("detail", infoList.get(i).get("detail"));
                intent.putExtra("date", infoList.get(i).get("date"));
                //intent.putExtra("nickImage" , list_itemArrayList.get(i).getProfile_image());

                startActivity(intent);

            }
        });
/*
        Bundle bundle = getArguments();
        if(!bundle.isEmpty()) {
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            String date = bundle.getString("date");



            titleView = v.findViewById(R.id.textView8);
            contentView = v.findViewById(R.id.textView9);
            dateView = v.findViewById(R.id.textView4);

            titleView.setText(title);
            contentView.setText(content);
            dateView.setText(date);

        }
*/
        write = v.findViewById(R.id.write_diary);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Write_Diary.class);
                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<DiaryFragment> weakReference;

        public MyHandler(DiaryFragment activity) {
            weakReference = new WeakReference<DiaryFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DiaryFragment activity = weakReference.get();

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
                    URL url = new URL("http://172.20.10.4:3000/select/diary");
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
                    System.out.println("여기서 에러가 나나");
                    result = sb.toString().trim();
                    System.out.print(result);
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
        //String nickname = null;
        String date = null;
        String content = null;

        //String [] arraysum = new String[3];

        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("data");
            for (int a = 0; a < jarray.length(); a++) {
                //HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(a);

                title = jObject.optString("title");
                //nickname = jObject.optString("nickname");
                date = jObject.optString("date");
                content = jObject.optString("detail");

                HashMap<String, String> infoMap = new HashMap<String, String>();
                HashMap<String, String> pushMap = new HashMap<String, String>();


                //infoMap.put("nickname", nickname);
                infoMap.put("title", title);
                infoMap.put("date", date);
                infoMap.put("detail", content);

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
