package com.example.arkanoid;

import android.graphics.Rect;

public class ball {
    private Rect ball;
    private int width;
    private int height;
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public ball(int xScreen, int yScreen){
        this.width = 30;
        this.height = 30;
        this.x = xScreen/2;
        this.y = yScreen/2;
        this.xSpeed = 10;
        this.ySpeed = 10;
        this.ball = new Rect(x, y, x+width, y+height);
    }
    public Rect getRect(){
        return ball;
    }

    public void reverseXSpeed(){xSpeed = -xSpeed;}
    public void reverseYSpeed(){ySpeed = -ySpeed;}

    public void update(){
        ball.left = ball.left + xSpeed;
        ball.top = ball.top + ySpeed;
        ball.right = ball.left + width;
        ball.bottom = ball.top + height;

    }
    public void restartBall(int xScreen, int yScreen){
        ball.left = xScreen/2;
        ball.top = yScreen/2;
        ball.right = ball.left + width;
        ball.bottom = ball.top + height;
    }
}
