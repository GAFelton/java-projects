import java.util.*;

/*
 * A PuzzleTask is a Task that requires the user to answer a logical or knowledge-specific question.
 * The PuzzleTask has one correct answer.
 */
public class PuzzleTask extends Task {
    public static final List<String> actionOptions = Arrays.asList("hint", "solve <solution>", "solve");
    private String solution;
    private List<String> hints;
    private String showHint;
    private boolean completed;
    private int hintIndex;

    /*
     * Construct a new PuzzleTask. Each PuzzleTask requires the following inputs:
     * - The correct solution for the PuzzleTask.
     * - A Comma-separated list of hints, which the user can ask for one at a time.
     * - A description of the task.
     */
    public PuzzleTask(String solution, List<String> hints, String description) {
        super(description);
        this.solution = solution;
        this.hints = hints;
        this.showHint = "";
        this.completed = false;
    }

    /*
     * Returns a string representation of the task. This description will update if
     * the user requests hints to be revealed.
     * 
     * @return the string representation of the task
     */
    public String getDescription() {
        return super.getDescription() + showHint;
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
     * @return true if the action is inputting the correct solution, false otherwise.
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
            return addHint();
        } else if (actionComponents[1].equals(solution)) {
            completed = true;
            return true;
        }
        return false;
    }

    /*
     * Helper method to display a hint at the bottom of the Task description,
     * and mark the action as successful if it gets a hint, or unsuccessful
     * if the user is out of hints.
     * 
     * @return true if a hint has been added to description, or false if the user is
     * out of hints.
     */
    private boolean addHint() {
        if (hintIndex < hints.size()) {
            showHint = "\n" + hints.get(hintIndex);
            hintIndex++;
            return true;
        } else {
            return false;
        }
    }
}
