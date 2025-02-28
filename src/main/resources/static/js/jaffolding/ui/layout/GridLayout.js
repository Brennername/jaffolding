/**
 * A grid layout arranges components in a grid of rows and columns.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class GridLayout {
    /**
     * Creates a new GridLayout instance.
     * @param {number} rows - The number of rows
     * @param {number} cols - The number of columns
     * @param {number} [hgap=0] - The horizontal gap
     * @param {number} [vgap=0] - The vertical gap
     */
    constructor(rows, cols, hgap = 0, vgap = 0) {
      this.rows = rows;
      this.cols = cols;
      this.hgap = hgap;
      this.vgap = vgap;
    }

    /**
     * Applies the layout to a container.
     * @param {Component} container - The container component
     */
    applyTo(container) {
      container.setStyle('display', 'grid');
      
      if (this.cols > 0) {
        container.setStyle('grid-template-columns', `repeat(${this.cols}, 1fr)`);
      }
      
      if (this.rows > 0) {
        container.setStyle('grid-template-rows', `repeat(${this.rows}, auto)`);
      }
      
      if (this.hgap > 0 || this.vgap > 0) {
        container.setStyle('gap', `${this.vgap}px ${this.hgap}px`);
      }
    }

    /**
     * Adds a component to the layout.
     * @param {Component} comp - The component to add
     */
    addLayoutComponent(comp) {
      // No specific positioning needed
    }

    /**
     * Adds a component to the layout with constraints.
     * @param {Component} comp - The component to add
     * @param {Object} constraints - The layout constraints
     */
    addLayoutComponent(comp, constraints) {
      if (constraints && typeof constraints === 'object') {
        if (constraints.gridx >= 0) {
          comp.setStyle('grid-column-start', String(constraints.gridx + 1));
        }
        
        if (constraints.gridy >= 0) {
          comp.setStyle('grid-row-start', String(constraints.gridy + 1));
        }
        
        if (constraints.gridwidth > 1) {
          comp.setStyle('grid-column-end', `span ${constraints.gridwidth}`);
        }
        
        if (constraints.gridheight > 1) {
          comp.setStyle('grid-row-end', `span ${constraints.gridheight}`);
        }
      }
    }

    /**
     * Removes a component from the layout.
     * @param {Component} comp - The component to remove
     */
    removeLayoutComponent(comp) {
      // Nothing to do
    }

    /**
     * Lays out the container.
     * @param {Component} container - The container component
     */
    layoutContainer(container) {
      this.applyTo(container);
    }

    /**
     * Creates grid constraints.
     * @param {number} gridx - The grid x coordinate
     * @param {number} gridy - The grid y coordinate
     * @param {number} [gridwidth=1] - The grid width
     * @param {number} [gridheight=1] - The grid height
     * @returns {Object} - The grid constraints
     * @static
     */
    static createConstraints(gridx, gridy, gridwidth = 1, gridheight = 1) {
      return {
        gridx,
        gridy,
        gridwidth,
        gridheight
      };
    }
  }

  // Export the GridLayout class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.GridLayout = GridLayout;
})();