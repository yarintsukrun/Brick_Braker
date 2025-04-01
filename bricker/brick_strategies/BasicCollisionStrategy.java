package bricker.brick_strategies;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Class of the basic Collision Strategy, the brick will only disappear
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    GameObjectCollection gameObject;
    private final Counter bricksCounter;

    /**
     * Constructor of basic Collision Strategy
     * @param gameObject gameObject
     * @param hitCounter hitCounter
     */
    public BasicCollisionStrategy(GameObjectCollection gameObject, Counter hitCounter){
        this.gameObject = gameObject;
        this.bricksCounter = hitCounter;
    }

    /**
     * basic Collision Strategy, the brick will only disappear
     * @param obj1 brick
     * @param obj2 other
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        if (obj1.getTag().equals("Brick") && gameObject.removeGameObject(obj1)){
            bricksCounter.decrement();
        }
    }
}
