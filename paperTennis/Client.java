// Starter Code

import java.util.*;

public class Client {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        // Play PaperTennis or TicTacToe by changing which game you instantiate below.
        AbstractStrategyGame game = new PaperTennis();
        // AbstractStrategyGame game = new TicTacToe();

        System.out.println(game.instructions());
        System.out.println();

        while (!game.isGameOver()) {
            System.out.println(game);
            System.out.printf("Next Turn:\n");
            try {
                game.makeMove(console);
            } catch (IllegalArgumentException ex) {
                System.out.println("**Illegal move: " + ex.getMessage());
            }
        }
        System.out.println(game);
        int winner = game.getWinner();
        if (winner > 0) {
            System.out.printf("Player %d wins!\n", winner);
        } else {
            System.out.println("It's a tie!");
        }
    }
}
