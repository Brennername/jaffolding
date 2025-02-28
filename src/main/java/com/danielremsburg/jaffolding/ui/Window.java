package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MouseEvent;

import com.danielremsburg.jaffolding.Component;

/**
 * A draggable, resizable, closeable, minimizable, maximizable window component.
 */
public class Window extends Component {
    private String title;
    private Component content;
    private boolean draggable = true;
    private boolean resizable = true;
    private boolean closeable = true;
    private boolean minimizable = true;
    private boolean maximizable = true;
    
    private boolean isDragging = false;
    private boolean isResizing = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;
    private double resizeStartX = 0;
    private double resizeStartY = 0;
    private double initialWidth = 0;
    private double initialHeight = 0;
    
    private boolean isMinimized = false;
    private boolean isMaximized = false;
    private double savedX = 0;
    private double savedY = 0;
    private double savedWidth = 0;
    private double savedHeight = 0;
    
    private Component titleBar;
    private Component contentArea;
    private Component resizeHandle;
    
    public Window(String title, Component content) {
        super("div");
        this.title = title;
        this.content = content;
        initializeWindow();
    }
    
    private void initializeWindow() {
        setStyle("position", "absolute")
            .setStyle("top", "50px")
            .setStyle("left", "50px")
            .setStyle("width", "400px")
            .setStyle("height", "300px")
            .setStyle("background-color", "white")
            .setStyle("border-radius", "8px")
            .setStyle("box-shadow", "0 4px 20px rgba(0, 0, 0, 0.15)")
            .setStyle("display", "flex")
            .setStyle("flex-direction", "column")
            .setStyle("overflow", "hidden")
            .setStyle("transition", "box-shadow 0.2s ease");
        
        // Create title bar
        titleBar = new Component("div");
        titleBar.setStyle("display", "flex")
                .setStyle("align-items", "center")
                .setStyle("padding", "8px 12px")
                .setStyle("background-color", "#f5f5f5")
                .setStyle("border-bottom", "1px solid #e0e0e0")
                .setStyle("cursor", draggable ? "move" : "default")
                .setStyle("user-select", "none");
        
        Component titleLabel = new Component("div");
        titleLabel.setText(title)
                .setStyle("flex", "1")
                .setStyle("font-weight", "500");
        
        Component windowControls = new Component("div");
        windowControls.setStyle("display", "flex")
                    .setStyle("gap", "8px");
        
        if (minimizable) {
            Component minimizeBtn = new Component("button");
            minimizeBtn.setText("−")
                   .setStyle("width", "20px")
                   .setStyle("height", "20px")
                   .setStyle("display", "flex")
                   .setStyle("align-items", "center")
                   .setStyle("justify-content", "center")
                   .setStyle("background-color", "#ffbd44")
                   .setStyle("color", "#5f5f5f")
                   .setStyle("border", "none")
                   .setStyle("border-radius", "50%")
                   .setStyle("font-size", "14px")
                   .setStyle("cursor", "pointer")
                   .addEventListener("click", e -> {
                       toggleMinimize();
                       e.stopPropagation();
                   });
            
            windowControls.addChild(minimizeBtn);
        }
        
        if (maximizable) {
            Component maximizeBtn = new Component("button");
            maximizeBtn.setText("□")
                   .setStyle("width", "20px")
                   .setStyle("height", "20px")
                   .setStyle("display", "flex")
                   .setStyle("align-items", "center")
                   .setStyle("justify-content", "center")
                   .setStyle("background-color", "#00ca56")
                   .setStyle("color", "#5f5f5f")
                   .setStyle("border", "none")
                   .setStyle("border-radius", "50%")
                   .setStyle("font-size", "10px")
                   .setStyle("cursor", "pointer")
                   .addEventListener("click", e -> {
                       toggleMaximize();
                       e.stopPropagation();
                   });
            
            windowControls.addChild(maximizeBtn);
        }
        
        if (closeable) {
            Component closeBtn = new Component("button");
            closeBtn.setText("×")
                    .setStyle("width", "20px")
                    .setStyle("height", "20px")
                    .setStyle("display", "flex")
                    .setStyle("align-items", "center")
                    .setStyle("justify-content", "center")
                    .setStyle("background-color", "#ff605c")
                    .setStyle("color", "#5f5f5f")
                    .setStyle("border", "none")
                    .setStyle("border-radius", "50%")
                    .setStyle("font-size", "14px")
                    .setStyle("cursor", "pointer")
                    .addEventListener("click", e -> {
                        close();
                        e.stopPropagation();
                    });
            
            windowControls.addChild(closeBtn);
        }
        
        titleBar.addChild(titleLabel);
        titleBar.addChild(windowControls);
        
        // Create content area
        contentArea = new Component("div");
        contentArea.setStyle("flex", "1")
                   .setStyle("overflow", "auto")
                   .setStyle("position", "relative");
        
        contentArea.addChild(content);
        
        // Create resize handle
        if (resizable) {
            resizeHandle = new Component("div");
            resizeHandle.setStyle("position", "absolute")
                        .setStyle("bottom", "0")
                        .setStyle("right", "0")
                        .setStyle("width", "16px")
                        .setStyle("height", "16px")
                        .setStyle("cursor", "nwse-resize")
                        .setStyle("background", "linear-gradient(135deg, transparent 50%, rgba(0, 0, 0, 0.2) 50%)")
                        .setStyle("z-index", "10");
            
            addChild(resizeHandle);
        }
        
        addChild(titleBar);
        addChild(contentArea);
        
        // Set up drag functionality
        if (draggable) {
            titleBar.addEventListener("mousedown", e -> {
                startDrag((MouseEvent)e);
                e.stopPropagation();
            });
        }
        
        // Set up resize functionality
        if (resizable) {
            resizeHandle.addEventListener("mousedown", e -> {
                startResize((MouseEvent)e);
                e.stopPropagation();
            });
        }
        
        // Global mouse events for drag and resize
        HTMLDocument.current().addEventListener("mousemove", new EventListener<MouseEvent>() {
            @Override
            public void handleEvent(MouseEvent e) {
                if (isDragging) {
                    drag(e);
                } else if (isResizing) {
                    resize(e);
                }
            }
        });
        
        HTMLDocument.current().addEventListener("mouseup", new EventListener<Event>() {
            @Override
            public void handleEvent(Event e) {
                isDragging = false;
                isResizing = false;
                if (getElement() != null) {
                    getElement().getStyle().setProperty("transition", "box-shadow 0.2s ease");
                }
            }
        });
        
        // Focus handling
        addEventListener("mousedown", e -> {
            bringToFront();
        });
    }
    
    private void startDrag(MouseEvent e) {
        if (isMaximized) return;
        
        isDragging = true;
        
        HTMLElement element = getElement();
        if (element != null) {
            element.getStyle().setProperty("transition", "none");
            
            // Calculate the offset from the mouse position to the window's top-left corner
            dragOffsetX = e.getClientX() - element.getBoundingClientRect().getLeft();
            dragOffsetY = e.getClientY() - element.getBoundingClientRect().getTop();
        }
    }
    
    private void drag(MouseEvent e) {
        if (!isDragging) return;
        
        HTMLElement element = getElement();
        if (element != null) {
            double newX = e.getClientX() - dragOffsetX;
            double newY = e.getClientY() - dragOffsetY;
            
            // Ensure the window stays within the viewport
            Window browserWindow = org.teavm.jso.browser.Window.current();
            newX = Math.max(0, Math.min(newX, browserWindow.getInnerWidth() - element.getBoundingClientRect().getWidth()));
            newY = Math.max(0, Math.min(newY, browserWindow.getInnerHeight() - element.getBoundingClientRect().getHeight()));
            
            element.getStyle().setProperty("left", newX + "px");
            element.getStyle().setProperty("top", newY + "px");
        }
    }
    
    private void startResize(MouseEvent e) {
        if (isMaximized) return;
        
        isResizing = true;
        
        HTMLElement element = getElement();
        if (element != null) {
            element.getStyle().setProperty("transition", "none");
            
            resizeStartX = e.getClientX();
            resizeStartY = e.getClientY();
            initialWidth = element.getBoundingClientRect().getWidth();
            initialHeight = element.getBoundingClientRect().getHeight();
        }
    }
    
    private void resize(MouseEvent e) {
        if (!isResizing) return;
        
        HTMLElement element = getElement();
        if (element != null) {
            double deltaX = e.getClientX() - resizeStartX;
            double deltaY = e.getClientY() - resizeStartY;
            
            double newWidth = Math.max(200, initialWidth + deltaX);
            double newHeight = Math.max(150, initialHeight + deltaY);
            
            element.getStyle().setProperty("width", newWidth + "px");
            element.getStyle().setProperty("height", newHeight + "px");
        }
    }
    
    private void toggleMinimize() {
        isMinimized = !isMinimized;
        
        HTMLElement element = getElement();
        if (element != null) {
            if (isMinimized) {
                savedHeight = element.getBoundingClientRect().getHeight();
                contentArea.getElement().style.setProperty("display", "none");
                if (resizeHandle != null) {
                    resizeHandle.getElement().style.setProperty("display", "none");
                }
                element.getStyle().setProperty("height", "auto");
            } else {
                contentArea.getElement().style.setProperty("display", "block");
                if (resizeHandle != null) {
                    resizeHandle.getElement().style.setProperty("display", "block");
                }
                element.getStyle().setProperty("height", savedHeight + "px");
            }
        }
    }
    
    private void toggleMaximize() {
        isMaximized = !isMaximized;
        
        HTMLElement element = getElement();
        if (element != null) {
            if (isMaximized) {
                // Save current position and size
                savedX = element.getBoundingClientRect().getLeft();
                savedY = element.getBoundingClientRect().getTop();
                savedWidth = element.getBoundingClientRect().getWidth();
                savedHeight = element.getBoundingClientRect().getHeight();
                
                // Maximize
                element.getStyle().setProperty("top", "0");
                element.getStyle().setProperty("left", "0");
                element.getStyle().setProperty("width", "100%");
                element.getStyle().setProperty("height", "100%");
                element.getStyle().setProperty("border-radius", "0");
            } else {
                // Restore
                element.getStyle().setProperty("top", savedY + "px");
                element.getStyle().setProperty("left", savedX + "px");
                element.getStyle().setProperty("width", savedWidth + "px");
                element.getStyle().setProperty("height", savedHeight + "px");
                element.getStyle().setProperty("border-radius", "8px");
            }
        }
    }
    
    private void close() {
        if (getElement() != null && getElement().getParentNode() != null) {
            getElement().getParentNode().removeChild(getElement());
        }
    }
    
    private void bringToFront() {
        if (getElement() != null) {
            // Find the highest z-index among siblings
            HTMLElement parent = (HTMLElement) getElement().getParentNode();
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
            getElement().getStyle().setProperty("z-index", String.valueOf(highestZIndex + 1));
        }
    }
    
    public Window setPosition(int x, int y) {
        setStyle("left", x + "px");
        setStyle("top", y + "px");
        return this;
    }
    
    public Window setSize(int width, int height) {
        setStyle("width", width + "px");
        setStyle("height", height + "px");
        return this;
    }
    
    public Window setDraggable(boolean draggable) {
        this.draggable = draggable;
        if (titleBar != null) {
            titleBar.setStyle("cursor", draggable ? "move" : "default");
        }
        return this;
    }
    
    public Window setResizable(boolean resizable) {
        this.resizable = resizable;
        if (resizeHandle != null) {
            resizeHandle.setStyle("display", resizable ? "block" : "none");
        }
        return this;
    }
}