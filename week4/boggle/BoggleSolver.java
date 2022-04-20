/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 20/04/2022
 *  Description: Algos 2 Assignment boggle
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BoggleSolver {
    private BoggleTrie dictTrie;
    private BoggleTrie foundTrie;
    private ArrayList<String> boggleWordList;
    private int numRow;
    private int numCol;
    private int[] board1D;
    // private boolean[] marked;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictTrie = new BoggleTrie();
        foundTrie = new BoggleTrie();

        for (String word : dictionary) {
            dictTrie.put(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        numRow = board.rows();
        numCol = board.cols();
        board1D = new int[numRow * numCol];
        boggleWordList = new ArrayList<>();

        int idx = 0;
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                board1D[idx] = board.getLetter(i, j);
                idx++;
            }
        }

        for (int i = 0; i < board1D.length; i++) {
            boolean[] marked = new boolean[numRow * numCol];
            dfs(i, "", marked);
        }

        return boggleWordList;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return dictTrie.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private int[] adj(int index) {
        int len = numRow * numCol;

        // Top row
        if (index < numCol) {
            // Top left corner
            if (index == 0) {
                return new int[] { 1, numCol, numCol + 1 };
            }

            // Top right corner
            if (index == numCol - 1) {
                // (numCol - 1) is the top right corner
                return new int[] { numCol - 2, numCol - 1 + numCol, numCol - 2 + numCol };
            }

            // Top row non-corner
            return new int[] {
                    index - 1, index + 1, index - 1 + numCol, index + numCol, index + 1 + numCol
            };
        }

        // Bottom row
        if (index >= (len - numCol)) {
            // Bottom left corner
            if (index == len - numCol) {
                return new int[] { index - numCol, index - numCol + 1, index + 1 };
            }

            // Bottom right corner
            if (index == len - 1) {
                return new int[] { index - 1, index - numCol, index - numCol - 1 };
            }

            // Bottom row non-corner
            return new int[] {
                    index - 1, index + 1, index - numCol, index - numCol - 1, index - numCol + 1
            };
        }

        // Left col non-corner
        if (index % numCol == 0) {
            return new int[] {
                    index - numCol, index + numCol, index + 1, index + 1 - numCol,
                    index + 1 + numCol
            };
        }

        // Right col non-corner
        if (index % numCol == (numCol - 1)) {
            return new int[] {
                    index - numCol, index + numCol, index - 1, index - 1 - numCol,
                    index - 1 + numCol
            };
        }

        // Non-edge
        return new int[] {
                index - numCol, index + numCol, index - 1, index + 1,
                index - 1 - numCol, index - 1 + numCol,
                index + 1 - numCol, index + 1 + numCol
        };
    }

    private void dfs(int index, String sbString, boolean[] marked) {
        marked[index] = true;
        sbString += (char) board1D[index];

        // Stop DFS is no possible prefix in dictionary
        if (dictTrie.get(sbString) == -1) {
            return;
        }

        // Add to list if is a valid dictionary word and foundTrie doesn't already contain the word
        if (dictTrie.get(sbString) > 0 && foundTrie.get(sbString) <= 0) {
            boggleWordList.add(sbString);
            foundTrie.put(sbString);
        }

        for (int neighbour : adj(index)) {
            if (!marked[neighbour]) {
                dfs(neighbour, sbString, Arrays.copyOf(marked, marked.length));
                // dfs(neighbour, sbString, marked);
            }
        }
    }

    private class BoggleTrie {
        // 26 upper case english characters
        private static final int R = 26;
        private Node root = new Node();

        private BoggleTrie() {

        }

        private class Node {
            private int value = 0;
            private Node[] next = new Node[R];
        }

        private void put(String key) {
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
            // if Node doesn't exist, return -1
            // If a node exists but no value assigned to it
            // i.e. not the endpoint of a put() but on the way there, return 0
            if (x == null) {
                return -1;
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
