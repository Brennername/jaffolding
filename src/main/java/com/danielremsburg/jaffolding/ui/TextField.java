package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLInputElement;

import java.util.function.Consumer;

/**
 * A text input field component.
 * Similar to JTextField in Swing.
 */
public class TextField extends UIComponent {
    private String placeholder;
    
    public TextField() {
        super("input");
        setAttribute("type", "text");
        initializeStyles();
    }
    
    public TextField(String initialText) {
        super("input");
        setAttribute("type", "text");
        setValue(initialText);
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("padding", "8px 12px")
            .setStyle("border", "1px solid #dadce0")
            .setStyle("border-radius", "4px")
            .setStyle("font-size", "14px")
            .setStyle("width", "100%")
            .setStyle("transition", "border-color 0.2s")
            .addEventListener("focus", e -> {
                if (isEnabled()) {
                    setStyle("border-color", "#4285f4");
                    setStyle("outline", "none");
                }
            })
            .addEventListener("blur", e -> {
                if (isEnabled()) {
                    setStyle("border-color", "#dadce0");
                }
            });
    }
    
    public String getValue() {
        if (getElement() != null) {
            return ((HTMLInputElement) getElement()).getValue();
        }
        return "";
    }
    
    public TextField setValue(String value) {
        if (getElement() != null) {
            ((HTMLInputElement) getElement()).setValue(value);
        } else {
            setAttribute("value", value);
        }
        return this;
    }
    
    public TextField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setAttribute("placeholder", placeholder);
        return this;
    }
    
    public TextField setMaxLength(int maxLength) {
        setAttribute("maxlength", String.valueOf(maxLength));
        return this;
    }
    
    public TextField setReadOnly(boolean readOnly) {
        if (readOnly) {
            setAttribute("readonly", "readonly");
        } else {
            getElement().removeAttribute("readonly");
        }
        return this;
    }
    
    public TextField setOnChange(Consumer<Event> handler) {
        addEventListener("change", handler);
        return this;
    }
    
    public TextField setOnInput(Consumer<Event> handler) {
        addEventListener("input", handler);
        return this;
    }
    
    public TextField setOnKeyPress(Consumer<Event> handler) {
        addEventListener("keypress", handler);
        return this;
    }
}