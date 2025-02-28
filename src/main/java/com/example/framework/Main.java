package com.example.framework;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Main entry point for the Java front-end framework.
 */
public class Main {
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement root = document.getElementById("app");
        
        if (root == null) {
            root = document.createElement("div");
            root.setAttribute("id", "app");
            document.getBody().appendChild(root);
        }
        
        // Create a simple component
        Component app = new Component("div")
            .addChild(new Component("h1").setText("Java TeaVM Framework"))
            .addChild(new Component("p").setText("A Java front-end framework running in WebAssembly"))
            .addChild(new Component("button")
                .setText("Click me")
                .setStyle("padding", "10px")
                .setStyle("background-color", "#4CAF50")
                .setStyle("color", "white")
                .setStyle("border", "none")
                .setStyle("border-radius", "4px")
                .setStyle("cursor", "pointer")
                .addEventListener("click", event -> {
                    alert("Button clicked!");
                }));
        
        // Render the component to the DOM
        app.render(root);
    }
    
    private static native void alert(String message) /*-{
        window.alert(message);
    }-*/;
}