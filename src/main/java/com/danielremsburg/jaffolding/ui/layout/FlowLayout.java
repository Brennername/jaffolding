package com.danielremsburg.jaffolding.ui.layout;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.UIComponent;

/**
 * A simple flow layout that arranges components in a row.
 * Similar to FlowLayout in Swing.
 */
public class FlowLayout implements Layout {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    
    private int align;
    private int hgap;
    private int vgap;
    
    public FlowLayout() {
        this(LEFT, 5, 5);
    }
    
    public FlowLayout(int align) {
        this(align, 5, 5);
    }
    
    public FlowLayout(int align, int hgap, int vgap) {
        this.align = align;
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
    @Override
    public void applyTo(UIComponent container) {
        container.setStyle("display", "flex");
        container.setStyle("flex-wrap", "wrap");
        
        switch (align) {
            case LEFT:
                container.setStyle("justify-content", "flex-start");
                break;
            case CENTER:
                container.setStyle("justify-content", "center");
                break;
            case RIGHT:
                container.setStyle("justify-content", "flex-end");
                break;
        }
        
        container.setStyle("gap", vgap + "px " + hgap + "px");
    }
    
    @Override
    public void addLayoutComponent(Component comp) {
        if (comp instanceof UIComponent) {
            UIComponent uiComp = (UIComponent) comp;
            uiComp.setStyle("margin", "0");
        }
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        addLayoutComponent(comp);
    }
    
    @Override
    public void removeLayoutComponent(Component comp) {
        // Nothing to do
    }
    
    @Override
    public void layoutContainer(UIComponent container) {
        applyTo(container);
    }
}