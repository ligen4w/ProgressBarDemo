package com.example.lg.progressbardemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lg.progressbardemo.R;


/**
 * Created by ligen on 2017/5/18.
 */

public class CircleProgressBarView extends View {
    private float mViewWidth;
    private float mViewHeight;
    private Paint mCirclePaint ;
    private Paint mRingPaint ;
    private Paint mTextPaint ;
    private Paint mCenterPaint ;
    private float mCircleRadius;
    private float mRingRadius;
    private float mStrokeWidth;
    private int mCircleColor;
    private int mRingColor;
//    private int mTextColor;
    private int mProgress;
    private float mTextWidth;
    private float mTextHeight;
    // 圆心x坐标
    private float mXCenter;
    // 圆心y坐标
    private float mYCenter;
    // 总进度
    private int mMaxProgress ;

    public CircleProgressBarView(Context context) {
        this(context,null);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView, 0, 0);
        mCircleRadius = typedArray.getDimension(R.styleable.CircleProgressBarView_radius, 80);
        mStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBarView_strokeWidth, 10);
        mCircleColor = typedArray.getColor(R.styleable.CircleProgressBarView_circleColorCpb,0xffffffff);
        mRingColor = typedArray.getColor(R.styleable.CircleProgressBarView_ringColor,context.getResources().getColor(R.color.colorAccent));
        mMaxProgress = typedArray.getInt(R.styleable.CircleProgressBarView_maxProgress,100);
        mRingRadius = mCircleRadius + mStrokeWidth / 2;
        mViewWidth = (mCircleRadius + mStrokeWidth) * 2;
        mViewHeight = mViewWidth;
        typedArray.recycle();
    }

    private void initVariable() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setARGB(255,255,255,255);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mCircleRadius/2);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (float) Math.ceil(fm.descent - fm.ascent);
        Log.i("lg","mTextHeight = "+mTextHeight);
        Log.i("lg","fm.descent = "+fm.descent);
        Log.i("lg","fm.ascent = "+fm.ascent);

        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setColor(Color.RED);
        mCenterPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = mViewWidth/2;
        mYCenter = mViewHeight/2;

        canvas.drawCircle(mXCenter,mYCenter,mCircleRadius,mCirclePaint);

        if(mProgress >= 0){
            RectF rectF = new RectF();
            rectF.left = mXCenter - mRingRadius;
            rectF.top = mYCenter - mRingRadius;
//            rectF.right = mXCenter + mRingRadius;
//            rectF.bottom = mYCenter + mRingRadius;
            rectF.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            rectF.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(rectF,-90,1.0f*mProgress/mMaxProgress*360,false,mRingPaint);

            String txt = mProgress + "%";
            mTextWidth = mTextPaint.measureText(txt,0,txt.length());
            canvas.drawText(txt,mXCenter - mTextWidth/2,mYCenter + mTextHeight/4,mTextPaint);

            canvas.drawPoint(mXCenter,mYCenter,mCenterPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 控制整个自定义View的大小，使gravity属性生效
        setMeasuredDimension(resolveSizeAndState((int)mViewWidth,widthMeasureSpec,0),
                resolveSizeAndState((int)mViewHeight,heightMeasureSpec,0));
    }

    public void setProgress(int progress){
        mProgress = progress;
        postInvalidate();
    }

    public void setmMaxProgress(int maxProgress){
        mMaxProgress = maxProgress;
    }
}
