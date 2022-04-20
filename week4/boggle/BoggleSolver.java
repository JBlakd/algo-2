/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 20/04/2022
 *  Description: Algos 2 Assignment boggle
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private BoggleTrie boggleTrie = new BoggleTrie();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            boggleTrie.put(word, boggleTrie.getPoints(word));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // TODO
        return null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return boggleTrie.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllLines();
        BoggleSolver boggleSolver = new BoggleSolver(dictionary);

        for (String word : dictionary) {
            StdOut.printf("%s is worth %d points\n", word, boggleSolver.scoreOf(word));
        }
    }

    private class BoggleTrie {
        // 26 upper case english characters
        private static final int R = 26;
        private Node root = new Node();

        private BoggleTrie() {

        }

        private class Node {
            private int value;
            private Node[] next = new Node[R];
        }

        private void put(String key, int val) {
            root = putHelper(root, key, getPoints(key), 0);
        }

        private Node putHelper(Node curNode, String key, int val, int d) {
            // null spot for a node found via recursion, create the node
            if (curNode == null) {
                curNode = new Node();
            }

            // We have recursively called put() enough times corresponding to the key string
            if (d == key.length()) {
                // Set the current node's value to the new value
                // If curNode already exists then it would be an overwriting operation
                curNode.value = val;
                return curNode;
            }

            char c = key.charAt(d);
            curNode.next[toIndex(c)] = putHelper(curNode.next[toIndex(c)], key, val, d + 1);
            return curNode;
        }

        public int get(String key) {
            Node x = getHelper(root, key, 0);
            if (x == null) {
                return 0;
            }
            return x.value;
        }

        private Node getHelper(Node curNode, String key, int d) {
            if (curNode == null) {
                return null;
            }

            if (d == key.length()) {
                return curNode;
            }

            char c = key.charAt(d);

            return getHelper(curNode.next[toIndex(c)], key, d + 1);
        }

        private int toIndex(char c) {
            return c - 65;
        }

        private int getPoints(String word) {
            int len = word.length();
            switch (len) {
                case 0:
                    return 0;
                case 1:
                    return 0;
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        }
    }
}
