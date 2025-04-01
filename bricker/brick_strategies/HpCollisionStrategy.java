package bricker.brick_strategies;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.health.Health;
import static main.finals.*;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import bricker.health.HealthManager;
import bricker.gameobjects.Paddle;

/**
 * Class of the Disk Collision Strategy, the brick will disappear and Heart will be released
 * from a brick for the main paddle to catch
 */
public class HpCollisionStrategy  implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;
    private final Renderable renderable;
    private final Paddle mainPaddle;
    private final HealthManager healthBar;

    /**
     * Constructor for the class
     * @param gameObjects gameObjects
     * @param brickCounter brickCounter
     * @param renderable renderable
     * @param mainPaddle mainPaddle
     * @param healthBar healthBar
     */
    public HpCollisionStrategy(GameObjectCollection gameObjects, Counter brickCounter,
                               Renderable renderable, Paddle mainPaddle, HealthManager healthBar) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.renderable = renderable;
        this.mainPaddle = mainPaddle;
        this.healthBar = healthBar;
    }

    /**
     * the brick will disappear and Heart will be released
     * from a brick for the main paddle to catch
     * @param obj1 brick
     * @param obj2 other
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        if (gameObjects.removeGameObject(obj1)) {
            if (obj1.getTag().equals("Brick")) {
                brickCounter.decrement();
            }
            Health health = new Health(obj1.getCenter(), new Vector2(HEART_SIZE, HEART_SIZE),
                    renderable, gameObjects, mainPaddle, healthBar);
            health.setVelocity(new Vector2(0, 100));
            gameObjects.addGameObject(health);
        }
    }
}
