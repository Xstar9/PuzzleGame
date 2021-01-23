package com.swust.zhengxin.puzzle.History;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 历史记录界面滑动导航效果
 */
public class TabNavitationLayout extends RelativeLayout{

    private TextView[] textViews; // 标题栏数组，用于存储要显示的标题
    private LinearLayout titleLayout; //标题栏父控件
    private ViewPager viewPager;

    private OnTitleClickListener onTitleClickListener;
    private OnNaPageChangeListener onNaPageChangeListener;


    public TabNavitationLayout(Context context) {
        this(context, null);
    }
    public TabNavitationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TabNavitationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        titleLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        titleLayout.setLayoutParams(layoutParams);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(titleLayout);
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public void setOnNaPageChangeListener(OnNaPageChangeListener onNaPageChangeListener) {
        this.onNaPageChangeListener = onNaPageChangeListener;
    }

    /**
     *
     * @param context 上下文
     * @param titles 标题
     * @param viewPager
     * @param leftdrawable 最左背景
     * @param middrawable 中间背景
     * @param rightdrawable 最右背景
     * @param txtUnselectecolor 没选中时的字体颜色
     * @param txtSelectedcolor 选择时字体颜色
     * @param textsize 字体大小
     * @param currentPosition 当前标题位置
     * @param borderwidth 边框宽度
     * @param smoothScroll 点击标题是否滑动效果
     */
    public void setViewPager(final Context context, String[] titles, ViewPager viewPager,
                             int leftdrawable, int middrawable, int rightdrawable,
                             final int txtUnselectecolor, final int txtSelectedcolor,
                             int textsize, int currentPosition, float borderwidth, boolean smoothScroll)
    {
        if(titles == null || titles.length == 1)
        {
            Toast.makeText(context, "至少需要2个标题", Toast.LENGTH_SHORT).show();
            return;
        }
        this.viewPager = viewPager;
        setTitles(context, titles, leftdrawable, middrawable, rightdrawable, textsize, borderwidth, smoothScroll);
        setSelectedTxtColor(context, txtSelectedcolor, txtUnselectecolor, currentPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(onNaPageChangeListener != null)
                {
                    onNaPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTxtColor(context, txtSelectedcolor, txtUnselectecolor, position);
                if(onNaPageChangeListener != null)
                {
                    onNaPageChangeListener.onPageSelected(position);
                }
            }

            //轮播滑动
            @Override
            public void onPageScrollStateChanged(int state) {
                if(onNaPageChangeListener != null)
                {
                    onNaPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    //设置各个难度记录标题栏
    private void setTitles(Context context, String[] titles, int leftdrawable, int middrawable, int rightdrawable,
                           int textsize, float borderwidth, final boolean smoothScroll)
    {
        int length = titles.length;
        this.textViews = new TextView[titles.length];
        // 根据标题栏动态生成TextView显示标题，每个标题栏的宽度比为1:1,其中的内容居中。
        for(int i = 0; i < length; i++)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            final int index = i;
            TextView textView = new TextView(context);
            textView.setText(titles[i]);
            textView.setTextSize(textsize);
            textView.setGravity(Gravity.CENTER);
            textViews[i] = textView;
            textViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(index, smoothScroll);
                    if(onTitleClickListener != null)
                    {
                        onTitleClickListener.onTitleClick(v);
                    }
                }
            });

            if(i == 0)
            {
                textView.setBackground(context.getResources().getDrawable(leftdrawable));
                params.setMargins(0, 0, 0, 0);
            }
            else if(i == length - 1)
            {
                textView.setBackground(context.getResources().getDrawable(rightdrawable));
                params.setMargins(-diptopx(context, borderwidth), 0, 0, 0);
            }
            else
            {
                textView.setBackground(context.getResources().getDrawable(middrawable));
                params.setMargins(-diptopx(context, borderwidth), 0, 0, 0);
            }

            titleLayout.addView(textView, params);
        }
    }

    private void setUnselectedTxtColor(Context context, int unselectedcolor, int unselectedsize)
    {
        if(textViews != null)
        {
            int length = textViews.length;
            for(int i = 0; i < length; i++)
            {
                textViews[i].setTextColor(context.getResources().getColor(unselectedcolor));
                textViews[i].setTextSize(unselectedsize);
            }
        }
    }

    private void setSelectedTxtColor(Context context, int selectedcolor, int unselectedColor, int position)
    {
        if(textViews != null)
        {
            int length = textViews.length;
            for(int i = 0; i < length; i++)
            {
                if(i == position) {
                    textViews[i].setTextColor(context.getResources().getColor(selectedcolor));
                    textViews[i].setSelected(true);
                }
                else
                {
                    textViews[i].setTextColor(context.getResources().getColor(unselectedColor));
                    textViews[i].setSelected(false);
                }
            }
        }
    }


    /**
     * 获取设备屏幕宽度
     *
     */
    private static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int diptopx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (dipValue * scale + 0.5f);
    }

    /**
     * 点击标题栏事件监听器接口
     */
    public interface OnTitleClickListener
    {
        void onTitleClick(View v);
    }

    /**
     * 屏幕触摸滑动事件监听器接口
     */
    public interface OnNaPageChangeListener
    {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

}


