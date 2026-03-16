package co.edu.uptc.model.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class DoubleList<T> {

    @AllArgsConstructor
    @NoArgsConstructor
    private class Node<T> {
        private T value;
        private Node<T> next;
        private Node<T> previous;
    }

    private Node<T> head = null;
    private Node<T> tail = null;

    private boolean addIfHeaderEmpty(T value) {
        if (head == null) {
            head = new Node<>(value, null, null);
            tail = head;
            return true;
        }
        return false;
    }

    public void addFirst(T value) {
        if (addIfHeaderEmpty(value))
            return;
        Node<T> newNode = new Node<>(value, head, null);
        head.previous = newNode;
        head = newNode;
    }

    public void addLast(T value) {
        if (addIfHeaderEmpty(value))
            return;
        Node<T> newNode = new Node<>(value, null, tail);
        tail.next = newNode;
        tail = newNode;
    }

    public List<T> getAllList() {
        Node<T> auxNode = head;
        List<T> auxList = new ArrayList<>();

        while (auxNode != null) {
            auxList.add(auxNode.value);
            auxNode = auxNode.next;
        }

        return auxList;
    }
}
