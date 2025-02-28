/**
 * A chart component that integrates with Chart.js.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class ChartComponent extends jaffolding.Component {
    /**
     * Creates a new ChartComponent instance.
     */
    constructor() {
      super('canvas');
      this.type = 'bar';
      this.labels = [];
      this.datasets = [];
      this.options = {};
      this.chart = null;
      this.dataState = null;
      this.labelField = null;
      this.valueField = null;
      this.categoryField = null;
      this.labelExtractor = null;
      this.valueExtractor = null;
      this.categoryExtractor = null;
      
      this.initializeStyles();
    }

    /**
     * Initializes the chart styles.
     * @private
     */
    initializeStyles() {
      this.setStyle('width', '100%')
          .setStyle('height', '100%');
    }

    /**
     * Sets the chart type.
     * @param {string} type - The chart type (e.g., 'bar', 'line', 'pie')
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setType(type) {
      this.type = type;
      if (this.chart) {
        this.chart.destroy();
        this.createChart();
      }
      return this;
    }

    /**
     * Sets the chart labels.
     * @param {string[]} labels - The chart labels
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setLabels(labels) {
      this.labels = [...labels];
      if (this.chart) {
        this.updateChart();
      }
      return this;
    }

    /**
     * Adds a dataset to the chart.
     * @param {string} label - The dataset label
     * @param {number[]} data - The dataset values
     * @param {string} backgroundColor - The background color
     * @param {string} borderColor - The border color
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    addDataset(label, data, backgroundColor, borderColor) {
      this.datasets.push({
        label,
        data: [...data],
        backgroundColor,
        borderColor,
        borderWidth: 1
      });
      
      if (this.chart) {
        this.updateChart();
      }
      
      return this;
    }

    /**
     * Sets the chart options.
     * @param {Object} options - The chart options
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setOptions(options) {
      this.options = {...options};
      if (this.chart) {
        this.updateChart();
      }
      return this;
    }

    /**
     * Binds the chart to a data state.
     * @param {State} dataState - The data state
     * @param {string} labelField - The field to use for labels
     * @param {string} valueField - The field to use for values
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    bindToDataState(dataState, labelField, valueField) {
      this.dataState = dataState;
      this.labelField = labelField;
      this.valueField = valueField;
      
      // Default extractors
      this.labelExtractor = data => data[labelField];
      this.valueExtractor = data => data[valueField];
      
      // Subscribe to data changes
      dataState.subscribe(data => this.updateChartFromData(data));
      
      return this;
    }

    /**
     * Binds the chart to a data state with category grouping.
     * @param {State} dataState - The data state
     * @param {string} labelField - The field to use for labels
     * @param {string} valueField - The field to use for values
     * @param {string} categoryField - The field to use for categories
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    bindToDataStateWithCategory(dataState, labelField, valueField, categoryField) {
      this.bindToDataState(dataState, labelField, valueField);
      this.categoryField = categoryField;
      this.categoryExtractor = data => data[categoryField];
      
      return this;
    }

    /**
     * Sets the label extractor function.
     * @param {Function} extractor - The label extractor function
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setLabelExtractor(extractor) {
      this.labelExtractor = extractor;
      return this;
    }

    /**
     * Sets the value extractor function.
     * @param {Function} extractor - The value extractor function
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setValueExtractor(extractor) {
      this.valueExtractor = extractor;
      return this;
    }

    /**
     * Sets the category extractor function.
     * @param {Function} extractor - The category extractor function
     * @returns {ChartComponent} - The chart component instance for chaining
     */
    setCategoryExtractor(extractor) {
      this.categoryExtractor = extractor;
      return this;
    }

    /**
     * Updates the chart from data state.
     * @param {Array} data - The data array
     * @private
     */
    updateChartFromData(data) {
      if (!data || data.length === 0) {
        return;
      }
      
      // Extract labels and organize data
      const newLabels = [];
      const categoryData = {};
      
      if (this.categoryField) {
        // For grouped data (multiple datasets)
        for (const item of data) {
          const label = this.labelExtractor(item);
          const category = this.categoryExtractor(item);
          const value = this.valueExtractor(item);
          
          if (!newLabels.includes(label)) {
            newLabels.push(label);
          }
          
          if (!categoryData[category]) {
            categoryData[category] = {};
          }
          
          categoryData[category][label] = value;
        }
        
        // Clear existing datasets
        this.datasets = [];
        
        // Create a dataset for each category
        const colors = [
          '#4285f4', '#ea4335', '#fbbc05', '#34a853', 
          '#673ab7', '#3f51b5', '#2196f3', '#03a9f4', 
          '#00bcd4', '#009688', '#4caf50', '#8bc34a'
        ];
        
        let colorIndex = 0;
        for (const category in categoryData) {
          const values = categoryData[category];
          const dataPoints = [];
          
          for (const label of newLabels) {
            dataPoints.push(values[label] || 0);
          }
          
          const color = colors[colorIndex % colors.length];
          this.addDataset(category, dataPoints, color + '33', color);
          colorIndex++;
        }
      } else {
        // For simple data (single dataset)
        const values = [];
        
        for (const item of data) {
          const label = this.labelExtractor(item);
          const value = this.valueExtractor(item);
          
          newLabels.push(label);
          values.push(value);
        }
        
        // Clear existing datasets
        this.datasets = [];
        
        // Add a single dataset
        const backgroundColors = [];
        const borderColors = [];
        
        for (let i = 0; i < values.length; i++) {
          const hue = (i * 30) % 360;
          backgroundColors.push(`hsla(${hue}, 70%, 60%, 0.2)`);
          borderColors.push(`hsla(${hue}, 70%, 60%, 1)`);
        }
        
        this.addDataset(this.valueField, values, backgroundColors, borderColors);
      }
      
      // Update labels
      this.labels = newLabels;
      
      // Update the chart
      if (this.chart) {
        this.updateChart();
      }
    }

    /**
     * Creates the chart.
     * @private
     */
    createChart() {
      if (!this.element || !window.Chart) {
        return;
      }
      
      const ctx = this.element.getContext('2d');
      
      // If there's an existing chart, destroy it first
      if (this.chart) {
        this.chart.destroy();
        this.chart = null;
      }
      
      // Prepare chart configuration
      const config = {
        type: this.type,
        data: {
          labels: this.labels,
          datasets: this.datasets
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          ...this.options
        }
      };
      
      // Create the chart
      this.chart = new Chart(ctx, config);
    }

    /**
     * Updates the chart.
     * @private
     */
    updateChart() {
      if (!this.chart) {
        this.createChart();
        return;
      }
      
      this.chart.data.labels = this.labels;
      this.chart.data.datasets = this.datasets;
      this.chart.update();
    }

    /**
     * Renders the component to the DOM.
     * @param {HTMLElement} parent - The parent element to render into
     * @returns {HTMLElement} - The rendered element
     */
    render(parent) {
      const element = super.render(parent);
      
      // Initialize the chart when the canvas is rendered
      setTimeout(() => {
        this.createChart();
      }, 0);
      
      return element;
    }
  }

  // Export the ChartComponent class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.ChartComponent = ChartComponent;
})();