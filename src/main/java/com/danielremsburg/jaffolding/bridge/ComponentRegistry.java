package com.danielremsburg.jaffolding.bridge;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

/**
 * Registry for mapping between Java and JavaScript components.
 * This allows components to be shared between the two environments.
 */
public class ComponentRegistry {
    
    /**
     * Registers a Java component implementation.
     * @param id The component identifier
     * @param className The fully qualified Java class name
     */
    public static void registerJavaComponent(String id, String className) {
        registerJavaComponentImpl(id, className, "{}");
    }
    
    /**
     * Registers a Java component implementation with options.
     * @param id The component identifier
     * @param className The fully qualified Java class name
     * @param options The registration options as a JSON string
     */
    public static void registerJavaComponent(String id, String className, String options) {
        registerJavaComponentImpl(id, className, options);
    }
    
    /**
     * Gets a component implementation.
     * @param id The component identifier
     * @return The component implementation info as a JSObject
     */
    public static JSObject getComponent(String id) {
        return getComponentImpl(id, null);
    }
    
    /**
     * Gets a component implementation with a preferred type.
     * @param id The component identifier
     * @param preferredType The preferred implementation type ('js' or 'java')
     * @return The component implementation info as a JSObject
     */
    public static JSObject getComponent(String id, String preferredType) {
        return getComponentImpl(id, preferredType);
    }
    
    /**
     * Creates a component instance.
     * @param id The component identifier
     * @param args The constructor arguments as a JSObject array
     * @return The component instance as a JSObject
     */
    public static JSObject createComponent(String id, JSObject[] args) {
        return createComponentImpl(id, args, "{}");
    }
    
    /**
     * Creates a component instance with options.
     * @param id The component identifier
     * @param args The constructor arguments as a JSObject array
     * @param options The creation options as a JSON string
     * @return The component instance as a JSObject
     */
    public static JSObject createComponent(String id, JSObject[] args, String options) {
        return createComponentImpl(id, args, options);
    }
    
    /**
     * Enables or disables the bridge.
     * @param enabled Whether the bridge is enabled
     */
    public static void setBridgeEnabled(boolean enabled) {
        setBridgeEnabledImpl(enabled);
    }
    
    /**
     * Checks if the bridge is enabled.
     * @return Whether the bridge is enabled
     */
    public static boolean isBridgeEnabled() {
        return isBridgeEnabledImpl();
    }
    
    /**
     * Checks if a component is registered.
     * @param id The component identifier
     * @return Whether the component is registered
     */
    public static boolean isComponentRegistered(String id) {
        return isComponentRegisteredImpl(id);
    }
    
    /**
     * Checks if a JavaScript component is available.
     * @param id The component identifier
     * @return Whether the JavaScript component is available
     */
    public static boolean isJSComponentAvailable(String id) {
        return isJSComponentAvailableImpl(id);
    }
    
    /**
     * Checks if a Java component is available.
     * @param id The component identifier
     * @return Whether the Java component is available
     */
    public static boolean isJavaComponentAvailable(String id) {
        return isJavaComponentAvailableImpl(id);
    }
    
    // Native implementations
    
    @JSBody(params = {"id", "className", "options"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    window.jaffolding.ComponentRegistry.getInstance().registerJavaComponent(id, className, JSON.parse(options));" +
        "    return true;" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error registering Java component:', e);" +
        "  return false;" +
        "}")
    private static native boolean registerJavaComponentImpl(String id, String className, String options);
    
    @JSBody(params = {"id", "preferredType"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    return window.jaffolding.ComponentRegistry.getInstance().getComponent(id, preferredType);" +
        "  }" +
        "  return null;" +
        "} catch (e) {" +
        "  console.error('Error getting component:', e);" +
        "  return null;" +
        "}")
    private static native JSObject getComponentImpl(String id, String preferredType);
    
    @JSBody(params = {"id", "args", "options"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    return window.jaffolding.ComponentRegistry.getInstance().createComponent(id, args, JSON.parse(options));" +
        "  }" +
        "  return null;" +
        "} catch (e) {" +
        "  console.error('Error creating component:', e);" +
        "  return null;" +
        "}")
    private static native JSObject createComponentImpl(String id, JSObject[] args, String options);
    
    @JSBody(params = {"enabled"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    window.jaffolding.ComponentRegistry.getInstance().setBridgeEnabled(enabled);" +
        "    return true;" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error setting bridge enabled:', e);" +
        "  return false;" +
        "}")
    private static native boolean setBridgeEnabledImpl(boolean enabled);
    
    @JSBody(params = {}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    return window.jaffolding.ComponentRegistry.getInstance().isBridgeEnabled();" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error checking if bridge is enabled:', e);" +
        "  return false;" +
        "}")
    private static native boolean isBridgeEnabledImpl();
    
    @JSBody(params = {"id"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    return window.jaffolding.ComponentRegistry.getInstance().getComponent(id) !== null;" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error checking if component is registered:', e);" +
        "  return false;" +
        "}")
    private static native boolean isComponentRegisteredImpl(String id);
    
    @JSBody(params = {"id"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    const component = window.jaffolding.ComponentRegistry.getInstance().getComponent(id, 'js');" +
        "    return component !== null && component.type === 'js';" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error checking if JS component is available:', e);" +
        "  return false;" +
        "}")
    private static native boolean isJSComponentAvailableImpl(String id);
    
    @JSBody(params = {"id"}, script = 
        "try {" +
        "  if (window.jaffolding && window.jaffolding.ComponentRegistry) {" +
        "    const component = window.jaffolding.ComponentRegistry.getInstance().getComponent(id, 'java');" +
        "    return component !== null && component.type === 'java';" +
        "  }" +
        "  return false;" +
        "} catch (e) {" +
        "  console.error('Error checking if Java component is available:', e);" +
        "  return false;" +
        "}")
    private static native boolean isJavaComponentAvailableImpl(String id);
}