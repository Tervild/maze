package com.example.course;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Player extends Rectangle {
    private int pointX;
    private int pointY;

    private boolean isPlayer = false;

    public Player(){
        super(Config.SIZE_SQUARE, Config.SIZE_SQUARE);
        setX(Config.START_X);
        setY(Config.START_Y);
        setFill(Color.rgb(0,0,0));
        isPlayer = true;
    }

    public boolean isPlayer() {
        return isPlayer;
    }


}
