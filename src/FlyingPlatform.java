import bagel.Image;
import bagel.Input;
import java.util.Properties;

public class FlyingPlatform extends Platform {
    private RandomMovementComponent randomMovement;
    private int maxDisplacement;

    public FlyingPlatform(int x, int y, Properties props) {
        super(x, y, props); // Calls the Platform's constructor
        // Initialize RandomMovementComponent with maximum displacement for flying platform
        this.maxDisplacement = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));
        this.randomMovement = new RandomMovementComponent(maxDisplacement);
        this.image = new Image(props.getProperty("gameObjects.flyingPlatform.image")); // Override the image from Platform
    }

    @Override
    public void update(Input input) {
        super.move(input); // Utilizes the movement logic from Platform
        randomMovement.moveRandomly();
        this.x += randomMovement.getDisplacement(); // Apply the random displacement
        super.image.draw(super.x, super.y); // Draw the image at the updated position
    }

    @Override
    protected void move(Input input) {
        super.move(input); // First, execute the standard platform movement
        // Additional movements or modifications can be added here if needed
    }
}