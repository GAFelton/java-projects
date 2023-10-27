import java.util.*;

/*
 * A PuzzleTask extends the class Task. It requires the user to answer a
 * logical or knowledge-specific question.
 * The PuzzleTask has one correct answer.
 */
public class PuzzleTask extends Task {
    public static final List<String> actionOptions = Arrays.asList("hint", "solve <solution>", "solve");
    private String solution;
    private List<String> hints;
    private boolean completed;
    private int hintIndex;

    /*
     * Construct a new PuzzleTask. Each PuzzleTask requires the following inputs:
     * - The correct solution for the PuzzleTask.
     * - A comma-separated list of hints, which the user can ask for one at a time.
     * - A description of the task.
     */
    public PuzzleTask(String solution, List<String> hints, String description) {
        super(description);
        this.solution = solution;
        this.hints = arrangeHints(hints);
        this.completed = false;
    }

    /*
     * Private Helper Method to ensure that the first element of hints list is an
     * empty string, so no hints display when getDescription() is first called.
     * @returns a List of Strings containing "" in addition to all the hints provided.
     */
    private static List<String> arrangeHints(List<String> hints) {
        List<String> hintsList = new ArrayList<String>();
        hintsList.add("");
        hintsList.addAll(hints);
        return hintsList;
    }

    /*
     * Returns a string representation of the task. This description will update if
     * the user requests hints to be revealed.
     * 
     * @return the string representation of the task
     */
    public String getDescription() {
        return super.getDescription() + "\n" + hints.get(hintIndex);
    }

    /*
     * Returns the possible options that a user may take during their attempt to
     * complete this task.
     * 
     * @return the list of valid actions for the task. ("hint", "solve <solution>")
     */
    public List<String> getActionOptions() {
        return actionOptions.subList(0, 2);
    }

    /*
     * Returns whether or not this task has been completed.
     * 
     * @return true if the user entered the correct solution for the PuzzleTask.
     */
    public boolean isComplete() {
        return completed;
    }

    /*
     * Attempt to take an action towards completing the task.
     * The possible actions are asking for a hint or trying a solution.
     * 
     * @param action is the action to be attempted.
     * 
     * @return true if the action is 'hint' as long as there are more hints to
     * return, otherwise return false.
     * 
     * @return true if the action is inputting the correct solution with the format
     * 'solve <solution>', false otherwise.
     * 
     * @throws IllegalArgumentException if the action attempted is not one of the
     * valid actions as specified by getActionOptions().
     */
    public boolean takeAction(String action) {
        String[] actionComponents = action.split(" ");

        if (!actionOptions.contains(actionComponents[0])) {
            throw new IllegalArgumentException("The action " + action
                    + " is not valid! Please enter one of the following actions: " + getActionOptions());
        }
        if (action.equals("hint")) {
            // If there are hints remaining, increment the hintIndex which will cause
            // getDescription() to include the next hint.
            if (hintIndex < hints.size() - 1) {
                hintIndex++;
                return true;
            } else {
                return false;
            }
        } else if (actionComponents[1].equals(solution)) {
            completed = true;
            return true;
        }
        return false;
    }
}
