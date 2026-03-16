package co.edu.uptc.model.util;

import java.util.List;

public class Queue<T> {

    private final DoubleList<T> list = new DoubleList<>();

    public void enqueue(T value) {
        list.addLast(value);
    }

    public List<T> getAllElements() {
        return list.getAllList();
    }
}
