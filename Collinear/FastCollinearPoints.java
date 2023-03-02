import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] segments;
    private Point[][] segmentEndpoints;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is null");
        }

        this.numberOfSegments = 0;
        this.segments = new LineSegment[this.numberOfSegments];
        this.segmentEndpoints = new Point[0][0];

        Arrays.sort(points, Point::compareTo);
        for (int p = 0; p < points.length - 3; p++) {
            if (points[p] == null) {
                throw new IllegalArgumentException("Points array contains null point");
            }
            findSegments(points, p, points[p]);
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
    private void findSegments(Point[] points, int position, Point origin) {
        Point[] sortedPoints = new Point[points.length - position];
        System.arraycopy(points, position, sortedPoints, 0, sortedPoints.length);
        Arrays.sort(sortedPoints, origin.slopeOrder());

        int collinearCount = 0;

        Point segmentEnd = origin;
        for (int p = 1; p < sortedPoints.length; p++) {
            if (origin.slopeTo(sortedPoints[p]) == origin.slopeTo(sortedPoints[p - 1])) {
                collinearCount++;
                segmentEnd = sortedPoints[p];
            }
            else {
                if (collinearCount > 1) {
                    addSegment(origin, segmentEnd);
                }
                collinearCount = 0;
            }
        }
        if (collinearCount > 1) {
            addSegment(origin, segmentEnd);
        }
    }


    private void addSegment(Point p1, Point p2) {
        for (Point[] segmentEndpoint : segmentEndpoints) {
            if (p1.compareTo(segmentEndpoint[0]) >= 0
                    && p2.compareTo(segmentEndpoint[1]) == 0) return;
        }

        LineSegment[] newSegments = new LineSegment[++this.numberOfSegments];
        System.arraycopy(segments, 0, newSegments, 0, segments.length);
        newSegments[newSegments.length - 1] = new LineSegment(p1, p2);
        this.segments = newSegments;

        Point[][] newSegmentEndpoints = new Point[segmentEndpoints.length + 1][2];
        System.arraycopy(segmentEndpoints, 0, newSegmentEndpoints, 0,
                         segmentEndpoints.length);
        newSegmentEndpoints[newSegmentEndpoints.length - 1] = new Point[] { p1, p2 };
        this.segmentEndpoints = newSegmentEndpoints;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
