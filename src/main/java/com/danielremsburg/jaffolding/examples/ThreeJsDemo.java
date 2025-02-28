package com.danielremsburg.jaffolding.examples;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.js.ThreeJS;
import com.danielremsburg.jaffolding.js.ThreeJS.AmbientLight;
import com.danielremsburg.jaffolding.js.ThreeJS.BoxGeometry;
import com.danielremsburg.jaffolding.js.ThreeJS.DirectionalLight;
import com.danielremsburg.jaffolding.js.ThreeJS.Mesh;
import com.danielremsburg.jaffolding.js.ThreeJS.MeshPhongMaterial;
import com.danielremsburg.jaffolding.js.ThreeJS.PerspectiveCamera;
import com.danielremsburg.jaffolding.js.ThreeJS.Scene;
import com.danielremsburg.jaffolding.js.ThreeJS.SphereGeometry;
import com.danielremsburg.jaffolding.js.ThreeJS.WebGLRenderer;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.ComboBox;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;

import java.util.Arrays;

/**
 * Demo of Three.js integration with the Jaffolding framework.
 */
public class ThreeJsDemo {
    
    private Scene scene;
    private PerspectiveCamera camera;
    private WebGLRenderer renderer;
    private Mesh currentMesh;
    private String currentShape = "cube";
    private String currentColor = "#4285f4";
    private boolean isAnimating = true;
    private double rotationSpeed = 0.01;
    
    public Component createDemo() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("Three.js Integration Demo");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // 3D scene container
        Panel sceneContainer = new Panel();
        sceneContainer.setStyle("padding", "20px")
                      .setStyle("display", "flex")
                      .setStyle("flex-direction", "column")
                      .setStyle("align-items", "center");
        
        // Container for the Three.js renderer
        Panel rendererContainer = new Panel();
        rendererContainer.setStyle("width", "800px")
                         .setStyle("height", "400px")
                         .setStyle("max-width", "100%")
                         .setStyle("background-color", "#f5f5f5")
                         .setStyle("border-radius", "4px")
                         .setStyle("overflow", "hidden");
        
        // Controls
        Panel controls = new Panel();
        controls.setLayout(new GridLayout(1, 4, 20, 0));
        controls.setStyle("margin-top", "20px")
                .setStyle("width", "100%")
                .setStyle("max-width", "800px");
        
        // Shape selector
        Panel shapePanel = new Panel();
        shapePanel.addChild(new Label("Shape:").setStyle("margin-bottom", "8px"));
        
        ComboBox shapeSelector = new ComboBox(Arrays.asList("cube", "sphere"));
        shapeSelector.setSelectedItem("cube");
        shapeSelector.setOnSelectionChange(index -> {
            currentShape = shapeSelector.getSelectedItem();
            updateShape();
        });
        
        shapePanel.addChild(shapeSelector);
        
        // Color selector
        Panel colorPanel = new Panel();
        colorPanel.addChild(new Label("Color:").setStyle("margin-bottom", "8px"));
        
        ComboBox colorSelector = new ComboBox(Arrays.asList(
            "Blue (#4285f4)", 
            "Red (#ea4335)", 
            "Green (#34a853)", 
            "Yellow (#fbbc05)"
        ));
        colorSelector.setSelectedIndex(0);
        colorSelector.setOnSelectionChange(index -> {
            switch (index) {
                case 0: currentColor = "#4285f4"; break;
                case 1: currentColor = "#ea4335"; break;
                case 2: currentColor = "#34a853"; break;
                case 3: currentColor = "#fbbc05"; break;
                default: currentColor = "#4285f4";
            }
            updateShape();
        });
        
        colorPanel.addChild(colorSelector);
        
        // Animation control
        Panel animationPanel = new Panel();
        animationPanel.addChild(new Label("Animation:").setStyle("margin-bottom", "8px"));
        
        Button animationButton = new Button("Pause");
        animationButton.addEventListener("click", e -> {
            isAnimating = !isAnimating;
            animationButton.setText(isAnimating ? "Pause" : "Resume");
        });
        
        animationPanel.addChild(animationButton);
        
        // Speed control
        Panel speedPanel = new Panel();
        speedPanel.addChild(new Label("Speed:").setStyle("margin-bottom", "8px"));
        
        ComboBox speedSelector = new ComboBox(Arrays.asList("Slow", "Medium", "Fast"));
        speedSelector.setSelectedIndex(1); // Medium
        speedSelector.setOnSelectionChange(index -> {
            switch (index) {
                case 0: rotationSpeed = 0.005; break;
                case 1: rotationSpeed = 0.01; break;
                case 2: rotationSpeed = 0.02; break;
                default: rotationSpeed = 0.01;
            }
        });
        
        speedPanel.addChild(speedSelector);
        
        // Add controls
        controls.addChild(shapePanel);
        controls.addChild(colorPanel);
        controls.addChild(animationPanel);
        controls.addChild(speedPanel);
        
        // Add components to the container
        sceneContainer.addChild(rendererContainer);
        sceneContainer.addChild(controls);
        
        // Add components to the main panel
        mainPanel.addChild(header, BorderLayout.NORTH);
        mainPanel.addChild(sceneContainer, BorderLayout.CENTER);
        
        // Initialize Three.js when the renderer container is rendered
        rendererContainer.addEventListener("DOMNodeInserted", e -> {
            if (rendererContainer.getElement() != null) {
                initThreeJs(rendererContainer.getElement());
                startAnimation();
            }
        });
        
        return mainPanel;
    }
    
    private void initThreeJs(HTMLElement container) {
        // Create scene
        scene = ThreeJS.createScene();
        
        // Create camera
        double aspect = container.getClientWidth() / (double) container.getClientHeight();
        camera = ThreeJS.createPerspectiveCamera(75, aspect, 0.1, 1000);
        camera.getPosition().setZ(5);
        
        // Create renderer
        renderer = ThreeJS.createRenderer();
        renderer.setSize(container.getClientWidth(), container.getClientHeight());
        container.appendChild(renderer.getDomElement());
        
        // Add lights
        AmbientLight ambientLight = AmbientLight.create("#ffffff", 0.5);
        scene.add(ambientLight);
        
        DirectionalLight directionalLight = DirectionalLight.create("#ffffff", 0.8);
        directionalLight.getPosition().set(1, 1, 1);
        scene.add(directionalLight);
        
        // Create initial shape
        createShape();
        
        // Handle window resize
        Window.current().addEventListener("resize", evt -> {
            if (container.getClientWidth() > 0 && container.getClientHeight() > 0) {
                camera.setAspect(container.getClientWidth() / (double) container.getClientHeight());
                camera.updateProjectionMatrix();
                renderer.setSize(container.getClientWidth(), container.getClientHeight());
            }
        });
    }
    
    private void createShape() {
        MeshPhongMaterial material = MeshPhongMaterial.create(currentColor);
        
        if ("cube".equals(currentShape)) {
            BoxGeometry geometry = BoxGeometry.create(1, 1, 1);
            currentMesh = Mesh.create(geometry, material);
        } else if ("sphere".equals(currentShape)) {
            SphereGeometry geometry = SphereGeometry.create(1, 32, 32);
            currentMesh = Mesh.create(geometry, material);
        }
        
        scene.add(currentMesh);
    }
    
    private void updateShape() {
        // Remove current mesh
        if (currentMesh != null) {
            scene.remove(currentMesh);
        }
        
        // Create new mesh
        createShape();
    }
    
    private void startAnimation() {
        Window.current().requestAnimationFrame(timestamp -> {
            animate(timestamp);
        });
    }
    
    private void animate(double timestamp) {
        // Request next frame
        Window.current().requestAnimationFrame(ts -> {
            animate(ts);
        });
        
        // Rotate the mesh if animation is enabled
        if (isAnimating && currentMesh != null) {
            currentMesh.getRotation().setX(currentMesh.getRotation().getX() + rotationSpeed);
            currentMesh.getRotation().setY(currentMesh.getRotation().getY() + rotationSpeed);
        }
        
        // Render the scene
        renderer.render(scene, camera);
    }
}