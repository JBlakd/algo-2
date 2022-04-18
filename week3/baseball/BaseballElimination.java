/* *****************************************************************************
 *  Name: Ivan Hu
 *  Date: 17/04/2022
 *  Description: Algos 2 Baseball Elimination assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedHashMap;

public class BaseballElimination {
    private int n;
    // LinkedHashMap to preserve order between indices and list returned by teams()
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

    private int teamIndexToFlowNetworkIndex(int teamIndex, int subjectTeamIndex, int triangle) {
        if (teamIndex < subjectTeamIndex) {
            return 1 + triangle + teamIndex;
        }
        else if (teamIndex > subjectTeamIndex) {
            return triangle + teamIndex;
        }
        else {
            throw new RuntimeException("teamIndex provided equals to subjectTeamIndex");
        }
    }

    private FlowNetwork createBaseballFlowNetwork(Iterable<String> teams, String subjectTeam) {
        // Vertices in order
        // 1 source, triangle(numTeam), numTeam, 1 sink
        int triangle = ((n - 2) * (((n - 2) + 1) / 2));
        // StdOut.printf("Triangle: %d\n", triangle);
        int numVertices = 2 + triangle + (n - 1);
        int subjectTeamIndex = teamsIndexMap.get(subjectTeam);

        // Constructing unconnected FlowNetwork with only vertices
        FlowNetwork retVal = new FlowNetwork(numVertices);

        // Making the source to versus vertices
        int versusEdgeIndex = 1;
        for (String team : teams) {
            // Skip over the subject team
            if (team.equals(subjectTeam)) {
                continue;
            }

            int teamIndex = teamsIndexMap.get(team);
            for (int j = teamIndex + 1; j < n; j++) {
                // Skip over the subject team
                if (j == subjectTeamIndex) {
                    continue;
                }
                retVal.addEdge(new FlowEdge(0, versusEdgeIndex, g[teamIndex][j]));
                StdOut.printf("Edge (0, %d, %d) added corresponding to Team %d vs Team %d.\n",
                              versusEdgeIndex, g[teamIndex][j], teamIndex,
                              j);

                // Might as well add the edges from versus vertices to individual team vertices while we're here
                retVal.addEdge(new FlowEdge(versusEdgeIndex,
                                            teamIndexToFlowNetworkIndex(teamIndex, subjectTeamIndex,
                                                                        triangle),
                                            Integer.MAX_VALUE));
                retVal.addEdge(new FlowEdge(versusEdgeIndex,
                                            teamIndexToFlowNetworkIndex(j, subjectTeamIndex,
                                                                        triangle),
                                            Integer.MAX_VALUE));

                versusEdgeIndex++;
            }
        }

        StdOut.printf("%d edges added. Correct? %b\n", versusEdgeIndex - 1,
                      ((versusEdgeIndex - 1) == triangle));

        // Add edges from individual team vertices to sink
        for (String team : teams) {
            // Skip over the subject team
            if (team.equals(subjectTeam)) {
                continue;
            }

            int teamIndex = teamsIndexMap.get(team);
            int allowedWins = w[subjectTeamIndex] + r[subjectTeamIndex] - w[teamIndex];

            retVal.addEdge(
                    new FlowEdge(teamIndexToFlowNetworkIndex(teamIndex, subjectTeamIndex, triangle),
                                 retVal.V() - 1, allowedWins));
        }

        StdOut.printf("%d edges in total. Expected %d.", retVal.E(), 3 * triangle + (n - 1));

        return retVal;
    }

    // private List<String> otherTeams(Iterable<String> teams, String currentTeam) {
    //     List<String> otherTeams = new ArrayList<String>();
    //     for (String t : teams) {
    //         if (t.equals(currentTeam)) {
    //             continue;
    //         }
    //
    //         otherTeams.add(t);
    //     }
    //
    //     return otherTeams;
    // }

    // is given team eliminated?
    public boolean isEliminated(String team) {


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

        FlowNetwork fn = be.createBaseballFlowNetwork(be.teams(), args[1]);

        // String currentTeam = args[1];
        // List<String> otherTeams = be.otherTeams(be.teams(), currentTeam);
        // StdOut.printf("\nTeams other than %s:\n", currentTeam);
        // for (String otherTeam : otherTeams) {
        //     StdOut.printf("%20s: ", otherTeam);
        // }
    }
}
