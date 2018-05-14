package com.example.lg.progressbardemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.lg.progressbardemo.R;


/**
 * Created by ligen on 2017/5/18.
 */

public class CircleWaveView extends View {

    private int mCenterColor;
    private int mCircleColor;
    private float mCenterRadius;
    private float mMaxCircleRadius;
    private float mStokeWidth = 2;
    private Paint mCenterPaint;
    private Paint mCirclePaint;

    private int mXCenter;
    private int mYCenter;
    private float tempRadius;
    private ValueAnimator animator;

    public CircleWaveView(Context context) {
        this(context,null);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleWaveView, 0, 0);
        mCenterRadius = typedArray.getDimension(R.styleable.CircleWaveView_centerRadius,10);
        mMaxCircleRadius = typedArray.getDimension(R.styleable.CircleWaveView_maxCircleRadius,50);
        mCenterColor = typedArray.getColor(R.styleable.CircleWaveView_centerColor, context.getResources().getColor(R.color.colorAccent));
        mCircleColor = typedArray.getColor(R.styleable.CircleWaveView_circleColorWcv,context.getResources().getColor(R.color.colorAccent));
        typedArray.recycle();
    }

    private void initVariable() {
        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setColor(mCenterColor);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mStokeWidth);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        mXCenter = getWidth()/2;
        mYCenter = getHeight()/2;

        canvas.drawCircle(mXCenter,mYCenter,mCenterRadius,mCenterPaint);

//        canvas.save();
        canvas.drawCircle(mXCenter,mYCenter,tempRadius + mStokeWidth,mCirclePaint);
//        canvas.restore();
//        final RectF rectF = new RectF();

//        while(mMaxCircleRadius>mCenterRadius){
//            ValueAnimator animator = ValueAnimator.ofFloat(mCenterRadius,mMaxCircleRadius);
//            animator.setDuration(1000);
//            animator.setRepeatMode(ValueAnimator.INFINITE);
//            animator.setInterpolator(new AccelerateInterpolator());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float tempRadius = (float)animation.getAnimatedValue();
//                    Log.i("lg","tempRadius = "+tempRadius);
////                    rectF.left = mXCenter - (tempRadius + mStokeWidth);
////                    rectF.top = mYCenter - (tempRadius + mStokeWidth);
////                    rectF.right = mXCenter + (tempRadius + mStokeWidth);
////                    rectF.bottom = mYCenter + (tempRadius + mStokeWidth);
//                    canvas.save();
//                    canvas.drawCircle(mXCenter,mYCenter,tempRadius + mStokeWidth,mCirclePaint);
//                    canvas.restore();
//                }
//            });
//            animator.start();
//        }
    }

    public void start(){
        if(animator!=null&&animator.isRunning()){
            return;
        }
        animator = ValueAnimator.ofFloat(mCenterRadius,mMaxCircleRadius);
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tempRadius = (float)animation.getAnimatedValue();
                Log.i("lg","tempRadius = "+ tempRadius);
                postInvalidate();
            }
        });
        animator.start();
    }

    public void stop(){
        if(animator!=null&&animator.isRunning()){
            animator.cancel();
            animator = null;
        }
    }
}
