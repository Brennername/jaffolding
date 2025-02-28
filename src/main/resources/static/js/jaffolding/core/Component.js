/**
 * Base Component class for the Jaffolding framework.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Component {
    /**
     * Creates a new Component instance.
     * @param {string} tagName - The HTML tag name
     */
    constructor(tagName) {
      this.tagName = tagName;
      this.text = null;
      this.attributes = {};
      this.styles = {};
      this.children = [];
      this.eventListeners = {};
      this.element = null;
    }

    /**
     * Sets the text content of the component.
     * @param {string} text - The text content
     * @returns {Component} - The component instance for chaining
     */
    setText(text) {
      this.text = text;
      if (this.element) {
        this.element.textContent = text;
      }
      return this;
    }

    /**
     * Sets an attribute on the component.
     * @param {string} name - The attribute name
     * @param {string} value - The attribute value
     * @returns {Component} - The component instance for chaining
     */
    setAttribute(name, value) {
      this.attributes[name] = value;
      if (this.element) {
        if (value === null || value === undefined) {
          this.element.removeAttribute(name);
        } else {
          this.element.setAttribute(name, value);
        }
      }
      return this;
    }

    /**
     * Sets a style property on the component.
     * @param {string} property - The CSS property name
     * @param {string} value - The CSS property value
     * @returns {Component} - The component instance for chaining
     */
    setStyle(property, value) {
      this.styles[property] = value;
      if (this.element) {
        this.element.style[property] = value;
      }
      return this;
    }

    /**
     * Adds a child component to this component.
     * @param {Component} child - The child component to add
     * @returns {Component} - The component instance for chaining
     */
    addChild(child) {
      this.children.push(child);
      if (this.element && child.element === null) {
        child.render(this.element);
      }
      return this;
    }

    /**
     * Adds an event listener to the component.
     * @param {string} eventType - The event type (e.g., 'click')
     * @param {Function} listener - The event listener function
     * @returns {Component} - The component instance for chaining
     */
    addEventListener(eventType, listener) {
      if (!this.eventListeners[eventType]) {
        this.eventListeners[eventType] = [];
      }
      this.eventListeners[eventType].push(listener);
      
      if (this.element) {
        this.element.addEventListener(eventType, listener);
      }
      
      return this;
    }

    /**
     * Renders the component to the DOM.
     * @param {HTMLElement} parent - The parent element to render into
     * @returns {HTMLElement} - The rendered element
     */
    render(parent) {
      this.element = document.createElement(this.tagName);
      
      // Set text content if provided
      if (this.text !== null) {
        this.element.textContent = this.text;
      }
      
      // Set attributes
      for (const [name, value] of Object.entries(this.attributes)) {
        if (value !== null && value !== undefined) {
          this.element.setAttribute(name, value);
        }
      }
      
      // Set styles
      for (const [property, value] of Object.entries(this.styles)) {
        this.element.style[property] = value;
      }
      
      // Add event listeners
      for (const [eventType, listeners] of Object.entries(this.eventListeners)) {
        for (const listener of listeners) {
          this.element.addEventListener(eventType, listener);
        }
      }
      
      // Render children
      for (const child of this.children) {
        child.render(this.element);
      }
      
      // Append to parent
      parent.appendChild(this.element);
      
      // Dispatch DOMNodeInserted event
      if (this.eventListeners['DOMNodeInserted']) {
        const event = new Event('DOMNodeInserted');
        for (const listener of this.eventListeners['DOMNodeInserted']) {
          listener(event);
        }
      }
      
      return this.element;
    }

    /**
     * Gets the DOM element for this component.
     * @returns {HTMLElement|null} - The DOM element or null if not rendered
     */
    getElement() {
      return this.element;
    }

    /**
     * Removes the component from its parent.
     */
    removeFromParent() {
      if (this.element && this.element.parentNode) {
        this.element.parentNode.removeChild(this.element);
      }
    }

    /**
     * Clears all children from the component.
     */
    clear() {
      if (this.element) {
        while (this.element.firstChild) {
          this.element.removeChild(this.element.firstChild);
        }
      }
      this.children = [];
    }
  }

  // Export the Component class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Component = Component;
})();