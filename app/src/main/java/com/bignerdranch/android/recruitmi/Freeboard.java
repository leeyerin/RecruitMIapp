package com.bignerdranch.android.recruitmi;

import java.util.Date;
import java.util.UUID;

/**
 * Created by 이예린 on 2018-05-28.
 */

public class Freeboard {
    private UUID mId;
    private String mTitle;
    private int mRipple = 0;
    private int mGoods = 0;


    public Freeboard() {
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmRipple() {
        return mRipple;
    }

    public void setmRipple(int mRipple) {
        this.mRipple = mRipple;
    }

    public int getmGoods() {
        return mGoods;
    }

    public void setmGoods(int mGoods) {
        this.mGoods = mGoods;
    }
}
