package co.edu.uptc.model.util;

import java.util.List;

public class Stack<T> {

    private final DoubleList<T> list = new DoubleList<>();

    public void push(T value) {
        list.addFirst(value);
    }

    public List<T> getAllElements() {
        return list.getAllList();
    }
}
