package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.*;
import bricker.gameobjects.Ball;
import bricker.health.HealthManager;
import bricker.gameobjects.Paddle;
import java.util.Random;
import static main.finals.*;

/**
 * This class will Create strategies for the game
 */
public class CollisionStrategyFactory {
    private static final Random RANDOM = new Random();
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Paddle mainPaddle;
    private final HealthManager healthBar;
    private final SoundReader soundReader;
    private final Ball ball;
    private final TurboCollisionStrategy turboCollisionStrategy;

    /**
     * Constructor of the Collision Factory
     * @param gameObjects gameObjects
     * @param bricksCounter bricksCounter
     * @param imageReader imageReader
     * @param inputListener inputListener
     * @param mainPaddle the main paddle
     * @param healthManager healthManager
     * @param soundReader soundReader
     * @param ball the ball who collided ball
     * @param turboFlag turboFlag
     */
    public CollisionStrategyFactory(GameObjectCollection gameObjects,
                                    Counter bricksCounter,
                                    ImageReader imageReader,
                                    UserInputListener inputListener,
                                    Paddle mainPaddle,
                                    HealthManager healthManager,
                                    SoundReader soundReader,
                                    Ball ball, Counter turboFlag) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.mainPaddle = mainPaddle;
        this.healthBar = healthManager;
        this.soundReader = soundReader;
        this.ball = ball;
        this.turboCollisionStrategy = new TurboCollisionStrategy(
                gameObjects,
                ball,
                imageReader.readImage("assets/redball.png", true),turboFlag, bricksCounter);
    }

    /**
     * Factory method for creating a random strategy
     * @param round indicator for the recursive
     * @return the chosen CollisionStrategy
     */
    private CollisionStrategy getSpecialStrategy(Counter round) {
        int strategyIndex = RANDOM.nextInt(0,5);
        switch (strategyIndex) {
            case 3 -> {
                round.increment();
                return new PuckCollisionStrategy(
                gameObjects,
                new Vector2(BALL_SIZE * PUCK_BALL_FACTORIAL, BALL_SIZE * PUCK_BALL_FACTORIAL),
                imageReader.readImage("assets/mockBall.png", true),
                soundReader.readSound("assets/blop.wav"),
                        PUCK_BALL_AMOUNT, bricksCounter);
            }
            case 1 -> {
                round.increment();
                return new DiskCollisionStrategy(
                gameObjects,
                imageReader.readImage("assets/paddle.png", false),
                inputListener,
                bricksCounter
        );
            }
            case 2 -> {
                round.increment();
                return turboCollisionStrategy;
            }
            case 0 -> {
                round.increment();
                return new HpCollisionStrategy(
                gameObjects,
                bricksCounter,
                imageReader.readImage("assets/heart.png", false),
                mainPaddle,
                healthBar
        );
            }
            case 4 -> {
                return createDoubleStrategy(gameObjects, round);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Creating a random strategy
     * @Return: random CollisionStrategy
     */
    public CollisionStrategy getRandomStrategy(){
        boolean random = RANDOM.nextBoolean();
        if (random){
            return new BasicCollisionStrategy(gameObjects, bricksCounter);
        }
        else {
            Counter round = new Counter(0);
            return getSpecialStrategy(round);
        }
    }


    private CollisionStrategy createDoubleStrategy(GameObjectCollection gameObjects, Counter round) {
        CollisionStrategy strategy1 = getSpecialStrategy(round);
        if (round.value() < 3){
            CollisionStrategy strategy2 = getSpecialStrategy(round);
            return new DoubleStrategy(strategy1, strategy2, gameObjects);
        }
        else {
            CollisionStrategy strategy2 = new BasicCollisionStrategy(gameObjects, bricksCounter);
            return new DoubleStrategy(strategy1, strategy2, gameObjects);
        }
    }

    /**
     * shooting down turbo
     */
    public void shutDownTurbo(){
        turboCollisionStrategy.shutDownTurbo(ball);
    }
}
