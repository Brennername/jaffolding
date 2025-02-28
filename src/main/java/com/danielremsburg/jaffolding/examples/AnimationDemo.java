package com.danielremsburg.jaffolding.examples;

import java.util.Arrays;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.Animation;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.ComboBox;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;

/**
 * Demo of animation capabilities.
 */
public class AnimationDemo {
    
    public Component createDemo() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("Animation Demo");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // Content
        Panel content = new Panel();
        content.setPadding("20px");
        
        Label intro = new Label("This demo showcases the animation capabilities of the Jaffolding framework.");
        intro.setStyle("font-size", "16px")
             .setStyle("margin-bottom", "20px");
        
        // Animation demo panels
        Panel demoGrid = new Panel();
        demoGrid.setLayout(new GridLayout(2, 3, 20, 20));
        
        // Fade animations
        Panel fadePanel = createDemoPanel("Fade Animations");
        
        Panel fadeTarget = new Panel();
        fadeTarget.setStyle("width", "100%")
                  .setStyle("height", "100px")
                  .setStyle("background-color", "#4285f4")
                  .setStyle("border-radius", "4px")
                  .setStyle("display", "flex")
                  .setStyle("align-items", "center")
                  .setStyle("justify-content", "center")
                  .setStyle("color", "white")
                  .setStyle("font-weight", "bold");
        
        Label fadeLabel = new Label("Fade Target");
        fadeTarget.addChild(fadeLabel);
        
        Panel fadeControls = new Panel();
        fadeControls.setStyle("display", "flex")
                    .setStyle("gap", "10px")
                    .setStyle("margin-top", "10px");
        
        Button fadeInBtn = new Button("Fade In");
        fadeInBtn.addEventListener("click", e -> {
            Animation.fadeIn(fadeTarget, 0.5);
        });
        
        Button fadeOutBtn = new Button("Fade Out");
        fadeOutBtn.addEventListener("click", e -> {
            Animation.fadeOut(fadeTarget, 0.5);
        });
        
        fadeControls.addChild(fadeInBtn);
        fadeControls.addChild(fadeOutBtn);
        
        fadePanel.addChild(fadeTarget);
        fadePanel.addChild(fadeControls);
        
        // Slide animations
        Panel slidePanel = createDemoPanel("Slide Animations");
        
        Panel slideTarget = new Panel();
        slideTarget.setStyle("width", "100%")
                   .setStyle("height", "100px")
                   .setStyle("background-color", "#ea4335")
                   .setStyle("border-radius", "4px")
                   .setStyle("display", "flex")
                   .setStyle("align-items", "center")
                   .setStyle("justify-content", "center")
                   .setStyle("color", "white")
                   .setStyle("font-weight", "bold");
        
        Label slideLabel = new Label("Slide Target");
        slideTarget.addChild(slideLabel);
        
        Panel slideControls = new Panel();
        slideControls.setStyle("margin-top", "10px");
        
        Panel directionRow = new Panel();
        directionRow.setStyle("display", "flex")
                    .setStyle("gap", "10px")
                    .setStyle("margin-bottom", "10px");
        
        Label directionLabel = new Label("Direction:");
        ComboBox directionSelect = new ComboBox(Arrays.asList("left", "right", "top", "bottom"));
        
        directionRow.addChild(directionLabel);
        directionRow.addChild(directionSelect);
        
        Panel slideButtonRow = new Panel();
        slideButtonRow.setStyle("display", "flex")
                      .setStyle("gap", "10px");
        
        Button slideInBtn = new Button("Slide In");
        slideInBtn.addEventListener("click", e -> {
            Animation.slideIn(slideTarget, directionSelect.getSelectedItem(), 0.5);
        });
        
        Button slideOutBtn = new Button("Slide Out");
        slideOutBtn.addEventListener("click", e -> {
            Animation.slideOut(slideTarget, directionSelect.getSelectedItem(), 0.5);
        });
        
        slideButtonRow.addChild(slideInBtn);
        slideButtonRow.addChild(slideOutBtn);
        
        slideControls.addChild(directionRow);
        slideControls.addChild(slideButtonRow);
        
        slidePanel.addChild(slideTarget);
        slidePanel.addChild(slideControls);
        
        // Scale animations
        Panel scalePanel = createDemoPanel("Scale Animations");
        
        Panel scaleTarget = new Panel();
        scaleTarget.setStyle("width", "100px")
                   .setStyle("height", "100px")
                   .setStyle("background-color", "#fbbc05")
                   .setStyle("border-radius", "4px")
                   .setStyle("display", "flex")
                   .setStyle("align-items", "center")
                   .setStyle("justify-content", "center")
                   .setStyle("color", "white")
                   .setStyle("font-weight", "bold")
                   .setStyle("margin", "0 auto");
        
        Label scaleLabel = new Label("Scale Target");
        scaleTarget.addChild(scaleLabel);
        
        Panel scaleControls = new Panel();
        scaleControls.setStyle("display", "flex")
                     .setStyle("gap", "10px")
                     .setStyle("margin-top", "10px")
                     .setStyle("justify-content", "center");
        
        Button scaleUpBtn = new Button("Scale Up");
        scaleUpBtn.addEventListener("click", e -> {
            Animation.scale(scaleTarget, 1, 1.5, 0.5);
        });
        
        Button scaleDownBtn = new Button("Scale Down");
        scaleDownBtn.addEventListener("click", e -> {
            Animation.scale(scaleTarget, 1.5, 1, 0.5);
        });
        
        scaleControls.addChild(scaleUpBtn);
        scaleControls.addChild(scaleDownBtn);
        
        scalePanel.addChild(scaleTarget);
        scalePanel.addChild(scaleControls);
        
        // Rotate animations
        Panel rotatePanel = createDemoPanel("Rotate Animations");
        
        Panel rotateTarget = new Panel();
        rotateTarget.setStyle("width", "100px")
                    .setStyle("height", "100px")
                    .setStyle("background-color", "#34a853")
                    .setStyle("border-radius", "4px")
                    .setStyle("display", "flex")
                    .setStyle("align-items", "center")
                    .setStyle("justify-content", "center")
                    .setStyle("color", "white")
                    .setStyle("font-weight", "bold")
                    .setStyle("margin", "0 auto");
        
        Label rotateLabel = new Label("Rotate Target");
        rotateTarget.addChild(rotateLabel);
        
        Panel rotateControls = new Panel();
        rotateControls.setStyle("display", "flex")
                      .setStyle("gap", "10px")
                      .setStyle("margin-top", "10px")
                      .setStyle("justify-content", "center");
        
        Button rotateCWBtn = new Button("Rotate CW");
        rotateCWBtn.addEventListener("click", e -> {
            Animation.rotate(rotateTarget, 0, 360, 1);
        });
        
        Button rotateCCWBtn = new Button("Rotate CCW");
        rotateCCWBtn.addEventListener("click", e -> {
            Animation.rotate(rotateTarget, 0, -360, 1);
        });
        
        rotateControls.addChild(rotateCWBtn);
        rotateControls.addChild(rotateCCWBtn);
        
        rotatePanel.addChild(rotateTarget);
        rotatePanel.addChild(rotateControls);
        
        // Combined animations
        Panel combinedPanel = createDemoPanel("Combined Animations");
        
        Panel combinedTarget = new Panel();
        combinedTarget.setStyle("width", "100px")
                      .setStyle("height", "100px")
                      .setStyle("background-color", "#673ab7")
                      .setStyle("border-radius", "4px")
                      .setStyle("display", "flex")
                      .setStyle("align-items", "center")
                      .setStyle("justify-content", "center")
                      .setStyle("color", "white")
                      .setStyle("font-weight", "bold")
                      .setStyle("margin", "0 auto");
        
        Label combinedLabel = new Label("Combined");
        combinedTarget.addChild(combinedLabel);
        
        Panel combinedControls = new Panel();
        combinedControls.setStyle("display", "flex")
                        .setStyle("gap", "10px")
                        .setStyle("margin-top", "10px")
                        .setStyle("justify-content", "center");
        
        Button entranceBtn = new Button("Entrance");
        entranceBtn.addEventListener("click", e -> {
            combinedTarget.setStyle("opacity", "0");
            combinedTarget.setStyle("transform", "scale(0.5) translateY(20px)");
            
            Animation.setTimeout(() -> {
                combinedTarget.setStyle("transition", "opacity 0.5s ease, transform 0.5s ease");
                combinedTarget.setStyle("opacity", "1");
                combinedTarget.setStyle("transform", "scale(1) translateY(0)");
            }, 10);
        });
        
        Button bounceBtn = new Button("Bounce");
        bounceBtn.addEventListener("click", e -> {
            combinedTarget.setStyle("transition", "transform 0.2s ease");
            combinedTarget.setStyle("transform", "scale(0.8) translateY(0)");
            
            Animation.setTimeout(() -> {
                combinedTarget.setStyle("transition", "transform 0.2s ease");
                combinedTarget.setStyle("transform", "scale(1.1) translateY(-10px)");
                
                Animation.setTimeout(() -> {
                    combinedTarget.setStyle("transition", "transform 0.2s ease");
                    combinedTarget.setStyle("transform", "scale(0.9) translateY(5px)");
                    
                    Animation.setTimeout(() -> {
                        combinedTarget.setStyle("transition", "transform 0.2s ease");
                        combinedTarget.setStyle("transform", "scale(1) translateY(0)");
                    }, 200);
                }, 200);
            }, 200);
        });
        
        combinedControls.addChild(entranceBtn);
        combinedControls.addChild(bounceBtn);
        
        combinedPanel.addChild(combinedTarget);
        combinedPanel.addChild(combinedControls);
        
        // GSAP integration
        Panel gsapPanel = createDemoPanel("GSAP Integration");
        
        Panel gsapTarget = new Panel();
        gsapTarget.setStyle("width", "100px")
                  .setStyle("height", "100px")
                  .setStyle("background-color", "#e91e63")
                  .setStyle("border-radius", "4px")
                  .setStyle("display", "flex")
                  .setStyle("align-items", "center")
                  .setStyle("justify-content", "center")
                  .setStyle("color", "white")
                  .setStyle("font-weight", "bold")
                  .setStyle("margin", "0 auto");
        
        Label gsapLabel = new Label("GSAP Target");
        gsapTarget.addChild(gsapLabel);
        
        Panel gsapControls = new Panel();
        gsapControls.setStyle("display", "flex")
                    .setStyle("gap", "10px")
                    .setStyle("margin-top", "10px")
                    .setStyle("justify-content", "center");
        
        Button timelineBtn = new Button("Timeline");
        timelineBtn.addEventListener("click", e -> {
            // This will use GSAP if available, otherwise fall back to CSS transitions
            Animation.animate(gsapTarget, "x", 100, 0.5);
            Animation.setTimeout(() -> {
                Animation.animate(gsapTarget, "y", 50, 0.5);
                Animation.setTimeout(() -> {
                    Animation.animate(gsapTarget, "rotation", 360, 0.5);
                    Animation.setTimeout(() -> {
                        Animation.animate(gsapTarget, "x", 0, 0.5);
                        Animation.animate(gsapTarget, "y", 0, 0.5);
                        Animation.animate(gsapTarget, "rotation", 0, 0.5);
                    }, 600);
                }, 600);
            }, 600);
        });
        
        Button shakeBtn = new Button("Shake");
        shakeBtn.addEventListener("click", e -> {
            gsapTarget.setStyle("transition", "transform 0.1s ease");
            gsapTarget.setStyle("transform", "translateX(-10px)");
            
            Animation.setTimeout(() -> {
                gsapTarget.setStyle("transform", "translateX(10px)");
                Animation.setTimeout(() -> {
                    gsapTarget.setStyle("transform", "translateX(-8px)");
                    Animation.setTimeout(() -> {
                        gsapTarget.setStyle("transform", "translateX(8px)");
                        Animation.setTimeout(() -> {
                            gsapTarget.setStyle("transform", "translateX(-5px)");
                            Animation.setTimeout(() -> {
                                gsapTarget.setStyle("transform", "translateX(5px)");
                                Animation.setTimeout(() -> {
                                    gsapTarget.setStyle("transform", "translateX(0)");
                                }, 100);
                            }, 100);
                        }, 100);
                    }, 100);
                }, 100);
            }, 100);
        });
        
        gsapControls.addChild(timelineBtn);
        gsapControls.addChild(shakeBtn);
        
        gsapPanel.addChild(gsapTarget);
        gsapPanel.addChild(gsapControls);
        
        // Add all panels to the grid
        demoGrid.addChild(fadePanel);
        demoGrid.addChild(slidePanel);
        demoGrid.addChild(scalePanel);
        demoGrid.addChild(rotatePanel);
        demoGrid.addChild(combinedPanel);
        demoGrid.addChild(gsapPanel);
        
        content.addChild(intro);
        content.addChild(demoGrid);
        
        mainPanel.addChild(header, BorderLayout.NORTH);
        mainPanel.addChild(content, BorderLayout.CENTER);
        
        // Apply entrance animation to the whole demo
        mainPanel.addEventListener("DOMNodeInserted", e -> {
            if (mainPanel.getElement() != null) {
                Animation.fadeIn(header, 0.5);
                Animation.slideIn(content, "bottom", 0.5);
            }
        });
        
        return mainPanel;
    }
    
    private Panel createDemoPanel(String title) {
        Panel panel = new Panel();
        panel.setStyle("background-color", "white")
             .setStyle("border-radius", "4px")
             .setStyle("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
             .setStyle("padding", "15px");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("font-size", "16px")
                  .setStyle("font-weight", "bold")
                  .setStyle("margin-bottom", "15px");
        
        panel.addChild(titleLabel);
        
        return panel;
    }
}