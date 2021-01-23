package com.swust.zhengxin.puzzle.History;

import android.graphics.Bitmap;

public class HistoryList {
    //每个历史记录的属性
    private String time;
    private String date;
    private String step;
    private String mode;
    private Bitmap bitmap;


    public String getTime(){
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStep() {
        return step;
    }

    public String getMode() {
        return mode;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}

