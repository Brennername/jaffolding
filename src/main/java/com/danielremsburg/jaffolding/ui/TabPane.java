package com.danielremsburg.jaffolding.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLElement;

import com.danielremsburg.jaffolding.Component;

/**
 * A tabbed pane component.
 * Similar to JTabbedPane in Swing.
 */
public class TabPane extends Component {
    private List<Tab> tabs = new ArrayList<>();
    private int selectedIndex = 0;
    private Consumer<Integer> tabChangeListener;
    
    public TabPane() {
        super("div");
        initializeStyles();
    }
    
    private void initializeStyles() {
        setStyle("display", "flex")
            .setStyle("flex-direction", "column")
            .setStyle("width", "100%")
            .setStyle("height", "100%");
    }
    
    public TabPane addTab(String title, Component content) {
        Tab tab = new Tab(title, content);
        tabs.add(tab);
        refreshTabs();
        return this;
    }
    
    public TabPane removeTab(int index) {
        if (index >= 0 && index < tabs.size()) {
            tabs.remove(index);
            
            if (selectedIndex >= tabs.size()) {
                selectedIndex = Math.max(0, tabs.size() - 1);
            }
            
            refreshTabs();
        }
        return this;
    }
    
    public TabPane setSelectedIndex(int index) {
        if (index >= 0 && index < tabs.size() && index != selectedIndex) {
            selectedIndex = index;
            refreshTabs();
            
            if (tabChangeListener != null) {
                tabChangeListener.accept(selectedIndex);
            }
        }
        return this;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public TabPane setOnTabChange(Consumer<Integer> listener) {
        this.tabChangeListener = listener;
        return this;
    }
    
    private void refreshTabs() {
        if (getElement() == null) {
            return;
        }
        
        getElement().setInnerHTML("");
        
        // Create tab header
        Component tabHeader = new Component("div");
        tabHeader.setStyle("display", "flex")
                 .setStyle("border-bottom", "1px solid #dadce0");
        
        for (int i = 0; i < tabs.size(); i++) {
            final int tabIndex = i;
            Tab tab = tabs.get(i);
            
            Component tabButton = new Component("div");
            tabButton.setStyle("padding", "10px 16px")
                     .setStyle("cursor", "pointer")
                     .setStyle("user-select", "none")
                     .setStyle("transition", "background-color 0.2s");
            
            if (i == selectedIndex) {
                tabButton.setStyle("border-bottom", "2px solid #4285f4")
                         .setStyle("color", "#4285f4")
                         .setStyle("font-weight", "500");
            } else {
                tabButton.setStyle("border-bottom", "2px solid transparent")
                         .setStyle("color", "#5f6368");
                
                tabButton.addEventListener("mouseover", e -> {
                    tabButton.setStyle("background-color", "#f1f3f4");
                });
                
                tabButton.addEventListener("mouseout", e -> {
                    tabButton.setStyle("background-color", "transparent");
                });
            }
            
            tabButton.addEventListener("click", e -> {
                setSelectedIndex(tabIndex);
            });
            
            Label tabLabel = new Label(tab.title);
            tabButton.addChild(tabLabel);
            
            tabHeader.addChild(tabButton);
        }
        
        // Create content area
        Component contentArea = new Component("div");
        contentArea.setStyle("flex", "1")
                   .setStyle("overflow", "auto")
                   .setStyle("padding", "16px");
        
        if (selectedIndex >= 0 && selectedIndex < tabs.size()) {
            contentArea.addChild(tabs.get(selectedIndex).content);
        }
        
        addChild(tabHeader);
        addChild(contentArea);
    }
    
    @Override
    public HTMLElement render(HTMLElement parent) {
        HTMLElement element = super.render(parent);
        refreshTabs();
        return element;
    }
    
    private static class Tab {
        String title;
        Component content;
        
        Tab(String title, Component content) {
            this.title = title;
            this.content = content;
        }
    }
}