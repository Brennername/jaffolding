/**
 * Simple client-side router for the Jaffolding framework.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Router {
    /**
     * Creates a new Router instance.
     * @param {HTMLElement} container - The container element for rendering routes
     */
    constructor(container) {
      this.container = container;
      this.routes = {};
      this.currentPath = null;
      
      // Listen for popstate events (back/forward navigation)
      window.addEventListener('popstate', () => {
        this.navigateTo(this.getPath());
      });
    }

    /**
     * Adds a route to the router.
     * @param {string} path - The route path
     * @param {Function} componentFactory - Function that returns a Component for the route
     * @returns {Router} - The router instance for chaining
     */
    addRoute(path, componentFactory) {
      this.routes[path] = componentFactory;
      return this;
    }

    /**
     * Starts the router.
     */
    start() {
      // Handle initial route
      this.navigateTo(this.getPath());
      
      // Intercept link clicks for client-side routing
      document.addEventListener('click', (event) => {
        const target = event.target.closest('a');
        if (target && 
            target.hasAttribute('href') && 
            !target.getAttribute('href').startsWith('http') &&
            !target.getAttribute('href').startsWith('#')) {
          
          event.preventDefault();
          this.navigateTo(target.getAttribute('href'));
        }
      });
    }

    /**
     * Navigates to a specific route.
     * @param {string} path - The route path to navigate to
     */
    navigateTo(path) {
      if (path === this.currentPath) {
        return;
      }
      
      this.currentPath = path;
      
      // Update browser history
      window.history.pushState(null, '', path);
      
      // Find matching route
      let componentFactory = null;
      let matchedPath = null;
      
      // First try exact match
      if (this.routes[path]) {
        componentFactory = this.routes[path];
        matchedPath = path;
      } else {
        // Try pattern matching (simple implementation)
        for (const [routePath, factory] of Object.entries(this.routes)) {
          if (routePath.includes(':') && this.pathMatches(routePath, path)) {
            componentFactory = factory;
            matchedPath = routePath;
            break;
          }
        }
        
        // Fallback to "/" or 404
        if (!componentFactory) {
          componentFactory = this.routes['/'] || ((p) => {
            const notFound = new jaffolding.Component('div');
            notFound.setText('404 - Page not found');
            return notFound;
          });
          matchedPath = '/';
        }
      }
      
      // Clear container
      while (this.container.firstChild) {
        this.container.removeChild(this.container.firstChild);
      }
      
      // Render new component
      const component = componentFactory(path);
      component.render(this.container);
    }

    /**
     * Gets the current path from the browser location.
     * @returns {string} - The current path
     * @private
     */
    getPath() {
      const path = window.location.pathname;
      return path || '/';
    }

    /**
     * Checks if a path matches a route pattern.
     * @param {string} routePath - The route pattern
     * @param {string} actualPath - The actual path
     * @returns {boolean} - Whether the path matches the pattern
     * @private
     */
    pathMatches(routePath, actualPath) {
      const routeParts = routePath.split('/');
      const actualParts = actualPath.split('/');
      
      if (routeParts.length !== actualParts.length) {
        return false;
      }
      
      for (let i = 0; i < routeParts.length; i++) {
        if (routeParts[i].startsWith(':')) {
          // This is a parameter, it matches anything
          continue;
        }
        
        if (routeParts[i] !== actualParts[i]) {
          return false;
        }
      }
      
      return true;
    }

    /**
     * Gets a parameter value from the current path.
     * @param {string} path - The current path
     * @param {string} paramName - The parameter name
     * @returns {string|null} - The parameter value or null if not found
     */
    getParameter(path, paramName) {
      for (const routePath of Object.keys(this.routes)) {
        if (routePath.includes(':') && this.pathMatches(routePath, path)) {
          const routeParts = routePath.split('/');
          const actualParts = path.split('/');
          
          for (let i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith(':') && 
                routeParts[i].substring(1) === paramName) {
              return actualParts[i];
            }
          }
        }
      }
      
      return null;
    }
  }

  // Export the Router class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Router = Router;
})();