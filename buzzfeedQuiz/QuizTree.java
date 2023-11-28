import java.io.PrintStream;
import java.util.*;

/*
 * QuizTree class contains logic supporting a BuzzFeed style quiz built using a
 * binary tree data structure.
 */
public class QuizTree {
    private QuizTreeNode overallRoot;

    /*
     * QuizTree constructor
     * The QuizTree object is a binary tree of quiz questions. QuizTree root nodes
     * ask the user to choose between two options, and leaf nodes describe a
     * conclusion point for the quiz based on which choices were made along the way.
     * 
     * An empty QuizTree is still a valid tree and one which can be made, which we
     * can then addQuestions to.
     */
    public QuizTree() {
        overallRoot = null;
    }
    /*
     * QuizTree constructor
     * The QuizTree object is a binary tree of quiz questions. QuizTree root nodes
     * ask the user to choose between two options, and leaf nodes describe a
     * conclusion point for the quiz based on which choices were made along the way.
     * 
     * @input takes in and reads a txt file to construct the binary tree containing
     * the quiz. The input file must match the following format:
     * red/blue
     * yellow/green
     * END:Froot Loops
     * END:Raisin Bran
     * purple/orange
     * END:Frosted Flakes
     * black/white
     * END:Rice Krispies
     * END:Fruity Pebbles
     */

    public QuizTree(Scanner inputFile) {
        overallRoot = QuizTreeBuilder(inputFile, overallRoot);
    }

    /*
     * QuizTreeBuilder is a recursive private helper function which constructs the
     * QuizTree based off of a provided txt file. A node is determined either to be
     * a root node if it contains a "/" or a leaf node if it begins with the string
     * "END:".
     * 
     * @input Takes in the txt input file and the current node.
     * 
     * @return Returns the current node after construction, including filling out
     * branches.
     */
    private QuizTreeNode QuizTreeBuilder(Scanner inputFile, QuizTreeNode node) {
        String currInput = inputFile.nextLine();

        // If the current line is a question node, create a new root node and recurse
        // down into the QuizTree.
        if (currInput.contains("/")) {
            String leftChoice = currInput.substring(0, currInput.indexOf("/"));
            String rightChoice = currInput.substring(currInput.indexOf("/") + 1);
            String data = "Do you prefer " + leftChoice + " or " + rightChoice + "? ";
            node = new QuizTreeNode(data, leftChoice, rightChoice, null, null);
            node.leftResult = QuizTreeBuilder(inputFile, node.leftResult);
            node.rightResult = QuizTreeBuilder(inputFile, node.rightResult);
            // base case: If we've reached a result leaf node, add it to the quiz and do not
            // recurse further.
        } else if (currInput.contains("END:")) {
            String data = "Your result is: " + currInput.substring(currInput.indexOf(":") + 1);
            return new QuizTreeNode(data);
        }
        return node;
    }

    /*
     * takeQuiz requests user input to guide a user through the QuizTree. It will
     * advance through nodes based on the user's inputs, and will stop advancing
     * once it has reached a leaf node.
     * 
     * @input Console capturing user input on the command line.
     */
    public void takeQuiz(Scanner console) {
        takeQuiz(console, overallRoot);
    }

    /*
     * Private recursive helper function for takeQuiz. Checks user input against the
     * left and right choices of the current node, ending the quiz if the user has
     * reached a leaf node and printing the results.
     * 
     * @input Takes in the console capturing command-line user input and the current
     * QuizTree node.
     * 
     * @prints output to System.out.
     */
    private void takeQuiz(Scanner console, QuizTreeNode node) {
        System.out.print(node.data);
        // Base case: if we've reached a leaf node, print its data and end the quiz.
        if (node.leftResult != null) {
            String userInput = console.nextLine().toLowerCase();
            // If the user has inputted a string matching either leftChoice or rightChoice,
            // recurse down through the matching result node.
            if (userInput.equals(node.leftChoice)) {
                takeQuiz(console, node.leftResult);
            } else if (userInput.equals(node.rightChoice)) {
                takeQuiz(console, node.rightResult);
            } else {
                System.out.println("Invalid response; try again.");
                takeQuiz(console, node);
            }
        }
    }

    /*
     * export allows the user to write the QuizTree as it currently stands to a txt
     * file for future import. It does the inverse of the QuizTree constructor.
     * 
     * @input The specified output file
     */
    public void export(PrintStream outFile) {
        export(outFile, overallRoot);
        outFile.close();
    }

    /*
     * Private recursive helper function for export. Goes through each node and
     * writes to a file. Root nodes include the possible choices separated by a "/",
     * while leaf nodes are denoted by "END:".
     * 
     * @input Takes in the file to be written to, as well as the current node.
     */
    private void export(PrintStream outFile, QuizTreeNode node) {
        String currOutput = node.data;
        if (currOutput.substring(0, 13).equals("Do you prefer")) {
            // Add the current question and recurse through both choices.
            outFile.println(node.leftChoice + "/" + node.rightChoice);
            export(outFile, node.leftResult);
            export(outFile, node.rightResult);
            // base case: We've reached a leaf node. Print it to the file.
        } else if (currOutput.substring(0, 15).equals("Your result is:")) {
            outFile.println("END:" + currOutput.substring(currOutput.indexOf(":") + 2));
        }
    }

    /*
     * addQuestion replaces a specified leaf node with a new node, which includes
     * two choices which each lead to a new leaf node.
     * If the specified node is not found or is not a leaf node, addQuestion does
     * nothing.
     * 
     * @inputs:
     * The string matching the data of the node to be replaced. Case insensitive.
     * The left choice - the string which the user may select, leading to the left
     * result.
     * The right choice - the string which the user may select, leading to the right
     * result.
     * The left result - the result node which is reached by the user selecting the
     * left choice.
     * The right result - the result node which is reached by the user selecting the
     * rigt choice.
     */
    public void addQuestion(String toReplace, String leftChoice, String rightChoice, String leftResult,
            String rightResult) {
        overallRoot = addQuestion(toReplace, leftChoice, rightChoice, leftResult, rightResult, overallRoot);
    }

    /*
     * private recursive helper function addQuestion searches through each node of
     * the QuizTree until it finds a match, at which point it will replace the leaf
     * node containing the specified result with the new information provided.
     * 
     * @inputs:
     * The string matching the data of the node to be replaced. Case insensitive.
     * The left choice - the string which the user may select, leading to the left
     * result.
     * The right choice - the string which the user may select, leading to the right
     * result.
     * The left result - the result node which is reached by the user selecting the
     * left choice.
     * The right result - the result node which is reached by the user selecting the
     * rigt choice.
     * The current node.
     */
    private QuizTreeNode addQuestion(String toReplace, String leftChoice, String rightChoice, String leftResult,
            String rightResult, QuizTreeNode root) {
        // Base case: We've reached a leaf node as the current node with no matches
        // found.
        // We do not check for rightResult - if there is a leftResult, there must also
        // be a rightResult, because result leaf nodes do not contain either.
        if (root.leftResult != null) {
            // Base cases: Checks the left and right result nodes for a match rather than
            // the current node, because we need to be able to preserve reference to them
            // after replacing them with new nodes.
            if (root.leftResult.data.substring(0, 15).equals("Your result is:")
                    && root.leftResult.data.substring(16).toLowerCase().equals(toReplace.toLowerCase())) {
                root.leftResult = addNode(leftChoice, rightChoice, leftResult, rightResult);
            } else if (root.rightResult.data.substring(0, 15).equals("Your result is:")
                    && root.rightResult.data.substring(16).toLowerCase().equals(toReplace.toLowerCase())) {
                root.rightResult = addNode(leftChoice, rightChoice, leftResult, rightResult);
            } else {
                // If we do not find a match, recurse through the QuizTree
                root.leftResult = addQuestion(toReplace, leftChoice, rightChoice, leftResult, rightResult,
                        root.leftResult);
                root.rightResult = addQuestion(toReplace, leftChoice, rightChoice, leftResult, rightResult,
                        root.rightResult);
            }
        }
        return root;
    }

    /*
     * private helper function addNode inserts a new node that replaces an existing
     * leaf node with a new root node and it's own resulting leaf nodes.
     * 
     * @inputs
     * The left choice - the string which the user may select, leading to the left
     * result.
     * The right choice - the string which the user may select, leading to the right
     * result.
     * The left result - the result node which is reached by the user selecting the
     * left choice.
     * The right result - the result node which is reached by the user selecting the
     * rigt choice.
     * 
     * @return Returns the new root node which contains the question and two result
     * node answers.
     */
    private QuizTreeNode addNode(String leftChoice, String rightChoice, String leftResult,
            String rightResult) {
        String data = "Do you prefer " + leftChoice + " or " + rightChoice + "? ";
        QuizTreeNode root = new QuizTreeNode(data, leftChoice, rightChoice, null, null);
        root.leftResult = new QuizTreeNode("Your result is: " + leftResult);
        root.rightResult = new QuizTreeNode("Your result is: " + rightResult);

        return root;
    }

    /*
     * QuizTreeNode class represents a single node in the tree. Each node contains
     * data - the question being asked or the result at the end of the quiz.
     * Beyond this, root nodes also contain the left and right choices a user may
     * select, which will lead to their respective left and right result nodes.
     */
    public static class QuizTreeNode {

        public String data;
        public String leftChoice;
        public String rightChoice;
        public QuizTreeNode leftResult;
        public QuizTreeNode rightResult;

        // Constructs a leaf node with the given data.
        public QuizTreeNode(String data) {
            this(data, null, null, null, null);
        }

        // Constructs a leaf or branch node with the given data, choices, and links to
        // other nodes.
        public QuizTreeNode(String data, String leftChoice, String rightChoice, QuizTreeNode leftResult,
                QuizTreeNode rightResult) {
            this.data = data;
            this.leftChoice = leftChoice;
            this.rightChoice = rightChoice;
            this.leftResult = leftResult;
            this.rightResult = rightResult;
        }
    }
}