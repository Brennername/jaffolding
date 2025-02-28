package com.danielremsburg.jaffolding.ui;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.layout.Layout;

/**
 * Base class for all UI components in the Jaffolding framework.
 * Extends the basic Component with UI-specific functionality.
 */
public class UIComponent extends Component {
    private Layout layout;
    private boolean enabled = true;
    private boolean visible = true;
    
    public UIComponent(String tagName) {
        super(tagName);
        
        // Default styles for all UI components
        setStyle("box-sizing", "border-box");
    }
    
    public void setLayout(Layout layout) {
        this.layout = layout;
        if (layout != null) {
            layout.applyTo(this);
        }
    }
    
    public Layout getLayout() {
        return layout;
    }
    
    @Override
    public UIComponent addChild(Component child) {
        super.addChild(child);
        
        // If we have a layout, let it handle the child positioning
        if (layout != null && getElement() != null) {
            layout.addLayoutComponent(child);
        }
        
        return this;
    }
    
    public UIComponent addChild(Component child, Object constraints) {
        super.addChild(child);
        
        // If we have a layout, let it handle the child positioning with constraints
        if (layout != null && getElement() != null) {
            layout.addLayoutComponent(child, constraints);
        }
        
        return this;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public UIComponent setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (getElement() != null) {
            if (enabled) {
                getElement().removeAttribute("disabled");
                setStyle("opacity", "1.0");
                setStyle("pointer-events", "auto");
            } else {
                getElement().setAttribute("disabled", "true");
                setStyle("opacity", "0.6");
                setStyle("pointer-events", "none");
            }
        }
        return this;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public UIComponent setVisible(boolean visible) {
        this.visible = visible;
        if (getElement() != null) {
            getElement().getStyle().setProperty("display", visible ? "" : "none");
        }
        return this;
    }
    
    public UIComponent setToolTip(String text) {
        setAttribute("title", text);
        return this;
    }
    
    public UIComponent setId(String id) {
        setAttribute("id", id);
        return this;
    }
    
    public UIComponent setWidth(String width) {
        setStyle("width", width);
        return this;
    }
    
    public UIComponent setHeight(String height) {
        setStyle("height", height);
        return this;
    }
    
    public UIComponent setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
        return this;
    }
    
    public UIComponent setMargin(String margin) {
        setStyle("margin", margin);
        return this;
    }
    
    public UIComponent setPadding(String padding) {
        setStyle("padding", padding);
        return this;
    }
    
    public UIComponent setBackground(String color) {
        setStyle("background-color", color);
        return this;
    }
    
    public UIComponent setForeground(String color) {
        setStyle("color", color);
        return this;
    }
    
    public UIComponent setBorder(String border) {
        setStyle("border", border);
        return this;
    }
    
    public UIComponent setFont(String font) {
        setStyle("font", font);
        return this;
    }
    
    public UIComponent setFontSize(String size) {
        setStyle("font-size", size);
        return this;
    }
    
    public UIComponent setFontWeight(String weight) {
        setStyle("font-weight", weight);
        return this;
    }
    
    public UIComponent setTextAlign(String align) {
        setStyle("text-align", align);
        return this;
    }
}