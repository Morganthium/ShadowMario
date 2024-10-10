import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public class InvinciblePower extends Collectable {
    private static final int ACTIVE_FRAMES = 500;
    private int activeFrames = 0;
    private static final int SPEED_X = 5;
    private static final int COLLISION_SPEED = -10;

    public InvinciblePower(int x, int y, Properties props) {
        super(x, y, props);
        this.image = new Image(props.getProperty("gameObjects.invinciblePower.image"));
        this.radius = Double.parseDouble(props.getProperty("gameObjects.invinciblePower.radius"));
    }

    @Override
    public int updateWithTarget(Input input, Player target) {
        if (isCollected) {
            y += COLLISION_SPEED; // Move upwards and disappear
            if (y < 0) isCollected = false; // Deactivate when off-screen
            return 0;
        }

        move(input);
        draw();

        if (checkCollision(target)) {
            isCollected = true;
            activeFrames = ACTIVE_FRAMES;
            return value; // Return the value to add to score if necessary
        }

        return 0;
    }

    public boolean isActive() {
        if (activeFrames > 0) {
            activeFrames--;
            return true;
        }
        return false;
    }

    @Override
    protected void move(Input input) {
        if (!isCollected) {
            if (input.isDown(Keys.RIGHT)) {
                this.x -= SPEED_X;
            } else if (input.isDown(Keys.LEFT)) {
                this.x += SPEED_X;
            }
        }
    }
}
