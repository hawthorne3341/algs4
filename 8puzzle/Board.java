import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {

    private int dimension;
    private int[][] tiles;
    private int[] blankCoordinates;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        this.tiles = new int[this.dimension][this.dimension];

        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (tiles[i][j] == 0) this.blankCoordinates = new int[] { i, j };
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        int n = dimension();
        StringBuilder str = new StringBuilder();
        str.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str.append(String.format("%2d", this.tiles[i][j])).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0, n = dimension(), currentValue;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentValue = this.tiles[i][j];
                if (currentValue > 0 && currentValue != (n * i) + j + 1) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int expectedRow, expectedCol, currentValue, manhattan = 0, n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentValue = this.tiles[i][j];
                if (currentValue < 1) continue;
                expectedRow = (currentValue - 1) / n;
                manhattan += Math.abs(i - expectedRow);
                expectedCol = (currentValue - 1) % n;
                manhattan += Math.abs(j - expectedCol);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return dimension == board.dimension && Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        Board neighbor;
        int tileValue, n = dimension();

        // tile moved down
        if (blankCoordinates[0] > 0) {
            neighbor = new Board(this.tiles);
            // swap non-blank tile with blank tile
            tileValue = neighbor.tiles[blankCoordinates[0] - 1][blankCoordinates[1]];
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1]] = tileValue;
            neighbor.tiles[blankCoordinates[0] - 1][blankCoordinates[1]] = 0;
            // update neighbor's coordinates of blank tile
            neighbor.blankCoordinates = new int[] { blankCoordinates[0] - 1, blankCoordinates[1] };
            neighbors.enqueue(neighbor);
        }

        // tile moved left
        if (blankCoordinates[1] < (n - 1)) {
            neighbor = new Board(this.tiles);
            tileValue = neighbor.tiles[blankCoordinates[0]][blankCoordinates[1] + 1];
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1]] = tileValue;
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1] + 1] = 0;
            neighbor.blankCoordinates = new int[] { blankCoordinates[0], blankCoordinates[1] + 1 };
            neighbors.enqueue(neighbor);
        }

        // tile moved up
        if (blankCoordinates[0] < (n - 1)) {
            neighbor = new Board(this.tiles);
            tileValue = neighbor.tiles[blankCoordinates[0] + 1][blankCoordinates[1]];
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1]] = tileValue;
            neighbor.tiles[blankCoordinates[0] + 1][blankCoordinates[1]] = 0;
            neighbor.blankCoordinates = new int[] { blankCoordinates[0] + 1, blankCoordinates[1] };
            neighbors.enqueue(neighbor);
        }

        // tile moved right
        if (blankCoordinates[1] > 0) {
            neighbor = new Board(this.tiles);
            tileValue = neighbor.tiles[blankCoordinates[0]][blankCoordinates[1] - 1];
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1]] = tileValue;
            neighbor.tiles[blankCoordinates[0]][blankCoordinates[1] - 1] = 0;
            neighbor.blankCoordinates = new int[] { blankCoordinates[0], blankCoordinates[1] - 1 };
            neighbors.enqueue(neighbor);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(this.tiles);
        int n = dimension();
        int[] firstTileCoordinates, secondTileCoordinates;

        firstTileCoordinates = secondTileCoordinates = null;

        // get first non-blank tile from first row
        for (int j = 0; j < n; j++) {
            if (twin.tiles[0][j] > 0) {
                firstTileCoordinates = new int[] { 0, j };
                break;
            }
        }

        // get first non-blank tile from last row
        for (int j = 0; j < n; j++) {
            if (twin.tiles[n - 1][j] > 0) {
                secondTileCoordinates = new int[] { n - 1, j };
                break;
            }
        }

        if (firstTileCoordinates == null || secondTileCoordinates == null)
            throw new IllegalArgumentException("Invalid board - entire row of blank tiles");

        // swap tiles
        int firstTileValue = twin.tiles[firstTileCoordinates[0]][firstTileCoordinates[1]];
        twin.tiles[firstTileCoordinates[0]][firstTileCoordinates[1]]
                = twin.tiles[secondTileCoordinates[0]][secondTileCoordinates[1]];
        twin.tiles[secondTileCoordinates[0]][secondTileCoordinates[1]] = firstTileValue;
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    }


}
