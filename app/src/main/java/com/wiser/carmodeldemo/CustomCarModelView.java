package com.wiser.carmodeldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wiser.library.helper.WISERHelper;
import com.wiser.library.util.WISERApp;


/**
 * @author Wiser
 * <p>
 * 自定义车锁视图
 */
public class CustomCarModelView extends View implements View.OnTouchListener {

    public static final int OPEN = 1;                                                            // 门开

    public static final int CLOSE = 0;                                                            // 门关

    private int lfDoorState = CLOSE;                                                        // 左前门状态

    private int rfDoorState = CLOSE;                                                        // 右前门状态

    private int lbDoorState = CLOSE;                                                        // 左后门状态

    private int rbDoorState = CLOSE;                                                        // 右后门状态

    private int tDoorState = CLOSE;                                                        // 后备箱门状态

    public static final int LF_DOOR = 100;                                                            // 左前门

    public static final int RF_DOOR = 101;                                                            // 右前门

    public static final int LB_DOOR = 102;                                                            // 左后门

    public static final int RB_DOOR = 103;                                                            // 右后门

    public static final int T_DOOR = 104;                                                            // 后备箱

    public static final int LF_DOOR_OPEN = 1000;                                                            // 左前门开

    public static final int LF_DOOR_CLOSE = 1001;                                                            // 左前门关

    public static final int RF_DOOR_OPEN = 1002;                                                            // 右前门开

    public static final int RF_DOOR_CLOSE = 1003;                                                            // 右前门关

    public static final int LB_DOOR_OPEN = 1004;                                                            // 左后门开

    public static final int LB_DOOR_CLOSE = 1005;                                                            // 左后门关

    public static final int RB_DOOR_OPEN = 1006;                                                            // 右后门开

    public static final int RB_DOOR_CLOSE = 1007;                                                            // 右后门关

    public static final int T_DOOR_OPEN = 1008;                                                            // 后备箱开

    public static final int T_DOOR_CLOSE = 1009;                                                            // 后备箱关

    private int doorType;                                                                                // 车门类型

    private int doorResultType;                                                                            // 车门操作类型

    private DoorListener doorListener;                                                                            // 点击车门开关状态监听

    private Bitmap carBodyBitmap;                                                                            // 车身图片

    private Bitmap carDoorBitmapLFD;                                                                        // 车左前门图片

    private Bitmap carDoorBitmapRFD;                                                                        // 车右前门图片

    private Bitmap carDoorBitmapLBD;                                                                        // 车左后门图片

    private Bitmap carDoorBitmapRBD;                                                                        // 车右后图片

    private Bitmap carDoorBitmapTD;                                                                        // 车后备箱门图片

    private Rect mSrcRectB;                                                                                // 车身

    private Rect mDestRectB;                                                                                // 车身

    private Rect mSrcRectLFD;                                                                            // 左前车门

    private Rect mDestRectLFD;                                                                            // 左前车门

    private Rect mSrcRectRFD;                                                                            // 右前车门

    private Rect mDestRectRFD;                                                                            // 右前车门

    private Rect mSrcRectLBD;                                                                            // 左后车门

    private Rect mDestRectLBD;                                                                            // 左后车门

    private Rect mSrcRectRBD;                                                                            // 右后车门

    private Rect mDestRectRBD;                                                                            // 右后车门

    private Rect mSrcRectTD;                                                                                // 后备箱车门

    private Rect mDestRectTD;                                                                            // 后备箱车门

    private int leftBody, topBody;

    private int widthBody, heightBody, widthFrontDoor, heightFrontDoor, widthBackDoor, heightBackDoor;

    private Paint mPaint;

    private String dfColor = "#80959595";                                                    // 默认颜色 也是关的颜色

    private String openBgColor = "#80f45813";                                                    // 开颜色

    private String openCloseDfColor = "#80ffffff";                                                    // 开关默认色

    private int selectStateTextColor = Color.WHITE;                                                    // 选择状态字体颜色

    private int unselectStateTextColor = Color.BLACK;                                                    // 未选择状态字体颜色

    private int dotRadius = 5;                                                            // 实心点半径

    private int hollowRadius = dotRadius + 5;                                                // 空心圆半径

    private int lineLong = 170;                                                            // 线长

    private int rectangleY = 150;                                                            // 矩形高

    private int rectangleX = 300;                                                            // 矩形宽

    private int rectangleXPadding = 50;                                                            // 矩形文本X轴Padding

    private int rectangleYPadding = 40;                                                            // 矩形文本Y轴Padding

    private String openText = "开";

    private String closeText = "关";

    private int widthText, heightText;

    private int textSize = 40;

    public CustomCarModelView(Context context) {
        super(context);
        init();
    }

    public CustomCarModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        lineLong = WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_170));
        rectangleX = WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_300));
        rectangleY = WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_150));
        rectangleXPadding = WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_40));
        rectangleYPadding = WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_35));

        initPaint();

        carBodyBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_body);
        carDoorBitmapLFD = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_left_front_door);
        carDoorBitmapRFD = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_right_front_door);
        carDoorBitmapLBD = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_left_back_door);
        carDoorBitmapRBD = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_right_back_door);
        carDoorBitmapTD = BitmapFactory.decodeResource(getResources(), R.mipmap.car_lock_t_door);

        widthBody = carBodyBitmap.getWidth();
        heightBody = carBodyBitmap.getHeight();
        widthFrontDoor = carDoorBitmapLFD.getWidth();
        heightFrontDoor = carDoorBitmapLFD.getHeight();
        widthBackDoor = carDoorBitmapLBD.getWidth();
        heightBackDoor = carDoorBitmapLBD.getHeight();
        int[] values = getTextValue(mPaint, openText);
        widthText = values[0];
        heightText = values[1];
        rectangleX = 2 * (widthText + 2 * rectangleXPadding);
        rectangleY = heightText + 2 * rectangleYPadding;

        mSrcRectB = new Rect(0, 0, widthBody, carBodyBitmap.getHeight());
        leftBody = WISERApp.getScreenWidth() / 2 - widthBody / 2;
        topBody = WISERApp.getScreenHeight() / 2 - (heightBody + lineLong + rectangleY - WISERApp.px2dip(getResources().getDimension(R.dimen.dp_dimen_80))) / 2;
        // topBody = WISERApp.getScreenHeight() / 2 - heightBody / 2 -
        // WISERApp.dip2px(50);
        mDestRectB = new Rect(leftBody, topBody, leftBody + widthBody, topBody + carBodyBitmap.getHeight());
        lfDoorCreateRect();
        rfDoorCreateRect();
        lbDoorCreateRect();
        rbDoorCreateRect();
        tDoorRect();

        setOnTouchListener(this);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(dfColor));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(textSize);
    }

    //左前门
    private void lfDoorCreateRect() {
        if (carDoorBitmapLFD == null) return;
        mSrcRectLFD = new Rect(0, 0, carDoorBitmapLFD.getWidth(), carDoorBitmapLFD.getHeight());
        mDestRectLFD = new Rect(leftBody - carDoorBitmapLFD.getWidth(), topBody + heightBody * 4 / 15, leftBody + 20, topBody + heightBody * 4 / 15 + carDoorBitmapLFD.getHeight());
    }

    //右前门
    private void rfDoorCreateRect() {
        if (carDoorBitmapRFD == null) return;
        mSrcRectRFD = new Rect(0, 0, carDoorBitmapRFD.getWidth(), carDoorBitmapRFD.getHeight());
        mDestRectRFD = new Rect(leftBody + widthBody - 20, topBody + heightBody * 4 / 15, leftBody + widthBody - 20 + carDoorBitmapRFD.getWidth(),
                topBody + heightBody * 4 / 15 + carDoorBitmapRFD.getHeight());
    }

    //左后门
    private void lbDoorCreateRect() {
        if (carDoorBitmapLBD == null) return;
        mSrcRectLBD = new Rect(0, 0, carDoorBitmapLBD.getWidth(), carDoorBitmapLBD.getHeight());
        mDestRectLBD = new Rect(leftBody - carDoorBitmapLBD.getWidth(), topBody + heightBody * 8 / 15, leftBody + 20, topBody + heightBody * 8 / 15 + carDoorBitmapLBD.getHeight());
    }

    //右后门
    private void rbDoorCreateRect() {
        if (carDoorBitmapRFD == null) return;
        mSrcRectRBD = new Rect(0, 0, carDoorBitmapRFD.getWidth(), carDoorBitmapRFD.getHeight());
        mDestRectRBD = new Rect(leftBody + widthBody - 20, topBody + heightBody * 8 / 15, leftBody + widthBody - 20 + carDoorBitmapRBD.getWidth(),
                topBody + heightBody * 8 / 15 + carDoorBitmapRBD.getHeight());
    }

    //后备箱
    private void tDoorRect() {
        if (carDoorBitmapTD == null) return;
        mSrcRectTD = new Rect(0, 0, carDoorBitmapTD.getWidth(), carDoorBitmapTD.getHeight());
        mDestRectTD = new Rect(leftBody + widthBody / 2 + carDoorBitmapTD.getWidth() / 2, topBody + heightBody * 12 / 15, leftBody + widthBody / 2 - carDoorBitmapTD.getWidth() / 2,
                topBody + heightBody * 12 / 15 + carDoorBitmapTD.getHeight());
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        // 绘制画布颜色
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.parseColor(dfColor));
        mPaint.setStyle(Paint.Style.FILL);
        // 画车身
        if (carBodyBitmap != null) canvas.drawBitmap(carBodyBitmap, mSrcRectB, mDestRectB, null);
        // // 左前门
        // if (carDoorBitmapLFD != null) canvas.drawBitmap(carDoorBitmapLFD,
        // mSrcRectLFD, mDestRectLFD, null);
        // // 右前门
        // if (carDoorBitmapRFD != null) canvas.drawBitmap(carDoorBitmapRFD,
        // mSrcRectRFD, mDestRectRFD, null);
        // // 左后门
        // if (carDoorBitmapLBD != null) canvas.drawBitmap(carDoorBitmapLBD,
        // mSrcRectLBD, mDestRectLBD, null);
        // // 右后门
        // if (carDoorBitmapRBD != null) canvas.drawBitmap(carDoorBitmapRBD,
        // mSrcRectRBD, mDestRectRBD, null);
        // // 后备箱
        // if (carDoorBitmapTD != null) canvas.drawBitmap(carDoorBitmapTD, mSrcRectTD,
        // mDestRectTD, null);

        canvasCarDoor(canvas);

        // TODO 画左前门实心点
        // 画左前车门实心点
        canvas.drawCircle((float) (leftBody + 20 - widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2), dotRadius, mPaint);
        // 画右前车门实心点
        canvas.drawCircle((float) (leftBody + widthBody - 20 + widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2), dotRadius, mPaint);
        // 画左后车门实心点
        canvas.drawCircle((float) (leftBody + 20 - widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2), dotRadius, mPaint);
        // 画右后车门实心点
        canvas.drawCircle((float) (leftBody + widthBody - 20 + widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2), dotRadius, mPaint);
        // 画右后备箱实心点
        canvas.drawCircle((float) (leftBody + widthBody / 2), (float) (topBody + heightBody * 13 / 15), dotRadius, mPaint);
        // TODO 画左前门空心圆
        mPaint.setStyle(Paint.Style.STROKE);
        // 画左前车门空心
        canvas.drawCircle((float) (leftBody + 20 - widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2), hollowRadius, mPaint);
        // 画右前车门空心
        canvas.drawCircle((float) (leftBody + widthBody - 20 + widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2), hollowRadius, mPaint);
        // 画左后车门空心
        canvas.drawCircle((float) (leftBody + 20 - widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2), hollowRadius, mPaint);
        // 画右后车门空心
        canvas.drawCircle((float) (leftBody + widthBody - 20 + widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2), hollowRadius, mPaint);
        // 画右后备箱空心
        canvas.drawCircle((float) (leftBody + widthBody / 2), (float) (topBody + heightBody * 13 / 15), hollowRadius, mPaint);
        // TODO 画线
        // 画左前门线
        canvas.drawLine((float) (leftBody + 20 - widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius), (float) (leftBody + 20 - widthFrontDoor / 2),
                (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
        // 画右前门线
        canvas.drawLine((float) (leftBody + widthBody - 20 + widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius),
                (float) (leftBody + widthBody - 20 + widthFrontDoor / 2), (float) (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
        // 画左后门线
        canvas.drawLine((float) (leftBody + 20 - widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius), (float) (leftBody + 20 - widthBackDoor / 2),
                (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong), mPaint);
        // 画右后门线
        canvas.drawLine((float) (leftBody + widthBody - 20 + widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius),
                (float) (leftBody + widthBody - 20 + widthBackDoor / 2), (float) (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong), mPaint);
        // 画右后备箱线
        canvas.drawLine((float) (leftBody + widthBody / 2), (float) (topBody + heightBody * 13 / 15 + hollowRadius), (float) (leftBody + widthBody / 2),
                (float) (topBody + heightBody * 13 / 15 + hollowRadius + lineLong), mPaint);
        // TODO 画开关边框
        // 画左前门矩形
        canvas.drawRoundRect(new RectF(leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                leftBody + 20 - (float) widthFrontDoor / 2 + (float) rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), 5, 5, mPaint);
        // 画右前门矩形
        canvas.drawRoundRect(
                new RectF(leftBody + widthBody - 20 + (float) widthFrontDoor / 2 - (float) rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong),
                5, 5, mPaint);
        // 画左后门矩形
        canvas.drawRoundRect(new RectF(leftBody + 20 - widthBackDoor / 2 - rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                leftBody + 20 - (float) widthBackDoor / 2 + (float) rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), 5, 5, mPaint);
        // 画右后门矩形
        canvas.drawRoundRect(new RectF(leftBody + widthBody - 20 + (float) widthBackDoor / 2 - (float) rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), 5, 5, mPaint);
        // 画后备箱矩形
        canvas.drawRoundRect(new RectF(leftBody + (float) widthBody / 2 - (float) rectangleX / 2, topBody + heightBody * 13 / 15 + hollowRadius + lineLong, leftBody + widthBody / 2 + rectangleX / 2,
                topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY), 5, 5, mPaint);
        // TODO 画开关背景以及文字内容
        canvasOpenClose(canvas);
    }

    /**
     * 画车门
     *
     * @param canvas
     */
    private void canvasCarDoor(Canvas canvas) {
        switch (lfDoorState) {
            case OPEN:// 左前门开
                // 左前门
                if (carDoorBitmapLFD != null)
                    canvas.drawBitmap(carDoorBitmapLFD, mSrcRectLFD, mDestRectLFD, null);
                break;
            case CLOSE:// 左前门关
                break;
        }
        switch (rfDoorState) {
            case OPEN:// 右前门开
                // 右前门
                if (carDoorBitmapRFD != null)
                    canvas.drawBitmap(carDoorBitmapRFD, mSrcRectRFD, mDestRectRFD, null);
                break;
            case CLOSE:// 右前门关
                break;
        }
        switch (lbDoorState) {
            case OPEN:// 左后门开
                // 左后门
                if (carDoorBitmapLBD != null)
                    canvas.drawBitmap(carDoorBitmapLBD, mSrcRectLBD, mDestRectLBD, null);
                break;
            case CLOSE:// 左后门关
                break;
        }
        switch (rbDoorState) {
            case OPEN:// 右后门开
                // 右后门
                if (carDoorBitmapRBD != null)
                    canvas.drawBitmap(carDoorBitmapRBD, mSrcRectRBD, mDestRectRBD, null);
                break;
            case CLOSE:// 右后门关
                break;
        }
        switch (tDoorState) {
            case OPEN:// 后备箱门开
                // 后备箱
                if (carDoorBitmapTD != null)
                    canvas.drawBitmap(carDoorBitmapTD, mSrcRectTD, mDestRectTD, null);
                break;
            case CLOSE:// 后备箱门关
                break;
        }
    }

    /**
     * 画开关背景颜色状态
     *
     * @param canvas
     */
    private void canvasOpenClose(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        // 左前门
        switch (lfDoorState) {
            case OPEN:// 左前门开
                mPaint.setColor(Color.parseColor(openBgColor));
                // 画左前门开背景
                canvas.drawRect(new Rect(leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + 20 - widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画左前门关背景
                canvas.drawRect(new Rect(leftBody + 20 - widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + 20 - widthFrontDoor / 2 + rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画左前门开文本
                canvas.drawText(openText, leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2 + rectangleXPadding,
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画左前门关文本
                canvas.drawText(closeText, (float) (leftBody + 20 - widthFrontDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                break;
            case CLOSE:// 左前门关
                mPaint.setColor(Color.parseColor(dfColor));
                // 画左前门关背景
                canvas.drawRect(new Rect(leftBody + 20 - widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + 20 - widthFrontDoor / 2 + rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画左前门开背景
                canvas.drawRect(new Rect(leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + 20 - widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画左前门关文本
                canvas.drawText(closeText, (float) (leftBody + 20 - widthFrontDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画左前门开文本
                canvas.drawText(openText, leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2 + rectangleXPadding,
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                break;
        }
        // 右前门
        switch (rfDoorState) {
            case OPEN:// 右前门开
                mPaint.setColor(Color.parseColor(openBgColor));
                // 画右前门开背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthFrontDoor / 2 - rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + widthBody - 20 + widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画右前门关背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画右前门开文本
                canvas.drawText(openText, (float) (leftBody + widthBody - 20 + widthFrontDoor / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画右前门关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                break;
            case CLOSE:// 右前门关
                mPaint.setColor(Color.parseColor(dfColor));
                // 画右前门关背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画右前门开背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthFrontDoor / 2 - rectangleX / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY,
                        leftBody + widthBody - 20 + widthFrontDoor / 2, topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画右前门关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画右前门开文本
                canvas.drawText(openText, (float) (leftBody + widthBody - 20 + widthFrontDoor / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY + rectangleYPadding + heightText, mPaint);
                break;
        }
        // 左后门
        switch (lbDoorState) {
            case OPEN:// 左后门开
                mPaint.setColor(Color.parseColor(openBgColor));
                // 画左后门开背景
                canvas.drawRect(new Rect(leftBody + 20 - widthBackDoor / 2 - rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + 20 - widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画左后门关背景
                canvas.drawRect(new Rect(leftBody + 20 - widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + 20 - widthBackDoor / 2 + rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画左后门开文本
                canvas.drawText(openText, leftBody + 20 - widthBackDoor / 2 - rectangleX / 2 + rectangleXPadding,
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画左后门关文本
                canvas.drawText(closeText, (float) (leftBody + 20 - widthBackDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
            case CLOSE:// 左后门关
                mPaint.setColor(Color.parseColor(dfColor));
                // 画左后门关背景
                canvas.drawRect(new Rect(leftBody + 20 - widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + 20 - widthBackDoor / 2 + rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画左后门开背景
                canvas.drawRect(new Rect(leftBody + 20 - widthBackDoor / 2 - rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + 20 - widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画左后门关文本
                canvas.drawText(closeText, (float) (leftBody + 20 - widthBackDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画左后门开文本
                canvas.drawText(openText, leftBody + 20 - widthBackDoor / 2 - rectangleX / 2 + rectangleXPadding,
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
        }
        // 右后门
        switch (rbDoorState) {
            case OPEN:// 右后门开
                mPaint.setColor(Color.parseColor(openBgColor));
                // 画右后门开背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthBackDoor / 2 - rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + widthBody - 20 + widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画右后门关背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画右后门开文本
                canvas.drawText(openText, (float) (leftBody + widthBody - 20 + widthBackDoor / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画右后门关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
            case CLOSE:// 右后门关
                mPaint.setColor(Color.parseColor(dfColor));
                // 画右后门关背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画右后门开背景
                canvas.drawRect(new Rect(leftBody + widthBody - 20 + widthBackDoor / 2 - rectangleX / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong,
                        leftBody + widthBody - 20 + widthBackDoor / 2, topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画右后门关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画右后门开文本
                canvas.drawText(openText, (float) (leftBody + widthBody - 20 + widthBackDoor / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
        }
        // 后备箱
        switch (tDoorState) {
            case OPEN:// 后备箱门开
                mPaint.setColor(Color.parseColor(openBgColor));
                // 画后备箱开背景
                canvas.drawRect(new Rect(leftBody + widthBody / 2 - rectangleX / 2, topBody + heightBody * 13 / 15 + hollowRadius + lineLong, leftBody + widthBody / 2,
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画后备箱关背景
                canvas.drawRect(new Rect(leftBody + widthBody / 2, topBody + heightBody * 13 / 15 + hollowRadius + lineLong, leftBody + widthBody / 2 + rectangleX / 2,
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画后备箱开文本
                canvas.drawText(openText, (float) (leftBody + widthBody / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画后备箱关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody / 2 + rectangleXPadding), topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
            case CLOSE:// 后备箱门关
                mPaint.setColor(Color.parseColor(dfColor));
                // 画后备箱关背景
                canvas.drawRect(new Rect(leftBody + widthBody / 2, topBody + heightBody * 13 / 15 + hollowRadius + lineLong, leftBody + widthBody / 2 + rectangleX / 2,
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(Color.parseColor(openCloseDfColor));
                // 画后备箱开背景
                canvas.drawRect(new Rect(leftBody + widthBody / 2 - rectangleX / 2, topBody + heightBody * 13 / 15 + hollowRadius + lineLong, leftBody + widthBody / 2,
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY), mPaint);
                mPaint.setColor(selectStateTextColor);
                // 画后备箱关文本
                canvas.drawText(closeText, (float) (leftBody + widthBody / 2 + rectangleXPadding), topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                mPaint.setColor(unselectStateTextColor);
                // 画后备箱开文本
                canvas.drawText(openText, (float) (leftBody + widthBody / 2 - rectangleX / 2 + rectangleXPadding),
                        topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleYPadding + heightText, mPaint);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // int measureWidth = widthBody + widthFrontDoor + rectangleX - 40;
        // int measureHeight = heightBody * 4 / 15 + 2 * hollowRadius + 2 * lineLong + 2
        // * rectangleY;
        int measureWidth = WISERApp.getScreenWidth();
        int measureHeight = WISERApp.getScreenHeight();
        setMeasuredDimension(measureSpec(measureWidth, widthMeasureSpec), measureSpec(measureHeight, heightMeasureSpec));
    }

    private int measureSpec(int size, int measureSpec) {
        int specModel = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specModel) {
            case MeasureSpec.AT_MOST:
                if (size <= specSize) {
                    return size;
                } else {
                    return specSize;
                }
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.UNSPECIFIED:
                return size;
            default:
                return size;
        }
    }

    // 获取文字宽高
    private int[] getTextValue(Paint paint, String text) {
        int[] values = new int[2];
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        values[0] = rect.width();
        values[1] = rect.height();
        return values;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
                clickView(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:// 抬起
                break;
        }
        return true;
    }

    private void clickView(float x, float y) {
        // 左前门开
        if (x >= (leftBody + 20 - widthFrontDoor / 2 - rectangleX / 2) && x <= (leftBody + 20 - widthFrontDoor / 2)
                && y >= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY)
                && y <= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong)) {
            WISERHelper.log().d("----左前门开");
            WISERHelper.toast().show("左前门开");
            lfDoorState = OPEN;
            doorResultType = LF_DOOR_OPEN;
        }
        // 左前门关
        if (x >= (leftBody + 20 - widthFrontDoor / 2) && x <= (leftBody + 20 - widthFrontDoor / 2 + rectangleX / 2)
                && y >= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY)
                && y <= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong)) {
            WISERHelper.log().d("----左前门关");
            WISERHelper.toast().show("左前门关");
            lfDoorState = CLOSE;
            doorResultType = LF_DOOR_CLOSE;
        }
        // 右前门开
        if (x >= (leftBody + widthBody - 20 + widthFrontDoor / 2 - rectangleX / 2) && x <= (leftBody + widthBody - 20 + widthFrontDoor / 2)
                && y >= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY)
                && y <= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong)) {
            WISERHelper.log().d("----右前门开");
            WISERHelper.toast().show("右前门开");
            rfDoorState = OPEN;
            doorResultType = RF_DOOR_OPEN;
        }
        // 右前门关
        if (x >= (leftBody + widthBody - 20 + widthFrontDoor / 2) && x <= (leftBody + widthBody - 20 + widthFrontDoor / 2 + rectangleX / 2)
                && y >= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong - rectangleY)
                && y <= (topBody + heightBody * 4 / 15 + heightFrontDoor / 2 - hollowRadius - lineLong)) {
            WISERHelper.log().d("----右前门关");
            WISERHelper.toast().show("右前门关");
            rfDoorState = CLOSE;
            doorResultType = RF_DOOR_CLOSE;
        }
        // 左后门开
        if (x >= (leftBody + 20 - widthBackDoor / 2 - rectangleX / 2) && x <= (leftBody + 20 - widthBackDoor / 2) && y >= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----左后门开");
            WISERHelper.toast().show("左后门开");
            lbDoorState = OPEN;
            doorResultType = LB_DOOR_OPEN;
        }
        // 左后门关
        if (x >= (leftBody + 20 - widthBackDoor / 2) && x <= (leftBody + 20 - widthBackDoor / 2 + rectangleX / 2) && y >= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----左后门关");
            WISERHelper.toast().show("左后门关");
            lbDoorState = CLOSE;
            doorResultType = LB_DOOR_CLOSE;
        }
        // 右后门开
        if (x >= (leftBody + widthBody - 20 + widthBackDoor / 2 - rectangleX / 2) && x <= (leftBody + widthBody - 20 + widthBackDoor / 2)
                && y >= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----右后门开");
            WISERHelper.toast().show("右后门开");
            rbDoorState = OPEN;
            doorResultType = RB_DOOR_OPEN;
        }
        // 右后门关
        if (x >= (leftBody + widthBody - 20 + widthBackDoor / 2) && x <= (leftBody + widthBody - 20 + widthBackDoor / 2 + rectangleX / 2)
                && y >= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 8 / 15 + heightBackDoor / 2 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----右后门关");
            WISERHelper.toast().show("右后门关");
            rbDoorState = CLOSE;
            doorResultType = RB_DOOR_CLOSE;
        }
        // 后备箱门开
        if (x >= (leftBody + widthBody / 2 - rectangleX / 2) && x <= (leftBody + widthBody / 2) && y >= (topBody + heightBody * 13 / 15 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----后备箱门开");
            WISERHelper.toast().show("后备箱门开");
            tDoorState = OPEN;
            doorResultType = T_DOOR_OPEN;
        }
        // 后备箱门关
        if (x >= (leftBody + widthBody / 2) && x <= (leftBody + widthBody / 2 + rectangleX / 2) && y >= (topBody + heightBody * 13 / 15 + hollowRadius + lineLong)
                && y <= (topBody + heightBody * 13 / 15 + hollowRadius + lineLong + rectangleY)) {
            WISERHelper.log().d("----后备箱门关");
            WISERHelper.toast().show("后备箱门关");
            tDoorState = CLOSE;
            doorResultType = T_DOOR_CLOSE;
        }

        if (doorListener != null) {
            doorListener.onClickDoor(doorResultType);
        }

        invalidate();

    }

    // 左前门状态
    public int lfDoorState() {
        return lfDoorState;
    }

    // 右前门状态
    public int rfDoorState() {
        return rfDoorState;
    }

    // 左后门状态
    public int lbDoorState() {
        return lbDoorState;
    }

    // 右后门状态
    public int rbDoorState() {
        return rbDoorState;
    }

    // 后备箱门状态
    public int tDoorState() {
        return tDoorState;
    }

    /**
     * 更新车门UI
     *
     * @param doorType
     * @param doorState
     */
    public void updateDoorUi(int doorType, int doorState) {
        switch (doorType) {
            case LF_DOOR:// 左前门
                this.lfDoorState = doorState;
                break;
            case RF_DOOR:// 右前门
                this.rfDoorState = doorState;
                break;
            case LB_DOOR:
                this.lbDoorState = doorState;
                break;
            case RB_DOOR:
                this.rbDoorState = doorState;
                break;
            case T_DOOR:
                this.tDoorState = doorState;
                break;
        }
        invalidate();
    }

    public void setDoorListener(DoorListener doorListener) {
        this.doorListener = doorListener;
    }

    public interface DoorListener {

        void onClickDoor(int doorType);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (carDoorBitmapLFD != null) {
            carDoorBitmapLFD.recycle();
            carDoorBitmapLFD = null;
        }
        if (carDoorBitmapRFD != null) {
            carDoorBitmapRFD.recycle();
            carDoorBitmapRFD = null;
        }
        if (carDoorBitmapLBD != null) {
            carDoorBitmapLBD.recycle();
            carDoorBitmapLBD = null;
        }
        if (carDoorBitmapRBD != null) {
            carDoorBitmapRBD.recycle();
            carDoorBitmapRBD = null;
        }
        if (carDoorBitmapTD != null) {
            carDoorBitmapTD.recycle();
            carDoorBitmapTD = null;
        }
    }
}
