package bricker.health;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Paddle;

import static main.finals.SCREEN_WIDTH;


/**
 * Class of the health objects that are falling from the bricks, meant to give you HP
 */
public class Health extends GameObject {
    private final GameObjectCollection gameObjects;
    private final HealthManager healthBar;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Health(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  GameObjectCollection gameObjects, Paddle mainPaddle, HealthManager healthBar) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.healthBar = healthBar;
        shouldCollideWith(mainPaddle);
    }

    /**
     * allowing the health object to collide only with main Paddle
     * @param other The other GameObject.
     * @return boolean
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("mainPaddle");
    }

    /**
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        healthBar.addHealth();

    }

    /**
     * Removing the health object when it falls under the paddle
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > SCREEN_WIDTH){
            gameObjects.removeGameObject(this);
        }
    }
}
