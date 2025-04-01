package bricker.main;
import static main.finals.*;
import bricker.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import bricker.health.HealthManager;
import bricker.brick_strategies.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * BrickerGameManager handles the main logic and game loop of the Bricker game.
 * This class initializes game objects, processes input, and updates the game state.
 */
public class BrickerGameManager extends GameManager {


    private Brick[] bricks;
    private final int cols;
    private final int rows;
    private Ball ball;
    private HealthManager healthManager;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter hitsCounter;
    private final Counter turboFlag;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private Paddle mainPaddle;
    private SoundReader soundReader;
    private CollisionStrategyFactory collisionStrategyFactory;

    /**
     * Constructor for the BrickerGameManager class.
     * Initializes game settings and dimensions.
     * @param windowTitle the Title
     * @param windowDimensions the window dimention
     * @param cols number of cols in the egame
     * @param rows number of rows in the games
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        super(windowTitle, windowDimensions);
        this.bricks = new Brick[cols * rows];
        this.hitsCounter = new Counter(0);
        this.cols = cols;
        this.rows = rows;
        this.windowDimensions = windowDimensions;
        bricksCounter = new Counter(cols * rows);
        this.turboFlag = new Counter(0);
    }

    /**
     * Initializes all game objects for the Bricker game.
     * @param imageReader    Utility to read images from the resources.
     * @param soundReader    Utility to read sounds from the resources.
     * @param inputListener  Handles user keyboard input.
     * @param windowController Controls window-level operations.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        NewGameBuilder();
    }

    /**
     * Updates the game state on each frame.
     * Processes input and manages game object interactions.
     *
     * @param deltaTime Time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (turboFlag.value() != 0 && TURBO_HITS + turboFlag.value() <= ball.getCollisionCounter()) {
            collisionStrategyFactory.shutDownTurbo();
        }
        if (bricksCounter.value() == 0 || inputListener.wasKeyPressedThisFrame(KeyEvent.VK_W)) {
            boolean answer = windowController.messages().openYesNoDialog("You Win! Play again?");
            if (answer) {
                bricksCounter.increment();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        GameDestroyer();
                        NewGameBuilder();
                    }
                }, 50);

            } else {
                windowController.closeWindow();
            }
        } else if (healthManager.getHearts() == 0) {
            boolean answer = windowController.messages().openYesNoDialog("You lose! Play again?");
            if (answer) {
                GameDestroyer();
                NewGameBuilder();
            } else windowController.closeWindow();
        } else if (ball.getCenter().y() - BALL_SIZE > SCREEN_LENGTH) {
            mainPaddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_HEIGHT));
            healthManager.removeHealth();
            lunchBall();
        }
    }


    private void NewGameBuilder(){
        bricks = new Brick[cols * rows];
        bricksCounter = new Counter(cols * rows);
        hitsCounter = new Counter(0);
        creatBall(imageReader, soundReader);
        creatPeddle(imageReader, inputListener);
        creatWallsAndBackground(imageReader);
        HealthBuilder();
        BrickBuilder();
    }

    private void GameDestroyer(){
        gameObjects().forEach(gameObject -> gameObjects().removeGameObject(gameObject, Layer.UI));
        gameObjects().forEach(gameObject -> gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS));
        gameObjects().forEach(gameObject -> gameObjects().removeGameObject(gameObject, Layer.FOREGROUND));
        gameObjects().forEach(gameObject -> gameObjects().removeGameObject(gameObject, Layer.DEFAULT));
        gameObjects().forEach(gameObject -> gameObjects().removeGameObject(gameObject, Layer.BACKGROUND));

    }

    private void HealthBuilder() {
        healthManager = new HealthManager(imageReader, gameObjects());
    }

    private void BrickBuilder() {
        this.collisionStrategyFactory = new CollisionStrategyFactory(
                gameObjects(), bricksCounter, imageReader, inputListener, mainPaddle, healthManager,
                soundReader, ball, turboFlag);
        bricksCounter.reset();
        bricksCounter.increaseBy(cols * rows);
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        float brickWidth = (SCREEN_WIDTH - 4 * BORDER_WIDTH) / cols;
        float brickLength = (SCREEN_LENGTH / 3) / rows;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int index = j * cols + i;
                Vector2 position = new Vector2(2 * BORDER_WIDTH + brickWidth * i,
                        BORDER_WIDTH * 2 + brickLength * j);
                Vector2 dimensions = new Vector2(brickWidth - BORDER_WIDTH,
                        brickLength - BORDER_WIDTH);
                bricks[index] = new Brick(position, dimensions, brickImage, collisionStrategyFactory.getRandomStrategy());
                bricks[index].setTag("Brick");
                gameObjects().addGameObject(bricks[index]);
            }
        }
    }

    private void creatBall(ImageReader imageReader, SoundReader soundReader) {
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage,
                collisionSound, hitsCounter);
        lunchBall();
    }

    private void creatPeddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable peddleImage = imageReader.readImage("assets/paddle.png", false);
        mainPaddle = new Paddle(new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), peddleImage,
                inputListener);
        mainPaddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_HEIGHT));
        mainPaddle.setTag("mainPaddle");
        gameObjects().addGameObject(mainPaddle);
    }

    private void creatWallsAndBackground(ImageReader imageReader) {
        // Creating sealing
        GameObject sealing = new GameObject(Vector2.ZERO, new Vector2(SCREEN_WIDTH, BORDER_WIDTH),
                new RectangleRenderable(Color.CYAN));
        gameObjects().addGameObject(sealing);
        // Creating left wall
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(5, SCREEN_LENGTH),
                new RectangleRenderable(Color.CYAN));
        gameObjects().addGameObject(leftWall);
        // Creating right wall
        GameObject rightWall = new GameObject(new Vector2(SCREEN_WIDTH - BORDER_WIDTH + 1, 0),
                new Vector2(BORDER_WIDTH,
                        SCREEN_LENGTH),
                new RectangleRenderable(Color.CYAN));
        gameObjects().addGameObject(rightWall);
        //Background
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

    }


    /**
     * Launching the ball
     */
    public void lunchBall() {
        Random rand = new Random();
        float ballVecX = BALL_SPEED;
        float ballVecY = BALL_SPEED;
        if (rand.nextBoolean()) {
            ballVecX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVecY *= -1;
        }
        ball.setVelocity(new Vector2(ballVecX, ballVecY));

        ball.setCenter(WINDOW_DIM.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    /**
     * Main
     * @param args program's input
     */
    public static void main(String[] args) {
        Vector2 windowDimensions = new Vector2(SCREEN_WIDTH, SCREEN_LENGTH); // Width: 700, Height: 500
        BrickerGameManager bricker;
        if (args.length == 2) {
            bricker = new BrickerGameManager("Bricker", windowDimensions,
                    Integer.parseInt(args[1]), Integer.parseInt(args[0]));
        } else {
            bricker = new BrickerGameManager("Bricker", windowDimensions, 8, 7);
        }
        // Start the game loop
        bricker.run();
    }
}