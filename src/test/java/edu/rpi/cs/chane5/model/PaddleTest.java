package edu.rpi.cs.chane5.model;

import edu.rpi.cs.csci4963.finalproject.model.Paddle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaddleTest {

    @Test
    public void testMoveLeft() {
        Paddle paddle = new Paddle(100, 100, 50, 10);
        paddle.moveLeft();
        assertEquals(65, paddle.getX(), "Paddle should move 35 units to the left");
    }

    @Test
    public void testMoveRight() {
        Paddle paddle = new Paddle(700, 100, 50, 10);
        paddle.moveRight();
        assertEquals(735, paddle.getX(), "Paddle should move 35 units to the right");
    }

    @Test
    public void testMoveRightBoundary() {
        Paddle paddle = new Paddle(750, 100, 50, 10);
        paddle.moveRight();
        assertEquals(750, paddle.getX(), "Paddle should not move right beyond boundary");
    }

    @Test
    public void testIncreaseWidth() {
        Paddle paddle = new Paddle(100, 100, 50, 10);
        paddle.increaseWidth();
        assertEquals(60, paddle.getWidth(), "Paddle width should increase by 10 units");
    }
}
