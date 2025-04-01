package bricker.gameobjects;
import static main.finals.PADDLE_SPEED;
import static main.finals.SCREEN_WIDTH;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    UserInputListener inputListener;
    public Paddle(Vector2 vector2, Renderable peddleImage,
                  UserInputListener inputListener){
        super(Vector2.ZERO,vector2,peddleImage );
        this.inputListener = inputListener;}

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&  inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            return;
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                this.getTopLeftCorner().x() > 0){
            setVelocity(new Vector2(-PADDLE_SPEED,0));
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                this.getTopLeftCorner().x() < SCREEN_WIDTH - this.getDimensions().x()){
            setVelocity(new Vector2(PADDLE_SPEED,0));
        }
        else{setVelocity(new Vector2(0,0));}
    }
}
