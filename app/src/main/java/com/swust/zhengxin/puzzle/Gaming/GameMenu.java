package com.swust.zhengxin.puzzle.Gaming;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.swust.zhengxin.puzzle.Dialog.SelectDialog;
import com.swust.zhengxin.puzzle.R;


import java.util.LinkedList;
import java.util.List;


public class GameMenu extends AppCompatActivity {

    private SelectDialog selectImageDialog;
    private int MusicFlag = 0;
    private String name;
    private int MusicFlag1=0;
    private Cursor cursor;
    int flag;
    private String[] diff={"3","4","5","6"};
    private String[] mode={"Easy","Normal","Export"};
    private AlertDialog.Builder set_dialog;
    public static List<Activity> activityList = new LinkedList<>();

    Button btn_Start, btn_history, btn_Set, btn_About, btn_Quit;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        GameMenu.activityList.add(this);

        //获取Intent
        Intent intent=getIntent();
        //从intent取出bundle
        final Bundle bundle=intent.getBundleExtra("res");

        //按钮获取id
        btn_Start = findViewById(R.id.GameStart);
        btn_history = findViewById(R.id.GameHistory);
        btn_Set = findViewById(R.id.GameSet);
        btn_About = findViewById(R.id.GameAbout);
        btn_Quit = findViewById(R.id.GameQuit);

        if (MusicFlag==0){
            MusicStart();
        }

        /**
        *   为按钮绑定事件
         **/
        //开始游戏按钮
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SelectPicture();
            }
        });

        //历史记录按钮
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameMenu.this, GameHistory.class);
                //bundle.putString("name",name);
                intent.putExtra("res",bundle);
                startActivity(intent);
            }
        });

        btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框设置音乐
                showPopMenu(v);

            }
        });

        //退出游戏按钮
        btn_Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MusicStop();
                exit();
            }
        });

    }

    //退出程序
    public void exit() {
        for (Activity act : activityList) {
            act.finish();
        }
        System.exit(0);
    }

    //点击游戏介绍按钮弹出对话框
    public void showDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("游戏介绍");
        builder.setMessage("简介:这是一个简单愉快的拼图小游戏."+"\n"+"操作介绍:点击开始游戏,选择图片后即可进入游戏."
                +"点击白色拼图碎片的上、下、左、右四个方向的拼图碎片后,白色拼图碎片和点击选中的拼图碎片会自动交换."+"\n"
                +"游戏规则:游戏开始时,计时器会开始计时.将碎片还原成左下角的图片即可获得游戏胜利."+"\n"
                +"系统会保存您的游戏记录(步数,日期,用时,模式)."+"\n"
                +"游戏别名:娱乐拼图" + "\n"+"Author: ZhengXin" + "\n" + "School: SWUST" + "\n" + "Version: v1.0");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //音乐设置弹出选择栏
    public void showPopMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.getMenuInflater().inflate(R.menu.game_setting, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.turn_on:
                        MusicStart();
                        Toast.makeText(GameMenu.this, "音乐已打开~", Toast.LENGTH_SHORT).show();
                        MusicFlag = 0;
                        break;

                    case R.id.turn_off:
                        MusicStop();
                        Toast.makeText(GameMenu.this, "音乐已关闭~", Toast.LENGTH_SHORT).show();
                        MusicFlag = 1;
                        break;

                    case R.id.default_set:
                        MusicStart();
                        Toast.makeText(GameMenu.this, "音乐已恢复默认设置~", Toast.LENGTH_SHORT).show();
                        MusicFlag = 1;
                        break;

                }
                return true;
            }
        });

        menu.show();
    }

    public void MusicStart() {
        Intent intent = new Intent(this, BackgroundMusic.class);
        startService(intent);
    }

    public void MusicStop() {
        Intent intent = new Intent(this, BackgroundMusic.class);
        stopService(intent);
    }

    private void SelectPicture() {
        CharSequence[] items = { "拍照","默认图片"};

        // 弹出对话框提示用户选择图片
        new AlertDialog.Builder(GameMenu.this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = 1;
                        switch (which) {
                            case 0:
                               Toast.makeText(GameMenu.this,"功能正在探索中，敬请期待哦~！",Toast.LENGTH_SHORT).show();
                                break;

                            //内置图片
                            case 1:
                                if (selectImageDialog == null) {
                                    selectImageDialog = new SelectDialog();
                                    selectImageDialog.addItemClickListener(new SelectDialog.OnItemClickListener() {
                                        @Override
                                        public void itemClick(int position, int res) {
                                            //更新布局
                                            // 实例化一个Bundle
                                            Bundle bundle = new Bundle();
                                            // 实例化一个intent
                                            Intent intent2 = new Intent(GameMenu.this, MainMenu.class);
                                            // 获取输入的数据,把数据保存到Bundle里
                                            bundle.putInt("res_default", res);
                                            bundle.putInt("changeImgFlag", 0);
                                            //bundle.putString("name",name);
                                            // 把bundle放入intent里
                                            intent2.putExtra("res", bundle);
                                            startActivity(intent2);
                                            finish();

                                        }
                                    });
                                }

                                selectImageDialog.showDialog(getSupportFragmentManager(), "dialog", 0);
                        }
                    }
                }).show();
    }

    /**
     *  回调函数
     * 调用startActivityForResult方法启动一个intent后，
     * 可以在该方法中拿到返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

}











