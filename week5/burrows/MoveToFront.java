/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 21/04/2022
 *  Description: Algs 2 Burrows-Wheeler assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> seq = new LinkedList<Character>();

        // Initialise the sequencer by making the i-th char in the sequencer
        // equal to the i-th extended ASCII char
        for (int i = 0; i <= 0xFF; i++) {
            seq.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // BinaryStdOut.write((int) c);
            for (int i = 0; i < seq.size(); i++) {
                if (seq.get(i) == c) {
                    // BinaryStdOut.write(i, 8);
                    StdOut.printf("%c", i);
                    seq.remove(i);
                    seq.addFirst(c);
                    break;
                }
            }
        }
        BinaryStdIn.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
