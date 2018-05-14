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
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.example.lg.progressbardemo.R;


/**
 * Created by ligen on 2017/9/20.
 */

public class CircleBallsLoadingView extends View{

    private float mSmallBallRadius; //小球半径
    private float mLargeBallRadius; //大球半径
    private float mLargeBallsDistance; //大球圆心之间的间距
    private float mCircleRadius; //圆形进度条半径
    private int mBallColor; //球的颜色
    private int duration; //小球运动的时间
    private int mBallsNum; //大球的个数
    private Paint mPaint;
    private float mViewWidth; //整个自定义View的宽度
    private float mViewHeight;//整个自定义View的高度
    private float scale = 0.3f; //两球相交时，大球的缩放比例
    private float[] circleCenter = new float[2];
    private float mInterpolatedTime;//小球运动位移与circle周长的百分比

    public CircleBallsLoadingView(Context context) {
        this(context,null);
    }

    public CircleBallsLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleBallsLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleBallsLoadingView, 0, 0);
        mSmallBallRadius = typedArray.getDimension(R.styleable.CircleBallsLoadingView_smallBallRadius_c, 15);
        mLargeBallRadius = typedArray.getDimension(R.styleable.CircleBallsLoadingView_largeBallRadius_c, 20);
        mCircleRadius = typedArray.getDimension(R.styleable.CircleBallsLoadingView_circleRadius, 120);
        mBallColor = typedArray.getColor(R.styleable.CircleBallsLoadingView_ballColor_c, Color.RED);
        duration = typedArray.getInt(R.styleable.CircleBallsLoadingView_duration_c, 5000);
        mBallsNum = typedArray.getInt(R.styleable.CircleBallsLoadingView_ballsNum_c, 9);
        typedArray.recycle();
    }

    private void initVariable() {
        mPaint = new Paint();
        mPaint.setColor(mBallColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mViewWidth = mViewHeight = (mCircleRadius + mLargeBallRadius * (1 + scale)) * 2;
        circleCenter[0] = circleCenter[1] = mCircleRadius + mLargeBallRadius * (1 + scale);
        double degree = (Math.PI * 2)/(mBallsNum);
        float ballX = circleCenter[0] + mCircleRadius;
        float ballY = circleCenter[1] - mCircleRadius;
        float ball1X = circleCenter[0] + mCircleRadius * (float)Math.sin(degree);
        float ball1Y = circleCenter[1] - mCircleRadius * (float)Math.cos(degree);
        mLargeBallsDistance = (float) Math.sqrt((Math.pow(Math.abs(ballX - ball1X),2) +
                Math.pow(Math.abs(ballY - ball1Y),2)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double degree = mInterpolatedTime * (Math.PI * 2);
        Ball smallBall = new Ball();
        smallBall.center[0] = circleCenter[0] + mCircleRadius * (float)Math.sin(degree);
        smallBall.center[1] = circleCenter[1] - mCircleRadius * (float)Math.cos(degree);
        smallBall.radius = mSmallBallRadius;
        canvas.drawCircle(smallBall.center[0],smallBall.center[1],smallBall.radius,mPaint);

        for(double i = 0;i < Math.PI * 2;i+= (Math.PI * 2)/mBallsNum){
            Ball largeBall = new Ball();
            if(i == 0 && degree > (Math.PI * 2) * (mBallsNum - 1)/mBallsNum){
                // 画角度360对应的大球，以及大球和运动小球的贝塞尔曲线（为了计算正确的控制点）
                largeBall.center[0] = circleCenter[0] + mCircleRadius * (float)Math.sin(Math.PI * 2);
                largeBall.center[1] = circleCenter[1] - mCircleRadius * (float)Math.cos(Math.PI * 2);
                largeBall.radius = mLargeBallRadius;
                drawPath(canvas,smallBall,largeBall,degree,Math.PI * 2);

            }else{
                // 其他角度下的大球和贝塞尔曲线
                largeBall.center[0] = circleCenter[0] + mCircleRadius * (float)Math.sin(i);
                largeBall.center[1] = circleCenter[1] - mCircleRadius * (float)Math.cos(i);
                largeBall.radius = mLargeBallRadius;
                drawPath(canvas,smallBall,largeBall,degree,i);
            }
        }

        invalidate();
    }

    private void drawPath(Canvas canvas,Ball smallBall,Ball largeBall,double degree,double i){

        float centerDistance = (float) Math.sqrt((Math.pow(Math.abs(smallBall.center[0] - largeBall.center[0]),2) +
                Math.pow(Math.abs(smallBall.center[1] - largeBall.center[1]),2)));
        if(centerDistance <= mLargeBallRadius + mSmallBallRadius){
            // 根据小球与相交大球的圆心距离，改变大球的半径
            canvas.drawCircle(largeBall.center[0],largeBall.center[1],
                    largeBall.radius * (1 + scale * (1 - centerDistance/(mSmallBallRadius + mLargeBallRadius))),mPaint);
        }else{
            // 大球半径不变
            canvas.drawCircle(largeBall.center[0],largeBall.center[1],largeBall.radius,mPaint);
        }

        //控制点坐标
        float controlPointX ;
        float controlPointY ;
        if(centerDistance > 0 && centerDistance < mLargeBallsDistance){
            controlPointX = circleCenter[0] + mCircleRadius * (float)Math.sin((degree + i)/2);
            controlPointY = circleCenter[1] - mCircleRadius * (float)Math.cos((degree + i)/2);
            // 画贝塞尔曲线
            Path path = new Path();
            path.moveTo(largeBall.center[0] + largeBall.radius * (float) Math.sin(i),
                    largeBall.center[1] - largeBall.radius * (float) Math.cos(i));
            path.quadTo(controlPointX,controlPointY,
                    smallBall.center[0] + smallBall.radius * (float) Math.sin(i),
                    smallBall.center[1] - smallBall.radius * (float) Math.cos(i));
            path.lineTo(smallBall.center[0] - smallBall.radius * (float) Math.sin(i),
                    smallBall.center[1] + smallBall.radius * (float) Math.cos(i));
            path.quadTo(controlPointX,controlPointY,
                    largeBall.center[0] - largeBall.radius * (float) Math.sin(i),
                    largeBall.center[1] + largeBall.radius * (float) Math.cos(i));
            path.lineTo(largeBall.center[0] - largeBall.radius * (float) Math.sin(i),
                    largeBall.center[1] + largeBall.radius * (float) Math.cos(i));
            path.close();
            canvas.drawPath(path,mPaint);
//            canvas.drawCircle(controlPointX,controlPointY,5,new Paint(Color.YELLOW)); //画控制点，观察控制点的位置
        }
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
        ballAnimation.setRepeatMode(Animation.RESTART);
        ballAnimation.setInterpolator(new LinearInterpolator());
        startAnimation(ballAnimation);
    }

    class BallAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
        }
    }

    class Ball{
        float[] center = new float[2];
        float radius ;
    }
}
