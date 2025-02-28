package com.example.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Base component class for the framework.
 * Represents a virtual DOM element that can be rendered to the actual DOM.
 */
public class Component {
    private String tagName;
    private String text;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, String> styles = new HashMap<>();
    private List<Component> children = new ArrayList<>();
    private Map<String, List<Consumer<Event>>> eventListeners = new HashMap<>();
    private HTMLElement element;
    
    public Component(String tagName) {
        this.tagName = tagName;
    }
    
    public Component setText(String text) {
        this.text = text;
        if (element != null) {
            element.setTextContent(text);
        }
        return this;
    }
    
    public Component setAttribute(String name, String value) {
        attributes.put(name, value);
        if (element != null) {
            if (value == null) {
                element.removeAttribute(name);
            } else {
                element.setAttribute(name, value);
            }
        }
        return this;
    }
    
    public Component setStyle(String property, String value) {
        styles.put(property, value);
        if (element != null) {
            element.getStyle().setProperty(property, value);
        }
        return this;
    }
    
    public Component addChild(Component child) {
        children.add(child);
        if (element != null && child.element == null) {
            child.render(element);
        }
        return this;
    }
    
    public Component addEventListener(String eventType, Consumer<Event> listener) {
        eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        if (element != null) {
            element.addEventListener(eventType, new EventListener<Event>() {
                @Override
                public void handleEvent(Event event) {
                    listener.accept(event);
                }
            });
        }
        return this;
    }
    
    public HTMLElement render(HTMLElement parent) {
        element = HTMLDocument.current().createElement(tagName);
        
        // Set text content if provided
        if (text != null) {
            element.setTextContent(text);
        }
        
        // Set attributes
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            if (attr.getValue() == null) {
                element.removeAttribute(attr.getKey());
            } else {
                element.setAttribute(attr.getKey(), attr.getValue());
            }
        }
        
        // Set styles
        for (Map.Entry<String, String> style : styles.entrySet()) {
            element.getStyle().setProperty(style.getKey(), style.getValue());
        }
        
        // Add event listeners
        for (Map.Entry<String, List<Consumer<Event>>> entry : eventListeners.entrySet()) {
            String eventType = entry.getKey();
            List<Consumer<Event>> listeners = entry.getValue();
            
            for (Consumer<Event> listener : listeners) {
                element.addEventListener(eventType, new EventListener<Event>() {
                    @Override
                    public void handleEvent(Event event) {
                        listener.accept(event);
                    }
                });
            }
        }
        
        // Render children
        for (Component child : children) {
            child.render(element);
        }
        
        // Append to parent
        parent.appendChild(element);
        
        return element;
    }
    
    public HTMLElement getElement() {
        return element;
    }
    
    public void removeFromParent() {
        if (element != null && element.getParentNode() != null) {
            element.getParentNode().removeChild(element);
        }
    }
    
    public void clear() {
        if (element != null) {
            while (element.getFirstChild() != null) {
                element.removeChild(element.getFirstChild());
            }
        }
        children.clear();
    }
}