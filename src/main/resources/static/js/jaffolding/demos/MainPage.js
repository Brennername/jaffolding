/**
 * Main page demo for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the main page.
   * @param {Router} router - The router instance
   * @returns {Component} - The main page component
   */
  function create(router) {
    const mainPanel = new jaffolding.Component('div');
    
    // Header
    const header = new jaffolding.Component('div');
    header.setStyle('background-color', '#4285f4')
          .setStyle('color', 'white')
          .setStyle('padding', '20px')
          .setStyle('text-align', 'center');
    
    const title = new jaffolding.Component('h1');
    title.setText('Jaffolding Framework')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Navigation
    const nav = new jaffolding.Component('div');
    nav.setStyle('display', 'grid')
       .setStyle('grid-template-columns', 'repeat(3, 1fr)')
       .setStyle('grid-template-rows', 'repeat(2, auto)')
       .setStyle('gap', '20px')
       .setStyle('padding', '20px');
    
    // Add navigation cards
    nav.addChild(createNavCard('UI Components', 'Explore the various UI components available in the framework.', '#4285f4', () => router.navigateTo('/components')));
    nav.addChild(createNavCard('Chart.js Integration', 'See how the framework integrates with Chart.js for data visualization.', '#ea4335', () => router.navigateTo('/chart')));
    nav.addChild(createNavCard('Three.js Integration', 'Explore 3D graphics capabilities with Three.js integration.', '#fbbc05', () => router.navigateTo('/three')));
    nav.addChild(createNavCard('Sales Data Visualization', 'Interactive data visualization with reactive charts and tables.', '#34a853', () => router.navigateTo('/sales')));
    nav.addChild(createNavCard('Desktop Environment', 'Draggable, resizable windows with a desktop-like interface.', '#673ab7', () => router.navigateTo('/desktop')));
    nav.addChild(createNavCard('Animation Demo', 'Explore the animation capabilities of the framework.', '#e91e63', () => router.navigateTo('/animation')));
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    const intro = new jaffolding.Component('h2');
    intro.setText('Welcome to Jaffolding - A Java UI Framework for the Web')
         .setStyle('font-size', '18px')
         .setStyle('margin-bottom', '20px');
    
    const description = new jaffolding.Component('p');
    description.setText(
      'Jaffolding is a Java framework that compiles to JavaScript using TeaVM. ' +
      'It provides a familiar component-based API for Java developers to build web applications ' +
      'without writing JavaScript. It also includes wrappers for popular JavaScript libraries.'
    )
    .setStyle('line-height', '1.5');
    
    const jsNote = new jaffolding.Component('p');
    jsNote.setText(
      'You are currently using the JavaScript implementation of Jaffolding. ' +
      'This provides the same API as the Java version, allowing you to use either language interchangeably.'
    )
    .setStyle('margin-top', '20px')
    .setStyle('padding', '10px')
    .setStyle('background-color', '#f5f5f5')
    .setStyle('border-left', '4px solid #4285f4');
    
    content.addChild(intro);
    content.addChild(description);
    content.addChild(jsNote);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(nav);
    mainPanel.addChild(content);
    
    return mainPanel;
  }

  /**
   * Creates a navigation card.
   * @param {string} title - The card title
   * @param {string} description - The card description
   * @param {string} color - The card accent color
   * @param {Function} onClick - The click handler
   * @returns {Component} - The card component
   */
  function createNavCard(title, description, color, onClick) {
    const card = new jaffolding.Component('div');
    card.setStyle('background-color', 'white')
        .setStyle('border-radius', '8px')
        .setStyle('box-shadow', '0 2px 8px rgba(0, 0, 0, 0.1)')
        .setStyle('padding', '20px')
        .setStyle('cursor', 'pointer')
        .setStyle('transition', 'transform 0.2s, box-shadow 0.2s')
        .addEventListener('mouseover', () => {
          card.setStyle('transform', 'translateY(-5px)');
          card.setStyle('box-shadow', '0 8px 16px rgba(0, 0, 0, 0.1)');
        })
        .addEventListener('mouseout', () => {
          card.setStyle('transform', 'translateY(0)');
          card.setStyle('box-shadow', '0 2px 8px rgba(0, 0, 0, 0.1)');
        })
        .addEventListener('click', onClick);
    
    const iconCircle = new jaffolding.Component('div');
    iconCircle.setStyle('width', '50px')
              .setStyle('height', '50px')
              .setStyle('border-radius', '25px')
              .setStyle('background-color', color)
              .setStyle('margin-bottom', '15px')
              .setStyle('display', 'flex')
              .setStyle('align-items', 'center')
              .setStyle('justify-content', 'center')
              .setStyle('color', 'white')
              .setStyle('font-weight', 'bold')
              .setStyle('font-size', '20px');
    
    const iconLabel = new jaffolding.Component('span');
    iconLabel.setText(title.substring(0, 1));
    iconCircle.addChild(iconLabel);
    
    const titleLabel = new jaffolding.Component('h3');
    titleLabel.setText(title)
              .setStyle('font-size', '18px')
              .setStyle('font-weight', 'bold')
              .setStyle('margin-bottom', '10px');
    
    const descLabel = new jaffolding.Component('p');
    descLabel.setText(description)
             .setStyle('color', '#666')
             .setStyle('font-size', '14px')
             .setStyle('line-height', '1.4');
    
    card.addChild(iconCircle);
    card.addChild(titleLabel);
    card.addChild(descLabel);
    
    return card;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.MainPage = {
    create: create
  };
})();