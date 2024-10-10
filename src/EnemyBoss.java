import bagel.*;
import bagel.util.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class EnemyBoss implements Damageable {
    private final int SPEED = 5;
    private final int FALL_SPEED = 2;
    private final int FIREBALL_SPEED = 8;
    private final int SHOOTING_INTERVAL = 100; // frames
    private final double INITIAL_HEALTH = 1.0;
    private final int healthX;
    private final int healthY;
    private final double RADIUS;

    private int x, y;
    private double health;
    private boolean isAlive = true;
    private Image image;
    private Font healthFont;
    private List<Fireball> fireballs = new ArrayList<>();
    private int frameCounter = 0;
    private Random random = new Random();
    private Properties props;
    private DrawOptions redColor;

    public EnemyBoss(int x, int y, Properties props, Font healthFont, int healthX, int healthY) {
        this.x = x;
        this.y = y;
        this.health = INITIAL_HEALTH;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.enemyBoss.radius"));
        this.image = new Image(props.getProperty("gameObjects.enemyBoss.image"));
        this.healthFont = healthFont; // Use the common font
        this.props = props;
        this.healthX = healthX;
        this.healthY = healthY;
        this.redColor = new DrawOptions().setBlendColour(Colour.RED);
    }

        public void update(Input input, Player player) {
        if (isAlive) {
            move(input);
            frameCounter++;

            if (frameCounter % SHOOTING_INTERVAL == 0 && random.nextBoolean() && Math.abs(player.getX() - x) >= 500) {
                fire(props);
            }

            for (Fireball fireball : fireballs) {
                fireball.update();
                fireball.checkCollision(player);
            }

            // Remove inactive fireballs
            fireballs.removeIf(fireball -> !fireball.isActive());

            drawHealth();
        } else {
            y += FALL_SPEED; // Move down off the screen when dead
        }

        image.draw(x, y);
    }

    private void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            x -= SPEED; // Move left when player moves right
        } else if (input.isDown(Keys.LEFT)) {
            x += SPEED; // Move right when player moves left
        }
    }

    private void drawHealth() {
        String healthText = "HEALTH " + (int) (health * 100);
        healthFont.drawString(healthText, healthX, healthY, redColor);
    }

    public void fire(Properties props) {
        Fireball fireball = new Fireball(this.x, this.y, false, this, props); // Enemy fires to the left
        fireballs.add(fireball);
    }

    @Override
    public void applyDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            isAlive = false;
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public double getRADIUS() {
        return RADIUS;
    }

    public boolean isDead() {
        return !isAlive;
    }
}
