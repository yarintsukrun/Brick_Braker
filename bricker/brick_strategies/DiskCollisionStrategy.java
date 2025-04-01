package bricker.brick_strategies;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import danogl.GameObject;
import static main.finals.*;

/**
 * Class of the Disk Collision Strategy, the brick will disappear and a second disk
 * will appear for limited hits
 */
public class DiskCollisionStrategy  implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final Renderable peddleImage;
    private final UserInputListener inputListener;
    private final Counter bricksCounter;

    /**
     * the constructor of the collision
     * @param gameObjects gameObjects
     * @param peddleImage peddleImage
     * @param inputListener inputListener
     * @param bricksCounter bricksCounter
     */
    public DiskCollisionStrategy(GameObjectCollection gameObjects, Renderable peddleImage,
                                 UserInputListener inputListener, Counter bricksCounter){
        this.gameObjects = gameObjects;
        this.peddleImage = peddleImage;
        this.inputListener = inputListener;
        this.bricksCounter = bricksCounter;
    }

    /**
     * the brick only disappear and a second disk will appear for limited hits
     * @param obj1 brick
     * @param obj2 pther
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        if (gameObjects.removeGameObject(obj1)) {
            if (obj1.getTag().equals("Brick")){
                bricksCounter.decrement();
            }
            for (GameObject gameObject: gameObjects){
                if (gameObject.getTag().equals("SecondPaddle")){
                    return;
                }
            }
            SecondPaddle paddle2 = new SecondPaddle(paddleDim,peddleImage,inputListener, gameObjects);
            paddle2.setCenter(new Vector2(SCREEN_WIDTH/2, SCREEN_LENGTH - PADDLE_LENGTH *3));
            paddle2.setTag("SecondPaddle");
            gameObjects.addGameObject(paddle2);
        }
    }
}
