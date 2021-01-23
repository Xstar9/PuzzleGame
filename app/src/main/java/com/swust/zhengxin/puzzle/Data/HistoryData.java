package com.swust.zhengxin.puzzle.Data;

import android.graphics.Bitmap;

public class HistoryData {
    private String historyStep;//记录步数
    private String historyDate;//记录日期
    private String historyTime;//记录时间
    private String historyMode;//历史模式
    private String historyDiff;//历史关卡
    private Bitmap historyPhoto;//历史游戏图片

    /**
     * 获取历史记录信息
     */
    public String getHistoryStep() {
        return historyStep;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public String getHistoryMode() {
        return historyMode;
    }

    public String getHistoryDiff() {
        return historyDiff;
    }

    public Bitmap getHistoryPhoto() {
        return historyPhoto;
    }
    //历史数据记录
    public HistoryData(String historyStep, String historyDate, String historyTime, String historyMode, String historyDiff,Bitmap historyPhoto){
        super();

        this.historyStep=historyStep;
        this.historyDate=historyDate;
        this.historyTime=historyTime;
        this.historyMode=historyMode;
        this.historyDiff=historyDiff;
        this.historyPhoto=historyPhoto;
    }

}

