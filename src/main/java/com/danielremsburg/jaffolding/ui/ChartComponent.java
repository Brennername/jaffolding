package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.json.JSON;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.State;

/**
 * A chart component that integrates with Chart.js.
 */
public class ChartComponent extends Component {
    private String type = "bar";
    private List<String> labels = new ArrayList<>();
    private List<Dataset> datasets = new ArrayList<>();
    private Map<String, Object> options = new HashMap<>();
    private Chart chart;
    private State<List<Map<String, Object>>> dataState;
    private String labelField;
    private String valueField;
    private String categoryField;
    private Function<Map<String, Object>, String> labelExtractor;
    private Function<Map<String, Object>, Number> valueExtractor;
    private Function<Map<String, Object>, String> categoryExtractor;
    
    public ChartComponent() {
        super("canvas");
        initializeStyles();
    }

    private void initializeStyles() {
        setStyle("width", "100%")
            .setStyle("height", "300px")
            .setStyle("max-height", "100%");
    }

    public ChartComponent setType(String type) {
        this.type = type;
        return this;
    }

    public ChartComponent setLabels(List<String> labels) {
        this.labels = new ArrayList<>(labels);
        return this;
    }

    public ChartComponent addDataset(String label, List<Number> data, String backgroundColor, String borderColor) {
        Dataset dataset = new Dataset();
        dataset.label = label;
        dataset.data = new ArrayList<>(data);
        dataset.backgroundColor = backgroundColor;
        dataset.borderColor = borderColor;
        dataset.borderWidth = 1;
        
        datasets.add(dataset);
        return this;
    }

    public ChartComponent setOptions(Map<String, Object> options) {
        this.options = new HashMap<>(options);
        return this;
    }

    public ChartComponent bindToDataState(State<List<Map<String, Object>>> dataState, 
                                         String labelField, 
                                         String valueField) {
        this.dataState = dataState;
        this.labelField = labelField;
        this.valueField = valueField;
        
        // Default extractors
        this.labelExtractor = data -> data.get(labelField).toString();
        this.valueExtractor = data -> (Number) data.get(valueField);
        
        // Subscribe to data changes
        dataState.subscribe(this::updateChartFromData);
        
        return this;
    }

    public ChartComponent bindToDataState(State<List<Map<String, Object>>> dataState, 
                                         String labelField, 
                                         String valueField,
                                         String categoryField) {
        bindToDataState(dataState, labelField, valueField);
        this.categoryField = categoryField;
        this.categoryExtractor = data -> data.get(categoryField).toString();
        
        return this;
    }

    public ChartComponent setLabelExtractor(Function<Map<String, Object>, String> extractor) {
        this.labelExtractor = extractor;
        return this;
    }

    public ChartComponent setValueExtractor(Function<Map<String, Object>, Number> extractor) {
        this.valueExtractor = extractor;
        return this;
    }

    public ChartComponent setCategoryExtractor(Function<Map<String, Object>, String> extractor) {
        this.categoryExtractor = extractor;
        return this;
    }

    private void updateChartFromData(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        
        // Extract labels and organize data
        List<String> newLabels = new ArrayList<>();
        Map<String, Map<String, Number>> categoryData = new HashMap<>();
        
        if (categoryField != null) {
            // For grouped data (multiple datasets)
            for (Map<String, Object> item : data) {
                String label = labelExtractor.apply(item);
                String category = categoryExtractor.apply(item);
                Number value = valueExtractor.apply(item);
                
                if (!newLabels.contains(label)) {
                    newLabels.add(label);
                }
                
                categoryData.computeIfAbsent(category, k -> new HashMap<>())
                           .put(label, value);
            }
            
            // Clear existing datasets
            datasets.clear();
            
            // Create a dataset for each category
            String[] colors = {
                "#4285f4", "#ea4335", "#fbbc05", "#34a853", 
                "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", 
                "#00bcd4", "#009688", "#4caf50", "#8bc34a"
            };
            
            int colorIndex = 0;
            for (Map.Entry<String, Map<String, Number>> entry : categoryData.entrySet()) {
                String category = entry.getKey();
                Map<String, Number> values = entry.getValue();
                
                List<Number> dataPoints = new ArrayList<>();
                for (String label : newLabels) {
                    dataPoints.add(values.getOrDefault(label, 0));
                }
                
                String color = colors[colorIndex % colors.length];
                addDataset(category, dataPoints, color + "33", color);
                colorIndex++;
            }
        } else {
            // For simple data (single dataset)
            List<Number> values = new ArrayList<>();
            
            for (Map<String, Object> item : data) {
                String label = labelExtractor.apply(item);
                Number value = valueExtractor.apply(item);
                
                newLabels.add(label);
                values.add(value);
            }
            
            // Clear existing datasets
            datasets.clear();
            
            // Add a single dataset
            List<String> backgroundColors = new ArrayList<>();
            List<String> borderColors = new ArrayList<>();
            
            for (int i = 0; i < values.size(); i++) {
                int hue = (i * 30) % 360;
                backgroundColors.add("hsla(" + hue + ", 70%, 60%, 0.2)");
                borderColors.add("hsla(" + hue + ", 70%, 60%, 1)");
            }
            
            Dataset dataset = new Dataset();
            dataset.label = valueField;
            dataset.data = values;
            dataset.backgroundColor = backgroundColors;
            dataset.borderColor = borderColors;
            dataset.borderWidth = 1;
            
            datasets.add(dataset);
        }
        
        // Update labels
        this.labels = newLabels;
        
        // Update the chart
        if (chart != null) {
            updateChart();
        }
    }

    private void createChart() {
        if (getElement() == null) {
            return;
        }
        
        HTMLCanvasElement canvas = (HTMLCanvasElement) getElement();
        
        // Prepare chart configuration
        JSObject config = createEmptyObject();
        setObjectProperty(config, "type", type);
        
        // Prepare data
        JSObject data = createEmptyObject();
        setObjectProperty(data, "labels", convertToJSArray(labels));
        
        // Prepare datasets
        JSObject[] jsDatasets = new JSObject[datasets.size()];
        for (int i = 0; i < datasets.size(); i++) {
            Dataset dataset = datasets.get(i);
            JSObject jsDataset = createEmptyObject();
            
            setObjectProperty(jsDataset, "label", dataset.label);
            setObjectProperty(jsDataset, "data", convertToJSArray(dataset.data));
            
            if (dataset.backgroundColor instanceof List) {
                setObjectProperty(jsDataset, "backgroundColor", convertToJSArray((List<?>) dataset.backgroundColor));
            } else {
                setObjectProperty(jsDataset, "backgroundColor", dataset.backgroundColor);
            }
            
            if (dataset.borderColor instanceof List) {
                setObjectProperty(jsDataset, "borderColor", convertToJSArray((List<?>) dataset.borderColor));
            } else {
                setObjectProperty(jsDataset, "borderColor", dataset.borderColor);
            }
            
            setObjectProperty(jsDataset, "borderWidth", dataset.borderWidth);
            
            jsDatasets[i] = jsDataset;
        }
        
        setObjectProperty(data, "datasets", convertToJSArray(jsDatasets));
        setObjectProperty(config, "data", data);
        
        // Prepare options
        JSObject jsOptions = createEmptyObject();
        setObjectProperty(jsOptions, "responsive", true);
        setObjectProperty(jsOptions, "maintainAspectRatio", false);
        
        // Add custom options
        for (Map.Entry<String, Object> entry : options.entrySet()) {
            setObjectProperty(jsOptions, entry.getKey(), entry.getValue());
        }
        
        setObjectProperty(config, "options", jsOptions);
        
        // Create the chart
        chart = createChart(canvas, config);
    }

    private void updateChart() {
        if (chart == null || getElement() == null) {
            return;
        }
        
        // Update labels
        JSObject data = getChartData(chart);
        setObjectProperty(data, "labels", convertToJSArray(labels));
        
        // Update datasets
        JSObject[] jsDatasets = new JSObject[datasets.size()];
        for (int i = 0; i < datasets.size(); i++) {
            Dataset dataset = datasets.get(i);
            JSObject jsDataset = createEmptyObject();
            
            setObjectProperty(jsDataset, "label", dataset.label);
            setObjectProperty(jsDataset, "data", convertToJSArray(dataset.data));
            
            if (dataset.backgroundColor instanceof List) {
                setObjectProperty(jsDataset, "backgroundColor", convertToJSArray((List<?>) dataset.backgroundColor));
            } else {
                setObjectProperty(jsDataset, "backgroundColor", dataset.backgroundColor);
            }
            
            if (dataset.borderColor instanceof List) {
                setObjectProperty(jsDataset, "borderColor", convertToJSArray((List<?>) dataset.borderColor));
            } else {
                setObjectProperty(jsDataset, "borderColor", dataset.borderColor);
            }
            
            setObjectProperty(jsDataset, "borderWidth", dataset.borderWidth);
            
            jsDatasets[i] = jsDataset;
        }
        
        setObjectProperty(data, "datasets", convertToJSArray(jsDatasets));
        
        // Update the chart
        updateChartData(chart);
    }

    @Override
    public HTMLElement render(HTMLElement parent) {
        HTMLElement element = super.render(parent);
        
        // Initialize the chart when the canvas is rendered
        createChart();
        
        return element;
    }

    /**
     * Dataset class for Chart.js.
     */
    public static class Dataset {
        public String label;
        public List<Number> data;
        public Object backgroundColor; // Can be String or List<String>
        public Object borderColor; // Can be String or List<String>
        public int borderWidth;
    }

    // JavaScript interop methods
    
    @JSBody(params = {}, script = "return {};")
    private static native JSObject createEmptyObject();
    
    @JSBody(params = {"obj", "key", "value"}, script = "obj[key] = value;")
    private static native void setObjectProperty(JSObject obj, String key, Object value);
    
    @JSBody(params = {"items"}, script = "return Array.from(items);")
    private static native JSObject convertToJSArray(Object[] items);
    
    @JSBody(params = {"items"}, script = "return Array.from(items);")
    private static native JSObject convertToJSArray(List<?> items);
    
    @JSBody(params = {"canvas", "config"}, script = "return new Chart(canvas, config);")
    private static native Chart createChart(HTMLCanvasElement canvas, JSObject config);
    
    @JSBody(params = {"chart"}, script = "return chart.data;")
    private static native JSObject getChartData(Chart chart);
    
    @JSBody(params = {"chart"}, script = "chart.update();")
    private static native void updateChartData(Chart chart);
    
    /**
     * Chart.js Chart interface.
     */
    private interface Chart extends JSObject {
        void update();
        void destroy();
    }
}