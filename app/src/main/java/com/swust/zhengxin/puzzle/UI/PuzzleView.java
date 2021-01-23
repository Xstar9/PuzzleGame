package com.swust.zhengxin.puzzle.UI;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.swust.zhengxin.puzzle.Gaming.MainMenu;
import com.swust.zhengxin.puzzle.Module.ImagePiece;
import com.swust.zhengxin.puzzle.R;
import com.swust.zhengxin.puzzle.Utils.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;




public class PuzzleView extends FrameLayout implements View.OnClickListener {
    public static final String GAME_MODE_EXCHANGE = "GameModeExchange";
    public static final String GAME_MODE_NORMAL = "GameModeNormal";

    private static final int DEFAULT_MARGIN = 3;
    //改变模式
    private String mGameMode = GAME_MODE_EXCHANGE;
    //拼图布局：正方形边长为屏幕的宽
    private int mViewWidth = 0;
    //每行图片数，默认为3
    private int mCount = 3;
    //每张图片宽度
    private int mItemWidth;
    //Game Bitmap集合
    private List<ImagePiece> mImagePieces;
    //设置每个图片的大小
    private FrameLayout.LayoutParams layoutParams;


    private Bitmap mBitmap , ChangeBitmap;
    //边缘
    private int mMargin;
    //填充
    private int mPadding;

    //Animation层
    private RelativeLayout mAnimationLayout;
    //选中的第一张图片
    private ImageView mFirst;
    //选中的第二张图片
    private ImageView mSecond;
    //是否已添加动画层
    private boolean isAddAnimatorLayout = false;
    //是否正在进行动画
    private boolean isAnimation = false;

    //默认资源图
    private int res = R.mipmap.scenery;
    //步数
    public int stepCount;
    //打乱后的可解性
    boolean solution=true;
    //切换图片标记
    private int changeImgFlag=0;
    //模式标号
    private int  modeFlag=1;

    public PuzzleView(Context context) {
        this(context, null);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        init(context);
        //图片切割
        initBitmaps();
        //设置碎片大小以及layout
        initBitmapsWidth();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewWidth);
    }
    //碎片布局
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageView) {
                ImageView imageView = (ImageView) getChildAt(i);
                imageView.layout(imageView.getLeft(), imageView.getTop(), imageView.getRight(), imageView.getBottom());
            }
            else {
                RelativeLayout relativeLayout = (RelativeLayout) getChildAt(i);
                relativeLayout.layout(0, 0, mViewWidth, mViewWidth);
            }
        }
    }

    private void init(Context context) {
        mMargin = Utils.dptopx(context, DEFAULT_MARGIN);
        mViewWidth = Utils.getScreenWidth(context)[0];
        mPadding = Utils.getMinLength(getPaddingBottom(), getPaddingLeft(), getPaddingRight(), getPaddingTop());
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (mCount - 1)) / mCount;
    }

    private void initBitmaps() {
        if (mBitmap == null&&changeImgFlag==0) {
            mBitmap = BitmapFactory.decodeResource(getResources(), res);
            Log.i("res", "已完成设置 ");
        }
        else {
            mBitmap=ChangeBitmap;
            Log.i("Bitmap", "已成功设置 ");
        }

        mGameMode=GAME_MODE_EXCHANGE;
        if (modeFlag==1){
            mGameMode=GAME_MODE_NORMAL;
            Log.i("改为普通模式", "Okay！ ");
        }
        else if(modeFlag==2){
            mGameMode=GAME_MODE_EXCHANGE;
            Log.i("改为专家模式", "Okay！ ");
        }
        else if(modeFlag==3){
            mGameMode=GAME_MODE_EXCHANGE;
            Log.i("改为简单模式", "Okay！ ");
        }
        Log.i("当前模式为", mGameMode);

        //对图片进行切割,编号并打乱
        mImagePieces = Utils.splitImage(getContext(), mBitmap, mCount, mGameMode);
        sortImagePieces();
    }

    /**
     * 对碎片进行打乱
     * Collections.sort(List<?> list,Comparator<? super T> );进行升降序打乱
     **/
    private void sortImagePieces() {
        stepCount=0;
        solution=true;
        int count =0;
        int blank=mCount-1;

            while(solution) {
                count=0;
                Collections.sort(mImagePieces, new Comparator<ImagePiece>() {
                    @Override
                    public int compare(ImagePiece left, ImagePiece right) {

                        //随机升降序
                        return Math.random() > 0.5 ? 1 : -1;
                    }
                });

                //倒置序列和： Tsum=count需为偶数
                for(int i=0;i<mImagePieces.size();i++){
                    ImagePiece imagePiece1=mImagePieces.get(i);
                    for (int j=i;j<mImagePieces.size();j++){
                        ImagePiece imagePiece2=mImagePieces.get(j);
                        if (imagePiece1.getIndex()>imagePiece2.getIndex()){
                            count++;
                            Log.i("", "sortImagePieces: "+count);
                        }
                    }
                }

                if (count!=0&&mImagePieces.size()%2==1){
                    Log.i("", "倒置序列和count为: "+count);

                    solution=(count%2==0);//false;
                }
                else{
                    if((mImagePieces.size()%-blank)%2==1){
                        Log.i("", "倒置序列和count为: "+count);
                        solution=(count%2==0);
                    }
                    else{
                        Log.i("", "倒置序列和count为: "+count);
                        solution=(count%2==1);
                    }
                }
            }

            //将空白碎片置于最后
            ImagePiece tempImagePieces = null;
            int tempIndex = 0;
            for (int i = 0; i < mImagePieces.size(); i++) {
                ImagePiece imagePiece = mImagePieces.get(i);
                if (imagePiece.getType() == ImagePiece.TYPE_EMPTY) {
                    tempImagePieces = imagePiece;
                    tempIndex = i;
                    break;
                }
            }
            if (tempImagePieces == null) return;
            //互换
            mImagePieces.remove(tempIndex);
            mImagePieces.add(mImagePieces.size(), tempImagePieces);

    }



    /**
     * 设置碎片大小以及属性
     */
    private void initBitmapsWidth() {
        int line = 0;
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < mImagePieces.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(mImagePieces.get(i).getBitmap());
            layoutParams = new LayoutParams(mItemWidth, mItemWidth);
            imageView.setLayoutParams(layoutParams);
            if (i != 0 && i % mCount == 0) {
                line++;
            }
            if (i % mCount == 0) {
                left = i % mCount * mItemWidth;
            }
            else {
                left = i % mCount * mItemWidth + (i % mCount) * mMargin;
            }
            top = mItemWidth * line + line * mMargin;
            right = left + mItemWidth;
            bottom = top + mItemWidth;
            //设置属性
            imageView.setRight(right);
            imageView.setLeft(left);
            imageView.setBottom(bottom);
            imageView.setTop(top);
            imageView.setId(i);
            imageView.setOnClickListener(this);
            mImagePieces.get(i).setImageView(imageView);
            addView(imageView);
        }
    }


    //转换模式（刷新布局）
    public void changeMode(@NonNull String gameMode) {
//        if (gameMode.equals(mGameMode)) {
//            return;
//        }
        this.mGameMode = gameMode;
        reset();
    }

    //重新布局
    public void reset() {
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (mCount - 1)) / mCount;
        if (mImagePieces != null) {
            mImagePieces.clear();
        }
        isAddAnimatorLayout = false;
        mBitmap = null;
        removeAllViews();
        initBitmaps();
        initBitmapsWidth();
    }

    //增加阶级，最高为6
    public boolean addCount() {
        mCount++;
        if (mCount > 6) {
            mCount--;
            return false;
        }
        reset();
        return true;
    }

    //切换图片
    public void changeRes(int res,int level,String mode) {
        this.res = res;
        this.mCount=level;
        if (mode.equals("Normal")) {
            modeFlag=1;
            Log.i("modeFlag="+modeFlag, "转换成功");
        }
        else if(mode.equals("Export")) {
            modeFlag=2;
            Log.i("modeFlag="+modeFlag, "转换成功");
        }
        else{
            modeFlag=3;
            Log.i("modeFlag="+modeFlag, "转换成功");
        }
        changeImgFlag=0;

        reset();
    }

    //游戏中切换图片
    public void changeBitmap(Bitmap bitmap,int level,String mode){
        ChangeBitmap=bitmap;
        this.mCount=level;
        changeImgFlag=1;

        reset();
    }

    //降低阶级，最低为3
    public boolean reduceCount() {
        mCount--;
        if (mCount < 3) {
            mCount++;
            return false;
        }

        reset();
        return true;
    }


    @Override
    public void onClick(View v) {
        if(isAnimation){
            //若在运行动画，则不能点击
            return ;
        }
        if (!(v instanceof ImageView)) {
            //若点击的图片还不是碎片时，不允许点击
            return;
        }
        if(!MainMenu.isPlay){
            //若计时器暂停，则不能点击
            return ;
        }

            ImageView imageView = (ImageView) v;
            ImagePiece imagePiece = mImagePieces.get(imageView.getId());
            if(imagePiece.getType()==ImagePiece.TYPE_EMPTY){
                //点击空白碎片不处理
                return;
            }
            //判断是否已被点击
            if(mFirst==null){
                mFirst=(ImageView)v;
            }
            stepCount++;
            checkEmptyImage(mFirst);

    }

    //判断空白碎片所在位置
    private void checkEmptyImage(ImageView imageView){
        int index = imageView.getId();
        int line = mImagePieces.size()/mCount;//第n行
        ImagePiece imagePiece = null;
        if(index < mCount){
            //第一行，判断下一行是否存在空白碎片,判断当前同一列是否有空碎片
            imagePiece = checkCurrentLine(index);
            imagePiece = checkOtherLine(index + mCount,imagePiece);
        }
        else if(index < (line-1)*mCount){
            //中间行（第二行）判断上下行以及同一列是否有空碎片
            imagePiece = checkCurrentLine(index);
            imagePiece = checkOtherLine(index - mCount, imagePiece);//当前行上一行
            imagePiece = checkOtherLine(index + mCount, imagePiece);//当前行下一行
        }
        else{
            //最后一行（第三行），判断上一行以及同一列是否有空白碎片
            imagePiece = checkCurrentLine(index);
            imagePiece = checkOtherLine(index - mCount, imagePiece);
        }
        if(imagePiece == null){
            //四周没有空白碎片
            mFirst = null;
            mSecond = null;
        }
        else{
            //标记第二张选中的碎片
            mSecond = imagePiece.getImageView();
            exChangeView();//交换动画效果
        }
    }

    /**
     *      检查当前行是否有空白碎片
     */
    private ImagePiece checkCurrentLine(int index) {
        ImagePiece imagePiece = null;
        //第一行
        if (index % mCount == 0) {
            //第一个
            imagePiece = getCheckEmptyImageView(index + 1);
        }
        else if (index % mCount == mCount - 1) {
            //最后一个
            imagePiece = getCheckEmptyImageView(index - 1);
        }
        else {
            imagePiece = getCheckEmptyImageView(index + 1);
            if (imagePiece == null) {
                imagePiece = getCheckEmptyImageView(index - 1);
            }
        }
        return imagePiece;
    }

    /**
     *       检查其他（上下行）是否有空白碎片
     */
    private ImagePiece checkOtherLine(int index, ImagePiece imagePiece) {
        if (imagePiece != null) {
            return imagePiece;
        } else {
            return getCheckEmptyImageView(index);
        }
    }

    /**
     *       获取空白碎片位置
     */
    private ImagePiece getCheckEmptyImageView(int index) {
        ImagePiece imagePiece = mImagePieces.get(index);
        if (imagePiece.getType() == ImagePiece.TYPE_EMPTY) {
            //找到空的imageView
            return imagePiece;
        }
        return null;
    }

    /**
     *       添加动画层，拼图碎片交换平移动画
     */
    private ImageView addAnimationImageView(ImageView imageView){
        ImageView getImage = new ImageView(getContext());
        RelativeLayout.LayoutParams firstParams = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        firstParams.leftMargin = imageView.getLeft() - mPadding;
        firstParams.topMargin = imageView.getTop() - mPadding;
        Bitmap firstBitmap = mImagePieces.get(imageView.getId()).getBitmap();
        getImage.setImageBitmap(firstBitmap);
        getImage.setLayoutParams(firstParams);
        mAnimationLayout.addView(getImage);
        return getImage;
    }
    //设置动画层
    private void setUpAnimLayout() {
        if (mAnimationLayout == null) {
            mAnimationLayout = new RelativeLayout(getContext());
        }
        if (!isAddAnimatorLayout) {
            isAddAnimatorLayout = true;
            addView(mAnimationLayout);
        }
    }
    //平移交换
    private void exChangeView(){
        //添加动画层
        setUpAnimLayout();
        //点击选中的第一个图片，将动画添加到上面
        ImageView first = addAnimationImageView(mFirst);
        //点击选中的第二个图片，将动画添加到上面
        ImageView second = addAnimationImageView(mSecond);
        //平移距离
        ObjectAnimator secondXAnimator = ObjectAnimator.ofFloat(second, "TranslationX", 0f, -(mSecond.getLeft() - mFirst.getLeft()));
        ObjectAnimator secondYAnimator = ObjectAnimator.ofFloat(second, "TranslationY", 0f, -(mSecond.getTop() - mFirst.getTop()));
        ObjectAnimator firstXAnimator = ObjectAnimator.ofFloat(first, "TranslationX", 0f, mSecond.getLeft() - mFirst.getLeft());
        ObjectAnimator firstYAnimator = ObjectAnimator.ofFloat(first, "TranslationY", 0f, mSecond.getTop() - mFirst.getTop());
        AnimatorSet secondAnimator = new AnimatorSet();
        secondAnimator.play(secondXAnimator).with(secondYAnimator).with(firstXAnimator).with(firstYAnimator);
        secondAnimator.setDuration(500);

        final ImagePiece firstPiece = mImagePieces.get(mFirst.getId());
        final ImagePiece secondPiece = mImagePieces.get(mSecond.getId());
        final int firstType = firstPiece.getType();
        final int secondType = secondPiece.getType();
        final Bitmap firstBitmap = mImagePieces.get(mFirst.getId()).getBitmap();
        final Bitmap secondBitmap = mImagePieces.get(mSecond.getId()).getBitmap();

        //setListener设置一个监听器，使用addListener可设置多个监听器，不会被覆盖
        secondAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画结束
                int firstIndex = firstPiece.getIndex();
                int secondIndex = firstPiece.getIndex();
                if(mFirst != null){
                    mFirst.setColorFilter(null);
                    /**
                     * setVisibility()；
                     * VISIBLE 可见
                     * INVISIBILITY 组件不可见，但占用原本空间
                     * GONE  不可见，且不占用原布局空间
                     */
                    mFirst.setVisibility(VISIBLE);
                    mFirst.setImageBitmap(secondBitmap);
                    firstPiece.setBitmap(secondBitmap);
                    firstPiece.setIndex(secondIndex);
                }
                if (mSecond != null) {
                    mSecond.setVisibility(VISIBLE);
                    mSecond.setImageBitmap(firstBitmap);
                    secondPiece.setBitmap(firstBitmap);
                    secondPiece.setIndex(firstIndex);
                }

                    firstPiece.setType(secondType);
                    secondPiece.setType(firstType);

                mAnimationLayout.removeAllViews();//重新加载
                mAnimationLayout.setVisibility(GONE);
                mFirst = null;
                mSecond = null;
                isAnimation = false;
                invalidate();
                if (checkSuccess()) {

                    Log.i("成功","Success");
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    if (mSuccessListener != null) {
                        mSuccessListener.success();
                    }
                }
            }
            //启动动画效果
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimation = true;
                mAnimationLayout.setVisibility(VISIBLE);
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }
        });

        secondAnimator.start();
    }

    /**
     * 判断游戏是否成功
     */
    private boolean checkSuccess() {

        boolean isSuccess = true;//初值置为真
        //size（）碎片集合元素数量
        for (int i = 0; i < mImagePieces.size()-1; i++) {
            ImagePiece imagePiece = mImagePieces.get(i);//获取第i标号的碎片对象
            //判断i与碎片对应标号Index是否匹配
            if (i != imagePiece.getIndex()) {
                isSuccess = false;
                break;
            }
        }

        return isSuccess;
    }

    private SuccessListener mSuccessListener;
    //添加检测成功监听器
    public void addSuccessListener(SuccessListener successListener) {
        this.mSuccessListener = successListener;
    }
    //成功监听器接口
    public interface SuccessListener {
        public void success();
    }

    //获取每行碎片数
    public int getCount() {
        return mCount;
    }
    //获取移动步数
    public int getStep(){
        return stepCount;
    }
    //获取资源的位图
    public Bitmap getBitmap() {
        return mBitmap;
    }
    //获取图片资源
    public int getRes() {
        return res;
    }

}

