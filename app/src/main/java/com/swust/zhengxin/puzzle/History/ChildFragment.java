package com.swust.zhengxin.puzzle.History;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swust.zhengxin.puzzle.Data.HistoryDataManager;
import com.swust.zhengxin.puzzle.R;

import java.util.ArrayList;


public class ChildFragment extends Fragment {

    private ImageView mPic;
    private TextView mStep;
    private TextView mDate;
    private TextView mTime;
    private TextView mMode;

    private String key=null;
    private String diff;
    private ListView listView;//下滑列表控件
    private MyListViewAdapter adapter;//适配器绑定数据
    private HistoryDataManager historyDataManager;
    private ArrayList<HistoryList> listData;
    private Cursor cursor;

    public static ChildFragment newInstance(String s){
        ChildFragment childFragment=new ChildFragment();
        Bundle bundle =new Bundle();
        bundle.putString("key",s);
        childFragment.setArguments(bundle);

        return childFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Bundle bundle=getArguments();
        key= bundle!=null ? bundle.getString("key") : null;

        super.onCreate(savedInstanceState);
        if (historyDataManager == null) {
            historyDataManager=new HistoryDataManager(getActivity());
            historyDataManager.openDataBase();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        mPic=rootView.findViewById(R.id.iv_headPortrait);
        mStep=rootView.findViewById(R.id.tv_step);
        mDate=rootView.findViewById(R.id.tv_date);
        mTime=rootView.findViewById(R.id.tv_time);
        mMode=rootView.findViewById(R.id.tv_mode);
        listView=rootView.findViewById(R.id.lv_history);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listData=new ArrayList<HistoryList>();
        historyDataManager.openDataBase();

        if (key.equals(Content.S3)) {
            diff="1";
        } else if (key.equals(Content.S4)) {
            diff="2";
        } else if (key.equals(Content.S5)) {
            diff="3";
        } else if (key.equals(Content.S6)) {
            diff="4";
        }

        cursor=historyDataManager.fetchUserHistory(diff);
        while (cursor.moveToNext()) {
            HistoryList historyList=new HistoryList();
            historyList.setStep(cursor.getString(1));
            historyList.setDate(cursor.getString(2));
            historyList.setTime(cursor.getString(3));
            historyList.setMode(cursor.getString(5));
            byte[] blob=cursor.getBlob(7);//将图片转为二进制位图存入记录数据库

            Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
            historyList.setBitmap(bmp);
            listData.add(historyList);
        }
        adapter=new MyListViewAdapter(this.getActivity(),listData);
        listView.setAdapter(adapter);   //ListView列表绑定适配器
        adapter.notifyDataSetChanged(); //动态更新列表
    }

}


