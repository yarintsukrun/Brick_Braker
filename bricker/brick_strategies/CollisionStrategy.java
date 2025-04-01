package bricker.brick_strategies;
import danogl.GameObject;

/**
 * Interface of the CollisionStrategy
 */
public interface CollisionStrategy {
    void onCollision(GameObject obj1, GameObject obj2);
}
