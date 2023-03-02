import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is null");
        }

        this.numberOfSegments = 0;
        this.segments = new LineSegment[this.numberOfSegments];

        Arrays.sort(points, Point::compareTo);

        for (int p = 0; p < points.length - 3; p++) {
            if (points[p] == null) {
                throw new IllegalArgumentException("Points array contains null point");
            }
            for (int q = p + 1; q < points.length - 2; q++) {
                if (points[q] == null) {
                    throw new IllegalArgumentException("Points array contains null point");
                }
                for (int r = q + 1; r < points.length - 1; r++) {
                    if (points[r] == null) {
                        throw new IllegalArgumentException("Points array contains null point");
                    }
                    for (int s = r + 1; s < points.length; s++) {
                        if (points[s] == null) {
                            throw new IllegalArgumentException("Points array contains null point");
                        }
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r])
                                && points[p].slopeTo(points[q]) == points[p].slopeTo(points[s])) {
                            addSegment(points[p], points[s]);
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, numberOfSegments());
    }

    private void addSegment(Point p1, Point p2) {
        Point segBegin, segEnd;
        if (p1.compareTo(p2) <= 0) {
            segBegin = p1;
            segEnd = p2;
        }
        else {
            segBegin = p2;
            segEnd = p1;
        }
        LineSegment newSegment = new LineSegment(segBegin, segEnd);

        for (LineSegment segment : segments) {
            if (segment.equals(newSegment)) return;
        }

        LineSegment[] newSegments = new LineSegment[++this.numberOfSegments];

        System.arraycopy(segments, 0, newSegments, 0, segments.length);
        newSegments[newSegments.length - 1] = newSegment;
        this.segments = newSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();


        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}