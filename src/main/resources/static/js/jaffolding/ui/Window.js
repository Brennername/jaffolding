/**
 * A draggable, resizable, closeable, minimizable, maximizable window component.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Window extends jaffolding.Component {
    /**
     * Creates a new Window instance.
     * @param {string} title - The window title
     * @param {Component} content - The window content
     */
    constructor(title, content) {
      super('div');
      this.title = title;
      this.content = content;
      this.draggable = true;
      this.resizable = true;
      this.closeable = true;
      this.minimizable = true;
      this.maximizable = true;
      
      this.isDragging = false;
      this.isResizing = false;
      this.dragOffsetX = 0;
      this.dragOffsetY = 0;
      this.resizeStartX = 0;
      this.resizeStartY = 0;
      this.initialWidth = 0;
      this.initialHeight = 0;
      
      this.isMinimized = false;
      this.isMaximized = false;
      this.savedX = 0;
      this.savedY = 0;
      this.savedWidth = 0;
      this.savedHeight = 0;
      
      this.titleBar = null;
      this.contentArea = null;
      this.resizeHandle = null;
      
      // Generate a unique ID for the window
      this.id = 'window_' + Date.now() + '_' + Math.floor(Math.random() * 1000);
      
      this.initializeWindow();
    }

    /**
     * Initializes the window structure and behavior.
     * @private
     */
    initializeWindow() {
      this.setStyle('position', 'absolute')
          .setStyle('top', '50px')
          .setStyle('left', '50px')
          .setStyle('width', '400px')
          .setStyle('height', '300px')
          .setStyle('background-color', '#2e3440')
          .setStyle('border-radius', '8px')
          .setStyle('box-shadow', '0 4px 20px rgba(0, 0, 0, 0.15)')
          .setStyle('display', 'flex')
          .setStyle('flex-direction', 'column')
          .setStyle('overflow', 'hidden')
          .setStyle('transition', 'box-shadow 0.2s ease')
          .setStyle('color', '#e6e6e6');
      
      // Create title bar
      this.titleBar = new jaffolding.Component('div');
      this.titleBar.setStyle('display', 'flex')
                   .setStyle('align-items', 'center')
                   .setStyle('padding', '8px 12px')
                   .setStyle('background-color', '#3b4252')
                   .setStyle('border-bottom', '1px solid #434c5e')
                   .setStyle('cursor', this.draggable ? 'move' : 'default')
                   .setStyle('user-select', 'none');
      
      const titleLabel = new jaffolding.Component('div');
      titleLabel.setText(this.title)
                .setStyle('flex', '1')
                .setStyle('font-weight', '500')
                .setStyle('white-space', 'nowrap')
                .setStyle('overflow', 'hidden')
                .setStyle('text-overflow', 'ellipsis')
                .setStyle('color', '#eceff4');
      
      const windowControls = new jaffolding.Component('div');
      windowControls.setStyle('display', 'flex')
                    .setStyle('gap', '8px');
      
      if (this.minimizable) {
        const minimizeBtn = new jaffolding.Component('button');
        minimizeBtn.setText('−')
                   .setStyle('width', '20px')
                   .setStyle('height', '20px')
                   .setStyle('display', 'flex')
                   .setStyle('align-items', 'center')
                   .setStyle('justify-content', 'center')
                   .setStyle('background-color', '#ebcb8b')
                   .setStyle('color', '#2e3440')
                   .setStyle('border', 'none')
                   .setStyle('border-radius', '50%')
                   .setStyle('font-size', '14px')
                   .setStyle('cursor', 'pointer')
                   .addEventListener('click', (e) => {
                     this.toggleMinimize();
                     e.stopPropagation();
                   });
        
        windowControls.addChild(minimizeBtn);
      }
      
      if (this.maximizable) {
        const maximizeBtn = new jaffolding.Component('button');
        maximizeBtn.setText('□')
                   .setStyle('width', '20px')
                   .setStyle('height', '20px')
                   .setStyle('display', 'flex')
                   .setStyle('align-items', 'center')
                   .setStyle('justify-content', 'center')
                   .setStyle('background-color', '#a3be8c')
                   .setStyle('color', '#2e3440')
                   .setStyle('border', 'none')
                   .setStyle('border-radius', '50%')
                   .setStyle('font-size', '10px')
                   .setStyle('cursor', 'pointer')
                   .addEventListener('click', (e) => {
                     this.toggleMaximize();
                     e.stopPropagation();
                   });
        
        windowControls.addChild(maximizeBtn);
      }
      
      if (this.closeable) {
        const closeBtn = new jaffolding.Component('button');
        closeBtn.setText('×')
                .setStyle('width', '20px')
                .setStyle('height', '20px')
                .setStyle('display', 'flex')
                .setStyle('align-items', 'center')
                .setStyle('justify-content', 'center')
                .setStyle('background-color', '#bf616a')
                .setStyle('color', '#2e3440')
                .setStyle('border', 'none')
                .setStyle('border-radius', '50%')
                .setStyle('font-size', '14px')
                .setStyle('cursor', 'pointer')
                .addEventListener('click', (e) => {
                  this.close();
                  e.stopPropagation();
                });
        
        windowControls.addChild(closeBtn);
      }
      
      this.titleBar.addChild(titleLabel);
      this.titleBar.addChild(windowControls);
      
      // Create content area
      this.contentArea = new jaffolding.Component('div');
      this.contentArea.setStyle('flex', '1')
                      .setStyle('overflow', 'auto')
                      .setStyle('position', 'relative')
                      .setStyle('background-color', '#3b4252');
      
      this.contentArea.addChild(this.content);
      
      // Create resize handle
      if (this.resizable) {
        this.resizeHandle = new jaffolding.Component('div');
        this.resizeHandle.setStyle('position', 'absolute')
                         .setStyle('bottom', '0')
                         .setStyle('right', '0')
                         .setStyle('width', '16px')
                         .setStyle('height', '16px')
                         .setStyle('cursor', 'nwse-resize')
                         .setStyle('background', 'linear-gradient(135deg, transparent 50%, rgba(236, 239, 244, 0.2) 50%)')
                         .setStyle('z-index', '10');
        
        this.addChild(this.resizeHandle);
      }
      
      this.addChild(this.titleBar);
      this.addChild(this.contentArea);
      
      // Set up drag functionality
      if (this.draggable) {
        this.titleBar.addEventListener('mousedown', (e) => {
          this.startDrag(e);
          e.stopPropagation();
        });
        
        // Touch support for mobile
        this.titleBar.addEventListener('touchstart', (e) => {
          const touch = e.touches[0];
          const mouseEvent = {
            clientX: touch.clientX,
            clientY: touch.clientY
          };
          this.startDrag(mouseEvent);
          e.preventDefault();
        });
      }
      
      // Set up resize functionality
      if (this.resizable) {
        this.resizeHandle.addEventListener('mousedown', (e) => {
          this.startResize(e);
          e.stopPropagation();
        });
        
        // Touch support for mobile
        this.resizeHandle.addEventListener('touchstart', (e) => {
          const touch = e.touches[0];
          const mouseEvent = {
            clientX: touch.clientX,
            clientY: touch.clientY
          };
          this.startResize(mouseEvent);
          e.preventDefault();
        });
      }
      
      // Global mouse/touch events for drag and resize
      const moveHandler = (e) => {
        let clientX, clientY;
        
        if (e.type === 'touchmove') {
          const touch = e.touches[0];
          clientX = touch.clientX;
          clientY = touch.clientY;
          e.preventDefault();
        } else {
          clientX = e.clientX;
          clientY = e.clientY;
        }
        
        if (this.isDragging) {
          this.drag({ clientX, clientY });
        } else if (this.isResizing) {
          this.resize({ clientX, clientY });
        }
      };
      
      const endHandler = () => {
        this.isDragging = false;
        this.isResizing = false;
        if (this.element) {
          this.element.style.transition = 'box-shadow 0.2s ease';
        }
      };
      
      // Mouse events
      document.addEventListener('mousemove', moveHandler);
      document.addEventListener('mouseup', endHandler);
      
      // Touch events
      document.addEventListener('touchmove', moveHandler, { passive: false });
      document.addEventListener('touchend', endHandler);
      
      // Focus handling
      this.addEventListener('mousedown', () => {
        this.bringToFront();
      });
      
      this.addEventListener('touchstart', () => {
        this.bringToFront();
      });
    }

    /**
     * Starts dragging the window.
     * @param {MouseEvent|Object} e - The mouse event or touch event data
     * @private
     */
    startDrag(e) {
      if (this.isMaximized) return;
      
      this.isDragging = true;
      
      if (this.element) {
        this.element.style.transition = 'none';
        
        // Calculate the offset from the mouse position to the window's top-left corner
        const rect = this.element.getBoundingClientRect();
        this.dragOffsetX = e.clientX - rect.left;
        this.dragOffsetY = e.clientY - rect.top;
      }
    }

    /**
     * Drags the window.
     * @param {Object} e - The mouse or touch event data
     * @private
     */
    drag(e) {
      if (!this.isDragging) return;
      
      if (this.element) {
        let newX = e.clientX - this.dragOffsetX;
        let newY = e.clientY - this.dragOffsetY;
        
        // Ensure the window stays within the viewport
        newX = Math.max(0, Math.min(newX, window.innerWidth - this.element.offsetWidth));
        newY = Math.max(0, Math.min(newY, window.innerHeight - this.element.offsetHeight));
        
        this.element.style.left = newX + 'px';
        this.element.style.top = newY + 'px';
      }
    }

    /**
     * Starts resizing the window.
     * @param {MouseEvent|Object} e - The mouse event or touch event data
     * @private
     */
    startResize(e) {
      if (this.isMaximized) return;
      
      this.isResizing = true;
      
      if (this.element) {
        this.element.style.transition = 'none';
        
        this.resizeStartX = e.clientX;
        this.resizeStartY = e.clientY;
        this.initialWidth = this.element.offsetWidth;
        this.initialHeight = this.element.offsetHeight;
      }
    }

    /**
     * Resizes the window.
     * @param {Object} e - The mouse or touch event data
     * @private
     */
    resize(e) {
      if (!this.isResizing) return;
      
      if (this.element) {
        const deltaX = e.clientX - this.resizeStartX;
        const deltaY = e.clientY - this.resizeStartY;
        
        const newWidth = Math.max(200, this.initialWidth + deltaX);
        const newHeight = Math.max(150, this.initialHeight + deltaY);
        
        this.element.style.width = newWidth + 'px';
        this.element.style.height = newHeight + 'px';
      }
    }

    /**
     * Toggles the minimized state of the window.
     */
    toggleMinimize() {
      this.isMinimized = !this.isMinimized;
      
      if (this.element) {
        if (this.isMinimized) {
          this.savedHeight = this.element.offsetHeight;
          this.contentArea.getElement().style.display = 'none';
          if (this.resizeHandle) {
            this.resizeHandle.getElement().style.display = 'none';
          }
          this.element.style.height = 'auto';
        } else {
          this.contentArea.getElement().style.display = 'block';
          if (this.resizeHandle) {
            this.resizeHandle.getElement().style.display = 'block';
          }
          this.element.style.height = this.savedHeight + 'px';
        }
      }
    }

    /**
     * Toggles the maximized state of the window.
     */
    toggleMaximize() {
      this.isMaximized = !this.isMaximized;
      
      if (this.element) {
        if (this.isMaximized) {
          // Save current position and size
          const rect = this.element.getBoundingClientRect();
          this.savedX = rect.left;
          this.savedY = rect.top;
          this.savedWidth = this.element.offsetWidth;
          this.savedHeight = this.element.offsetHeight;
          
          // Maximize
          this.element.style.top = '0';
          this.element.style.left = '0';
          this.element.style.width = '100%';
          this.element.style.height = '100%';
          this.element.style.borderRadius = '0';
        } else {
          // Restore
          this.element.style.top = this.savedY + 'px';
          this.element.style.left = this.savedX + 'px';
          this.element.style.width = this.savedWidth + 'px';
          this.element.style.height = this.savedHeight + 'px';
          this.element.style.borderRadius = '8px';
        }
      }
    }

    /**
     * Closes the window.
     */
    close() {
      if (this.element && this.element.parentNode) {
        this.element.parentNode.removeChild(this.element);
        
        // Dispatch a custom close event
        const closeEvent = new CustomEvent('windowclose', {
          detail: { window: this }
        });
        document.dispatchEvent(closeEvent);
        
        // Call onClose callback if defined
        if (typeof this.onClose === 'function') {
          this.onClose();
        }
      }
    }

    /**
     * Brings the window to the front.
     */
    bringToFront() {
      if (this.element) {
        // Find the highest z-index among siblings
        const parent = this.element.parentNode;
        let highestZIndex = 0;
        
        for (let i = 0; i < parent.children.length; i++) {
          const sibling = parent.children[i];
          const zIndexStr = getComputedStyle(sibling).zIndex;
          if (zIndexStr && zIndexStr !== 'auto') {
            const zIndex = parseInt(zIndexStr, 10);
            if (!isNaN(zIndex)) {
              highestZIndex = Math.max(highestZIndex, zIndex);
            }
          }
        }
        
        // Set this window's z-index to be higher
        this.element.style.zIndex = (highestZIndex + 1).toString();
      }
    }

    /**
     * Sets the position of the window.
     * @param {number} x - The x coordinate
     * @param {number} y - The y coordinate
     * @returns {Window} - The window instance for chaining
     */
    setPosition(x, y) {
      this.setStyle('left', x + 'px');
      this.setStyle('top', y + 'px');
      return this;
    }

    /**
     * Sets the size of the window.
     * @param {number} width - The width in pixels
     * @param {number} height - The height in pixels
     * @returns {Window} - The window instance for chaining
     */
    setSize(width, height) {
      this.setStyle('width', width + 'px');
      this.setStyle('height', height + 'px');
      return this;
    }

    /**
     * Sets the width of the window.
     * @param {number} width - The width in pixels
     * @returns {Window} - The window instance for chaining
     */
    setWidth(width) {
      this.setStyle('width', width + 'px');
      return this;
    }

    /**
     * Sets the height of the window.
     * @param {number} height - The height in pixels
     * @returns {Window} - The window instance for chaining
     */
    setHeight(height) {
      this.setStyle('height', height + 'px');
      return this;
    }

    /**
     * Sets whether the window is draggable.
     * @param {boolean} draggable - Whether the window is draggable
     * @returns {Window} - The window instance for chaining
     */
    setDraggable(draggable) {
      this.draggable = draggable;
      if (this.titleBar) {
        this.titleBar.setStyle('cursor', draggable ? 'move' : 'default');
      }
      return this;
    }

    /**
     * Sets whether the window is resizable.
     * @param {boolean} resizable - Whether the window is resizable
     * @returns {Window} - The window instance for chaining
     */
    setResizable(resizable) {
      this.resizable = resizable;
      if (this.resizeHandle) {
        this.resizeHandle.setStyle('display', resizable ? 'block' : 'none');
      }
      return this;
    }
  }

  // Export the Window class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Window = Window;
})();