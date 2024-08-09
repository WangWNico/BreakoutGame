package edu.rpi.cs.csci4963.finalproject.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {
    private double x, y, width, height;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, width, height);
    }

    public void moveLeft() {
        if (x > 0) x -= 20;
    }

    public void moveRight() {
        if (x + width < 800) x += 20;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}