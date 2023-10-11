
/*
 * Paper Tennis implemented by George Felton
 * 
 * How to Play:
 * Run Client.java to play the game.
 * 
 * Possible addition to the game:
 * The game consists of three rounds played as below, with the winner of each round earning 1 point for winning, 
 * and an additional point for winning via moving the game piece beyond -2 or +2.
 * The player with more points after all three rounds is the overall winner.
 */
import java.util.*;

public class PaperTennis implements AbstractStrategyGame {
    static final int STARTING_POINTS = 50;
    private int[] board;
    private int currentPosition;
    private int[] validBoardPositions = new int[] { -3, -2, -1, 1, 2, 3 };
    private int leftPoints;
    private int rightPoints;

    // Class constructor, creating a new PaperTennis match.
    public PaperTennis() {
        board = new int[] { -2, -1, 0, 1, 2 };
        currentPosition = 0;
        leftPoints = STARTING_POINTS;
        rightPoints = STARTING_POINTS;
    }

    /*
     * Return String of instructions on how to play the game.
     */
    public String instructions() {
        String result = "";
        result += "In Paper Tennis, both players start with 50 points each \n";
        result += "and a ball directly between them on a court with four zones.\n\n";
        result += "On each turn, players will wager a number of points, which will be\n";
        result += "subtracted from their 50 point total. The player who places a higher wager\n";
        result += "in a round gets to advance the ball one zone on the court towards\n";
        result += "their opponent. If the ball leaves the court through one side,\n";
        result += "that player loses the game. If both players wager all their points,\n";
        result += "the game ends and the loser is determined by which half of the court\n";
        result += "the ball is in.";
        return result;
    }

    /*
     * Returns a String showing the current board state, including the ball location
     * and players current point values.
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

        String boardState = "";
        boardState += "_________________\n";
        boardState += "|   |   I   |   |\n";
        boardState += ballPlacement;
        boardState += "|   |   I   |   |\n";
        boardState += "-----------------\n";
        boardState += "\n";
        boardState += String.format("Player 1 (Left) Points: %d\n", leftPoints);
        boardState += String.format("Player 2 (Right) Points: %d\n", rightPoints);

        return boardState;
    }

    /*
     * Both players input a score (0 < score < current points of player).
     * Throw an IllegalArgumentException if the wager of either player is outside of
     * that range. If one wager is higher than other, move ball towards a valid
     * space
     * on the lower wager's side. If wagers are equal do not move the ball.
     * Subtract the inputted score from both sides.
     */
    public void makeMove(Scanner input) {
        System.out.print("Left Player wager? ");
        int leftWager = input.nextInt();
        System.out.print("Right Player wager? ");
        int rightWager = input.nextInt();

        // Check that wagers are valid.
        if (leftWager < 0 || leftWager > leftPoints ||
            rightWager < 0 || rightWager > rightPoints) {
            String invalidWagerError = "Invalid wager. ";
            invalidWagerError += "Wager must be between 0 and your current available points.";
            throw new IllegalArgumentException(invalidWagerError);
        }
        // Reduce wagered points from both sides
        leftPoints = leftPoints - leftWager;
        rightPoints = rightPoints - rightWager;

        // Determine how the ball should move. If findcurrentValidPositionIndex()
        // returns -1, meaning that the ball is still at the center line,
        // adjust valid position index according to where the ball will move.
        int currentValidPositionIndex = findcurrentValidPositionIndex(currentPosition);
        if (leftWager < rightWager) {
            if (currentValidPositionIndex == -1) {
                currentValidPositionIndex = 3;
            }
            currentPosition = validBoardPositions[currentValidPositionIndex - 1];
        }
        if (leftWager > rightWager) {
            if (currentValidPositionIndex == -1) {
                currentValidPositionIndex = 2;
            }
            currentPosition = validBoardPositions[currentValidPositionIndex + 1];
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
        for (int x = 0; x < validBoardPositions.length; x++) {
            if (validBoardPositions[x] == currentPosition) {
                returnIndex = x;
            }
        }
        return returnIndex;
    }

    public boolean isGameOver() {
        return getWinner() > 0;
    }

    /*
     * Winner is declared when both players are at 0 points remaining, or when
     * ball goes outside bounds of board. Returns 0 if no winner, or the player
     * number: 1 or 2.
     * 
     */
    public int getWinner() {
        int winner = 0;
        if (leftPoints == 0 && rightPoints == 0) {
            winner = calculateWinner();
            if (winner == 0) {
                // This can only happen if both players wager 50 points at
                // the beginning of the game. In this case, return 3 to signify a draw.
                winner = 3;
            }
        }
        if (currentPosition < -2 || 2 < currentPosition) {
            winner = calculateWinner();
        }
        return winner;
    }

    // private helper method to determine the winner based on the side of the board
    // the ball is on. Returns 1 if ball is left of center, 2 if ball is right of
    // center, or 0 if a tie.
    private int calculateWinner() {
        if (currentPosition > 0) {
            return 1;
        }
        if (currentPosition < 0) {
            return 2;
        }
        return 0;
    }

    // This method is not relevant to this implementation of PaperTennis, but is
    // required by the AbstractStrategyGame super class
    // (and is relevant for other games).
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        }
        return 1;
    }
}
