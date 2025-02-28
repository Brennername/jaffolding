package com.example.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A reactive state container that triggers re-renders when the state changes.
 * @param <T> The type of the state value
 */
public class State<T> {
    private T value;
    private List<Consumer<T>> listeners = new ArrayList<>();
    
    public State(T initialValue) {
        this.value = initialValue;
    }
    
    public T get() {
        return value;
    }
    
    public void set(T newValue) {
        if (value == newValue || (value != null && value.equals(newValue))) {
            return; // No change
        }
        
        this.value = newValue;
        notifyListeners();
    }
    
    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
    }
    
    public void unsubscribe(Consumer<T> listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (Consumer<T> listener : listeners) {
            listener.accept(value);
        }
    }
}