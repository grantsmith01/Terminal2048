/**
 * This file is for testing the GameState program.
 */

/**
 * A manager class that runs the 2048 game. It parses command line
 * arguments, initializes the game accordingly.
 */
public class PlayManager
{
    private static final String USAGE = "`java PlayManager [SAVEFILE]` " +
        "to start a game from SAVEFILE, omit for a default game";
    private static final String START_MSG =
        "Starting a default-sized random game...";
    private static final String LOAD_MSG_FMT =
        "Attempting to load game from %s..";

    /**
     * Main method. Parses command line arg, load games, run games.
     * @param args an array of command line args in Strings
     */
    public static void main (String[] args) {
        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }

        Game2048 game = null;

        if (args.length == 0) {
            System.out.println(START_MSG);
            game = new Game2048();
        } else {
            System.out.println(String.format(LOAD_MSG_FMT, args[0]));
            game = new Game2048(args[0]);
        }
        game.play();
    }
}
