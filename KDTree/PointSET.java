import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private SET<Point2D> pointSet;

    public PointSET()                               // construct an empty set of points
    {
        this.pointSet = new SET<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return this.pointSet.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("Cannot insert null point");
        this.pointSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Cannot check null point");
        return this.pointSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setXscale();
        StdDraw.setYscale();
        for (Point2D p : this.pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Cannot check null rectangle");
        SET<Point2D> range = new SET<>();
        double x, y;
        boolean inHorizontalBound, inVerticalBound;
        for (Point2D point : this.pointSet) {
            x = point.x();
            y = point.y();
            inHorizontalBound = x <= rect.xmax() && x >= rect.xmin();
            inVerticalBound = y <= rect.ymax() && y >= rect.ymin();
            if (inHorizontalBound && inVerticalBound) range.add(point);
        }
        return range;
    }


    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Cannot check null point");
        double curDistSquared, minDistSquared = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D point : this.pointSet) {
            curDistSquared = point.distanceSquaredTo(p);
            if (curDistSquared == Math.min(minDistSquared, curDistSquared)) {
                nearest = point;
                minDistSquared = curDistSquared;
            }
        }
        return nearest;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        In in = new In(args[0]);
        PointSET ps = new PointSET();
        while (in.hasNextLine()) {
            String line = in.readLine();
            if (!line.equals("")) {
                String[] tokens = line.split(" ");
                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);
                ps.insert(new Point2D(x, y));
            }
        }

        ps.draw();
    }
}

