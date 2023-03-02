/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {


    private SearchNode solvedNode;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null argument to constructor");
        Board twin = initial.twin();

        MinPQ<SearchNode> searchQueue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();

        SearchNode initialNode = new SearchNode(null, initial, 0);
        searchQueue.insert(initialNode);

        SearchNode twinNode = new SearchNode(null, twin, 0);
        twinQueue.insert(twinNode);

        SearchNode min, twinMin;
        Iterable<Board> minNeighbors, twinMinNeighbors;

        while (!(searchQueue.min().getManhattan() == 0 || twinQueue.min().getManhattan() == 0)) {
            min = searchQueue.min();
            twinMin = twinQueue.min();

            searchQueue.delMin();
            twinQueue.delMin();

            minNeighbors = min.board.neighbors();
            twinMinNeighbors = twinMin.board.neighbors();

            for (Board minNeighbor : minNeighbors) {
                if (min.previous != null && minNeighbor.equals(min.previous.board)) continue;
                searchQueue.insert(new SearchNode(min, minNeighbor, min.moves + 1));
            }

            for (Board twinMinNeighbor : twinMinNeighbors) {
                if (twinMin.previous != null && twinMinNeighbor.equals(twinMin.previous.board))
                    continue;
                twinQueue.insert(new SearchNode(twinMin, twinMinNeighbor, twinMin.moves + 1));
            }
        }

        if (searchQueue.min().getManhattan() == 0) {
            this.solvedNode = searchQueue.min();
            this.solvable = true;
        }
        else {
            this.solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solvedNode.moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solutionSteps = new Stack<>();
        SearchNode step = solvedNode;
        while (step != null) {
            solutionSteps.push(step.board);
            step = step.previous;
        }
        return solutionSteps;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode previous;
        private Board board;
        private int manhattan;
        private int moves;
        private int priority;

        public SearchNode(SearchNode previous, Board board, int moves) {
            this.previous = previous;
            this.board = board;
            this.manhattan = board.manhattan();
            this.moves = moves;
            this.priority = this.manhattan + board.hamming() + this.moves;
        }

        public int getManhattan() {
            return this.manhattan;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }
}

