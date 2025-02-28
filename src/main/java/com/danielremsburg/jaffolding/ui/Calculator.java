package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.bridge.JSBridge;
import com.danielremsburg.jaffolding.bridge.JSComponentWrapper;

/**
 * A calculator component.
 * This class can use either the Java implementation or delegate to the JavaScript implementation.
 */
public class Calculator extends Component {
    private JSComponentWrapper jsWrapper;
    private boolean usingJSImplementation;
    
    /**
     * Creates a new Calculator instance.
     */
    public Calculator() {
        super("div");
        
        // Create a wrapper for the JS component
        jsWrapper = new JSComponentWrapper("jaffolding.Calculator") {
            @Override
            protected boolean initJSComponent(HTMLElement container) {
                if (JSBridge.isComponentAvailable(componentPath)) {
                    jsComponent = JSBridge.createComponent(componentPath, null);
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
     * Initializes the Java implementation of the calculator.
     */
    private void initJavaImplementation() {
        // TODO: Implement Java version of Calculator
        System.out.println("Using Java implementation of Calculator");
        
        // Set basic styles
        setStyle("width", "100%")
            .setStyle("height", "100%")
            .setStyle("background-color", "#2e3440")
            .setStyle("border-radius", "4px")
            .setStyle("overflow", "hidden")
            .setStyle("color", "#eceff4");
        
        // Create display
        Component displayContainer = new Component("div");
        displayContainer.setStyle("background-color", "#3b4252")
                        .setStyle("padding", "15px")
                        .setStyle("margin", "10px")
                        .setStyle("border-radius", "4px")
                        .setStyle("box-shadow", "inset 0 0 5px rgba(0, 0, 0, 0.2)")
                        .setStyle("text-align", "right")
                        .setStyle("font-family", "monospace")
                        .setStyle("font-size", "24px")
                        .setStyle("height", "60px")
                        .setStyle("display", "flex")
                        .setStyle("align-items", "center")
                        .setStyle("justify-content", "flex-end")
                        .setStyle("color", "#eceff4");
        
        Component displayElement = new Component("div");
        displayElement.setText("0");
        displayContainer.addChild(displayElement);
        
        // Create keypad
        Component keypad = new Component("div");
        keypad.setStyle("display", "grid")
              .setStyle("grid-template-columns", "repeat(4, 1fr)")
              .setStyle("grid-gap", "10px")
              .setStyle("padding", "10px")
              .setStyle("flex", "1");
        
        // Add buttons
        // TODO: Add calculator buttons and functionality
        
        // Add components to calculator
        addChild(displayContainer);
        addChild(keypad);
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