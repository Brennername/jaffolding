package com.danielremsburg.jaffolding.ui;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.layout.FlowLayout;
import com.danielremsburg.jaffolding.ui.layout.Layout;

/**
 * A container component that can hold other components.
 * Similar to JPanel in Swing.
 */
public class Panel extends UIComponent {
    
    public Panel() {
        super("div");
        setLayout(new FlowLayout());
        setStyle("display", "flex");
        setStyle("flex-wrap", "wrap");
    }
    
    @Override
    public void setLayout(Layout layout) {
        super.setLayout(layout);
        
        // Apply layout to existing children
        if (getElement() != null && layout != null) {
            layout.layoutContainer(this);
        }
    }
    
    /**
     * Creates a panel with a title border.
     */
    public static Panel createTitledPanel(String title) {
        Panel panel = new Panel();
        panel.setBorder("1px solid #ccc")
             .setStyle("border-radius", "4px")
             .setStyle("overflow", "hidden");
        
        Panel titleBar = new Panel();
        titleBar.setBackground("#f5f5f5")
                .setStyle("padding", "8px 12px")
                .setStyle("border-bottom", "1px solid #ccc")
                .setStyle("font-weight", "bold");
        
        Label titleLabel = new Label(title);
        titleBar.addChild(titleLabel);
        
        Panel contentPanel = new Panel();
        contentPanel.setPadding("12px");
        
        panel.addChild(titleBar);
        panel.addChild(contentPanel);
        
        return panel;
    }
}