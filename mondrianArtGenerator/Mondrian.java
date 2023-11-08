import java.util.*;
import java.awt.*;

/*
 * Mondrian by George Felton
 * 
 * This is a program designed to generate random artwork in the style of Mondrian.
 * Each random artwork will start with a blank canvas of a given width and height
 * and will repeatedly break the canvas into smaller and smaller regions until the
 * regions are below a certain size. The specifics of this algorithm are be as follows:
 * 
 * - If the region being considered is at least one-fourth the height of
 * the full canvas and at least one-fourth the width of the full canvas,
 * split it into four smaller regions by choosing one vertical and one horizontal
 * dividing line at random.
 * - If the region being considered is at least one-fourth the height of
 * the full canvas, split it into two smaller regions by choosing a horizontal
 * dividing line at random.
 * - If the region being considered is at least one-fourth the width of
 * the full canvas, split it into two smaller regions by choosing a vertical
 * dividing line at random.
 * - If the region being considered is smaller than one-fourth the height of
 * the full canvas and smaller than one-fourth the width of the full canvas,
 * do not split the region.
 * 
 * Any time a region is split, the dividing line(s) should be chosen at random
 * to be within the bounds of the region.
 * 
 * Once a region is below a certain size, it will be filled in with a color
 * chosen randomly from red, yellow, cyan, and white.
 * 
 * If the complex version is chosen, it will choose between a rectangle, oval,
 * or cross when determining how to fill the region.
 */
public class Mondrian {

    /*
     * getRandomDivider is a helper function that determines a point at which to
     * split a region, whether horizontally or vertically.
     * 
     * @return an integer representation of a random point between the two points
     * fed into the function.
     */
    private int getRandomDivider(int start, int end) {
        int returnNum = start + 1 + (int) (Math.random() * (double) (end - 1));
        return returnNum;
    }

    /*
     * getRandomColor is a helper function that returns a random color among red,
     * yellow, cyan, or white.
     * 
     * @return a Color for use with filling shapes.
     */
    private Color getRandomColor() {
        Color[] colorOptions = new Color[] { Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE };
        return colorOptions[(int) (Math.random() * 4)];
    }

    /*
     * colorArea is a helper function that fills in a given rectangular subsection
     * within a provided larger 2D color array. The color is determined randomly,
     * and the rectangle is filled with that color and surrounded by a 1px thick
     * black border.
     */
    private void colorArea(Color[][] pixels, int left, int top, int right, int bottom) {
        Color shapeColor = getRandomColor();

        for (int i = top; i < bottom; i++) { // height
            for (int j = left; j < right; j++) { // width
                // single pixel black border, else fill with color
                if (i == top || i == bottom - 1 || j == left || j == right - 1) {
                    pixels[i][j] = Color.BLACK;
                } else {
                    pixels[i][j] = shapeColor;
                }
            }
        }
    }

    /*
     * Helper function to determine if a given point is within an oval.
     * 
     * @return a double which helps tell whether a given point (x,y) is within an
     * oval. Returns 1 if outside the oval, or <= 1 if inside the oval.
     * 
     * I translated a formula written by user Srivatsan
     * (https://math.stackexchange.com/users/13425/srivatsan), from
     * Math.StackExchange for their answer to "Check if a
     * point is within an ellipse", URL (version: 2011-10-27):
     * https://math.stackexchange.com/q/76463
     */
    private double isInOval(int x, int y, int left, int top, int right, int bottom) {
        double hRad = (right - left) / 2;
        double hMid = hRad + left;
        double vRad = (bottom - top) / 2;
        double vMid = vRad + top;

        double val = Math.pow(x - hMid, 2) / Math.pow(hRad, 2) + Math.pow(y - vMid, 2) / Math.pow(vRad, 2);
        return val;

    }

    /*
     * colorAreaOval is a helper function that inserts an oval into a given
     * rectangular subsection within a provided larger 2D color array. The colors
     * are determined randomly, and the rectangular subsection is surrounded by a
     * black 1px thick border. The oval fills the rectangle.
     */
    private void colorAreaOval(Color[][] pixels, int left, int top, int right, int bottom) {
        Color shapeColor = getRandomColor();
        Color shapeColor2 = shapeColor;
        while (shapeColor2 == shapeColor) {
            shapeColor2 = getRandomColor();
        }
        for (int i = top; i < bottom; i++) { // height
            for (int j = left; j < right; j++) { // width
                double ovalVal = isInOval(j, i, left, top, right, bottom);
                // single pixel black border
                if (i == top || i == bottom - 1 || j == left || j == right - 1 || ovalVal == 1) {
                    pixels[i][j] = Color.BLACK;
                    // if inside oval, fill with primary color
                } else if (ovalVal < 1) {
                    pixels[i][j] = shapeColor;
                    // if outside oval, fill with secondary color
                } else {
                    pixels[i][j] = shapeColor2;
                }
            }
        }
    }

    /*
     * colorAreaCross is a helper function that inserts a cross into a given
     * rectangular subsection within a provided larger 2D color array. The colors
     * are determined randomly, and the rectangular subsection is surrounded by a
     * black 1px thick border. The cross is centered around the midpoint of the
     * rectangular subsection.
     */
    private void colorAreaCross(Color[][] pixels, int left, int top, int right, int bottom) {
        Color shapeColor = getRandomColor();
        Color shapeColor2 = shapeColor;
        while (shapeColor2 == shapeColor) {
            shapeColor2 = getRandomColor();
        }

        double hMid = (right - left) / 2 + left;
        double vMid = (bottom - top) / 2 + top;
        int crossWidth = 2;

        for (int i = top; i < bottom; i++) { // height
            for (int j = left; j < right; j++) { // width
                // single pixel black border
                if (i == top || i == bottom - 1 || j == left || j == right - 1) {
                    pixels[i][j] = Color.BLACK;
                    // create cross by finding rectangles for each corner of rectangle
                } else if (j + crossWidth < hMid && i + crossWidth < vMid
                        || j - crossWidth > hMid && i - crossWidth > vMid
                        || j - crossWidth > hMid && i + crossWidth < vMid
                        || j + crossWidth < hMid && i - crossWidth > vMid) {
                    pixels[i][j] = shapeColor;
                } else {
                    pixels[i][j] = shapeColor2;
                }
            }
        }

    }

    /*
     * paintBasicMondrian is a private helper function that uses recursion to fill a
     * given 2D color array with colored rectangles. It follows a set algorithm to
     * split up the canvas:
     * - If both height and width are greater than 1/4 of the overall canvas, split
     * this section into randomly-sized quadrants.
     * - If either height OR width are greater than 1/4 of the overall canvas (but
     * not both), split this section into randomly-sized halves.
     * - If neither height nor width are greater than 1/4 of the overall canvas,
     * fill in this section with a randomly selected color.
     */
    private void paintBasicMondrian(Color[][] pixels, int left, int top, int right, int bottom) {
        int height = bottom - top;
        int width = right - left;
        int verticalDivider = getRandomDivider(left, width);
        int horizontalDivider = getRandomDivider(top, height);

        if (height >= pixels.length / 4 && width >= pixels[0].length / 4) {
            // order of inputs: (Color[][], left, top, right, bottom)
            paintBasicMondrian(pixels, left, top, verticalDivider, horizontalDivider); // top left
            paintBasicMondrian(pixels, verticalDivider, top, right, horizontalDivider); // bottom left
            paintBasicMondrian(pixels, left, horizontalDivider, verticalDivider, bottom); // top right
            paintBasicMondrian(pixels, verticalDivider, horizontalDivider, right, bottom); // bottom right
        } else if (height >= pixels.length / 4) {
            paintBasicMondrian(pixels, left, top, right, horizontalDivider); // top half
            paintBasicMondrian(pixels, left, horizontalDivider, right, bottom); // bottom half
        } else if (width >= pixels[0].length / 4) {
            paintBasicMondrian(pixels, left, top, verticalDivider, bottom); // left half
            paintBasicMondrian(pixels, verticalDivider, top, right, bottom); // right half
        } else {
            colorArea(pixels, left, top, right, bottom); // base case
        }
    }

    /*
     * paintComplexMondrian is a private helper function that uses recursion to fill
     * a given 2D color array with colored shapes. It follows a set algorithm to
     * split up the canvas:
     * - If both height and width are greater than 1/4 of the overall canvas, split
     * this section into randomly-sized quadrants.
     * - If either height OR width are greater than 1/4 of the overall canvas (but
     * not both), split this section into randomly-sized halves.
     * - If neither height nor width are greater than 1/4 of the overall canvas,
     * fill in this section with a randomly selected shape of a random basic color,
     * chosen from rectangle, oval, or cross.
     */
    private void paintComplexMondrian(Color[][] pixels, int left, int top, int right, int bottom) {
        int height = bottom - top;
        int width = right - left;
        int verticalDivider = getRandomDivider(left, width);
        int horizontalDivider = getRandomDivider(top, height);

        if (height >= pixels.length / 4 && width >= pixels[0].length / 4) {
            // order of inputs: (Color[][], left, top, right, bottom)
            paintComplexMondrian(pixels, left, top, verticalDivider, horizontalDivider); // top left
            paintComplexMondrian(pixels, verticalDivider, top, right, horizontalDivider); // bottom left
            paintComplexMondrian(pixels, left, horizontalDivider, verticalDivider, bottom); // top right
            paintComplexMondrian(pixels, verticalDivider, horizontalDivider, right, bottom); // bottom right
        } else if (height >= pixels.length / 4) {
            paintComplexMondrian(pixels, left, top, right, horizontalDivider); // top half
            paintComplexMondrian(pixels, left, horizontalDivider, right, bottom); // bottom half
        } else if (width >= pixels[0].length / 4) {
            paintComplexMondrian(pixels, left, top, verticalDivider, bottom); // left half
            paintComplexMondrian(pixels, verticalDivider, top, right, bottom); // right half
        } else {
            // base case: fill shape
            // 33% chance to fill with a rectangle
            // 33% chance to fill with a cross
            // 33% chance to fill with an oval
            int randomShape = (int) (Math.random() * 3);
            if (randomShape == 1) {
                colorArea(pixels, left, top, right, bottom);
            } else if (randomShape == 2) {
                colorAreaCross(pixels, left, top, right, bottom);
            } else {
                colorAreaOval(pixels, left, top, right, bottom);
            }
        }
    }

    /*
     * paintBasicMondrian takes in a blank canvas and fills it with randomly
     * sized/color rectangles.
     */
    public void paintBasicMondrian(Color[][] pixels) {
        paintBasicMondrian(pixels, 0, 0, pixels.length, pixels[0].length);
    }

    /*
     * paintComplexMondrian takes in a blank canvas and fills it with randomly
     * sized/color shapes, chosen randomly from rectangles, ovals, and crosses.
     */
    public void paintComplexMondrian(Color[][] pixels) {
        paintComplexMondrian(pixels, 0, 0, pixels.length, pixels[0].length);
    }

}
