/**
 * Calculator application for the Jaffolding framework.
 */
(function() {
  /**
   * Creates the calculator application.
   * @returns {Component} - The calculator component
   */
  function create() {
    // Create a calculator component
    const calculator = new jaffolding.Calculator();
    
    // Style it for the app window
    calculator.setStyle('width', '100%')
              .setStyle('height', '100%')
              .setStyle('background-color', '#2e3440')
              .setStyle('border-radius', '0')
              .setStyle('overflow', 'hidden');
    
    return calculator;
  }

  // Export the module
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.demos = window.jaffolding.demos || {};
  window.jaffolding.demos.CalculatorApp = {
    create: create
  };
})();