package bricker.gameobjects;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * the Class of the main ball
 */
public class Ball extends GameObject {
    private final Counter collisionCounter;
    private final Sound collisionSound;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, Counter collisionCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = collisionCounter;
        this.setTag("Ball");
    }

    /**
     * Determent the ball behavior after collision
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (collisionCounter != null){
            collisionCounter.increment();}
        collision.getNormal();
        setVelocity(getVelocity().flipped(collision.getNormal()));
        collisionSound.play();
    }

    /**
     * get the collision counter
     * @return amount of time that the ball collided with other objects
     */
    public int getCollisionCounter(){
        return collisionCounter.value();
    }

}
