
/*
 * Paper Tennis implemented by George Felton
 * 
 * How to Play:
 * Run Client.java to play the game.
 * 
 */
import java.util.*;

public class PaperTennis implements AbstractStrategyGame {
    private static final int STARTING_WAGER_POINTS = 50;
    private static final int[] VALID_BOARD_POSITIONS = new int[] { -3, -2, -1, 1, 2, 3 };
    private static final int NUMBER_OF_ROUNDS = 3;
    private int round;
    private int leftRoundPoints;
    private int rightRoundPoints;
    private int currentPosition;
    private int leftWagerPoints;
    private int rightWagerPoints;

    // Class constructor, creating a new PaperTennis match.
    public PaperTennis() {
        this.round = 1;
        this.leftRoundPoints = 0;
        this.rightRoundPoints = 0;
        this.currentPosition = 0;
        this.leftWagerPoints = STARTING_WAGER_POINTS;
        this.rightWagerPoints = STARTING_WAGER_POINTS;
    }

    /*
     * Return String of instructions on how to play the game.
     */
    public String instructions() {
        String result = "";
        result += "In Paper Tennis, both players start with 50 points each\n";
        result += "and a ball directly between them on a court with four zones.\n\n";
        result += "On each turn, players will wager a number of points, which will be\n";
        result += "subtracted from their 50 point total. The player who places a higher wager\n";
        result += "in a round gets to advance the ball one zone on the court towards\n";
        result += "their opponent. If the ball leaves the court through one side,\n";
        result += "that player loses the round. If both players wager all their points,\n";
        result += "the round ends and the loser is determined by which half of the court\n";
        result += "the ball is in.\n\n";
        result += "Paper Tennis is played over a series of three rounds. The winner of each round\n";
        result += "will score either one point for having the ball in their opponent's half,\n";
        result += "or two points for knocking the ball out of the court.\n";
        result += "The player with the most points after three rounds is the overall winner!\n";
        return result;
    }

    /*
     * Returns a String showing the current board state, including the ball location
     * and players current point values.
     * The representation of the board will look like the image below,
     * with the ball `*` moving back and forth across the center:
     * _________________
     * |   |   I   |   |
     * |   |   I   |   |
     * |   |   I   |   |
     * -----------------
     * If the game has ended, return a String showing final points.
     */
    public String toString() {
        // Initialize Map of possible ball positions on the board.
        HashMap<Integer, String> posMap = new HashMap<Integer, String>();
        posMap.put(-2, " ");
        posMap.put(-1, " ");
        posMap.put(0, "I");
        posMap.put(1, " ");
        posMap.put(2, " ");
        // Replace location with ball "*"
        posMap.put(currentPosition, "*");

        String ballPlacement = String.format("| %s | %s %s %s | %s |\n",
                posMap.get(-2), posMap.get(-1), posMap.get(0), posMap.get(1), posMap.get(2));

        // Now that ball placement has been set up, capture board state
        // to show to player.
        String boardState = "";
        boardState += "_________________\n";
        boardState += "|   |   I   |   |\n";
        boardState += ballPlacement;
        boardState += "|   |   I   |   |\n";
        boardState += "-----------------\n";
        boardState += "\n";
        boardState += String.format("Player 1 (Left) Wager Points Remaining: %d\n", leftWagerPoints);
        boardState += String.format("Player 2 (Right) Wager Points Remaining: %d\n", rightWagerPoints);

        // If the game is over, prepare to print the final score.
        if (round > NUMBER_OF_ROUNDS) {
            String finalDescription = "";
            finalDescription += String.format("The Final Score is %d : %d, meaning...", leftRoundPoints,
                    rightRoundPoints);
            return finalDescription;
        }
        return boardState;
    }

    /*
     * Both players input a score (0 < score < current points of player).
     * Throw an IllegalArgumentException if the wager of either player is outside of
     * that range. If one wager is higher than other, move ball towards a valid
     * space on the lower wager's side. If wagers are equal do not move the ball.
     * Subtract the inputted score from both sides.
     */
    public void makeMove(Scanner input) {
        System.out.print("Left Player wager? ");
        int leftWager = input.nextInt();
        System.out.print("Right Player wager? ");
        int rightWager = input.nextInt();

        // Check that wagers are valid.
        if (leftWager < 0 || leftWager > leftWagerPoints ||
                rightWager < 0 || rightWager > rightWagerPoints) {
            String invalidWagerError = "Invalid wager. ";
            invalidWagerError += "Wager must be between 0 and your current available points.";
            invalidWagerError += "\nPlease try again:";
            throw new IllegalArgumentException(invalidWagerError);
        }
        // Reduce wagered points from both sides
        leftWagerPoints = leftWagerPoints - leftWager;
        rightWagerPoints = rightWagerPoints - rightWager;

        // Determine how the ball should move. If findcurrentValidPositionIndex()
        // returns -1, meaning that the ball is still at the center line,
        // adjust valid position index according to where the ball will move.
        int currentValidPositionIndex = findcurrentValidPositionIndex(currentPosition);
        if (leftWager < rightWager) {
            if (currentValidPositionIndex == -1) {
                currentValidPositionIndex = 3;
            }
            currentPosition = VALID_BOARD_POSITIONS[currentValidPositionIndex - 1];
        }
        if (leftWager > rightWager) {
            if (currentValidPositionIndex == -1) {
                currentValidPositionIndex = 2;
            }
            currentPosition = VALID_BOARD_POSITIONS[currentValidPositionIndex + 1];
        }
    }

    /*
     * Private helper method for makeMove.
     * The ball cannot return to position "0" on the board once the game has begun.
     * Therefore, this function searches for the index of the matching position on a
     * list of valid board positions (-3 through 3). Returns either the index of the
     * current position, or -1 if it's not found (a.k.a. ball is at position "0").
     */
    private int findcurrentValidPositionIndex(int currentPosition) {
        int returnIndex = -1;
        for (int x = 0; x < VALID_BOARD_POSITIONS.length; x++) {
            if (VALID_BOARD_POSITIONS[x] == currentPosition) {
                returnIndex = x;
            }
        }
        return returnIndex;
    }

    /*
     * Determines whether the game is over.
     * If winner is -1, then the game has not reached an end-condition,
     * so returns false. Otherwise, the game has ended and returns true.
     */
    public boolean isGameOver() {
        return getWinner() != -1;
    }

    /*
     * Determine the winner of the overall game. First, checks the round winner &
     * advances the round. If the game has completed (meaning 3 rounds have been
     * played), tally up points and return an overall winner. Returns -2 if no
     * winner, or the player number: 1 or 2.
     * Important: If the game has not concluded, return -1.
     */
    public int getWinner() {
        getRoundWinner();
        int winner = -1;
        if (round > NUMBER_OF_ROUNDS) {
            if (leftRoundPoints > rightRoundPoints) {
                winner = 1;
            } else if (leftRoundPoints < rightRoundPoints) {
                winner = 2;
            } else {
                // A tie can only happen if both players wager 50 points at
                // the beginning of the game. In this case, return -2 to signify a draw.
                winner = -2;
            }
        }
        return winner;
    }

    /*
     * Private helper method for getRoundWinner() to reset the board. Prints the
     * board state for the players, then sets currentPosition and left/right wager
     * points to starting values.
     */
    private void resetBoard() {
        String roundDescription = "";
        roundDescription += String.format("New Round: Round %d\n", round);
        roundDescription += String.format("Player 1 Round Points %d\n", leftRoundPoints);
        roundDescription += String.format("Player 2 Round Points %d\n", rightRoundPoints);
        System.out.println(roundDescription);
        currentPosition = 0;
        leftWagerPoints = STARTING_WAGER_POINTS;
        rightWagerPoints = STARTING_WAGER_POINTS;
    }

    /*
     * Private helper method for getWinner().
     * Round is over and a round winner is declared when both players are at 0
     * points remaining, or when ball goes outside bounds of board. If the ball goes
     * out of bounds, that winner earns two round points. If the ball ends a round
     * inside the court, the winner earns one round point.
     * 
     */
    private void getRoundWinner() {
        if (leftWagerPoints == 0 && rightWagerPoints == 0) {
            calculateRoundWinner(1);
        } else if (currentPosition < -2 || 2 < currentPosition) {
            calculateRoundWinner(2);

        }
    }

    /*
     * Private helper method for getRoundWinner() to determine the round winner
     * based on the side of the board the ball is on, and increment the round
     * number. Prints 'player 1 wins' if ball is left of center, 'Player 2 wins' if
     * ball is right of center, or a 'tie'. The board resets as long as there are
     * rounds remaining to play.
     */
    private void calculateRoundWinner(int roundPoints) {
        if (currentPosition > 0) {
            leftRoundPoints = leftRoundPoints + roundPoints;
            System.out.println("Player 1 wins this round!\n");
        } else if (currentPosition < 0) {
            rightRoundPoints = rightRoundPoints + roundPoints;
            System.out.println("Player 2 wins this round!\n");
        } else {
            System.out.println("This round ends in a tie!\n");
        }
        round++;
        if (round <= NUMBER_OF_ROUNDS) {
            resetBoard();
        }
    }

    // The getNextPlayer() method is not relevant to this implementation
    // of PaperTennis, but is issued by the AbstractStrategyGame super class
    // (and is relevant for other games). I have included it here in case it's
    // lack of presence would impact the running of Client.java.
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        }
        return 1;
    }
}
