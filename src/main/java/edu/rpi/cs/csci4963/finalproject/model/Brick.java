package edu.rpi.cs.csci4963.finalproject.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick {
    private double x, y, width, height;
    private boolean destroyed;

    public Brick(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
    }

    public void draw(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.RED);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}