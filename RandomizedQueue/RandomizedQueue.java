import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// we want an array for this one because we need random access
public class RandomizedQueue<Item> implements Iterable<Item> {
    private DynamicArray dynamicArray;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.dynamicArray = new DynamicArray();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return dynamicArray.getSize() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return dynamicArray.getSize();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot enqueue element with null value");
        dynamicArray.insert(item);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Cannot dequeue element from empty queue");
        return dynamicArray.remove(StdRandom.uniformInt(dynamicArray.getSize()));
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample element from empty queue");
        return dynamicArray.get(StdRandom.uniformInt(dynamicArray.getSize()));
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private DynamicArray getDynamicArray() {
        return dynamicArray;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 32; i++) {
            randomizedQueue.enqueue(StdRandom.uniformInt(1024));
        }
        StdOut.println(randomizedQueue.sample());
        StdOut.println(randomizedQueue.size());

        StdOut.println(randomizedQueue.dequeue());
        for (Integer integer : randomizedQueue) {
            StdOut.println(integer);
        }
        StdOut.println(randomizedQueue.isEmpty());
    }

    private class DynamicArray {
        int capacity;
        int size;
        Item[] elements;

        DynamicArray() {
            this.capacity = 1;
            this.size = 0;
            this.elements = (Item[]) new Object[this.capacity];
        }

        void resize(int newCapacity) {
            capacity = newCapacity;
            Item[] newElements = (Item[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        Item get(int index) {
            return elements[index];
        }

        void insert(Item item) {
            if (size + 1 == capacity) resize(capacity * 2);
            elements[size++] = item;
        }

        Item remove(int index) {
            Item item = elements[index];
            if (size < (capacity / 4)) resize(capacity / 2);
            for (int i = index; i < size; i++) {
                elements[i] = elements[i + 1];
            }
            size--;
            return item;
        }

        DynamicArray getCopy() {
            DynamicArray copy = new DynamicArray();
            copy.capacity = capacity;
            copy.size = size;
            Item[] copiedElements = (Item[]) new Object[capacity];
            for (int i = 0; i < capacity; i++) {
                copiedElements[i] = elements[i];
            }
            copy.elements = copiedElements;
            return copy;
        }

        public int getSize() {
            return size;
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int current = 0;
        private Item[] randomizedElements;

        RandomizedQueueIterator() {
            DynamicArray copy = getDynamicArray().getCopy();
            this.randomizedElements = (Item[]) new Object[size()];
            for (int i = 0; i < randomizedElements.length; i++) {
                randomizedElements[i] = copy.remove(StdRandom.uniformInt(copy.getSize()));
            }
        }

        public boolean hasNext() {
            return current < randomizedElements.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No elements left in randomized queue");
            return randomizedElements[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported");
        }
    }
}