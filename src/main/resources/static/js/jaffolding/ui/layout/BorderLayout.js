/**
 * A border layout arranges components in five regions: north, south, east, west, and center.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class BorderLayout {
    /**
     * Creates a new BorderLayout instance.
     * @param {number} [hgap=0] - The horizontal gap
     * @param {number} [vgap=0] - The vertical gap
     */
    constructor(hgap = 0, vgap = 0) {
      this.hgap = hgap;
      this.vgap = vgap;
      this.components = {};
    }

    /**
     * Applies the layout to a container.
     * @param {Component} container - The container component
     */
    applyTo(container) {
      container.setStyle('display', 'grid');
      container.setStyle('grid-template-areas', 
          '"north north north" ' +
          '"west center east" ' +
          '"south south south"');
      container.setStyle('grid-template-rows', 'auto 1fr auto');
      container.setStyle('grid-template-columns', 'auto 1fr auto');
      
      if (this.hgap > 0 || this.vgap > 0) {
        container.setStyle('gap', `${this.vgap}px ${this.hgap}px`);
      }
      
      container.setStyle('height', '100%');
    }

    /**
     * Adds a component to the layout.
     * @param {Component} comp - The component to add
     */
    addLayoutComponent(comp) {
      // Default to center if no constraint is provided
      this.addLayoutComponent(comp, BorderLayout.CENTER);
    }

    /**
     * Adds a component to the layout with constraints.
     * @param {Component} comp - The component to add
     * @param {string} constraints - The layout constraints
     */
    addLayoutComponent(comp, constraints) {
      if (typeof constraints === 'string') {
        this.components[constraints] = comp;
        
        switch (constraints) {
          case BorderLayout.NORTH:
            comp.setStyle('grid-area', 'north');
            break;
          case BorderLayout.SOUTH:
            comp.setStyle('grid-area', 'south');
            break;
          case BorderLayout.EAST:
            comp.setStyle('grid-area', 'east');
            break;
          case BorderLayout.WEST:
            comp.setStyle('grid-area', 'west');
            break;
          case BorderLayout.CENTER:
            comp.setStyle('grid-area', 'center');
            break;
        }
      }
    }

    /**
     * Removes a component from the layout.
     * @param {Component} comp - The component to remove
     */
    removeLayoutComponent(comp) {
      for (const [key, value] of Object.entries(this.components)) {
        if (value === comp) {
          delete this.components[key];
          break;
        }
      }
    }

    /**
     * Lays out the container.
     * @param {Component} container - The container component
     */
    layoutContainer(container) {
      this.applyTo(container);
    }
  }

  // Define constants
  BorderLayout.NORTH = 'North';
  BorderLayout.SOUTH = 'South';
  BorderLayout.EAST = 'East';
  BorderLayout.WEST = 'West';
  BorderLayout.CENTER = 'Center';

  // Export the BorderLayout class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.BorderLayout = BorderLayout;
})();