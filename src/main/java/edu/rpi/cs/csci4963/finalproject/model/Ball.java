package edu.rpi.cs.csci4963.finalproject.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y, radius, dx, dy;

    public Ball(double x, double y, double radius, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < radius || x > 800 - radius) dx = -dx;
        if (y < radius) dy = -dy;
    }

    public boolean isOutOfBounds() {
        return y > 600; // Assuming the bottom of the frame is at y = 600
    }

    public void checkCollision(Paddle paddle, Brick[] bricks) {
        if (x > paddle.getX() && x < paddle.getX() + paddle.getWidth() && y + radius > paddle.getY()) {
            dy = -dy;
        }
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && x > brick.getX() && x < brick.getX() + brick.getWidth() && y - radius < brick.getY() + brick.getHeight()) {
                dy = -dy;
                brick.setDestroyed(true);
            }
        }
    }
}