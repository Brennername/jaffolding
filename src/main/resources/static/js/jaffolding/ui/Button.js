/**
 * A button component.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Button extends jaffolding.Component {
    /**
     * Creates a new Button instance.
     * @param {string} [text] - The button text
     */
    constructor(text) {
      super('button');
      if (text) {
        this.setText(text);
      }
      this.initializeStyles();
    }

    /**
     * Initializes the button styles.
     * @private
     */
    initializeStyles() {
      this.setStyle('padding', '8px 16px')
          .setStyle('background-color', '#4285f4')
          .setStyle('color', 'white')
          .setStyle('border', 'none')
          .setStyle('border-radius', '4px')
          .setStyle('cursor', 'pointer')
          .setStyle('font-size', '14px')
          .setStyle('transition', 'background-color 0.2s')
          .addEventListener('mouseover', () => {
            if (this.isEnabled()) {
              this.setStyle('background-color', '#3367d6');
            }
          })
          .addEventListener('mouseout', () => {
            if (this.isEnabled()) {
              this.setStyle('background-color', '#4285f4');
            }
          })
          .addEventListener('mousedown', () => {
            if (this.isEnabled()) {
              this.setStyle('transform', 'scale(0.98)');
            }
          })
          .addEventListener('mouseup', () => {
            if (this.isEnabled()) {
              this.setStyle('transform', 'scale(1)');
            }
          });
    }

    /**
     * Sets the click handler.
     * @param {Function} handler - The click handler function
     * @returns {Button} - The button instance for chaining
     */
    setOnClick(handler) {
      this.addEventListener('click', handler);
      return this;
    }

    /**
     * Sets the button as primary.
     * @returns {Button} - The button instance for chaining
     */
    setPrimary() {
      this.setStyle('background-color', '#4285f4');
      this.setStyle('color', 'white');
      return this;
    }

    /**
     * Sets the button as secondary.
     * @returns {Button} - The button instance for chaining
     */
    setSecondary() {
      this.setStyle('background-color', '#f1f3f4');
      this.setStyle('color', '#3c4043');
      this.setStyle('border', '1px solid #dadce0');
      return this;
    }

    /**
     * Sets the button as danger.
     * @returns {Button} - The button instance for chaining
     */
    setDanger() {
      this.setStyle('background-color', '#ea4335');
      this.setStyle('color', 'white');
      return this;
    }

    /**
     * Sets the button as success.
     * @returns {Button} - The button instance for chaining
     */
    setSuccess() {
      this.setStyle('background-color', '#34a853');
      this.setStyle('color', 'white');
      return this;
    }

    /**
     * Sets the button as warning.
     * @returns {Button} - The button instance for chaining
     */
    setWarning() {
      this.setStyle('background-color', '#fbbc05');
      this.setStyle('color', 'white');
      return this;
    }

    /**
     * Sets the button as outline.
     * @returns {Button} - The button instance for chaining
     */
    setOutline() {
      this.setStyle('background-color', 'transparent');
      this.setStyle('color', '#4285f4');
      this.setStyle('border', '1px solid #4285f4');
      return this;
    }

    /**
     * Checks if the button is enabled.
     * @returns {boolean} - Whether the button is enabled
     */
    isEnabled() {
      return !this.element || !this.element.disabled;
    }
  }

  // Export the Button class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Button = Button;
})();