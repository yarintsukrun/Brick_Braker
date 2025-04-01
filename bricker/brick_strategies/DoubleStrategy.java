package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

/**
 * a class for allowing double strategy brick
 */
public class DoubleStrategy implements CollisionStrategy{
    private final CollisionStrategy collisionStrategy1;
    private final CollisionStrategy collisionStrategy2;
    private final GameObjectCollection gameObjects;

    /**
     * contractor
     * @param collisionStrategy1 collisionStrategy1
     * @param collisionStrategy2 collisionStrategy2
     * @param gameObjects gameObjects
     */
    public DoubleStrategy(CollisionStrategy collisionStrategy1,
                          CollisionStrategy collisionStrategy2, GameObjectCollection gameObjects){
        this.collisionStrategy1 = collisionStrategy1;
        this.collisionStrategy2 = collisionStrategy2;
        this.gameObjects = gameObjects;
    }

    /**
     * collision function to activate the other collisions
     * @param obj1 brick
     * @param obj2 other
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        // making a clone so I could activate the second start
        for (GameObject gameObject : gameObjects){
            if (gameObject.equals(obj1)){
                GameObject clone = new GameObject(obj1.getTopLeftCorner(),obj1.getDimensions(),
                        null);
                gameObjects.addGameObject(clone);
                collisionStrategy1.onCollision(obj1,obj2);
                collisionStrategy2.onCollision(clone,obj2);
                gameObjects.removeGameObject(clone);
            }
        }

    }
}
