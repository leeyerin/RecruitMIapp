package com.bignerdranch.android.recruitmi;

import java.util.Date;

/**
 * Created by 이예린 on 2018-06-05.
 */

public class list_item {

    private String nickname;
    private String title;
    //private Date write_date;
    private String write_date;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }


    public list_item(String title, String nickname,String date)  {
        this.write_date = date;
        this.nickname = nickname;
        this.title = title;
    }

//    public list_item(int profile_image, String nickname, String title, Date write_date, String content) {
//        this.profile_image = profile_image;
//        this.nickname = nickname;
//        this.title = title;
//        this.write_date = write_date;
//        this.content = content;
//    }


}
