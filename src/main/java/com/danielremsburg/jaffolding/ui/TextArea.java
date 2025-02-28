package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLTextAreaElement;

import java.util.function.Consumer;

/**
 * A multi-line text input component.
 * Similar to JTextArea in Swing.
 */
public class TextArea extends UIComponent {
    
    public TextArea() {
        super("textarea");
        initializeStyles();
    }
    
    public TextArea(String initialText) {
        super("textarea");
        setText(initialText);
        initializeStyles();
    }
    
    public TextArea(int rows, int cols) {
        super("textarea");
        setAttribute("rows", String.valueOf(rows));
        setAttribute("cols", String.valueOf(cols));
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("padding", "8px 12px")
            .setStyle("border", "1px solid #dadce0")
            .setStyle("border-radius", "4px")
            .setStyle("font-size", "14px")
            .setStyle("font-family", "inherit")
            .setStyle("width", "100%")
            .setStyle("resize", "vertical")
            .setStyle("min-height", "80px")
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
            return ((HTMLTextAreaElement) getElement()).getValue();
        }
        return "";
    }
    
    public TextArea setValue(String value) {
        if (getElement() != null) {
            ((HTMLTextAreaElement) getElement()).setValue(value);
        } else {
            setText(value);
        }
        return this;
    }
    
    public TextArea setPlaceholder(String placeholder) {
        setAttribute("placeholder", placeholder);
        return this;
    }
    
    public TextArea setRows(int rows) {
        setAttribute("rows", String.valueOf(rows));
        return this;
    }
    
    public TextArea setCols(int cols) {
        setAttribute("cols", String.valueOf(cols));
        return this;
    }
    
    public TextArea setReadOnly(boolean readOnly) {
        if (readOnly) {
            setAttribute("readonly", "readonly");
        } else {
            getElement().removeAttribute("readonly");
        }
        return this;
    }
    
    public TextArea setOnChange(Consumer<Event> handler) {
        addEventListener("change", handler);
        return this;
    }
    
    public TextArea setOnInput(Consumer<Event> handler) {
        addEventListener("input", handler);
        return this;
    }
}