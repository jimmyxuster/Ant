package com.jimmyhsu.creepinggame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jimmyhsu.creepinggame.R;
import com.jimmyhsu.creepinggame.bean.Ant;
import com.jimmyhsu.creepinggame.bean.Stick;
import com.jimmyhsu.creepinggame.utils.CreepingGame;
import com.jimmyhsu.creepinggame.utils.PlayRoom;

import java.util.List;

/**
 * Created by xuyanzhe on 19/9/17.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final int ANT_SIZE = 80;
    private static final int INDICATE_LINE_LENGTH = 80;
    private static final int[] ANT_COLORS = new int[]{0x88E40007, 0x8824E10E, 0x881000EA,
            0x885908D3, 0x88000000};
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread t;
    private boolean isVisible;
    private float mVelocity;

    private int mWidth;
    private int mHeight;
    private float mStickWidth;

    private Rect mBgRect = new Rect();
    private Rect mStickRect = new Rect();
    private RectF mAntRect = new RectF();

    private Paint mLinePaint;

    private Bitmap[] mAntBitmaps;
    private Bitmap stickBitmap;
    private Bitmap bgBitmap;
    private List<Ant> mAnts;

    private int mCount = 0;
    private int mMaxCount;

    private GameCallback mCallback;

    public GameView(Context context) {
        this(context, null);
    }

    public void setAnts(List<Ant> ants) {
        mAnts = ants;
        mAntBitmaps = new Bitmap[ants.size() * 4];
        initAntBitmaps();
    }

    public void setCallback(GameCallback callback) {
        mCallback = callback;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);

        setZOrderOnTop(false);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg1);
        stickBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stick_bg);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isVisible = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBgRect.set(0, 0, w, h);
        mStickRect.set(w / 12, (int) (h * 0.3), 11 * w / 12, (int) (h * 0.3 + 50));
        mStickWidth = 5 * w / 6f;
        mVelocity = Ant.DEFAULT_VELOCITY / (PlayRoom.DEFAULT_INC_TIME / 20f);
        mMaxCount = (int) Math.floor(PlayRoom.DEFAULT_INC_TIME / 20f);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            isVisible = false;
        }
    }

    @Override
    public void run() {
        while (isVisible) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();

            try {
                if (end - start < 20) {
                    Thread.sleep(20 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        synchronized (this) {
            try {
                mCanvas = mHolder.lockCanvas();
                if (mCanvas != null) {
                    drawBackground();
                    drawStick();
                    drawAnts();
                }
            } catch (Exception e) {

            } finally {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawBackground() {
        mCanvas.drawBitmap(bgBitmap, null, mBgRect, null);
    }

    private void drawStick() {
        mCanvas.drawBitmap(stickBitmap, null, mStickRect, null);
    }

    private void drawAnts() {
        if (mAnts == null) return;
        int antsCount = mAnts.size();
        for (int i = 0; i < antsCount; i++) {
            Ant ant = mAnts.get(i);
            float left = mWidth / 12f + ant.getDisplayPosition() * 1.0f / 300 * mStickWidth;
            float top = mStickRect.top - ANT_SIZE - INDICATE_LINE_LENGTH;
            mAntRect.set(left - ANT_SIZE / 2, top, left + ANT_SIZE, top + ANT_SIZE);
            mCanvas.drawBitmap(mAntBitmaps[i * 2 + (ant.getPosition() % 10 == 0 ? 0 : 1) + (ant.getDirection() == Ant.DIR_RIGHT ? 10 : 0)], null, mAntRect, null);
            mLinePaint.setColor(ANT_COLORS[i]);
            mCanvas.drawLine(left, top + ANT_SIZE, left, mStickRect.top, mLinePaint);
            if (ant.isAlive()) {
                ant.setDisplayPosition(ant.getDisplayPosition() + ant.getDirection() * mVelocity);
            }
        }
        mCount++;
        if (mCount >= mMaxCount) notifyPlayRoom();
    }

    private void notifyPlayRoom() {
        if (mCallback != null) {
            mCallback.onRequestIterate();
            mCount = 0;
        }
    }

    private void initAntBitmaps() {
        if (mAntBitmaps != null) {
            mAntBitmaps[0] = getBitmapFromRes(R.drawable.red_ant1, ANT_SIZE);
            mAntBitmaps[1] = getBitmapFromRes(R.drawable.red_ant2, ANT_SIZE);
            mAntBitmaps[2] = getBitmapFromRes(R.drawable.green_ant2, ANT_SIZE);
            mAntBitmaps[3] = getBitmapFromRes(R.drawable.green_ant2, ANT_SIZE);
            mAntBitmaps[4] = getBitmapFromRes(R.drawable.blue_ant1, ANT_SIZE);
            mAntBitmaps[5] = getBitmapFromRes(R.drawable.blue_ant2, ANT_SIZE);
            mAntBitmaps[6] = getBitmapFromRes(R.drawable.purple_ant1, ANT_SIZE);
            mAntBitmaps[7] = getBitmapFromRes(R.drawable.purple_ant2, ANT_SIZE);
            mAntBitmaps[8] = getBitmapFromRes(R.drawable.black_ant1, ANT_SIZE);
            mAntBitmaps[9] = getBitmapFromRes(R.drawable.black_ant2, ANT_SIZE);
            for (int i = 10; i < 20; i++) {
                mAntBitmaps[i] = createBitmapReversed(mAntBitmaps[i - 10]);
            }
        }
    }

    private Bitmap createBitmapReversed(Bitmap origin) {
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        return Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, true);
    }

    private Bitmap getBitmapFromRes(int resId, int dstWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(options.outWidth / dstWidth, options.outHeight / dstWidth);
        return BitmapFactory.decodeResource(getResources(), resId, options);
    }

    public interface GameCallback {
        void onRequestIterate();
    }
}
