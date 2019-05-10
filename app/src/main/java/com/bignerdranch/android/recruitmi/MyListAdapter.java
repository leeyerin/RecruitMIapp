package com.bignerdranch.android.recruitmi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 이예린 on 2018-06-05.
 */

public class MyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<list_item> list_itemArrayList;

    TextView nickname_textView;
    TextView title_textView;
    TextView date_textView;
    TextView content_textVIew;
    ImageView profile_imageView;

    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    /*
    getCount() : 이 리스트뷰가 몇개의 아이템을 가지고 있는지를 알려주는 함수
    우리는 arrayList의 size만큼 가지고 있음
    * */
    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    /*
    getItem() : 현재 어떤 아이템인지를 알려주는 부분
    arrayList에 저장되어 이쓴 객체 중 position에 해당하는 것을 가져올 것
     */
    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    /*
    * getItemId()  : 현재 어떤 포지션인지를 알려주는 부분
    * */
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        리스트 뷰에서 아이템과 xml을 연결하여 화면에 표시해주는 가장 중요한 부분
        getView 부분에서 반복문이 실행된다고 이해하면 된다.
         */
        if(convertView == null) {
            //LayoutInflater 클래스를 이용하면 다른 클래스에서도 xml을 가져올 수 있음
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            nickname_textView = (TextView)convertView.findViewById(R.id.nickname_textView);
            //content_textVIew = (TextView)convertView.findViewById(R.id.content_textView);
            date_textView = (TextView)convertView.findViewById(R.id.date_textView);
            title_textView = (TextView)convertView.findViewById(R.id.title_textView);
            //profile_imageView = (ImageView)convertView.findViewById(R.id.profile_imageView);

        }
        //내용입력
        nickname_textView.setText(list_itemArrayList.get(position).getNickname());
        title_textView.setText(list_itemArrayList.get(position).getTitle());
        date_textView.setText(list_itemArrayList.get(position).getWrite_date().toString());
//        content_textVIew.setText(list_itemArrayList.get(position).getContent());
//        profile_imageView.setImageResource(list_itemArrayList.get(position).getProfile_image());


        return convertView;
    }
}
