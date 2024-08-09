package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.csci4963.finalproject.model.Ball;
import edu.rpi.cs.csci4963.finalproject.model.Brick;
import edu.rpi.cs.csci4963.finalproject.model.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class BreakoutController {
    @FXML
    private Canvas gameCanvas;
    @FXML
    private BorderPane rootPane;
    @FXML
    private Text remainingBallsText;

    private GraphicsContext gc;
    private Paddle paddle;
    private Ball ball;
    private Brick[] bricks;
    private int remainingBalls = 3;
    private boolean isGameOver = false; // Flag to indicate game over

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        paddle = new Paddle(450, 750, 100, 10);
        ball = new Ball(500, 400, 10, 2, 2);
        bricks = new Brick[30];
        for (int i = 0; i < 30; i++) {
            bricks[i] = new Brick((i % 10) * 100, (i / 10) * 30, 100, 30);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> gameCanvas.setWidth(newVal.doubleValue()));
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> gameCanvas.setHeight(newVal.doubleValue()));

        // Ensure rootPane is focusable and request focus
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();
        System.out.println("rootPane is focusable: " + rootPane.isFocusTraversable());
        System.out.println("rootPane has focus: " + rootPane.isFocused());

        // Add a key event listener to rootPane
        rootPane.setOnKeyPressed(this::handleKeyPressed);
        System.out.println("Key event listener added to rootPane");

        // Add focus listener to rootPane
        rootPane.focusedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("rootPane focus changed: " + newVal);
        });
    }

    private void run() {
        if (isGameOver) {
            return; // Stop the game loop if the game is over
        }

        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        paddle.draw(gc);
        ball.draw(gc);
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(gc);
            }
        }
        ball.move();
        ball.checkCollision(paddle, bricks);
        if (ball.isOutOfBounds()) {
            remainingBalls--;
            remainingBallsText.setText("Remaining Balls: " + remainingBalls);
            if (remainingBalls > 0) {
                resetBall();
            } else {
                gameOver();
            }
        }
    }

    private void resetBall() {
        ball = new Ball(500, 400, 10, 2, 2); // Adjusted reset position
    }

    private void gameOver() {
        isGameOver = true; // Set the game over flag
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36)); // Set font to bold and size 36
        gc.fillText("Game Over", 450, 400); // Adjusted game over text position
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        System.out.println("Key pressed: " + event.getCode());
        if (isGameOver) {
            return;
        }
        switch (event.getCode()) {
            case LEFT -> paddle.moveLeft();
            case RIGHT -> paddle.moveRight();
        }
    }
}