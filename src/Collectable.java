import bagel.Image;
import bagel.Input;

import java.util.Properties;

public abstract class Collectable {
    protected int x, y;
    protected Image image;
    protected boolean isCollected = false;
    protected double radius;
    protected int value; // Common property used by all collectibles for scoring purposes.

    public Collectable(int x, int y, Properties props) {
        this.x = x;
        this.y = y;
        this.value = Integer.parseInt(props.getProperty("value", "10")); // Default value
        this.radius = Double.parseDouble(props.getProperty("radius", "0"));
    }

    public abstract int updateWithTarget(Input input, Player target); // Abstract method for updating state with player interaction.

    protected void move(Input input) {
    }

    public void draw() {
        if (!isCollected) {
            image.draw(x, y);
        }
    }

    public boolean checkCollision(Player player) {
        return !isCollected && Math.sqrt(Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2)) <= player.getRADIUS() + this.radius;
    }
}
