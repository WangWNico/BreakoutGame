// BreakoutController.java
package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.csci4963.finalproject.model.Ball;
import edu.rpi.cs.csci4963.finalproject.model.Brick;
import edu.rpi.cs.csci4963.finalproject.model.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.rpi.cs.chane5.Utils.alertInfo;

public class BreakoutController {
    @FXML
    private Canvas gameCanvas;
    @FXML
    private BorderPane rootPane;
    @FXML
    private Text remainingBallsText;

    private GraphicsContext gc;
    private Paddle paddle;
    private List<Ball> balls;
    private Brick[] bricks;
    private int remainingBalls = 3;
    private boolean isGameOver = false;
    private Random random = new Random();
    private boolean isPaused = false;
    private double  gameWidth;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();

        paddle = new Paddle(450, 700, 200, 40);
        balls = new ArrayList<>();
        balls.add(new Ball(500, 400, 10, 5, 5));

        gameWidth = gameCanvas.getWidth();
        double brickWidth = 100;
        double brickHeight = 40;
        int bricksPerRow = (int) (gameWidth / brickWidth);
        int numberOfRows = 5;

        bricks = new Brick[bricksPerRow * numberOfRows];
        for (int i = 0; i < bricks.length; i++) {
            double x = (i % bricksPerRow) * brickWidth;
            double y = (i / bricksPerRow) * brickHeight;
            String brick = "file:src/main/resources/edu/rpi/cs/csci4963/finalproject/brick.jpg";
            bricks[i] = new Brick(x + 7, y + 10, brickWidth, brickHeight, brick);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();
        rootPane.setOnKeyPressed(this::handleKeyPressed);
    }

    private void run() {
        if (isGameOver || isPaused) {
            return;
        }
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        paddle.draw(gc);
        for (Ball ball : balls) {
            ball.draw(gc);
        }
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(gc);
            }
        }
        for (Ball ball : balls) {
            ball.move();
            if (ball.checkCollision(paddle, bricks)) {
                for (Brick brick : bricks) {
                    if (brick.isDestroyed() && !brick.isExtraBallsSpawned()) {
                        brick.setExtraBallsSpawned(true);
                        if (random.nextDouble() < 0.2) {
                            spawnExtraBalls(brick.getX() + brick.getWidth() / 2, brick.getY() + brick.getHeight() / 2);
                        }
                        if (random.nextDouble() < 0.1) {
                            paddle.increaseWidth();
                        }
                    }
                }
            }
            if (ball.isOutOfBounds() && !ball.isSpawned()) {
                remainingBalls--;
                remainingBallsText.setText("Remaining Balls: " + remainingBalls);
                if (remainingBalls > 0) {
                    resetBall();
                } else {
                    gameOver();
                }
            } else if (ball.isOutOfBounds() && ball.isSpawned()) {
                balls.remove(ball);
            }
            if (checkAllBricksDestroyed()) {
                gameWon();
                togglePause();
            }
        }
    }

    private void gameWon() {
        isGameOver = true;
        alertInfo("Game Won", "You won the game!");
    }

    private void resetBall() {
        balls.clear();
        balls.add(new Ball(500, 400, 10, 5, 5));
    }

    private void gameOver() {
        isGameOver = true;
        alertInfo("Game Over", "You lost!");
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (isGameOver) {
            return;
        }
        switch (event.getCode()) {
            case LEFT -> paddle.moveLeft();
            case RIGHT -> paddle.moveRight();
            case SPACE -> togglePause();
        }
    }

    @FXML
    private void handleTogglePause() {
        togglePause();
    }

    @FXML
    private void handleRestart() {
        isGameOver = false;
        isPaused = false;
        remainingBalls = 3;
        remainingBallsText.setText("Remaining Balls: " + remainingBalls);
        resetBall();
        for (Brick brick : bricks) {
            brick.setDestroyed(false);
            brick.setExtraBallsSpawned(false);
        }
        rootPane.requestFocus();
    }

    private void togglePause() {
        isPaused = !isPaused;
    }

    private void spawnExtraBalls(double x, double y) {
        for (int i = 0; i < 3; i++) {
            Ball ball = new Ball(x, y, 10, random.nextDouble() * 4, random.nextDouble() * 4);
            ball.setSpawned(true);
            balls.add(ball);
        }
    }

    private boolean checkAllBricksDestroyed() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}