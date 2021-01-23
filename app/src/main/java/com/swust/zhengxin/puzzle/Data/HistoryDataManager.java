package com.swust.zhengxin.puzzle.Data;


import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class HistoryDataManager {
    private static final String TAG = "HistoryDataManager";//数据库标签
    private static final String DB_NAME = "user_history";//历史记录数据库名
    private static final String TABLE_NAME = "History";//数据库表名
   //数据库记录的数据
    public static final String STEP = "history_step";
    public static final String DATE = "history_date";
    public static final String TIME = "history_time";
    public static final String DIFF = "history_diff";
    public static final String MODE = "history_mode";
    public static final String PHOTO = "history_photo";

    private String step,date,time,diff,mode;
    private static final int DB_VERSION = 2;//版本
    private static Context mContext = null;

    //创建历史记录信息表
    private static final String DB_CREATE="CREATE TABLE " + TABLE_NAME + " ("
            +  STEP + " varchar," + DATE + " varchar," + TIME +" varchar,"
            + DIFF + " varchar," + MODE + " varchar," + PHOTO + " BLOB" + ");";

    //定义数据库对象
    private SQLiteDatabase sqLiteDatabase=null;
    private HistoryDBHelper historyDBHelper=null;
    //继承自SQLiteHelper
    private static class HistoryDBHelper extends SQLiteOpenHelper{

        HistoryDBHelper(Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");//备份前若表存在先删除表，在备份；若不存在，则直接备份创建
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "HistoryDBHelper OnUpgrade");
            onCreate(db);
        }

    }

    public HistoryDataManager(Context context){
        mContext=context;
        Log.i(TAG,"HistoryDataManager construction!");
    }

    //打开数据库
    public void openDataBase() throws SQLException {
        historyDBHelper=new HistoryDBHelper(mContext);
        sqLiteDatabase=historyDBHelper.getWritableDatabase();
    }

    //关闭数据库
    public void closeDataBase() throws SQLException{
        historyDBHelper.close();
    }

    //添加数据
    public long insertHistoryData(HistoryData historyData){
        step = historyData.getHistoryStep();
        date = historyData.getHistoryDate();
        time = historyData.getHistoryTime();
        diff = historyData.getHistoryDiff();
        mode = historyData.getHistoryMode();

        byte[] photo=SaveBitmap(historyData.getHistoryPhoto());//图片二进制

        ContentValues values = new ContentValues();
        values.put(STEP,step);
        values.put(DATE,date);
        values.put(TIME,time);
        values.put(DIFF,diff);
        values.put(MODE,mode);;
        values.put(PHOTO,photo);
        Log.i(TAG, "数据库添加："+step+date+time+diff+mode+"照片："+photo);

        return sqLiteDatabase.insert(TABLE_NAME,null,values);
    }

    //获取用户历史纪录
    public Cursor fetchUserHistory(String diff){
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME,null,DIFF+"='"+diff+"'",
                null,null,null," cast(history_time as '9999')"); //cast(history_time as '9999')" 转换时间类型
        if (cursor == null) {
            Log.i(TAG, "当前数据库无数据！");
        }

        return cursor;
    }

    /**
     * Bitmap.CompressFormat.JPEG 与.PNG
     * JPEG是有损数据图像，PNG使用从LZ77算法派生的无损数据压缩算法。
     * 压缩本地图片方法：.compress() ，将Bitmap压缩成指定格式和质量的输出流
     * 将图片压缩存入数据库
     */
    public byte[] SaveBitmap(Bitmap bitmap){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);

        return os.toByteArray();
    }
}

