/**
 * A calculator component for the Jaffolding framework.
 */
(function() {
  class Calculator extends jaffolding.Component {
    /**
     * Creates a new Calculator instance.
     */
    constructor() {
      super('div');
      this.display = '';
      this.currentValue = null;
      this.operator = null;
      this.waitingForOperand = false;
      this.memory = 0;
      
      this.initializeCalculator();
    }

    /**
     * Initializes the calculator structure and behavior.
     * @private
     */
    initializeCalculator() {
      this.setStyle('display', 'flex')
          .setStyle('flex-direction', 'column')
          .setStyle('width', '100%')
          .setStyle('height', '100%')
          .setStyle('background-color', '#2e3440')
          .setStyle('border-radius', '4px')
          .setStyle('overflow', 'hidden')
          .setStyle('color', '#eceff4');
      
      // Create display
      const displayContainer = new jaffolding.Component('div');
      displayContainer.setStyle('background-color', '#3b4252')
                      .setStyle('padding', '15px')
                      .setStyle('margin', '10px')
                      .setStyle('border-radius', '4px')
                      .setStyle('box-shadow', 'inset 0 0 5px rgba(0, 0, 0, 0.2)')
                      .setStyle('text-align', 'right')
                      .setStyle('font-family', 'monospace')
                      .setStyle('font-size', '24px')
                      .setStyle('height', '60px')
                      .setStyle('display', 'flex')
                      .setStyle('align-items', 'center')
                      .setStyle('justify-content', 'flex-end')
                      .setStyle('color', '#eceff4');
      
      this.displayElement = new jaffolding.Component('div');
      this.displayElement.setText('0');
      displayContainer.addChild(this.displayElement);
      
      // Create keypad
      const keypad = new jaffolding.Component('div');
      keypad.setStyle('display', 'grid')
            .setStyle('grid-template-columns', 'repeat(4, 1fr)')
            .setStyle('grid-gap', '10px')
            .setStyle('padding', '10px')
            .setStyle('flex', '1');
      
      // Define button layout
      const buttons = [
        { text: 'MC', type: 'memory', action: () => this.memoryClear() },
        { text: 'MR', type: 'memory', action: () => this.memoryRecall() },
        { text: 'M+', type: 'memory', action: () => this.memoryAdd() },
        { text: 'M-', type: 'memory', action: () => this.memorySubtract() },
        
        { text: 'C', type: 'clear', action: () => this.clear() },
        { text: 'CE', type: 'clear', action: () => this.clearEntry() },
        { text: '±', type: 'unary', action: () => this.negate() },
        { text: '÷', type: 'operator', action: () => this.setOperator('/') },
        
        { text: '7', type: 'digit', action: () => this.appendDigit('7') },
        { text: '8', type: 'digit', action: () => this.appendDigit('8') },
        { text: '9', type: 'digit', action: () => this.appendDigit('9') },
        { text: '×', type: 'operator', action: () => this.setOperator('*') },
        
        { text: '4', type: 'digit', action: () => this.appendDigit('4') },
        { text: '5', type: 'digit', action: () => this.appendDigit('5') },
        { text: '6', type: 'digit', action: () => this.appendDigit('6') },
        { text: '-', type: 'operator', action: () => this.setOperator('-') },
        
        { text: '1', type: 'digit', action: () => this.appendDigit('1') },
        { text: '2', type: 'digit', action: () => this.appendDigit('2') },
        { text: '3', type: 'digit', action: () => this.appendDigit('3') },
        { text: '+', type: 'operator', action: () => this.setOperator('+') },
        
        { text: '0', type: 'digit', action: () => this.appendDigit('0'), span: 2 },
        { text: '.', type: 'digit', action: () => this.appendDecimal() },
        { text: '=', type: 'equals', action: () => this.calculate() }
      ];
      
      // Create buttons
      buttons.forEach(button => {
        const btn = this.createButton(button.text, button.type);
        btn.addEventListener('click', button.action);
        
        if (button.span) {
          btn.setStyle('grid-column', `span ${button.span}`);
        }
        
        keypad.addChild(btn);
      });
      
      // Add components to calculator
      this.addChild(displayContainer);
      this.addChild(keypad);
      
      // Set up keyboard support
      document.addEventListener('keydown', (e) => {
        if (!this.getElement()) return;
        
        // Only process keys if calculator is visible and in focus
        const calcElement = this.getElement();
        if (!calcElement.offsetParent) return;
        
        const key = e.key;
        
        // Handle digits
        if (/^[0-9]$/.test(key)) {
          this.appendDigit(key);
          e.preventDefault();
        }
        
        // Handle operators
        switch (key) {
          case '+':
          case '-':
            this.setOperator(key);
            e.preventDefault();
            break;
          case '*':
          case 'x':
          case 'X':
            this.setOperator('*');
            e.preventDefault();
            break;
          case '/':
            this.setOperator('/');
            e.preventDefault();
            break;
          case '=':
          case 'Enter':
            this.calculate();
            e.preventDefault();
            break;
          case '.':
          case ',':
            this.appendDecimal();
            e.preventDefault();
            break;
          case 'Escape':
            this.clear();
            e.preventDefault();
            break;
          case 'Backspace':
            this.clearEntry();
            e.preventDefault();
            break;
        }
      });
    }

    /**
     * Creates a calculator button.
     * @param {string} text - The button text
     * @param {string} type - The button type
     * @returns {Component} - The button component
     * @private
     */
    createButton(text, type) {
      const button = new jaffolding.Component('button');
      button.setText(text)
            .setStyle('padding', '10px')
            .setStyle('border', 'none')
            .setStyle('border-radius', '4px')
            .setStyle('font-size', '18px')
            .setStyle('cursor', 'pointer')
            .setStyle('transition', 'background-color 0.2s, transform 0.1s')
            .addEventListener('mousedown', () => {
              button.setStyle('transform', 'scale(0.95)');
            })
            .addEventListener('mouseup', () => {
              button.setStyle('transform', 'scale(1)');
            })
            .addEventListener('mouseleave', () => {
              button.setStyle('transform', 'scale(1)');
            });
      
      // Style based on button type
      switch (type) {
        case 'digit':
          button.setStyle('background-color', '#4c566a')
                .setStyle('color', '#eceff4');
          break;
        case 'operator':
          button.setStyle('background-color', '#5e81ac')
                .setStyle('color', '#eceff4');
          break;
        case 'equals':
          button.setStyle('background-color', '#88c0d0')
                .setStyle('color', '#2e3440');
          break;
        case 'clear':
          button.setStyle('background-color', '#bf616a')
                .setStyle('color', '#eceff4');
          break;
        case 'memory':
          button.setStyle('background-color', '#a3be8c')
                .setStyle('color', '#2e3440');
          break;
        case 'unary':
          button.setStyle('background-color', '#ebcb8b')
                .setStyle('color', '#2e3440');
          break;
        default:
          button.setStyle('background-color', '#434c5e')
                .setStyle('color', '#eceff4');
      }
      
      // Add hover effect
      button.addEventListener('mouseover', () => {
        const currentBg = button.getElement().style.backgroundColor;
        // Lighten the background color
        button.setStyle('background-color', lightenColor(currentBg, 10));
      });
      
      button.addEventListener('mouseout', () => {
        // Reset to original color based on type
        switch (type) {
          case 'digit':
            button.setStyle('background-color', '#4c566a');
            break;
          case 'operator':
            button.setStyle('background-color', '#5e81ac');
            break;
          case 'equals':
            button.setStyle('background-color', '#88c0d0');
            break;
          case 'clear':
            button.setStyle('background-color', '#bf616a');
            break;
          case 'memory':
            button.setStyle('background-color', '#a3be8c');
            break;
          case 'unary':
            button.setStyle('background-color', '#ebcb8b');
            break;
          default:
            button.setStyle('background-color', '#434c5e');
        }
      });
      
      return button;
    }

    /**
     * Updates the calculator display.
     * @param {string} text - The text to display
     * @private
     */
    updateDisplay(text) {
      this.display = text;
      this.displayElement.setText(text);
    }

    /**
     * Appends a digit to the display.
     * @param {string} digit - The digit to append
     */
    appendDigit(digit) {
      if (this.waitingForOperand) {
        this.display = '';
        this.waitingForOperand = false;
      }
      
      // Replace initial 0 unless adding a decimal point
      if (this.display === '0' && digit !== '.') {
        this.display = '';
      }
      
      // Limit display length to prevent overflow
      if (this.display.length < 12) {
        this.display += digit;
        this.updateDisplay(this.display);
      }
    }

    /**
     * Appends a decimal point to the display.
     */
    appendDecimal() {
      if (this.waitingForOperand) {
        this.display = '0';
        this.waitingForOperand = false;
      }
      
      // Add decimal point if not already present
      if (this.display.indexOf('.') === -1) {
        this.display += '.';
        this.updateDisplay(this.display);
      }
    }

    /**
     * Sets the operator for the calculation.
     * @param {string} op - The operator
     */
    setOperator(op) {
      const inputValue = parseFloat(this.display);
      
      // If we already have a pending operation, calculate it first
      if (this.operator && !this.waitingForOperand) {
        this.calculate();
      } else if (this.currentValue === null) {
        this.currentValue = inputValue;
      }
      
      this.operator = op;
      this.waitingForOperand = true;
    }

    /**
     * Performs the calculation.
     */
    calculate() {
      if (this.operator === null) return;
      
      const inputValue = parseFloat(this.display);
      let result = 0;
      
      switch (this.operator) {
        case '+':
          result = this.currentValue + inputValue;
          break;
        case '-':
          result = this.currentValue - inputValue;
          break;
        case '*':
          result = this.currentValue * inputValue;
          break;
        case '/':
          if (inputValue !== 0) {
            result = this.currentValue / inputValue;
          } else {
            this.updateDisplay('Error');
            this.currentValue = null;
            this.operator = null;
            this.waitingForOperand = true;
            return;
          }
          break;
      }
      
      // Format the result
      result = this.formatResult(result);
      
      // Update display and state
      this.updateDisplay(result.toString());
      this.currentValue = result;
      this.operator = null;
      this.waitingForOperand = true;
    }

    /**
     * Formats the calculation result.
     * @param {number} result - The result to format
     * @returns {number|string} - The formatted result
     * @private
     */
    formatResult(result) {
      // Handle very large or small numbers
      if (Math.abs(result) > 1e12 || (Math.abs(result) < 1e-12 && result !== 0)) {
        return result.toExponential(6);
      }
      
      // Convert to string and check length
      const resultStr = result.toString();
      
      // If the result is too long, round it
      if (resultStr.length > 12) {
        if (resultStr.includes('e')) {
          // Already in exponential form
          return result;
        } else if (resultStr.includes('.')) {
          // Decimal number, round to fit
          const decimalPos = resultStr.indexOf('.');
          const digitsBeforeDecimal = decimalPos;
          const maxDigitsAfterDecimal = Math.max(0, 11 - digitsBeforeDecimal);
          return parseFloat(result.toFixed(maxDigitsAfterDecimal));
        } else {
          // Integer too large, convert to exponential
          return result.toExponential(6);
        }
      }
      
      return result;
    }

    /**
     * Clears the calculator.
     */
    clear() {
      this.display = '0';
      this.currentValue = null;
      this.operator = null;
      this.waitingForOperand = false;
      this.updateDisplay(this.display);
    }

    /**
     * Clears the current entry.
     */
    clearEntry() {
      this.display = '0';
      this.updateDisplay(this.display);
    }

    /**
     * Negates the current value.
     */
    negate() {
      const value = parseFloat(this.display);
      if (value !== 0) {
        this.updateDisplay((-value).toString());
      }
    }

    /**
     * Clears the memory.
     */
    memoryClear() {
      this.memory = 0;
    }

    /**
     * Recalls the value from memory.
     */
    memoryRecall() {
      this.updateDisplay(this.memory.toString());
      this.waitingForOperand = true;
    }

    /**
     * Adds the current value to memory.
     */
    memoryAdd() {
      this.memory += parseFloat(this.display);
    }

    /**
     * Subtracts the current value from memory.
     */
    memorySubtract() {
      this.memory -= parseFloat(this.display);
    }
  }

  /**
   * Lightens a color by the given percentage.
   * @param {string} color - The color to lighten
   * @param {number} percent - The percentage to lighten
   * @returns {string} - The lightened color
   * @private
   */
  function lightenColor(color, percent) {
    // Convert hex to rgb
    let r, g, b;
    
    if (color.startsWith('rgb')) {
      // Parse RGB format
      const rgbMatch = color.match(/rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*[\d.]+)?\)/);
      if (rgbMatch) {
        r = parseInt(rgbMatch[1]);
        g = parseInt(rgbMatch[2]);
        b = parseInt(rgbMatch[3]);
      } else {
        return color;
      }
    } else if (color.startsWith('#')) {
      // Parse hex format
      const hex = color.substring(1);
      r = parseInt(hex.substr(0, 2), 16);
      g = parseInt(hex.substr(2, 2), 16);
      b = parseInt(hex.substr(4, 2), 16);
    } else {
      return color;
    }
    
    // Lighten
    r = Math.min(255, Math.floor(r * (100 + percent) / 100));
    g = Math.min(255, Math.floor(g * (100 + percent) / 100));
    b = Math.min(255, Math.floor(b * (100 + percent) / 100));
    
    return `rgb(${r}, ${g}, ${b})`;
  }

  // Export the Calculator class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Calculator = Calculator;
})();