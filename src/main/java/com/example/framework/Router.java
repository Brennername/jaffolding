package com.example.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.teavm.jso.browser.Location;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Simple client-side router for the framework.
 */
public class Router {
    private static final Window window = Window.current();
    private HTMLElement container;
    private Map<String, Function<String, Component>> routes = new HashMap<>();
    private String currentPath;
    
    public Router(HTMLElement container) {
        this.container = container;
        
        // Listen for popstate events (back/forward navigation)
        window.addEventListener("popstate", evt -> {
            navigateTo(getPath());
        });
    }
    
    public Router addRoute(String path, Function<String, Component> componentFactory) {
        routes.put(path, componentFactory);
        return this;
    }
    
    public void start() {
        // Handle initial route
        navigateTo(getPath());
        
        // Intercept link clicks for client-side routing
        window.addEventListener("click", evt -> {
            HTMLElement target = (HTMLElement) evt.getTarget();
            if ("A".equalsIgnoreCase(target.getTagName()) && 
                target.hasAttribute("href") && 
                !target.getAttribute("href").startsWith("http")) {
                
                evt.preventDefault();
                navigateTo(target.getAttribute("href"));
            }
        });
    }
    
    public void navigateTo(String path) {
        if (path.equals(currentPath)) {
            return;
        }
        
        currentPath = path;
        
        // Update browser history
        window.getHistory().pushState(null, "", path);
        
        // Find matching route
        Function<String, Component> componentFactory = null;
        String matchedPath = null;
        
        // First try exact match
        if (routes.containsKey(path)) {
            componentFactory = routes.get(path);
            matchedPath = path;
        } else {
            // Try pattern matching (simple implementation)
            for (Map.Entry<String, Function<String, Component>> entry : routes.entrySet()) {
                String routePath = entry.getKey();
                if (routePath.contains(":") && pathMatches(routePath, path)) {
                    componentFactory = entry.getValue();
                    matchedPath = routePath;
                    break;
                }
            }
            
            // Fallback to "/" or 404
            if (componentFactory == null) {
                componentFactory = routes.getOrDefault("/", p -> new Component("div")
                    .setText("404 - Page not found"));
                matchedPath = "/";
            }
        }
        
        // Clear container
        while (container.getFirstChild() != null) {
            container.removeChild(container.getFirstChild());
        }
        
        // Render new component
        Component component = componentFactory.apply(path);
        component.render(container);
    }
    
    private String getPath() {
        Location location = window.getLocation();
        String path = location.getPathname();
        return path.isEmpty() ? "/" : path;
    }
    
    private boolean pathMatches(String routePath, String actualPath) {
        String[] routeParts = routePath.split("/");
        String[] actualParts = actualPath.split("/");
        
        if (routeParts.length != actualParts.length) {
            return false;
        }
        
        for (int i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith(":")) {
                // This is a parameter, it matches anything
                continue;
            }
            
            if (!routeParts[i].equals(actualParts[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    public String getParameter(String path, String paramName) {
        for (String routePath : routes.keySet()) {
            if (routePath.contains(":") && pathMatches(routePath, path)) {
                String[] routeParts = routePath.split("/");
                String[] actualParts = path.split("/");
                
                for (int i = 0; i < routeParts.length; i++) {
                    if (routeParts[i].startsWith(":") && 
                        routeParts[i].substring(1).equals(paramName)) {
                        return actualParts[i];
                    }
                }
            }
        }
        
        return null;
    }
}