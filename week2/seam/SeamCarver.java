/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 15/04/2022
 *  Description: Algos 2 Seam Carving assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {

    private static final int TOP_VIRTUAL_INDEX = 0;
    private static final int BOTTOM_VIRTUAL_INDEX = 1;
    private static final int NUM_VIRTUAL_VERTICES = 2;

    Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        // defensive copy
        this.picture = new Picture(picture);
    }

    private int toIndex(int col, int row) {
        return row * width() + col + NUM_VIRTUAL_VERTICES;
    }

    private int toCol(int index) {
        return (index - NUM_VIRTUAL_VERTICES) % width();
    }

    private int toRow(int index) {
        return (index - NUM_VIRTUAL_VERTICES) / width();
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        // Avoid redundant calls to the get() method in Picture.
        // For example, to access the red, green, and blue components of a pixel,
        // call get() only once (and not three times).
        int rgbRight = picture.getRGB(x + 1, y);
        int redRight = (rgbRight >> 16) & 0xFF;
        int greenRight = (rgbRight >> 8) & 0xFF;
        int blueRight = rgbRight & 0xFF;
        int rgbLeft = picture.getRGB(x - 1, y);
        int redLeft = (rgbLeft >> 16) & 0xFF;
        int greenLeft = (rgbLeft >> 8) & 0xFF;
        int blueLeft = rgbLeft & 0xFF;
        int xSquared = (redRight - redLeft) * (redRight - redLeft) +
                (greenRight - greenLeft) * (greenRight - greenLeft) +
                (blueRight - blueLeft) * (blueRight - blueLeft);

        int rgbTop = picture.getRGB(x, y - 1);
        int redTop = (rgbTop >> 16) & 0xFF;
        int greenTop = (rgbTop >> 8) & 0xFF;
        int blueTop = rgbTop & 0xFF;
        int rgbBottom = picture.getRGB(x, y + 1);
        int redBottom = (rgbBottom >> 16) & 0xFF;
        int greenBottom = (rgbBottom >> 8) & 0xFF;
        int blueBottom = rgbBottom & 0xFF;
        int ySquared = (redBottom - redTop) * (redBottom - redTop) +
                (greenBottom - greenTop) * (greenBottom - greenTop) +
                (blueBottom - blueTop) * (blueBottom - blueTop);

        return Math.sqrt((double) xSquared + ySquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // TODO
        return null;
    }

    private void relax(int v, int w, double[] distTo, int[] edgeTo, double[] energy) {
        if (distTo[w] > distTo[v] + energy[w]) {
            distTo[w] = distTo[v] + energy[w];
            edgeTo[w] = v;
        }
    }

    private int[] adj(int index) {
        if (index == TOP_VIRTUAL_INDEX) {
            // return array containing top row
            int[] retVal = new int[width()];
            for (int i = 0; i < width(); i++) {
                retVal[i] = i + NUM_VIRTUAL_VERTICES;
            }
            return retVal;
        }

        if (index == BOTTOM_VIRTUAL_INDEX) {
            return null;
        }

        int col = toCol(index);
        int row = toRow(index);

        // Bottom row
        if (row == height() - 1) {
            return null;
        }

        // Right-most column
        if (col == 0) {
            return new int[] { index + width(), index + width() + 1 };
        }

        // Left-most column
        if (col == width() - 1) {
            return new int[] { index + width() - 1, index + width() };
        }

        // All other vertices
        return new int[] { index + width() - 1, index + width(), index + width() + 1 };
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // When finding a seam, call energy() at most once per pixel.
        // For example, you can save the energies in a local variable energy[][]
        // and access the information directly from the 2D array (instead of recomputing from scratch).
        double[] energy = new double[height() * width() + NUM_VIRTUAL_VERTICES];

        // calculate energies
        energy[TOP_VIRTUAL_INDEX] = 0;
        energy[BOTTOM_VIRTUAL_INDEX] = 0;
        int i = NUM_VIRTUAL_VERTICES;
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[i] = energy(x, y);
                i++;
            }
        }

        // Initialize edgeTo
        int[] edgeTo = new int[height() * width() + NUM_VIRTUAL_VERTICES];
        Arrays.fill(edgeTo, -1);

        // Initialize distTo
        double[] distTo = new double[height() * width() + NUM_VIRTUAL_VERTICES];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[TOP_VIRTUAL_INDEX] = 0.0;

        // Calculate
        for (int v = 0; v < energy.length; v++) {
            int[] neighbors = adj(v);
            if (neighbors == null) {
                continue;
            }

            for (int w : neighbors) {
                relax(v, w, distTo, edgeTo, energy);
            }
        }

        // TODO
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // TODO
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // TODO
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture(args[0]));
        StdOut.printf("Width of the picture is %d\n", sc.width());
        StdOut.printf("Height of the picture is %d\n", sc.height());

        StdOut.println("Energy of the picture: ");
        for (int y = 0; y < sc.height(); y++) {
            for (int x = 0; x < sc.width(); x++) {
                StdOut.printf("%.2f", sc.energy(x, y));
                if (x == sc.width() - 1) {
                    StdOut.print("\n");
                }
                else {
                    StdOut.print(", ");
                }
            }
        }
    }

}
