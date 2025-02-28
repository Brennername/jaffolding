/**
 * KDE Plasma-inspired desktop environment for the Jaffolding framework.
 */
(function() {
  class KDEDesktop {
    /**
     * Creates a new KDEDesktop instance.
     * @param {HTMLElement} container - The container element
     */
    constructor(container) {
      this.container = container;
      this.windows = [];
      this.activeWindow = null;
      this.desktopIcons = null;
      this.dock = null;
      this.runningApps = {};
      
      this.initializeDesktop();
    }

    /**
     * Initializes the desktop environment.
     * @private
     */
    initializeDesktop() {
      // Set up desktop event listeners
      this.container.addEventListener('click', (e) => {
        // Check if clicking on the desktop background (not on a window)
        if (e.target === this.container || e.target.classList.contains('desktop-wallpaper')) {
          // Deactivate all windows
          this.windows.forEach(window => {
            if (window.element) {
              window.element.style.boxShadow = '0 4px 20px rgba(0, 0, 0, 0.15)';
            }
          });
          this.activeWindow = null;
        }
      });
      
      // Handle window resize
      window.addEventListener('resize', () => {
        this.adjustWindowPositions();
      });
    }

    /**
     * Creates desktop icons for applications.
     * @param {Object[]} apps - The applications to create icons for
     */
    createDesktopIcons(apps) {
      // Create desktop icons container if it doesn't exist
      if (!this.desktopIcons) {
        this.desktopIcons = document.createElement('div');
        this.desktopIcons.className = 'desktop-icons';
        this.container.appendChild(this.desktopIcons);
      } else {
        // Clear existing icons
        this.desktopIcons.innerHTML = '';
      }
      
      // Create icons for each app
      apps.forEach(app => {
        const icon = document.createElement('div');
        icon.className = 'desktop-icon';
        icon.setAttribute('data-app-id', app.id);
        
        const iconImage = document.createElement('div');
        iconImage.className = 'icon-image';
        iconImage.style.backgroundColor = app.color || '#5294e2';
        iconImage.innerHTML = app.icon || app.name.charAt(0);
        
        const iconLabel = document.createElement('div');
        iconLabel.className = 'icon-label';
        iconLabel.textContent = app.name;
        
        icon.appendChild(iconImage);
        icon.appendChild(iconLabel);
        
        // Add click handler to launch the app
        icon.addEventListener('click', () => {
          if (window.jaffolding && window.jaffolding.AppManager) {
            const appManager = this.getAppManager();
            if (appManager) {
              appManager.launchApp(app.id);
            }
          }
        });
        
        // Add double-click animation
        icon.addEventListener('dblclick', () => {
          iconImage.style.transform = 'scale(0.8)';
          setTimeout(() => {
            iconImage.style.transform = 'scale(1)';
          }, 100);
        });
        
        this.desktopIcons.appendChild(icon);
      });
    }

    /**
     * Creates the dock/taskbar with pinned applications.
     * @param {Object[]} apps - The applications to pin to the dock
     */
    createDock(apps) {
      // Create dock if it doesn't exist
      if (!this.dock) {
        this.dock = document.createElement('div');
        this.dock.className = 'plasma-dock';
        this.container.appendChild(this.dock);
      } else {
        // Clear existing dock items
        this.dock.innerHTML = '';
      }
      
      // Create dock items for each app
      apps.forEach((app, index) => {
        // Add separator after first item
        if (index === 1) {
          const separator = document.createElement('div');
          separator.className = 'dock-separator';
          this.dock.appendChild(separator);
        }
        
        const dockItem = document.createElement('div');
        dockItem.className = 'dock-item';
        dockItem.setAttribute('data-app-id', app.id);
        dockItem.style.backgroundColor = app.color || '#5294e2';
        dockItem.innerHTML = app.icon || app.name.charAt(0);
        
        // Add tooltip
        dockItem.setAttribute('title', app.name);
        
        // Add click handler to launch or focus the app
        dockItem.addEventListener('click', () => {
          if (window.jaffolding && window.jaffolding.AppManager) {
            const appManager = this.getAppManager();
            if (appManager) {
              if (appManager.isAppRunning(app.id)) {
                appManager.focusApp(app.id);
              } else {
                appManager.launchApp(app.id);
              }
            }
          }
        });
        
        this.dock.appendChild(dockItem);
      });
      
      // Add separator before system items
      const separator = document.createElement('div');
      separator.className = 'dock-separator';
      this.dock.appendChild(separator);
      
      // Add system items (settings, etc.)
      const settingsItem = document.createElement('div');
      settingsItem.className = 'dock-item';
      settingsItem.style.backgroundColor = '#607d8b';
      settingsItem.innerHTML = '⚙️';
      settingsItem.setAttribute('title', 'Settings');
      
      settingsItem.addEventListener('click', () => {
        this.showSettings();
      });
      
      this.dock.appendChild(settingsItem);
    }

    /**
     * Updates the dock to reflect running applications.
     * @param {string} appId - The application ID
     * @param {boolean} isRunning - Whether the application is running
     */
    updateDock(appId, isRunning) {
      if (!this.dock) return;
      
      // Update running state
      this.runningApps[appId] = isRunning;
      
      // Update dock item appearance
      const dockItem = this.dock.querySelector(`[data-app-id="${appId}"]`);
      if (dockItem) {
        if (isRunning) {
          dockItem.classList.add('active');
        } else {
          dockItem.classList.remove('active');
        }
      }
    }

    /**
     * Adds a window to the desktop.
     * @param {Window} window - The window to add
     * @returns {KDEDesktop} - The desktop instance for chaining
     */
    addWindow(window) {
      this.windows.push(window);
      
      // Render the window to the container
      window.render(this.container);
      
      // Set up window activation
      window.addEventListener('mousedown', () => {
        this.activateWindow(window);
      });
      
      // Set up window close handler
      const originalClose = window.close.bind(window);
      window.close = () => {
        // Call the original close method
        originalClose();
        
        // Remove from windows array
        const index = this.windows.indexOf(window);
        if (index !== -1) {
          this.windows.splice(index, 1);
        }
        
        // Call onClose callback if defined
        if (typeof window.onClose === 'function') {
          window.onClose();
        }
      };
      
      // Activate the window
      this.activateWindow(window);
      
      return this;
    }

    /**
     * Activates a window, bringing it to the front.
     * @param {Window} window - The window to activate
     */
    activateWindow(window) {
      // Deactivate all windows
      this.windows.forEach(w => {
        if (w.element) {
          w.element.style.boxShadow = '0 4px 20px rgba(0, 0, 0, 0.15)';
        }
      });
      
      // Activate the window
      if (window.element) {
        window.bringToFront();
        window.element.style.boxShadow = '0 0 0 2px #5294e2, 0 8px 30px rgba(0, 0, 0, 0.3)';
      }
      
      this.activeWindow = window;
    }

    /**
     * Adjusts window positions to ensure they are within the viewport.
     */
    adjustWindowPositions() {
      const viewportWidth = window.innerWidth;
      const viewportHeight = window.innerHeight;
      
      this.windows.forEach(window => {
        if (window.element) {
          const rect = window.element.getBoundingClientRect();
          
          // Check if window is outside the viewport
          if (rect.right > viewportWidth) {
            window.element.style.left = Math.max(0, viewportWidth - rect.width) + 'px';
          }
          
          if (rect.bottom > viewportHeight) {
            window.element.style.top = Math.max(0, viewportHeight - rect.height) + 'px';
          }
        }
      });
    }

    /**
     * Shows the settings dialog.
     */
    showSettings() {
      // Create settings content
      const content = new jaffolding.Component('div');
      content.setStyle('padding', '20px')
             .setStyle('color', '#eceff4');
      
      const title = new jaffolding.Component('h2');
      title.setText('Settings')
           .setStyle('margin-bottom', '20px');
      
      const themeSection = new jaffolding.Component('div');
      themeSection.setStyle('margin-bottom', '20px');
      
      const themeTitle = new jaffolding.Component('h3');
      themeTitle.setText('Desktop Theme')
               .setStyle('margin-bottom', '10px');
      
      const themeSelector = new jaffolding.Component('select');
      themeSelector.setStyle('width', '100%')
                  .setStyle('padding', '8px')
                  .setStyle('border-radius', '4px')
                  .setStyle('border', '1px solid #4c566a')
                  .setStyle('margin-bottom', '15px')
                  .setStyle('background-color', '#3b4252')
                  .setStyle('color', '#eceff4');
      
      const themes = [
        { name: 'Breeze Dark (Default)', value: 'breeze-dark' },
        { name: 'Breeze Light', value: 'breeze-light' },
        { name: 'Oxygen', value: 'oxygen' },
        { name: 'Adapta', value: 'adapta' }
      ];
      
      themes.forEach(theme => {
        const option = new jaffolding.Component('option');
        option.setText(theme.name);
        option.setAttribute('value', theme.value);
        themeSelector.addChild(option);
      });
      
      themeSelector.addEventListener('change', (e) => {
        this.changeTheme(e.target.value);
      });
      
      const aboutSection = new jaffolding.Component('div');
      aboutSection.setStyle('margin-top', '30px')
                 .setStyle('padding-top', '20px')
                 .setStyle('border-top', '1px solid #4c566a');
      
      const aboutTitle = new jaffolding.Component('h3');
      aboutTitle.setText('About Jaffolding Desktop')
               .setStyle('margin-bottom', '10px');
      
      const aboutText = new jaffolding.Component('p');
      aboutText.setText('Jaffolding Desktop v1.0.0')
              .setStyle('margin-bottom', '5px');
      
      const aboutDesc = new jaffolding.Component('p');
      aboutDesc.setText('A KDE Plasma-inspired desktop environment built with the Jaffolding framework.')
              .setStyle('margin-bottom', '15px');
      
      // Assemble the content
      themeSection.addChild(themeTitle);
      themeSection.addChild(themeSelector);
      
      aboutSection.addChild(aboutTitle);
      aboutSection.addChild(aboutText);
      aboutSection.addChild(aboutDesc);
      
      content.addChild(title);
      content.addChild(themeSection);
      content.addChild(aboutSection);
      
      // Create and show the window
      const settingsWindow = new jaffolding.Window('Settings', content);
      settingsWindow.setSize(400, 500);
      settingsWindow.setPosition(
        Math.floor((window.innerWidth - 400) / 2),
        Math.floor((window.innerHeight - 500) / 2)
      );
      
      this.addWindow(settingsWindow);
    }

    /**
     * Changes the desktop theme.
     * @param {string} theme - The theme name
     */
    changeTheme(theme) {
      const wallpaper = this.container.querySelector('.desktop-wallpaper');
      if (!wallpaper) return;
      
      switch (theme) {
        case 'breeze-light':
          wallpaper.style.background = 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)';
          document.body.style.color = '#333';
          break;
        case 'oxygen':
          wallpaper.style.background = 'linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)';
          document.body.style.color = '#e6e6e6';
          break;
        case 'adapta':
          wallpaper.style.background = 'linear-gradient(135deg, #43cea2 0%, #185a9d 100%)';
          document.body.style.color = '#e6e6e6';
          break;
        case 'breeze-dark':
        default:
          wallpaper.style.background = 'linear-gradient(135deg, #1e2229 0%, #2d3748 100%)';
          document.body.style.color = '#e6e6e6';
          break;
      }
    }

    /**
     * Gets the app manager instance.
     * @returns {AppManager|null} - The app manager instance
     * @private
     */
    getAppManager() {
      return window.jaffolding && window.jaffolding.AppManager ? 
        Object.values(window.__jaffolding_instances || {})
          .find(instance => instance instanceof window.jaffolding.AppManager) : 
        null;
    }
  }

  // Initialize instance tracking
  window.__jaffolding_instances = window.__jaffolding_instances || {};
  
  // Override the constructor to track instances
  const originalConstructor = KDEDesktop;
  KDEDesktop = function(...args) {
    const instance = new originalConstructor(...args);
    const id = 'kde_desktop_' + Date.now();
    window.__jaffolding_instances[id] = instance;
    return instance;
  };
  
  // Copy prototype
  KDEDesktop.prototype = originalConstructor.prototype;
  
  // Export the KDEDesktop class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.KDEDesktop = KDEDesktop;
})();