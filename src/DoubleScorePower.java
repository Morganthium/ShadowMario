import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public class DoubleScorePower extends Collectable {
    private static final int ACTIVE_FRAMES = 500;
    private int activeFrames = 0;
    private final int COLLISION_SPEED = -10;
    private final int SPEED_X;

    public DoubleScorePower(int x, int y, Properties props) {
        super(x, y, props);
        this.radius = Double.parseDouble(props.getProperty("gameObjects.coin.radius"));
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.coin.speed")); // Move to collectable later
        this.image = new Image(props.getProperty("gameObjects.doubleScore.image"));
    }

    @Override
    public int updateWithTarget(Input input, Player target) {
        if (isCollected) {
            y += COLLISION_SPEED; // Move upwards and disappear
            if (y < 0) isCollected = false; // Or deactivate completely
            return 0;
        }

        move(input);
        draw();

        if (checkCollision(target)) {
            isCollected = true;
            activeFrames = ACTIVE_FRAMES;
            return value; // The value could determine score multiplication factor or similar
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
        super.move(input); // For any common movement logic
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT)){
            this.x += SPEED_X;
        }
    }
}
