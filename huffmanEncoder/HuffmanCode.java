import java.util.*;
import java.io.*;

/*
 * HuffmanCode by George Felton 12/2023
 * 
 * HuffmanCode is the interface which supports encoding/decoding user input via either
 * a provided HuffmanCompressor file.
 * HuffmanCode allows for the encoding of text via the Huffman Encoding Algorithm,
 * which maximises space storage by matching commonly-used characters with short bit
 * encodings. This program can encode and decode .txt files.
 */
public class HuffmanCode {
    private HuffmanNode overallRoot;

    /*
     * HuffmanCode constructor
     * Takes in an array of ASCII values/frequencies and assembles a binary tree
     * matching the Huffman encoding of those values. Characters with frequencies <=
     * 0 are not included.
     * 
     * @input: An array of frequencies, where each index is an ASCII value and the
     * integer at that index represents the number of occurrences in the input file.
     */
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> pq = new PriorityQueue<>();
        // Loop through frequencies array, adding relevant nodes to the PriorityQueue
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0) {
                HuffmanNode node = new HuffmanNode(toChar(i), frequencies[i]);
                pq.add(node);
            }
        }
        // Use the completed PriorityQueue to build the binary tree
        overallRoot = HuffmanCodeBuilder(pq);
    }

    /*
     * HuffmanCodeBuilder is a private recursive helper method which takes in a
     * PriorityQueue containing Nodes, each storing an ASCII value and it's
     * frequency of occurrence. Algorithm uses recursion to place HuffmanNodes in
     * order of frequency in the tree, and returns the subtree to the PriorityQueue.
     * This is repeated until only one node remains in the PriorityQueue, the root
     * node of the assembled encoding tree.
     * 
     * @input: Takes in and modifies the PriorityQueue assembled in the constructor.
     * 
     * @return: Returns the root node of an assembled subtree.
     */
    private HuffmanNode HuffmanCodeBuilder(Queue<HuffmanNode> pq) {
        HuffmanNode root;
        // Base case: If PriorityQueue has a size of 1, we've reached the full tree and
        // should return the root node.
        if (pq.size() == 1) {
            root = pq.remove();
            // Recursive case: Add the two least frequent nodes to a subtree, sum their
            // frequencies for the new root node, and add that node back to the
            // PriorityQueue.
        } else {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            HuffmanNode node = new HuffmanNode(toChar(0), left.frequency + right.frequency, left, right);
            pq.add(node);
            root = HuffmanCodeBuilder(pq);
        }
        return root;
    }

    /*
     * HuffmanCode constructor
     * This constructor initializes a new HuffmanCode tree by reading in a
     * previously constructed code from a .code file. This sets up this algorithm to
     * decode a previously-encoded file. In the case of decoding, frequency of
     * occurrence is no longer a factor, so this constructor ignores it and assigns
     * the value `0` as the frequency for each node.
     * 
     * @input: A Scanner reading the specified .code file. It is assumed that the
     * Scanner is not null and always contains properly encoded data.
     */
    public HuffmanCode(Scanner input) {
        // while loop to ingest all input. On each loop, kicks off recursive function to
        // update the tree based on the current character/encoding.
        while (input.hasNext() != false) {
            String character = toChar(Integer.parseInt(input.nextLine()));
            String location = input.nextLine();
            overallRoot = HuffmanCodeScanner(overallRoot, character, location);
        }
    }

    /*
     * HuffmanCodeScanner is a private recursive helper function to build out the
     * Huffman binary tree based on the .code cipher file. Uses recursion to
     * traverse the tree, building nodes as needed, to place the provided character
     * at the correct spot to match it's encoding.
     * 
     * @input: Takes in a node, the current character, and encoding for that
     * character.
     * 
     * @return: Returns the root node of the tree, building out the overall encoding
     * tree on each recursion.
     */
    private HuffmanNode HuffmanCodeScanner(HuffmanNode root, String character, String location) {
        String curr = location.substring(0, 1);
        // Initial case & non-leaf case - build the root node.
        if (root == null) {
            root = new HuffmanNode(" ", 0);
        }
        // Base case: if we've reached the node just prior to the desired location for
        // the character, add it.
        if (location.length() - 1 <= 0) {
            if (curr.equals("0")) {
                root.left = new HuffmanNode(character, 0);
            }
            if (curr.equals("1")) {
                root.right = new HuffmanNode(character, 0);
            }
            // Recursive case: Recurse on left or right nodes, as specified by the enoding
            // string.
        } else {
            String remainder = location.substring(1, location.length());

            if (curr.equals("0")) {
                root.left = HuffmanCodeScanner(root.left, character, remainder);
            } else if (curr.equals("1")) {
                root.right = HuffmanCodeScanner(root.right, character, remainder);
            }
        }
        return root;
    }

    /*
     * save is a method that stores the current HuffmanCode object in a .code
     * cipher, for later loading and use. The encoding is stored as pairs of
     * integers, each on their own line, with the first being the ASCII
     * representation of a character and the second being the Huffman encoding. An
     * example would be:
     * ```
     * 97
     * 0
     * 99
     * 10
     * 98
     * 11
     * ```
     * 
     * @input: PrintStream attached to the .code file which will store the encoding.
     */
    public void save(PrintStream output) {
        save(output, overallRoot, "");
    }

    /*
     * Private recursive helper function for save method that reads the stored
     * HuffmanCode binary tree and outputs the encoding to a .code file.
     * 
     * @input: PrintStream attached to the .code file, The current tree node, and
     * the encoding from the overall root to the current node.
     */
    private void save(PrintStream output, HuffmanNode root, String encodingString) {
        // base case: null node, do not recurse
        if (root != null) {
            // base case: leaf node, write cipher data to .code file
            if (root.left == null && root.right == null) {
                output.println(toASCII(root.data));
                output.println(encodingString);
                // Recursive case: Adjust encodingString depending on left vs. right, and
                // recurse on both.
            } else {
                // Add 0 for left
                encodingString += "0";
                save(output, root.left, encodingString);
                // Replace 0 with 1 for right
                encodingString = encodingString.substring(0, encodingString.length() - 1) + "1";
                save(output, root.right, encodingString);
            }
        }
    }

    /*
     * translate is a method that reads individual bits from an input stream and
     * writes the corresponding characters to the output. Translate will stop
     * reading when the input stream is empty, and assumes that the input stream is
     * a legal encoding of characters for this tree's Huffman Code.
     * 
     * @input: The BitInputStream originating from the encoded .short file that is
     * meant to be decoded, and the desired PrintStream output (whether stdout or
     * file).
     */
    public void translate(BitInputStream input, PrintStream output) {
        String bits = "";
        // While there are more bits in the input stream, keep adding them and checking
        // the encoding tree until a character is reached. Then reset our current bits
        // counter and continue
        while (input.hasNextBit() == true) {
            int bit = input.nextBit();
            bits += String.valueOf(bit);
            boolean newChar = translateHelper(bits, overallRoot, output, false);
            if (newChar == true) {
                bits = "";
            }
        }
    }

    /*
     * Private recursive helper function translateHelper checks a provided encoding
     * against the HuffmanCode binary tree and either adds a character to the print
     * stream and returns true, or returns false if the provided encoding does not
     * match a known character.
     * 
     * @input: String representation of the encoding (ex. "1001"), the current node,
     * the desired output stream, and a boolean tracking whether a character was
     * successfully decoded.
     * 
     * @return: Returns a boolean: true if a character was successfully decoded and
     * written to the output, or false if otherwise.
     */
    private boolean translateHelper(String bits, HuffmanNode root, PrintStream output, boolean returnBool) {
        // Base Case: leaf node, print character and return true, telling base function
        // to reset the current bits string.
        if (root.left == null && root.right == null) {
            output.write(toASCII(root.data));
            returnBool = true;
            // Base case: if there are no more bits to read, return boolean
        } else if (bits.length() > 0) {
            String curr = bits.substring(0, 1);
            String remainder = "";
            if (bits.length() > 1) {
                remainder = bits.substring(1);
            }
            // Recursive case: Check either left or right matching the current front
            // character of the encoding string.
            if (curr.equals("0")) {
                returnBool = translateHelper(remainder, root.left, output, returnBool);
            } else if (curr.equals("1")) {
                returnBool = translateHelper(remainder, root.right, output, returnBool);
            }
        }
        return returnBool;
    }

    // Helper function to convert an ASCII integer into its corresponding character
    private String toChar(int ASCII) {
        return Character.toString(ASCII);
    }

    // Helper function to convert a character into its corresponding ASCII integer
    private int toASCII(String character) {
        return (int) character.charAt(0);
    }

    /*
     * HuffmanNode is a private static inner class of HuffmanCode representing each
     * tree node. Each node stores the character, its frequency of occurrence, and
     * references to left/right nodes. HuffmanNodes are comparable to each other
     * based on the stored frequency.
     */
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public String data;
        public int frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        // Constructs a leaf node with the given data.
        public HuffmanNode(String data, int frequency) {
            this(data, frequency, null, null);
        }

        // Constructs a leaf or branch node with the given data and links.
        public HuffmanNode(String data, int frequency, HuffmanNode left, HuffmanNode right) {
            this.data = data;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        // Implement Comparable interface, comparing based on frequency
        public int compareTo(HuffmanNode node) {
            if (this.frequency < node.frequency) {
                return -1;
            } else if (this.frequency > node.frequency) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
