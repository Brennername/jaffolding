package com.danielremsburg.jaffolding.examples;

import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.ui.Animation;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.ChartComponent;
import com.danielremsburg.jaffolding.ui.DataTable;
import com.danielremsburg.jaffolding.ui.Desktop;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.Window;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo of the desktop environment with windows.
 */
public class DesktopDemo {
    
    public Component createDemo() {
        // Create desktop
        Desktop desktop = new Desktop();
        
        // Add a welcome window
        Window welcomeWindow = createWelcomeWindow();
        desktop.addWindow(welcomeWindow);
        
        // Add a chart window
        Window chartWindow = createChartWindow();
        chartWindow.setPosition(100, 150);
        desktop.addWindow(chartWindow);
        
        // Add a data table window
        Window tableWindow = createTableWindow();
        tableWindow.setPosition(500, 100);
        desktop.addWindow(tableWindow);
        
        // Add a button to create new windows
        Panel controls = new Panel();
        controls.setStyle("position", "absolute")
                .setStyle("top", "20px")
                .setStyle("right", "20px")
                .setStyle("display", "flex")
                .setStyle("gap", "10px");
        
        Button newChartBtn = new Button("New Chart");
        newChartBtn.setPrimary();
        newChartBtn.addEventListener("click", e -> {
            Window newWindow = createChartWindow();
            newWindow.setPosition(Math.random() * 400, Math.random() * 200);
            desktop.addWindow(newWindow);
        });
        
        Button newTableBtn = new Button("New Table");
        newTableBtn.setSuccess();
        newTableBtn.addEventListener("click", e -> {
            Window newWindow = createTableWindow();
            newWindow.setPosition(Math.random() * 400, Math.random() * 200);
            desktop.addWindow(newWindow);
        });
        
        controls.addChild(newChartBtn);
        controls.addChild(newTableBtn);
        
        desktop.addChild(controls);
        
        // Apply animations
        desktop.addEventListener("DOMNodeInserted", e -> {
            if (desktop.getElement() != null) {
                Animation.fadeIn(desktop, 0.5);
            }
        });
        
        return desktop;
    }
    
    private Window createWelcomeWindow() {
        Panel content = new Panel();
        content.setLayout(new BorderLayout());
        content.setPadding("20px");
        
        Label title = new Label("Welcome to Jaffolding Desktop");
        title.setHeading(2);
        title.setStyle("margin-bottom", "15px");
        
        Label description = new Label(
            "This is a demonstration of the window and desktop components. " +
            "You can drag windows by their title bars, resize them using the bottom-right corner, " +
            "and minimize, maximize, or close them using the buttons in the top-right corner."
        );
        description.setStyle("line-height", "1.5");
        
        Panel buttonPanel = new Panel();
        buttonPanel.setStyle("margin-top", "20px")
                   .setStyle("display", "flex")
                   .setStyle("justify-content", "center");
        
        Button getStartedBtn = new Button("Get Started");
        getStartedBtn.setPrimary();
        getStartedBtn.setStyle("padding", "10px 20px");
        
        buttonPanel.addChild(getStartedBtn);
        
        content.addChild(title, BorderLayout.NORTH);
        content.addChild(description, BorderLayout.CENTER);
        content.addChild(buttonPanel, BorderLayout.SOUTH);
        
        Window window = new Window("Welcome", content);
        window.setSize(400, 300);
        window.setPosition(50, 50);
        
        return window;
    }
    
    private Window createChartWindow() {
        Panel content = new Panel();
        content.setLayout(new BorderLayout());
        content.setPadding("15px");
        
        Label title = new Label("Sales Chart");
        title.setStyle("font-size", "18px")
             .setStyle("font-weight", "bold")
             .setStyle("margin-bottom", "15px");
        
        // Create chart
        ChartComponent chart = new ChartComponent();
        chart.setType("bar");
        
        // Add sample data
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June");
        List<Number> data1 = Arrays.asList(65, 59, 80, 81, 56, 55);
        List<Number> data2 = Arrays.asList(28, 48, 40, 19, 86, 27);
        
        chart.setLabels(labels);
        chart.addDataset("Dataset 1", data1, "rgba(75, 192, 192, 0.2)", "rgba(75, 192, 192, 1)");
        chart.addDataset("Dataset 2", data2, "rgba(255, 99, 132, 0.2)", "rgba(255, 99, 132, 1)");
        
        content.addChild(title, BorderLayout.NORTH);
        content.addChild(chart, BorderLayout.CENTER);
        
        Window window = new Window("Chart Demo", content);
        window.setSize(500, 400);
        
        return window;
    }
    
    private Window createTableWindow() {
        Panel content = new Panel();
        content.setLayout(new BorderLayout());
        content.setPadding("15px");
        
        Label title = new Label("Sales Data");
        title.setStyle("font-size", "18px")
             .setStyle("font-weight", "bold")
             .setStyle("margin-bottom", "15px");
        
        // Create data table
        DataTable dataTable = new DataTable(Arrays.asList("Product", "Category", "Sales", "Revenue", "Month"));
        dataTable.setColumnType("Sales", "number");
        dataTable.setColumnType("Revenue", "number");
        
        // Add sample data
        List<Map<String, Object>> tableData = new ArrayList<>();
        
        Map<String, Object> row1 = new HashMap<>();
        row1.put("Product", "Laptop");
        row1.put("Category", "Electronics");
        row1.put("Sales", 120);
        row1.put("Revenue", 120000);
        row1.put("Month", "January");
        tableData.add(row1);
        
        Map<String, Object> row2 = new HashMap<>();
        row2.put("Product", "Smartphone");
        row2.put("Category", "Electronics");
        row2.put("Sales", 200);
        row2.put("Revenue", 100000);
        row2.put("Month", "January");
        tableData.add(row2);
        
        Map<String, Object> row3 = new HashMap<>();
        row3.put("Product", "Headphones");
        row3.put("Category", "Accessories");
        row3.put("Sales", 150);
        row3.put("Revenue", 15000);
        row3.put("Month", "January");
        tableData.add(row3);
        
        dataTable.setData(tableData);
        
        content.addChild(title, BorderLayout.NORTH);
        content.addChild(dataTable, BorderLayout.CENTER);
        
        Window window = new Window("Table Demo", content);
        window.setSize(600, 400);
        
        return window;
    }
}