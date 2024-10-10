import bagel.*;

import java.util.Properties;

public class Fireball {
    private static final double DAMAGE = 0.5;
    private static final int SPEED = 8;
    private int x, y;
    private boolean direction; // true for right, false for left
    private Image image;
    private boolean isActive = true; // Track if the fireball is still in play
    private Damageable source; // Reference to the firer

    public Fireball(int x, int y, boolean direction, Damageable source, Properties props) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.source = source;
        this.image = new Image(props.getProperty("gameObjects.fireball.image"));
    }

    public void update() {
        if (!isActive) return;

        if (direction) { // Fire left or right
            x += SPEED;
        } else {
            x -= SPEED;
        }

        // Check if the fireball has moved out of the window bounds
        if (x < 0 || x > Window.getWidth()) {
            isActive = false;
        }

        image.draw(x, y);
    }

    public boolean checkCollision(Damageable target) {
        if (!isActive || target == source) return false; // Exclude the source from collision checks

        // Simplified collision detection
        double distance = Math.sqrt(Math.pow(target.getX() - x, 2) + Math.pow(target.getY() - y, 2));
        double combinedRadii = target.getRADIUS() + image.getWidth() / 2.0;

        if (distance <= combinedRadii) {
            isActive = false;
            target.applyDamage(DAMAGE);
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }
}
