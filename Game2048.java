/**
 * This file is for testing the GameState program.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A class that creates the 2048 game. It can create default games,
 * or load saved games.
 */
public class Game2048
{
    GameState currentState;
    String filename;

    private static final String GAME_OVER_MSG = "Game Over!";
    private static final String PROMPT_STR = "> ";
    private static final String CMD_LENGTH_ERR_MSG =
        "Command must be one char long.";
    private static final String POSSIBLE_CMDS_STR =
        "Possible commands:\n w - up\n a - left\n s - down\n " +
        "d - right\n o - save to file\n q - quit game";
    private static final String VALUE_SEP = " ";
    private static final String SAVED_MSG = "Saved current state to: ";

    /**
     * Default constructor that create the game based on settings in
     * `Config.java`.
     */
    public Game2048 () {
        currentState = new GameState(Config.DEFAULT_SIZE, Config.DEFAULT_SIZE);
        filename = Config.DEFAULT_OUTFILE;
        for (int i=0; i<Config.INITIAL_TILE_COUNT; i++) {
            currentState.addTile();
        }
    }

    /**
     * Initialize the game based on input file.
     *
     * @param filename the file that contains the game state of the game to
     *                 load
     */
    public Game2048 (String filename) {
        this();
        this.filename = filename;
        loadFromFile(filename);
    }

    /**
     * Loads a GameState based on the contents of the input file.
     *
     * @param inputfilename the file to initialize GameState from
     */
    void loadFromFile(String inputfilename) {
        try {
            File file = new File(inputfilename);
            if (file.exists()) {
                Scanner sc = new Scanner(new File(inputfilename));
                currentState = new GameState(sc.nextInt(), sc.nextInt());
                currentState.setScore(sc.nextInt());
                int[][] board = currentState.getBoard();
                for (int r = 0; r<board.length; r++) {
                    for (int c=0; c<board[r].length; c++) {
                        board[r][c] = sc.nextInt();
                    }
                }
                currentState.setBoard(board);
                sc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the interactive part of the game. wasd to move,
     * o to save, q to quit.
     */
    void play () {
        Scanner sc = new Scanner(System.in);
        while (!currentState.isGameOver()) {
            System.out.print(currentState + PROMPT_STR);
            String line = sc.nextLine();
            if (line.length() != 1) {
                System.out.println(CMD_LENGTH_ERR_MSG);
                continue;
            }
            char command = line.charAt(0);
            switch (command) {
                case Config.UP_KEY:
                    currentState.move(Direction.UP);
                    break;
                case Config.LEFT_KEY:
                    currentState.move(Direction.LEFT);
                    break;
                case Config.DOWN_KEY:
                    currentState.move(Direction.DOWN);
                    break;
                case Config.RIGHT_KEY:
                    currentState.move(Direction.RIGHT);
                    break;
                case Config.SAVE_KEY:
                    saveToFile();
                    break;
                case Config.QUIT_KEY:
                    sc.close();
                    return;
                default:
                    System.out.println(POSSIBLE_CMDS_STR);
            }
        }
        System.out.print(currentState);
        System.out.println(GAME_OVER_MSG);
        sc.close();
    }

    /**
     * Save the game to file.
     */
    void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(filename);
            int[][] board = currentState.getBoard();
            writer.println(board.length + VALUE_SEP + board[0].length);
            writer.println(currentState.getScore());
            for (int r = 0; r < board.length; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < board[r].length-1; c++) {
                    sb.append(board[r][c]);
                    sb.append(VALUE_SEP);
                }
                sb.append(board[r][board[r].length-1]);
                writer.println(sb.toString());
            }
            writer.close();
            System.out.println(SAVED_MSG + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
