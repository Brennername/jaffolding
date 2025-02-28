package com.danielremsburg.jaffolding.ui;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLInputElement;

import java.util.function.Consumer;

import com.danielremsburg.jaffolding.Component;

/**
 * A checkbox component.
 * Similar to JCheckBox in Swing.
 */
public class CheckBox extends Component {
    private String label;
    
    public CheckBox() {
        super("div");
        initializeComponent("");
    }
    
    public CheckBox(String label) {
        super("div");
        initializeComponent(label);
    }
    
    private void initializeComponent(String label) {
        this.label = label;
        
        setStyle("display", "flex")
            .setStyle("align-items", "center")
            .setStyle("cursor", "pointer");
        
        Component input = new Component("input");
        input.setAttribute("type", "checkbox")
             .setStyle("margin-right", "8px");
        
        addChild(input);
        
        if (label != null && !label.isEmpty()) {
            Label labelComponent = new Label(label);
            addChild(labelComponent);
        }
        
        // Make the whole component clickable
        addEventListener("click", e -> {
            if (isEnabled()) {
                HTMLInputElement checkboxElement = (HTMLInputElement) getElement().getFirstChild();
                checkboxElement.setChecked(!checkboxElement.isChecked());
                
                // Manually trigger change event
                Event changeEvent = HTMLDocument.current().createEvent("Event");
                changeEvent.initEvent("change", true, true);
                checkboxElement.dispatchEvent(changeEvent);
            }
        });
    }
    
    public boolean isChecked() {
        if (getElement() != null) {
            HTMLInputElement checkboxElement = (HTMLInputElement) getElement().getFirstChild();
            return checkboxElement.isChecked();
        }
        return false;
    }
    
    public CheckBox setChecked(boolean checked) {
        if (getElement() != null) {
            HTMLInputElement checkboxElement = (HTMLInputElement) getElement().getFirstChild();
            checkboxElement.setChecked(checked);
        }
        return this;
    }
    
    public CheckBox setOnChange(Consumer<Event> handler) {
        if (getElement() != null) {
            HTMLInputElement checkboxElement = (HTMLInputElement) getElement().getFirstChild();
            checkboxElement.addEventListener("change", new EventListener<Event>() {
                @Override
                public void handleEvent(Event event) {
                    handler.accept(event);
                }
            });
        }
        return this;
    }
    
    public String getLabel() {
        return label;
    }
    
    public CheckBox setLabel(String label) {
        this.label = label;
        
        if (getElement() != null) {
            // Update the label if it exists
            if (getElement().getChildNodes().getLength() > 1) {
                Label labelComponent = (Label) getElement().getChildNodes().item(1);
                labelComponent.setText(label);
            } else if (label != null && !label.isEmpty()) {
                // Add a new label if it doesn't exist
                Label labelComponent = new Label(label);
                addChild(labelComponent);
            }
        }
        
        return this;
    }
    
    public boolean isEnabled() {
        if (getElement() != null) {
            return !getElement().hasAttribute("disabled");
        }
        return true;
    }
}