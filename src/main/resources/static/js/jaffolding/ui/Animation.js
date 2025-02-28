/**
 * Animation utility for UI components.
 * JavaScript implementation that mirrors the Java API.
 */
(function() {
  class Animation {
    /**
     * Animates a component with a fade-in effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {number} duration - The animation duration in seconds
     */
    static fadeIn(component, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      element.style.opacity = '0';
      element.style.transition = `opacity ${duration}s ease`;
      
      // Use setTimeout to ensure the transition works
      setTimeout(() => {
        element.style.opacity = '1';
      }, 10);
    }

    /**
     * Animates a component with a fade-out effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {number} duration - The animation duration in seconds
     */
    static fadeOut(component, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      element.style.opacity = '1';
      element.style.transition = `opacity ${duration}s ease`;
      
      setTimeout(() => {
        element.style.opacity = '0';
      }, 10);
    }

    /**
     * Animates a component with a slide-in effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {string} direction - The slide direction ('left', 'right', 'top', 'bottom')
     * @param {number} duration - The animation duration in seconds
     */
    static slideIn(component, direction, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      let transform = '';
      switch (direction.toLowerCase()) {
        case 'left':
          transform = 'translateX(-100%)';
          break;
        case 'right':
          transform = 'translateX(100%)';
          break;
        case 'top':
          transform = 'translateY(-100%)';
          break;
        case 'bottom':
          transform = 'translateY(100%)';
          break;
        default:
          transform = 'translateY(20px)';
      }
      
      element.style.transform = transform;
      element.style.opacity = '0';
      element.style.transition = `transform ${duration}s ease, opacity ${duration}s ease`;
      
      setTimeout(() => {
        element.style.transform = 'translate(0, 0)';
        element.style.opacity = '1';
      }, 10);
    }

    /**
     * Animates a component with a slide-out effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {string} direction - The slide direction ('left', 'right', 'top', 'bottom')
     * @param {number} duration - The animation duration in seconds
     */
    static slideOut(component, direction, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      let transform = '';
      switch (direction.toLowerCase()) {
        case 'left':
          transform = 'translateX(-100%)';
          break;
        case 'right':
          transform = 'translateX(100%)';
          break;
        case 'top':
          transform = 'translateY(-100%)';
          break;
        case 'bottom':
          transform = 'translateY(100%)';
          break;
        default:
          transform = 'translateY(20px)';
      }
      
      element.style.transform = 'translate(0, 0)';
      element.style.opacity = '1';
      element.style.transition = `transform ${duration}s ease, opacity ${duration}s ease`;
      
      setTimeout(() => {
        element.style.transform = transform;
        element.style.opacity = '0';
      }, 10);
    }

    /**
     * Animates a component with a scale effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {number} fromScale - The starting scale
     * @param {number} toScale - The ending scale
     * @param {number} duration - The animation duration in seconds
     */
    static scale(component, fromScale, toScale, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      element.style.transform = `scale(${fromScale})`;
      element.style.transition = `transform ${duration}s ease`;
      
      setTimeout(() => {
        element.style.transform = `scale(${toScale})`;
      }, 10);
    }

    /**
     * Animates a component with a rotate effect.
     * @param {Component|HTMLElement} component - The component to animate
     * @param {number} fromDegrees - The starting rotation in degrees
     * @param {number} toDegrees - The ending rotation in degrees
     * @param {number} duration - The animation duration in seconds
     */
    static rotate(component, fromDegrees, toDegrees, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      element.style.transform = `rotate(${fromDegrees}deg)`;
      element.style.transition = `transform ${duration}s ease`;
      
      setTimeout(() => {
        element.style.transform = `rotate(${toDegrees}deg)`;
      }, 10);
    }

    /**
     * Animates a property of a component using GSAP (if available).
     * @param {Component|HTMLElement} component - The component to animate
     * @param {string} property - The property to animate
     * @param {*} value - The target value
     * @param {number} duration - The animation duration in seconds
     */
    static animate(component, property, value, duration) {
      const element = component.getElement ? component.getElement() : component;
      if (!element) return;
      
      try {
        if (window.gsap) {
          const props = {};
          props[property] = value;
          props.duration = duration;
          window.gsap.to(element, props);
        } else {
          // Fallback to CSS transitions if GSAP is not available
          element.style.transition = `${property} ${duration}s ease`;
          element.style[property] = value;
        }
      } catch (e) {
        console.warn('Animation failed:', e);
        // Fallback to direct property setting
        element.style[property] = value;
      }
    }

    /**
     * Sets a timeout to execute a callback after a delay.
     * @param {Function} callback - The callback function
     * @param {number} delay - The delay in milliseconds
     */
    static setTimeout(callback, delay) {
      return setTimeout(callback, delay);
    }
  }

  // Export the Animation class
  window.jaffolding = window.jaffolding || {};
  window.jaffolding.Animation = Animation;
})();