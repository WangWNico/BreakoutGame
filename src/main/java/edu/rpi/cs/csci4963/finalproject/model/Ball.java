package edu.rpi.cs.csci4963.finalproject.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y, radius, dx, dy;
    private boolean spawned = false;

    public Ball(double x, double y, double radius, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
    }

    public void draw(GraphicsContext gc) {
        if(isSpawned()) {
            gc.setFill(Color.BLUE);
        }
        else {
            gc.setFill(Color.RED);
        }
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < radius || x > 800 - radius) dx = -dx;
        if (y < radius) dy = -dy;
    }

    public boolean isOutOfBounds() {
        return y > 800;
    }

    public boolean checkCollision(Paddle paddle, Brick[] bricks) {
        boolean brickBroken = false;
        if (x > paddle.getX() && x < paddle.getX() + paddle.getWidth() && y + radius > paddle.getY()) {
            dy = -dy;
        }
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && x > brick.getX() && x < brick.getX() + brick.getWidth() && y - radius < brick.getY() + brick.getHeight()) {
                dy = -dy;
                brick.setDestroyed(true);
                brickBroken = true;
            }
        }
        return brickBroken;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getDy() {
        return dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public boolean isSpawned() {
        return spawned;
    }
}