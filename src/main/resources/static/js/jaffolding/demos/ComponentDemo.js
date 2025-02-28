/**
 * Component demo for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the component demo page.
   * @returns {Component} - The component demo page
   */
  function create() {
    const mainPanel = new jaffolding.Component('div');
    
    // Header
    const header = new jaffolding.Component('div');
    header.setStyle('background-color', '#4285f4')
          .setStyle('color', 'white')
          .setStyle('padding', '20px')
          .setStyle('text-align', 'center');
    
    const title = new jaffolding.Component('h1');
    title.setText('UI Components Demo')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    // Buttons section
    const buttonSection = createButtonSection();
    
    // Layout section
    const layoutSection = createLayoutSection();
    
    // Add sections to content
    content.addChild(buttonSection);
    content.addChild(layoutSection);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(content);
    
    return mainPanel;
  }

  /**
   * Creates the button section.
   * @returns {Component} - The button section component
   */
  function createButtonSection() {
    const buttonSection = new jaffolding.Component('div');
    buttonSection.setStyle('margin-bottom', '30px');
    
    const buttonTitle = new jaffolding.Component('h2');
    buttonTitle.setText('Buttons')
               .setStyle('margin-bottom', '15px');
    
    const buttonContainer = new jaffolding.Component('div');
    buttonContainer.setStyle('display', 'flex')
                   .setStyle('flex-wrap', 'wrap')
                   .setStyle('gap', '10px');
    
    // Create different button types
    const primaryBtn = new jaffolding.Button('Primary');
    primaryBtn.setPrimary();
    
    const secondaryBtn = new jaffolding.Button('Secondary');
    secondaryBtn.setSecondary();
    
    const dangerBtn = new jaffolding.Button('Danger');
    dangerBtn.setDanger();
    
    const successBtn = new jaffolding.Button('Success');
    successBtn.setSuccess();
    
    const warningBtn = new jaffolding.Button('Warning');
    warningBtn.setWarning();
    
    const outlineBtn = new jaffolding.Button('Outline');
    outlineBtn.setOutline();
    
    buttonContainer.addChild(primaryBtn);
    buttonContainer.addChild(secondaryBtn);
    buttonContainer.addChild(dangerBtn);
    buttonContainer.addChild(successBtn);
    buttonContainer.addChild(warningBtn);
    buttonContainer.addChild(outlineBtn);
    
    buttonSection.addChild(buttonTitle);
    buttonSection.addChild(buttonContainer);
    
    return buttonSection;
  }

  /**
   * Creates the layout section.
   * @returns {Component} - The layout section component
   */
  function createLayoutSection() {
    const layoutSection = new jaffolding.Component('div');
    layoutSection.setStyle('margin-bottom', '30px');
    
    const layoutTitle = new jaffolding.Component('h2');
    layoutTitle.setText('Layouts')
               .setStyle('margin-bottom', '15px');
    
    // Border Layout demo
    const borderLayoutTitle = new jaffolding.Component('h3');
    borderLayoutTitle.setText('Border Layout')
                     .setStyle('margin-bottom', '10px');
    
    const borderLayoutDemo = createBorderLayoutDemo();
    
    // Grid Layout demo
    const gridLayoutTitle = new jaffolding.Component('h3');
    gridLayoutTitle.setText('Grid Layout')
                   .setStyle('margin-bottom', '10px')
                   .setStyle('margin-top', '20px');
    
    const gridLayoutDemo = createGridLayoutDemo();
    
    layoutSection.addChild(layoutTitle);
    layoutSection.addChild(borderLayoutTitle);
    layoutSection.addChild(borderLayoutDemo);
    layoutSection.addChild(gridLayoutTitle);
    layoutSection.addChild(gridLayoutDemo);
    
    return layoutSection;
  }

  /**
   * Creates a border layout demo.
   * @returns {Component} - The border layout demo component
   */
  function createBorderLayoutDemo() {
    const borderLayoutDemo = new jaffolding.Panel();
    borderLayoutDemo.setLayout(new jaffolding.BorderLayout(10, 10));
    borderLayoutDemo.setStyle('height', '300px')
                    .setStyle('border', '1px solid #ddd')
                    .setStyle('margin-bottom', '20px');
    
    const northPanel = new jaffolding.Panel();
    northPanel.setBackground('#e8f0fe')
              .setPadding('10px')
              .setTextAlign('center');
    
    const northLabel = new jaffolding.Component('div');
    northLabel.setText('NORTH');
    northPanel.addChild(northLabel);
    
    const southPanel = new jaffolding.Panel();
    southPanel.setBackground('#e8f0fe')
              .setPadding('10px')
              .setTextAlign('center');
    
    const southLabel = new jaffolding.Component('div');
    southLabel.setText('SOUTH');
    southPanel.addChild(southLabel);
    
    const eastPanel = new jaffolding.Panel();
    eastPanel.setBackground('#e8f0fe')
             .setPadding('10px')
             .setTextAlign('center');
    
    const eastLabel = new jaffolding.Component('div');
    eastLabel.setText('EAST');
    eastPanel.addChild(eastLabel);
    
    const westPanel = new jaffolding.Panel();
    westPanel.setBackground('#e8f0fe')
             .setPadding('10px')
             .setTextAlign('center');
    
    const westLabel = new jaffolding.Component('div');
    westLabel.setText('WEST');
    westPanel.addChild(westLabel);
    
    const centerPanel = new jaffolding.Panel();
    centerPanel.setBackground('#e8f0fe')
               .setPadding('10px')
               .setTextAlign('center');
    
    const centerLabel = new jaffolding.Component('div');
    centerLabel.setText('CENTER');
    centerPanel.addChild(centerLabel);
    
    borderLayoutDemo.addChild(northPanel, jaffolding.BorderLayout.NORTH);
    borderLayoutDemo.addChild(southPanel, jaffolding.BorderLayout.SOUTH);
    borderLayoutDemo.addChild(eastPanel, jaffolding.BorderLayout.EAST);
    borderLayoutDemo.addChild(westPanel, jaffolding.BorderLayout.WEST);
    borderLayoutDemo.addChild(centerPanel, jaffolding.BorderLayout.CENTER);
    
    return borderLayoutDemo;
  }

  /**
   * Creates a grid layout demo.
   * @returns {Component} - The grid layout demo component
   */
  function createGridLayoutDemo() {
    const gridLayoutDemo = new jaffolding.Panel();
    gridLayoutDemo.setLayout(new jaffolding.GridLayout(3, 3, 10, 10));
    gridLayoutDemo.setStyle('height', '300px')
                  .setStyle('border', '1px solid #ddd');
    
    for (let i = 1; i <= 9; i++) {
      const cell = new jaffolding.Panel();
      cell.setBackground('#e8f0fe')
          .setPadding('10px')
          .setTextAlign('center');
      
      const cellLabel = new jaffolding.Component('div');
      cellLabel.setText('Cell ' + i);
      cell.addChild(cellLabel);
      
      gridLayoutDemo.addChild(cell);
    }
    
    return gridLayoutDemo;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.ComponentDemo = {
    create: create
  };
})();