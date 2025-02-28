/**
 * A panel component that can contain other components.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Panel extends jaffolding.Component {
    /**
     * Creates a new Panel instance.
     */
    constructor() {
      super('div');
      this.layout = null;
      this.setStyle('display', 'flex');
      this.setStyle('flex-wrap', 'wrap');
    }

    /**
     * Sets the layout for the panel.
     * @param {Object} layout - The layout object
     * @returns {Panel} - The panel instance for chaining
     */
    setLayout(layout) {
      this.layout = layout;
      
      if (layout && layout.applyTo) {
        layout.applyTo(this);
      }
      
      return this;
    }

    /**
     * Adds a child component to the panel.
     * @param {Component} child - The child component
     * @param {*} [constraints] - The layout constraints
     * @returns {Panel} - The panel instance for chaining
     */
    addChild(child, constraints) {
      super.addChild(child);
      
      if (this.layout && this.layout.addLayoutComponent) {
        if (constraints !== undefined) {
          this.layout.addLayoutComponent(child, constraints);
        } else {
          this.layout.addLayoutComponent(child);
        }
      }
      
      return this;
    }

    /**
     * Sets the background color of the panel.
     * @param {string} color - The background color
     * @returns {Panel} - The panel instance for chaining
     */
    setBackground(color) {
      this.setStyle('background-color', color);
      return this;
    }

    /**
     * Sets the padding of the panel.
     * @param {string} padding - The padding value
     * @returns {Panel} - The panel instance for chaining
     */
    setPadding(padding) {
      this.setStyle('padding', padding);
      return this;
    }

    /**
     * Sets the border of the panel.
     * @param {string} border - The border value
     * @returns {Panel} - The panel instance for chaining
     */
    setBorder(border) {
      this.setStyle('border', border);
      return this;
    }

    /**
     * Sets the text alignment of the panel.
     * @param {string} align - The text alignment
     * @returns {Panel} - The panel instance for chaining
     */
    setTextAlign(align) {
      this.setStyle('text-align', align);
      return this;
    }

    /**
     * Creates a panel with a title border.
     * @param {string} title - The panel title
     * @returns {Panel} - The panel instance
     * @static
     */
    static createTitledPanel(title) {
      const panel = new Panel();
      panel.setBorder('1px solid #ccc')
           .setStyle('border-radius', '4px')
           .setStyle('overflow', 'hidden');
      
      const titleBar = new Panel();
      titleBar.setBackground('#f5f5f5')
              .setStyle('padding', '8px 12px')
              .setStyle('border-bottom', '1px solid #ccc')
              .setStyle('font-weight', 'bold');
      
      const titleLabel = new jaffolding.Component('div');
      titleLabel.setText(title);
      titleBar.addChild(titleLabel);
      
      const contentPanel = new Panel();
      contentPanel.setPadding('12px');
      
      panel.addChild(titleBar);
      panel.addChild(contentPanel);
      
      return panel;
    }
  }

  // Export the Panel class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Panel = Panel;
})();