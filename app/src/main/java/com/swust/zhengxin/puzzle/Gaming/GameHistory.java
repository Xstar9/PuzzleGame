package com.swust.zhengxin.puzzle.Gaming;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.swust.zhengxin.puzzle.History.ChildFragment;
import com.swust.zhengxin.puzzle.History.Content;
import com.swust.zhengxin.puzzle.History.TabNavitationLayout;
import com.swust.zhengxin.puzzle.History.ViewPagerAdapter;
import com.swust.zhengxin.puzzle.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class GameHistory extends FragmentActivity {

    private TabNavitationLayout tabNavitationLayout;
    private ViewPager viewPager; //滑动切换
    private ViewPagerAdapter viewPagerAdapter;

    private List<Fragment> fragments;   //历史记录界面子标题活动
    private String[] titles = new String[]{"3X3", "4X4", "5X5", "6X6"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history);
        GameMenu.activityList.add(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("res");

        tabNavitationLayout= findViewById(R.id.bar);
        viewPager= findViewById(R.id.viewpager);

        Fragment S3= ChildFragment.newInstance(Content.S3);
        Fragment S4=ChildFragment.newInstance(Content.S4);
        Fragment S5=ChildFragment.newInstance(Content.S5);
        Fragment S6= ChildFragment.newInstance(Content.S6);

        fragments=new ArrayList<>();
        fragments.add(S3);
        fragments.add(S4);
        fragments.add(S5);
        fragments.add(S6);

        //绑定适配器
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),fragments,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabNavitationLayout.setViewPager(this,titles,viewPager,
                R.drawable.drawable_left,R.drawable.drawable_mid,R.drawable.drawable_right,
                R.color.color_ffffff,R.color.color_282d31,16,0,1f,true);


    }
}


