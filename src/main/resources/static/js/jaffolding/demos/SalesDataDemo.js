/**
 * Sales data visualization demo for the Jaffolding framework.
 */
(function() {
  // State for the demo
  const salesData = new jaffolding.State([]);
  const filteredData = new jaffolding.State([]);
  const categories = new jaffolding.State([]);
  const products = new jaffolding.State([]);
  const months = new jaffolding.State([]);
  const selectedCategory = new jaffolding.State('All');
  const selectedMonth = new jaffolding.State('All');
  const chartType = new jaffolding.State('bar');

  /**
   * Creates the sales data visualization demo page.
   * @returns {Component} - The sales data demo page
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
    title.setText('Sales Data Visualization')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    // Filters panel
    const filtersPanel = createFiltersPanel();
    
    // Main content area with table and chart
    const mainContent = new jaffolding.Component('div');
    mainContent.setStyle('display', 'grid')
               .setStyle('grid-template-columns', '1fr 1fr')
               .setStyle('gap', '20px')
               .setStyle('margin-top', '20px');
    
    // Table panel
    const tablePanel = createTablePanel();
    
    // Chart panel
    const chartPanel = createChartPanel();
    
    mainContent.addChild(tablePanel);
    mainContent.addChild(chartPanel);
    
    content.addChild(filtersPanel);
    content.addChild(mainContent);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(content);
    
    // Load initial data
    loadData();
    loadCategories();
    loadMonths();
    loadProducts();
    
    // Set up data filtering
    salesData.subscribe(data => {
      filterData();
    });
    
    // Apply animations
    mainPanel.addEventListener('DOMNodeInserted', () => {
      if (mainPanel.getElement()) {
        jaffolding.Animation.fadeIn(header, 0.5);
        jaffolding.Animation.slideIn(filtersPanel, 'top', 0.5);
        jaffolding.Animation.slideIn(tablePanel, 'left', 0.5);
        jaffolding.Animation.slideIn(chartPanel, 'right', 0.5);
      }
    });
    
    return mainPanel;
  }

  /**
   * Creates the filters panel.
   * @returns {Component} - The filters panel component
   */
  function createFiltersPanel() {
    const filtersPanel = new jaffolding.Component('div');
    filtersPanel.setStyle('display', 'grid')
                .setStyle('grid-template-columns', 'repeat(4, 1fr)')
                .setStyle('gap', '20px')
                .setStyle('margin-bottom', '20px')
                .setStyle('padding', '15px')
                .setStyle('background-color', '#f5f5f5')
                .setStyle('border-radius', '4px');
    
    // Category filter
    const categoryPanel = new jaffolding.Component('div');
    categoryPanel.setStyle('display', 'flex')
                 .setStyle('flex-direction', 'column')
                 .setStyle('gap', '8px');
    
    const categoryLabel = new jaffolding.Component('label');
    categoryLabel.setText('Category:');
    
    const categorySelector = new jaffolding.Component('select');
    categorySelector.setStyle('padding', '8px')
                    .setStyle('border-radius', '4px')
                    .setStyle('border', '1px solid #ddd');
    
    // Add "All" option
    const allCategoryOption = new jaffolding.Component('option');
    allCategoryOption.setText('All');
    allCategoryOption.setAttribute('value', 'All');
    categorySelector.addChild(allCategoryOption);
    
    // Subscribe to categories state
    categories.subscribe(cats => {
      // Clear existing options except "All"
      const element = categorySelector.getElement();
      if (element) {
        while (element.children.length > 1) {
          element.removeChild(element.lastChild);
        }
      }
      
      // Add category options
      cats.forEach(cat => {
        const option = new jaffolding.Component('option');
        option.setText(cat);
        option.setAttribute('value', cat);
        categorySelector.addChild(option);
      });
    });
    
    categorySelector.addEventListener('change', e => {
      selectedCategory.set(e.target.value);
      filterData();
    });
    
    categoryPanel.addChild(categoryLabel);
    categoryPanel.addChild(categorySelector);
    
    // Month filter
    const monthPanel = new jaffolding.Component('div');
    monthPanel.setStyle('display', 'flex')
              .setStyle('flex-direction', 'column')
              .setStyle('gap', '8px');
    
    const monthLabel = new jaffolding.Component('label');
    monthLabel.setText('Month:');
    
    const monthSelector = new jaffolding.Component('select');
    monthSelector.setStyle('padding', '8px')
                 .setStyle('border-radius', '4px')
                 .setStyle('border', '1px solid #ddd');
    
    // Add "All" option
    const allMonthOption = new jaffolding.Component('option');
    allMonthOption.setText('All');
    allMonthOption.setAttribute('value', 'All');
    monthSelector.addChild(allMonthOption);
    
    // Subscribe to months state
    months.subscribe(ms => {
      // Clear existing options except "All"
      const element = monthSelector.getElement();
      if (element) {
        while (element.children.length > 1) {
          element.removeChild(element.lastChild);
        }
      }
      
      // Add month options
      ms.forEach(m => {
        const option = new jaffolding.Component('option');
        option.setText(m);
        option.setAttribute('value', m);
        monthSelector.addChild(option);
      });
    });
    
    monthSelector.addEventListener('change', e => {
      selectedMonth.set(e.target.value);
      filterData();
    });
    
    monthPanel.addChild(monthLabel);
    monthPanel.addChild(monthSelector);
    
    // Chart type selector
    const chartTypePanel = new jaffolding.Component('div');
    chartTypePanel.setStyle('display', 'flex')
                  .setStyle('flex-direction', 'column')
                  .setStyle('gap', '8px');
    
    const chartTypeLabel = new jaffolding.Component('label');
    chartTypeLabel.setText('Chart Type:');
    
    const chartTypeSelector = new jaffolding.Component('select');
    chartTypeSelector.setStyle('padding', '8px')
                     .setStyle('border-radius', '4px')
                     .setStyle('border', '1px solid #ddd');
    
    const chartTypes = ['bar', 'line', 'pie', 'doughnut', 'polarArea'];
    
    chartTypes.forEach(type => {
      const option = new jaffolding.Component('option');
      option.setText(type);
      option.setAttribute('value', type);
      chartTypeSelector.addChild(option);
    });
    
    chartTypeSelector.addEventListener('change', e => {
      chartType.set(e.target.value);
    });
    
    chartTypePanel.addChild(chartTypeLabel);
    chartTypePanel.addChild(chartTypeSelector);
    
    // Search
    const searchPanel = new jaffolding.Component('div');
    searchPanel.setStyle('display', 'flex')
               .setStyle('flex-direction', 'column')
               .setStyle('gap', '8px');
    
    const searchLabel = new jaffolding.Component('label');
    searchLabel.setText('Search:');
    
    const searchField = new jaffolding.Component('input');
    searchField.setAttribute('type', 'text')
               .setAttribute('placeholder', 'Search products...')
               .setStyle('padding', '8px')
               .setStyle('border-radius', '4px')
               .setStyle('border', '1px solid #ddd');
    
    searchField.addEventListener('input', e => {
      const searchText = e.target.value.toLowerCase();
      if (searchText === '') {
        filterData();
      } else {
        const filtered = salesData.get().filter(row => {
          const product = String(row.product).toLowerCase();
          return product.includes(searchText);
        });
        filteredData.set(filtered);
      }
    });
    
    searchPanel.addChild(searchLabel);
    searchPanel.addChild(searchField);
    
    // Add all filters to the panel
    filtersPanel.addChild(categoryPanel);
    filtersPanel.addChild(monthPanel);
    filtersPanel.addChild(chartTypePanel);
    filtersPanel.addChild(searchPanel);
    
    return filtersPanel;
  }

  /**
   * Creates the table panel.
   * @returns {Component} - The table panel component
   */
  function createTablePanel() {
    const tablePanel = new jaffolding.Component('div');
    tablePanel.setStyle('background-color', 'white')
              .setStyle('border-radius', '4px')
              .setStyle('box-shadow', '0 2px 4px rgba(0, 0, 0, 0.1)')
              .setStyle('padding', '15px');
    
    const tableTitle = new jaffolding.Component('h2');
    tableTitle.setText('Sales Data')
              .setStyle('font-size', '18px')
              .setStyle('font-weight', 'bold')
              .setStyle('margin-bottom', '15px');
    
    // Create data table
    const dataTable = new jaffolding.DataTable(['Product', 'Category', 'Sales', 'Revenue', 'Month']);
    dataTable.setColumnType('Sales', 'number');
    dataTable.setColumnType('Revenue', 'number');
    
    // Bind to filtered data state
    filteredData.subscribe(data => {
      const tableData = data.map(item => ({
        Product: item.product,
        Category: item.category,
        Sales: item.sales,
        Revenue: item.revenue,
        Month: item.month
      }));
      dataTable.setData(tableData);
    });
    
    // Add table controls
    const tableControls = new jaffolding.Component('div');
    tableControls.setStyle('display', 'grid')
                 .setStyle('grid-template-columns', '1fr 1fr')
                 .setStyle('gap', '10px')
                 .setStyle('margin-top', '15px');
    
    const refreshButton = new jaffolding.Button('Refresh Data');
    refreshButton.setPrimary();
    refreshButton.addEventListener('click', () => {
      loadData();
    });
    
    const addButton = new jaffolding.Button('Add Random Sale');
    addButton.setSuccess();
    addButton.addEventListener('click', () => {
      addRandomSale();
    });
    
    tableControls.addChild(refreshButton);
    tableControls.addChild(addButton);
    
    tablePanel.addChild(tableTitle);
    tablePanel.addChild(dataTable);
    tablePanel.addChild(tableControls);
    
    return tablePanel;
  }

  /**
   * Creates the chart panel.
   * @returns {Component} - The chart panel component
   */
  function createChartPanel() {
    const chartPanel = new jaffolding.Component('div');
    chartPanel.setStyle('background-color', 'white')
              .setStyle('border-radius', '4px')
              .setStyle('box-shadow', '0 2px 4px rgba(0, 0, 0, 0.1)')
              .setStyle('padding', '15px');
    
    const chartTitle = new jaffolding.Component('h2');
    chartTitle.setText('Sales Visualization')
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
    
    chartWrapper.addChild(chart);
    
    // Bind chart to filtered data
    filteredData.subscribe(data => {
      // Extract labels and datasets
      const productLabels = [...new Set(data.map(item => item.product))];
      
      // Group data by product
      const salesByProduct = {};
      data.forEach(item => {
        salesByProduct[item.product] = (salesByProduct[item.product] || 0) + item.sales;
      });
      
      // Create dataset
      const salesData = productLabels.map(product => salesByProduct[product]);
      
      // Update chart
      chart.setLabels(productLabels);
      chart.datasets = [];
      chart.addDataset('Sales', salesData, 'rgba(75, 192, 192, 0.2)', 'rgba(75, 192, 192, 1)');
      
      if (chart.chart) {
        chart.updateChart();
      }
    });
    
    // Update chart type when changed
    chartType.subscribe(type => {
      chart.setType(type);
    });
    
    chartPanel.addChild(chartTitle);
    chartPanel.addChild(chartWrapper);
    
    return chartPanel;
  }

  /**
   * Loads sales data from the API.
   */
  function loadData() {
    fetch('/api/sales')
      .then(response => response.json())
      .then(data => {
        salesData.set(data);
      })
      .catch(error => {
        console.error('Error loading sales data:', error);
        // Fallback to mock data
        salesData.set(createMockData());
      });
  }

  /**
   * Loads categories from the API.
   */
  function loadCategories() {
    fetch('/api/categories')
      .then(response => response.json())
      .then(data => {
        categories.set(data);
      })
      .catch(error => {
        console.error('Error loading categories:', error);
        // Fallback to mock data
        categories.set(['Electronics', 'Accessories', 'Software', 'Services']);
      });
  }

  /**
   * Loads months from the API.
   */
  function loadMonths() {
    fetch('/api/months')
      .then(response => response.json())
      .then(data => {
        months.set(data);
      })
      .catch(error => {
        console.error('Error loading months:', error);
        // Fallback to mock data
        months.set(['January', 'February', 'March', 'April', 'May', 'June', 
                   'July', 'August', 'September', 'October', 'November', 'December']);
      });
  }

  /**
   * Loads products from the API.
   */
  function loadProducts() {
    fetch('/api/products')
      .then(response => response.json())
      .then(data => {
        products.set(data);
      })
      .catch(error => {
        console.error('Error loading products:', error);
        // Fallback to mock data
        products.set(['Laptop', 'Smartphone', 'Headphones', 'Monitor', 'Keyboard', 'Mouse', 'Tablet', 'Printer']);
      });
  }

  /**
   * Filters the data based on selected category and month.
   */
  function filterData() {
    const data = salesData.get();
    const category = selectedCategory.get();
    const month = selectedMonth.get();
    
    const filtered = data.filter(item => {
      const categoryMatch = category === 'All' || item.category === category;
      const monthMatch = month === 'All' || item.month === month;
      return categoryMatch && monthMatch;
    });
    
    filteredData.set(filtered);
  }

  /**
   * Adds a random sale to the data.
   */
  function addRandomSale() {
    const productList = products.get();
    const categoryList = categories.get();
    const monthList = months.get();
    
    if (productList.length === 0 || categoryList.length === 0 || monthList.length === 0) {
      return;
    }
    
    // Generate random sale
    const product = productList[Math.floor(Math.random() * productList.length)];
    const category = categoryList[Math.floor(Math.random() * categoryList.length)];
    const month = monthList[Math.floor(Math.random() * monthList.length)];
    const sales = 50 + Math.floor(Math.random() * 200);
    const revenue = sales * (100 + Math.random() * 400);
    
    const newSale = {
      id: salesData.get().length + 1,
      product,
      category,
      sales,
      revenue,
      month
    };
    
    // Add to local data
    const newData = [...salesData.get(), newSale];
    salesData.set(newData);
    
    // Post to server
    fetch('/api/sales', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newSale)
    }).catch(error => {
      console.error('Error adding sale:', error);
    });
  }

  /**
   * Creates mock sales data.
   * @returns {Object[]} - The mock data
   */
  function createMockData() {
    return [
      { id: 1, product: 'Laptop', category: 'Electronics', sales: 120, revenue: 120000, month: 'January' },
      { id: 2, product: 'Smartphone', category: 'Electronics', sales: 200, revenue: 100000, month: 'January' },
      { id: 3, product: 'Headphones', category: 'Accessories', sales: 150, revenue: 15000, month: 'January' },
      { id: 4, product: 'Monitor', category: 'Electronics', sales: 80, revenue: 24000, month: 'January' },
      { id: 5, product: 'Keyboard', category: 'Accessories', sales: 100, revenue: 5000, month: 'January' },
      { id: 6, product: 'Laptop', category: 'Electronics', sales: 130, revenue: 130000, month: 'February' },
      { id: 7, product: 'Smartphone', category: 'Electronics', sales: 180, revenue: 90000, month: 'February' },
      { id: 8, product: 'Headphones', category: 'Accessories', sales: 170, revenue: 17000, month: 'February' },
      { id: 9, product: 'Monitor', category: 'Electronics', sales: 85, revenue: 25500, month: 'February' },
      { id: 10, product: 'Keyboard', category: 'Accessories', sales: 110, revenue: 5500, month: 'February' }
    ];
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.SalesDataDemo = {
    create: create
  };
})();