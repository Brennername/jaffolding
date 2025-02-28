package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.json.JSON;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.State;
import com.danielremsburg.jaffolding.bridge.HTMLTableElements.HTMLTableElement;
import com.danielremsburg.jaffolding.bridge.HTMLTableElements.HTMLTableRowElement;

/**
 * An enhanced data table component with sorting, filtering, and data binding.
 */
public class DataTable extends Component {
    private List<String> columnNames = new ArrayList<>();
    private List<Map<String, Object>> data = new ArrayList<>();
    private State<List<Map<String, Object>>> dataState;
    private boolean selectable = true;
    private int selectedRow = -1;
    private Consumer<Integer> selectionListener;
    private Consumer<Map<String, Object>> rowClickListener;
    private String sortColumn = null;
    private boolean sortAscending = true;
    private Map<String, String> columnTypes = new HashMap<>();
    private Map<String, String> filters = new HashMap<>();
    
    public DataTable() {
        super("div");
        this.dataState = new State<>(new ArrayList<>());
        initializeStyles();
    }
    
    public DataTable(List<String> columnNames) {
        super("div");
        this.columnNames = new ArrayList<>(columnNames);
        this.dataState = new State<>(new ArrayList<>());
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("width", "100%")
            .setStyle("overflow", "auto")
            .setStyle("border", "1px solid #e0e0e0")
            .setStyle("border-radius", "4px")
            .setStyle("background-color", "white");
        
        // Subscribe to data state changes
        dataState.subscribe(newData -> {
            this.data = newData;
            refreshTable();
        });
    }
    
    public DataTable setColumnNames(List<String> columnNames) {
        this.columnNames = new ArrayList<>(columnNames);
        refreshTable();
        return this;
    }
    
    public DataTable setColumnType(String columnName, String type) {
        columnTypes.put(columnName, type);
        return this;
    }
    
    public DataTable setData(List<Map<String, Object>> data) {
        List<Map<String, Object>> newData = new ArrayList<>();
        for (Map<String, Object> row : data) {
            newData.add(new HashMap<>(row));
        }
        dataState.set(newData);
        return this;
    }
    
    public DataTable addRow(Map<String, Object> row) {
        List<Map<String, Object>> newData = new ArrayList<>(data);
        newData.add(new HashMap<>(row));
        dataState.set(newData);
        return this;
    }
    
    public DataTable removeRow(int index) {
        if (index >= 0 && index < data.size()) {
            List<Map<String, Object>> newData = new ArrayList<>(data);
            newData.remove(index);
            dataState.set(newData);
        }
        return this;
    }
    
    public DataTable updateRow(int index, Map<String, Object> row) {
        if (index >= 0 && index < data.size()) {
            List<Map<String, Object>> newData = new ArrayList<>(data);
            newData.set(index, new HashMap<>(row));
            dataState.set(newData);
        }
        return this;
    }
    
    public DataTable clearData() {
        dataState.set(new ArrayList<>());
        return this;
    }
    
    public DataTable setSelectable(boolean selectable) {
        this.selectable = selectable;
        refreshTable();
        return this;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public Map<String, Object> getSelectedRowData() {
        if (selectedRow >= 0 && selectedRow < data.size()) {
            return data.get(selectedRow);
        }
        return null;
    }
    
    public DataTable setSelectedRow(int row) {
        if (row >= -1 && row < data.size()) {
            selectedRow = row;
            updateSelection();
        }
        return this;
    }
    
    public DataTable setOnRowSelect(Consumer<Integer> listener) {
        this.selectionListener = listener;
        return this;
    }
    
    public DataTable setOnRowClick(Consumer<Map<String, Object>> listener) {
        this.rowClickListener = listener;
        return this;
    }
    
    public DataTable sortBy(String columnName, boolean ascending) {
        this.sortColumn = columnName;
        this.sortAscending = ascending;
        
        if (data.size() > 0 && columnName != null) {
            List<Map<String, Object>> newData = new ArrayList<>(data);
            
            newData.sort((a, b) -> {
                Object valA = a.get(columnName);
                Object valB = b.get(columnName);
                
                if (valA == null && valB == null) return 0;
                if (valA == null) return ascending ? -1 : 1;
                if (valB == null) return ascending ? 1 : -1;
                
                String type = columnTypes.getOrDefault(columnName, "string");
                int result = 0;
                
                switch (type) {
                    case "number":
                        double numA = valA instanceof Number ? ((Number) valA).doubleValue() : 
                                      Double.parseDouble(valA.toString());
                        double numB = valB instanceof Number ? ((Number) valB).doubleValue() : 
                                      Double.parseDouble(valB.toString());
                        result = Double.compare(numA, numB);
                        break;
                    case "date":
                        // Simple string comparison for dates (assumes ISO format)
                        result = valA.toString().compareTo(valB.toString());
                        break;
                    default:
                        result = valA.toString().compareTo(valB.toString());
                }
                
                return ascending ? result : -result;
            });
            
            dataState.set(newData);
        }
        
        return this;
    }
    
    public DataTable filter(String columnName, String value) {
        if (value == null || value.trim().isEmpty()) {
            filters.remove(columnName);
        } else {
            filters.put(columnName, value.toLowerCase());
        }
        
        applyFilters();
        return this;
    }
    
    public DataTable clearFilters() {
        filters.clear();
        dataState.set(data);
        return this;
    }
    
    private void applyFilters() {
        if (filters.isEmpty()) {
            return;
        }
        
        List<Map<String, Object>> filteredData = new ArrayList<>();
        
        for (Map<String, Object> row : data) {
            boolean matches = true;
            
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String columnName = filter.getKey();
                String filterValue = filter.getValue();
                
                if (!row.containsKey(columnName) || row.get(columnName) == null) {
                    matches = false;
                    break;
                }
                
                String cellValue = row.get(columnName).toString().toLowerCase();
                if (!cellValue.contains(filterValue)) {
                    matches = false;
                    break;
                }
            }
            
            if (matches) {
                filteredData.add(row);
            }
        }
        
        dataState.set(filteredData);
    }
    
    public DataTable loadFromApi(String url) {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("GET", url);
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                if (xhr.getStatus() == 200) {
                    try {
                        JSObject jsonData = JSON.parse(xhr.getResponseText());
                        List<Map<String, Object>> newData = convertJSArrayToList(jsonData);
                        dataState.set(newData);
                    } catch (Exception e) {
                        System.err.println("Error parsing JSON: " + e.getMessage());
                    }
                }
            }
        });
        xhr.send();
        return this;
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> convertJSArrayToList(JSObject jsArray) {
        List<Map<String, Object>> result = new ArrayList<>();
        int length = getArrayLength(jsArray);
        
        for (int i = 0; i < length; i++) {
            JSObject jsObject = getArrayItem(jsArray, i);
            Map<String, Object> row = new HashMap<>();
            
            for (String key : getObjectKeys(jsObject)) {
                Object value = getObjectProperty(jsObject, key);
                row.put(key, value);
            }
            
            result.add(row);
        }
        
        return result;
    }
    
    @JSBody(params = {"array"}, script = "return array.length;")
    private static native int getArrayLength(JSObject array);
    
    @JSBody(params = {"array", "index"}, script = "return array[index];")
    private static native JSObject getArrayItem(JSObject array, int index);
    
    @JSBody(params = {"object"}, script = "return Object.keys(object);")
    private static native String[] getObjectKeys(JSObject object);
    
    @JSBody(params = {"object", "key"}, script = "return object[key];")
    private static native Object getObjectProperty(JSObject object, String key);
    
    private void refreshTable() {
        if (getElement() == null) {
            return;
        }
        
        getElement().setInnerHTML("");
        
        // Create table element
        HTMLElement table = HTMLDocument.current().createElement("table");
        table.getStyle().setProperty("width", "100%");
        table.getStyle().setProperty("border-collapse", "collapse");
        table.getStyle().setProperty("font-size", "14px");
        
        // Create header
        if (!columnNames.isEmpty()) {
            HTMLElement thead = HTMLDocument.current().createElement("thead");
            HTMLElement headerRow = HTMLDocument.current().createElement("tr");
            
            for (String columnName : columnNames) {
                HTMLElement th = HTMLDocument.current().createElement("th");
                th.setTextContent(columnName);
                th.getStyle().setProperty("padding", "12px 10px");
                th.getStyle().setProperty("text-align", "left");
                th.getStyle().setProperty("border-bottom", "2px solid #ddd");
                th.getStyle().setProperty("font-weight", "600");
                th.getStyle().setProperty("background-color", "#f5f5f5");
                th.getStyle().setProperty("cursor", "pointer");
                
                // Add sort indicator if this column is sorted
                if (columnName.equals(sortColumn)) {
                    th.setTextContent(columnName + (sortAscending ? " ▲" : " ▼"));
                }
                
                // Add sort functionality
                final String colName = columnName;
                th.addEventListener("click", e -> {
                    boolean asc = !colName.equals(sortColumn) || !sortAscending;
                    sortBy(colName, asc);
                });
                
                headerRow.appendChild(th);
            }
            
            thead.appendChild(headerRow);
            table.appendChild(thead);
        }
        
        // Create filter row
        if (!columnNames.isEmpty() && !filters.isEmpty()) {
            HTMLElement filterRow = HTMLDocument.current().createElement("tr");
            filterRow.getStyle().setProperty("background-color", "#f9f9f9");
            
            for (String columnName : columnNames) {
                HTMLElement td = HTMLDocument.current().createElement("td");
                td.getStyle().setProperty("padding", "8px 10px");
                
                if (filters.containsKey(columnName)) {
                    HTMLElement input = HTMLDocument.current().createElement("input");
                    input.setAttribute("type", "text");
                    input.setAttribute("placeholder", "Filter...");
                    input.setAttribute("value", filters.get(columnName));
                    input.getStyle().setProperty("width", "100%");
                    input.getStyle().setProperty("padding", "4px");
                    input.getStyle().setProperty("border", "1px solid #ddd");
                    input.getStyle().setProperty("border-radius", "3px");
                    
                    final String colName = columnName;
                    input.addEventListener("input", e -> {
                        String value = input.getAttribute("value");
                        filter(colName, value);
                    });
                    
                    td.appendChild(input);
                }
                
                filterRow.appendChild(td);
            }
            
            table.appendChild(filterRow);
        }
        
        // Create body
        HTMLElement tbody = HTMLDocument.current().createElement("tbody");
        
        for (int i = 0; i < data.size(); i++) {
            final int rowIndex = i;
            Map<String, Object> rowData = data.get(i);
            HTMLElement row = HTMLDocument.current().createElement("tr");
            
            if (selectable) {
                row.getStyle().setProperty("cursor", "pointer");
                
                row.addEventListener("click", e -> {
                    selectedRow = rowIndex;
                    updateSelection();
                    
                    if (selectionListener != null) {
                        selectionListener.accept(rowIndex);
                    }
                    
                    if (rowClickListener != null) {
                        rowClickListener.accept(rowData);
                    }
                });
                
                if (rowIndex == selectedRow) {
                    row.getStyle().setProperty("background-color", "#e8f0fe");
                }
            }
            
            // Add hover effect
            row.addEventListener("mouseover", e -> {
                if (rowIndex != selectedRow) {
                    row.getStyle().setProperty("background-color", "#f5f5f5");
                }
            });
            
            row.addEventListener("mouseout", e -> {
                if (rowIndex != selectedRow) {
                    row.getStyle().setProperty("background-color", "");
                }
            });
            
            // Add cells
            for (String columnName : columnNames) {
                HTMLElement td = HTMLDocument.current().createElement("td");
                td.getStyle().setProperty("padding", "10px");
                td.getStyle().setProperty("border-bottom", "1px solid #ddd");
                
                Object cellValue = rowData.get(columnName);
                td.setTextContent(cellValue != null ? cellValue.toString() : "");
                
                row.appendChild(td);
            }
            
            tbody.appendChild(row);
        }
        
        table.appendChild(tbody);
        getElement().appendChild(table);
        
        // Add empty state message if no data
        if (data.isEmpty()) {
            HTMLElement emptyState = HTMLDocument.current().createElement("div");
            emptyState.setTextContent("No data available");
            emptyState.getStyle().setProperty("padding", "20px");
            emptyState.getStyle().setProperty("text-align", "center");
            emptyState.getStyle().setProperty("color", "#888");
            
            getElement().appendChild(emptyState);
        }
    }
    
    private void updateSelection() {
        if (getElement() == null) {
            return;
        }
        
        HTMLElement table = (HTMLElement) getElement().querySelector("table");
        if (table == null) return;
        
        HTMLElement tbody = (HTMLElement) table.querySelector("tbody");
        if (tbody == null) return;
        
        for (int i = 0; i < tbody.getChildNodes().getLength(); i++) {
            HTMLElement row = (HTMLElement) tbody.getChildNodes().item(i);
            
            if (i == selectedRow) {
                row.getStyle().setProperty("background-color", "#e8f0fe");
            } else {
                row.getStyle().setProperty("background-color", "");
            }
        }
    }
    
    @Override
    public HTMLElement render(HTMLElement parent) {
        HTMLElement element = super.render(parent);
        refreshTable();
        return element;
    }
    
    public List<Map<String, Object>> getData() {
        return new ArrayList<>(data);
    }
    
    public State<List<Map<String, Object>>> getDataState() {
        return dataState;
    }
}