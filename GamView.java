package com.example.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GamView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GameView(this));
    }

    class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private DrawThread drawThread;
        private ball ball;
        private paddle paddle;
        private Paint paint;
        private Canvas canvas;
        private int xScreen;
        private int yScreen;
        private int x2 = 0;

        private int lives = 3;
        private int score = 0;
        private int win = 0;
        private int lose = 0;


        private brick[] bricks = new brick[36];
        private int Bricks;
        private int numRows;
        private int bricksInRows;
        private int countDestroyedBricks = 0;


        public GameView(Context context){

            super(context);
            paint = new Paint();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();

            display.getSize(size);
            xScreen = size.x;
            yScreen = size.y;

            paddle = new paddle(xScreen, yScreen);
            ball = new ball(xScreen, yScreen);

            bricksInRows = 9;
            numRows = 4;

            int brickwidth = 120;
            int brickHeight = 90;
            Bricks = 0;

            for(int i = 0; i < numRows; i++){
                for(int j = 0; j < bricksInRows; j++){
                    bricks[Bricks] = new brick(j, i, brickwidth, brickHeight);
                    Bricks++;
                }
            }

            getHolder().addCallback(this);
        }



        public void update(){

            paddle.update();
            ball.update();

            for(int i = 0; i < Bricks; i++){

                if(i == 0 || i == 3 || i == 6 || i == 9 || i == 20
                        || i == 11 || i == 14 || i == 23  || i == 30 || i == 34) {
                    if(bricks[i].check()){
                        if(Rect.intersects(bricks[i].getRect(), ball.getRect())){
                            bricks[i].setDestroyed();
                            ball.reverseYSpeed();
                            score += 30;
                            countDestroyedBricks++;
                        }
                    }
                }

                if(i == 1 || i == 4 || i == 8 || i == 12 || i == 17 || i == 22
                        || i == 26 || i == 28 || i == 32 || i == 33) {
                    if(bricks[i].check()){
                        if(Rect.intersects(bricks[i].getRect(), ball.getRect())){
                            bricks[i].setDestroyed();
                            ball.reverseYSpeed();
                            score += 60;
                            countDestroyedBricks++;
                        }
                    }
                }
                if(i == 5 || i == 7 || i == 13 || i == 16 || i == 19 || i == 21
                        || i == 24 || i == 27 || i == 35 || i == 31) {
                    if(bricks[i].check()){
                        if(Rect.intersects(bricks[i].getRect(), ball.getRect())){
                            bricks[i].setDestroyed();
                            ball.reverseYSpeed();
                            score += 90;
                            countDestroyedBricks++;
                        }
                    }
                }

                if(i == 10 || i == 16 || i == 28 || i == 2 || i == 15 || i == 18
                        || i == 25 || i == 29 || i == 36) {
                    if(bricks[i].check()){
                        if(Rect.intersects(bricks[i].getRect(), ball.getRect())){
                            bricks[i].setDestroyed();
                            ball.reverseYSpeed();
                            score += 120;
                            countDestroyedBricks++;
                        }
                    }
                }
            }

            if(Rect.intersects(paddle.getRect(), ball.getRect())){
                ball.reverseYSpeed();
            }
            if(ball.getRect().right + 30 > xScreen){
                ball.reverseXSpeed();
            }
            if(ball.getRect().left - 30 < 0){
                ball.reverseXSpeed();
            }
            if(ball.getRect().bottom + 30 > yScreen){
                ball.reverseYSpeed();
                lives--;
            }
            if(ball.getRect().top - 30 < 0){
                ball.reverseYSpeed();
            }
            if(lives == 0){
                restart();
                lose++;
            }
            if(countDestroyedBricks == 36){
                restart();
                win++;
            }
        }
        public void restart()
        {
            ball.restartBall(xScreen, yScreen);
            int brickwidth = 120;
            int brickHeight = 90;
            Bricks = 0;

            for(int i = 0; i < numRows; i++){
                for(int j = 0; j < bricksInRows; j++){
                    bricks[Bricks] = new brick(j, i, brickwidth, brickHeight);
                    Bricks++;
                }
            }

            lives = 3;
            countDestroyedBricks = 0;
        }
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while(retry){
                try{
                    drawThread.join();
                    retry = false;

                }catch (InterruptedException e){

                }
            }
        }

        class DrawThread extends Thread{
            private boolean running = false;
            private SurfaceHolder surfaceHolder;

            public DrawThread(SurfaceHolder surfaceHolder){
                this.surfaceHolder = surfaceHolder;
            }
            public void setRunning(boolean running){
                this.running = running;
            }

            @Override
            public void run() {
                //Canvas canvas;
                while(running){
                    canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas(null);
                        if(canvas == null)
                            continue;

                        update();

                        canvas.drawColor(Color.WHITE);

                        paint.setColor(Color.argb(255, 0, 0, 255));
                        canvas.drawRect(paddle.getRect(), paint);

                        paint.setColor(Color.argb(255, 0, 255, 0));
                        canvas.drawRect(ball.getRect(), paint);

                        for(int i = 0; i < Bricks; i++){
                            if(i == 0 || i == 3 || i == 6 || i == 9 || i == 20
                                    || i == 11 || i == 14 || i == 23  || i == 30 || i == 34) {
                                paint.setColor(Color.RED);
                                if (bricks[i].check()) {
                                    canvas.drawRect(bricks[i].getRect(), paint);
                                }
                            }
                            if(i == 1 || i == 4 || i == 8 || i == 12 || i == 17 || i == 22
                                    || i == 26 || i == 28 || i == 32 || i == 33) {
                                paint.setColor(Color.BLUE);
                                if (bricks[i].check()) {
                                    canvas.drawRect(bricks[i].getRect(), paint);
                                }
                            }

                            if(i == 5 || i == 7 || i == 13 || i == 16 || i == 19 || i == 21
                                    || i == 24 || i == 27 || i == 35 || i == 31) {
                                paint.setColor(Color.GREEN);
                                if (bricks[i].check()) {
                                    canvas.drawRect(bricks[i].getRect(), paint);
                                }
                            }
                            if(i == 10 || i == 16 || i == 28 || i == 2 || i == 15 || i == 18
                                    || i == 25 || i == 29 || i == 36) {
                                paint.setColor(Color.YELLOW);
                                if (bricks[i].check()) {
                                    canvas.drawRect(bricks[i].getRect(), paint);
                                }
                            }
                        }

                        paint.setColor(Color.argb(255, 0, 0, 0));
                        paint.setTextSize(50);
                        paint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText("score: " + score + ",  lives: " + lives, 30, yScreen - 30, paint);


                        paint.setTextSize(50);
                        paint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText("wins: " + win + ",  loses: " + lose, xScreen - 30, yScreen - 30, paint);

                    }finally{
                        if(canvas != null){
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            x2 = (int)event.getX();
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                paddle.setX(x2);
            }
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Do you want to quit the game?");
        builder.setPositiveButton("YES", (dialogInterface, i) -> finish());
        builder.setNegativeButton("NO", (dialogInterface, i) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
