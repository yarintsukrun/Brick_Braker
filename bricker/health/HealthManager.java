package bricker.health;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import static main.finals.*;
import static main.finals.HEART_SIZE;


/**
 * An object who Manage the health objects and counter
 */
public class HealthManager {
    private final Counter healthNum;
    private final GameObjectCollection childObjects;
    private final HealthCounter healthCounter;
    GameObject[] heartList;
    Renderable heartImage;

    /**
     * Construct a composite GameObject that contains other GameObjects.
     * @param gameObjects   The collection to manage child objects.
     */
    public HealthManager(ImageReader imageReader,
                         GameObjectCollection gameObjects) {
        this.childObjects = gameObjects;
        healthNum = new Counter(START_HEARTH);
        healthCounter = new HealthCounter(new Vector2(BORDER_WIDTH,
                SCREEN_LENGTH - HEART_SIZE), new Vector2(HEART_SIZE, HEART_SIZE), healthNum);
        gameObjects.addGameObject(healthCounter, Layer.UI);
        heartList = new GameObject[MAX_HEALTH];
        this.heartImage = imageReader.readImage("assets/heart.png", true);
        resetHearts();

    }

    /**
     * adding health to the player
     */
    public void addHealth() {
        if (healthNum.value() >= MAX_HEALTH){
            return;
        }
        heartList[healthNum.value()] = new GameObject(
                new Vector2(5 * HEART_SIZE + (healthNum.value()-1) * (HEART_SIZE + BORDER_WIDTH),
                        SCREEN_LENGTH - HEART_SIZE),
                new Vector2(HEART_SIZE, HEART_SIZE), heartImage);
        childObjects.addGameObject(heartList[healthNum.value()], Layer.UI);
        healthNum.increment();
        healthCounter.updateRenderable();
    }

    /**
     * Resetting the health for the user to the default settings
     */
    public void resetHearts() {
        healthNum.reset();
        for (int i = 0; i < START_HEARTH; i++) {
            addHealth();
        }
        healthCounter.updateRenderable();
    }


    /**
     * Remove a child GameObject from the composite object.
     */
    public void removeHealth() {
        childObjects.removeGameObject(heartList[healthNum.value()-1], Layer.UI);
        healthNum.decrement();
        healthCounter.updateRenderable();
    }


    /**
     * Gets the amount of user's HP
     * @return amount of user's HP
     */
    public int getHearts(){
        return healthNum.value();
    }
}
