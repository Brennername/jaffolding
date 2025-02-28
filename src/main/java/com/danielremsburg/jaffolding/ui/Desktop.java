package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;

/**
 * A desktop environment component with a taskbar.
 */
public class Desktop extends UIComponent {
    private Panel desktopArea;
    private Panel taskbar;
    private List<TaskbarItem> taskbarItems = new ArrayList<>();
    
    public Desktop() {
        super("div");
        initializeDesktop();
    }
    
    private void initializeDesktop() {
        setStyle("position", "relative")
            .setStyle("width", "100%")
            .setStyle("height", "100%")
            .setStyle("overflow", "hidden")
            .setStyle("background", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)");
        
        // Create desktop area
        desktopArea = new Panel();
        desktopArea.setStyle("position", "absolute")
                   .setStyle("top", "0")
                   .setStyle("left", "0")
                   .setStyle("right", "0")
                   .setStyle("bottom", "60px") // Leave space for taskbar
                   .setStyle("overflow", "hidden");
        
        // Create taskbar
        taskbar = new Panel();
        taskbar.setStyle("position", "absolute")
               .setStyle("bottom", "20px")
               .setStyle("left", "50%")
               .setStyle("transform", "translateX(-50%)")
               .setStyle("height", "50px")
               .setStyle("background-color", "rgba(255, 255, 255, 0.7)")
               .setStyle("backdrop-filter", "blur(10px)")
               .setStyle("border-radius", "25px")
               .setStyle("box-shadow", "0 4px 20px rgba(0, 0, 0, 0.1)")
               .setStyle("display", "flex")
               .setStyle("align-items", "center")
               .setStyle("padding", "0 15px")
               .setStyle("z-index", "1000");
        
        addChild(desktopArea);
        addChild(taskbar);
    }
    
    public Desktop addWindow(Window window) {
        desktopArea.addChild(window);
        
        // Create taskbar item for the window
        TaskbarItem taskbarItem = new TaskbarItem(window);
        taskbarItems.add(taskbarItem);
        taskbar.addChild(taskbarItem);
        
        // Adjust taskbar width based on number of items
        int taskbarWidth = Math.min(800, 60 + taskbarItems.size() * 50);
        taskbar.setStyle("width", taskbarWidth + "px");
        
        return this;
    }
    
    /**
     * A taskbar item representing a window.
     */
    private class TaskbarItem extends UIComponent {
        private Window window;
        
        public TaskbarItem(Window window) {
            super("div");
            this.window = window;
            
            setStyle("width", "40px")
                .setStyle("height", "40px")
                .setStyle("background-color", "rgba(66, 133, 244, 0.8)")
                .setStyle("border-radius", "20px")
                .setStyle("margin", "0 5px")
                .setStyle("cursor", "pointer")
                .setStyle("transition", "transform 0.2s, background-color 0.2s")
                .addEventListener("mouseover", e -> {
                    setStyle("transform", "scale(1.1)");
                })
                .addEventListener("mouseout", e -> {
                    setStyle("transform", "scale(1)");
                })
                .addEventListener("click", e -> {
                    // Bring the window to front when clicking its taskbar item
                    HTMLElement windowElement = window.getElement();
                    if (windowElement != null) {
                        // Find the highest z-index
                        HTMLElement parent = (HTMLElement) windowElement.getParentNode();
                        int highestZIndex = 0;
                        
                        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
                            if (parent.getChildNodes().item(i) instanceof HTMLElement) {
                                HTMLElement sibling = (HTMLElement) parent.getChildNodes().item(i);
                                String zIndexStr = sibling.getStyle().getPropertyValue("z-index");
                                if (zIndexStr != null && !zIndexStr.isEmpty()) {
                                    try {
                                        int zIndex = Integer.parseInt(zIndexStr);
                                        highestZIndex = Math.max(highestZIndex, zIndex);
                                    } catch (NumberFormatException ex) {
                                        // Ignore parsing errors
                                    }
                                }
                            }
                        }
                        
                        // Set this window's z-index to be higher
                        windowElement.getStyle().setProperty("z-index", String.valueOf(highestZIndex + 1));
                    }
                });
        }
    }
}