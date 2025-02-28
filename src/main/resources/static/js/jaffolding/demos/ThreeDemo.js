/**
 * Three.js demo for the Jaffolding framework.
 */
(function() {
  // State variables for the demo
  let scene, camera, renderer, cube;
  let isAnimating = true;
  let currentShape = 'cube';
  let currentColor = '#4285f4';
  let rotationSpeed = 0.01;

  /**
   * Creates the Three.js demo page.
   * @returns {Component} - The Three.js demo page
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
    title.setText('Three.js Integration Demo')
         .setStyle('font-size', '24px')
         .setStyle('font-weight', 'bold');
    
    header.addChild(title);
    
    // Content
    const content = new jaffolding.Component('div');
    content.setStyle('padding', '20px');
    
    // Three.js container
    const threeContainer = new jaffolding.Component('div');
    threeContainer.setStyle('width', '100%')
                  .setStyle('height', '400px')
                  .setStyle('background-color', '#f5f5f5')
                  .setStyle('border-radius', '4px')
                  .setStyle('overflow', 'hidden')
                  .setStyle('margin-bottom', '20px');
    
    // Controls
    const controls = createControls();
    
    content.addChild(threeContainer);
    content.addChild(controls);
    
    // Add components to the main panel
    mainPanel.addChild(header);
    mainPanel.addChild(content);
    
    // Initialize Three.js when the container is rendered
    threeContainer.addEventListener('DOMNodeInserted', () => {
      if (threeContainer.getElement()) {
        initThreeJs(threeContainer.getElement());
      }
    });
    
    return mainPanel;
  }

  /**
   * Creates the controls for the Three.js demo.
   * @returns {Component} - The controls component
   */
  function createControls() {
    const controls = new jaffolding.Component('div');
    controls.setStyle('display', 'flex')
            .setStyle('gap', '20px')
            .setStyle('flex-wrap', 'wrap')
            .setStyle('justify-content', 'center');
    
    // Shape selector
    const shapePanel = new jaffolding.Component('div');
    shapePanel.setStyle('display', 'flex')
              .setStyle('flex-direction', 'column')
              .setStyle('gap', '8px');
    
    const shapeLabel = new jaffolding.Component('label');
    shapeLabel.setText('Shape:');
    
    const shapeSelector = new jaffolding.Component('select');
    shapeSelector.setStyle('padding', '8px')
                 .setStyle('border-radius', '4px')
                 .setStyle('border', '1px solid #ddd');
    
    const shapes = ['cube', 'sphere', 'torus'];
    
    shapes.forEach(shape => {
      const option = new jaffolding.Component('option');
      option.setText(shape);
      option.setAttribute('value', shape);
      shapeSelector.addChild(option);
    });
    
    shapeSelector.addEventListener('change', e => {
      currentShape = e.target.value;
      updateShape();
    });
    
    shapePanel.addChild(shapeLabel);
    shapePanel.addChild(shapeSelector);
    
    // Color selector
    const colorPanel = new jaffolding.Component('div');
    colorPanel.setStyle('display', 'flex')
              .setStyle('flex-direction', 'column')
              .setStyle('gap', '8px');
    
    const colorLabel = new jaffolding.Component('label');
    colorLabel.setText('Color:');
    
    const colorSelector = new jaffolding.Component('select');
    colorSelector.setStyle('padding', '8px')
                 .setStyle('border-radius', '4px')
                 .setStyle('border', '1px solid #ddd');
    
    const colors = [
      { name: 'Blue', value: '#4285f4' },
      { name: 'Red', value: '#ea4335' },
      { name: 'Green', value: '#34a853' },
      { name: 'Yellow', value: '#fbbc05' }
    ];
    
    colors.forEach(color => {
      const option = new jaffolding.Component('option');
      option.setText(color.name);
      option.setAttribute('value', color.value);
      colorSelector.addChild(option);
    });
    
    colorSelector.addEventListener('change', e => {
      currentColor = e.target.value;
      updateShape();
    });
    
    colorPanel.addChild(colorLabel);
    colorPanel.addChild(colorSelector);
    
    // Animation control
    const animationPanel = new jaffolding.Component('div');
    animationPanel.setStyle('display', 'flex')
                  .setStyle('flex-direction', 'column')
                  .setStyle('gap', '8px');
    
    const animationLabel = new jaffolding.Component('label');
    animationLabel.setText('Animation:');
    
    const animationButton = new jaffolding.Button('Pause');
    animationButton.addEventListener('click', () => {
      isAnimating = !isAnimating;
      animationButton.setText(isAnimating ? 'Pause' : 'Resume');
    });
    
    animationPanel.addChild(animationLabel);
    animationPanel.addChild(animationButton);
    
    // Speed control
    const speedPanel = new jaffolding.Component('div');
    speedPanel.setStyle('display', 'flex')
              .setStyle('flex-direction', 'column')
              .setStyle('gap', '8px');
    
    const speedLabel = new jaffolding.Component('label');
    speedLabel.setText('Speed:');
    
    const speedSelector = new jaffolding.Component('select');
    speedSelector.setStyle('padding', '8px')
                 .setStyle('border-radius', '4px')
                 .setStyle('border', '1px solid #ddd');
    
    const speeds = [
      { name: 'Slow', value: 0.005 },
      { name: 'Medium', value: 0.01 },
      { name: 'Fast', value: 0.02 }
    ];
    
    speeds.forEach(speed => {
      const option = new jaffolding.Component('option');
      option.setText(speed.name);
      option.setAttribute('value', speed.value);
      if (speed.value === rotationSpeed) {
        option.setAttribute('selected', 'selected');
      }
      speedSelector.addChild(option);
    });
    
    speedSelector.addEventListener('change', e => {
      rotationSpeed = parseFloat(e.target.value);
    });
    
    speedPanel.addChild(speedLabel);
    speedPanel.addChild(speedSelector);
    
    // Add all controls
    controls.addChild(shapePanel);
    controls.addChild(colorPanel);
    controls.addChild(animationPanel);
    controls.addChild(speedPanel);
    
    return controls;
  }

  /**
   * Initializes the Three.js scene.
   * @param {HTMLElement} container - The container element
   */
  function initThreeJs(container) {
    if (!window.THREE) {
      console.error('Three.js is not available');
      return;
    }
    
    // Create scene
    scene = new THREE.Scene();
    
    // Create camera
    const aspect = container.clientWidth / container.clientHeight;
    camera = new THREE.PerspectiveCamera(75, aspect, 0.1, 1000);
    camera.position.z = 5;
    
    // Create renderer
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(container.clientWidth, container.clientHeight);
    container.appendChild(renderer.domElement);
    
    // Add lights
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
    scene.add(ambientLight);
    
    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.position.set(1, 1, 1);
    scene.add(directionalLight);
    
    // Create initial shape
    createShape();
    
    // Handle window resize
    window.addEventListener('resize', () => {
      if (container.clientWidth > 0 && container.clientHeight > 0) {
        camera.aspect = container.clientWidth / container.clientHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(container.clientWidth, container.clientHeight);
      }
    });
    
    // Start animation loop
    animate();
  }

  /**
   * Creates a shape based on the current settings.
   */
  function createShape() {
    // Remove current shape if it exists
    if (cube) {
      scene.remove(cube);
    }
    
    // Create material
    const material = new THREE.MeshPhongMaterial({ color: currentColor });
    
    // Create geometry based on selected shape
    let geometry;
    
    switch (currentShape) {
      case 'sphere':
        geometry = new THREE.SphereGeometry(1, 32, 32);
        break;
      case 'torus':
        geometry = new THREE.TorusGeometry(0.7, 0.3, 16, 32);
        break;
      case 'cube':
      default:
        geometry = new THREE.BoxGeometry(1, 1, 1);
        break;
    }
    
    // Create mesh
    cube = new THREE.Mesh(geometry, material);
    scene.add(cube);
  }

  /**
   * Updates the shape based on current settings.
   */
  function updateShape() {
    createShape();
  }

  /**
   * Animation loop.
   */
  function animate() {
    requestAnimationFrame(animate);
    
    // Rotate the shape if animation is enabled
    if (isAnimating && cube) {
      cube.rotation.x += rotationSpeed;
      cube.rotation.y += rotationSpeed;
    }
    
    // Render the scene
    if (renderer && scene && camera) {
      renderer.render(scene, camera);
    }
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.ThreeDemo = {
    create: create
  };
})();