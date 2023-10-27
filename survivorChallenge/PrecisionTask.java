import java.util.*;

/*
 * A PrecisionTask extends the class Task. It requires the user to complete a
 * series of actions in the correct order.
 */
public class PrecisionTask extends Task {
    public static final List<String> actionOptions = Arrays.asList("jump", "swim", "run", "climb", "crawl");
    private List<String> requiredActions;
    private int currentActionIndex;

    /*
     * Construct a new PrecisionTask. Each PrecisionTask requires the following inputs:
     * - Which actions must be done, in what order Use any combination of the
     * following: "jump", "swim", "run", "climb", "crawl"
     * - Describe the task to the user.
     */
    public PrecisionTask(List<String> requiredActions, String description) {
        super(description);
        this.requiredActions = requiredActions;
    }

    /*
     * Returns the possible options that a user may take to attempt to complete this task.
     * 
     * @return the list of valid actions for the task.
     */
    public List<String> getActionOptions() {
        return actionOptions;
    }

    /*
     * Returns whether or not this task has been completed.
     * 
     * @return true if the user has done all required actions in the specified order.
     */
    public boolean isComplete() {
        if (currentActionIndex == (requiredActions.size())) {
            return true;
        }
        return false;
    }

    /*
     * Attempt to take an action towards completing the task.
     * Depending on how many items have been specified in the requiredActions sequence,
     * the PrecisionTask may take multiple iterations to complete.
     * 
     * @param action is the action to be attempted.
     * 
     * @return true if the action matches the next required action in the sequence,
     * false otherwise.
     * 
     * @throws IllegalArgumentException if the action attempted is not one of the
     * valid actions as specified by getActionOptions().
     */
    public boolean takeAction(String action) {
        if (!getActionOptions().contains(action)) {
            throw new IllegalArgumentException("The action " + action
                    + " is not valid! Please enter one of the following actions: " + getActionOptions());
        }
        if (action.equals(requiredActions.get(currentActionIndex))) {
            currentActionIndex++;
            return true;
        }
        return false;
    }
}
