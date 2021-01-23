package com.swust.zhengxin.puzzle.Game;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.swust.zhengxin.puzzle.R;
import com.swust.zhengxin.puzzle.UI.PuzzleView;


public class GameAction implements Game, PuzzleView.SuccessListener{
    private PuzzleView puzzleView;
    private GameStateListener gameStateListener;
    private Context context;

    public void addGameStateListener(GameStateListener stateListener){
        this.gameStateListener = stateListener;
    }

    public GameAction(@NonNull Context context, @NonNull PuzzleView puzzleView) {
        this.context = context.getApplicationContext();
        this.puzzleView = puzzleView;
        puzzleView.addSuccessListener(this);
    }

    private boolean checkNull() {
        return puzzleView == null;
    }

    @Override
    public void addLevel() {
        if(checkNull()){
            return ;
        }
        if(!puzzleView.addCount()){
            Toast.makeText(context,context.getString(R.string.Highest), Toast.LENGTH_SHORT).show();
        }
        if(gameStateListener!= null){
            gameStateListener.setLevel(getLevel());
        }
    }

    @Override
    public void reduceLevel() {
        if (checkNull()) {
            return;
        }
        if (!puzzleView.reduceCount()) {
            Toast.makeText(context, context.getString(R.string.Lowest), Toast.LENGTH_SHORT).show();
        }
        if (gameStateListener != null) {
            gameStateListener.setLevel(getLevel());
        }
    }

    @Override
    public void changeMode(String gameMode) {
        puzzleView.changeMode(gameMode);
    }


    @Override
    public void changeImage(int res) {
         puzzleView.changeRes(res,3,"Normal");
    }
    //取当前关卡等级
    public int getLevel() {
        if (checkNull()) {
            return 0;
        }
        int count = puzzleView.getCount();
        return count - 3 + 1;
    }

    public interface GameStateListener {
        public void setLevel(int level);

        public void gameSuccess(int level);
    }

    public void success() {
        if (gameStateListener != null) {
            gameStateListener.gameSuccess(getLevel());
        }
    }
}
