import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// we want a linked list for this one because we do not need
// random access and we must support all operations in constant time
// not constant amortized time
public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node first;
    private Node last;

    // construct an empty deque
    public Deque() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot add element with null item to deque");
        Node newFirst = new Node(item);
        newFirst.next = first;
        if (first != null) first.prev = newFirst;
        first = newFirst;
        if (last == null) last = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot add element with null item to deque");
        Node newLast = new Node(item);
        if (last != null) {
            last.next = newLast;
            newLast.prev = last;
        }
        last = newLast;
        if (first == null) first = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");
        Node removedFirst = first;
        first = first.next;
        if (first == null) {
            last = null;
        }
        else {
            first.prev = null;
        }
        size--;
        return removedFirst.getItem();
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");
        Node removedLast = last;
        last = last.prev;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        size--;
        return removedLast.getItem();
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> intDeque = new Deque<>();

        for (int i = 0; i < 12; i++) {
            if (i % 2 == 0)
                intDeque.addFirst(i);
            else
                intDeque.addLast(i);
        }

        Iterator<Integer> intDequeIter = intDeque.iterator();

        while (intDequeIter.hasNext()) {
            StdOut.printf("%d%s", intDequeIter.next(), intDequeIter.hasNext() ? " " : "");
        }
        StdOut.println(intDeque.isEmpty());
        StdOut.println(intDeque.size());
        StdOut.println();

        for (int i = 0; i < 12; i++) {
            int removed;
            if (i % 2 == 0)
                removed = intDeque.removeFirst();
            else
                removed = intDeque.removeLast();
            StdOut.printf("removed - %d, remaining: %d%n", removed, intDeque.size());
        }
    }


    // 16 obj overhead + 8 ptr to encl + 3 inst ref (x 8) = 16 + 8 + 24 = 24 + 24 = 48
    private class Node {
        Item item;
        Node prev;
        Node next;

        Node(Item item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        Item getItem() {
            return item;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        Node getNext() {
            return next;
        }

        void setNext(Node next) {
            this.next = next;
        }
    }

    // 16 obj + 8 ptr to encl + 8 inst ref = 32
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No elements left in deque");
            Item item = current.getItem();
            current = current.next;
            return item;
        }
    }
}