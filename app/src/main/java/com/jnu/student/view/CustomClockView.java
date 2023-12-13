// 在view子目录下新建一个CustomClockView.java文件，继承自View类
package com.jnu.student.view;

import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.jnu.student.R;

import java.util.Calendar;

public class CustomClockView extends View {

    // 定义表盘、时针、分针、秒针的图片资源
    private int dialResId = R.drawable.clock; // 你可以替换成你喜欢的图片
    private int hourHandResId = R.drawable.hour;
    private int minuteHandResId = R.drawable.minute;
    private int secondHandResId = R.drawable.second;

    // 定义表盘、时针、分针、秒针的图片对象
    private Bitmap dialBitmap;
    private Bitmap hourHandBitmap;
    private Bitmap minuteHandBitmap;
    private Bitmap secondHandBitmap;


    public CustomClockView(Context context) {
        this(context, null);
    }

    public CustomClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化图片资源
        initBitmaps();

    }

    // 初始化图片资源的方法
    private void initBitmaps() {
        // 根据图片资源ID获取图片对象
        dialBitmap = BitmapFactory.decodeResource(getResources(), dialResId);
        hourHandBitmap = BitmapFactory.decodeResource(getResources(), hourHandResId);
        minuteHandBitmap = BitmapFactory.decodeResource(getResources(), minuteHandResId);
        secondHandBitmap = BitmapFactory.decodeResource(getResources(), secondHandResId);

    }


    // 绘制View的内容的方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制表盘
        //canvas.drawBitmap(dialBitmap, centerX - dialWidth / 2, centerY - dialHeight / 2, null);
        // 获取当前的时间
        long currentTime = System.currentTimeMillis();
        int hours = (int) ((currentTime / (1000 * 60 * 60)) % 12);
        int minutes = (int) ((currentTime / (1000 * 60)) % 60);
        int seconds = (int) ((currentTime / 1000) % 60);

// 计算时针、分针、秒针的角度
        float hourAngle = (hours + minutes / 60.0f) * 360 / 12;
        float minuteAngle = (minutes + seconds / 60.0f) * 360 / 60;
        float secondAngle = seconds * 360f / 60f;

// 调整图片的大小
        int screen_size=min(getHeight(),getWidth());
        int hourHand_size=screen_size;
        int minuteHand_size=screen_size;
        int secondHand_size=screen_size;

        dialBitmap = Bitmap.createScaledBitmap(dialBitmap, screen_size, screen_size, true);
        hourHandBitmap = Bitmap.createScaledBitmap(hourHandBitmap, hourHand_size, hourHand_size, true);
        minuteHandBitmap = Bitmap.createScaledBitmap(minuteHandBitmap, minuteHand_size, minuteHand_size, true);
        secondHandBitmap = Bitmap.createScaledBitmap(secondHandBitmap, secondHand_size, secondHand_size, true);

        int width = getWidth();
        int height = getHeight();

        // 绘制表盘
        canvas.drawBitmap(dialBitmap, (width- screen_size)/2f, (height- screen_size)/2f, null);
        secondAngle = seconds * 360f / 60f;

        // 绘制时针
        canvas.save();
        canvas.rotate(hourAngle, width / 2f, height / 2f);
        canvas.drawBitmap(hourHandBitmap, (width- hourHandBitmap.getWidth())/2f, (height- hourHandBitmap.getHeight())/2f, null);
        canvas.restore();

        // 绘制分针
        canvas.save();
        canvas.rotate(minuteAngle, width / 2f, height / 2f);
        canvas.drawBitmap(minuteHandBitmap, (width- minuteHandBitmap.getWidth())/2f, (height- minuteHandBitmap.getHeight())/2f, null);
        canvas.restore();

        // 绘制秒针
        canvas.save();
        canvas.rotate(secondAngle, width / 2f, height / 2f);
        canvas.drawBitmap(secondHandBitmap, (width- secondHandBitmap.getWidth())/2f,(height- secondHandBitmap.getHeight())/2f, null);
        canvas.restore();

        // 重绘
        postInvalidateDelayed(1000);


    }
}