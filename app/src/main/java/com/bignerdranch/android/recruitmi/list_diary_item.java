package com.bignerdranch.android.recruitmi;

/**
 * Created by 이예린 on 2018-11-15.
 */

public class list_diary_item {
    private String date;
    private String diarytitle;
    private String diarydetail;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiarytitle() {
        return diarytitle;
    }

    public void setDiarytitle(String diarytitle) {
        this.diarytitle = diarytitle;
    }

    public String getDiarydetail() {
        return diarydetail;
    }

    public void setDiarydetail(String diarydetail) {
        this.diarydetail = diarydetail;
    }

    public list_diary_item(String date, String diarytitle, String diarydetail) {
        this.date = date;
        this.diarytitle = diarytitle;
        this.diarydetail = diarydetail;
    }
}
