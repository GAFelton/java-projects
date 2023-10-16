import java.util.*;

/*
 * An EnduranceTask is a Task that requires a specified number of repetitions
 * of a specific action to complete.
 */
public class EnduranceTask extends Task {
    public static final List<String> actionOptions = Arrays.asList("jump", "swim", "run", "climb", "crawl");
    private String type;
    private int duration;

    /*
     * Construct a new EnduranceTask. Each EnduranceTask requires the following
     * inputs:
     * - Which action must be repeated? Use one of: "jump", "swim", "run", "climb", "crawl"
     * - How many times must that action be repeated?
     * - Describe the task to the user.
     */
    public EnduranceTask(String type, int duration, String description) {
        super(description);
        this.type = type;
        this.duration = duration;
    }

    /*
     * Returns the possible options that a user may take to attempt to complete this
     * task.
     * 
     * @return the list of valid actions for the task.
     */
    public List<String> getActionOptions() {
        return actionOptions;
    }

    /*
     * Returns whether or not this task has been completed.
     * 
     * @return true if the user has done the specific action as many times as
     * required.
     */
    public boolean isComplete() {
        if (duration == 0) {
            return true;
        }
        return false;
    }

    /*
     * Attempt to take an action towards completing the task.
     * Depending on how many repitions are required, the EnduranceTask may take
     * multiple iterations to complete.
     * 
     * @param action is the action to be attempted.
     * 
     * @return true if the action was successful, false otherwise.
     * 
     * @throws IllegalArgumentException if the action attempted is not one of the
     * valid actions as specified by getActionOptions().
     */
    public boolean takeAction(String action) {
        if (!getActionOptions().contains(action)) {
            throw new IllegalArgumentException("The action " + action
                    + " is not valid! Please enter one of the following actions: " + getActionOptions());
        }
        if (action.equals(type)) {
            duration--;
            return true;
        }
        return false;

    }
}
