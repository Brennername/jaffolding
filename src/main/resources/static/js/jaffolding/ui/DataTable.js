/**
 * A data table component for displaying tabular data.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class DataTable extends jaffolding.Component {
    /**
     * Creates a new DataTable instance.
     * @param {string[]} [columnNames] - The column names
     */
    constructor(columnNames = []) {
      super('div');
      this.columnNames = [...columnNames];
      this.data = [];
      this.dataState = new jaffolding.State([]);
      this.selectable = true;
      this.selectedRow = -1;
      this.selectionListener = null;
      this.rowClickListener = null;
      this.sortColumn = null;
      this.sortAscending = true;
      this.columnTypes = {};
      this.filters = {};
      
      this.initializeStyles();
      
      // Subscribe to data state changes
      this.dataState.subscribe(newData => {
        this.data = newData;
        this.refreshTable();
      });
    }

    /**
     * Initializes the table styles.
     * @private
     */
    initializeStyles() {
      this.setStyle('width', '100%')
          .setStyle('overflow', 'auto')
          .setStyle('border', '1px solid #e0e0e0')
          .setStyle('border-radius', '4px')
          .setStyle('background-color', 'white');
    }

    /**
     * Sets the column names.
     * @param {string[]} columnNames - The column names
     * @returns {DataTable} - The data table instance for chaining
     */
    setColumnNames(columnNames) {
      this.columnNames = [...columnNames];
      this.refreshTable();
      return this;
    }

    /**
     * Sets the column type.
     * @param {string} columnName - The column name
     * @param {string} type - The column type ('string', 'number', 'date')
     * @returns {DataTable} - The data table instance for chaining
     */
    setColumnType(columnName, type) {
      this.columnTypes[columnName] = type;
      return this;
    }

    /**
     * Sets the table data.
     * @param {Object[]} data - The table data
     * @returns {DataTable} - The data table instance for chaining
     */
    setData(data) {
      const newData = data.map(row => ({...row}));
      this.dataState.set(newData);
      return this;
    }

    /**
     * Adds a row to the table.
     * @param {Object} row - The row data
     * @returns {DataTable} - The data table instance for chaining
     */
    addRow(row) {
      const newData = [...this.data, {...row}];
      this.dataState.set(newData);
      return this;
    }

    /**
     * Removes a row from the table.
     * @param {number} index - The row index
     * @returns {DataTable} - The data table instance for chaining
     */
    removeRow(index) {
      if (index >= 0 && index < this.data.length) {
        const newData = [...this.data];
        newData.splice(index, 1);
        this.dataState.set(newData);
        
        if (this.selectedRow >= this.data.length) {
          this.selectedRow = this.data.length - 1;
        }
      }
      return this;
    }

    /**
     * Updates a row in the table.
     * @param {number} index - The row index
     * @param {Object} row - The new row data
     * @returns {DataTable} - The data table instance for chaining
     */
    updateRow(index, row) {
      if (index >= 0 && index < this.data.length) {
        const newData = [...this.data];
        newData[index] = {...row};
        this.dataState.set(newData);
      }
      return this;
    }

    /**
     * Clears the table data.
     * @returns {DataTable} - The data table instance for chaining
     */
    clearData() {
      this.dataState.set([]);
      return this;
    }

    /**
     * Sets whether the table is selectable.
     * @param {boolean} selectable - Whether the table is selectable
     * @returns {DataTable} - The data table instance for chaining
     */
    setSelectable(selectable) {
      this.selectable = selectable;
      this.refreshTable();
      return this;
    }

    /**
     * Gets the selected row index.
     * @returns {number} - The selected row index
     */
    getSelectedRow() {
      return this.selectedRow;
    }

    /**
     * Gets the selected row data.
     * @returns {Object|null} - The selected row data or null if no row is selected
     */
    getSelectedRowData() {
      if (this.selectedRow >= 0 && this.selectedRow < this.data.length) {
        return this.data[this.selectedRow];
      }
      return null;
    }

    /**
     * Sets the selected row.
     * @param {number} row - The row index
     * @returns {DataTable} - The data table instance for chaining
     */
    setSelectedRow(row) {
      if (row >= -1 && row < this.data.length) {
        this.selectedRow = row;
        this.updateSelection();
      }
      return this;
    }

    /**
     * Sets the row selection listener.
     * @param {Function} listener - The selection listener function
     * @returns {DataTable} - The data table instance for chaining
     */
    setOnRowSelect(listener) {
      this.selectionListener = listener;
      return this;
    }

    /**
     * Sets the row click listener.
     * @param {Function} listener - The click listener function
     * @returns {DataTable} - The data table instance for chaining
     */
    setOnRowClick(listener) {
      this.rowClickListener = listener;
      return this;
    }

    /**
     * Sorts the table by a column.
     * @param {string} columnName - The column name
     * @param {boolean} ascending - Whether to sort in ascending order
     * @returns {DataTable} - The data table instance for chaining
     */
    sortBy(columnName, ascending) {
      this.sortColumn = columnName;
      this.sortAscending = ascending;
      
      if (this.data.length > 0 && columnName) {
        const newData = [...this.data];
        
        newData.sort((a, b) => {
          const valA = a[columnName];
          const valB = b[columnName];
          
          if (valA === undefined && valB === undefined) return 0;
          if (valA === undefined) return ascending ? -1 : 1;
          if (valB === undefined) return ascending ? 1 : -1;
          
          const type = this.columnTypes[columnName] || 'string';
          let result = 0;
          
          switch (type) {
            case 'number':
              const numA = typeof valA === 'number' ? valA : parseFloat(valA);
              const numB = typeof valB === 'number' ? valB : parseFloat(valB);
              result = numA - numB;
              break;
            case 'date':
              const dateA = new Date(valA);
              const dateB = new Date(valB);
              result = dateA - dateB;
              break;
            default:
              result = String(valA).localeCompare(String(valB));
          }
          
          return ascending ? result : -result;
        });
        
        this.dataState.set(newData);
      }
      
      return this;
    }

    /**
     * Filters the table by a column.
     * @param {string} columnName - The column name
     * @param {string} value - The filter value
     * @returns {DataTable} - The data table instance for chaining
     */
    filter(columnName, value) {
      if (!value || value.trim() === '') {
        delete this.filters[columnName];
      } else {
        this.filters[columnName] = value.toLowerCase();
      }
      
      this.applyFilters();
      return this;
    }

    /**
     * Clears all filters.
     * @returns {DataTable} - The data table instance for chaining
     */
    clearFilters() {
      this.filters = {};
      this.dataState.set(this.data);
      return this;
    }

    /**
     * Applies all filters to the data.
     * @private
     */
    applyFilters() {
      if (Object.keys(this.filters).length === 0) {
        return;
      }
      
      const filteredData = this.data.filter(row => {
        for (const [columnName, filterValue] of Object.entries(this.filters)) {
          if (row[columnName] === undefined || row[columnName] === null) {
            return false;
          }
          
          const cellValue = String(row[columnName]).toLowerCase();
          if (!cellValue.includes(filterValue)) {
            return false;
          }
        }
        
        return true;
      });
      
      this.dataState.set(filteredData);
    }

    /**
     * Loads data from an API endpoint.
     * @param {string} url - The API endpoint URL
     * @returns {DataTable} - The data table instance for chaining
     */
    loadFromApi(url) {
      fetch(url)
        .then(response => response.json())
        .then(data => {
          this.dataState.set(data);
        })
        .catch(error => {
          console.error('Error loading data from API:', error);
        });
      
      return this;
    }

    /**
     * Refreshes the table.
     * @private
     */
    refreshTable() {
      if (!this.element) {
        return;
      }
      
      this.element.innerHTML = '';
      
      // Create table element
      const table = document.createElement('table');
      table.style.width = '100%';
      table.style.borderCollapse = 'collapse';
      table.style.fontSize = '14px';
      
      // Create header
      if (this.columnNames.length > 0) {
        const thead = document.createElement('thead');
        const headerRow = document.createElement('tr');
        
        for (const columnName of this.columnNames) {
          const th = document.createElement('th');
          th.textContent = columnName;
          th.style.padding = '12px 10px';
          th.style.textAlign = 'left';
          th.style.borderBottom = '2px solid #ddd';
          th.style.fontWeight = '600';
          th.style.backgroundColor = '#f5f5f5';
          th.style.cursor = 'pointer';
          
          // Add sort indicator if this column is sorted
          if (columnName === this.sortColumn) {
            th.textContent = `${columnName} ${this.sortAscending ? '▲' : '▼'}`;
          }
          
          // Add sort functionality
          th.addEventListener('click', () => {
            const asc = columnName !== this.sortColumn || !this.sortAscending;
            this.sortBy(columnName, asc);
          });
          
          headerRow.appendChild(th);
        }
        
        thead.appendChild(headerRow);
        table.appendChild(thead);
      }
      
      // Create filter row
      if (this.columnNames.length > 0 && Object.keys(this.filters).length > 0) {
        const filterRow = document.createElement('tr');
        filterRow.style.backgroundColor = '#f9f9f9';
        
        for (const columnName of this.columnNames) {
          const td = document.createElement('td');
          td.style.padding = '8px 10px';
          
          if (this.filters[columnName]) {
            const input = document.createElement('input');
            input.type = 'text';
            input.placeholder = 'Filter...';
            input.value = this.filters[columnName];
            input.style.width = '100%';
            input.style.padding = '4px';
            input.style.border = '1px solid #ddd';
            input.style.borderRadius = '3px';
            
            input.addEventListener('input', () => {
              this.filter(columnName, input.value);
            });
            
            td.appendChild(input);
          }
          
          filterRow.appendChild(td);
        }
        
        table.appendChild(filterRow);
      }
      
      // Create body
      const tbody = document.createElement('tbody');
      
      for (let i = 0; i < this.data.length; i++) {
        const rowData = this.data[i];
        const row = document.createElement('tr');
        
        if (this.selectable) {
          row.style.cursor = 'pointer';
          
          row.addEventListener('click', () => {
            this.selectedRow = i;
            this.updateSelection();
            
            if (this.selectionListener) {
              this.selectionListener(i);
            }
            
            if (this.rowClickListener) {
              this.rowClickListener(rowData);
            }
          });
          
          if (i === this.selectedRow) {
            row.style.backgroundColor = '#e8f0fe';
          }
        }
        
        // Add hover effect
        row.addEventListener('mouseover', () => {
          if (i !== this.selectedRow) {
            row.style.backgroundColor = '#f5f5f5';
          }
        });
        
        row.addEventListener('mouseout', () => {
          if (i !== this.selectedRow) {
            row.style.backgroundColor = '';
          }
        });
        
        // Add cells
        for (const columnName of this.columnNames) {
          const td = document.createElement('td');
          td.style.padding = '10px';
          td.style.borderBottom = '1px solid #ddd';
          
          const cellValue = rowData[columnName];
          td.textContent = cellValue !== undefined ? String(cellValue) : '';
          
          row.appendChild(td);
        }
        
        tbody.appendChild(row);
      }
      
      table.appendChild(tbody);
      this.element.appendChild(table);
      
      // Add empty state message if no data
      if (this.data.length === 0) {
        const emptyState = document.createElement('div');
        emptyState.textContent = 'No data available';
        emptyState.style.padding = '20px';
        emptyState.style.textAlign = 'center';
        emptyState.style.color = '#888';
        
        this.element.appendChild(emptyState);
      }
    }

    /**
     * Updates the selection.
     * @private
     */
    updateSelection() {
      if (!this.element) {
        return;
      }
      
      const table = this.element.querySelector('table');
      if (!table) return;
      
      const tbody = table.querySelector('tbody');
      if (!tbody) return;
      
      for (let i = 0; i < tbody.childNodes.length; i++) {
        const row = tbody.childNodes[i];
        
        if (i === this.selectedRow) {
          row.style.backgroundColor = '#e8f0fe';
        } else {
          row.style.backgroundColor = '';
        }
      }
    }

    /**
     * Gets the table data.
     * @returns {Object[]} - The table data
     */
    getData() {
      return [...this.data];
    }

    /**
     * Gets the data state.
     * @returns {State} - The data state
     */
    getDataState() {
      return this.dataState;
    }
  }

  // Export the DataTable class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.DataTable = DataTable;
})();