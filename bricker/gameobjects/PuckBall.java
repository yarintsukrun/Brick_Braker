package bricker.gameobjects;
import static main.finals.*;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class PuckBall extends Ball{
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound collisionSound
     */
    public PuckBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    Sound collisionSound, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, collisionSound, null);
        this.gameObjects = gameObjects;
        this.setTag("PuckBall");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > SCREEN_WIDTH){
            gameObjects.removeGameObject(this);
        }
    }
}