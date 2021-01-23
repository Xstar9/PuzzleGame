package com.swust.zhengxin.puzzle.Gaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.swust.zhengxin.puzzle.Data.HistoryData;
import com.swust.zhengxin.puzzle.Data.HistoryDataManager;
import com.swust.zhengxin.puzzle.Dialog.SelectDialog;
import com.swust.zhengxin.puzzle.Dialog.SuccessDialog;
import com.swust.zhengxin.puzzle.Game.GameAction;
import com.swust.zhengxin.puzzle.R;
import com.swust.zhengxin.puzzle.UI.PuzzleView;
import com.swust.zhengxin.puzzle.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainMenu extends AppCompatActivity implements GameAction.GameStateListener {

    private PuzzleView puzzleLayout;//自定义拼图界面布局
    private GameAction puzzleGame;
    private ImageView SrcImg;//原图
    private Spinner spinner; //切换模式栏
    private TextView tv_Level;//当前阶级
    private Chronometer timer;//计时器
    private ImageView backImg;//返回键
    private ImageView musicImg;//音乐开关
    public static boolean isPlay = true;//计时器正在计时
    private ImageButton pause;//暂停
    private int res;
    private int MusicFlag=0;
    private boolean set_music=true;
    private int set_level;//改变阶级
    private String set_mode;//改变模式

    private String mode="普通模式";//预置模式
    private int changeImgFlag;//切换图片标记
    private Bitmap bitmap;//游戏切割位图
    private SharedPreferences SP_history;
    private HistoryDataManager historyDataManager;//游戏记录数据库
    private SelectDialog selectImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_puzzle_game);
        GameMenu.activityList.add(this);

        //初始化
        initView();
        initListener();

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainMenu.this,GameMenu.class);
                Bundle bundle=new Bundle();
               // bundle.putString("name",name);
                bundle.putInt("MusicFlag",MusicFlag);
                intent.putExtra("res",bundle);
                startActivity(intent);
                finish();
            }
        });
        musicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置音乐
                showPopMenu(v);
            }
        });

        //建立游戏记录数据库
        if (historyDataManager == null) {
            historyDataManager=new HistoryDataManager(this);
            historyDataManager.openDataBase();
        }


        //计时器开关键
        pause.setOnClickListener(new View.OnClickListener() {
            long rangeTime=0;
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    ((ImageButton) v).setBackgroundResource(R.drawable.play);
                    rangeTime= SystemClock.elapsedRealtime()-timer.getBase();
                    timer.stop();
                    Toast.makeText(MainMenu.this, "已暂停", Toast.LENGTH_SHORT).show();
                }else{
                    ((ImageButton) v).setBackgroundResource(R.drawable.pause);
                    timer.setBase(SystemClock.elapsedRealtime()-rangeTime);
                    timer.start();
                    Toast.makeText(MainMenu.this, "继续游戏", Toast.LENGTH_SHORT).show();
                }
                isPlay=!isPlay;
            }
        });

    }

    private void initView() {
        puzzleLayout = (PuzzleView) findViewById(R.id.puzzleView);
        puzzleGame = new GameAction(this, puzzleLayout);
        SrcImg = (ImageView) findViewById(R.id.iv_CurIma);
        spinner = (Spinner) findViewById(R.id.ModeSpinner);
        tv_Level = (TextView) findViewById(R.id.tvLevel);
        backImg=findViewById(R.id.iv_GameBack);
        musicImg=findViewById(R.id.iv_GameMenu);
        pause=findViewById(R.id.ib_pause);
        SP_history=getSharedPreferences("historyInfo",0);

        //获取当前动作
        Intent intent=getIntent();
        //从intent取出bundle
        Bundle bundle=intent.getBundleExtra("res");
        //获取数据
        changeImgFlag=bundle.getInt("changeImgFlag");

        String level="3";
        Log.i("当前阶级为",level );
        set_level=Integer.parseInt(level);
        //set_mode=cursor.getString(4);
        set_mode="Normal_Mode";
        Log.i("当前模式为", set_mode);

        if (changeImgFlag == 0) {
            res=bundle.getInt("res_default");
            SrcImg.setImageBitmap(Utils.ReadBitmap(getApplicationContext(), res, 4));
            puzzleLayout.changeRes(res,set_level,set_mode);
            bitmap= BitmapFactory.decodeResource(getResources(),res);
        }
        else if (changeImgFlag==1){
            bitmap=bundle.getParcelable("res_default");
            SrcImg.setImageBitmap(bitmap);
            puzzleLayout.changeBitmap(bitmap,set_level,set_mode);
            Toast.makeText(this, "设置完成", Toast.LENGTH_SHORT).show();

        }
        if (set_mode.equals("Easy")) {
            Log.i("切换spinner为", "简单模式");
            spinner.setSelection(2,true);
        }
        else if (set_mode.equals("Export")) {
            Log.i("切换spinner为", "专家模式");
            spinner.setSelection(1,true);
        }
        else{
            Log.i("切换spinner为", "普通模式");
            spinner.setSelection(0,true);
        }
        SrcImg.setImageBitmap(Utils.ReadBitmap(getApplicationContext(), puzzleLayout.getRes(), 4));

        timer=findViewById(R.id.timer);
        //计时器归零
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        tv_Level.setText("难度等级：" + puzzleGame.getLevel());

    }

    private void initListener() {
        puzzleGame.addGameStateListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    puzzleGame.changeMode(PuzzleView.GAME_MODE_NORMAL);
                    mode="普通模式";
                }
                else if(position == 2){
                    puzzleGame.changeMode(PuzzleView.GAME_MODE_EXCHANGE);
                    mode="简单模式";
                }
                else{
                    puzzleGame.changeMode(PuzzleView.GAME_MODE_EXCHANGE);
                    mode="专家模式";
                }
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectImageDialog == null) {
            selectImageDialog = new SelectDialog();
            selectImageDialog.addItemClickListener(new SelectDialog.OnItemClickListener() {
                @Override
                public void itemClick(int position, int res) {
                    //更新布局
                    puzzleGame.changeImage(res);
                    SrcImg.setImageBitmap(Utils.ReadBitmap(getApplicationContext(), res, 4));
                }
            });
        }
        SrcImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 selectImageDialog.showDialog(getSupportFragmentManager(), "dialog", 0);
            }
        });
    }

    //点击设置弹出选择框
    public void showPopMenu(View view) {
        //设置时时间暂停
        final long rangeTime=SystemClock.elapsedRealtime()-timer.getBase();
        timer.stop();
        PopupMenu menu = new PopupMenu(this, view);
        menu.getMenuInflater().inflate(R.menu.game_setting, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.turn_on:

                        MusicStart();
                        if (set_music) {
                            Toast.makeText(MainMenu.this, "音乐已打开", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainMenu.this, "设置未完成", Toast.LENGTH_SHORT).show();
                        }

                        timer.setBase(SystemClock.elapsedRealtime() - rangeTime);
                        timer.start();
                        break;

                    case R.id.turn_off:

                        MusicStop();
                        if (!set_music) {
                            Toast.makeText(MainMenu.this, "音乐已关闭", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainMenu.this, "设置未完成", Toast.LENGTH_SHORT).show();
                        }
                        timer.setBase(SystemClock.elapsedRealtime() - rangeTime);
                        timer.start();
                        break;

                }
                return true;
            }
        });
        menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                timer.setBase(SystemClock.elapsedRealtime() - rangeTime);
                timer.start();
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

    public void addLevel(View view) {
        puzzleGame.addLevel();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    public void reduceLevel(View view) {
        puzzleGame.reduceLevel();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }


    @Override
    public void setLevel(int level) {
        tv_Level.setText("难度等级：" + level);
    }

    @Override
    public void gameSuccess(int level) {

        final SuccessDialog successDialog = new SuccessDialog();
        Bundle bundle=new Bundle();

        //获取当前时间
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sDateFormat.format(new Date());
        bundle.putString("date",date);

        //获取游戏时长
        timer.stop();
        int temp0=Integer.parseInt(timer.getText().toString().split(":")[0]);
        int temp1=Integer.parseInt(timer.getText().toString().split(":")[1]);
        int temp= temp0*60 + temp1;
        bundle.putString("time",""+temp);

        //获取游戏步数
        bundle.putString("step",""+puzzleLayout.getStep());

        //游戏记录数据库添加数据
        HistoryData mHistory = new HistoryData(""+puzzleLayout.getStep(),""+date,
                ""+temp,""+ mode,""+level,bitmap);
        historyDataManager.openDataBase();

        long flag = historyDataManager.insertHistoryData(mHistory);
        if (flag == -1) {
            Toast.makeText(this, "记录失败", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainMenu.this, "当前游戏记录已保存", Toast.LENGTH_SHORT).show();
        }


        successDialog.setArguments(bundle);
        successDialog.show(getFragmentManager(), "successDialog");

        //游戏胜利监听
        successDialog.addButtonClickListener(new SuccessDialog.OnButtonClickListener() {
            @Override
            public void nextLevelClick() {
                puzzleGame.addLevel();
                successDialog.dismiss();
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
            }

            //取消，消除对话框
            @Override
            public void cancelClick() {
                successDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        if (historyDataManager == null) {
            historyDataManager = new HistoryDataManager(this);
            historyDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (historyDataManager!= null) {
            historyDataManager.closeDataBase();
            historyDataManager = null;
        }
        super.onPause();
    }

}
