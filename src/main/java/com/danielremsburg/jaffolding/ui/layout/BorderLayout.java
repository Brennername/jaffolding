package com.danielremsburg.jaffolding.ui.layout;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.UIComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A border layout arranges components in five regions: north, south, east, west, and center.
 * Similar to BorderLayout in Swing.
 */
public class BorderLayout implements Layout {
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String CENTER = "Center";
    
    private Map<String, Component> components = new HashMap<>();
    private int hgap;
    private int vgap;
    
    public BorderLayout() {
        this(0, 0);
    }
    
    public BorderLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
    @Override
    public void applyTo(UIComponent container) {
        container.setStyle("display", "grid");
        container.setStyle("grid-template-areas", 
                "\"north north north\" " +
                "\"west center east\" " +
                "\"south south south\"");
        container.setStyle("grid-template-rows", "auto 1fr auto");
        container.setStyle("grid-template-columns", "auto 1fr auto");
        
        if (hgap > 0 || vgap > 0) {
            container.setStyle("gap", vgap + "px " + hgap + "px");
        }
        
        container.setStyle("height", "100%");
    }
    
    @Override
    public void addLayoutComponent(Component comp) {
        // Default to center if no constraint is provided
        addLayoutComponent(comp, CENTER);
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof String) {
            String position = (String) constraints;
            components.put(position, comp);
            
            if (comp instanceof UIComponent) {
                UIComponent uiComp = (UIComponent) comp;
                
                switch (position) {
                    case NORTH:
                        uiComp.setStyle("grid-area", "north");
                        break;
                    case SOUTH:
                        uiComp.setStyle("grid-area", "south");
                        break;
                    case EAST:
                        uiComp.setStyle("grid-area", "east");
                        break;
                    case WEST:
                        uiComp.setStyle("grid-area", "west");
                        break;
                    case CENTER:
                        uiComp.setStyle("grid-area", "center");
                        break;
                }
            }
        }
    }
    
    @Override
    public void removeLayoutComponent(Component comp) {
        for (Map.Entry<String, Component> entry : components.entrySet()) {
            if (entry.getValue() == comp) {
                components.remove(entry.getKey());
                break;
            }
        }
    }
    
    @Override
    public void layoutContainer(UIComponent container) {
        applyTo(container);
    }
}