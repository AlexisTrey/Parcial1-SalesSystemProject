package co.edu.uptc.model.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DoubleList<T> {

    @AllArgsConstructor
    @NoArgsConstructor
    private class Node {
        T value;
        Node next;
        Node previous;
    }

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    public void addFirst(T value) {
        Node newNode = new Node(value, head, null);
        if (isEmpty()) {
            tail = newNode;
        } else {
            head.previous = newNode;
        }
        head = newNode;
        size++;
    }

    public void addLast(T value) {
        Node newNode = new Node(value, null, tail);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) return null;
        T value = head.value;
        head = head.next;
        if (head != null) head.previous = null;
        else tail = null;
        size--;
        return value;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        T value = tail.value;
        tail = tail.previous;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return value;
    }

    public T getFirst() {
        return (head != null) ? head.value : null;
    }

    public boolean removeWhere(Predicate<T> predicate) {
        Node current = head;
        while (current != null) {
            if (predicate.test(current.value)) {
                unlinkNode(current);
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public T findWhere(Predicate<T> predicate) {
        Node current = head;
        while (current != null) {
            if (predicate.test(current.value)) return current.value;
            current = current.next;
        }
        return null;
    }

    private void unlinkNode(Node node) {
        if (node.previous != null) node.previous.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.previous = node.previous;
        else tail = node.previous;
    }

    public List<T> getAllList() {
        List<T> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.value);
            current = current.next;
        }
        return result;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }
}
