/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 11/04/2022
 *  Description: Algos 2 assignment wordnet
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // TODO
        return 0;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        boolean[] vMarked = new boolean[G.V()];
        boolean[] wMarked = new boolean[G.V()];

        int[] vEdgeTo = new int[G.V()];
        Arrays.fill(vEdgeTo, -1);
        int[] wEdgeTo = new int[G.V()];
        Arrays.fill(wEdgeTo, -1);

        int[] vDistTo = new int[G.V()];
        int[] wDistTo = new int[G.V()];

        Queue<Integer> vQ = new Queue<Integer>();
        Queue<Integer> wQ = new Queue<Integer>();

        // Initial state
        vQ.enqueue(v);
        wQ.enqueue(w);
        vMarked[v] = true;
        wMarked[w] = true;
        vDistTo[v] = 0;
        wDistTo[w] = 0;

        // Start lockstep loop
        while (true) {
            // Lockstep BFS
            int curV = vQ.dequeue();
            int curW = wQ.dequeue();

            for (int vAdj : G.adj(curV)) {
                if (!vMarked[vAdj]) {
                    vQ.enqueue(vAdj);
                    vMarked[vAdj] = true;
                    vEdgeTo[vAdj] = curV;
                    vDistTo[vAdj]++;
                }
            }
            for (int wAdj : G.adj(curW)) {
                if (!wMarked[wAdj]) {
                    wMarked[wAdj] = true;
                    wQ.enqueue(wAdj);
                    wEdgeTo[wAdj] = curV;
                    wDistTo[wAdj]++;
                }
            }

            // Lockstep BFS complete, now to check if common ancestor found
            // TODO
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // TODO
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // TODO
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        Digraph myDigraph = new Digraph(in);

        StdOut.print("Adj list of 10: ");
        for (int adj : myDigraph.adj(10)) {
            StdOut.print(adj);
        }
        StdOut.println();
        StdOut.print("Adj list of 5: ");
        for (int adj : myDigraph.adj(5)) {
            StdOut.print(adj);
        }
    }
}
