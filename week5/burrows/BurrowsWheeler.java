/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 21/04/2022
 *  Description: Algs 2 Burrows-Wheeler assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {

    private static char circCharAt(String buffer, int i, int index) {
        return buffer.charAt((i + index) % buffer.length());
    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {

        String buffer = BinaryStdIn.readString();
        BinaryStdIn.close();
        CircularSuffixArray csa = new CircularSuffixArray(buffer);
        StringBuilder transform = new StringBuilder();
        for (int i = 0; i < buffer.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i, 32);
            }
            transform.append(circCharAt(buffer, csa.index(i), buffer.length() - 1));
        }
        BinaryStdOut.write(transform.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        StringBuilder t = new StringBuilder();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            t.append(c);
        }
        BinaryStdIn.close();

        // Sort the t StringBuilder into the array next[]
        int N = t.length();
        char[] sorted = new char[N];
        int[] next = new int[N];
        int R = 256;
        int[] count = new int[R + 1];

        for (int i = 0; i < N; i++) {
            // For each index i in t, interpret t.charAt(i) as an index for count[]
            // Increment the next count[] index
            count[t.charAt(i) + 1]++;
        }

        for (int r = 0; r < R; r++) {
            // Cumulate count[]
            count[r + 1] += count[r];
        }

        // Move items into the output array next[]
        for (int i = 0; i < N; i++) {
            int nextIndex = count[t.charAt(i)]++;
            sorted[nextIndex] = t.charAt(i);
            next[nextIndex] = i;
            // StdOut.printf("%C %d\n", sorted[nextIndex], nextIndex);
        }

        StringBuilder decoded = new StringBuilder();
        int jumpyIndex = first;
        while (true) {
            decoded.append(sorted[jumpyIndex]);
            int temp = jumpyIndex;
            jumpyIndex = next[temp];

            if (jumpyIndex == first) {
                break;
            }
        }

        StdOut.print(decoded.toString());
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
