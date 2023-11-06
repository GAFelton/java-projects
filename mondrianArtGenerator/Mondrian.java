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
 * Once a region is below a certain size, it should be filled in with a color
 * chosen randomly from red, yellow, cyan, and white. When filling a region,
 * leave a one-pixel border around the edge uncolored -- this will give the
 * appearance of black lines separating the colored regions.


 */
public class Mondrian {

    private int getRandomDivider(int start, int end) {
        int returnNum = start + 1 + (int) (Math.random() * (double) (end - 1));
        return returnNum;
    }

    private Color getRandomColor() {
        Color[] colorOptions = new Color[] { Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE };
        return colorOptions[(int) (Math.random() * 4)];
    }

    private Color[][] defineAndColorShape(int left, int top, int right, int bottom) {
        Color[][] temp = new Color[bottom - top][right - left];
        Color shapeColor = getRandomColor();

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                // single pixel black border, else fill with color
                if (i == 0 || i == temp.length - 1 || j == 0 || j == temp[0].length - 1) {
                    temp[i][j] = Color.BLACK;
                } else {
                    temp[i][j] = shapeColor;
                }
            }
        }
        return temp;

    }

    private void addSubShapeToArtwork(Color[][] temp, Color[][] pixels) {
        int vPointer = 0;
        for (int i = 0; i < temp.length; i++) {
            int hPointer = 0;
            for (int j = 0; j < temp[0].length; j++) {
                pixels[vPointer][hPointer] = temp[i][j];
                hPointer++;
            }
            vPointer++;
        }
    }

    private void paintBasicMondrian(Color[][] pixels, int totalHeight, int totalWidth) {
        int left = 0;
        int top = 0;
        int height = pixels.length;
        int width = pixels[0].length;

        if (width >= (totalWidth) / 4 || height >= (totalHeight / 4)) {
            int verticalDivider = getRandomDivider(left, width);
            int horizontalDivider = getRandomDivider(top, height);

             if (width >= (totalWidth / 4)) {
                // left half
                int bottom = height;
                int right = left + verticalDivider;
                Color[][] temp = defineAndColorShape(left, top, right, bottom);
                paintBasicMondrian(temp, totalHeight, totalWidth);
                addSubShapeToArtwork(temp, pixels);

                // right half
                right = width;
                Color[][] temp2 = defineAndColorShape(verticalDivider, top, right, bottom);
                paintBasicMondrian(temp2, totalHeight, totalWidth);
                addSubShapeToArtwork(temp2, pixels);
            }
            if (height >= (totalHeight / 4)) {
                // top half
                int bottom = top + horizontalDivider;
                int right = width;
                Color[][] temp = defineAndColorShape(left, top, right, bottom);
                paintBasicMondrian(temp, totalHeight, totalWidth);
                addSubShapeToArtwork(temp, pixels);

                // bottom half
                bottom = height;
                Color[][] temp2 = defineAndColorShape(left, horizontalDivider, right, bottom);
                paintBasicMondrian(temp2, totalHeight, totalWidth);
                addSubShapeToArtwork(temp2, pixels);
            }
        }
    }

    // pixels with Color objects (using the java.awt.Color class) according to the
    // basic algorithm specified above.
    public void paintBasicMondrian(Color[][] pixels) {
        int totalHeight = pixels.length;
        int totalWidth = pixels[0].length;
        paintBasicMondrian(pixels, totalHeight, totalWidth);
    }

    // Fill pixels withColor objects (using the java.awt.Color class) based on your
    // chosen extension.
    public void paintComplexMondrian(Color[][] pixels) {

    }

}
