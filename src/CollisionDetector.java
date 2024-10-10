/**
 * Class that handles the collision detection.
 */
public class CollisionDetector {

    /**
     * Method that checks for a collision between the player and the given entity's position.
     */
    public static boolean isCollided(Player player, int x, int y, double radius) {
        return Math.sqrt(Math.pow(player.getX() - x, 2) +
                Math.pow(player.getY() - y, 2)) <= player.getRADIUS() + radius;
    }
    /**
     * Checks for a collision between the player and a platform based on specific landing conditions.
     */
    public static boolean canLandOnPlatform(Player player, FlyingPlatform platform, int halfLength, int halfHeight) {
        int playerX = player.getX();
        int platformX = platform.getX();
        int playerY = player.getY();
        int platformY = platform.getY();

        boolean withinHorizontalBounds = Math.abs(playerX - platformX) < halfLength;
        boolean abovePlatform = playerY < platformY && playerY >= platformY - halfHeight;

        return withinHorizontalBounds && abovePlatform;
    }
}