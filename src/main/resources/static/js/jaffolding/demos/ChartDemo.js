/**
 * Chart demo for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the chart demo page.
   * @returns {Component} - The chart demo page
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
    title.setText('Chart.js Integration Demo')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    // Chart container
    const chartContainer = new jaffolding.Component('div');
    chartContainer.setStyle('padding', '20px')
                  .setStyle('display', 'flex')
                  .setStyle('flex-direction', 'column')
                  .setStyle('align-items', 'center');
    
    // Create chart with fixed dimensions
    const chartWrapper = new jaffolding.Component('div');
    chartWrapper.setStyle('width', '100%')
                .setStyle('max-width', '800px')
                .setStyle('height', '400px')
                .setStyle('position', 'relative');
    
    // Create chart
    const chart = new jaffolding.ChartComponent();
    chart.setStyle('width', '100%')
         .setStyle('height', '100%')
         .setStyle('position', 'absolute')
         .setStyle('top', '0')
         .setStyle('left', '0');
    chart.setType('bar');
    
    // Add sample data
    const labels = ['January', 'February', 'March', 'April', 'May', 'June'];
    const data1 = [65, 59, 80, 81, 56, 55];
    const data2 = [28, 48, 40, 19, 86, 27];
    
    chart.setLabels(labels);
    chart.addDataset('Dataset 1', data1, 'rgba(75, 192, 192, 0.2)', 'rgba(75, 192, 192, 1)');
    chart.addDataset('Dataset 2', data2, 'rgba(255, 99, 132, 0.2)', 'rgba(255, 99, 132, 1)');
    
    chartWrapper.addChild(chart);
    
    // Controls
    const controls = createChartControls(chart, labels);
    
    chartContainer.addChild(chartWrapper);
    chartContainer.addChild(controls);
    
    content.addChild(chartContainer);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(content);
    
    return mainPanel;
  }

  /**
   * Creates chart controls.
   * @param {ChartComponent} chart - The chart component
   * @param {string[]} labels - The chart labels
   * @returns {Component} - The controls component
   */
  function createChartControls(chart, labels) {
    const controls = new jaffolding.Component('div');
    controls.setStyle('margin-top', '20px')
            .setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('width', '100%')
            .setStyle('max-width', '800px')
            .setStyle('justify-content', 'center');
    
    // Chart type selector
    const chartTypeSelector = new jaffolding.Component('select');
    chartTypeSelector.setStyle('padding', '8px')
                     .setStyle('border-radius', '4px')
                     .setStyle('border', '1px solid #ddd');
    
    const chartTypes = ['bar', 'line', 'pie', 'doughnut', 'radar', 'polarArea'];
    
    chartTypes.forEach(type => {
      const option = new jaffolding.Component('option');
      option.setText(type);
      option.setAttribute('value', type);
      chartTypeSelector.addChild(option);
    });
    
    chartTypeSelector.addEventListener('change', e => {
      chart.setType(e.target.value);
    });
    
    // Random data button
    const randomDataBtn = new jaffolding.Button('Generate Random Data');
    randomDataBtn.setPrimary();
    randomDataBtn.addEventListener('click', () => {
      const newData1 = labels.map(() => Math.floor(Math.random() * 100));
      const newData2 = labels.map(() => Math.floor(Math.random() * 100));
      
      chart.datasets = [];
      chart.addDataset('Dataset 1', newData1, 'rgba(75, 192, 192, 0.2)', 'rgba(75, 192, 192, 1)');
      chart.addDataset('Dataset 2', newData2, 'rgba(255, 99, 132, 0.2)', 'rgba(255, 99, 132, 1)');
      
      if (chart.chart) {
        chart.updateChart();
      }
    });
    
    controls.addChild(chartTypeSelector);
    controls.addChild(randomDataBtn);
    
    return controls;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.ChartDemo = {
    create: create
  };
})();