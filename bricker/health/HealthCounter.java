package bricker.health;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Numeric Counter on the screen
 */
public class HealthCounter extends GameObject {
    private final Counter healthCounter;

    /**
     * Construct a new HealthCounter instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param healthCounter The HealthBar associated with this counter.
     */
    public HealthCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter healthCounter) {
        super(topLeftCorner, dimensions, null); // Start with null renderable
        this.healthCounter = healthCounter;
        updateRenderable(); // Initialize the renderable
    }

    /**
     * Update the health counter display to reflect the current health.
     */
    public void updateRenderable() {
        TextRenderable textRenderable = createTextRenderable();
        this.renderer().setRenderable(textRenderable);
    }

    private TextRenderable createTextRenderable() {
        TextRenderable text = new TextRenderable("HP " + healthCounter.value());
        if (healthCounter.value() <= 1) {
            text.setColor(Color.RED);
        } else if (healthCounter.value() == 2) {
            text.setColor(Color.YELLOW);
        } else {
            text.setColor(Color.GREEN);
        }
        return text;
    }
}