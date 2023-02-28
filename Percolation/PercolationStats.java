import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private double[] trialResults;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Grid size and number of trials must be positive");
        this.trialResults = new double[trials];
        runTrial(n, trials, this.trialResults);
    }

    private static void runTrial(int n, int trials, double[] trialResults) {
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            // while system does not percolate
            while (!percolation.percolates()) {
                // pick random index to open
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                percolation.open(row, col);
            }

            trialResults[i] = (double) percolation.numberOfOpenSites() / (
                    n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialResults.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialResults.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.printf("mean = %.10f%n", percolationStats.mean());
        StdOut.printf("stddev = %.10f%n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = [%.10f, %.10f]%n",
                      percolationStats.confidenceLo(),
                      percolationStats.confidenceHi());
    }
}