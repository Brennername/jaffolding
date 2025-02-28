package com.danielremsburg.jaffolding.bridge;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Bridge to access JavaScript components from Java.
 * This allows the Java framework to use JavaScript components when needed.
 */
public class JSBridge {
    
    /**
     * Checks if a JavaScript component is available.
     * @param componentPath The path to the component (e.g., "jaffolding.KDEDesktop")
     * @return True if the component is available, false otherwise
     */
    @JSBody(params = {"componentPath"}, script = 
        "try {" +
        "  let parts = componentPath.split('.');" +
        "  let obj = window;" +
        "  for (let i = 0; i < parts.length; i++) {" +
        "    obj = obj[parts[i]];" +
        "    if (obj === undefined) return false;" +
        "  }" +
        "  return typeof obj === 'function' || typeof obj === 'object';" +
        "} catch (e) {" +
        "  return false;" +
        "}")
    public static native boolean isComponentAvailable(String componentPath);
    
    /**
     * Creates a JavaScript component instance.
     * @param componentPath The path to the component (e.g., "jaffolding.KDEDesktop")
     * @param container The container element
     * @return The JavaScript object representing the component
     */
    @JSBody(params = {"componentPath", "container"}, script = 
        "try {" +
        "  let parts = componentPath.split('.');" +
        "  let Constructor = window;" +
        "  for (let i = 0; i < parts.length; i++) {" +
        "    Constructor = Constructor[parts[i]];" +
        "    if (Constructor === undefined) return null;" +
        "  }" +
        "  if (typeof Constructor === 'function') {" +
        "    return new Constructor(container);" +
        "  }" +
        "  return null;" +
        "} catch (e) {" +
        "  console.error('Error creating JS component:', e);" +
        "  return null;" +
        "}")
    public static native JSObject createComponent(String componentPath, HTMLElement container);
    
    /**
     * Calls a method on a JavaScript object.
     * @param object The JavaScript object
     * @param methodName The method name
     * @param args The method arguments
     * @return The result of the method call
     */
    @JSBody(params = {"object", "methodName", "args"}, script = 
        "try {" +
        "  if (object && typeof object[methodName] === 'function') {" +
        "    return object[methodName].apply(object, args || []);" +
        "  }" +
        "  return null;" +
        "} catch (e) {" +
        "  console.error('Error calling JS method:', e);" +
        "  return null;" +
        "}")
    public static native JSObject callMethod(JSObject object, String methodName, JSObject[] args);
    
    /**
     * Gets a property from a JavaScript object.
     * @param object The JavaScript object
     * @param propertyName The property name
     * @return The property value
     */
    @JSBody(params = {"object", "propertyName"}, script = 
        "try {" +
        "  if (object) {" +
        "    return object[propertyName];" +
        "  }" +
        "  return null;" +
        "} catch (e) {" +
        "  console.error('Error getting JS property:', e);" +
        "  return null;" +
        "}")
    public static native JSObject getProperty(JSObject object, String propertyName);
    
    /**
     * Sets a property on a JavaScript object.
     * @param object The JavaScript object
     * @param propertyName The property name
     * @param value The property value
     */
    @JSBody(params = {"object", "propertyName", "value"}, script = 
        "try {" +
        "  if (object) {" +
        "    object[propertyName] = value;" +
        "  }" +
        "} catch (e) {" +
        "  console.error('Error setting JS property:', e);" +
        "}")
    public static native void setProperty(JSObject object, String propertyName, JSObject value);
    
    /**
     * Creates an empty JavaScript array.
     * @return An empty JavaScript array
     */
    @JSBody(params = {}, script = "return [];")
    public static native JSObject createArray();
    
    /**
     * Creates a JavaScript object from a JSON string.
     * @param json The JSON string
     * @return The JavaScript object
     */
    @JSBody(params = {"json"}, script = "return JSON.parse(json);")
    public static native JSObject parseJSON(String json);
    
    /**
     * Converts a JavaScript object to a JSON string.
     * @param object The JavaScript object
     * @return The JSON string
     */
    @JSBody(params = {"object"}, script = "return JSON.stringify(object);")
    public static native String stringifyJSON(JSObject object);
}