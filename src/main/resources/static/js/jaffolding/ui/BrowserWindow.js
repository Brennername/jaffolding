/**
 * A browser-like window component for the Jaffolding framework.
 */
(function() {
  class BrowserWindow extends jaffolding.Window {
    /**
     * Creates a new BrowserWindow instance.
     * @param {string} title - The window title
     * @param {string} url - The initial URL
     */
    constructor(title, url) {
      // Create content container
      const content = new jaffolding.Component('div');
      content.setStyle('display', 'flex')
             .setStyle('flex-direction', 'column')
             .setStyle('height', '100%');
      
      super(title, content);
      
      this.url = url;
      this.history = [url];
      this.currentIndex = 0;
      
      this.initializeBrowser();
    }

    /**
     * Initializes the browser structure and behavior.
     * @private
     */
    initializeBrowser() {
      // Create browser toolbar
      const toolbar = new jaffolding.Component('div');
      toolbar.setStyle('display', 'flex')
             .setStyle('align-items', 'center')
             .setStyle('padding', '8px 12px')
             .setStyle('background-color', '#2e3440')
             .setStyle('border-bottom', '1px solid #434c5e');
      
      // Navigation buttons
      const backButton = new jaffolding.Component('button');
      backButton.setText('←')
                .setStyle('width', '30px')
                .setStyle('height', '30px')
                .setStyle('border-radius', '4px')
                .setStyle('border', 'none')
                .setStyle('background-color', '#4c566a')
                .setStyle('color', '#eceff4')
                .setStyle('margin-right', '5px')
                .setStyle('cursor', 'pointer')
                .addEventListener('click', () => {
                  this.goBack();
                });
      
      const forwardButton = new jaffolding.Component('button');
      forwardButton.setText('→')
                   .setStyle('width', '30px')
                   .setStyle('height', '30px')
                   .setStyle('border-radius', '4px')
                   .setStyle('border', 'none')
                   .setStyle('background-color', '#4c566a')
                   .setStyle('color', '#eceff4')
                   .setStyle('margin-right', '5px')
                   .setStyle('cursor', 'pointer')
                   .addEventListener('click', () => {
                     this.goForward();
                   });
      
      const refreshButton = new jaffolding.Component('button');
      refreshButton.setText('↻')
                   .setStyle('width', '30px')
                   .setStyle('height', '30px')
                   .setStyle('border-radius', '4px')
                   .setStyle('border', 'none')
                   .setStyle('background-color', '#4c566a')
                   .setStyle('color', '#eceff4')
                   .setStyle('margin-right', '10px')
                   .setStyle('cursor', 'pointer')
                   .addEventListener('click', () => {
                     this.refresh();
                   });
      
      // Address bar
      const addressBar = new jaffolding.Component('input');
      addressBar.setAttribute('type', 'text')
                .setAttribute('value', this.url)
                .setStyle('flex', '1')
                .setStyle('padding', '8px 12px')
                .setStyle('border-radius', '4px')
                .setStyle('border', '1px solid #4c566a')
                .setStyle('background-color', '#3b4252')
                .setStyle('color', '#eceff4')
                .setStyle('font-size', '14px')
                .addEventListener('keypress', (e) => {
                  if (e.key === 'Enter') {
                    this.navigate(e.target.value);
                  }
                });
      
      this.addressBar = addressBar;
      
      // Add elements to toolbar
      toolbar.addChild(backButton);
      toolbar.addChild(forwardButton);
      toolbar.addChild(refreshButton);
      toolbar.addChild(addressBar);
      
      // Create content frame
      const contentFrame = new jaffolding.Component('div');
      contentFrame.setStyle('flex', '1')
                  .setStyle('overflow', 'auto')
                  .setStyle('background-color', '#3b4252');
      
      this.contentFrame = contentFrame;
      
      // Add toolbar and content frame to the window content
      this.content.addChild(toolbar);
      this.content.addChild(contentFrame);
      
      // Load initial content
      this.loadContent();
    }

    /**
     * Navigates to a URL.
     * @param {string} url - The URL to navigate to
     */
    navigate(url) {
      // Remove any forward history
      if (this.currentIndex < this.history.length - 1) {
        this.history = this.history.slice(0, this.currentIndex + 1);
      }
      
      // Add the URL to history
      this.history.push(url);
      this.currentIndex = this.history.length - 1;
      
      // Update the URL
      this.url = url;
      
      // Update the address bar
      if (this.addressBar && this.addressBar.getElement()) {
        this.addressBar.getElement().value = url;
      }
      
      // Load the content
      this.loadContent();
    }

    /**
     * Goes back in the browser history.
     */
    goBack() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
        this.url = this.history[this.currentIndex];
        
        // Update the address bar
        if (this.addressBar && this.addressBar.getElement()) {
          this.addressBar.getElement().value = this.url;
        }
        
        // Load the content
        this.loadContent();
      }
    }

    /**
     * Goes forward in the browser history.
     */
    goForward() {
      if (this.currentIndex < this.history.length - 1) {
        this.currentIndex++;
        this.url = this.history[this.currentIndex];
        
        // Update the address bar
        if (this.addressBar && this.addressBar.getElement()) {
          this.addressBar.getElement().value = this.url;
        }
        
        // Load the content
        this.loadContent();
      }
    }

    /**
     * Refreshes the current page.
     */
    refresh() {
      this.loadContent();
    }

    /**
     * Loads content based on the current URL.
     * @private
     */
    loadContent() {
      if (!this.contentFrame) return;
      
      // Clear existing content
      this.contentFrame.clear();
      
      // Show loading indicator
      const loading = new jaffolding.Component('div');
      loading.setText('Loading...')
             .setStyle('padding', '20px')
             .setStyle('text-align', 'center')
             .setStyle('color', '#eceff4');
      
      this.contentFrame.addChild(loading);
      
      // Parse the URL
      const path = this.url.startsWith('/') ? this.url : '/' + this.url;
      
      // Create content based on the path
      setTimeout(() => {
        this.contentFrame.clear();
        
        let content;
        
        // Map paths to demo components
        switch (path) {
          case '/components':
            content = jaffolding.demos.ComponentDemo.create();
            break;
          case '/chart':
            content = jaffolding.demos.ChartDemo.create();
            break;
          case '/three':
            content = jaffolding.demos.ThreeDemo.create();
            break;
          case '/sales':
            content = jaffolding.demos.SalesDataDemo.create();
            break;
          case '/animation':
            content = jaffolding.demos.AnimationDemo.create();
            break;
          default:
            // 404 page
            content = new jaffolding.Component('div');
            content.setStyle('padding', '20px')
                   .setStyle('text-align', 'center')
                   .setStyle('color', '#eceff4');
            
            const errorTitle = new jaffolding.Component('h2');
            errorTitle.setText('404 - Page Not Found');
            
            const errorMessage = new jaffolding.Component('p');
            errorMessage.setText(`The page "${path}" could not be found.`);
            
            content.addChild(errorTitle);
            content.addChild(errorMessage);
        }
        
        this.contentFrame.addChild(content);
      }, 500);
    }
  }

  // Export the BrowserWindow class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.BrowserWindow = BrowserWindow;
})();