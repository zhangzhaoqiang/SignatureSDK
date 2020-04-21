package com.tonghui.signaturelib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * .----.
 * _.'__    `.
 * .--(Q)(OK)---/$\
 * .' @          /$$$\
 * :         ,   $$$$$
 * `-..__.-' _.-\$$/
 * `;_:    `"'
 * .'"""""`.
 * /,  ZZQ  ,\
 * //         \\
 * `-._______.-'
 * ___`. | .'___
 * (______|______)
 *
 * @包名: com.tonghui.signaturelib
 * @类名: PaintView
 * @作者: 赵强
 * @创建时间: 2020/4/2 10:02
 * @描述: 手写画板View
 */
public class PaintView extends View {
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int strokeWidth = 10;
    private BasePen mStokeBrushPen;

    /**
     * 是否允许写字
     */
    private boolean isFingerEnable = true;


    /**
     * 画笔轨迹记录
     */
    private StepOperator mStepOperation;

    //是否正在绘制
    private boolean isDrawing = false;

    //记录手写笔类型：触控笔/手指
    private int toolType = 0;


    public PaintView(Context context) {
        this(context, null);
        init();
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        mStokeBrushPen = new SteelPen();
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mStepOperation = new StepOperator();
        mStepOperation.addBitmap(mBitmap);
        initCanvas();
    }

    /**
     * 初始画笔设置
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#101010"));
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha(0xFF);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeMiter(1.0f);
        mStokeBrushPen.setPaint(mPaint);
    }

    private void initCanvas() {
        mCanvas = new Canvas(mBitmap);
        //设置画布的背景色为透明
        mCanvas.drawColor(Color.TRANSPARENT);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        mStokeBrushPen.draw(canvas);
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过压感值来设置画笔的粗细
        setPaintPressure(event.getPressure());
        //获取触摸的工具
        toolType = event.getToolType(event.getActionIndex());
        // FINGER手指   STYLUS手写笔    MOUSE鼠标
        if (!isFingerEnable && toolType != MotionEvent.TOOL_TYPE_STYLUS) {
            return false;
        }

        //画笔的手势监听
        mStokeBrushPen.onTouchEvent(event, mCanvas);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isDrawing = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                isDrawing = false;
                break;
            case MotionEvent.ACTION_UP:
                if (mStepOperation != null && isDrawing) {
                    mStepOperation.addBitmap(mBitmap);
                }
                isDrawing = false;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    /**
     * 构建Bitmap，用来保存绘制的图
     * @isCrop 是否清除边界空白
     * @return 所绘制的bitmap
     */
    public Bitmap buildAreaBitmap(boolean isCrop) {
        Bitmap result;
        if (isCrop) {
            result = BitmapUtil.clearBlank(mBitmap, 20, Color.TRANSPARENT);
        } else {
            result = mBitmap;
        }
        destroyDrawingCache();
        return result;
    }


    /**
     * 设置压力传感值
     * @param pressure
     */
    private void setPaintPressure(float pressure) {
        if (mPaint != null) {
            mPaint.setStrokeWidth(strokeWidth*pressure);
            mStokeBrushPen.setPaint(mPaint);
            invalidate();
        }
    }

    /**
     * 设置画笔大小
     * @param width 大小
     */
    public void setPaintWidth(int width) {
        if (mPaint != null) {
            strokeWidth = width;
            mPaint.setStrokeWidth(width);
            mStokeBrushPen.setPaint(mPaint);
            invalidate();
        }
    }


    /**
     * 设置画笔颜色
     * @param color 颜色
     */
    public void setPaintColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(color);
            mStokeBrushPen.setPaint(mPaint);
            invalidate();
        }
    }


    /**
     * 清除画布，记得清除点的集合
     */
    public void reset() {
        mBitmap.eraseColor(Color.TRANSPARENT);
        mStokeBrushPen.clear();
        if (mStepOperation != null) {
            mStepOperation.reset();
            mStepOperation.addBitmap(mBitmap);
        }
        invalidate();
    }


    /**
     * 释放
     */
    public void release() {
        destroyDrawingCache();
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mStepOperation != null) {
            mStepOperation.freeBitmaps();
            mStepOperation = null;
        }
    }
}
