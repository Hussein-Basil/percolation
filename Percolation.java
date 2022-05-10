import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;
    private final int bottom;
    private final int n;
    private final boolean[][] opened;
    private int openSites;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufBackwash;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 1);
        this.ufBackwash = new WeightedQuickUnionUF(n * n + 2);
        this.opened = new boolean[n][n];
        this.bottom = n * n + 1;
        this.openSites = 0;
    }

    // performs conversion from 2D to 1D index
    private int xyTo1D(int row, int col) {
        return (row - 1) * this.n + col;
    }

    private void validateIndex(int row, int col) {
        if (row <= 0 || row > this.n) {
            throw new IllegalArgumentException("row index out of bounds");
        }
        if (col <= 0 || col > this.n) {
            throw new IllegalArgumentException("col index out of bounds");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row, col);

        if (isOpen(row, col)) {
            return;
        }

        this.opened[row - 1][col - 1] = true;
        this.openSites++;

        int index = xyTo1D(row, col);

        // connect with source
        if (row == 1) {
            this.uf.union(TOP, index);
            this.ufBackwash.union(TOP, index);
        }

        // connect with target
        if (row == this.n) {
            this.ufBackwash.union(this.bottom, index);
        }

        // connect with TOP
        if (row > 1 && isOpen(row - 1, col)) {
            this.uf.union(index, xyTo1D(row - 1, col));
            this.ufBackwash.union(index, xyTo1D(row - 1, col));
        }

        // connect with bottom
        if (row < this.n && isOpen(row + 1, col)) {
            this.uf.union(index, xyTo1D(row + 1, col));
            this.ufBackwash.union(index, xyTo1D(row + 1, col));
        }

        // connect with left
        if (col > 1 && isOpen(row, col - 1)) {
            this.uf.union(index, xyTo1D(row, col - 1));
            this.ufBackwash.union(index, xyTo1D(row, col - 1));
        }

        // connect with right
        if (col < this.n && isOpen(row, col + 1)) {
            this.uf.union(index, xyTo1D(row, col + 1));
            this.ufBackwash.union(index, xyTo1D(row, col + 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndex(row, col);
        return this.opened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && this.uf.find(TOP) == this.uf.find(xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.ufBackwash.find(TOP) == this.ufBackwash.find(this.bottom);
    }
}
