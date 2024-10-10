import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Player implements Damageable{
    private final int X;
    private final int INITIAL_Y;
    private final double RADIUS;
    private final Properties PROPS;
    private final int INITIAL_JUMP_SPEED = -20;
    private final int FALL_SPEED = 2;
    private int y;
    private int verticalSpeed = 0;
    private double health;
    private Image image;
    private boolean onPlatform = false;
    private List<Fireball> fireballs = new ArrayList<>();
    private boolean facingRight = true;

    public Player(int x, int y, Properties props) {
        this.X = x;
        this.y = y;
        this.INITIAL_Y = y;
        this.PROPS = props;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.player.radius"));
        this.image = new Image(props.getProperty("gameObjects.player.imageRight"));
        this.health = Double.parseDouble(props.getProperty("gameObjects.player.health"));
    }

    public void update(Input input) {
        if (input.wasPressed(Keys.LEFT)) {
            image = new Image(this.PROPS.getProperty("gameObjects.player.imageLeft"));
            facingRight = false;
        }
        if (input.wasPressed(Keys.RIGHT)) {
            image = new Image(this.PROPS.getProperty("gameObjects.player.imageRight"));
            facingRight = true;
        }
        image.draw(X, y);
        jump(input);

        if (input.wasPressed(Keys.SPACE)) {
            fire(PROPS);
        }

        for (Fireball fireball : fireballs) {
            fireball.update();
        }

        // Remove inactive fireballs
        fireballs.removeIf(fireball -> !fireball.isActive());
    }

    public void jump(Input input) {
        if (input.wasPressed(Keys.UP) && y == INITIAL_Y) {
            verticalSpeed = INITIAL_JUMP_SPEED;
        }

        if (y < INITIAL_Y) {
            verticalSpeed += 1; // Adjusted for gravity effect
        }

        if (verticalSpeed > 0 && y >= INITIAL_Y && !isDead()) {
            verticalSpeed = 0;  // Resets verticalSpeed when landing
            y = INITIAL_Y;  // Ensures initial level on ground platform
        }

        this.y += verticalSpeed;
    }

    public void dead() {
        verticalSpeed = FALL_SPEED;  // When dead, begin falling with a fixed speed
    }

    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setVerticalSpeed(int speed) {
        this.verticalSpeed = speed;
    }

    @Override
    public double getRADIUS() {
        return RADIUS;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double newHealth) {
        this.health = newHealth;
    }

    public boolean isDead() {
        return health <= 0;
    }
    public void fire(Properties props) {
        Fireball fireball = new Fireball(this.X, this.y, facingRight, this, props);
        fireballs.add(fireball);
    }

    public List<Fireball> getFireballs() {
        return fireballs;
    }

    @Override
    public void applyDamage(double damage) {
        double newHealth = getHealth() - damage;
        setHealth(newHealth);
        if (newHealth <= 0) {
            dead();
        }
    }
}