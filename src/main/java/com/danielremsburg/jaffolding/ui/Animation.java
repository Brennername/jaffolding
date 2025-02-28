package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Animation utility for UI components.
 */
public class Animation {
    
    /**
     * Animates a component with a fade-in effect.
     */
    public static void fadeIn(UIComponent component, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            element.getStyle().setProperty("opacity", "0");
            element.getStyle().setProperty("transition", "opacity " + duration + "s ease");
            
            // Use setTimeout to ensure the transition works
            setTimeout(() -> {
                element.getStyle().setProperty("opacity", "1");
            }, 10);
        }
    }
    
    /**
     * Animates a component with a fade-out effect.
     */
    public static void fadeOut(UIComponent component, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            element.getStyle().setProperty("opacity", "1");
            element.getStyle().setProperty("transition", "opacity " + duration + "s ease");
            
            setTimeout(() -> {
                element.getStyle().setProperty("opacity", "0");
            }, 10);
        }
    }
    
    /**
     * Animates a component with a slide-in effect.
     */
    public static void slideIn(UIComponent component, String direction, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            
            String transform = "";
            switch (direction.toLowerCase()) {
                case "left":
                    transform = "translateX(-100%)";
                    break;
                case "right":
                    transform = "translateX(100%)";
                    break;
                case "top":
                    transform = "translateY(-100%)";
                    break;
                case "bottom":
                    transform = "translateY(100%)";
                    break;
                default:
                    transform = "translateY(20px)";
            }
            
            element.getStyle().setProperty("transform", transform);
            element.getStyle().setProperty("opacity", "0");
            element.getStyle().setProperty("transition", "transform " + duration + "s ease, opacity " + duration + "s ease");
            
            setTimeout(() -> {
                element.getStyle().setProperty("transform", "translate(0, 0)");
                element.getStyle().setProperty("opacity", "1");
            }, 10);
        }
    }
    
    /**
     * Animates a component with a slide-out effect.
     */
    public static void slideOut(UIComponent component, String direction, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            
            String transform = "";
            switch (direction.toLowerCase()) {
                case "left":
                    transform = "translateX(-100%)";
                    break;
                case "right":
                    transform = "translateX(100%)";
                    break;
                case "top":
                    transform = "translateY(-100%)";
                    break;
                case "bottom":
                    transform = "translateY(100%)";
                    break;
                default:
                    transform = "translateY(20px)";
            }
            
            element.getStyle().setProperty("transform", "translate(0, 0)");
            element.getStyle().setProperty("opacity", "1");
            element.getStyle().setProperty("transition", "transform " + duration + "s ease, opacity " + duration + "s ease");
            
            setTimeout(() -> {
                element.getStyle().setProperty("transform", transform);
                element.getStyle().setProperty("opacity", "0");
            }, 10);
        }
    }
    
    /**
     * Animates a component with a scale effect.
     */
    public static void scale(UIComponent component, double fromScale, double toScale, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            element.getStyle().setProperty("transform", "scale(" + fromScale + ")");
            element.getStyle().setProperty("transition", "transform " + duration + "s ease");
            
            setTimeout(() -> {
                element.getStyle().setProperty("transform", "scale(" + toScale + ")");
            }, 10);
        }
    }
    
    /**
     * Animates a component with a rotate effect.
     */
    public static void rotate(UIComponent component, double fromDegrees, double toDegrees, double duration) {
        if (component.getElement() != null) {
            HTMLElement element = component.getElement();
            element.getStyle().setProperty("transform", "rotate(" + fromDegrees + "deg)");
            element.getStyle().setProperty("transition", "transform " + duration + "s ease");
            
            setTimeout(() -> {
                element.getStyle().setProperty("transform", "rotate(" + toDegrees + "deg)");
            }, 10);
        }
    }
    
    /**
     * Animates a property of a component using GSAP (if available).
     */
    public static void animate(UIComponent component, String property, Object value, double duration) {
        if (component.getElement() != null) {
            try {
                gsapTo(component.getElement(), duration, property, value);
            } catch (Exception e) {
                // Fallback to CSS transitions if GSAP is not available
                HTMLElement element = component.getElement();
                element.getStyle().setProperty("transition", property + " " + duration + "s ease");
                element.getStyle().setProperty(property, value.toString());
            }
        }
    }
    
    /**
     * Sets a timeout to execute a callback after a delay.
     */
    @JSBody(params = {"callback", "delay"}, script = "setTimeout(callback, delay);")
    public static native void setTimeout(Runnable callback, int delay);
    
    /**
     * Calls GSAP's to() method to animate an element.
     */
    @JSBody(params = {"element", "duration", "property", "value"}, 
            script = "try { gsap.to(element, {duration: duration, [property]: value}); } catch(e) { console.warn('GSAP not available'); }")
    private static native void gsapTo(Object element, double duration, String property, Object value);
}