/**
 * Component loader for the Jaffolding framework.
 * This module handles loading components and their dependencies.
 */
(function() {
  class ComponentLoader {
    constructor() {
      this.loadedModules = {};
      this.loadingPromises = {};
      this.moduleDefinitions = {
        'core': [
          '/js/jaffolding/core/Component.js',
          '/js/jaffolding/core/State.js',
          '/js/jaffolding/core/Router.js',
          '/js/jaffolding/bridge/JavaBridge.js',
          '/js/jaffolding/bridge/ComponentRegistry.js'
        ],
        'ui': [
          '/js/jaffolding/ui/Animation.js',
          '/js/jaffolding/ui/Button.js',
          '/js/jaffolding/ui/Panel.js',
          '/js/jaffolding/ui/Window.js',
          '/js/jaffolding/ui/Desktop.js',
          '/js/jaffolding/ui/ChartComponent.js',
          '/js/jaffolding/ui/DataTable.js',
          '/js/jaffolding/ui/Calculator.js',
          '/js/jaffolding/ui/MobileNavigation.js',
          '/js/jaffolding/ui/layout/BorderLayout.js',
          '/js/jaffolding/ui/layout/GridLayout.js',
          '/js/jaffolding/ui/AppManager.js',
          '/js/jaffolding/ui/BrowserWindow.js',
          '/js/jaffolding/ui/KDEDesktop.js'
        ],
        'demos': [
          '/js/jaffolding/demos/MainPage.js',
          '/js/jaffolding/demos/ComponentDemo.js',
          '/js/jaffolding/demos/ChartDemo.js',
          '/js/jaffolding/demos/ThreeDemo.js',
          '/js/jaffolding/demos/SalesDataDemo.js',
          '/js/jaffolding/demos/DesktopDemo.js',
          '/js/jaffolding/demos/AnimationDemo.js',
          '/js/jaffolding/demos/CalculatorApp.js'
        ]
      };
      
      // Dependencies between modules
      this.dependencies = {
        'ui': ['core'],
        'demos': ['core', 'ui']
      };
    }

    /**
     * Loads a module.
     * @param {string} moduleName - The module name
     * @returns {Promise} - A promise that resolves when the module is loaded
     */
    loadModule(moduleName) {
      // If the module is already loaded, return a resolved promise
      if (this.loadedModules[moduleName]) {
        return Promise.resolve();
      }
      
      // If the module is already being loaded, return the existing promise
      if (this.loadingPromises[moduleName]) {
        return this.loadingPromises[moduleName];
      }
      
      // Load dependencies first
      const dependencies = this.dependencies[moduleName] || [];
      const dependencyPromises = dependencies.map(dep => this.loadModule(dep));
      
      // Load the module
      const moduleFiles = this.moduleDefinitions[moduleName] || [];
      const modulePromise = Promise.all(dependencyPromises)
        .then(() => this.loadFiles(moduleFiles))
        .then(() => {
          this.loadedModules[moduleName] = true;
          delete this.loadingPromises[moduleName];
          console.log(`Module ${moduleName} loaded`);
        })
        .catch(error => {
          delete this.loadingPromises[moduleName];
          console.error(`Error loading module ${moduleName}:`, error);
          throw error;
        });
      
      this.loadingPromises[moduleName] = modulePromise;
      return modulePromise;
    }

    /**
     * Loads files.
     * @param {string[]} files - The files to load
     * @returns {Promise} - A promise that resolves when all files are loaded
     */
    loadFiles(files) {
      return new Promise((resolve, reject) => {
        let loaded = 0;
        let errors = 0;
        
        if (files.length === 0) {
          resolve();
          return;
        }
        
        files.forEach(url => {
          // Skip already loaded files
          if (document.querySelector(`script[src="${url}"]`)) {
            loaded++;
            if (loaded + errors === files.length) {
              if (errors > 0) {
                reject(new Error(`Failed to load ${errors} files`));
              } else {
                resolve();
              }
            }
            return;
          }
          
          const script = document.createElement('script');
          script.src = url;
          script.onload = () => {
            loaded++;
            console.log(`Loaded file ${loaded}/${files.length}: ${url}`);
            if (loaded + errors === files.length) {
              if (errors > 0) {
                reject(new Error(`Failed to load ${errors} files`));
              } else {
                resolve();
              }
            }
          };
          script.onerror = (error) => {
            errors++;
            console.error(`Failed to load file: ${url}`, error);
            if (loaded + errors === files.length) {
              reject(new Error(`Failed to load ${errors} files`));
            }
          };
          document.head.appendChild(script);
        });
      });
    }

    /**
     * Loads all modules.
     * @returns {Promise} - A promise that resolves when all modules are loaded
     */
    loadAllModules() {
      return Promise.all(
        Object.keys(this.moduleDefinitions).map(moduleName => this.loadModule(moduleName))
      );
    }

    /**
     * Gets the singleton instance.
     * @returns {ComponentLoader} - The singleton instance
     */
    static getInstance() {
      if (!ComponentLoader.instance) {
        ComponentLoader.instance = new ComponentLoader();
      }
      return ComponentLoader.instance;
    }
  }

  // Create the singleton instance
  ComponentLoader.instance = null;

  // Export the ComponentLoader class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.ComponentLoader = ComponentLoader;
})();