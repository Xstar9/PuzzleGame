package com.swust.zhengxin.puzzle.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.swust.zhengxin.puzzle.Module.ImagePiece;
import com.swust.zhengxin.puzzle.R;
import com.swust.zhengxin.puzzle.UI.PuzzleView;

import java.util.ArrayList;
import java.util.List;



        /**
         *
         *      切割工具
         *
         **/
public class Utils {

    //获取设备屏幕宽高
    public static int[] getScreenWidth(Context context){
        context=context.getApplicationContext();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        int width = outMetrics.widthPixels;//获取屏幕宽像素px
        int height = outMetrics.heightPixels;//高像素
        int size[]=new int[2];//数组返回（宽高）值
        size[0]=width;
        size[1]=height;

        return size;
    }

    /**
     *            传入Bitmap
     *            return 切割的碎片Pieces集合
     */
    public static List<ImagePiece> splitImage(Context context, Bitmap bitmap, int count, String gameMode){
        List<ImagePiece> imagePieces = new ArrayList<>();//动态数组
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int PicWidth = Math.min(width,height)/count; //拼图区域为正方形，即宽和高兼容最小的；并取得每个碎片的边长

        for(int i =0;i<count;i++){
            for(int j=0;j<count;j++){
                ImagePiece imagePiece = new ImagePiece();

                //获取切割碎片的坐标（x,y)
                int x = j * PicWidth;
                int y = i * PicWidth;
                imagePiece.setIndex(j + i*count);// 切割碎片预置编号

                    if(i == count-1 && j == count-1 ){
                        //最后一块为空白碎片
                        imagePiece.setType(ImagePiece.TYPE_EMPTY);
                        Bitmap emptyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
                        imagePiece.setBitmap(emptyBitmap);
                    }
                    else{
                        imagePiece.setBitmap(Bitmap.createBitmap(bitmap,x,y,PicWidth,PicWidth));
                    }

                //添加imagePiece碎片进容器imagePieces
                imagePieces.add(imagePiece);
            }
        }

        return imagePieces;//返回碎片集合
    }

    /**
     * 读取图片,按比例缩放返回bitmap
     *  synhronized:达到线程同步
     *  当线程运行到该方法前,若有其他线程正在运行时,需等待；无,则可以直接运行线程
     */
    public synchronized static Bitmap ReadBitmap(Context context,int res, int scale){
        try{
            //Options ,控制解码Bitmap时的各个参数
            BitmapFactory.Options options = new BitmapFactory.Options();
            //属性inJustDecodeBounds ： 置false，返回bitmap；置true，则只返回bitmap的尺寸大小
            options.inJustDecodeBounds = false;
            //按照 (1：scale) 比例缩放
            options.inSampleSize = scale;
            //设置色彩,采用RGB_565模式，该模式下，一个像素点占用2bytes。
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //解析资源图
            return BitmapFactory.decodeResource(context.getResources(),res,options);
        }
        //抛出异常，返回空
        catch (Exception e){
            return null;
        }
    }
    /**
     *    ...params 可变参数数组  取数据中最小值
     *
     */
    public static int getMinLength(int... params) {
        int min = params[0];
        for (int para : params) {
            if (para < min) {
                min = para;
            }
        }

        return min;
    }

    /**
     * 像素单位标准化
     * 标准单位：px
     * 非标准单位：dp,sp,pt
    **/
    public static int dptopx(Context context, int dpval) {
        context = context.getApplicationContext();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpval, context.getResources().getDisplayMetrics());
    }
}

