package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.bridge.JSBridge;
import com.danielremsburg.jaffolding.bridge.JSComponentWrapper;

/**
 * A browser-like window component for the Jaffolding framework.
 * This class can use either the Java implementation or delegate to the JavaScript implementation.
 */
public class BrowserWindow extends Window {
    private JSComponentWrapper jsWrapper;
    private String url;
    private boolean usingJSImplementation;
    
    /**
     * Creates a new BrowserWindow instance.
     * @param title The window title
     * @param url The initial URL
     */
    public BrowserWindow(String title, String url) {
        super(title, new Component("div"));
        this.url = url;
        
        // Create a wrapper for the JS component
        jsWrapper = new JSComponentWrapper("jaffolding.BrowserWindow") {
            @Override
            public boolean initJSComponent(HTMLElement container) {
                if (JSBridge.isComponentAvailable(componentPath)) {
                    JSObject jsTitle = JSBridge.parseJSON("\"" + title + "\"");
                    JSObject jsUrl = JSBridge.parseJSON("\"" + url + "\"");
                    jsComponent = JSBridge.callMethod(
                        JSBridge.createComponent(componentPath, null), 
                        "constructor", 
                        new JSObject[] { jsTitle, jsUrl }
                    );
                    return jsComponent != null;
                }
                return false;
            }
        };
        
        // Try to use the JavaScript implementation
        usingJSImplementation = jsWrapper.isJSComponentAvailable();
        
        if (usingJSImplementation) {
            jsWrapper.initJSComponent(null);
        } else {
            // If JavaScript implementation is not available, use Java implementation
            initJavaImplementation();
        }
    }
    
    /**
     * Initializes the Java implementation of the browser window.
     */
    private void initJavaImplementation() {
        // TODO: Implement Java version of BrowserWindow
        System.out.println("Using Java implementation of BrowserWindow");
        
        // Create browser toolbar
        Component toolbar = new Component("div");
        toolbar.setStyle("display", "flex")
               .setStyle("align-items", "center")
               .setStyle("padding", "8px 12px")
               .setStyle("background-color", "#2e3440")
               .setStyle("border-bottom", "1px solid #434c5e");
        
        // Navigation buttons
        Component backButton = new Component("button");
        backButton.setText("←")
                  .setStyle("width", "30px")
                  .setStyle("height", "30px")
                  .setStyle("border-radius", "4px")
                  .setStyle("border", "none")
                  .setStyle("background-color", "#4c566a")
                  .setStyle("color", "#eceff4")
                  .setStyle("margin-right", "5px")
                  .setStyle("cursor", "pointer")
                  .addEventListener("click", e -> goBack());
        
        Component forwardButton = new Component("button");
        forwardButton.setText("→")
                     .setStyle("width", "30px")
                     .setStyle("height", "30px")
                     .setStyle("border-radius", "4px")
                     .setStyle("border", "none")
                     .setStyle("background-color", "#4c566a")
                     .setStyle("color", "#eceff4")
                     .setStyle("margin-right", "5px")
                     .setStyle("cursor", "pointer")
                     .addEventListener("click", e -> goForward());
        
        Component refreshButton = new Component("button");
        refreshButton.setText("↻")
                     .setStyle("width", "30px")
                     .setStyle("height", "30px")
                     .setStyle("border-radius", "4px")
                     .setStyle("border", "none")
                     .setStyle("background-color", "#4c566a")
                     .setStyle("color", "#eceff4")
                     .setStyle("margin-right", "10px")
                     .setStyle("cursor", "pointer")
                     .addEventListener("click", e -> refresh());
        
        // Address bar
        Component addressBar = new Component("input");
        addressBar.setAttribute("type", "text")
                  .setAttribute("value", url)
                  .setStyle("flex", "1")
                  .setStyle("padding", "8px 12px")
                  .setStyle("border-radius", "4px")
                  .setStyle("border", "1px solid #4c566a")
                  .setStyle("background-color", "#3b4252")
                  .setStyle("color", "#eceff4")
                  .setStyle("font-size", "14px");
        
        // Add elements to toolbar
        toolbar.addChild(backButton);
        toolbar.addChild(forwardButton);
        toolbar.addChild(refreshButton);
        toolbar.addChild(addressBar);
        
        // Create content frame
        Component contentFrame = new Component("div");
        contentFrame.setStyle("flex", "1")
                    .setStyle("overflow", "auto")
                    .setStyle("background-color", "#3b4252");
        
        // Add toolbar and content frame to the window content
        content.setStyle("display", "flex")
               .setStyle("flex-direction", "column")
               .setStyle("height", "100%");
        
        content.addChild(toolbar);
        content.addChild(contentFrame);
    }
    
    /**
     * Navigates to a URL.
     * @param url The URL to navigate to
     */
    public void navigate(String url) {
        if (usingJSImplementation) {
            JSObject jsUrl = JSBridge.parseJSON("\"" + url + "\"");
            jsWrapper.callMethod("navigate", jsUrl);
        } else {
            // Java implementation
            this.url = url;
            // TODO: Implement navigation in Java version
        }
    }
    
    /**
     * Goes back in the browser history.
     */
    public void goBack() {
        if (usingJSImplementation) {
            jsWrapper.callMethod("goBack");
        } else {
            // Java implementation
            // TODO: Implement back navigation in Java version
        }
    }
    
    /**
     * Goes forward in the browser history.
     */
    public void goForward() {
        if (usingJSImplementation) {
            jsWrapper.callMethod("goForward");
        } else {
            // Java implementation
            // TODO: Implement forward navigation in Java version
        }
    }
    
    /**
     * Refreshes the current page.
     */
    public void refresh() {
        if (usingJSImplementation) {
            jsWrapper.callMethod("refresh");
        } else {
            // Java implementation
            // TODO: Implement refresh in Java version
        }
    }
    
    @Override
    public HTMLElement render(HTMLElement parent) {
        if (usingJSImplementation) {
            // Use the JS implementation's render method
            JSObject jsParent = parent;
            JSObject result = jsWrapper.callMethod("render", jsParent);
            return (HTMLElement) result;
        } else {
            // Use the Java implementation's render method
            return super.render(parent);
        }
    }
}