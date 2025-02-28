/**
 * Main JavaScript file for Jaffolding framework
 * This is a fallback implementation when TeaVM is not available
 */
document.addEventListener('DOMContentLoaded', function() {
  console.log('Initializing Jaffolding JS implementation...');
  
  // Load all required modules
  loadModules([
    '/js/jaffolding/core/Component.js',
    '/js/jaffolding/core/State.js',
    '/js/jaffolding/core/Router.js',
    '/js/jaffolding/ui/Animation.js',
    '/js/jaffolding/ui/Button.js',
    '/js/jaffolding/ui/Panel.js',
    '/js/jaffolding/ui/Window.js',
    '/js/jaffolding/ui/Desktop.js',
    '/js/jaffolding/ui/ChartComponent.js',
    '/js/jaffolding/ui/DataTable.js',
    '/js/jaffolding/ui/Calculator.js',
    '/js/jaffolding/ui/KDEDesktop.js',
    '/js/jaffolding/ui/AppManager.js',
    '/js/jaffolding/ui/BrowserWindow.js',
    '/js/jaffolding/ui/layout/BorderLayout.js',
    '/js/jaffolding/ui/layout/GridLayout.js',
    '/js/jaffolding/demos/MainPage.js',
    '/js/jaffolding/demos/ComponentDemo.js',
    '/js/jaffolding/demos/ChartDemo.js',
    '/js/jaffolding/demos/ThreeDemo.js',
    '/js/jaffolding/demos/SalesDataDemo.js',
    '/js/jaffolding/demos/DesktopDemo.js',
    '/js/jaffolding/demos/AnimationDemo.js',
    '/js/jaffolding/demos/CalculatorApp.js'
  ], initializeApp);
  
  function loadModules(urls, callback) {
    let loaded = 0;
    let errors = 0;
    
    urls.forEach(url => {
      const script = document.createElement('script');
      script.src = url;
      script.onload = () => {
        loaded++;
        console.log(`Loaded module ${loaded}/${urls.length}: ${url}`);
        if (loaded + errors === urls.length) {
          if (errors > 0) {
            console.warn(`Completed loading with ${errors} errors`);
          }
          callback();
        }
      };
      script.onerror = (error) => {
        errors++;
        console.error(`Failed to load module: ${url}`, error);
        if (loaded + errors === urls.length) {
          if (errors > 0) {
            console.warn(`Completed loading with ${errors} errors`);
          }
          callback();
        }
      };
      document.head.appendChild(script);
    });
  }
  
  function initializeApp() {
    // Get the app container
    const appElement = document.getElementById('app');
    
    if (appElement) {
      try {
        console.log("Starting app initialization");
        
        // Clear loading message
        appElement.innerHTML = '<div class="desktop-wallpaper"></div>';
        
        // Check if KDEDesktop is available
        if (!window.jaffolding || !window.jaffolding.KDEDesktop) {
          throw new Error("KDEDesktop component not available");
        }
        
        // Create the desktop environment
        console.log("Creating desktop environment");
        const desktop = new window.jaffolding.KDEDesktop(appElement);
        
        // Check if AppManager is available
        if (!window.jaffolding || !window.jaffolding.AppManager) {
          throw new Error("AppManager component not available");
        }
        
        // Initialize the app manager
        console.log("Initializing app manager");
        const appManager = new window.jaffolding.AppManager(desktop);
        
        // Register apps
        console.log("Registering apps");
        appManager.registerApp({
          id: 'components',
          name: 'UI Components',
          icon: '🧩',
          color: '#5294e2',
          createWindow: () => {
            return appManager.createAppWindow('UI Components', window.jaffolding.demos.ComponentDemo.create(), {
              width: 800,
              height: 600,
              x: 100,
              y: 50
            });
          }
        });
        
        appManager.registerApp({
          id: 'chart',
          name: 'Chart Demo',
          icon: '📊',
          color: '#ea4335',
          createWindow: () => {
            return appManager.createAppWindow('Chart Demo', window.jaffolding.demos.ChartDemo.create(), {
              width: 800,
              height: 600,
              x: 150,
              y: 100
            });
          }
        });
        
        appManager.registerApp({
          id: 'three',
          name: '3D Graphics',
          icon: '🧊',
          color: '#fbbc05',
          createWindow: () => {
            return appManager.createAppWindow('3D Graphics', window.jaffolding.demos.ThreeDemo.create(), {
              width: 800,
              height: 600,
              x: 200,
              y: 150
            });
          }
        });
        
        appManager.registerApp({
          id: 'sales',
          name: 'Sales Dashboard',
          icon: '📈',
          color: '#34a853',
          createWindow: () => {
            return appManager.createBrowserWindow('Sales Dashboard', '/sales', {
              width: 900,
              height: 700,
              x: 250,
              y: 50
            });
          }
        });
        
        appManager.registerApp({
          id: 'animation',
          name: 'Animation Studio',
          icon: '🎬',
          color: '#e91e63',
          createWindow: () => {
            return appManager.createAppWindow('Animation Studio', window.jaffolding.demos.AnimationDemo.create(), {
              width: 800,
              height: 600,
              x: 300,
              y: 100
            });
          }
        });
        
        appManager.registerApp({
          id: 'calculator',
          name: 'Calculator',
          icon: '🧮',
          color: '#673ab7',
          createWindow: () => {
            return appManager.createAppWindow('Calculator', window.jaffolding.demos.CalculatorApp.create(), {
              width: 320,
              height: 480,
              x: 400,
              y: 150
            });
          }
        });
        
        // Create desktop icons
        console.log("Creating desktop icons");
        desktop.createDesktopIcons(appManager.getApps());
        
        // Create dock with pinned apps
        console.log("Creating dock");
        desktop.createDock([
          appManager.getApp('components'),
          appManager.getApp('chart'),
          appManager.getApp('three'),
          appManager.getApp('sales'),
          appManager.getApp('calculator')
        ]);
        
        // Open welcome window
        console.log("Creating welcome window");
        const welcomeContent = new window.jaffolding.Component('div');
        welcomeContent.setStyle('padding', '20px')
                     .setStyle('color', '#eceff4');
        
        const welcomeTitle = new window.jaffolding.Component('h2');
        welcomeTitle.setText('Welcome to Jaffolding Desktop')
                   .setStyle('margin-bottom', '15px');
        
        const welcomeText = new window.jaffolding.Component('p');
        welcomeText.setText('This is a KDE Plasma-inspired desktop environment built with the Jaffolding framework. Click on the desktop icons or dock items to launch applications.')
                  .setStyle('line-height', '1.5')
                  .setStyle('margin-bottom', '20px');
        
        const welcomeList = new window.jaffolding.Component('ul');
        welcomeList.setStyle('margin-bottom', '20px')
                  .setStyle('padding-left', '20px');
        
        const listItems = [
          'UI Components - Explore various UI components',
          'Chart Demo - Interactive data visualization',
          '3D Graphics - Three.js integration',
          'Sales Dashboard - Data dashboard with browser controls',
          'Animation Studio - Animation capabilities',
          'Calculator - Functional calculator application'
        ];
        
        listItems.forEach(item => {
          const li = new window.jaffolding.Component('li');
          li.setText(item)
            .setStyle('margin-bottom', '8px');
          welcomeList.addChild(li);
        });
        
        welcomeContent.addChild(welcomeTitle);
        welcomeContent.addChild(welcomeText);
        welcomeContent.addChild(welcomeList);
        
        const welcomeWindow = appManager.createAppWindow('Welcome', welcomeContent, {
          width: 500,
          height: 400,
          x: 200,
          y: 100
        });
        
        desktop.addWindow(welcomeWindow);
        
        // Update mode indicator
        console.log("Updating mode indicator");
        const modeIndicator = document.getElementById('mode-indicator');
        if (modeIndicator) {
          modeIndicator.textContent = 'Running in: JavaScript Mode';
          
          // Make sure the mode indicator fades out
          setTimeout(() => {
            modeIndicator.style.opacity = '0';
            modeIndicator.style.visibility = 'hidden';
          }, 5000);
        }
        
        console.log("App initialization complete");
      } catch (error) {
        console.error("Error during app initialization:", error);
        appElement.innerHTML = `
          <div class="loading">
            <h2>Error Loading Framework</h2>
            <p>There was an error initializing the Jaffolding framework: ${error.message}</p>
            <p>Please check the console for details.</p>
          </div>
        `;
      }
    }
  }
});