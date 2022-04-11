/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 11/04/2022
 *  Description: Algos 2 assignment wordnet
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class SAP {

    private Digraph G;
    private Queue<CachedExploration> cache = new Queue<CachedExploration>();

    private static class CachedExploration {
        private int v;
        private int w;
        private int ancestor;
        private int length;

        private CachedExploration(int v, int w, int ancestor, int length) {
            this.v = v;
            this.w = w;
            this.ancestor = ancestor;
            this.length = length;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    private int twoNodesBFS(int v, int w, boolean isReturnAncestor) {
        boolean[] vMarked = new boolean[G.V()];
        boolean[] wMarked = new boolean[G.V()];

        // int[] vEdgeTo = new int[G.V()];
        // Arrays.fill(vEdgeTo, -1);
        // int[] wEdgeTo = new int[G.V()];
        // Arrays.fill(wEdgeTo, -1);

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

        // Start lockstep BFS loop
        while (!vQ.isEmpty() && !wQ.isEmpty()) {
            // Perform action on current nodes only
            int curV = vQ.dequeue();
            int curW = wQ.dequeue();

            for (int vAdj : G.adj(curV)) {
                if (!vMarked[vAdj]) {
                    vQ.enqueue(vAdj);
                    vMarked[vAdj] = true;
                    // vEdgeTo[vAdj] = curV;
                    vDistTo[vAdj]++;
                }
            }
            for (int wAdj : G.adj(curW)) {
                if (!wMarked[wAdj]) {
                    wMarked[wAdj] = true;
                    wQ.enqueue(wAdj);
                    // wEdgeTo[wAdj] = curV;
                    wDistTo[wAdj]++;
                }
            }

            // Actions on current nodes complete, now to check if common ancestor found
            for (int i = 0; i < vMarked.length; i++) {
                if (!vMarked[i] || !wMarked[i]) {
                    continue;
                }

                if (vMarked[i] && wMarked[i]) {
                    // Cache
                    if (cache.size() > 5) {
                        cache.dequeue();
                    }

                    int distance = vDistTo[i] + wDistTo[i];
                    cache.enqueue(new CachedExploration(v, w, i, distance));

                    return isReturnAncestor ? i : distance;
                }
            }
        }

        return -1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // Check if result already cached before performing calculation
        for (CachedExploration cachedExploration : cache) {
            if (cachedExploration.v == v && cachedExploration.w == w) {
                // StdOut.printf("(%d, %d) cache hit.\n", v, w);
                return cachedExploration.length;
            }
        }

        return twoNodesBFS(v, w, false);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // Check if result already cached before performing calculation
        for (CachedExploration cachedExploration : cache) {
            if (cachedExploration.v == v && cachedExploration.w == w) {
                // StdOut.printf("(%d, %d) cache hit.\n", v, w);
                return cachedExploration.ancestor;
            }
        }

        return twoNodesBFS(v, w, true);
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
        SAP mySAP = new SAP(new Digraph(in));

        // Ancestor and Length should be 1 and 43 respectively
        int v1 = 7;
        int w1 = 9;

        int v2 = 12;
        int w2 = 8;

        Stopwatch stopwatch = new Stopwatch();
        StdOut.printf("Common ancestor of %d and %d is %d\n", v1, w1, mySAP.ancestor(v1, w1));
        StdOut.printf("Took %f seconds.\n\n", stopwatch.elapsedTime());
        stopwatch = new Stopwatch();
        StdOut.printf("Common ancestor of %d and %d is %d\n", v2, w2, mySAP.ancestor(v2, w2));
        StdOut.printf("Took %f seconds.\n\n", stopwatch.elapsedTime());
        stopwatch = new Stopwatch();
        StdOut.printf("Length between %d and %d is %d\n", v1, w1, mySAP.length(v1, w1));
        StdOut.printf("Took %f seconds.\n\n", stopwatch.elapsedTime());
        stopwatch = new Stopwatch();
        StdOut.printf("Common ancestor of %d and %d is %d\n", v1, w1, mySAP.ancestor(v1, w1));
        StdOut.printf("Took %f seconds.\n\n", stopwatch.elapsedTime());

    }
}
