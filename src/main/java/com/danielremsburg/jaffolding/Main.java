package com.danielremsburg.jaffolding;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.Event;

import java.util.function.Consumer;

import com.danielremsburg.jaffolding.bridge.JSBridge;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.TextField;
import com.danielremsburg.jaffolding.ui.Window;
import com.danielremsburg.jaffolding.ui.kde.AppManager;
import com.danielremsburg.jaffolding.ui.kde.KDEDesktop;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;
import com.danielremsburg.jaffolding.examples.AnimationDemo;
import com.danielremsburg.jaffolding.examples.ComponentDemo;
import com.danielremsburg.jaffolding.examples.ChartDemo;
import com.danielremsburg.jaffolding.examples.DesktopDemo;
import com.danielremsburg.jaffolding.examples.SalesDataDemo;
import com.danielremsburg.jaffolding.examples.ThreeJsDemo;

/**
 * Main entry point for the Jaffolding framework.
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
        
        // Initialize the application
        JaffoldingApp.initialize(root);
    }
    
    /**
     * Creates the main page.
     * @param router The router
     * @return The main page component
     */
    public static Component createMainPage(Router router) {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("Jaffolding Framework");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // Navigation
        Panel nav = new Panel();
        nav.setLayout(new GridLayout(2, 3, 20, 20));
        nav.setStyle("padding", "20px");
        
        // UI Components Demo
        Panel componentsCard = createNavCard(
            "UI Components",
            "Explore the various UI components available in the framework.",
            "#4285f4",
            e -> router.navigateTo("/components")
        );
        
        // Chart.js Demo
        Panel chartCard = createNavCard(
            "Chart.js Integration",
            "See how the framework integrates with Chart.js for data visualization.",
            "#ea4335",
            e -> router.navigateTo("/chart")
        );
        
        // Three.js Demo
        Panel threeCard = createNavCard(
            "Three.js Integration",
            "Explore 3D graphics capabilities with Three.js integration.",
            "#fbbc05",
            e -> router.navigateTo("/three")
        );
        
        // Sales Data Demo
        Panel salesCard = createNavCard(
            "Sales Data Visualization",
            "Interactive data visualization with reactive charts and tables.",
            "#34a853",
            e -> router.navigateTo("/sales")
        );
        
        // Desktop Demo
        Panel desktopCard = createNavCard(
            "Desktop Environment",
            "Draggable, resizable windows with a desktop-like interface.",
            "#673ab7",
            e -> router.navigateTo("/desktop")
        );
        
        // Animation Demo
        Panel animationCard = createNavCard(
            "Animation Demo",
            "Explore the animation capabilities of the framework.",
            "#e91e63",
            e -> router.navigateTo("/animation")
        );
        
        nav.addChild(componentsCard);
        nav.addChild(chartCard);
        nav.addChild(threeCard);
        nav.addChild(salesCard);
        nav.addChild(desktopCard);
        nav.addChild(animationCard);
        
        // Content
        Panel content = new Panel();
        content.setStyle("padding", "20px");
        
        Label intro = new Label("Welcome to Jaffolding - A Java UI Framework for the Web");
        intro.setStyle("font-size", "18px")
             .setStyle("margin-bottom", "20px");
        
        Label description = new Label(
            "Jaffolding is a Java framework that compiles to JavaScript using TeaVM. " +
            "It provides a familiar component-based API for Java developers to build web applications " +
            "without writing JavaScript. It also includes wrappers for popular JavaScript libraries."
        );
        description.setStyle("line-height", "1.5");
        
        content.addChild(intro);
        content.addChild(description);
        
        // Add components to the main panel
        mainPanel.addChild(header, BorderLayout.NORTH);
        mainPanel.addChild(nav, BorderLayout.CENTER);
        mainPanel.addChild(content, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Creates a navigation card.
     * @param title The card title
     * @param description The card description
     * @param color The card color
     * @param onClick The click handler
     * @return The card component
     */
    private static Panel createNavCard(String title, String description, String color, Consumer<Event> onClick) {
        Panel card = new Panel();
        card.setStyle("background-color", "white")
            .setStyle("border-radius", "8px")
            .setStyle("box-shadow", "0 2px 8px rgba(0, 0, 0, 0.1)")
            .setStyle("padding", "20px")
            .setStyle("cursor", "pointer")
            .setStyle("transition", "transform 0.2s, box-shadow 0.2s")
            .addEventListener("mouseover", e -> {
                card.setStyle("transform", "translateY(-5px)");
                card.setStyle("box-shadow", "0 8px 16px rgba(0, 0, 0, 0.1)");
            })
            .addEventListener("mouseout", e -> {
                card.setStyle("transform", "translateY(0)");
                card.setStyle("box-shadow", "0 2px 8px rgba(0, 0, 0, 0.1)");
            })
            .addEventListener("click", onClick);
        
        Panel iconCircle = new Panel();
        iconCircle.setStyle("width", "50px")
                  .setStyle("height", "50px")
                  .setStyle("border-radius", "25px")
                  .setStyle("background-color", color)
                  .setStyle("margin-bottom", "15px")
                  .setStyle("display", "flex")
                  .setStyle("align-items", "center")
                  .setStyle("justify-content", "center")
                  .setStyle("color", "white")
                  .setStyle("font-weight", "bold")
                  .setStyle("font-size", "20px");
        
        Label iconLabel = new Label(title.substring(0, 1));
        iconCircle.addChild(iconLabel);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("font-size", "18px")
                  .setStyle("font-weight", "bold")
                  .setStyle("margin-bottom", "10px");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("color", "#666")
                 .setStyle("font-size", "14px")
                 .setStyle("line-height", "1.4");
        
        card.addChild(iconCircle);
        card.addChild(titleLabel);
        card.addChild(descLabel);
        
        return card;
    }
}