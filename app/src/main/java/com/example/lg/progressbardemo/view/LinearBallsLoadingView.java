package com.example.lg.progressbardemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


import com.example.lg.progressbardemo.R;

import java.util.ArrayList;

/**
 * Created by ligen on 2017/9/19.
 */

public class LinearBallsLoadingView extends View {

    private float mSmallBallRadius; //小球半径
    private float mLargeBallRadius; //大球半径
    private float mBallsSpacing; //大球之间的间距
    private int mBallColor; //球的颜色
    private int duration; //小球运动的时间
    private int mBallsNum; //大球的个数
    private Paint mPaint;
    private float mViewWidth; //整个自定义View的宽度
    private float mViewHeight;//整个自定义View的高度
    private float mInterpolatedTime; //小球运动位移与mViewWidth的百分比
    private ArrayList<Ball> mBallsList;
    private float centerDistance;//小球与相邻大球的圆心距离
    private float scale = 0.3f; //两球相交时，大球的缩放比例

    public LinearBallsLoadingView(Context context) {
        this(context,null);
    }

    public LinearBallsLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinearBallsLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinearBallsLoadingView, 0, 0);
        mSmallBallRadius = typedArray.getDimension(R.styleable.LinearBallsLoadingView_smallBallRadius, 20);
        mLargeBallRadius = typedArray.getDimension(R.styleable.LinearBallsLoadingView_largeBallRadius, 30);
        mBallsSpacing = typedArray.getDimension(R.styleable.LinearBallsLoadingView_ballsSpacing, 50);
        mBallColor = typedArray.getColor(R.styleable.LinearBallsLoadingView_ballColor, Color.RED);
        duration = typedArray.getInt(R.styleable.LinearBallsLoadingView_duration, 4500);
        mBallsNum = typedArray.getInt(R.styleable.LinearBallsLoadingView_ballsNum, 5);
        typedArray.recycle();
    }

    private void initVariable() {
        mPaint = new Paint();
        mPaint.setColor(mBallColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mBallsList = new ArrayList<>();

        mViewWidth = mLargeBallRadius * 2 * 5 + mBallsSpacing * 6 + mSmallBallRadius * 4;
        mViewHeight = Math.max(mSmallBallRadius * 2,mLargeBallRadius * 2) * (1 + scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Ball smallBall = new Ball();
        smallBall.center[0] = mSmallBallRadius + (mViewWidth - mSmallBallRadius * 2) * mInterpolatedTime;
        smallBall.center[1] = mLargeBallRadius * (1 + scale);
        smallBall.radius = mSmallBallRadius;
        mBallsList.add(0,smallBall);
        canvas.drawCircle(smallBall.center[0],smallBall.center[1],smallBall.radius,mPaint);
        for(int i = 1 ;i < mBallsNum + 1 ; i++){
            Ball largeBall = new Ball();
            largeBall.center[0] = mSmallBallRadius * 2 + (mLargeBallRadius + mBallsSpacing) * i + mLargeBallRadius*(i-1);
            largeBall.center[1] = mLargeBallRadius * (1 + scale);
            largeBall.radius = mLargeBallRadius;
            mBallsList.add(largeBall);
            centerDistance = Math.abs(smallBall.center[0] - largeBall.center[0]);
            if(centerDistance < (mSmallBallRadius + mLargeBallRadius)){
                // 根据小球与相交大球的圆心距离，改变大球的半径
                canvas.drawCircle(largeBall.center[0],largeBall.center[1],
                        largeBall.radius * (1 + scale * (1 - centerDistance/(mSmallBallRadius + mLargeBallRadius))),mPaint);
            }else {
                // 大球半径不变
                canvas.drawCircle(largeBall.center[0],largeBall.center[1],largeBall.radius,mPaint);
            }
            if(centerDistance >= mLargeBallRadius
                    && centerDistance <= (mLargeBallRadius + mBallsSpacing)){
                // 画贝塞尔曲线
                Path path = new Path();
                path.moveTo(largeBall.center[0],largeBall.center[1] - mLargeBallRadius);
                path.quadTo((smallBall.center[0] + largeBall.center[0])/2,largeBall.center[1],smallBall.center[0],smallBall.center[1] - mSmallBallRadius);
                path.lineTo(smallBall.center[0],smallBall.center[1] + mSmallBallRadius);
                path.quadTo((smallBall.center[0] + largeBall.center[0])/2,largeBall.center[1],largeBall.center[0],largeBall.center[1] + mLargeBallRadius);
                path.close();
                canvas.drawPath(path,mPaint);
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 控制整个自定义View的大小，使gravity属性生效
        setMeasuredDimension(resolveSizeAndState((int)mViewWidth,widthMeasureSpec,0),
                resolveSizeAndState((int)mViewHeight,heightMeasureSpec,0));
    }

    /**
     * 当自定义View添加到window中后，开启动画
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    private void startAnimation(){
        BallAnimation ballAnimation = new BallAnimation();
        ballAnimation.setDuration(duration);
        ballAnimation.setRepeatCount(Animation.INFINITE);
        ballAnimation.setRepeatMode(Animation.REVERSE);
        startAnimation(ballAnimation);
        invalidate();
    }

    class BallAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mInterpolatedTime = interpolatedTime;
        }
    }

    class Ball{
        float[] center = new float[2];
        float radius;
    }
}
