package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import bricker.gameobjects.Ball;

/**
 * TurboCollisionStrategy is a class that implements the CollisionStrategy interface.
 * It defines the behavior when a collision occurs, particularly focusing on
 * modifying the ball's properties to a "turbo" state.
 */
public class TurboCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Ball ball;
    private final Renderable regularBallImage;
    private final Renderable TurboBallImage;
    private final Counter turboState;
    private final Counter brickCounter;

    /**
     * Constructor to initialize the TurboCollisionStrategy.
     *
     * @param gameObjects A collection of game objects.
     * @param ball The ball object that can enter a turbo state.
     * @param TurboBallImage The renderable image for the turbo state.
     * @param turboState A counter to track the turbo state.
     * @param brickCounter A counter to keep track of the number of bricks.
     */
    public TurboCollisionStrategy(GameObjectCollection gameObjects, Ball ball,
                                  Renderable TurboBallImage, Counter turboState,
                                  Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.ball = ball;
        regularBallImage = ball.renderer().getRenderable();
        this.TurboBallImage = TurboBallImage;
        this.turboState = turboState;
        this.brickCounter = brickCounter;
    }

    /**
     * Method to handle the behavior when a collision occurs between two game objects.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        // Check if obj2 is a Ball
        if (obj2.getTag().equals("Ball")) {
            // Remove obj1 from the game object collection
            if (gameObjects.removeGameObject(obj1)) {
                // If obj1 is a brick, decrement the brick counter
                if (obj1.getTag().equals("Brick")) {
                    brickCounter.decrement();
                }
                // If turbo state is already active, do nothing
                if (turboState.value() >= 1) {
                    return;
                } else {
                    // Increase ball's velocity and change its image to turbo state
                    ball.setVelocity(ball.getVelocity().mult(1.4f));
                    ball.renderer().setRenderable(TurboBallImage);
                    turboState.increaseBy(ball.getCollisionCounter());
                }
            }
        } else {
            // Remove obj1 from the game object collection
            if (gameObjects.removeGameObject(obj1)) {
                // If obj1 is a brick, decrement the brick counter
                if (obj1.getTag().equals("Brick")) {
                    brickCounter.decrement();
                }
            }
        }
    }

    /**
     * Method to revert the ball back to its regular state.
     *
     * @param ball The ball to revert to regular state.
     */
    public void shutDownTurbo(Ball ball) {
        turboState.reset();
        ball.renderer().setRenderable(regularBallImage);
        ball.setVelocity(ball.getVelocity().mult(1f / 1.4f));
    }
}
