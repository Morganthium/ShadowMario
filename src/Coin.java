import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the coin entity.
 */
public class Coin extends Collectable {
    private final double RADIUS;
    private final int SPEED_X;
    private final int COLLISION_SPEED = -10;
    private int speedY = 0;

    public Coin(int x, int y, Properties props) {
        super(x, y, props);
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.coin.radius"));
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.coin.speed"));
        this.image = new Image(props.getProperty("gameObjects.coin.image"));
    }

    @Override
    public int updateWithTarget(Input input, Player target) {
        move(input);
        draw();

        if (checkCollision(target)) {
            isCollected = true;
            speedY = COLLISION_SPEED;
            return value; // Assumes value is set appropriately in properties
        }

        return 0;
    }

    @Override
    protected void move(Input input) {
        super.move(input); // For any common movement logic
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT)){
            this.x += SPEED_X;
        }
        this.y += speedY;
    }
}