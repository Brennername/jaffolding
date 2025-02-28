/**
 * Registry for mapping between Java and JavaScript components.
 * This allows components to be shared between the two environments.
 */
(function() {
  class ComponentRegistry {
    constructor() {
      this.components = {};
      this.javaComponents = {};
      this.jsComponents = {};
      this.bridgeEnabled = true;
    }

    /**
     * Registers a JavaScript component implementation.
     * @param {string} id - The component identifier
     * @param {Function} constructor - The component constructor
     * @param {Object} [options] - Registration options
     */
    registerJSComponent(id, constructor, options = {}) {
      this.jsComponents[id] = {
        constructor,
        options
      };
      
      // Also register in the general registry
      this.components[id] = {
        type: 'js',
        implementation: constructor,
        options
      };
      
      console.log(`Registered JS component: ${id}`);
    }

    /**
     * Registers a Java component implementation.
     * @param {string} id - The component identifier
     * @param {string} className - The fully qualified Java class name
     * @param {Object} [options] - Registration options
     */
    registerJavaComponent(id, className, options = {}) {
      this.javaComponents[id] = {
        className,
        options
      };
      
      // Also register in the general registry
      this.components[id] = {
        type: 'java',
        implementation: className,
        options
      };
      
      console.log(`Registered Java component: ${id}`);
    }

    /**
     * Gets a component implementation.
     * @param {string} id - The component identifier
     * @param {string} [preferredType] - The preferred implementation type ('js' or 'java')
     * @returns {Object} - The component implementation info
     */
    getComponent(id, preferredType) {
      const component = this.components[id];
      if (!component) return null;
      
      // If no preference or the preferred type matches, return the component
      if (!preferredType || component.type === preferredType) {
        return component;
      }
      
      // If the preferred type is different, check if the other type is available
      const otherType = preferredType === 'js' ? 'java' : 'js';
      const otherComponent = this.components[id + '_' + otherType];
      
      // Return the preferred type if available, otherwise the original
      return otherComponent || component;
    }

    /**
     * Creates a component instance.
     * @param {string} id - The component identifier
     * @param {Array} args - The constructor arguments
     * @param {Object} [options] - Creation options
     * @returns {Object} - The component instance
     */
    createComponent(id, args = [], options = {}) {
      const preferredType = options.preferredType || (window.$rt_javaClassesLoaded ? 'java' : 'js');
      const component = this.getComponent(id, preferredType);
      
      if (!component) {
        console.error(`Component not found: ${id}`);
        return null;
      }
      
      try {
        if (component.type === 'js') {
          // Create JavaScript component
          return new component.implementation(...args);
        } else {
          // Create Java component
          if (!window.$rt_javaClassesLoaded) {
            console.warn(`Cannot create Java component ${id}: TeaVM not loaded`);
            
            // Try to fall back to JS implementation
            const jsComponent = this.getComponent(id, 'js');
            if (jsComponent) {
              console.log(`Falling back to JS implementation for ${id}`);
              return new jsComponent.implementation(...args);
            }
            
            return null;
          }
          
          return window.jaffolding.JavaBridge.createComponent(component.implementation, args);
        }
      } catch (e) {
        console.error(`Error creating component ${id}:`, e);
        return null;
      }
    }

    /**
     * Enables or disables the bridge.
     * @param {boolean} enabled - Whether the bridge is enabled
     */
    setBridgeEnabled(enabled) {
      this.bridgeEnabled = enabled;
    }

    /**
     * Checks if the bridge is enabled.
     * @returns {boolean} - Whether the bridge is enabled
     */
    isBridgeEnabled() {
      return this.bridgeEnabled;
    }

    /**
     * Gets the singleton instance.
     * @returns {ComponentRegistry} - The singleton instance
     */
    static getInstance() {
      if (!ComponentRegistry.instance) {
        ComponentRegistry.instance = new ComponentRegistry();
      }
      return ComponentRegistry.instance;
    }
  }

  // Create the singleton instance
  ComponentRegistry.instance = null;

  // Export the ComponentRegistry class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.ComponentRegistry = ComponentRegistry;
  
  // Register core components
  const registry = ComponentRegistry.getInstance();
  
  // Register KDEDesktop
  if (window.jaffolding.KDEDesktop) {
    registry.registerJSComponent('kde-desktop', window.jaffolding.KDEDesktop);
  }
  
  // Register AppManager
  if (window.jaffolding.AppManager) {
    registry.registerJSComponent('app-manager', window.jaffolding.AppManager);
  }
  
  // Register BrowserWindow
  if (window.jaffolding.BrowserWindow) {
    registry.registerJSComponent('browser-window', window.jaffolding.BrowserWindow);
  }
  
  // Register Calculator
  if (window.jaffolding.Calculator) {
    registry.registerJSComponent('calculator', window.jaffolding.Calculator);
  }
  
  console.log('Component Registry initialized');
})();