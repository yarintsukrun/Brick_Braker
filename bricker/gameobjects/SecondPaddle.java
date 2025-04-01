package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class SecondPaddle extends Paddle{
    private final GameObjectCollection gameObjects;
    int hits;
    public SecondPaddle(Vector2 vector2, Renderable peddleImage, UserInputListener inputListener,
                        GameObjectCollection gameObjects) {
        super(vector2, peddleImage, inputListener);
        this.gameObjects = gameObjects;
        hits = 0;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        String objType = other.getTag();
        if (objType.equals("Ball") || objType.equals("PuckBall")) {
            hits++;
            if (hits >= 4) {
                gameObjects.removeGameObject(this);
            }
        }
    }
}
