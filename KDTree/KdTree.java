import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {

    private Node root;
    private int size;

    private int dimension;
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;
    private static final int TOP = 0;
    private static final int RIGHT = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;

    public KdTree()                               // construct an empty set of points
    {
        // specification only requires support of 2 dimensions
        this.dimension = 2;
        this.size = 0;
        this.root = null;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return this.size;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("Cannot insert null point");
        root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
        this.size++;
    }

    private Node insert(Node root, Point2D p, int depth, RectHV current) {
        int orientation = depth % this.dimension;
        if (root == null) {
            RectHV rect;
            // set up rectangle
            switch (orientation) {
                case VERTICAL:
                    double xmin, xmax;
                    boolean rightOfCenter = (p.x() - current.xmin()) >= (current.width() / 2);
                    xmin = rightOfCenter ? p.x() : current.xmin();
                    xmax = rightOfCenter ? current.xmax() : p.x();
                    rect = new RectHV(xmin, current.ymin(), xmax, current.ymax());
                    return new Node(p, orientation, rect, rightOfCenter ? LEFT : RIGHT);
                case HORIZONTAL:
                    double ymin, ymax;
                    boolean aboveCenter = (p.y() - current.ymin()) >= (current.height() / 2);
                    ymin = aboveCenter ? p.y() : current.ymin();
                    ymax = aboveCenter ? current.ymax() : p.y();
                    rect = new RectHV(current.xmin(), ymin, current.xmax(), ymax);

                    return new Node(p, orientation, rect, aboveCenter ? BOTTOM : TOP);
                default:
                    throw new IllegalArgumentException("Unable to parse orientation");
            }

        }
        int cmp = comparePoints(p, root.p, orientation);
        RectHV subdivision;
        if (cmp < 0) {
            switch (orientation) {
                case VERTICAL:
                    subdivision = new RectHV(current.xmin(), current.ymin(), root.p.x(),
                                             current.ymax());
                    break;
                case HORIZONTAL:
                    subdivision = new RectHV(current.xmin(), current.ymin(), current.xmax(),
                                             root.p.y());
                    break;
                default:
                    throw new IllegalArgumentException("Unable to parse orientation");
            }
            root.lb = insert(root.lb, p, depth + 1, subdivision);
        }

        if (cmp >= 0) {
            switch (orientation) {
                case VERTICAL:
                    subdivision = new RectHV(root.p.x(), current.ymin(), current.xmax(),
                                             current.ymax());
                    break;
                case HORIZONTAL:
                    subdivision = new RectHV(current.xmin(), root.p.y(), current.xmax(),
                                             current.ymax());
                    break;
                default:
                    throw new IllegalArgumentException("Unable to parse orientation");
            }
            root.rt = insert(root.rt, p, depth + 1, subdivision);
        }

        return root;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Cannot check null point");
        return contains(root, p, 0) != null;
    }

    private Node contains(Node root, Point2D p, int depth) {
        if (root == null) return null;
        if (root.p.equals(p)) return root;
        int cmp = comparePoints(p, root.p, depth % this.dimension);
        if (cmp < 0) return contains(root.lb, p, depth + 1);
        else return contains(root.rt, p, depth + 1);
    }

    private int comparePoints(Point2D p1, Point2D p2, int orientation) {
        // if node level mod 2 == 0, look left or right of current point,
        // else look above or below
        return orientation == VERTICAL ? Double.compare(p1.x(), p2.x()) :
               Double.compare(p1.y(), p2.y());
    }

    public void draw()                         // draw all points to standard draw
    {
        Queue<Node> nodeQueue = new Queue<>();
        Node head;

        nodeQueue.enqueue(root);

        Color penColor;

        while (!nodeQueue.isEmpty()) {
            head = nodeQueue.dequeue();

            penColor = head.getOrientation() == VERTICAL ? Color.RED : Color.BLUE;
            StdDraw.setPenColor(penColor);

            head.draw();

            if (head.lb != null) nodeQueue.enqueue(head.lb);
            if (head.rt != null) nodeQueue.enqueue(head.rt);
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Cannot check null rectangle");
        SET<Point2D> pointsInRange = new SET<>();
        findPointsInRange(root, rect, pointsInRange, 0);
        return pointsInRange;
    }

    private void findPointsInRange(Node root, RectHV rect, SET<Point2D> rangeSet, int depth) {
        if (root == null) return;
        // if rect contains point, add to queue
        if (rect.contains(root.p)) rangeSet.add(root.p);

        int orientation = depth % this.dimension;
        switch (orientation) {
            case VERTICAL:
                if (root.p.x() > rect.xmax()) {
                    findPointsInRange(root.lb, rect, rangeSet, depth + 1);
                }
                else if (root.p.x() <= rect.xmin()) {
                    findPointsInRange(root.rt, rect, rangeSet, depth + 1);
                }
                else {
                    findPointsInRange(root.lb, rect, rangeSet, depth + 1);
                    findPointsInRange(root.rt, rect, rangeSet, depth + 1);
                }
                break;
            case HORIZONTAL:
                if (root.p.y() > rect.ymax()) {
                    findPointsInRange(root.lb, rect, rangeSet, depth + 1);
                }
                else if (root.p.y() <= rect.ymin()) {
                    findPointsInRange(root.rt, rect, rangeSet, depth + 1);
                }
                else {
                    findPointsInRange(root.lb, rect, rangeSet, depth + 1);
                    findPointsInRange(root.rt, rect, rangeSet, depth + 1);
                }
                break;
            default:
                throw new IllegalArgumentException("Unable to parse orientation");
        }

    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Cannot check null point");
        Node head = root;
        int depth = 0, orientation;
        double minDistSquared = Double.MAX_VALUE, curDistSquared;
        Point2D nearest = null;
        while (head != null) {
            curDistSquared = head.p.distanceSquaredTo(p);
            // if point is equivalent to p, return that point
            if (curDistSquared == 0) return head.p;
            if (curDistSquared == Math.min(minDistSquared, curDistSquared)) {
                nearest = head.p;
                minDistSquared = curDistSquared;
            }
            orientation = depth % this.dimension;
            switch (orientation) {
                case VERTICAL:
                    head = head.p.x() <= p.x() ? head.rt : head.lb;
                    break;
                case HORIZONTAL:
                    head = head.p.y() <= p.y() ? head.rt : head.lb;
                    break;
                default:
                    throw new IllegalArgumentException("Unable to parse orientation");
            }
            depth++;
        }
        return nearest;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
    }

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int orientation;
        private int line;

        public Node(Point2D p, int orientation, RectHV rect, int line) {
            this.p = p;
            this.rect = rect;
            this.orientation = orientation;
            this.line = line;
        }

        public void draw() {
            switch (this.line) {
                case TOP:
                    StdDraw.line(rect.xmin(), rect.ymax(), rect.xmax(), rect.ymax());
                    break;
                case RIGHT:
                    StdDraw.line(rect.xmax(), rect.ymax(), rect.xmax(), rect.ymin());
                    break;
                case BOTTOM:
                    StdDraw.line(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymin());
                    break;
                case LEFT:
                    StdDraw.line(rect.xmin(), rect.ymin(), rect.xmin(), rect.ymax());
                    break;
                default:
                    throw new IllegalArgumentException("Unable to parse line to draw");
            }
        }

        public int getOrientation() {
            return this.orientation;
        }
    }
}
