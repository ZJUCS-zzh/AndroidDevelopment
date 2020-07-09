package com.bytedance.camp.chapter4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bytedance.camp.chapter4.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockView extends View {

    private static final int FULL_CIRCLE_DEGREE = 360;
    private static final int UNIT_DEGREE = 6;

    private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
    private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
    private static final int NORMAL_UNIT_ALPHA = 0x80;

    private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
    private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
    private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
    private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
    private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
    private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度

    private Calendar calendar = Calendar.getInstance();

    private float radius = 0; // 表盘半径
    private float centerX = 0; // 表盘圆心X坐标
    private float centerY = 0; // 表盘圆心Y坐标

    private List<RectF> unitLinePositions = new ArrayList<>();
    private Paint unitPaint = new Paint();
    private Paint needlePaint = new Paint();
    private Paint numberPaint = new Paint();

    private Handler handler = new Handler();

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.WHITE);
        unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
        unitPaint.setStrokeCap(Paint.Cap.ROUND);
        unitPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // TODO 设置绘制时、分、秒针的画笔: needlePaint
        needlePaint.setAntiAlias(true);
        needlePaint.setStyle(Paint.Style.FILL);
        // TODO 设置绘制时间数字的画笔: numberPaint
        numberPaint.setAntiAlias(true);
        numberPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configWhenLayoutChanged();
    }

    private void configWhenLayoutChanged() {
        float newRadius = Math.min(getWidth(), getHeight()) / 2f;
        if (newRadius == radius) {
            return;
        }
        radius = newRadius;
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        // 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
        for (int degree = 0; degree < FULL_CIRCLE_DEGREE; degree += UNIT_DEGREE) {
            double radians = Math.toRadians(degree);
            float startX = (float) (centerX + (radius * (1 - 0.05f)) * Math.cos(radians));
            float startY = (float) (centerY + (radius * (1 - 0.05f)) * Math.sin(radians));
            float stopX = (float) (centerX + (radius * (1 - 0.02f)) * Math.cos(radians));
            float stopY = (float) (centerY + (radius * (1 - 0.02f)) * Math.sin(radians));
            unitLinePositions.add(new RectF(startX, startY, stopX, stopY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawTimeNeedles(canvas);
        drawTimeNumbers(canvas);
        // TODO 实现时间的转动，每一秒刷新一次
        postInvalidateDelayed(1000);
    }
    // 绘制表盘上的刻度
    private void drawUnit(Canvas canvas) {
        for (int i = 0; i < unitLinePositions.size(); i++) {
            if (i % 5 == 0) {
                unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
            } else {
                unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
            }
            RectF linePosition = unitLinePositions.get(i);
            //canvas.drawLine(linePosition.left, linePosition.top, linePosition.right, linePosition.bottom, unitPaint);
            canvas.drawCircle(linePosition.right, linePosition.bottom,5, unitPaint);
        }
    }

    private void drawTimeNeedles(Canvas canvas) {
        Time time = getCurrentTime();
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        // TODO 根据当前时间，绘制时针、分针、秒针
        /**
         * 思路：
         * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
         * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
         * 3、从圆心到终点画一条线，此为时针
         * 注1：计算时针转动角度时要把时和分都得考虑进去
         * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
         * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
         *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
         * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
         */

        //Log.d("time is:",hour+":"+minute+":"+second);
        //时针
         int hourDegree = 360;
         int minuteHourDegree=30;
         int hourConvert=hour-3;
         if(hourConvert<0)hourConvert+=12;
         float endX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree*hourConvert/12+minuteHourDegree*minute/60)));
         float endY = (float) (centerY + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.sin(Math.toRadians(hourDegree*hourConvert/12+minuteHourDegree*minute/60)));
         needlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
         needlePaint.setColor(getResources().getColor(android.R.color.holo_orange_light));
         canvas.drawLine(centerX,centerY,endX,endY,needlePaint);
        //分针
         int minuteDegree=360;
         int secondMinuteDegree=6;
         int minuteConvert=minute-15;
         if(minuteConvert<0)minuteDegree+=60;
         endX=(float)(centerX+radius*MINUTE_NEEDLE_LENGTH_RATIO*Math.cos(Math.toRadians(minuteDegree*minuteConvert/60+secondMinuteDegree*second/60)));
         endY=(float)(centerX+radius*MINUTE_NEEDLE_LENGTH_RATIO*Math.sin(Math.toRadians(minuteDegree*minuteConvert/60+secondMinuteDegree*second/60)));
         needlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
         needlePaint.setColor(getResources().getColor(android.R.color.holo_purple));
         canvas.drawLine(centerX,centerY,endX,endY,needlePaint);
         //秒针
         int secondDegree=360;
         int secondConvert=second-15;
         if(secondConvert<0)secondConvert+=60;
         endX=(float)(centerX+radius*SECOND_NEEDLE_LENGTH_RATIO*Math.cos(Math.toRadians(secondDegree*secondConvert/60)));
         endY=(float)(centerX+radius*SECOND_NEEDLE_LENGTH_RATIO*Math.sin(Math.toRadians(secondDegree*secondConvert/60)));
         needlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
         needlePaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
         canvas.drawLine(centerX,centerY,endX,endY,needlePaint);
        //中央圆
        Paint circlePaint=new Paint();
        circlePaint.setColor(Color.BLACK);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setStrokeWidth(50);
        canvas.drawCircle(centerX,centerY,(float)Math.toRadians(360),circlePaint);
    }

    private void drawTimeNumbers(Canvas canvas) {
        // TODO 绘制表盘时间数字（可选）
        numberPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
        numberPaint.setTextSize(100);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        int hourText=1;
        float offsetX[]=new float [13];
        float offsetY[]=new float [13];
        for(int i=1;i<=12;i++){
            offsetX[i]=radius;
            offsetY[i]=radius;
        }
        offsetX[1]*=0.02f;offsetY[1]*=0.05f;
        offsetX[2]*=0.02f;offsetY[2]*=0.04f;
        offsetX[3]*=0.03f;offsetY[3]*=0.05f;
        offsetX[4]*=0.03f;offsetY[4]*=0.05f;
        offsetX[5]*=0;offsetY[5]*=0.05f;
        offsetX[6]*=0;offsetY[6]*=0.05f;
        offsetX[7]*=0.01f;offsetY[7]*=0.05f;
        offsetX[8]*=0.02f;offsetY[8]*=0.06f;
        offsetX[9]*=0.01f;offsetY[9]*=0.04f;
        offsetX[10]*=0.04f;offsetY[10]*=0.04f;
        offsetX[11]*=0.03f;offsetY[11]*=0.05f;
        offsetX[12]*=0;offsetY[12]*=0.05f;
        String hourSet[]=new String[13];
        hourSet[1]="1";hourSet[2]="2";hourSet[3]="3";
        hourSet[4]="4";hourSet[5]="5";hourSet[6]="6";
        hourSet[7]="7";hourSet[8]="8";hourSet[9]="9";
        hourSet[10]="10";hourSet[11]="11";hourSet[12]="12";
        float endTextX;
        float endTextY;
        for(;hourText<=12;hourText++){
            int hourTextConvert=hourText-3;
            if(hourTextConvert<=0)hourTextConvert+=12;
            endTextX=(float)(centerX+radius*(1-0.2f)*Math.cos(Math.toRadians(30*hourTextConvert))+offsetX[hourText]);
            endTextY=(float)(centerY+radius*(1-0.2f)*Math.sin(Math.toRadians(30*hourTextConvert))+offsetY[hourText]);
            canvas.drawText(hourSet[hourText],endTextX,endTextY,numberPaint);
        }

    }

    // 获取当前的时间：时、分、秒
    private Time getCurrentTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new Time(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
}
