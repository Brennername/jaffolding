package com.danielremsburg.jaffolding.examples;

import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.js.ChartJS;
import com.danielremsburg.jaffolding.js.ChartJS.Chart;
import com.danielremsburg.jaffolding.js.ChartJS.ChartConfig;
import com.danielremsburg.jaffolding.js.ChartJS.ChartData;
import com.danielremsburg.jaffolding.js.ChartJS.ChartLegend;
import com.danielremsburg.jaffolding.js.ChartJS.ChartOptions;
import com.danielremsburg.jaffolding.js.ChartJS.ChartTitle;
import com.danielremsburg.jaffolding.js.ChartJS.Dataset;
import com.danielremsburg.jaffolding.js.ChartJS.Factory;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.ComboBox;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;

import java.util.Arrays;

/**
 * Demo of Chart.js integration with the Jaffolding framework.
 */
public class ChartDemo {
    
    private Chart currentChart;
    private String currentChartType = "bar";
    
    public Component createDemo() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("Chart.js Integration Demo");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // Chart container
        Panel chartContainer = new Panel();
        chartContainer.setStyle("padding", "20px")
                      .setStyle("display", "flex")
                      .setStyle("flex-direction", "column")
                      .setStyle("align-items", "center");
        
        // Canvas for the chart
        Component canvas = new Component("canvas")
            .setAttribute("width", "800")
            .setAttribute("height", "400")
            .setStyle("max-width", "100%");
        
        // Controls
        Panel controls = new Panel();
        controls.setLayout(new GridLayout(1, 3, 20, 0));
        controls.setStyle("margin-top", "20px")
                .setStyle("width", "100%")
                .setStyle("max-width", "800px");
        
        // Chart type selector
        Panel chartTypePanel = new Panel();
        chartTypePanel.addChild(new Label("Chart Type:").setStyle("margin-bottom", "8px"));
        
        ComboBox chartTypeSelector = new ComboBox(Arrays.asList(
            "bar", "line", "pie", "doughnut", "radar", "polarArea"
        ));
        chartTypeSelector.setSelectedItem("bar");
        chartTypeSelector.setOnSelectionChange(index -> {
            currentChartType = chartTypeSelector.getSelectedItem();
            updateChart((HTMLCanvasElement) canvas.getElement());
        });
        
        chartTypePanel.addChild(chartTypeSelector);
        
        // Random data button
        Panel randomDataPanel = new Panel();
        randomDataPanel.addChild(new Label("Data:").setStyle("margin-bottom", "8px"));
        
        Button randomDataButton = new Button("Generate Random Data");
        randomDataButton.setPrimary();
        randomDataButton.addEventListener("click", e -> {
            updateChart((HTMLCanvasElement) canvas.getElement());
        });
        
        randomDataPanel.addChild(randomDataButton);
        
        // Add controls
        controls.addChild(chartTypePanel);
        controls.addChild(randomDataPanel);
        
        // Add components to the container
        chartContainer.addChild(canvas);
        chartContainer.addChild(controls);
        
        // Add components to the main panel
        mainPanel.addChild(header, BorderLayout.NORTH);
        mainPanel.addChild(chartContainer, BorderLayout.CENTER);
        
        // Initialize the chart when the canvas is rendered
        canvas.addEventListener("DOMNodeInserted", e -> {
            if (canvas.getElement() != null) {
                updateChart((HTMLCanvasElement) canvas.getElement());
            }
        });
        
        return mainPanel;
    }
    
    private void updateChart(HTMLCanvasElement canvas) {
        // Destroy previous chart if it exists
        if (currentChart != null) {
            currentChart.destroy();
        }
        
        // Create chart configuration
        ChartConfig config = Factory.createConfig();
        config.setType(currentChartType);
        
        // Create chart data
        ChartData data = Factory.createData();
        
        // Set labels
        String[] labels = {"January", "February", "March", "April", "May", "June", "July"};
        data.setLabels(Factory.createStringArray(labels));
        
        // Create datasets
        JSArray<Dataset> datasets = JSArray.create();
        
        // First dataset
        Dataset dataset1 = Factory.createDataset();
        dataset1.setLabel("Dataset 1");
        
        // Generate random data
        double[] values1 = new double[labels.length];
        for (int i = 0; i < values1.length; i++) {
            values1[i] = Math.random() * 100;
        }
        dataset1.setData(Factory.createNumberArray(values1));
        
        // Set colors
        String[] bgColors = {"rgba(255, 99, 132, 0.2)", "rgba(54, 162, 235, 0.2)", 
                            "rgba(255, 206, 86, 0.2)", "rgba(75, 192, 192, 0.2)",
                            "rgba(153, 102, 255, 0.2)", "rgba(255, 159, 64, 0.2)",
                            "rgba(199, 199, 199, 0.2)"};
        
        String[] borderColors = {"rgba(255, 99, 132, 1)", "rgba(54, 162, 235, 1)", 
                                "rgba(255, 206, 86, 1)", "rgba(75, 192, 192, 1)",
                                "rgba(153, 102, 255, 1)", "rgba(255, 159, 64, 1)",
                                "rgba(199, 199, 199, 1)"};
        
        dataset1.setBackgroundColor(Factory.createStringArray(bgColors));
        dataset1.setBorderColor(Factory.createStringArray(borderColors));
        dataset1.setBorderWidth(1);
        
        // Second dataset (for bar and line charts)
        if ("bar".equals(currentChartType) || "line".equals(currentChartType)) {
            Dataset dataset2 = Factory.createDataset();
            dataset2.setLabel("Dataset 2");
            
            // Generate random data
            double[] values2 = new double[labels.length];
            for (int i = 0; i < values2.length; i++) {
                values2[i] = Math.random() * 100;
            }
            dataset2.setData(Factory.createNumberArray(values2));
            
            // Set colors
            String[] bgColors2 = {"rgba(54, 162, 235, 0.2)", "rgba(255, 99, 132, 0.2)", 
                                "rgba(75, 192, 192, 0.2)", "rgba(255, 206, 86, 0.2)",
                                "rgba(255, 159, 64, 0.2)", "rgba(153, 102, 255, 0.2)",
                                "rgba(199, 199, 199, 0.2)"};
            
            String[] borderColors2 = {"rgba(54, 162, 235, 1)", "rgba(255, 99, 132, 1)", 
                                    "rgba(75, 192, 192, 1)", "rgba(255, 206, 86, 1)",
                                    "rgba(255, 159, 64, 1)", "rgba(153, 102, 255, 1)",
                                    "rgba(199, 199, 199, 1)"};
            
            dataset2.setBackgroundColor(Factory.createStringArray(bgColors2));
            dataset2.setBorderColor(Factory.createStringArray(borderColors2));
            dataset2.setBorderWidth(1);
            
            datasets.push(dataset2);
        }
        
        datasets.push(dataset1);
        data.setDatasets(datasets);
        config.setData(data);
        
        // Set options
        ChartOptions options = Factory.createOptions();
        options.setResponsive(true);
        
        ChartTitle title = Factory.createTitle();
        title.setDisplay(true);
        title.setText(currentChartType.substring(0, 1).toUpperCase() + currentChartType.substring(1) + " Chart Example");
        title.setFontSize(18);
        options.setTitle(title);
        
        ChartLegend legend = Factory.createLegend();
        legend.setDisplay(true);
        legend.setPosition("bottom");
        options.setLegend(legend);
        
        config.setOptions(options);
        
        // Create the chart
        currentChart = ChartJS.createChart(canvas, config);
    }
}