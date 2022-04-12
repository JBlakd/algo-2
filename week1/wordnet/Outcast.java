/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 12/04/2022
 *  Description: Algos 2 assignment wordnet
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException();
        }

        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException();
        }

        int distanceSumChampionSum = -1;
        String distanceSumChampionNoun = "NO_CHAMPION_YET";

        for (int i = 0; i < nouns.length; i++) {
            int curDistanceSum = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) {
                    continue;
                }

                curDistanceSum += wordNet.distance(nouns[i], nouns[j]);
            }

            if (curDistanceSum > distanceSumChampionSum) {
                distanceSumChampionSum = curDistanceSum;
                distanceSumChampionNoun = nouns[i];
            }
        }

        return distanceSumChampionNoun;
    }

    // see test client below
    public static void main(String[] args) {
        Outcast outcast = new Outcast(new WordNet(args[0], args[1]));

        for (int i = 2; i < args.length; i++) {
            StdOut.printf("%s: ", args[i]);

            In in = new In(args[i]);
            String[] stringArr = in.readAllStrings();
            StdOut.println(outcast.outcast(stringArr));
        }
    }
}
