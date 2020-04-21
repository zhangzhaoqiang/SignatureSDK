package com.tonghui.signaturelib;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

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
 * @类名: StepOperator
 * @作者: 赵强
 * @创建时间: 2020/4/2 10:03
 * @描述: 删除、释放操作
 */
public class StepOperator {

    /**
     * 缓存步骤数
     */
    private static final int CAPACITY = 12;
    /**
     * 保存每一步绘制的bitmap
     */
    private List<Bitmap> mBitmaps = null;

    /**
     * 允许缓存Bitmap的最大宽度限制，过大容易内存溢出
     */
    public static int MAX_CACHE_BITMAP_WIDTH = 1024;
    /**
     * 当前位置
     */
    private int currentIndex;

    private boolean isUndo = false;

    /**
     * 最大缓存内存大小
     */
    private int cacheMemory;

    public StepOperator() {
        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>();
        }
        currentIndex = -1;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        cacheMemory = maxMemory / 3;
    }

    /**
     * 重置
     */
    public void reset() {
        if (mBitmaps != null) {
            for (Bitmap bitmap : mBitmaps) {
                bitmap.recycle();
            }
            mBitmaps.clear();
        }
        currentIndex = -1;
    }

    /**
     * 内存是否足够
     *
     * @return
     */
    private boolean isMemoryEnable() {
        int bitmapCache = 0;
        for (Bitmap bitmap : mBitmaps) {
            bitmapCache += bitmap.getRowBytes() * bitmap.getHeight();
        }
        if (bitmapCache > cacheMemory) {
            return false;
        }
        return true;
    }

    /**
     * 缓存绘制的Bitmap
     *
     * @param bitmap
     */
    public void addBitmap(Bitmap bitmap) {
        if (mBitmaps == null) {
            return;
        }
        try {
            if (!isMemoryEnable() && mBitmaps.size() > 1) {
                mBitmaps.get(1).recycle();
                //删除第一笔（0的位置有空的占位图）
                mBitmaps.remove(1);
            }
            if (currentIndex != -1 && isUndo) {
                for (int i = currentIndex + 1; i < mBitmaps.size(); i++) {
                    mBitmaps.get(i).recycle();
                }
                mBitmaps = mBitmaps.subList(0, currentIndex + 1);
                isUndo = false;
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
            mBitmaps.add(bitmap);
            currentIndex = mBitmaps.size() - 1;

            if (mBitmaps.size() > CAPACITY) {
                mBitmaps.get(1).recycle();
                //删除第一笔（0的位置有空的占位图）
                mBitmaps.remove(1);
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
        }
    }


    /**
     * 清空
     */
    public void freeBitmaps() {
        if (mBitmaps == null) {
            return;
        }
        for (Bitmap bitmap : mBitmaps) {
            bitmap.recycle();
        }
        mBitmaps.clear();
        mBitmaps = null;
        currentIndex = -1;
    }
}
