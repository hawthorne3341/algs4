public class Percolation {
    private int n;

    private int numberOpen;

    private boolean[][] sites;

    private WeightedQuickUnionUFWithMax system;

    private boolean percolates;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid must have positive dimensions");
        // keep record of grid dimensions for handling requests to out-of-bounds indices
        this.n = n;
        // empty n x n grid of closed sites
        this.sites = new boolean[n][n];
        // the data structure keeping track of whether the grid percolates
        // initializing with one more element than grid elements because object
        // 0 will be the canonical element of the filled set
        // since this is the second step of the constructor requiring n^2 operations,
        // our constructor takes time proportional to n^2

        // the system "flattens" the grid - to reach site[row][col], we access system[((row - 1)  * n) + col]
        this.system = new WeightedQuickUnionUFWithMax(n * n + 1);
        // the number of sites currently open - when the grid does percolate, the percolation
        // threshold for that iteration will be the number of open sites as a percentage of
        // the total number of sites
        this.numberOpen = 0;
        this.percolates = false;
    }

    private boolean validIndex(int row, int col) {
        return !((row < 1 || row > n) || (col < 1 || col > n));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validIndex(row, col))
            throw new IllegalArgumentException("Requested index outside grid");

        // if site is closed
        if (!isOpen(row, col)) {
            // open site
            sites[row - 1][col - 1] = true;
            // if row == 1, union with canonical element of filled set
            if (row == 1) system.union(col, 0);
            // increment numberOpen
            numberOpen++;
        }

        // check each neighbor
        // if neighbor is open && site/neighbor are not connected,
        // then union(site, neighbor)

        // left
        if (validIndex(row, col - 1) && isOpen(row, col - 1)) {
            // 2 calls to find
            system.union((row - 1) * n + col, (row - 1) * n + (col - 1));
        }

        // top
        if (validIndex(row - 1, col) && isOpen(row - 1, col)) {
            // 2 calls to find
            system.union((row - 1) * n + col, (row - 2) * n + col);
        }

        // right
        if (validIndex(row, col + 1) && isOpen(row, col + 1)) {
            // 2 calls to find
            system.union((row - 1) * n + col, (row - 1) * n + (col + 1));
        }

        // bottom
        if (validIndex(row + 1, col) && isOpen(row + 1, col)) {
            // 2 calls to find
            system.union((row - 1) * n + col, row * n + col);
        }

        // now that the site has been connected to its open neighbors, we check whether the
        // site is full, meaning whether it is connected to the canonical element of
        // the filled set

        // if that is the case, then we want to check whether the filled set now extends
        // to the bottom row, which we can determine by looking at the greatest element
        // in the filled set - if the filled set contains an element whose position
        // exceeds n * (n - 1), that means the filled set extends to the bottom row and
        // the system percolates
        if (isFull(row, col)) this.percolates = system.max((row - 1) * n + col) > n * (n - 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validIndex(row, col))
            throw new IllegalArgumentException("Requested index outside grid");
        return sites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validIndex(row, col))
            throw new IllegalArgumentException("Requested index outside grid");
        return system.find((row - 1) * n + col) == 0;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    private class WeightedQuickUnionUFWithMax {
        int[] parent;
        int[] max;
        int[] size;

        public WeightedQuickUnionUFWithMax(int n) {
            parent = new int[n];
            max = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                max[i] = i;
                size[i] = 1;
            }
        }

        public int find(int p) {
            while (p != parent[p])
                p = parent[p];
            return p;
        }

        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            // if one of our roots is the canonical root of the
            // filled set, it must remain the root of the tree
            // after union
            if (rootP == 0) {
                parent[rootQ] = rootP;
                size[rootP] += size[rootQ];
                // we always update the max value of the
                // new tree so that we can determine when we
                // have reached the bottom level
                max[rootP] = Math.max(max[rootP], max[rootQ]);
            }
            else if (rootQ == 0) {
                parent[rootP] = rootQ;
                size[rootQ] += size[rootP];
                max[rootQ] = Math.max(max[rootP], max[rootQ]);
            }
            // otherwise we make the smaller root point to the larger one
            else if (size[rootP] < size[rootQ]) {
                parent[rootP] = rootQ;
                size[rootQ] += size[rootP];
                max[rootQ] = Math.max(max[rootP], max[rootQ]);
            }
            else {
                parent[rootQ] = rootP;
                size[rootP] += size[rootQ];
                max[rootP] = Math.max(max[rootP], max[rootQ]);
            }
        }

        public int max(int p) {
            return max[find(p)];
        }
    }
}