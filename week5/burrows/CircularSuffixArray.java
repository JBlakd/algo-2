/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 21/04/2022
 *  Description: Algs 2 Burrows-Wheeler assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private String original;
    // Array which will initially contain a[i] = i
    // After sorting it will contain the i-th circular shifted string in alphabetical order
    private int[] a;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        original = s;
        a = new int[original.length()];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }

        sort(a, 0, original.length() - 1, 0);
    }

    // length of s
    public int length() {
        return original.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return a[i];
    }

    // a: array to sort
    // lo: lower bound of the partition
    // hi: higher bound of the partition
    // d: the d-th character that partitioning will depend on
    private void sort(int[] arr, int lo, int hi, int d) {
        // base case
        if (hi <= lo) {
            return;
        }

        // lt is the lower bound of the future lower partition
        int lt = lo;
        // gt is the top bound of the future greater partition
        int gt = hi;

        // v is the character at index d of the lo-th circular rotated string
        int v = circCharAt(a[lo], d);
        // i is the incrementing index starting at lo
        int i = lo + 1;
        while (i <= gt) {
            int t = circCharAt(a[i], d);
            // if the iterating element is less than the partitioning element
            if (t < v) {
                // exchange lt and i, incrementing both
                int temp = a[lt];
                arr[lt] = arr[i];
                arr[i] = temp;
                lt++;
                i++;
            }
            // If the iterating element is greater than the partitioning element
            else if (t > v) {
                // exchange i and gt, decrementing gt
                int temp = arr[gt];
                arr[gt] = arr[i];
                arr[i] = temp;
                gt--;
            }
            else {
                i++;
            }
        }

        sort(arr, lo, lt - 1, d);
        // If index d of the lo-th circular rotated string is length()
        // if (d <= length()) {
        sort(arr, lt, gt, d + 1);
        // }
        sort(arr, gt + 1, hi, d);
    }

    // i implicitly represents the ith circular left shift string of original
    // index represents the charAt(index) of i
    // E.g. original = STRONG ...
    // ... circCharAt(2, 1) returns the 1-th character of the 2-th circular string (RONGST)
    // ... this would return O
    private char circCharAt(int i, int index) {
        return original.charAt((i + index) % original.length());
    }

    private String[] strRep() {
        String[] retVal = new String[a.length];
        Arrays.fill(retVal, "");
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < length(); j++) {
                retVal[i] += circCharAt(a[i], j);
            }
        }
        return retVal;
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        String[] strRep = csa.strRep();
        StdOut.println("j\t" + "            \t" + "i");
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i) + ":\t" + strRep[i] + "\t" + i);
        }
    }

}
