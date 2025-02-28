/**
 * A reactive state container that triggers updates when the state changes.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class State {
    /**
     * Creates a new State instance.
     * @param {*} initialValue - The initial value of the state
     */
    constructor(initialValue) {
      this.value = initialValue;
      this.listeners = [];
    }

    /**
     * Gets the current value of the state.
     * @returns {*} - The current state value
     */
    get() {
      return this.value;
    }

    /**
     * Sets a new value for the state and notifies listeners.
     * @param {*} newValue - The new state value
     */
    set(newValue) {
      // Skip update if value hasn't changed
      if (this.value === newValue || 
          (this.value !== null && 
           typeof this.value === 'object' && 
           JSON.stringify(this.value) === JSON.stringify(newValue))) {
        return;
      }
      
      this.value = newValue;
      this.notifyListeners();
    }

    /**
     * Subscribes a listener to state changes.
     * @param {Function} listener - The listener function
     */
    subscribe(listener) {
      this.listeners.push(listener);
      // Immediately notify with current value
      listener(this.value);
    }

    /**
     * Unsubscribes a listener from state changes.
     * @param {Function} listener - The listener function to remove
     */
    unsubscribe(listener) {
      const index = this.listeners.indexOf(listener);
      if (index !== -1) {
        this.listeners.splice(index, 1);
      }
    }

    /**
     * Notifies all listeners of a state change.
     * @private
     */
    notifyListeners() {
      for (const listener of this.listeners) {
        listener(this.value);
      }
    }
  }

  // Export the State class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.State = State;
})();