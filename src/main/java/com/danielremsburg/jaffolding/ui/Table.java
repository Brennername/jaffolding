package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.bridge.HTMLTableElements.HTMLTableElement;
import com.danielremsburg.jaffolding.bridge.HTMLTableElements.HTMLTableRowElement;

/**
 * A table component for displaying tabular data.
 * Similar to JTable in Swing.
 */
public class Table extends Component {
    private List<String> columnNames = new ArrayList<>();
    private List<List<String>> data = new ArrayList<>();
    private boolean selectable = true;
    private int selectedRow = -1;
    private Consumer<Integer> selectionListener;
    
    public Table() {
        super("table");
        initializeStyles();
    }
    
    public Table(List<String> columnNames) {
        super("table");
        this.columnNames = new ArrayList<>(columnNames);
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("width", "100%")
            .setStyle("border-collapse", "collapse")
            .setStyle("font-size", "14px");
    }
    
    public Table setColumnNames(List<String> columnNames) {
        this.columnNames = new ArrayList<>(columnNames);
        refreshTable();
        return this;
    }
    
    public Table setData(List<List<String>> data) {
        this.data = new ArrayList<>();
        for (List<String> row : data) {
            this.data.add(new ArrayList<>(row));
        }
        refreshTable();
        return this;
    }
    
    public Table addRow(List<String> row) {
        data.add(new ArrayList<>(row));
        refreshTable();
        return this;
    }
    
    public Table removeRow(int index) {
        if (index >= 0 && index < data.size()) {
            data.remove(index);
            refreshTable();
        }
        return this;
    }
    
    public Table clearData() {
        data.clear();
        refreshTable();
        return this;
    }
    
    public Table setSelectable(boolean selectable) {
        this.selectable = selectable;
        refreshTable();
        return this;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public Table setSelectedRow(int row) {
        if (row >= -1 && row < data.size()) {
            selectedRow = row;
            updateSelection();
        }
        return this;
    }
    
    public Table setOnRowSelect(Consumer<Integer> listener) {
        this.selectionListener = listener;
        return this;
    }
    
    private void refreshTable() {
        if (getElement() == null) {
            return;
        }
        
        HTMLElement tableElement = getElement();
        tableElement.setInnerHTML("");
        
        // Create header
        if (!columnNames.isEmpty()) {
            HTMLElement thead = HTMLDocument.current().createElement("thead");
            HTMLElement headerRow = HTMLDocument.current().createElement("tr");
            
            for (String columnName : columnNames) {
                HTMLElement th = HTMLDocument.current().createElement("th");
                th.setTextContent(columnName);
                th.getStyle().setProperty("padding", "10px");
                th.getStyle().setProperty("text-align", "left");
                th.getStyle().setProperty("border-bottom", "2px solid #ddd");
                th.getStyle().setProperty("font-weight", "bold");
                headerRow.appendChild(th);
            }
            
            thead.appendChild(headerRow);
            tableElement.appendChild(thead);
        }
        
        // Create body
        HTMLElement tbody = HTMLDocument.current().createElement("tbody");
        
        for (int i = 0; i < data.size(); i++) {
            final int rowIndex = i;
            List<String> rowData = data.get(i);
            HTMLElement row = HTMLDocument.current().createElement("tr");
            
            if (selectable) {
                row.getStyle().setProperty("cursor", "pointer");
                
                row.addEventListener("click", e -> {
                    selectedRow = rowIndex;
                    updateSelection();
                    
                    if (selectionListener != null) {
                        selectionListener.accept(rowIndex);
                    }
                });
                
                if (rowIndex == selectedRow) {
                    row.getStyle().setProperty("background-color", "#e8f0fe");
                }
            }
            
            for (String cellData : rowData) {
                HTMLElement td = HTMLDocument.current().createElement("td");
                td.setTextContent(cellData);
                td.getStyle().setProperty("padding", "8px 10px");
                td.getStyle().setProperty("border-bottom", "1px solid #ddd");
                row.appendChild(td);
            }
            
            tbody.appendChild(row);
        }
        
        tableElement.appendChild(tbody);
    }
    
    private void updateSelection() {
        if (getElement() == null) {
            return;
        }
        
        HTMLElement tableElement = getElement();
        HTMLElement tbody = (HTMLElement) tableElement.getElementsByTagName("tbody").item(0);
        
        if (tbody == null) {
            return;
        }
        
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
    
    public List<List<String>> getData() {
        return new ArrayList<>(data);
    }
}