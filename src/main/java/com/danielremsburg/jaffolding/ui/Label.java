package com.danielremsburg.jaffolding.ui;

/**
 * A label component for displaying text.
 * Similar to JLabel in Swing.
 */
public class Label extends UIComponent {
    
    public Label() {
        super("span");
        initializeStyles();
    }
    
    public Label(String text) {
        super("span");
        setText(text);
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("display", "inline-block");
    }
    
    public Label setHeading(int level) {
        if (level < 1) level = 1;
        if (level > 6) level = 6;
        
        setStyle("font-size", (24 - (level * 2)) + "px");
        setStyle("font-weight", "bold");
        setStyle("margin", "0.5em 0");
        
        return this;
    }
    
    public Label setBold() {
        setStyle("font-weight", "bold");
        return this;
    }
    
    public Label setItalic() {
        setStyle("font-style", "italic");
        return this;
    }
    
    public Label setUnderline() {
        setStyle("text-decoration", "underline");
        return this;
    }
}