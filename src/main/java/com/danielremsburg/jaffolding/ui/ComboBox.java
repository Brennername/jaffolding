package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLOptionElement;
import org.teavm.jso.dom.html.HTMLSelectElement;

/**
 * A dropdown selection component.
 * Similar to JComboBox in Swing.
 */
public class ComboBox extends UIComponent {
    private List<String> items = new ArrayList<>();
    private int selectedIndex = -1;
    private Consumer<Integer> selectionListener;
    private boolean editable = false;
    
    public ComboBox() {
        super("select");
        initializeStyles();
    }
    
    public ComboBox(List<String> items) {
        super("select");
        this.items = new ArrayList<>(items);
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("padding", "8px 12px")
            .setStyle("border", "1px solid #dadce0")
            .setStyle("border-radius", "4px")
            .setStyle("font-size", "14px")
            .setStyle("width", "100%")
            .setStyle("background-color", "white")
            .setStyle("cursor", "pointer")
            .setStyle("transition", "border-color 0.2s")
            .addEventListener("focus", e -> {
                if (isEnabled()) {
                    setStyle("border-color", "#4285f4");
                    setStyle("outline", "none");
                }
            })
            .addEventListener("blur", e -> {
                if (isEnabled()) {
                    setStyle("border-color", "#dadce0");
                }
            })
            .addEventListener("change", e -> {
                if (isEnabled()) {
                    HTMLSelectElement select = (HTMLSelectElement) getElement();
                    int newIndex = select.getSelectedIndex();
                    
                    if (newIndex != selectedIndex) {
                        selectedIndex = newIndex;
                        
                        if (selectionListener != null) {
                            selectionListener.accept(selectedIndex);
                        }
                    }
                }
            });
    }
    
    public ComboBox setItems(List<String> items) {
        this.items = new ArrayList<>(items);
        refreshItems();
        return this;
    }
    
    public List<String> getItems() {
        return new ArrayList<>(items);
    }
    
    public ComboBox addItem(String item) {
        items.add(item);
        refreshItems();
        return this;
    }
    
    public ComboBox removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            
            if (selectedIndex >= items.size()) {
                selectedIndex = items.isEmpty() ? -1 : items.size() - 1;
            }
            
            refreshItems();
        }
        return this;
    }
    
    public ComboBox removeItem(String item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            removeItem(index);
        }
        return this;
    }
    
    public ComboBox clearItems() {
        items.clear();
        selectedIndex = -1;
        refreshItems();
        return this;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public ComboBox setSelectedIndex(int index) {
        if (index >= -1 && index < items.size() && index != selectedIndex) {
            selectedIndex = index;
            
            if (getElement() != null) {
                HTMLSelectElement select = (HTMLSelectElement) getElement();
                select.setSelectedIndex(Math.max(0, selectedIndex));
            }
        }
        return this;
    }
    
    public String getSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            return items.get(selectedIndex);
        }
        return null;
    }
    
    public ComboBox setSelectedItem(String item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            setSelectedIndex(index);
        }
        return this;
    }
    
    public ComboBox setOnSelectionChange(Consumer<Integer> listener) {
        this.selectionListener = listener;
        return this;
    }
    
    public ComboBox setEditable(boolean editable) {
        this.editable = editable;
        // Note: HTML select elements don't support editable mode directly
        // For a truly editable combo box, we would need to implement a custom component
        // with an input field and a dropdown
        return this;
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    private void refreshItems() {
        if (getElement() == null) {
            return;
        }
        
        HTMLSelectElement select = (HTMLSelectElement) getElement();
        select.setInnerHTML("");
        
        for (int i = 0; i < items.size(); i++) {
            HTMLOptionElement option = (HTMLOptionElement) HTMLDocument.current().createElement("option");
            option.setText(items.get(i));
            option.setValue(String.valueOf(i));
            
            if (i == selectedIndex) {
                option.setSelected(true);
            }
            
            select.appendChild(option);
        }
    }
    
    @Override
    public HTMLElement render(HTMLElement parent) {
        HTMLElement element = super.render(parent);
        refreshItems();
        return element;
    }
}