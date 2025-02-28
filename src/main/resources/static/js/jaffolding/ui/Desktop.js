/**
 * A desktop environment component with a taskbar.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Desktop extends jaffolding.Component {
    /**
     * Creates a new Desktop instance.
     */
    constructor() {
      super('div');
      this.desktopArea = null;
      this.taskbar = null;
      this.navigation = null;
      this.taskbarItems = [];
      this.windows = [];
      this.isMobile = window.innerWidth < 768;
      
      this.initializeDesktop();
    }

    /**
     * Initializes the desktop structure and behavior.
     * @private
     */
    initializeDesktop() {
      this.setStyle('position', 'relative')
          .setStyle('width', '100%')
          .setStyle('height', '100%')
          .setStyle('overflow', 'hidden')
          .setStyle('background', 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)');
      
      // Create desktop area
      this.desktopArea = new jaffolding.Component('div');
      this.desktopArea.setStyle('position', 'absolute')
                      .setStyle('top', '0')
                      .setStyle('left', '0')
                      .setStyle('right', '0')
                      .setStyle('bottom', this.isMobile ? '120px' : '60px') // Leave space for taskbar and navigation
                      .setStyle('overflow', 'hidden');
      
      // Create taskbar
      this.taskbar = new jaffolding.Component('div');
      this.taskbar.setStyle('position', 'absolute')
                  .setStyle('bottom', this.isMobile ? '70px' : '20px')
                  .setStyle('left', '50%')
                  .setStyle('transform', 'translateX(-50%)')
                  .setStyle('height', '50px')
                  .setStyle('background-color', 'rgba(255, 255, 255, 0.7)')
                  .setStyle('backdrop-filter', 'blur(10px)')
                  .setStyle('border-radius', '25px')
                  .setStyle('box-shadow', '0 4px 20px rgba(0, 0, 0, 0.1)')
                  .setStyle('display', 'flex')
                  .setStyle('align-items', 'center')
                  .setStyle('padding', '0 15px')
                  .setStyle('z-index', '1000')
                  .setStyle('overflow-x', 'auto')
                  .setStyle('overflow-y', 'hidden')
                  .setStyle('white-space', 'nowrap')
                  .setStyle('scrollbar-width', 'none') // Firefox
                  .setStyle('-ms-overflow-style', 'none'); // IE/Edge
      
      // Hide scrollbar for Chrome/Safari
      const style = new jaffolding.Component('style');
      style.setText(`
        #taskbar::-webkit-scrollbar {
          display: none;
        }
      `);
      this.addChild(style);
      this.taskbar.setAttribute('id', 'taskbar');
      
      // Add mobile navigation if on mobile
      if (this.isMobile) {
        this.navigation = new jaffolding.MobileNavigation(this);
      }
      
      this.addChild(this.desktopArea);
      this.addChild(this.taskbar);
      if (this.navigation) {
        this.addChild(this.navigation);
      }
      
      // Listen for window close events
      document.addEventListener('windowclose', (e) => {
        const closedWindow = e.detail.window;
        const index = this.windows.indexOf(closedWindow);
        
        if (index !== -1) {
          this.windows.splice(index, 1);
          
          // Remove taskbar item
          if (index < this.taskbarItems.length) {
            const taskbarItem = this.taskbarItems[index];
            taskbarItem.removeFromParent();
            this.taskbarItems.splice(index, 1);
            
            // Adjust taskbar width
            this.adjustTaskbarWidth();
          }
        }
      });
      
      // Handle window resize
      window.addEventListener('resize', () => {
        const wasMobile = this.isMobile;
        this.isMobile = window.innerWidth < 768;
        
        // Update layout if mobile state changed
        if (wasMobile !== this.isMobile) {
          this.updateLayout();
        }
      });
    }

    /**
     * Updates the desktop layout based on mobile state.
     * @private
     */
    updateLayout() {
      // Update desktop area
      this.desktopArea.setStyle('bottom', this.isMobile ? '120px' : '60px');
      
      // Update taskbar position
      this.taskbar.setStyle('bottom', this.isMobile ? '70px' : '20px');
      
      // Add or remove navigation
      if (this.isMobile && !this.navigation) {
        this.navigation = new jaffolding.MobileNavigation(this);
        this.addChild(this.navigation);
      } else if (!this.isMobile && this.navigation) {
        this.navigation.removeFromParent();
        this.navigation = null;
      }
    }

    /**
     * Adds a window to the desktop.
     * @param {Window} window - The window to add
     * @returns {Desktop} - The desktop instance for chaining
     */
    addWindow(window) {
      this.windows.push(window);
      this.desktopArea.addChild(window);
      
      // Create taskbar item for the window
      const taskbarItem = this.createTaskbarItem(window);
      this.taskbarItems.push(taskbarItem);
      this.taskbar.addChild(taskbarItem);
      
      // Adjust taskbar width
      this.adjustTaskbarWidth();
      
      // Dispatch a custom event for the window opening
      const openEvent = new CustomEvent('windowopen', {
        detail: { window: window }
      });
      document.dispatchEvent(openEvent);
      
      return this;
    }

    /**
     * Creates a taskbar item for a window.
     * @param {Window} window - The window to create an item for
     * @returns {Component} - The taskbar item component
     * @private
     */
    createTaskbarItem(window) {
      const taskbarItem = new jaffolding.Component('div');
      taskbarItem.setStyle('width', '40px')
                 .setStyle('height', '40px')
                 .setStyle('background-color', 'rgba(66, 133, 244, 0.8)')
                 .setStyle('border-radius', '20px')
                 .setStyle('margin', '0 5px')
                 .setStyle('cursor', 'pointer')
                 .setStyle('transition', 'transform 0.2s, background-color 0.2s')
                 .setStyle('display', 'inline-flex')
                 .setStyle('align-items', 'center')
                 .setStyle('justify-content', 'center')
                 .setStyle('color', 'white')
                 .setStyle('font-weight', 'bold')
                 .setStyle('font-size', '16px')
                 .setText(window.title ? window.title.charAt(0) : '?')
                 .addEventListener('mouseover', () => {
                   taskbarItem.setStyle('transform', 'scale(1.1)');
                 })
                 .addEventListener('mouseout', () => {
                   taskbarItem.setStyle('transform', 'scale(1)');
                 })
                 .addEventListener('click', () => {
                   window.bringToFront();
                 });
      
      // Add tooltip with window title
      if (window.title) {
        taskbarItem.setAttribute('title', window.title);
      }
      
      return taskbarItem;
    }

    /**
     * Adjusts the taskbar width based on the number of items.
     * @private
     */
    adjustTaskbarWidth() {
      const itemWidth = 50; // 40px + 5px margin on each side
      const minWidth = 60;
      const maxWidth = Math.min(800, window.innerWidth - 40);
      
      const contentWidth = Math.max(minWidth, this.taskbarItems.length * itemWidth);
      const taskbarWidth = Math.min(maxWidth, contentWidth);
      
      this.taskbar.setStyle('width', taskbarWidth + 'px');
    }
  }

  // Export the Desktop class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Desktop = Desktop;
})();