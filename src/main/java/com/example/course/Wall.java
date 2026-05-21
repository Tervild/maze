package com.example.course;
import javafx.scene.shape.Rectangle;
public class Wall {
    private Rectangle square;
    private int active = 0;
    private int x, y;
    private int previousX;
    private int PreviousY;
    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
        square = new Rectangle(Config.SIZE_SQUARE, Config.SIZE_SQUARE);
        square.setX(x);
        square.setY(y);
        Action.fillSquare(square, false);
    }
    public Rectangle getSquare() {
        return square;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getPreviousX() {
        return previousX;
    }
    public void setPrevious(int X, int Y) {
        previousX = X;
        PreviousY = Y;
    }
    public int getPreviousY() {
        return PreviousY;
    }
}


