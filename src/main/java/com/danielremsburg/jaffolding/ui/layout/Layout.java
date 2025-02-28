package com.danielremsburg.jaffolding.ui.layout;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.UIComponent;

/**
 * Base interface for all layout managers in the Jaffolding framework.
 * Similar to LayoutManager in Swing.
 */
public interface Layout {
    /**
     * Apply this layout to a container.
     */
    void applyTo(UIComponent container);
    
    /**
     * Add a component to the layout.
     */
    void addLayoutComponent(Component comp);
    
    /**
     * Add a component to the layout with constraints.
     */
    void addLayoutComponent(Component comp, Object constraints);
    
    /**
     * Remove a component from the layout.
     */
    void removeLayoutComponent(Component comp);
    
    /**
     * Layout the container's children according to this layout's rules.
     */
    void layoutContainer(UIComponent container);
}