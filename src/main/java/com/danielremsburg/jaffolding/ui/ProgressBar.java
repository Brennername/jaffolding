package com.danielremsburg.jaffolding.ui;

/**
 * A progress bar component.
 * Similar to JProgressBar in Swing.
 */
public class ProgressBar extends UIComponent {
    private int min = 0;
    private int max = 100;
    private int value = 0;
    private boolean indeterminate = false;
    private String foregroundColor = "#4285f4";
    
    public ProgressBar() {
        super("div");
        initializeStyles();
    }
    
    public ProgressBar(int min, int max) {
        super("div");
        this.min = min;
        this.max = max;
        initializeStyles();
    }
    
    public ProgressBar(int min, int max, int value) {
        super("div");
        this.min = min;
        this.max = max;
        this.value = value;
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("width", "100%")
            .setStyle("height", "8px")
            .setStyle("background-color", "#e0e0e0")
            .setStyle("border-radius", "4px")
            .setStyle("overflow", "hidden")
            .setStyle("position", "relative");
        
        updateProgress();
    }
    
    public ProgressBar setValue(int value) {
        this.value = Math.max(min, Math.min(max, value));
        updateProgress();
        return this;
    }
    
    public int getValue() {
        return value;
    }
    
    public ProgressBar setMinimum(int min) {
        this.min = min;
        this.value = Math.max(min, value);
        updateProgress();
        return this;
    }
    
    public int getMinimum() {
        return min;
    }
    
    public ProgressBar setMaximum(int max) {
        this.max = max;
        this.value = Math.min(max, value);
        updateProgress();
        return this;
    }
    
    public int getMaximum() {
        return max;
    }
    
    public ProgressBar setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        updateProgress();
        return this;
    }
    
    public boolean isIndeterminate() {
        return indeterminate;
    }
    
    public ProgressBar setForeground(String color) {
        this.foregroundColor = color;
        updateProgress();
        return this;
    }
    
    private void updateProgress() {
        if (getElement() == null) {
            return;
        }
        
        getElement().setInnerHTML("");
        
        UIComponent progressBar = new UIComponent("div");
        
        if (indeterminate) {
            progressBar.setStyle("position", "absolute")
                       .setStyle("top", "0")
                       .setStyle("left", "0")
                       .setStyle("height", "100%")
                       .setStyle("width", "30%")
                       .setStyle("background-color", foregroundColor)
                       .setStyle("border-radius", "4px")
                       .setStyle("animation", "progress-bar-indeterminate 2s infinite linear");
            
            // Add keyframes for animation
            UIComponent style = new UIComponent("style");
            style.setText(
                "@keyframes progress-bar-indeterminate {\n" +
                "  0% { left: -30%; }\n" +
                "  100% { left: 100%; }\n" +
                "}"
            );
            
            addChild(style);
        } else {
            double percent = (max > min) ? (double)(value - min) / (max - min) * 100 : 0;
            
            progressBar.setStyle("width", percent + "%")
                       .setStyle("height", "100%")
                       .setStyle("background-color", foregroundColor)
                       .setStyle("border-radius", "4px")
                       .setStyle("transition", "width 0.3s ease");
        }
        
        addChild(progressBar);
    }
}