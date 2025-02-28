package com.danielremsburg.jaffolding.ui.layout;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.UIComponent;

/**
 * A grid layout arranges components in a grid of rows and columns.
 * Similar to GridLayout in Swing.
 */
public class GridLayout implements Layout {
    private int rows;
    private int cols;
    private int hgap;
    private int vgap;
    
    public GridLayout(int rows, int cols) {
        this(rows, cols, 0, 0);
    }
    
    public GridLayout(int rows, int cols, int hgap, int vgap) {
        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
    @Override
    public void applyTo(UIComponent container) {
        container.setStyle("display", "grid");
        
        if (cols > 0) {
            container.setStyle("grid-template-columns", "repeat(" + cols + ", 1fr)");
        }
        
        if (rows > 0) {
            container.setStyle("grid-template-rows", "repeat(" + rows + ", auto)");
        }
        
        if (hgap > 0 || vgap > 0) {
            container.setStyle("gap", vgap + "px " + hgap + "px");
        }
    }
    
    @Override
    public void addLayoutComponent(Component comp) {
        // No specific positioning needed
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof GridConstraints) {
            GridConstraints gc = (GridConstraints) constraints;
            
            if (comp instanceof UIComponent) {
                UIComponent uiComp = (UIComponent) comp;
                
                if (gc.gridx >= 0) {
                    uiComp.setStyle("grid-column-start", String.valueOf(gc.gridx + 1));
                }
                
                if (gc.gridy >= 0) {
                    uiComp.setStyle("grid-row-start", String.valueOf(gc.gridy + 1));
                }
                
                if (gc.gridwidth > 1) {
                    uiComp.setStyle("grid-column-end", "span " + gc.gridwidth);
                }
                
                if (gc.gridheight > 1) {
                    uiComp.setStyle("grid-row-end", "span " + gc.gridheight);
                }
            }
        }
    }
    
    @Override
    public void removeLayoutComponent(Component comp) {
        // Nothing to do
    }
    
    @Override
    public void layoutContainer(UIComponent container) {
        applyTo(container);
    }
    
    /**
     * Constraints for grid layout positioning.
     */
    public static class GridConstraints {
        public int gridx = -1;
        public int gridy = -1;
        public int gridwidth = 1;
        public int gridheight = 1;
        
        public GridConstraints() {}
        
        public GridConstraints(int gridx, int gridy) {
            this.gridx = gridx;
            this.gridy = gridy;
        }
        
        public GridConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }
    }
}