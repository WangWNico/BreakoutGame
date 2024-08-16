// BreakoutController.java
package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.connection.Connection;
import edu.rpi.cs.csci4963.finalproject.commands.WinGameCommand;
import edu.rpi.cs.csci4963.finalproject.model.Ball;
import edu.rpi.cs.csci4963.finalproject.model.Brick;
import edu.rpi.cs.csci4963.finalproject.model.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import static edu.rpi.cs.chane5.Utils.alertInfo;

/**
 * The BreakoutController class handles the game logic and user interactions for the Breakout game.
 * It manages the game state, updates the game objects, and processes user input.
 */
public class BreakoutController {
    private static BreakoutController breakoutController;
    @FXML
    private Button buttonRestart;
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
    private double gameWidth;
    private Timeline timeline;

    /**
     * Gets the singleton instance of BreakoutController.
     *
     * @return the BreakoutController instance
     */
    public static BreakoutController get() {
        return breakoutController;
    }

    /**
     * Initializes the game by setting up the game objects, starting the game loop, and setting up event handlers.
     */
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

        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();
        rootPane.setOnKeyPressed(this::handleKeyPressed);

        breakoutController = this;
    }

    /**
     * The main game loop that updates the game state and renders the game objects.
     */
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

    /**
     * Handles the game won scenario by displaying a message and stopping the game.
     */
    private void gameWon() {
        isGameOver = true;
        alertInfo("Game Won", "You won the game!");
    }

    /**
     * Resets the ball to its initial position and state.
     */
    private void resetBall() {
        balls.clear();
        balls.add(new Ball(500, 400, 10, 5, 5));
    }

    /**
     * Handles the game over scenario by displaying a message and sending a WinGameCommand.
     */
    private void gameOver() {
        isGameOver = true;

        Connection connection = BreakoutApplication.get().getConnection();
        if (connection != null)
            connection.send(new WinGameCommand());

        alertInfo("Game Over", "You lost!");
    }

    /**
     * Handles key press events to control the paddle and pause the game.
     *
     * @param event the KeyEvent representing the key press
     */
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

    /**
     * Handles the pause toggle action.
     */
    @FXML
    private void handleTogglePause() {
        togglePause();
    }

    /**
     * Handles the restart action by resetting the game state and objects.
     */
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

    /**
     * Toggles the pause state of the game.
     */
    private void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * Spawns extra balls at the specified position.
     *
     * @param x the x-coordinate of the spawn position
     * @param y the y-coordinate of the spawn position
     */
    private void spawnExtraBalls(double x, double y) {
        for (int i = 0; i < 3; i++) {
            Ball ball = new Ball(x, y, 10, random.nextDouble() * 4, random.nextDouble() * 4);
            ball.setSpawned(true);
            balls.add(ball);
        }
    }

    /**
     * Checks if all bricks are destroyed.
     *
     * @return true if all bricks are destroyed, false otherwise
     */
    private boolean checkAllBricksDestroyed() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles the end game scenario in a multiplayer game.
     */
    public void multiplayerEndGame() {
        timeline.stop();
        isGameOver = true;
        alertInfo("Game Over", "The opponent broke all their bricks first!");
    }

    /**
     * Handles the win game scenario in a multiplayer game.
     */
    public void multiplayerWinGame() {
        timeline.stop();
        isGameOver = true;
        alertInfo("Game Won", "The opponent lost all their lives first!");
    }

    /**
     * Hides the restart button.
     */
    public void hideRestartButton() {
        buttonRestart.visibleProperty().set(false);
    }
}