/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 15/04/2022
 *  Description: Algos 2 Seam Carving assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {

    // For finding vertical seam
    private static final int TOP_VIRTUAL_INDEX = 0;
    private static final int BOTTOM_VIRTUAL_INDEX = 1;
    // For finding horizontal seam
    private static final int LEFT_VIRTUAL_INDEX = 0;
    private static final int RIGHT_VIRTUAL_INDEX = 1;
    private static final int NUM_VIRTUAL_VERTICES = 2;

    // Picture picture;
    private int[][] pictureArr;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        width = picture.width();
        height = picture.height();

        pictureArr = new int[height][width];

        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++) {
                pictureArr[row][col] = picture.getRGB(col, row);
            }
        }
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
        Picture picture = new Picture(width, height);

        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++) {
                picture.setRGB(col, row, pictureArr[row][col]);
            }
        }

        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        // Avoid redundant calls to the get() method in Picture.
        // For example, to access the red, green, and blue components of a pixel,
        // call get() only once (and not three times).
        int rgbRight = pictureArr[y][x + 1];
        int redRight = (rgbRight >> 16) & 0xFF;
        int greenRight = (rgbRight >> 8) & 0xFF;
        int blueRight = rgbRight & 0xFF;
        int rgbLeft = pictureArr[y][x - 1];
        int redLeft = (rgbLeft >> 16) & 0xFF;
        int greenLeft = (rgbLeft >> 8) & 0xFF;
        int blueLeft = rgbLeft & 0xFF;
        int xSquared = (redRight - redLeft) * (redRight - redLeft) +
                (greenRight - greenLeft) * (greenRight - greenLeft) +
                (blueRight - blueLeft) * (blueRight - blueLeft);

        int rgbTop = pictureArr[y - 1][x];
        int redTop = (rgbTop >> 16) & 0xFF;
        int greenTop = (rgbTop >> 8) & 0xFF;
        int blueTop = rgbTop & 0xFF;
        int rgbBottom = pictureArr[y + 1][x];
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
        // When finding a seam, call energy() at most once per pixel.
        // For example, you can save the energies in a local variable energy[][]
        // and access the information directly from the 2D array (instead of recomputing from scratch).
        double[] energy = new double[height() * width() + NUM_VIRTUAL_VERTICES];

        // calculate energies
        energy[LEFT_VIRTUAL_INDEX] = 0;
        energy[RIGHT_VIRTUAL_INDEX] = 0;
        int i = NUM_VIRTUAL_VERTICES;
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
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
        distTo[LEFT_VIRTUAL_INDEX] = 0.0;

        // First do the LEFT_VIRTUAL_INDEX
        int[] leftCol = adjHorizontal(LEFT_VIRTUAL_INDEX);
        if (leftCol != null) {
            for (int w : leftCol) {
                relax(LEFT_VIRTUAL_INDEX, w, distTo, edgeTo, energy);
            }
        }
        // Calculate COLUMN MAJOR TRAVERSAL
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int v = toIndex(x, y);
                int[] neighbors = adjHorizontal(v);
                if (neighbors == null) {
                    continue;
                }

                for (int w : neighbors) {
                    relax(v, w, distTo, edgeTo, energy);
                }
            }
        }

        // Calculate ROW MAJOR TRAVERSAL
        // for (int v = 0; v < energy.length; v++) {
        //     int[] neighbors = adjHorizontal(v);
        //     if (neighbors == null) {
        //         continue;
        //     }
        //
        //     for (int w : neighbors) {
        //         relax(v, w, distTo, edgeTo, energy);
        //     }
        // }

        int[] seam = new int[width()];
        i = width() - 1;
        int curNode = RIGHT_VIRTUAL_INDEX;
        while (edgeTo[curNode] != LEFT_VIRTUAL_INDEX) {
            seam[i] = toRow(edgeTo[curNode]);
            curNode = edgeTo[curNode];
            i--;
        }

        return seam;
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
                // retVal[i] = i + NUM_VIRTUAL_VERTICES;
                retVal[i] = toIndex(i, 0);
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
            return new int[] { BOTTOM_VIRTUAL_INDEX };
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

    private int[] adjHorizontal(int index) {
        if (index == LEFT_VIRTUAL_INDEX) {
            // return array containing left col
            int[] retVal = new int[height()];
            for (int i = 0; i < height(); i++) {
                retVal[i] = toIndex(0, i);
            }
            return retVal;
        }

        if (index == RIGHT_VIRTUAL_INDEX) {
            return null;
        }

        int col = toCol(index);
        int row = toRow(index);

        // Right-most column
        if (col == width() - 1) {
            return new int[] { RIGHT_VIRTUAL_INDEX };
        }

        // Top-most row
        if (row == 0) {
            return new int[] { index + 1, index + width() + 1 };
        }

        // Bottom-most row
        if (row == height() - 1) {
            return new int[] { index + 1, index - width() + 1 };
        }

        // All other vertices
        return new int[] { index - width() + 1, index + 1, index + width() + 1 };
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

        int[] seam = new int[height()];
        i = height() - 1;
        int curNode = BOTTOM_VIRTUAL_INDEX;
        while (edgeTo[curNode] != TOP_VIRTUAL_INDEX) {
            seam[i] = toCol(edgeTo[curNode]);
            curNode = edgeTo[curNode];
            i--;
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height <= 1) {
            throw new IllegalArgumentException();
        }

        int[][] returnCopy = new int[height - 1][width];

        // Can't use System.arraycopy(). Gotta use arithmetic
        for (int col = 0; col < width; col++) {
            if (seam[col] < 0 || seam[col] > height) {
                throw new IllegalArgumentException();
            }

            if (col > 0) {
                if (Math.abs(seam[col] - seam[col - 1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }

            int origRow = 0;
            int copyRow = 0;
            while (origRow < height) {
                if (origRow == seam[col]) {
                    origRow++;
                    continue;
                }

                returnCopy[copyRow][col] = pictureArr[origRow][col];

                origRow++;
                copyRow++;
            }
        }

        height--;
        pictureArr = returnCopy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width <= 1) {
            throw new IllegalArgumentException();
        }

        int[][] returnCopy = new int[height][width - 1];

        for (int row = 0; row < height; row++) {
            if (seam[row] < 0 || seam[row] > width) {
                throw new IllegalArgumentException();
            }

            if (row > 0) {
                if (Math.abs(seam[row] - seam[row - 1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }

            // Gotta remove seam[row]
            System.arraycopy(pictureArr[row], 0, returnCopy[row], 0, seam[row]);
            System.arraycopy(pictureArr[row], seam[row] + 1, returnCopy[row], seam[row],
                             pictureArr[row].length - seam[row] - 1);
        }

        width--;
        pictureArr = returnCopy;
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
