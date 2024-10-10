public class RandomMovementComponent implements randomlyMovable {
    private int displacement = 0;
    private int maxDisplacement;
    private int direction = 1;

    public RandomMovementComponent(int maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    @Override
    public void moveRandomly() {
        if (Math.abs(displacement) >= maxDisplacement) {
            direction *= -1;
        }
        displacement += direction;
    }

    public int getDisplacement() {
        return direction;
    }
}
