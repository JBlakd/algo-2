/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 17/04/2022
 *  Description: Algos 2 Baseball Elimination assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedHashMap;

public class BaseballElimination {
    private int n;
    // private String[] teams;
    private LinkedHashMap<String, Integer> teamsIndexMap;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }

        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        // teams = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        teamsIndexMap = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < n; i++) {
            // teams[i] = in.readString();
            teamsIndexMap.put(in.readString(), i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teamsIndexMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        return w[teamsIndexMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        return l[teamsIndexMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        return r[teamsIndexMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException();
        }

        return g[teamsIndexMap.get(team1)][teamsIndexMap.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        // TODO
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination be = new BaseballElimination(args[0]);

        for (String team : be.teams()) {
            StdOut.printf("Team %15s: wins %d losses %d remaining %d \t", team, be.wins(team),
                          be.losses(team), be.remaining(team));

            for (String againstTeam : be.teams()) {
                StdOut.printf("%d ", be.against(team, againstTeam));
            }

            StdOut.println();
        }

    }
}
