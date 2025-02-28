/**
 * Desktop environment demo for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the desktop environment demo page.
   * @returns {Component} - The desktop demo page
   */
  function create() {
    const mainPanel = new jaffolding.Component('div');
    mainPanel.setStyle('height', '100vh')
             .setStyle('margin', '-20px')
             .setStyle('overflow', 'hidden');
    
    // Create desktop
    const desktop = new jaffolding.Desktop();
    
    // Add a welcome window
    const welcomeWindow = createWelcomeWindow();
    desktop.addWindow(welcomeWindow);
    
    // Add a chart window
    const chartWindow = createChartWindow();
    chartWindow.setPosition(100, 150);
    desktop.addWindow(chartWindow);
    
    // Add a data table window
    const tableWindow = createTableWindow();
    tableWindow.setPosition(500, 100);
    desktop.addWindow(tableWindow);
    
    // Add a calculator window
    const calculatorWindow = createCalculatorWindow();
    calculatorWindow.setPosition(300, 200);
    desktop.addWindow(calculatorWindow);
    
    // Add a button to create new windows
    const controls = new jaffolding.Component('div');
    controls.setStyle('position', 'absolute')
            .setStyle('top', '20px')
            .setStyle('right', '20px')
            .setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('flex-wrap', 'wrap')
            .setStyle('max-width', '200px')
            .setStyle('justify-content', 'flex-end');
    
    const newChartBtn = new jaffolding.Button('New Chart');
    newChartBtn.setPrimary();
    newChartBtn.addEventListener('click', () => {
      const newWindow = createChartWindow();
      newWindow.setPosition(Math.floor(Math.random() * 400), Math.floor(Math.random() * 200));
      desktop.addWindow(newWindow);
    });
    
    const newTableBtn = new jaffolding.Button('New Table');
    newTableBtn.setSuccess();
    newTableBtn.addEventListener('click', () => {
      const newWindow = createTableWindow();
      newWindow.setPosition(Math.floor(Math.random() * 400), Math.floor(Math.random() * 200));
      desktop.addWindow(newWindow);
    });
    
    const newCalcBtn = new jaffolding.Button('Calculator');
    newCalcBtn.setStyle('background-color', '#673ab7')
              .setStyle('color', 'white');
    newCalcBtn.addEventListener('click', () => {
      const newWindow = createCalculatorWindow();
      newWindow.setPosition(Math.floor(Math.random() * 400), Math.floor(Math.random() * 200));
      desktop.addWindow(newWindow);
    });
    
    controls.addChild(newChartBtn);
    controls.addChild(newTableBtn);
    controls.addChild(newCalcBtn);
    
    desktop.addChild(controls);
    
    // Add mobile instructions
    const mobileInstructions = new jaffolding.Component('div');
    mobileInstructions.setStyle('position', 'absolute')
                      .setStyle('top', '20px')
                      .setStyle('left', '20px')
                      .setStyle('background-color', 'rgba(0, 0, 0, 0.7)')
                      .setStyle('color', 'white')
                      .setStyle('padding', '10px 15px')
                      .setStyle('border-radius', '8px')
                      .setStyle('font-size', '14px')
                      .setStyle('max-width', '300px')
                      .setStyle('z-index', '1000')
                      .setStyle('backdrop-filter', 'blur(5px)')
                      .setText('Swipe left/right to navigate between windows. Use the navigation bar at the bottom for quick access.');
    
    // Only show on mobile
    if (window.innerWidth < 768) {
      desktop.addChild(mobileInstructions);
      
      // Auto-hide after 5 seconds
      setTimeout(() => {
        if (mobileInstructions.getElement()) {
          jaffolding.Animation.fadeOut(mobileInstructions, 0.5);
          setTimeout(() => {
            mobileInstructions.removeFromParent();
          }, 500);
        }
      }, 5000);
    }
    
    // Apply animations
    desktop.addEventListener('DOMNodeInserted', () => {
      if (desktop.getElement()) {
        jaffolding.Animation.fadeIn(desktop, 0.5);
      }
    });
    
    mainPanel.addChild(desktop);
    
    return mainPanel;
  }

  /**
   * Creates a welcome window.
   * @returns {Window} - The welcome window
   */
  function createWelcomeWindow() {
    const content = new jaffolding.Panel();
    content.setLayout(new jaffolding.BorderLayout());
    content.setPadding('20px');
    
    const title = new jaffolding.Component('h2');
    title.setText('Welcome to Jaffolding Desktop')
         .setStyle('margin-bottom', '15px');
    
    const description = new jaffolding.Component('p');
    description.setText(
      'This is a demonstration of the window and desktop components. ' +
      'You can drag windows by their title bars, resize them using the bottom-right corner, ' +
      'and minimize, maximize, or close them using the buttons in the top-right corner.'
    )
    .setStyle('line-height', '1.5');
    
    const mobileInfo = new jaffolding.Component('p');
    mobileInfo.setText(
      'On mobile devices, you can use swipe gestures to navigate between windows. ' +
      'The navigation bar at the bottom provides quick access to windows and the calculator.'
    )
    .setStyle('line-height', '1.5')
    .setStyle('margin-top', '10px')
    .setStyle('padding', '10px')
    .setStyle('background-color', '#e8f0fe')
    .setStyle('border-left', '4px solid #4285f4')
    .setStyle('border-radius', '4px');
    
    const buttonPanel = new jaffolding.Panel();
    buttonPanel.setStyle('margin-top', '20px')
               .setStyle('display', 'flex')
               .setStyle('justify-content', 'center');
    
    const getStartedBtn = new jaffolding.Button('Get Started');
    getStartedBtn.setPrimary();
    getStartedBtn.setStyle('padding', '10px 20px');
    
    buttonPanel.addChild(getStartedBtn);
    
    const centerPanel = new jaffolding.Panel();
    centerPanel.addChild(description);
    centerPanel.addChild(mobileInfo);
    
    content.addChild(title, jaffolding.BorderLayout.NORTH);
    content.addChild(centerPanel, jaffolding.BorderLayout.CENTER);
    content.addChild(buttonPanel, jaffolding.BorderLayout.SOUTH);
    
    const window = new jaffolding.Window('Welcome', content);
    window.setSize(400, 350);
    window.setPosition(50, 50);
    
    return window;
  }

  /**
   * Creates a chart window.
   * @returns {Window} - The chart window
   */
  function createChartWindow() {
    const content = new jaffolding.Panel();
    content.setLayout(new jaffolding.BorderLayout());
    content.setPadding('15px');
    
    const title = new jaffolding.Component('h3');
    title.setText('Sales Chart')
         .setStyle('font-size', '18px')
         .setStyle('font-weight', 'bold')
         .setStyle('margin-bottom', '15px');
    
    // Create chart wrapper with fixed dimensions
    const chartWrapper = new jaffolding.Component('div');
    chartWrapper.setStyle('width', '100%')
                .setStyle('height', '300px')
                .setStyle('position', 'relative');
    
    // Create chart
    const chart = new jaffolding.ChartComponent();
    chart.setStyle('position', 'absolute')
         .setStyle('top', '0')
         .setStyle('left', '0')
         .setStyle('width', '100%')
         .setStyle('height', '100%');
    chart.setType('bar');
    
    // Add sample data
    const labels = ['January', 'February', 'March', 'April', 'May', 'June'];
    const data1 = [65, 59, 80, 81, 56, 55];
    const data2 = [28, 48, 40, 19, 86, 27];
    
    chart.setLabels(labels);
    chart.addDataset('Dataset 1', data1, 'rgba(75, 192, 192, 0.2)', 'rgba(75, 192, 192, 1)');
    chart.addDataset('Dataset 2', data2, 'rgba(255, 99, 132, 0.2)', 'rgba(255, 99, 132, 1)');
    
    chartWrapper.addChild(chart);
    
    content.addChild(title, jaffolding.BorderLayout.NORTH);
    content.addChild(chartWrapper, jaffolding.BorderLayout.CENTER);
    
    const window = new jaffolding.Window('Chart Demo', content);
    window.setSize(500, 400);
    
    return window;
  }

  /**
   * Creates a table window.
   * @returns {Window} - The table window
   */
  function createTableWindow() {
    const content = new jaffolding.Panel();
    content.setLayout(new jaffolding.BorderLayout());
    content.setPadding('15px');
    
    const title = new jaffolding.Component('h3');
    title.setText('Sales Data')
         .setStyle('font-size', '18px')
         .setStyle('font-weight', 'bold')
         .setStyle('margin-bottom', '15px');
    
    // Create data table
    const dataTable = new jaffolding.DataTable(['Product', 'Category', 'Sales', 'Revenue', 'Month']);
    dataTable.setColumnType('Sales', 'number');
    dataTable.setColumnType('Revenue', 'number');
    
    // Add sample data
    const tableData = [
      {
        Product: 'Laptop',
        Category: 'Electronics',
        Sales: 120,
        Revenue: 120000,
        Month: 'January'
      },
      {
        Product: 'Smartphone',
        Category: 'Electronics',
        Sales: 200,
        Revenue: 100000,
        Month: 'January'
      },
      {
        Product: 'Headphones',
        Category: 'Accessories',
        Sales: 150,
        Revenue: 15000,
        Month: 'January'
      }
    ];
    
    dataTable.setData(tableData);
    
    content.addChild(title, jaffolding.BorderLayout.NORTH);
    content.addChild(dataTable, jaffolding.BorderLayout.CENTER);
    
    const window = new jaffolding.Window('Table Demo', content);
    window.setSize(600, 400);
    
    return window;
  }

  /**
   * Creates a calculator window.
   * @returns {Window} - The calculator window
   */
  function createCalculatorWindow() {
    const content = new jaffolding.Panel();
    content.setLayout(new jaffolding.BorderLayout());
    
    // Create calculator component
    const calculator = new jaffolding.Calculator();
    
    content.addChild(calculator, jaffolding.BorderLayout.CENTER);
    
    const window = new jaffolding.Window('Calculator', content);
    window.setSize(300, 400);
    
    return window;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.DesktopDemo = {
    create: create
  };
})();