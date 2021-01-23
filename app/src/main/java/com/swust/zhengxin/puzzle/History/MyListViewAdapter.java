package com.swust.zhengxin.puzzle.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import com.swust.zhengxin.puzzle.R;
import java.util.ArrayList;


/**
 *      自定义适应历史记录布局的适配器
 */
public class MyListViewAdapter extends BaseAdapter {

    ArrayList<HistoryList> listData;
    LayoutInflater inflater;//xml解析器
    FragmentActivity activity;

    public MyListViewAdapter(FragmentActivity fragmentActivity, ArrayList<HistoryList>listData) {
        // TODO Auto-generated constructor stub
        this.listData = listData;
        this.activity = fragmentActivity;
        inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//解析xml,将其事例化
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size();
    }

    @Override
    public Object getItem(int arg) {
        // TODO Auto-generated method stub
        return listData.get(arg);
    }

    @Override
    public long getItemId(int arg) {
        // TODO Auto-generated method stub
        return arg;
    }

    //ViewHolder:容纳ListView，快速设置相应记录值
    class ViewHolder {
        TextView tv_step;//步数记录
        TextView tv_date;//记录日期
        TextView tv_time;//记录时间
        TextView tv_mode;//记录模式
        ImageView im_img;//记录成功图片
    }

    @Override
    public View getView(int arg, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder= null;//初始化

        if (view == null) {
            //绑定布局以及属性
            view = inflater.inflate(R.layout.history_record, null);
            viewHolder=new ViewHolder();
            viewHolder.im_img=view.findViewById(R.id.iv_headPortrait);
            viewHolder.tv_step=view.findViewById(R.id.tv_step);
            viewHolder.tv_date=view.findViewById(R.id.tv_date);
            viewHolder.tv_time=view.findViewById(R.id.tv_time);
            viewHolder.tv_mode=view.findViewById(R.id.tv_mode);
            view.setTag(viewHolder);
        }
        viewHolder=(ViewHolder)view.getTag();
        HistoryList model=listData.get(arg);
        viewHolder.tv_step.setText(model.getStep());
        viewHolder.tv_date.setText(model.getDate());
        viewHolder.tv_time.setText(model.getTime());
        viewHolder.tv_mode.setText(model.getMode());
        viewHolder.im_img.setImageBitmap(model.getBitmap());

        return view;
    }

}



