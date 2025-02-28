package com.danielremsburg.jaffolding;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.bridge.ComponentFactory;
import com.danielremsburg.jaffolding.bridge.ComponentRegistry;
import com.danielremsburg.jaffolding.ui.kde.AppManager;
import com.danielremsburg.jaffolding.ui.kde.KDEDesktop;

/**
 * Main application class for the Jaffolding framework.
 * This class initializes the framework and sets up the application.
 */
public class JaffoldingApp {
    
    /**
     * Initializes the application.
     * @param root The root element
     */
    public static void initialize(HTMLElement root) {
        // Register Java components with the registry
        ComponentFactory.registerJavaComponents();
        
        // Check if we should use the KDE desktop environment
        if (ComponentRegistry.isJSComponentAvailable("kde-desktop")) {
            initializeKDEDesktop(root);
        } else {
            // Fall back to the original router-based navigation
            initializeRouterNavigation(root);
        }
    }
    
    /**
     * Initializes the KDE desktop environment.
     * @param root The root element
     */
    private static void initializeKDEDesktop(HTMLElement root) {
        // Create the desktop environment
        KDEDesktop desktop = ComponentFactory.createKDEDesktop(root);
        
        // Initialize the app manager
        AppManager appManager = ComponentFactory.createAppManager(desktop);
        
        // Register apps
        registerApps(appManager);
        
        // Create desktop icons
        desktop.createDesktopIcons(appManager.getApps());
        
        // Create welcome window
        createWelcomeWindow(desktop, appManager);
    }
    
    /**
     * Registers applications with the app manager.
     * @param appManager The app manager
     */
    private static void registerApps(AppManager appManager) {
        // Components app
        appManager.registerApp(
            "{" +
            "  \"id\": \"components\"," +
            "  \"name\": \"UI Components\"," +
            "  \"icon\": \"🧩\"," +
            "  \"color\": \"#5294e2\"" +
            "}"
        );
        
        // Chart app
        appManager.registerApp(
            "{" +
            "  \"id\": \"chart\"," +
            "  \"name\": \"Chart Demo\"," +
            "  \"icon\": \"📊\"," +
            "  \"color\": \"#ea4335\"" +
            "}"
        );
        
        // Three.js app
        appManager.registerApp(
            "{" +
            "  \"id\": \"three\"," +
            "  \"name\": \"3D Graphics\"," +
            "  \"icon\": \"🧊\"," +
            "  \"color\": \"#fbbc05\"" +
            "}"
        );
        
        // Sales app
        appManager.registerApp(
            "{" +
            "  \"id\": \"sales\"," +
            "  \"name\": \"Sales Dashboard\"," +
            "  \"icon\": \"📈\"," +
            "  \"color\": \"#34a853\"" +
            "}"
        );
        
        // Animation app
        appManager.registerApp(
            "{" +
            "  \"id\": \"animation\"," +
            "  \"name\": \"Animation Studio\"," +
            "  \"icon\": \"🎬\"," +
            "  \"color\": \"#e91e63\"" +
            "}"
        );
        
        // Calculator app
        appManager.registerApp(
            "{" +
            "  \"id\": \"calculator\"," +
            "  \"name\": \"Calculator\"," +
            "  \"icon\": \"🧮\"," +
            "  \"color\": \"#673ab7\"" +
            "}"
        );
    }
    
    /**
     * Creates a welcome window.
     * @param desktop The desktop
     * @param appManager The app manager
     */
    private static void createWelcomeWindow(KDEDesktop desktop, AppManager appManager) {
        Panel welcomeContent = new Panel();
        welcomeContent.setStyle("padding", "20px")
                      .setStyle("color", "#eceff4");
        
        Label welcomeTitle = new Label("Welcome to Jaffolding Desktop");
        welcomeTitle.setStyle("font-size", "24px")
                    .setStyle("font-weight", "bold")
                    .setStyle("margin-bottom", "15px");
        
        Label welcomeText = new Label(
            "This is a KDE Plasma-inspired desktop environment built with the Jaffolding framework. " +
            "Click on the desktop icons or dock items to launch applications."
        );
        welcomeText.setStyle("line-height", "1.5")
                   .setStyle("margin-bottom", "20px");
        
        Component welcomeList = new Component("ul");
        welcomeList.setStyle("margin-bottom", "20px")
                   .setStyle("padding-left", "20px");
        
        String[] listItems = {
            "UI Components - Explore various UI components",
            "Chart Demo - Interactive data visualization",
            "3D Graphics - Three.js integration",
            "Sales Dashboard - Data dashboard with browser controls",
            "Animation Studio - Animation capabilities",
            "Calculator - Functional calculator application"
        };
        
        for (String item : listItems) {
            Component li = new Component("li");
            li.setText(item)
              .setStyle("margin-bottom", "8px");
            welcomeList.addChild(li);
        }
        
        welcomeContent.addChild(welcomeTitle);
        welcomeContent.addChild(welcomeText);
        welcomeContent.addChild(welcomeList);
        
        // Create the window using the factory
        Window welcomeWindow = ComponentFactory.createAppWindow(
            "Welcome", 
            welcomeContent, 
            "{\"width\": 500, \"height\": 400, \"x\": 200, \"y\": 100}"
        );
        
        // Add the window to the desktop
        desktop.addWindow(welcomeWindow);
    }
    
    /**
     * Initializes the router-based navigation.
     * @param root The root element
     */
    private static void initializeRouterNavigation(HTMLElement root) {
        // Clear the loading message
        root.setInnerHTML("");
        
        // Create a router for navigation
        Router router = new Router(root);
        
        // Add routes
        router.addRoute("/", path -> Main.createMainPage(router))
              .addRoute("/components", path -> new ComponentDemo().createDemo())
              .addRoute("/chart", path -> new ChartDemo().createDemo())
              .addRoute("/three", path -> new ThreeJsDemo().createDemo())
              .addRoute("/sales", path -> new SalesDataDemo().createDemo())
              .addRoute("/desktop", path -> new DesktopDemo().createDemo())
              .addRoute("/animation", path -> new AnimationDemo().createDemo());
        
        // Start the router
        router.start();
    }
}