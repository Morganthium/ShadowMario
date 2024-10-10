import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

public class Platform {
    protected int y;
    protected int SPEED_X;
    protected final int MAX_COORDINATE = 3000;
    protected int x;
    protected Image image;

    public Platform(int x, int y, Properties props) {
        this.x = x;
        this.y = y;
        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.platform.speed"));
        this.image = new Image(props.getProperty("gameObjects.platform.image"));
    }

    protected void update(Input input) {
        move(input);
        image.draw(x, y);
    }

    protected void move(Input input){
        if (input.isDown(Keys.RIGHT)){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT) && this.x < MAX_COORDINATE){
            this.x += SPEED_X;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
