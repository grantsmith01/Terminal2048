/**
 * An enumerator defining the 4 possible movement directions in a
 * 2048 game.
 */
public enum Direction
{
    DOWN    (0),
    LEFT    (1),
    UP      (2),
    RIGHT   (3);

    private int rotationCount;

    /**
     * Constructor for the enum
     *
     * @param rotationCount see getRotationCount()
     */
    Direction (int rotationCount) {
        this.rotationCount = rotationCount;
    }

    /**
     * Returns rotationCount, the number of counterclockwise rotations
     * needed to rotate the game state before moving down (e.g. to
     * move left, rotate once then move down).
     *
     * @return counterclockwise rotation count
     */
    public int getRotationCount () {
        return this.rotationCount;
    }
}
