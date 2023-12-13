package com.jnu.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



// GameViewFragment.java
public class GameViewFragment extends Fragment {

    private SurfaceView surfaceView; // 游戏视图
    private SurfaceHolder holder; // 视图控制器
    private GameThread gameThread; // 游戏线程
    private Bitmap bookBitmap; // 书本图片
    private List<GameSpirit> books; // 书本列表
    private Random random; // 随机数生成器
    private int score; // 学到的书本数
    private int countDown; // 倒计时
    private boolean isLive; // 游戏是否进行中
    private float touchX, touchY; // 触摸点坐标

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_view, container, false);
        // 初始化视图
        surfaceView = view.findViewById(R.id.gameview);
        holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 视图创建时，启动游戏线程
                gameThread = new GameThread();
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // 视图改变时，更新游戏精灵的边界
                for (GameSpirit book : books) {
                    book.updateBounds(width, height);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 视图销毁时，停止游戏线程
                gameThread.stopThread();
            }
        });
        // 初始化图片
        bookBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.book_1);
        // 初始化列表
        books = new ArrayList<>();
        // 初始化随机数生成器
        random = new Random();
        // 初始化分数和倒计时
        score = 0;
        countDown = 30;
        // 初始化游戏状态
        isLive = true;
        // 初始化触摸点
        touchX = touchY = -1;
        // 设置触摸监听器
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获取触摸点坐标
                touchX = event.getX();
                touchY = event.getY();
                return true;
            }
        });
        return view;
    }

    // 游戏线程类
    private class GameThread extends Thread {
        private boolean running; // 线程是否运行中
        public GameThread() {
            // 初始化线程状态
            running = true;
        }

        @Override
        public void run() {
            // 执行游戏逻辑
            while (isLive && running) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.rgb(240, 240, 240));

                    Paint paint = new Paint();
                    paint.setColor(Color.rgb(0, 0, 0));
                    paint.setTextSize(50f);
                    canvas.drawText("学习书本数： " + score, 50f, 50f, paint);
                    canvas.drawText("剩余时间： " + countDown + "秒", 50f, 120f, paint);

                    for (GameSpirit book : books) {
                        book.detectTouch();
                        book.draw(canvas);
                        book.move();
                    }
                    // 重置触摸点
                    touchX = touchY = -1;

                    // 倒计时结束
                    if (countDown == 0)
                        isLive = false;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(5);
                    // 计时器
                    if (System.currentTimeMillis() % 1000 == 0) {
                        countDown--;
                    }
                    // 生成新的书本
                    if (System.currentTimeMillis() % 1000== 0) {
                        // 随机生成书本的位置和速度
                        int x = random.nextInt(bookBitmap.getWidth());
                        int y = random.nextInt( bookBitmap.getHeight());
                        int speedX = random.nextInt(20) - 10;
                        int speedY = random.nextInt(20) - 10;
                        // 创建一个新的游戏精灵对象
                        GameSpirit book = new GameSpirit(bookBitmap, x, y, speedX, speedY);
                        // 将游戏精灵添加到列表中
                        books.add(book);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 停止线程的方法
        public void stopThread() {
            running = false;
        }
    }

    // 游戏精灵类
    private class GameSpirit {

        private Bitmap bitmap; // 图片
        private float x, y; // 坐标
        private float speedX, speedY; // 速度
        private float direction; // 方向
        private int width, height; // 边界

        public GameSpirit(Bitmap bitmap, float x, float y, float speedX, float speedY) {
            // 初始化属性
            this.bitmap = bitmap;
            this.x = x;
            this.y = y;
            this.speedX = speedX;
            this.speedY = speedY;
            this.direction = (float) Math.atan2(speedY, speedX);
        }

        // 绘制图片的方法
        public void draw(Canvas canvas) {
            // 确定边界
            width = canvas.getWidth();
            height = canvas.getHeight();
            // 绘制
            canvas.drawBitmap(bitmap, x, y, null);
        }

        // 移动图片的方法
        public void move() {
            // 按照当前方向进行移动
            x += Math.cos(direction) * speedX;
            y += Math.sin(direction) * speedY;
            // 边界碰撞检测
            if (x < 0) {
                x = 0;
                direction = (float) (Math.PI - direction);
            }
            if (x > width - bitmap.getWidth()) {
                x = width - bitmap.getWidth();
                direction = (float) (Math.PI - direction);
            }
            if (y < 0) {
                y = 0;
                direction = -direction;
            }
            if (y > height - bitmap.getHeight()) {
                y = height - bitmap.getHeight();
                direction = -direction;
            }
            // 5%的概率改变方向
            if (Math.random() < 0.05)
                direction = (float) (Math.random() * 2 * Math.PI);
        }

        // 检测是否被触摸的方法
        public void detectTouch() {
            // 点击检测
            if (touchX >= 0 && touchY >= 0) {
                // 点击到了
                if (touchX >= x && touchX <= x + bitmap.getWidth() &&
                        touchY >= y && touchY <= y + bitmap.getHeight()) {
                    touchX = -1;
                    touchY = -1;
                    // 移除该书本
                    books.remove(this);
                    // 分数加一
                    score++;
                }
            }
        }

        // 更新边界的方法
        public void updateBounds(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}




