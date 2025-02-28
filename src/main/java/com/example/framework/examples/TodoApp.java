package com.example.framework.examples;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

import com.example.framework.Component;
import com.example.framework.State;

/**
 * Example Todo application built with the framework.
 */
public class TodoApp {
    private static class TodoItem {
        String text;
        boolean completed;
        
        TodoItem(String text) {
            this.text = text;
            this.completed = false;
        }
    }
    
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement root = document.getElementById("app");
        
        // Create state
        State<List<TodoItem>> todos = new State<>(new ArrayList<>());
        State<String> newTodoText = new State<>("");
        
        // Input field for new todos
        Component inputField = new Component("div")
            .setStyle("display", "flex")
            .setStyle("margin-bottom", "20px")
            .addChild(new Component("input")
                .setAttribute("type", "text")
                .setAttribute("placeholder", "Add a new todo...")
                .setStyle("flex", "1")
                .setStyle("padding", "8px")
                .setStyle("font-size", "16px")
                .setStyle("border", "1px solid #ddd")
                .setStyle("border-radius", "4px 0 0 4px")
                .addEventListener("input", event -> {
                    HTMLInputElement input = (HTMLInputElement) event.getTarget();
                    newTodoText.set(input.getValue());
                })
                .addEventListener("keypress", event -> {
                    KeyboardEvent keyEvent = (KeyboardEvent) event;
                    if (keyEvent.getKeyCode() == 13 && !newTodoText.get().trim().isEmpty()) {
                        addTodo(todos, newTodoText.get());
                        newTodoText.set("");
                        ((HTMLInputElement) event.getTarget()).setValue("");
                    }
                }))
            .addChild(new Component("button")
                .setText("Add")
                .setStyle("padding", "8px 16px")
                .setStyle("background-color", "#4CAF50")
                .setStyle("color", "white")
                .setStyle("border", "none")
                .setStyle("border-radius", "0 4px 4px 0")
                .setStyle("cursor", "pointer")
                .addEventListener("click", event -> {
                    if (!newTodoText.get().trim().isEmpty()) {
                        addTodo(todos, newTodoText.get());
                        newTodoText.set("");
                        
                        // Clear input field
                        HTMLInputElement input = (HTMLInputElement) document.querySelector("input");
                        if (input != null) {
                            input.setValue("");
                        }
                    }
                }));
        
        // Todo list container
        Component todoList = new Component("ul")
            .setStyle("list-style", "none")
            .setStyle("padding", "0");
        
        // Subscribe to state changes
        todos.subscribe(items -> {
            HTMLElement todoListElement = todoList.getElement();
            if (todoListElement != null) {
                while (todoListElement.getFirstChild() != null) {
                    todoListElement.removeChild(todoListElement.getFirstChild());
                }
            
                // Add todo items
                for (int i = 0; i < items.size(); i++) {
                    final int index = i;
                    TodoItem item = items.get(i);
                    
                    Component todoItem = new Component("li")
                        .setStyle("display", "flex")
                        .setStyle("align-items", "center")
                        .setStyle("padding", "8px 0")
                        .setStyle("border-bottom", "1px solid #eee")
                        .addChild(new Component("input")
                            .setAttribute("type", "checkbox")
                            .setAttribute("checked", item.completed ? "checked" : null)
                            .setStyle("margin-right", "10px")
                            .addEventListener("change", e -> {
                                toggleTodo(todos, index);
                            }))
                        .addChild(new Component("span")
                            .setText(item.text)
                            .setStyle("flex", "1")
                            .setStyle("text-decoration", item.completed ? "line-through" : "none")
                            .setStyle("color", item.completed ? "#888" : "#000"))
                        .addChild(new Component("button")
                            .setText("Delete")
                            .setStyle("background-color", "#f44336")
                            .setStyle("color", "white")
                            .setStyle("border", "none")
                            .setStyle("border-radius", "4px")
                            .setStyle("padding", "4px 8px")
                            .setStyle("cursor", "pointer")
                            .addEventListener("click", e -> {
                                removeTodo(todos, index);
                            }));
                    
                    todoItem.render(todoListElement);
                }
            }
        });
        
        // Main app component
        Component app = new Component("div")
            .setStyle("max-width", "500px")
            .setStyle("margin", "0 auto")
            .setStyle("padding", "20px")
            .addChild(new Component("h1")
                .setText("Todo App")
                .setStyle("text-align", "center")
                .setStyle("margin-bottom", "20px"))
            .addChild(inputField)
            .addChild(todoList);
        
        // Initial render
        root.setInnerHTML("");
        app.render(root);
        
        // Add some initial todos
        addTodo(todos, "Learn Java TeaVM Framework");
        addTodo(todos, "Build a WebAssembly application");
        addTodo(todos, "Create more examples");
    }
    
    private static void addTodo(State<List<TodoItem>> todos, String text) {
        List<TodoItem> newList = new ArrayList<>(todos.get());
        newList.add(new TodoItem(text));
        todos.set(newList);
    }
    
    private static void toggleTodo(State<List<TodoItem>> todos, int index) {
        List<TodoItem> newList = new ArrayList<>(todos.get());
        TodoItem item = newList.get(index);
        item.completed = !item.completed;
        todos.set(newList);
    }
    
    private static void removeTodo(State<List<TodoItem>> todos, int index) {
        List<TodoItem> newList = new ArrayList<>(todos.get());
        newList.remove(index);
        todos.set(newList);
    }
}