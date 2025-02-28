package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.dom.events.Event;

import java.util.function.Consumer;

/**
 * A button component.
 * Similar to JButton in Swing.
 */
public class Button extends UIComponent {
    
    public Button() {
        super("button");
        initializeStyles();
    }
    
    public Button(String text) {
        super("button");
        setText(text);
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("padding", "8px 16px")
            .setStyle("background-color", "#4285f4")
            .setStyle("color", "white")
            .setStyle("border", "none")
            .setStyle("border-radius", "4px")
            .setStyle("cursor", "pointer")
            .setStyle("font-size", "14px")
            .setStyle("transition", "background-color 0.2s")
            .addEventListener("mouseover", e -> {
                if (isEnabled()) {
                    setStyle("background-color", "#3367d6");
                }
            })
            .addEventListener("mouseout", e -> {
                if (isEnabled()) {
                    setStyle("background-color", "#4285f4");
                }
            })
            .addEventListener("mousedown", e -> {
                if (isEnabled()) {
                    setStyle("transform", "scale(0.98)");
                }
            })
            .addEventListener("mouseup", e -> {
                if (isEnabled()) {
                    setStyle("transform", "scale(1)");
                }
            });
    }
    
    public Button setOnClick(Consumer<Event> handler) {
        addEventListener("click", handler);
        return this;
    }
    
    public Button setPrimary() {
        setStyle("background-color", "#4285f4");
        setStyle("color", "white");
        return this;
    }
    
    public Button setSecondary() {
        setStyle("background-color", "#f1f3f4");
        setStyle("color", "#3c4043");
        setStyle("border", "1px solid #dadce0");
        return this;
    }
    
    public Button setDanger() {
        setStyle("background-color", "#ea4335");
        setStyle("color", "white");
        return this;
    }
    
    public Button setSuccess() {
        setStyle("background-color", "#34a853");
        setStyle("color", "white");
        return this;
    }
    
    public Button setWarning() {
        setStyle("background-color", "#fbbc05");
        setStyle("color", "white");
        return this;
    }
    
    public Button setOutline() {
        setStyle("background-color", "transparent");
        setStyle("color", "#4285f4");
        setStyle("border", "1px solid #4285f4");
        return this;
    }
}