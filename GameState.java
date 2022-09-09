/*
 *  This file is used to make the game 2048 run. It implements all of the
 *  methods needed to make the game run smoothly. These methods are the
 *  functions of the game, like sliding and combining tiles.
 *
 */

/*
 * Name:    GameState
 * Purpose: This class is used to make the game 2048 run how it should, and
 * implement the methods required to make it happen. The game runs as the
 * code states.
 */
import java.util.Random;
public class GameState {
  private Random rng;
  private int[][] board;
  private int score;
  private static final int ROTATED_ONCE = 1;
  private static final int ROTATED_TWICE = 2;
  private static final int ROTATED_THRICE = 3;
  private static final int ROTATED_FOUR_TIMES = 4;

  /**
  * toString() for printing out the 2048 board with all its tiles.
  *
  * @return String that is the rows and columns of the current board.
  */
  public String toString() {
    StringBuilder outputString = new StringBuilder();
    outputString.append(String.format("Score: %d\n", getScore()));
    for ( int row = 0; row < getBoard().length; row++ ) {
      for ( int column = 0; column < getBoard()[0].length; column++ ) {
        outputString.append( getBoard()[row][column] == 0 ? "   -" :
          String.format( "%5d", getBoard()[row][column] ) );
        }
      outputString.append( "\n" );
    }
    return outputString.toString();
  }

  /**
  * Parameterized constructor to create a GameState object with "numRow"
  * rows and "numCols" columns.
  *
  * @param numRows the amount of rows in the 2D array
  * @param numCols the amount of columns in the 2D array
  */
  public GameState ( int numRows, int numCols ) {
    this.board = new int[numRows][numCols];
    this.score = 0;
    this.rng = new Random(Config.RANDOM_SEED);
  }

  /**
  * Getter method for making a deep copy of the board and returning it.
  *
  * @return deep copy of the board the method is called on.
  */
  public int[][] getBoard() {
    int[][] copyBoard = new int[this.board.length][this.board[0].length];
    for( int i = 0; i < copyBoard.length; i++ ) {
      for( int j = 0; j < copyBoard[0].length; j++ ) {
        copyBoard[i][j] = this.board[i][j];
      }
    }
    return copyBoard;
  }

  /**
  * Setter method for creating replacing the current board with the values
  * of this board by making a deep copy.
  *
  * @param newBoard the board to replace the current board.
  */
  public void setBoard( int[][] newBoard ) {
    if ( newBoard == null ) {
      return;
    }
    this.board = new int[newBoard.length][newBoard[0].length];
    for( int i = 0; i < newBoard.length; i++ ) {
      for( int j = 0; j < newBoard[0].length; j++ ) {
        this.board[i][j] = newBoard[i][j];
      }
    }
  }

  /**
  * Getter method for the current score of the game.
  *
  * @return the value of the score.
  */
  public int getScore() {
    return this.score;
  }

  /**
  * Setter method for replacing the current score with a new score inputted.
  *
  * @param newScore the score to replace the current score.
  */
  public void setScore( int newScore ) {
    this.score = newScore;
  }

  /**
  * A method to return a random numbre from 0 inclusive to bound exclusive.
  *
  * @param bound the number that is the limit of random number to
  * return, exclusive.
  * @return a random number from 0 to bound.
  */
  protected int rollRNG( int bound ) {
    return rng.nextInt( bound );
  }

  /**
  * Find a random tile, 70% chance to be 2 and 30% chance to be 4.
  *
  * @return a random tile, either 2 or 4
  */
  protected int randomTile() {
    if( rollRNG( 100 ) < Config.TWO_PROB ) {
      return Config.TWO_TILE;
    } else {
      return Config.FOUR_TILE;
    }
  }

  /**
  * Find the amount of 0's in the board.
  *
  * @return the number of empty tiles in the board
  */
  protected int countEmptyTiles() {
    int emptyTiles = 0;
    // Iterate through baord and increment emptyTiles if a tile is 0
    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[0].length; j++ ) {
        if ( this.board[i][j] == 0 ) {
          emptyTiles++;
        }
      }
    }
    return emptyTiles;
  }

  /**
  * If there are any empty tiles, return either a 2 or 4 using randomTile()
  * in a random spot out of these empty tiles. If no empty tiles, do nothing.
  *
  * @return 0 if there are no empty tiles, 2 or 4 if there are.
  */
  protected int addTile() {
    if ( this.countEmptyTiles() == 0 ) {
      return 0;
    }
    // Store a random number between 0 and amnt of empty tiles
    int placeZero = rollRNG( this.countEmptyTiles() );
    int zeroCounter = 0;
    int tile = randomTile();
    // Iterate through board and if an empty tile is found, increment
    // zeroCounter. If zeroCounter-1 reaches placeZero, set that board index
    // equal to the random tile.
    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[0].length; j++ ) {
        if( this.board[i][j] == 0 ) {
          zeroCounter++;
          if( zeroCounter-1 == placeZero ) {
            this.board[i][j] = tile;
            return tile;
          }
        }
      }
    }
    return tile;
  }

  /**
  * Rotate the board it is called on counter clockwise, different loops for
  * whether the 2d array is square or rectangle.
  *
  */
  protected void rotateCounterClockwise() {
    // If the board is square
    if( this.board.length == this.board[0].length ) {
      int[][] rotated = new int[this.board.length][this.board[0].length];
      for ( int i = 0; i < this.board[0].length; i++ ) {
        for ( int j = 0; j < this.board.length; j++ ) {
          rotated[i][j] = this.board[j][this.board[0].length-1-i];
        }
      }
      for ( int i = 0; i < this.board.length; i++ ) {
        for ( int j = 0; j < this.board[0].length; j++ ) {
          this.board[i][j] = rotated[i][j];
        }
      }
      //If the board is not square
    } else {
      int[][] rotated = new int[this.board[0].length][this.board.length];
      for( int i = 0; i < this.board.length; i++ ) {
        for( int j = 0; j < this.board[0].length; j++ ) {
          rotated[j][i] = this.board[i][this.board[0].length-j-1];
        }
      }
      this.board = new int[rotated.length][rotated[0].length];
      for ( int i = 0; i < this.board.length; i++ ) {
        for ( int j = 0; j < this.board[0].length; j++ ) {
          this.board[i][j] = rotated[i][j];
        }
      }
    }
  }

  /**
  * Loop through the board and if there are 0's below non-0 tiles or tiles
  * equal to each other that are on top of each other, return true.
  *
  * @return true if the tiles in the board can slide down, false if not
  */
  protected boolean canSlideDown() {
    // Iterate through board, check all rows but first to see if number above
    // is either zero or equal to current number.
    for( int i = this.board.length-1; i >= 0; i-- ) {
      for( int j = this.board[0].length-1; j >= 0; j-- ) {
        if( i != 0 ) {
          if( this.board[i][j] != 0
          && this.board[i][j] == this.board[i-1][j] ) {
            return true;
          } else if( this.board[i][j] == 0 && this.board[i-1][j] != 0 ) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
  * Check if the current board can slide down; if not, rotate and check again.
  * If it can't slide down in any rotation, return false. If it can, rotate
  * back to original state and return true.
  *
  * @return true if the game is over, false if it is not.
  */
  public boolean isGameOver() {
    int rotationCount = 0;
    if( this.canSlideDown() == false ) {
      rotationCount++;
      this.rotateCounterClockwise();
      if( this.canSlideDown() == false ) {
        rotationCount++;
        this.rotateCounterClockwise();
        if( this.canSlideDown() == false ) {
          rotationCount++;
          this.rotateCounterClockwise();
          if( this.canSlideDown() == false ) {
            rotationCount++;
            this.rotateCounterClockwise();
          }
        }
      }
    }
    // If game is not over, rotate board back to original position
    if( rotationCount == ROTATED_FOUR_TIMES ) {
      return true;
    } else {
      if( rotationCount == ROTATED_ONCE ) {
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
      }
      if( rotationCount == ROTATED_TWICE ) {
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
      }
      if( rotationCount == ROTATED_THRICE ) {
        this.rotateCounterClockwise();
      }
      return false;
    }
  }

  /**
  * If the tiles in the board can slide down, slide down an amount of times
  * equal to the length of the 2d array. Then, combine tiles if possible.
  * Finally, slide down one final time.
  *
  * @return true if the slide down was successful, false if not.
  */
  protected boolean slideDown() {
    boolean originalSlide = false;
    if ( this.canSlideDown() ) {
      originalSlide = true;
    }
    for( int k = 0; k < this.board.length; k++ ) {
      for( int i = this.board.length-1; i >= 0; i-- ) {
        for( int j = this.board[0].length-1; j >= 0; j-- ) {
          if( i != 0 ) {
            if( this.board[i][j] == 0 && this.board[i-1][j] != 0 ) {
              this.board[i][j] = this.board[i-1][j];
              this.board[i-1][j] = 0;
            }
          }
        }
      }
    }
      for( int i = this.board.length-1; i >= 0; i-- ) {
        for( int j = this.board[0].length-1; j >= 0; j-- ) {
          if( i != 0 ) {
            if( this.board[i][j] == this.board[i-1][j] ) {
              this.board[i][j] = this.board[i][j] + this.board[i-1][j];
              this.board[i-1][j] = 0;
              this.score += this.board[i][j];
            }
          }
        }
      }
      for( int i = this.board.length-1; i >= 0; i-- ) {
        for( int j = this.board[0].length-1; j >= 0; j-- ) {
          if( i != 0 ) {
            if( this.board[i][j] == 0 && this.board[i-1][j] != 0 ) {
              this.board[i][j] = this.board[i-1][j];
              this.board[i-1][j] = 0;
            }
          }
        }
      }
    if ( originalSlide ) {
      return true;
    } else {
    return false;
    }
  }

  /**
  * Take input, w, a, s, or d. Check if the input is equal to an enum in the
  * Direction.java enumerator. If it is, rotate the appropriate amount of
  * times, slide in that direction, add a tile, and rotate back to original
  * state.
  *
  * @param dir the direction the user wants the tiles in the board to slide.
  * @return true if movement was successful, false if not.
  */
  public boolean move( Direction dir ){
    if( dir == null ) {
      return false;
    }
    if( dir == Direction.DOWN && this.canSlideDown() ) {
      this.slideDown();
      this.addTile();
      return true;
    } else if( dir == Direction.LEFT ) {
      this.rotateCounterClockwise();
      if( this.canSlideDown() ) {
        this.slideDown();
        this.addTile();
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        return true;
      } else {
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
      }
    } else if ( dir == Direction.UP ) {
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        if( this.canSlideDown() ) {
          this.slideDown();
          this.addTile();
          this.rotateCounterClockwise();
          this.rotateCounterClockwise();
          return true;
        } else {
          this.rotateCounterClockwise();
          this.rotateCounterClockwise();
        }
      } else if ( dir == Direction.RIGHT ) {
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        this.rotateCounterClockwise();
        if( this.canSlideDown() ) {
          this.slideDown();
          this.addTile();
          this.rotateCounterClockwise();
          return true;
        } else {
          this.rotateCounterClockwise();
        }
      }
    return false;
  }
}
