package com.danielremsburg.jaffolding.bridge;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.BrowserWindow;
import com.danielremsburg.jaffolding.ui.Calculator;
import com.danielremsburg.jaffolding.ui.Window;
import com.danielremsburg.jaffolding.ui.kde.AppManager;
import com.danielremsburg.jaffolding.ui.kde.KDEDesktop;

/**
 * Factory for creating components.
 * This class provides a unified way to create components, whether they are Java or JavaScript implementations.
 */
public class ComponentFactory {
    
    /**
     * Creates a KDE desktop.
     * @param container The container element
     * @return The KDE desktop
     */
    public static KDEDesktop createKDEDesktop(HTMLElement container) {
        // Check if the component is registered in the registry
        if (ComponentRegistry.isComponentRegistered("kde-desktop")) {
            // Try to create the component using the registry
            JSObject[] args = { container };
            JSObject component = ComponentRegistry.createComponent("kde-desktop", args);
            
            if (component != null) {
                // Create a wrapper for the component
                return new KDEDesktop(container);
            }
        }
        
        // Fall back to direct creation
        return new KDEDesktop(container);
    }
    
    /**
     * Creates an app manager.
     * @param desktop The desktop
     * @return The app manager
     */
    public static AppManager createAppManager(KDEDesktop desktop) {
        // Check if the component is registered in the registry
        if (ComponentRegistry.isComponentRegistered("app-manager")) {
            // Try to create the component using the registry
            JSObject[] args = { desktop.getJSComponent() };
            JSObject component = ComponentRegistry.createComponent("app-manager", args);
            
            if (component != null) {
                // Create a wrapper for the component
                return new AppManager(desktop);
            }
        }
        
        // Fall back to direct creation
        return new AppManager(desktop);
    }
    
    /**
     * Creates a browser window.
     * @param title The window title
     * @param url The initial URL
     * @return The browser window
     */
    public static BrowserWindow createBrowserWindow(String title, String url) {
        // Check if the component is registered in the registry
        if (ComponentRegistry.isComponentRegistered("browser-window")) {
            // Try to create the component using the registry
            JSObject jsTitle = JSBridge.parseJSON("\"" + title + "\"");
            JSObject jsUrl = JSBridge.parseJSON("\"" + url + "\"");
            JSObject[] args = { jsTitle, jsUrl };
            JSObject component = ComponentRegistry.createComponent("browser-window", args);
            
            if (component != null) {
                // Create a wrapper for the component
                return new BrowserWindow(title, url);
            }
        }
        
        // Fall back to direct creation
        return new BrowserWindow(title, url);
    }
    
    /**
     * Creates a calculator.
     * @return The calculator
     */
    public static Calculator createCalculator() {
        // Check if the component is registered in the registry
        if (ComponentRegistry.isComponentRegistered("calculator")) {
            // Try to create the component using the registry
            JSObject[] args = {};
            JSObject component = ComponentRegistry.createComponent("calculator", args);
            
            if (component != null) {
                // Create a wrapper for the component
                return new Calculator();
            }
        }
        
        // Fall back to direct creation
        return new Calculator();
    }
    
    /**
     * Creates an application window.
     * @param title The window title
     * @param content The window content
     * @param options The window options as a JSON string
     * @return The window
     */
    public static Window createAppWindow(String title, Component content, String options) {
        // Parse options
        JSObject jsOptions = JSBridge.parseJSON(options);
        
        // Create the window
        Window window = new Window(title, content);
        
        // Apply options
        JSObject jsWidth = JSBridge.getProperty(jsOptions, "width");
        JSObject jsHeight = JSBridge.getProperty(jsOptions, "height");
        JSObject jsX = JSBridge.getProperty(jsOptions, "x");
        JSObject jsY = JSBridge.getProperty(jsOptions, "y");
        
        if (jsWidth != null && jsHeight != null) {
            int width = Integer.parseInt(JSBridge.stringifyJSON(jsWidth));
            int height = Integer.parseInt(JSBridge.stringifyJSON(jsHeight));
            window.setSize(width, height);
        }
        
        if (jsX != null && jsY != null) {
            int x = Integer.parseInt(JSBridge.stringifyJSON(jsX));
            int y = Integer.parseInt(JSBridge.stringifyJSON(jsY));
            window.setPosition(x, y);
        }
        
        return window;
    }
    
    /**
     * Registers all Java components with the registry.
     */
    public static void registerJavaComponents() {
        // Register KDEDesktop
        ComponentRegistry.registerJavaComponent("kde-desktop", "com.danielremsburg.jaffolding.ui.kde.KDEDesktop");
        
        // Register AppManager
        ComponentRegistry.registerJavaComponent("app-manager", "com.danielremsburg.jaffolding.ui.kde.AppManager");
        
        // Register BrowserWindow
        ComponentRegistry.registerJavaComponent("browser-window", "com.danielremsburg.jaffolding.ui.BrowserWindow");
        
        // Register Calculator
        ComponentRegistry.registerJavaComponent("calculator", "com.danielremsburg.jaffolding.ui.Calculator");
    }
}