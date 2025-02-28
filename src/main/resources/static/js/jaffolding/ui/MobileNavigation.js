/**
 * Mobile-friendly navigation component for the Jaffolding framework.
 */
(function() {
  class MobileNavigation extends jaffolding.Component {
    /**
     * Creates a new MobileNavigation instance.
     * @param {Desktop} desktop - The desktop instance to navigate
     */
    constructor(desktop) {
      super('div');
      this.desktop = desktop;
      this.history = [];
      this.currentIndex = -1;
      this.touchStartX = 0;
      this.touchEndX = 0;
      
      this.initializeNavigation();
    }

    /**
     * Initializes the navigation structure and behavior.
     * @private
     */
    initializeNavigation() {
      this.setStyle('position', 'absolute')
          .setStyle('bottom', '0')
          .setStyle('left', '0')
          .setStyle('right', '0')
          .setStyle('height', '60px')
          .setStyle('background-color', 'rgba(0, 0, 0, 0.7)')
          .setStyle('backdrop-filter', 'blur(10px)')
          .setStyle('display', 'flex')
          .setStyle('align-items', 'center')
          .setStyle('justify-content', 'space-between')
          .setStyle('padding', '0 20px')
          .setStyle('z-index', '2000');
      
      // Back button
      const backButton = new jaffolding.Component('button');
      backButton.setText('←')
                .setStyle('width', '40px')
                .setStyle('height', '40px')
                .setStyle('border-radius', '20px')
                .setStyle('background-color', 'rgba(255, 255, 255, 0.2)')
                .setStyle('color', 'white')
                .setStyle('border', 'none')
                .setStyle('font-size', '18px')
                .setStyle('cursor', 'pointer')
                .setStyle('display', 'flex')
                .setStyle('align-items', 'center')
                .setStyle('justify-content', 'center')
                .addEventListener('click', () => {
                  this.goBack();
                });
      
      // Forward button
      const forwardButton = new jaffolding.Component('button');
      forwardButton.setText('→')
                   .setStyle('width', '40px')
                   .setStyle('height', '40px')
                   .setStyle('border-radius', '20px')
                   .setStyle('background-color', 'rgba(255, 255, 255, 0.2)')
                   .setStyle('color', 'white')
                   .setStyle('border', 'none')
                   .setStyle('font-size', '18px')
                   .setStyle('cursor', 'pointer')
                   .setStyle('display', 'flex')
                   .setStyle('align-items', 'center')
                   .setStyle('justify-content', 'center')
                   .addEventListener('click', () => {
                     this.goForward();
                   });
      
      // Home button
      const homeButton = new jaffolding.Component('button');
      homeButton.setText('⌂')
                .setStyle('width', '40px')
                .setStyle('height', '40px')
                .setStyle('border-radius', '20px')
                .setStyle('background-color', 'rgba(255, 255, 255, 0.2)')
                .setStyle('color', 'white')
                .setStyle('border', 'none')
                .setStyle('font-size', '18px')
                .setStyle('cursor', 'pointer')
                .setStyle('display', 'flex')
                .setStyle('align-items', 'center')
                .setStyle('justify-content', 'center')
                .addEventListener('click', () => {
                  this.goHome();
                });
      
      // Calculator button
      const calcButton = new jaffolding.Component('button');
      calcButton.setText('🧮')
                .setStyle('width', '40px')
                .setStyle('height', '40px')
                .setStyle('border-radius', '20px')
                .setStyle('background-color', 'rgba(255, 255, 255, 0.2)')
                .setStyle('color', 'white')
                .setStyle('border', 'none')
                .setStyle('font-size', '18px')
                .setStyle('cursor', 'pointer')
                .setStyle('display', 'flex')
                .setStyle('align-items', 'center')
                .setStyle('justify-content', 'center')
                .addEventListener('click', () => {
                  this.openCalculator();
                });
      
      // Add buttons to the navigation bar
      this.addChild(backButton);
      this.addChild(homeButton);
      this.addChild(calcButton);
      this.addChild(forwardButton);
      
      // Set up touch event listeners for swipe navigation
      this.addEventListener('touchstart', (e) => {
        this.touchStartX = e.touches[0].clientX;
      });
      
      this.addEventListener('touchend', (e) => {
        this.touchEndX = e.changedTouches[0].clientX;
        this.handleSwipe();
      });
      
      // Listen for window creation and closing
      document.addEventListener('windowopen', (e) => {
        this.addToHistory(e.detail.window);
      });
      
      document.addEventListener('windowclose', (e) => {
        // If the closed window is the current one, go back
        if (this.currentIndex >= 0 && 
            this.history[this.currentIndex] === e.detail.window) {
          this.goBack();
        }
      });
    }

    /**
     * Handles swipe gestures.
     * @private
     */
    handleSwipe() {
      const swipeThreshold = 100; // Minimum distance for a swipe
      
      if (this.touchEndX - this.touchStartX > swipeThreshold) {
        // Swipe right - go back
        this.goBack();
      } else if (this.touchStartX - this.touchEndX > swipeThreshold) {
        // Swipe left - go forward
        this.goForward();
      }
    }

    /**
     * Adds a window to the navigation history.
     * @param {Window} window - The window to add
     */
    addToHistory(window) {
      // Remove any forward history
      if (this.currentIndex < this.history.length - 1) {
        this.history = this.history.slice(0, this.currentIndex + 1);
      }
      
      // Add the window to history
      this.history.push(window);
      this.currentIndex = this.history.length - 1;
      
      // Bring the window to front
      window.bringToFront();
    }

    /**
     * Navigates back in the history.
     */
    goBack() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
        const window = this.history[this.currentIndex];
        
        // Check if the window is still open
        if (window.getElement() && window.getElement().parentNode) {
          window.bringToFront();
        } else {
          // Window was closed, try to go back further
          this.goBack();
        }
      }
    }

    /**
     * Navigates forward in the history.
     */
    goForward() {
      if (this.currentIndex < this.history.length - 1) {
        this.currentIndex++;
        const window = this.history[this.currentIndex];
        
        // Check if the window is still open
        if (window.getElement() && window.getElement().parentNode) {
          window.bringToFront();
        } else {
          // Window was closed, try to go forward further
          this.goForward();
        }
      }
    }

    /**
     * Navigates to the home window.
     */
    goHome() {
      if (this.history.length > 0) {
        // Find the first window in history that's still open
        for (let i = 0; i < this.history.length; i++) {
          const window = this.history[i];
          if (window.getElement() && window.getElement().parentNode) {
            this.currentIndex = i;
            window.bringToFront();
            break;
          }
        }
      }
    }

    /**
     * Opens the calculator window.
     */
    openCalculator() {
      // Create a calculator window
      const calcWindow = createCalculatorWindow();
      calcWindow.setPosition(
        Math.floor((window.innerWidth - 300) / 2),
        Math.floor((window.innerHeight - 400) / 2)
      );
      
      // Add the window to the desktop
      this.desktop.addWindow(calcWindow);
      
      // Dispatch a custom event for the window opening
      const openEvent = new CustomEvent('windowopen', {
        detail: { window: calcWindow }
      });
      document.dispatchEvent(openEvent);
    }
  }

  /**
   * Creates a calculator window.
   * @returns {Window} - The calculator window
   */
  function createCalculatorWindow() {
    const content = new jaffolding.Panel();
    content.setLayout(new jaffolding.BorderLayout());
    
    // Create calculator component
    const calculator = new jaffolding.Calculator();
    
    content.addChild(calculator, jaffolding.BorderLayout.CENTER);
    
    const window = new jaffolding.Window('Calculator', content);
    window.setSize(300, 400);
    
    return window;
  }

  // Export the MobileNavigation class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.MobileNavigation = MobileNavigation;
})();