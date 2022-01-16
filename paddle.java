package com.example.arkanoid;

import android.graphics.Rect;

public class paddle {

    private Rect paddle;
    private int width;
    private int height;
    private int xSpeed;
    private int x;
    private int y;

    public paddle(int xScreen, int yScreen){
        this.width = 150;
        this.height = 50;
        this.x = xScreen/2;
        this.y = yScreen - height - 100;
        this.paddle = new Rect(x, y, width + x, height + y);
        this.xSpeed = 15;
    }
    public Rect getRect(){
        return paddle;
    }

    public void setX(int x2){this.x = x2;}

    public void update(){
        paddle.left = x;
        paddle.right = x + width;
    }
}
