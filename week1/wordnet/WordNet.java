/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 12/04/2022
 *  Description: Algos 2 assignment wordnet
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private HashMap<String, ArrayList<Integer>> wordIndexMap;
    private In in;
    private SAP sap;
    private ArrayList<String> wordArray;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        wordIndexMap = new HashMap<String, ArrayList<Integer>>();
        wordArray = new ArrayList<String>();

        // Read all synsets, use the string as a key to obtain the line number of the word
        in = new In(synsets);
        int lineCount = synsetToMap();

        // Read all hypernyms and construct a Digraph using it. numVertices is equal to lineCount.
        In hypernymIn = new In(hypernyms);
        sap = new SAP(hypernymToDigraph(hypernymIn, lineCount));
    }

    private Digraph hypernymToDigraph(In hypernymIn, int numVertices) {
        Digraph digraph = new Digraph(numVertices);

        while (hypernymIn.hasNextLine()) {
            String[] line = hypernymIn.readLine().split(",");
            if (line.length < 2) {
                continue;
            }

            // line[0] is the ORIGIN, all other parameters are DESTINATIONS
            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }
        }
        hypernymIn.close();

        return digraph;
    }

    private int synsetToMap() {
        if (wordIndexMap == null) {
            throw new IllegalArgumentException("Map has not been initialized.");
        }

        // In in = new In(synsets);
        int lineCount = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            if (line.length < 2) {
                throw new IllegalArgumentException(
                        "A line in the synset file is not formatted properly.");
            }

            // Add to wordArray
            wordArray.add(line[1]);

            // Add to map
            for (String word : line[1].split(" ")) {
                if (!wordIndexMap.containsKey(word)) {
                    ArrayList<Integer> newList = new ArrayList<Integer>();
                    wordIndexMap.put(word, newList);
                    newList.add(Integer.parseInt(line[0]));
                }
                else {
                    wordIndexMap.get(word).add(Integer.parseInt(line[0]));
                }
            }
            lineCount++;
        }
        in.close();

        return lineCount;
    }

    // Do I need this function?
    // private String indexToWord(int index) {
    //     // Index is always 1 less than the line number
    //
    // }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordIndexMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordIndexMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(wordIndexMap.get(nounA), wordIndexMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int sapIndex = sap.ancestor(wordIndexMap.get(nounA), wordIndexMap.get(nounB));
        // StdOut.printf("From WordNet.sap(): The ancestor of %s and %s is at index %d.\n", nounA,
        //               nounB,
        //               sapIndex);
        return wordArray.get(sapIndex);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);

        String containedWord = "Immaculate_Conception";
        String uncontainedWord = "Edmund_Blackadder";

        StdOut.printf("Does the WordNet contain \"%s\"? %b\n", containedWord,
                      wordNet.isNoun(containedWord));
        StdOut.printf("Does the WordNet contain \"%s\"? %b\n", uncontainedWord,
                      wordNet.isNoun(uncontainedWord));

        StdOut.printf("WordNet has %d distinct nouns.\n", wordNet.wordIndexMap.keySet().size());

        String nounA = args[2];
        String nounB = args[3];
        StdOut.printf("The ancestor of %s and %s is \"%s\". ", nounA, nounB,
                      wordNet.sap(nounA, nounB));
        StdOut.printf("The length between %s and %s is %d.\n", nounA, nounB,
                      wordNet.distance(nounA, nounB));

    }
}
