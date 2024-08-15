package edu.rpi.cs.csci4963.finalproject.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Brick {
    private double x, y, width, height;
    private boolean destroyed;
    private boolean extraBallsSpawned;
    private Image texture;

    public Brick(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
        this.texture = new Image(imagePath);
    }

    public void draw(GraphicsContext gc) {
        if (!destroyed) {
            gc.drawImage(texture, x, y, width, height);
        }
        gc.setStroke(Color.WHITE);
        gc.strokeRect(x, y, width, height);
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

    public boolean breakBrick() {
        if (!destroyed) {
            destroyed = true;
            return true;
        }
        return false;
    }

    public boolean isExtraBallsSpawned() {
        return extraBallsSpawned;
    }

    public void setExtraBallsSpawned(boolean extraBallsSpawned) {
        this.extraBallsSpawned = extraBallsSpawned;
    }
}