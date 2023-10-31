import java.util.*;
import java.text.SimpleDateFormat;

// TODO: Review Code comments to remove implementation details
public class Repository {
    private Commit head;
    private String name;

    /*
     * Creates a new, empty repository with the specified name.
     * 
     * @Exception
     * If the name is null or empty, throw an IllegalArgumentException
     */
    public Repository(String name) {
        if (name == null || name == "") {
            throw new IllegalArgumentException("Must give a non-empty String name.");
        }
        this.name = name;
        this.head = null;
    }

    /*
     * getRepoHead returns the current head.
     * 
     * @Return
     * Return the ID of the current head of this repository.
     * If the head is null, return null
     */
    public String getRepoHead() {
        if (head == null) {
            return null;
        } else {
            return head.id;
        }
    }

    /*
     * Returns the number of commits in the repository.
     * 
     * @Return
     * Integer representing number of commits
     */
    public int getRepoSize() {
        if (head == null) {
            return 0;
        } else if (head.past == null) {
            return 1;
        } else {
            int counter = 0;
            Commit curr = head;
            while (curr != null) {
                counter++;
                curr = curr.past;
            }
            return counter;
        }
    }

    /*
     * @Return
     * Return a string representation of this repository in the following format:
     * <name> - Current head: <head>
     * 
     * <head> should be the result of calling toString() on the head commit.
     * If there are no commits in this repository, instead return:
     * <name> - No commits
     */
    public String toString() {
        String returnString = String.format("%s - ", name);
        if (head == null) {
            return returnString + "No commits";
        } else {
            return returnString + "Current head: " + head.toString();
        }
    }

    /*
     * @Return
     * Return true if the commit with ID targetId is in the repository, false if
     * not.
     */
    public boolean contains(String targetId) {
        if (head == null) {
            return false;
        }
        Commit curr = head;
        while (curr != null) {
            if (curr.id.equals(targetId)) {
                return true;
            }
            curr = curr.past;
        }
        return false;
    }

    /*
     * Return a string consisting of the String representations of the most recent
     * n commits in this repository, with the most recent first.
     * Commits should be separated by a newline (\n) character.
     * 
     * @Return
     * If there are fewer than n commits in this repository, return them all.
     * If there are no commits in this repository, return the empty string.
     * If n is non-positive, throw an IllegalArgumentException.
     */
    public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("getHistory() input must be a positive integer.");
        }
        String returnString = "";
        Commit curr = head;
        int i = 0;
        while (curr != null && i < n) {
            returnString += curr.toString() + "\n";
            curr = curr.past;
            i++;
        }
        return returnString;
    }

    /*
     * Create a new commit with the given message, add it to this repository.
     * The new commit should become the new head of this repository, preserving the
     * history behind it.
     * 
     * @Return
     * Return the ID of the new commit.
     */
    public String commit(String message) {
        Commit newCommit;
        if (head != null) {
            newCommit = new Commit(message, head);
        } else {
            newCommit = new Commit(message);
        }
        head = newCommit;
        return newCommit.id;
    }

    /*
     * Remove the commit with ID targetId from this repository, maintaining the rest
     * of the history.
     * 
     * @Return
     * Returns true if the commit was successfully dropped, or
     * false if there is no commit that matches the given ID in the repository.
     */
    public boolean drop(String targetId) {
        // If repo is empty or does not contain targetId, return false.
        if (this.getRepoSize() == 0 || !this.contains(targetId)) {
            return false;
        }
        // If head is designated for removal, change head reference.
        if (head.id.equals(targetId)) {
            if (head.past == null) {
                head = null;
            } else {
                head = head.past;
            }
            return true;
        }
        // Otherwise, loop through array, looking for match
        Commit curr = head;
        while (curr != null && curr.past != null) {
            if (curr.past.id.equals(targetId)) {
                if (curr.past.past == null) {
                    curr.past = null;
                } else {
                    curr.past = curr.past.past;
                }
                return true;
            }
            curr = curr.past;
        }
        return false;
    }

    /*
     * Takes all the commits in the other repository and moves them into this
     * repository, combining the two repository histories such that chronological
     * order is preserved. That is, after executing this method, this repository
     * should contain all commits that were from this and other, and the commits
     * should be ordered in timestamp order from most recent to least recent.
     * If the other repository is empty, this repository should remain unchanged.
     * If this repository is empty, all commits in the other repository should be
     * moved into this repository.
     * At the end of this method's execution, other should be an empty repository in
     * all cases.
     * You should not construct any new Commit objects to implement this method. You
     * may however create as many references as you like.
     */
    public void synchronize(Repository other) {
        if (other.getRepoSize() == 0) {
            // If other repo is empty, do nothing.
        } else if (this.getRepoSize() == 0) {
            this.head = other.head;
            other.head = null;
        } else {
            Commit curr = head;
            while (curr.past != null && other.head != null) {
                if (head.timeStamp < other.head.timeStamp) {
                    Commit temp = other.head.past;
                    other.head.past = head;
                    head = other.head;
                    other.head = temp;
                    curr = head;
                }
                else if (curr.timeStamp < other.head.timeStamp) {
                    // if curr is earlier than other
                    Commit temp = other.head.past;
                    other.head.past = curr;
                    curr = other.head;
                    other.head = temp;
                }
                else if (curr.timeStamp > other.head.timeStamp) {
                    // if curr is later than other
                    if (curr.past.timeStamp <= other.head.timeStamp) {
                        // but curr.past is earlier than other
                        Commit temp = curr.past;
                        curr.past = other.head;
                        other.head = other.head.past;
                        curr.past.past = temp;
                    }
                    curr = curr.past;
                }
            }
            if (other.head != null) {
                curr.past = other.head;
                other.head = null;
            }
        }
    }

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this
     * class openly mention the fields of the class. This is fine
     * because the fields of the Commit class are public. In general,
     * be careful about revealing implementation details!
     */
    public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * 
         * @param message A message describing the changes made in this commit.
         * @param past    A reference to the commit made immediately before this
         *                commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * 
         * @param message A message describing the changes made in this commit.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         * "[identifier] at [timestamp]: [message]"
         * 
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
         * Resets the IDs of the commit nodes such that they reset to 0.
         * Primarily for testing purposes.
         */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
