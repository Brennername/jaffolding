/**
 * Application manager for the Jaffolding framework.
 * Manages application windows and their lifecycle.
 */
(function() {
  class AppManager {
    /**
     * Creates a new AppManager instance.
     * @param {KDEDesktop} desktop - The desktop instance
     */
    constructor(desktop) {
      this.desktop = desktop;
      this.apps = {};
      this.runningApps = {};
      this.appWindows = {};
    }

    /**
     * Registers an application.
     * @param {Object} app - The application definition
     */
    registerApp(app) {
      this.apps[app.id] = app;
    }

    /**
     * Gets an application by ID.
     * @param {string} id - The application ID
     * @returns {Object} - The application definition
     */
    getApp(id) {
      return this.apps[id];
    }

    /**
     * Gets all registered applications.
     * @returns {Object[]} - The application definitions
     */
    getApps() {
      return Object.values(this.apps);
    }

    /**
     * Launches an application.
     * @param {string} id - The application ID
     * @returns {Window} - The application window
     */
    launchApp(id) {
      const app = this.apps[id];
      if (!app) {
        console.error(`App not found: ${id}`);
        return null;
      }

      // Create the window
      const window = app.createWindow();
      
      // Track the running app
      if (!this.runningApps[id]) {
        this.runningApps[id] = [];
      }
      this.runningApps[id].push(window);
      
      // Track the window
      this.appWindows[window.id] = {
        appId: id,
        window: window
      };
      
      // Listen for window close
      window.onClose = () => {
        this.handleWindowClose(window.id);
      };
      
      // Add the window to the desktop
      this.desktop.addWindow(window);
      
      // Update the dock
      this.desktop.updateDock(id, true);
      
      return window;
    }

    /**
     * Handles window close.
     * @param {string} windowId - The window ID
     */
    handleWindowClose(windowId) {
      const windowInfo = this.appWindows[windowId];
      if (!windowInfo) return;
      
      const { appId } = windowInfo;
      
      // Remove from running apps
      if (this.runningApps[appId]) {
        this.runningApps[appId] = this.runningApps[appId].filter(w => w.id !== windowId);
        
        // Update the dock if no more windows for this app
        if (this.runningApps[appId].length === 0) {
          this.desktop.updateDock(appId, false);
        }
      }
      
      // Remove from app windows
      delete this.appWindows[windowId];
    }

    /**
     * Creates an application window.
     * @param {string} title - The window title
     * @param {Component} content - The window content
     * @param {Object} options - The window options
     * @returns {Window} - The window instance
     */
    createAppWindow(title, content, options = {}) {
      const window = new jaffolding.Window(title, content);
      window.id = 'window_' + Date.now() + '_' + Math.floor(Math.random() * 1000);
      
      if (options.width) window.setWidth(options.width);
      if (options.height) window.setHeight(options.height);
      if (options.x && options.y) window.setPosition(options.x, options.y);
      
      return window;
    }

    /**
     * Creates a browser window.
     * @param {string} title - The window title
     * @param {string} url - The initial URL
     * @param {Object} options - The window options
     * @returns {BrowserWindow} - The browser window instance
     */
    createBrowserWindow(title, url, options = {}) {
      const window = new jaffolding.BrowserWindow(title, url);
      window.id = 'window_' + Date.now() + '_' + Math.floor(Math.random() * 1000);
      
      if (options.width) window.setWidth(options.width);
      if (options.height) window.setHeight(options.height);
      if (options.x && options.y) window.setPosition(options.x, options.y);
      
      return window;
    }

    /**
     * Focuses all windows of an application.
     * @param {string} appId - The application ID
     */
    focusApp(appId) {
      if (this.runningApps[appId]) {
        this.runningApps[appId].forEach(window => {
          window.bringToFront();
        });
      }
    }

    /**
     * Checks if an application is running.
     * @param {string} appId - The application ID
     * @returns {boolean} - Whether the application is running
     */
    isAppRunning(appId) {
      return this.runningApps[appId] && this.runningApps[appId].length > 0;
    }
  }

  // Initialize instance tracking
  window.__jaffolding_instances = window.__jaffolding_instances || {};
  
  // Override the constructor to track instances
  const originalConstructor = AppManager;
  AppManager = function(...args) {
    const instance = new originalConstructor(...args);
    const id = 'app_manager_' + Date.now();
    window.__jaffolding_instances[id] = instance;
    return instance;
  };
  
  // Copy prototype
  AppManager.prototype = originalConstructor.prototype;

  // Export the AppManager class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.AppManager = AppManager;
})();