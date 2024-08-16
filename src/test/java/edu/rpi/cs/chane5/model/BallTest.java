package edu.rpi.cs.chane5.model;

import edu.rpi.cs.csci4963.finalproject.model.Ball;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.rpi.cs.csci4963.finalproject.model.Paddle;

public class BallTest {

    @Test
    public void testBallInitialization() {
        Ball ball = new Ball(100, 100, 10, 5, 5);
        assertEquals(100, ball.getX(), "Ball x-coordinate should be initialized correctly");
        assertEquals(100, ball.getY(), "Ball y-coordinate should be initialized correctly");
        assertEquals(10, ball.getRadius(), "Ball radius should be initialized correctly");
        assertEquals(5, ball.getDx(), "Ball dx should be initialized correctly");
        assertEquals(5, ball.getDy(), "Ball dy should be initialized correctly");
    }

    @Test
    public void testBallMove() {
        Ball ball = new Ball(100, 100, 10, 5, 5);
        ball.move();
        assertEquals(105, ball.getX(), "Ball should move 5 units in x-direction");
        assertEquals(105, ball.getY(), "Ball should move 5 units in y-direction");
    }

    @Test
    public void testBallOutOfBounds() {
        Ball ball = new Ball(100, 790, 10, 0, 15);
        ball.move();
        assertTrue(ball.isOutOfBounds(), "Ball should be out of bounds when it moves below the canvas");
    }


}
