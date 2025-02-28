/**
 * Bridge to access Java components from JavaScript.
 * This allows the JavaScript framework to use Java components when needed.
 */
(function() {
  class JavaBridge {
    /**
     * Checks if a Java component is available.
     * @param {string} className - The fully qualified Java class name
     * @returns {boolean} - Whether the component is available
     */
    static isComponentAvailable(className) {
      try {
        // Check if TeaVM is available
        if (!window.$rt_javaClassesLoaded) {
          return false;
        }
        
        // Try to find the Java class
        const classRef = window.$rt_classByName(className);
        return classRef !== undefined;
      } catch (e) {
        console.error('Error checking Java component availability:', e);
        return false;
      }
    }
    
    /**
     * Creates a Java component instance.
     * @param {string} className - The fully qualified Java class name
     * @param {Array} args - The constructor arguments
     * @returns {Object} - The Java component instance
     */
    static createComponent(className, args) {
      try {
        if (!window.$rt_javaClassesLoaded) {
          return null;
        }
        
        // Get the Java class
        const classRef = window.$rt_classByName(className);
        if (!classRef) {
          return null;
        }
        
        // Create an instance
        return window.$rt_new(classRef, ...args);
      } catch (e) {
        console.error('Error creating Java component:', e);
        return null;
      }
    }
    
    /**
     * Calls a method on a Java object.
     * @param {Object} object - The Java object
     * @param {string} methodName - The method name
     * @param {Array} args - The method arguments
     * @returns {*} - The result of the method call
     */
    static callMethod(object, methodName, args) {
      try {
        if (!object) {
          return null;
        }
        
        // Find the method
        const method = object[methodName];
        if (typeof method !== 'function') {
          return null;
        }
        
        // Call the method
        return method.apply(object, args || []);
      } catch (e) {
        console.error('Error calling Java method:', e);
        return null;
      }
    }
    
    /**
     * Gets a property from a Java object.
     * @param {Object} object - The Java object
     * @param {string} propertyName - The property name
     * @returns {*} - The property value
     */
    static getProperty(object, propertyName) {
      try {
        if (!object) {
          return null;
        }
        
        // Try to get the property
        return object[propertyName];
      } catch (e) {
        console.error('Error getting Java property:', e);
        return null;
      }
    }
    
    /**
     * Sets a property on a Java object.
     * @param {Object} object - The Java object
     * @param {string} propertyName - The property name
     * @param {*} value - The property value
     */
    static setProperty(object, propertyName, value) {
      try {
        if (!object) {
          return;
        }
        
        // Try to set the property
        object[propertyName] = value;
      } catch (e) {
        console.error('Error setting Java property:', e);
      }
    }
    
    /**
     * Converts a JavaScript value to a Java value.
     * @param {*} value - The JavaScript value
     * @returns {*} - The Java value
     */
    static toJava(value) {
      if (value === null || value === undefined) {
        return null;
      }
      
      if (typeof value === 'string') {
        return window.$rt_createString(value);
      }
      
      if (typeof value === 'number') {
        return value;
      }
      
      if (typeof value === 'boolean') {
        return value;
      }
      
      if (Array.isArray(value)) {
        // Convert to Java array
        const arrayClass = window.$rt_classByName('java.lang.Object[]');
        const array = window.$rt_createArray(arrayClass, value.length);
        for (let i = 0; i < value.length; i++) {
          array[i] = JavaBridge.toJava(value[i]);
        }
        return array;
      }
      
      if (typeof value === 'object') {
        // For now, just return the object as is
        return value;
      }
      
      return value;
    }
    
    /**
     * Converts a Java value to a JavaScript value.
     * @param {*} value - The Java value
     * @returns {*} - The JavaScript value
     */
    static toJS(value) {
      if (value === null || value === undefined) {
        return null;
      }
      
      // Check if it's a Java string
      if (value.$rt_classId && window.$rt_isInstance(value, window.$rt_classByName('java.lang.String'))) {
        return window.$rt_stringToJs(value);
      }
      
      // Check if it's a Java array
      if (value.$rt_classId && value.$rt_classId.itemType) {
        const result = [];
        for (let i = 0; i < value.length; i++) {
          result.push(JavaBridge.toJS(value[i]));
        }
        return result;
      }
      
      return value;
    }
  }

  // Export the JavaBridge class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.JavaBridge = JavaBridge;
})();