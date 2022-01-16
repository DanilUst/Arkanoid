package com.example.arkanoid;

import android.graphics.Rect;

public class brick {
    private Rect brick;
    private boolean alive;

    public brick(int col, int row, int width, int height){
        alive = true;

        brick = new Rect(col * width + 10, row * height + 10,
                col * width + width - 10,
                row * height + height-10);
    }
    public Rect getRect() {return brick;}

    public void setDestroyed(){alive = false;}

    public boolean check(){return alive;}
}
