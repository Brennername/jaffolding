package com.danielremsburg.jaffolding.bridge;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Base class for wrapping JavaScript components.
 * This allows Java classes to delegate to JavaScript implementations when needed.
 */
public abstract class JSComponentWrapper {
    protected JSObject jsComponent;
    protected String componentPath;
    
    /**
     * Creates a new JSComponentWrapper.
     * @param componentPath The path to the JavaScript component
     */
    public JSComponentWrapper(String componentPath) {
        this.componentPath = componentPath;
    }
    
    /**
     * Initializes the JavaScript component.
     * @param container The container element
     * @return True if initialization was successful, false otherwise
     */
    protected boolean initJSComponent(HTMLElement container) {
        if (JSBridge.isComponentAvailable(componentPath)) {
            jsComponent = JSBridge.createComponent(componentPath, container);
            return jsComponent != null;
        }
        return false;
    }
    
    /**
     * Calls a method on the JavaScript component.
     * @param methodName The method name
     * @param args The method arguments
     * @return The result of the method call
     */
    protected JSObject callMethod(String methodName, JSObject... args) {
        if (jsComponent != null) {
            return JSBridge.callMethod(jsComponent, methodName, args);
        }
        return null;
    }
    
    /**
     * Gets a property from the JavaScript component.
     * @param propertyName The property name
     * @return The property value
     */
    protected JSObject getProperty(String propertyName) {
        if (jsComponent != null) {
            return JSBridge.getProperty(jsComponent, propertyName);
        }
        return null;
    }
    
    /**
     * Sets a property on the JavaScript component.
     * @param propertyName The property name
     * @param value The property value
     */
    protected void setProperty(String propertyName, JSObject value) {
        if (jsComponent != null) {
            JSBridge.setProperty(jsComponent, propertyName, value);
        }
    }
    
    /**
     * Checks if the JavaScript component is available.
     * @return True if the JavaScript component is available, false otherwise
     */
    public boolean isJSComponentAvailable() {
        return JSBridge.isComponentAvailable(componentPath);
    }
    
    /**
     * Gets the underlying JavaScript component.
     * @return The JavaScript component
     */
    public JSObject getJSComponent() {
        return jsComponent;
    }
}