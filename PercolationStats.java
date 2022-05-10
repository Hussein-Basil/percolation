import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("out of bound");
        }
        this.trials = trials;
        this.thresholds = new double[trials];
        generateThresholds(n);
    }

    private void generateThresholds(int n) {
        int row;
        int col;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            this.thresholds[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n;
        int trials;
        PercolationStats percolationStats;

        n = Integer.parseInt(args[0]);
        trials = Integer.parseInt(args[1]);
        percolationStats = new PercolationStats(n, trials);

        System.out.println(String.format("%-23s = %.6f", "mean", percolationStats.mean()));
        System.out.println(String.format("%-23s = %.17f", "stddev", percolationStats.stddev()));
        System.out.println(
                String.format("%-23s = [%.16f, %.16f]", "95% confidence interval", percolationStats.confidenceLo(),
                        percolationStats.confidenceHi()));
    }
}
