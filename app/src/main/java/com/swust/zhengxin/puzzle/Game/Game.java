package com.swust.zhengxin.puzzle.Game;

public interface Game {
    /**
     * 增加难度
     */
    public void addLevel();

    /**
     * 降低难度
     */
    public void reduceLevel();

    /**
     * 模式
     */
    public void changeMode(String gameMode);

    /**
     * 修改图片
     */
    public void changeImage(int picId);
}

