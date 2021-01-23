package com.swust.zhengxin.puzzle.Module;


import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 *      图片切割碎片集合
 **/
public class ImagePiece {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_EMPTY = 1;

    private int type = TYPE_NORMAL;

    //碎片编号
    private int index;
    private Bitmap bitmap;
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
