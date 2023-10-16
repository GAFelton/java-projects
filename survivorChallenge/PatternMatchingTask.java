import java.util.*;

/*
 * A PatternMatchingTask is a type of PrecisionTask that requires the user to assess inputs
 * against a given pattern to determine which ones match.
 * The PatternMatchingTask has one correct answer, but it need not be given in the same order
 * that is described in the solution.
 */
public class PatternMatchingTask extends PrecisionTask {
    private List<String> solution;
    private List<String> options;
    private boolean completed;

    /*
     * Construct a new PatternMatchingTask. Each PatternMatchingTask requires the
     * following inputs:
     * - Solution - A comma-separated list of items that match the pattern. (Ex.
     * "4, 1, 8")
     * - Options - A comma-separated list describing possibilities which user must
     * narrow down from to find the solution. (Ex. "13, 16, 11")
     * - Description - Describe the pattern to the user. (Ex. "Integers < 10")
     */
    public PatternMatchingTask(List<String> solution, List<String> options, String description) {
        super(solution, description);
        this.solution = solution;
        this.options = options;
        this.completed = false;
    }

    /*
     * Returns the possible options that a user may take choose from to match the
     * pattern.
     * 
     * @return the list of potential matches.
     */
    public List<String> getActionOptions() {
        return options;
    }

    /*
     * Returns whether or not this task has been completed.
     * 
     * @return true if the user has correctly identified exactly which items match
     * the pattern.
     */
    public boolean isComplete() {
        return completed;
    }

    /*
     * Attempt to take an action towards completing the task.
     * The action must consist of a comma-separated list of items that match the
     * solution (regardless of order).
     * 
     * @param input is the list of items that match the pattern.
     * 
     * @return true if the action is inputting the correct solution,
     * and false otherwise.
     * 
     * @throws IllegalArgumentException if the input is not in the form of a
     * comma-separated list.
     */
    public boolean takeAction(String input) {
        int correctGuesses = 0;
        List<String> guesses = new ArrayList<String>(Arrays.asList(input.split(", ")));

        // Without using RegEx to match (!input.matches("^[a-zA-Z0-9]*(?:,
        // ?[a-zA-Z0-9]*){0,}$")),
        // we check first that the input string has been split at all, and if not,
        // whether spaces are present which might indicate that a user has
        // entered an improperly formatted input.
        if (guesses.size() == 1) {
            if (guesses.get(0).contains(" ")) {
                throw new IllegalArgumentException("Your input (" + input
                        + ") is not valid! Please enter a comma-separated list (ex. \"a, b, c\")");
            }
        }

        // We are checking for an exact match of correct guesses.
        // If a user has inputted an incorrect answer, decrement correctGuesses.
        // This will cover the case in which all correct answers may be present
        // in addition to one or more incorrect guesses.
        for (String item : guesses) {
            if (solution.contains(item)) {
                correctGuesses++;
            } else {
                correctGuesses--;
            }
        }

        if (correctGuesses == solution.size()) {
            completed = true;
            return true;
        }
        return false;
    }
}
