/**
 * Animation demo for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the animation demo page.
   * @returns {Component} - The animation demo page
   */
  function create() {
    const mainPanel = new jaffolding.Component('div');
    
    // Header
    const header = new jaffolding.Component('div');
    header.setStyle('background-color', '#4285f4')
          .setStyle('color', 'white')
          .setStyle('padding', '20px')
          .setStyle('text-align', 'center');
    
    const title = new jaffolding.Component('h1');
    title.setText('Animation Demo')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    const intro = new jaffolding.Component('p');
    intro.setText('This demo showcases the animation capabilities of the Jaffolding framework.')
         .setStyle('font-size', '16px')
         .setStyle('margin-bottom', '20px');
    
    // Animation demo grid
    const demoGrid = new jaffolding.Component('div');
    demoGrid.setStyle('display', 'grid')
            .setStyle('grid-template-columns', 'repeat(2, 1fr)')
            .setStyle('grid-template-rows', 'repeat(3, auto)')
            .setStyle('gap', '20px');
    
    // Fade animations
    const fadePanel = createDemoPanel('Fade Animations');
    fadePanel.addChild(createFadeDemo());
    
    // Slide animations
    const slidePanel = createDemoPanel('Slide Animations');
    slidePanel.addChild(createSlideDemo());
    
    // Scale animations
    const scalePanel = createDemoPanel('Scale Animations');
    scalePanel.addChild(createScaleDemo());
    
    // Rotate animations
    const rotatePanel = createDemoPanel('Rotate Animations');
    rotatePanel.addChild(createRotateDemo());
    
    // Combined animations
    const combinedPanel = createDemoPanel('Combined Animations');
    combinedPanel.addChild(createCombinedDemo());
    
    // GSAP integration
    const gsapPanel = createDemoPanel('GSAP Integration');
    gsapPanel.addChild(createGsapDemo());
    
    // Add all panels to the grid
    demoGrid.addChild(fadePanel);
    demoGrid.addChild(slidePanel);
    demoGrid.addChild(scalePanel);
    demoGrid.addChild(rotatePanel);
    demoGrid.addChild(combinedPanel);
    demoGrid.addChild(gsapPanel);
    
    content.addChild(intro);
    content.addChild(demoGrid);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(content);
    
    // Apply entrance animation to the whole demo
    mainPanel.addEventListener('DOMNodeInserted', () => {
      if (mainPanel.getElement()) {
        jaffolding.Animation.fadeIn(header, 0.5);
        jaffolding.Animation.slideIn(content, 'bottom', 0.5);
      }
    });
    
    return mainPanel;
  }

  /**
   * Creates a demo panel with a title.
   * @param {string} title - The panel title
   * @returns {Component} - The demo panel component
   */
  function createDemoPanel(title) {
    const panel = new jaffolding.Component('div');
    panel.setStyle('background-color', 'white')
         .setStyle('border-radius', '4px')
         .setStyle('box-shadow', '0 2px 4px rgba(0, 0, 0, 0.1)')
         .setStyle('padding', '15px');
    
    const titleLabel = new jaffolding.Component('h3');
    titleLabel.setText(title)
              .setStyle('font-size', '16px')
              .setStyle('font-weight', 'bold')
              .setStyle('margin-bottom', '15px');
    
    panel.addChild(titleLabel);
    
    return panel;
  }

  /**
   * Creates a fade animation demo.
   * @returns {Component} - The fade demo component
   */
  function createFadeDemo() {
    const container = new jaffolding.Component('div');
    
    const fadeTarget = new jaffolding.Component('div');
    fadeTarget.setStyle('width', '100%')
              .setStyle('height', '100px')
              .setStyle('background-color', '#4285f4')
              .setStyle('border-radius', '4px')
              .setStyle('display', 'flex')
              .setStyle('align-items', 'center')
              .setStyle('justify-content', 'center')
              .setStyle('color', 'white')
              .setStyle('font-weight', 'bold');
    
    const fadeLabel = new jaffolding.Component('span');
    fadeLabel.setText('Fade Target');
    fadeTarget.addChild(fadeLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('margin-top', '10px');
    
    const fadeInBtn = new jaffolding.Button('Fade In');
    fadeInBtn.addEventListener('click', () => {
      jaffolding.Animation.fadeIn(fadeTarget, 0.5);
    });
    
    const fadeOutBtn = new jaffolding.Button('Fade Out');
    fadeOutBtn.addEventListener('click', () => {
      jaffolding.Animation.fadeOut(fadeTarget, 0.5);
    });
    
    controls.addChild(fadeInBtn);
    controls.addChild(fadeOutBtn);
    
    container.addChild(fadeTarget);
    container.addChild(controls);
    
    return container;
  }

  /**
   * Creates a slide animation demo.
   * @returns {Component} - The slide demo component
   */
  function createSlideDemo() {
    const container = new jaffolding.Component('div');
    
    const slideTarget = new jaffolding.Component('div');
    slideTarget.setStyle('width', '100%')
               .setStyle('height', '100px')
               .setStyle('background-color', '#ea4335')
               .setStyle('border-radius', '4px')
               .setStyle('display', 'flex')
               .setStyle('align-items', 'center')
               .setStyle('justify-content', 'center')
               .setStyle('color', 'white')
               .setStyle('font-weight', 'bold');
    
    const slideLabel = new jaffolding.Component('span');
    slideLabel.setText('Slide Target');
    slideTarget.addChild(slideLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('margin-top', '10px');
    
    const directionRow = new jaffolding.Component('div');
    directionRow.setStyle('display', 'flex')
                .setStyle('gap', '10px')
                .setStyle('margin-bottom', '10px')
                .setStyle('align-items', 'center');
    
    const directionLabel = new jaffolding.Component('label');
    directionLabel.setText('Direction:');
    
    const directionSelect = new jaffolding.Component('select');
    directionSelect.setStyle('padding', '8px')
                   .setStyle('border-radius', '4px')
                   .setStyle('border', '1px solid #ddd');
    
    const directions = ['left', 'right', 'top', 'bottom'];
    directions.forEach(direction => {
      const option = new jaffolding.Component('option');
      option.setText(direction);
      option.setAttribute('value', direction);
      directionSelect.addChild(option);
    });
    
    directionRow.addChild(directionLabel);
    directionRow.addChild(directionSelect);
    
    const buttonRow = new jaffolding.Component('div');
    buttonRow.setStyle('display', 'flex')
             .setStyle('gap', '10px');
    
    const slideInBtn = new jaffolding.Button('Slide In');
    slideInBtn.addEventListener('click', () => {
      const direction = directionSelect.getElement().value;
      jaffolding.Animation.slideIn(slideTarget, direction, 0.5);
    });
    
    const slideOutBtn = new jaffolding.Button('Slide Out');
    slideOutBtn.addEventListener('click', () => {
      const direction = directionSelect.getElement().value;
      jaffolding.Animation.slideOut(slideTarget, direction, 0.5);
    });
    
    buttonRow.addChild(slideInBtn);
    buttonRow.addChild(slideOutBtn);
    
    controls.addChild(directionRow);
    controls.addChild(buttonRow);
    
    container.addChild(slideTarget);
    container.addChild(controls);
    
    return container;
  }

  /**
   * Creates a scale animation demo.
   * @returns {Component} - The scale demo component
   */
  function createScaleDemo() {
    const container = new jaffolding.Component('div');
    
    const scaleTarget = new jaffolding.Component('div');
    scaleTarget.setStyle('width', '100px')
               .setStyle('height', '100px')
               .setStyle('background-color', '#fbbc05')
               .setStyle('border-radius', '4px')
               .setStyle('display', 'flex')
               .setStyle('align-items', 'center')
               .setStyle('justify-content', 'center')
               .setStyle('color', 'white')
               .setStyle('font-weight', 'bold')
               .setStyle('margin', '0 auto');
    
    const scaleLabel = new jaffolding.Component('span');
    scaleLabel.setText('Scale Target');
    scaleTarget.addChild(scaleLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('margin-top', '10px')
            .setStyle('justify-content', 'center');
    
    const scaleUpBtn = new jaffolding.Button('Scale Up');
    scaleUpBtn.addEventListener('click', () => {
      jaffolding.Animation.scale(scaleTarget, 1, 1.5, 0.5);
    });
    
    const scaleDownBtn = new jaffolding.Button('Scale Down');
    scaleDownBtn.addEventListener('click', () => {
      jaffolding.Animation.scale(scaleTarget, 1.5, 1, 0.5);
    });
    
    controls.addChild(scaleUpBtn);
    controls.addChild(scaleDownBtn);
    
    container.addChild(scaleTarget);
    container.addChild(controls);
    
    return container;
  }

  /**
   * Creates a rotate animation demo.
   * @returns {Component} - The rotate demo component
   */
  function createRotateDemo() {
    const container = new jaffolding.Component('div');
    
    const rotateTarget = new jaffolding.Component('div');
    rotateTarget.setStyle('width', '100px')
                .setStyle('height', '100px')
                .setStyle('background-color', '#34a853')
                .setStyle('border-radius', '4px')
                .setStyle('display', 'flex')
                .setStyle('align-items', 'center')
                .setStyle('justify-content', 'center')
                .setStyle('color', 'white')
                .setStyle('font-weight', 'bold')
                .setStyle('margin', '0 auto');
    
    const rotateLabel = new jaffolding.Component('span');
    rotateLabel.setText('Rotate Target');
    rotateTarget.addChild(rotateLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('margin-top', '10px')
            .setStyle('justify-content', 'center');
    
    const rotateCWBtn = new jaffolding.Button('Rotate CW');
    rotateCWBtn.addEventListener('click', () => {
      jaffolding.Animation.rotate(rotateTarget, 0, 360, 1);
    });
    
    const rotateCCWBtn = new jaffolding.Button('Rotate CCW');
    rotateCCWBtn.addEventListener('click', () => {
      jaffolding.Animation.rotate(rotateTarget, 0, -360, 1);
    });
    
    controls.addChild(rotateCWBtn);
    controls.addChild(rotateCCWBtn);
    
    container.addChild(rotateTarget);
    container.addChild(controls);
    
    return container;
  }

  /**
   * Creates a combined animation demo.
   * @returns {Component} - The combined demo component
   */
  function createCombinedDemo() {
    const container = new jaffolding.Component('div');
    
    const combinedTarget = new jaffolding.Component('div');
    combinedTarget.setStyle('width', '100px')
                  .setStyle('height', '100px')
                  .setStyle('background-color', '#673ab7')
                  .setStyle('border-radius', '4px')
                  .setStyle('display', 'flex')
                  .setStyle('align-items', 'center')
                  .setStyle('justify-content', 'center')
                  .setStyle('color', 'white')
                  .setStyle('font-weight', 'bold')
                  .setStyle('margin', '0 auto');
    
    const combinedLabel = new jaffolding.Component('span');
    combinedLabel.setText('Combined');
    combinedTarget.addChild(combinedLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('margin-top', '10px')
            .setStyle('justify-content', 'center');
    
    const entranceBtn = new jaffolding.Button('Entrance');
    entranceBtn.addEventListener('click', () => {
      combinedTarget.setStyle('opacity', '0');
      combinedTarget.setStyle('transform', 'scale(0.5) translateY(20px)');
      
      setTimeout(() => {
        combinedTarget.setStyle('transition', 'opacity 0.5s ease, transform 0.5s ease');
        combinedTarget.setStyle('opacity', '1');
        combinedTarget.setStyle('transform', 'scale(1) translateY(0)');
      }, 10);
    });
    
    const bounceBtn = new jaffolding.Button('Bounce');
    bounceBtn.addEventListener('click', () => {
      combinedTarget.setStyle('transition', 'transform 0.2s ease');
      combinedTarget.setStyle('transform', 'scale(0.8) translateY(0)');
      
      setTimeout(() => {
        combinedTarget.setStyle('transition', 'transform 0.2s ease');
        combinedTarget.setStyle('transform', 'scale(1.1) translateY(-10px)');
        
        setTimeout(() => {
          combinedTarget.setStyle('transition', 'transform 0.2s ease');
          combinedTarget.setStyle('transform', 'scale(0.9) translateY(5px)');
          
          setTimeout(() => {
            combinedTarget.setStyle('transition', 'transform 0.2s ease');
            combinedTarget.setStyle('transform', 'scale(1) translateY(0)');
          }, 200);
        }, 200);
      }, 200);
    });
    
    controls.addChild(entranceBtn);
    controls.addChild(bounceBtn);
    
    container.addChild(combinedTarget);
    container.addChild(controls);
    
    return container;
  }

  /**
   * Creates a GSAP integration demo.
   * @returns {Component} - The GSAP demo component
   */
  function createGsapDemo() {
    const container = new jaffolding.Component('div');
    
    const gsapTarget = new jaffolding.Component('div');
    gsapTarget.setStyle('width', '100px')
              .setStyle('height', '100px')
              .setStyle('background-color', '#e91e63')
              .setStyle('border-radius', '4px')
              .setStyle('display', 'flex')
              .setStyle('align-items', 'center')
              .setStyle('justify-content', 'center')
              .setStyle('color', 'white')
              .setStyle('font-weight', 'bold')
              .setStyle('margin', '0 auto');
    
    const gsapLabel = new jaffolding.Component('span');
    gsapLabel.setText('GSAP Target');
    gsapTarget.addChild(gsapLabel);
    
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '10px')
            .setStyle('margin-top', '10px')
            .setStyle('justify-content', 'center');
    
    const timelineBtn = new jaffolding.Button('Timeline');
    timelineBtn.addEventListener('click', () => {
      // This will use GSAP if available, otherwise fall back to CSS transitions
      jaffolding.Animation.animate(gsapTarget, 'x', 100, 0.5);
      setTimeout(() => {
        jaffolding.Animation.animate(gsapTarget, 'y', 50, 0.5);
        setTimeout(() => {
          jaffolding.Animation.animate(gsapTarget, 'rotation', 360, 0.5);
          setTimeout(() => {
            jaffolding.Animation.animate(gsapTarget, 'x', 0, 0.5);
            jaffolding.Animation.animate(gsapTarget, 'y', 0, 0.5);
            jaffolding.Animation.animate(gsapTarget, 'rotation', 0, 0.5);
          }, 600);
        }, 600);
      }, 600);
    });
    
    const shakeBtn = new jaffolding.Button('Shake');
    shakeBtn.addEventListener('click', () => {
      gsapTarget.setStyle('transition', 'transform 0.1s ease');
      gsapTarget.setStyle('transform', 'translateX(-10px)');
      
      setTimeout(() => {
        gsapTarget.setStyle('transform', 'translateX(10px)');
        setTimeout(() => {
          gsapTarget.setStyle('transform', 'translateX(-8px)');
          setTimeout(() => {
            gsapTarget.setStyle('transform', 'translateX(8px)');
            setTimeout(() => {
              gsapTarget.setStyle('transform', 'translateX(-5px)');
              setTimeout(() => {
                gsapTarget.setStyle('transform', 'translateX(5px)');
                setTimeout(() => {
                  gsapTarget.setStyle('transform', 'translateX(0)');
                }, 100);
              }, 100);
            }, 100);
          }, 100);
        }, 100);
      }, 100);
    });
    
    controls.addChild(timelineBtn);
    controls.addChild(shakeBtn);
    
    container.addChild(gsapTarget);
    container.addChild(controls);
    
    return container;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.AnimationDemo = {
    create: create
  };
})();