package com.danielremsburg.jaffolding.examples;

import java.util.Arrays;

import org.teavm.jso.dom.events.Event;

import com.danielremsburg.jaffolding.Component;
import com.danielremsburg.jaffolding.State;
import com.danielremsburg.jaffolding.ui.Button;
import com.danielremsburg.jaffolding.ui.CheckBox;
import com.danielremsburg.jaffolding.ui.ComboBox;
import com.danielremsburg.jaffolding.ui.Label;
import com.danielremsburg.jaffolding.ui.Panel;
import com.danielremsburg.jaffolding.ui.ProgressBar;
import com.danielremsburg.jaffolding.ui.TabPane;
import com.danielremsburg.jaffolding.ui.Table;
import com.danielremsburg.jaffolding.ui.TextArea;
import com.danielremsburg.jaffolding.ui.TextField;
import com.danielremsburg.jaffolding.ui.layout.BorderLayout;
import com.danielremsburg.jaffolding.ui.layout.GridLayout;

/**
 * Demo of various UI components.
 */
public class ComponentDemo {
    
    public Component createDemo() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        Panel header = new Panel();
        header.setStyle("background-color", "#4285f4")
              .setStyle("color", "white")
              .setStyle("padding", "20px")
              .setStyle("text-align", "center");
        
        Label title = new Label("UI Components Demo");
        title.setStyle("font-size", "24px")
             .setStyle("font-weight", "bold");
        
        header.addChild(title);
        
        // Create tabs for different component categories
        TabPane tabPane = new TabPane();
        tabPane.addTab("Basic Controls", createBasicControlsPanel())
               .addTab("Layout Demo", createLayoutDemoPanel())
               .addTab("Table Demo", createTableDemoPanel())
               .addTab("Form Demo", createFormDemoPanel());
        
        // Add components to the main panel
        mainPanel.addChild(header, BorderLayout.NORTH)
                 .addChild(tabPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private Component createBasicControlsPanel() {
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(0, 2, 20, 20));
        panel.setPadding("20px");
        
        // Labels
        Panel labelPanel = Panel.createTitledPanel("Labels");
        labelPanel.addChild(new Label("Regular Label"));
        
        Label heading1 = new Label("Heading 1");
        heading1.setHeading(1);
        labelPanel.addChild(heading1);
        
        Label heading3 = new Label("Heading 3");
        heading3.setHeading(3);
        labelPanel.addChild(heading3);
        
        Label styled = new Label("Bold, Italic, Underlined");
        styled.setBold().setItalic().setUnderline();
        labelPanel.addChild(styled);
        
        // Buttons
        Panel buttonPanel = Panel.createTitledPanel("Buttons");
        
        Button primaryBtn = new Button("Primary Button");
        primaryBtn.setPrimary();
        buttonPanel.addChild(primaryBtn);
        
        Button secondaryBtn = new Button("Secondary Button");
        secondaryBtn.setSecondary();
        buttonPanel.addChild(secondaryBtn);
        
        Button dangerBtn = new Button("Danger Button");
        dangerBtn.setDanger();
        buttonPanel.addChild(dangerBtn);
        
        Button successBtn = new Button("Success Button");
        successBtn.setSuccess();
        buttonPanel.addChild(successBtn);
        
        Button outlineBtn = new Button("Outline Button");
        outlineBtn.setOutline();
        buttonPanel.addChild(outlineBtn);
        
        // Text inputs
        Panel textInputPanel = Panel.createTitledPanel("Text Inputs");
        
        TextField textField = new TextField();
        textField.setPlaceholder("Enter text here");
        textInputPanel.addChild(textField);
        
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Enter multiple lines of text");
        textArea.setRows(4);
        textInputPanel.addChild(textArea);
        
        // Checkboxes
        Panel checkboxPanel = Panel.createTitledPanel("Checkboxes");
        
        CheckBox checkbox1 = new CheckBox("Option 1");
        checkboxPanel.addChild(checkbox1);
        
        CheckBox checkbox2 = new CheckBox("Option 2");
        checkbox2.setChecked(true);
        checkboxPanel.addChild(checkbox2);
        
        CheckBox disabledCheckbox = new CheckBox("Disabled Option");
        disabledCheckbox.setEnabled(false);
        checkboxPanel.addChild(disabledCheckbox);
        
        // ComboBox
        Panel comboBoxPanel = Panel.createTitledPanel("ComboBox");
        
        ComboBox comboBox = new ComboBox(Arrays.asList(
            "Option 1", 
            "Option 2", 
            "Option 3", 
            "Option 4"
        ));
        comboBox.setSelectedIndex(0);
        
        Label selectionLabel = new Label("Selected: Option 1");
        
        comboBox.setOnSelectionChange(index -> {
            selectionLabel.setText("Selected: " + comboBox.getSelectedItem());
        });
        
        comboBoxPanel.addChild(comboBox);
        comboBoxPanel.addChild(selectionLabel);
        
        // Progress bars
        Panel progressPanel = Panel.createTitledPanel("Progress Bars");
        
        ProgressBar progress1 = new ProgressBar(0, 100, 25);
        progressPanel.addChild(progress1);
        
        ProgressBar progress2 = new ProgressBar(0, 100, 50);
        progress2.setForeground("#34a853");
        progressPanel.addChild(progress2);
        
        ProgressBar progress3 = new ProgressBar();
        progress3.setIndeterminate(true);
        progressPanel.addChild(progress3);
        
        // Add all panels to the main panel
        panel.addChild(labelPanel);
        panel.addChild(buttonPanel);
        panel.addChild(textInputPanel);
        panel.addChild(checkboxPanel);
        panel.addChild(comboBoxPanel);
        panel.addChild(progressPanel);
        
        return panel;
    }
    
    private Component createLayoutDemoPanel() {
        Panel panel = new Panel();
        panel.setPadding("20px");
        
        // Border Layout Demo
        Panel borderLayoutDemo = Panel.createTitledPanel("Border Layout");
        borderLayoutDemo.setLayout(new BorderLayout(10, 10));
        borderLayoutDemo.setStyle("height", "300px");
        
        Panel north = new Panel();
        north.setBackground("#e8f0fe")
             .setPadding("10px")
             .setTextAlign("center")
             .addChild(new Label("NORTH"));
        
        Panel south = new Panel();
        south.setBackground("#e8f0fe")
             .setPadding("10px")
             .setTextAlign("center")
             .addChild(new Label("SOUTH"));
        
        Panel east = new Panel();
        east.setBackground("#e8f0fe")
             .setPadding("10px")
             .setTextAlign("center")
             .addChild(new Label("EAST"));
        
        Panel west = new Panel();
        west.setBackground("#e8f0fe")
             .setPadding("10px")
             .setTextAlign("center")
             .addChild(new Label("WEST"));
        
        Panel center = new Panel();
        center.setBackground("#e8f0fe")
              .setPadding("10px")
              .setTextAlign("center")
              .addChild(new Label("CENTER"));
        
        borderLayoutDemo.addChild(north, BorderLayout.NORTH)
                        .addChild(south, BorderLayout.SOUTH)
                        .addChild(east, BorderLayout.EAST)
                        .addChild(west, BorderLayout.WEST)
                        .addChild(center, BorderLayout.CENTER);
        
        // Grid Layout Demo
        Panel gridLayoutDemo = Panel.createTitledPanel("Grid Layout (3x3)");
        gridLayoutDemo.setLayout(new GridLayout(3, 3, 10, 10));
        gridLayoutDemo.setStyle("height", "300px");
        
        for (int i = 1; i <= 9; i++) {
            Panel cell = new Panel();
            cell.setBackground("#e8f0fe")
                .setPadding("10px")
                .setTextAlign("center")
                .addChild(new Label("Cell " + i));
            
            gridLayoutDemo.addChild(cell);
        }
        
        panel.addChild(borderLayoutDemo);
        panel.addChild(new Component("div").setStyle("height", "20px")); // Spacer
        panel.addChild(gridLayoutDemo);
        
        return panel;
    }
    
    private Component createTableDemoPanel() {
        Panel panel = new Panel();
        panel.setPadding("20px");
        
        // Create a table
        Table table = new Table(Arrays.asList("ID", "Name", "Email", "Role"));
        
        // Add some data
        table.addRow(Arrays.asList("1", "John Doe", "john@example.com", "Administrator"));
        table.addRow(Arrays.asList("2", "Jane Smith", "jane@example.com", "Developer"));
        table.addRow(Arrays.asList("3", "Bob Johnson", "bob@example.com", "Designer"));
        table.addRow(Arrays.asList("4", "Alice Williams", "alice@example.com", "Manager"));
        table.addRow(Arrays.asList("5", "Charlie Brown", "charlie@example.com", "Analyst"));
        
        // Add selection handling
        Label selectionLabel = new Label("No row selected");
        selectionLabel.setStyle("margin-top", "20px");
        
        table.setOnRowSelect(rowIndex -> {
            List<String> rowData = table.getData().get(rowIndex);
            selectionLabel.setText("Selected: " + rowData.get(1) + " (" + rowData.get(3) + ")");
        });
        
        // Add controls
        Panel controlPanel = new Panel();
        controlPanel.setStyle("margin-top", "20px")
                    .setStyle("display", "flex")
                    .setStyle("gap", "10px");
        
        Button addButton = new Button("Add Row");
        addButton.setPrimary();
        addButton.addEventListener("click", e -> {
            int nextId = table.getData().size() + 1;
            table.addRow(Arrays.asList(
                String.valueOf(nextId),
                "New User " + nextId,
                "user" + nextId + "@example.com",
                "Guest"
            ));
        });
        
        Button removeButton = new Button("Remove Selected");
        removeButton.setDanger();
        removeButton.addEventListener("click", e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                table.removeRow(selectedRow);
                selectionLabel.setText("No row selected");
            }
        });
        
        controlPanel.addChild(addButton);
        controlPanel.addChild(removeButton);
        
        panel.addChild(table);
        panel.addChild(selectionLabel);
        panel.addChild(controlPanel);
        
        return panel;
    }
    
    private Component createFormDemoPanel() {
        Panel panel = new Panel();
        panel.setPadding("20px");
        
        // Create a form
        Panel form = new Panel();
        form.setStyle("max-width", "500px")
            .setStyle("margin", "0 auto");
        
        Label formTitle = new Label("User Registration Form");
        formTitle.setHeading(2);
        
        // Name field
        Panel nameField = new Panel();
        nameField.setStyle("margin-bottom", "15px");
        
        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle("display", "block")
                 .setStyle("margin-bottom", "5px")
                 .setStyle("font-weight", "500");
        
        TextField nameInput = new TextField();
        nameInput.setPlaceholder("Enter your full name");
        
        nameField.addChild(nameLabel);
        nameField.addChild(nameInput);
        
        // Email field
        Panel emailField = new Panel();
        emailField.setStyle("margin-bottom", "15px");
        
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("display", "block")
                  .setStyle("margin-bottom", "5px")
                  .setStyle("font-weight", "500");
        
        TextField emailInput = new TextField();
        emailInput.setPlaceholder("Enter your email address");
        
        emailField.addChild(emailLabel);
        emailField.addChild(emailInput);
        
        // Role field
        Panel roleField = new Panel();
        roleField.setStyle("margin-bottom", "15px");
        
        Label roleLabel = new Label("Role");
        roleLabel.setStyle("display", "block")
                 .setStyle("margin-bottom", "5px")
                 .setStyle("font-weight", "500");
        
        ComboBox roleInput = new ComboBox(Arrays.asList(
            "Developer",
            "Designer",
            "Product Manager",
            "QA Engineer",
            "DevOps Engineer"
        ));
        
        roleField.addChild(roleLabel);
        roleField.addChild(roleInput);
        
        // Bio field
        Panel bioField = new Panel();
        bioField.setStyle("margin-bottom", "15px");
        
        Label bioLabel = new Label("Bio");
        bioLabel.setStyle("display", "block")
                .setStyle("margin-bottom", "5px")
                .setStyle("font-weight", "500");
        
        TextArea bioInput = new TextArea();
        bioInput.setPlaceholder("Tell us about yourself");
        bioInput.setRows(4);
        
        bioField.addChild(bioLabel);
        bioField.addChild(bioInput);
        
        // Options
        Panel optionsField = new Panel();
        optionsField.setStyle("margin-bottom", "20px");
        
        CheckBox newsletterCheckbox = new CheckBox("Subscribe to newsletter");
        CheckBox termsCheckbox = new CheckBox("I agree to the terms and conditions");
        
        optionsField.addChild(newsletterCheckbox);
        optionsField.addChild(termsCheckbox);
        
        // Submit button
        Button submitButton = new Button("Register");
        submitButton.setPrimary();
        submitButton.setStyle("width", "100%");
        
        // Form result
        Panel resultPanel = new Panel();
        resultPanel.setStyle("margin-top", "20px")
                   .setStyle("padding", "15px")
                   .setStyle("border", "1px solid #ddd")
                   .setStyle("border-radius", "4px")
                   .setStyle("background-color", "#f9f9f9")
                   .setStyle("display", "none");
        
        Label resultLabel = new Label("");
        resultPanel.addChild(resultLabel);
        
        // Handle form submission
        submitButton.addEventListener("click", e -> {
            if (nameInput.getValue().trim().isEmpty() || 
                emailInput.getValue().trim().isEmpty() ||
                !termsCheckbox.isChecked()) {
                
                resultPanel.setStyle("display", "block")
                           .setStyle("background-color", "#ffebee")
                           .setStyle("border-color", "#ffcdd2");
                resultLabel.setText("Please fill in all required fields and accept the terms.");
                return;
            }
            
            resultPanel.setStyle("display", "block")
                       .setStyle("background-color", "#e8f5e9")
                       .setStyle("border-color", "#c8e6c9");
            
            StringBuilder result = new StringBuilder();
            result.append("Registration successful!\n\n");
            result.append("Name: ").append(nameInput.getValue()).append("\n");
            result.append("Email: ").append(emailInput.getValue()).append("\n");
            result.append("Role: ").append(roleInput.getSelectedItem()).append("\n");
            result.append("Newsletter: ").append(newsletterCheckbox.isChecked() ? "Yes" : "No");
            
            resultLabel.setText(result.toString());
        });
        
        // Add all fields to the form
        form.addChild(formTitle);
        form.addChild(nameField);
        form.addChild(emailField);
        form.addChild(roleField);
        form.addChild(bioField);
        form.addChild(optionsField);
        form.addChild(submitButton);
        form.addChild(resultPanel);
        
        panel.addChild(form);
        
        return panel;
    }
}