package com.danielremsburg.jaffolding;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.Event;

/**
 * Wrapper for the HTML document object.
 * This class provides access to the browser's document object.
 */
public abstract class HTMLDocument implements JSObject {
    
    @JSBody(script = "return document;")
    public static native HTMLDocument current();
    
    @JSProperty
    public abstract HTMLElement getBody();
    
    public abstract HTMLElement createElement(String tagName);
    
    public abstract HTMLElement getElementById(String id);
    
    public abstract HTMLElement querySelector(String selector);
    
    public abstract Event createEvent(String eventType);
    
    public abstract void addEventListener(String type, EventListener listener);
    
    public abstract void removeEventListener(String type, EventListener listener);
    
    /**
     * Event listener interface.
     */
    public interface EventListener extends JSObject {
        void handleEvent(Event event);
    }
}