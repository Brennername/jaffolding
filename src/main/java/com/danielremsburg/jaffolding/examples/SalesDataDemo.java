package com.danielremsburg.jaffolding.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.json.JSON;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.State;
import com.danielremsburg.jaffolding.ui.Animation;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.ChartComponent;
import com.danielremsburg.jaffolding.ui.ComboBox;
import com.danielremsburg.jaffolding.ui.DataTable;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.TextField;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;

/**
 * Demo of the sales data visualization with reactive charts.
 */
public class SalesDataDemo {
    
    private State<List<Map<String, Object>>> salesData = new State<>(new ArrayList<>());
    private State<List<Map<String, Object>>> filteredData = new State<>(new ArrayList<>());
    private State<List<String>> categories = new State<>(new ArrayList<>());
    private State<List<String>> products = new State<>(new ArrayList<>());
    private State<List<String>> months = new State<>(new ArrayList<>());
    private State<String> selectedCategory = new State<>("All");
    private State<String> selectedMonth = new State<>("All");
    private State<String> chartType = new State<>("bar");
    
    public Component createDemo() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("Sales Data Visualization");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // Content
        Panel content = new Panel();
        content.setLayout(new BorderLayout(20, 20));
        content.setPadding("20px");
        
        // Filters panel
        Panel filtersPanel = new Panel();
        filtersPanel.setLayout(new GridLayout(1, 4, 20, 0));
        filtersPanel.setStyle("margin-bottom", "20px")
                    .setStyle("padding", "15px")
                    .setStyle("background-color", "#f5f5f5")
                    .setStyle("border-radius", "4px");
        
        // Category filter
        Panel categoryPanel = new Panel();
        categoryPanel.addChild(new Label("Category:").setStyle("margin-bottom", "8px"));
        
        ComboBox categorySelector = new ComboBox();
        categorySelector.addItem("All");
        
        categories.subscribe(cats -> {
            for (String cat : cats) {
                categorySelector.addItem(cat);
            }
        });
        
        categorySelector.setOnSelectionChange(index -> {
            selectedCategory.set(categorySelector.getSelectedItem());
            filterData();
        });
        
        categoryPanel.addChild(categorySelector);
        
        // Month filter
        Panel monthPanel = new Panel();
        monthPanel.addChild(new Label("Month:").setStyle("margin-bottom", "8px"));
        
        ComboBox monthSelector = new ComboBox();
        monthSelector.addItem("All");
        
        months.subscribe(ms -> {
            for (String m : ms) {
                monthSelector.addItem(m);
            }
        });
        
        monthSelector.setOnSelectionChange(index -> {
            selectedMonth.set(monthSelector.getSelectedItem());
            filterData();
        });
        
        monthPanel.addChild(monthSelector);
        
        // Chart type selector
        Panel chartTypePanel = new Panel();
        chartTypePanel.addChild(new Label("Chart Type:").setStyle("margin-bottom", "8px"));
        
        ComboBox chartTypeSelector = new ComboBox(Arrays.asList(
            "bar", "line", "pie", "doughnut", "polarArea"
        ));
        
        chartTypeSelector.setOnSelectionChange(index -> {
            chartType.set(chartTypeSelector.getSelectedItem());
        });
        
        chartTypePanel.addChild(chartTypeSelector);
        
        // Search
        Panel searchPanel = new Panel();
        searchPanel.addChild(new Label("Search:").setStyle("margin-bottom", "8px"));
        
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search products...");
        searchField.setOnInput(e -> {
            String searchText = searchField.getValue().toLowerCase();
            if (searchText.isEmpty()) {
                filterData();
            } else {
                List<Map<String, Object>> filtered = new ArrayList<>();
                for (Map<String, Object> row : salesData.get()) {
                    String product = row.get("product").toString().toLowerCase();
                    if (product.contains(searchText)) {
                        filtered.add(row);
                    }
                }
                filteredData.set(filtered);
            }
        });
        
        searchPanel.addChild(searchField);
        
        filtersPanel.addChild(categoryPanel);
        filtersPanel.addChild(monthPanel);
        filtersPanel.addChild(chartTypePanel);
        filtersPanel.addChild(searchPanel);
        
        // Main content area with table and chart
        Panel mainContent = new Panel();
        mainContent.setLayout(new GridLayout(1, 2, 20, 0));
        
        // Table panel
        Panel tablePanel = new Panel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setStyle("background-color", "white")
                  .setStyle("border-radius", "4px")
                  .setStyle("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
                  .setStyle("padding", "15px");
        
        Label tableTitle = new Label("Sales Data");
        tableTitle.setStyle("font-size", "18px")
                  .setStyle("font-weight", "bold")
                  .setStyle("margin-bottom", "15px");
        
        // Create data table
        DataTable dataTable = new DataTable(Arrays.asList("Product", "Category", "Sales", "Revenue", "Month"));
        dataTable.setColumnType("Sales", "number");
        dataTable.setColumnType("Revenue", "number");
        
        // Bind to filtered data state
        filteredData.subscribe(data -> {
            List<Map<String, Object>> tableData = new ArrayList<>();
            for (Map<String, Object> item : data) {
                Map<String, Object> row = new HashMap<>();
                row.put("Product", item.get("product"));
                row.put("Category", item.get("category"));
                row.put("Sales", item.get("sales"));
                row.put("Revenue", item.get("revenue"));
                row.put("Month", item.get("month"));
                tableData.add(row);
            }
            dataTable.setData(tableData);
        });
        
        // Add table controls
        Panel tableControls = new Panel();
        tableControls.setLayout(new GridLayout(1, 2, 10, 0));
        tableControls.setStyle("margin-top", "15px");
        
        Button refreshButton = new Button("Refresh Data");
        refreshButton.setPrimary();
        refreshButton.addEventListener("click", e -> {
            loadData();
        });
        
        Button addButton = new Button("Add Random Sale");
        addButton.setSuccess();
        addButton.addEventListener("click", e -> {
            addRandomSale();
        });
        
        tableControls.addChild(refreshButton);
        tableControls.addChild(addButton);
        
        tablePanel.addChild(tableTitle, BorderLayout.NORTH);
        tablePanel.addChild(dataTable, BorderLayout.CENTER);
        tablePanel.addChild(tableControls, BorderLayout.SOUTH);
        
        // Chart panel
        Panel chartPanel = new Panel();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setStyle("background-color", "white")
                  .setStyle("border-radius", "4px")
                  .setStyle("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)")
                  .setStyle("padding", "15px");
        
        Label chartTitle = new Label("Sales Visualization");
        chartTitle.setStyle("font-size", "18px")
                  .setStyle("font-weight", "bold")
                  .setStyle("margin-bottom", "15px");
        
        // Create chart
        ChartComponent chart = new ChartComponent();
        chart.bindToDataState(filteredData, "product", "sales", "category");
        
        // Update chart type when changed
        chartType.subscribe(type -> {
            chart.setType(type);
        });
        
        chartPanel.addChild(chartTitle, BorderLayout.NORTH);
        chartPanel.addChild(chart, BorderLayout.CENTER);
        
        mainContent.addChild(tablePanel);
        mainContent.addChild(chartPanel);
        
        content.addChild(filtersPanel, BorderLayout.NORTH);
        content.addChild(mainContent, BorderLayout.CENTER);
        
        mainPanel.addChild(header, BorderLayout.NORTH);
        mainPanel.addChild(content, BorderLayout.CENTER);
        
        // Load initial data
        loadData();
        loadCategories();
        loadMonths();
        
        // Set up data filtering
        salesData.subscribe(data -> {
            filterData();
        });
        
        // Apply animations
        mainPanel.addEventListener("DOMNodeInserted", e -> {
            if (mainPanel.getElement() != null) {
                Animation.fadeIn(header, 0.5);
                Animation.slideIn(filtersPanel, "top", 0.5);
                Animation.slideIn(tablePanel, "left", 0.5);
                Animation.slideIn(chartPanel, "right", 0.5);
            }
        });
        
        return mainPanel;
    }
    
    private void loadData() {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("GET", "/api/sales");
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                if (xhr.getStatus() == 200) {
                    try {
                        List<Map<String, Object>> data = parseJsonArray(xhr.getResponseText());
                        salesData.set(data);
                    } catch (Exception e) {
                        System.err.println("Error parsing sales data: " + e.getMessage());
                        // Fallback to mock data
                        salesData.set(createMockData());
                    }
                } else {
                    // Fallback to mock data
                    salesData.set(createMockData());
                }
            }
        });
        xhr.send();
    }
    
    private void loadCategories() {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("GET", "/api/categories");
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                if (xhr.getStatus() == 200) {
                    try {
                        List<String> data = parseJsonStringArray(xhr.getResponseText());
                        categories.set(data);
                    } catch (Exception e) {
                        System.err.println("Error parsing categories: " + e.getMessage());
                        // Fallback to mock data
                        categories.set(Arrays.asList("Electronics", "Accessories", "Software", "Services"));
                    }
                } else {
                    // Fallback to mock data
                    categories.set(Arrays.asList("Electronics", "Accessories", "Software", "Services"));
                }
            }
        });
        xhr.send();
    }
    
    private void loadMonths() {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("GET", "/api/months");
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                if (xhr.getStatus() == 200) {
                    try {
                        List<String> data = parseJsonStringArray(xhr.getResponseText());
                        months.set(data);
                    } catch (Exception e) {
                        System.err.println("Error parsing months: " + e.getMessage());
                        // Fallback to mock data
                        months.set(Arrays.asList("January", "February", "March", "April", "May", "June", 
                                                "July", "August", "September", "October", "November", "December"));
                    }
                } else {
                    // Fallback to mock data
                    months.set(Arrays.asList("January", "February", "March", "April", "May", "June", 
                                            "July", "August", "September", "October", "November", "December"));
                }
            }
        });
        xhr.send();
    }
    
    private void filterData() {
        List<Map<String, Object>> data = salesData.get();
        List<Map<String, Object>> filtered = new ArrayList<>();
        
        String category = selectedCategory.get();
        String month = selectedMonth.get();
        
        for (Map<String, Object> item : data) {
            boolean categoryMatch = "All".equals(category) || category.equals(item.get("category"));
            boolean monthMatch = "All".equals(month) || month.equals(item.get("month"));
            
            if (categoryMatch && monthMatch) {
                filtered.add(item);
            }
        }
        
        filteredData.set(filtered);
    }
    
    private void addRandomSale() {
        List<Map<String, Object>> data = new ArrayList<>(salesData.get());
        List<String> productList = Arrays.asList("Laptop", "Smartphone", "Headphones", "Monitor", "Keyboard", "Mouse", "Tablet", "Printer");
        List<String> categoryList = Arrays.asList("Electronics", "Accessories", "Software", "Services");
        List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June");
        
        // Generate random sale
        String product = productList.get((int) (Math.random() * productList.size()));
        String category = categoryList.get((int) (Math.random() * categoryList.size()));
        String month = monthList.get((int) (Math.random() * monthList.size()));
        int sales = 50 + (int) (Math.random() * 200);
        double revenue = sales * (100 + Math.random() * 400);
        
        Map<String, Object> newSale = new HashMap<>();
        newSale.put("id", data.size() + 1);
        newSale.put("product", product);
        newSale.put("category", category);
        newSale.put("sales", sales);
        newSale.put("revenue", revenue);
        newSale.put("month", month);
        
        data.add(newSale);
        salesData.set(data);
        
        // Post to server
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("POST", "/api/sales");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(newSale));
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseJsonArray(String json) {
        List<Map<String, Object>> result = new ArrayList<>();
        Object parsed = JSON.parse(json);
        
        if (parsed instanceof Object[]) {
            Object[] array = (Object[]) parsed;
            for (Object item : array) {
                if (item instanceof Map) {
                    result.add((Map<String, Object>) item);
                }
            }
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private List<String> parseJsonStringArray(String json) {
        List<String> result = new ArrayList<>();
        Object parsed = JSON.parse(json);
        
        if (parsed instanceof Object[]) {
            Object[] array = (Object[]) parsed;
            for (Object item : array) {
                result.add(item.toString());
            }
        }
        
        return result;
    }
    
    private List<Map<String, Object>> createMockData() {
        List<Map<String, Object>> mockData = new ArrayList<>();
        
        // January data
        addMockSale(mockData, 1, "Laptop", "Electronics", 120, 120000, "January");
        addMockSale(mockData, 2, "Smartphone", "Electronics", 200, 100000, "January");
        addMockSale(mockData, 3, "Headphones", "Accessories", 150, 15000, "January");
        addMockSale(mockData, 4, "Monitor", "Electronics", 80, 24000, "January");
        addMockSale(mockData, 5, "Keyboard", "Accessories", 100, 5000, "January");
        
        // February data
        addMockSale(mockData, 6, "Laptop", "Electronics", 130, 130000, "February");
        addMockSale(mockData, 7, "Smartphone", "Electronics", 180, 90000, "February");
        addMockSale(mockData, 8, "Headphones", "Accessories", 170, 17000, "February");
        addMockSale(mockData, 9, "Monitor", "Electronics", 85, 25500, "February");
        addMockSale(mockData, 10, "Keyboard", "Accessories", 110, 5500, "February");
        
        return mockData;
    }
    
    private void addMockSale(List<Map<String, Object>> data, int id, String product, String category, 
                            int sales, double revenue, String month) {
        Map<String, Object> sale = new HashMap<>();
        sale.put("id", id);
        sale.put("product", product);
        sale.put("category", category);
        sale.put("sales", sales);
        sale.put("revenue", revenue);
        sale.put("month", month);
        data.add(sale);
    }
}