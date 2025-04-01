package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import static main.finals.*;
import java.util.Random;

/**
 * PuckCollisionStrategy is a class that implements the CollisionStrategy interface.
 * It defines the behavior when a collision occurs, particularly focusing on creating
 * PuckBall objects when a brick is hit and removed from the game.
 */
public class PuckCollisionStrategy implements CollisionStrategy {
    GameObjectCollection gameObject;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final Sound collisionSound;
    private final int ballAmount;
    private final Counter bricksCounter;

    /**
     * Constructor to initialize the PuckCollisionStrategy.
     *
     * @param gameObject A collection of game objects.
     * @param dimensions The dimensions of the PuckBall.
     * @param renderable The renderable representation of the PuckBall.
     * @param collisionSound The sound to be played when a collision occurs.
     * @param ball_amount The number of PuckBall objects to create.
     * @param bricksCounter A counter to keep track of the number of bricks.
     */
    public PuckCollisionStrategy(GameObjectCollection gameObject,
                                 Vector2 dimensions, Renderable renderable,
                                 Sound collisionSound,
                                 int ball_amount, Counter bricksCounter) {
        this.gameObject = gameObject;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.collisionSound = collisionSound;
        ballAmount = ball_amount;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Method to handle the behavior when a collision occurs between two game objects.
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        // Remove obj1 from the game object collection
        if (gameObject.removeGameObject(obj1)) {
            // If obj1 is a brick, decrement the brick counter
            if (obj1.getTag().equals("Brick")) {
                bricksCounter.decrement();
            }
            // If there are still bricks left, create PuckBall objects
            if (bricksCounter.value() != 0) {
                for (int i = 0; i < ballAmount; i++) {
                    PuckBall puckBall = new PuckBall(obj1.getCenter(), dimensions, renderable,
                            collisionSound, gameObject);
                    gameObject.addGameObject(puckBall);

                    // Generate random velocities for the PuckBall
                    Random random = new Random();
                    double angle = random.nextDouble() * Math.PI;
                    float velocityX = (float) Math.cos(angle) * BALL_SPEED;
                    float velocityY = (float) Math.sin(angle) * BALL_SPEED;
                    puckBall.setVelocity(new Vector2(velocityX, velocityY));
                }
            }
        }
    }
}
