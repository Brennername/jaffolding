# Java TeaVM Front-End Framework

A lightweight Java front-end framework that compiles to JavaScript using TeaVM.




I don't think this readme is up to date. It was sort of a roadmap, and I have since taken a few different directions, so this is all probably outdated. My bad.


## Demo

You can view the JavaScript side of the Jaffolding framework (the Java isn't ready) at https://jaffolding-58841cf7d4df.herokuapp.com/

This is a dyno, so it falls asleep after inactivity. If you are the first to visit in a while, it will have a good 1-3 second startup time. The app isn't really slow, it's just the cheap tier I pay for heroku.

## Features

- Write front-end applications in Java
- Compiles to JavaScript for browser compatibility
- Component-based architecture
- Reactive state management
- Client-side routing
- Event handling
- JavaScript library integrations (Chart.js, Three.js)

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── danielremsburg/
│   │   │           └── jaffolding/
│   │   │               ├── Component.java     # Core component class
│   │   │               ├── Main.java          # Entry point
│   │   │               ├── Router.java        # Client-side router
│   │   │               ├── State.java         # Reactive state management
│   │   │               └── examples/          # Example applications
│   │   └── resources/
│   │       └── static/
│   │           ├── index.html                 # HTML entry point
│   │           └── js/
│   │               └── main.js                # JavaScript fallback demo
│   └── test/
│       └── java/                              # Test classes
├── pom.xml                                    # Maven configuration
├── package.json                               # Node.js configuration
└── server.js                                  # Simple Express server
```

## Getting Started

### Prerequisites

- Node.js

### Running the Project

```bash
# Install Node.js dependencies
npm install

# Start the development server
npm start
```

Then open your browser to http://localhost:3000

## How It Works

This framework uses TeaVM to compile Java code to JavaScript, allowing it to run in any modern web browser. The core architecture includes:

1. **Component System**: A lightweight virtual DOM implementation
2. **State Management**: Reactive state with subscription-based updates
3. **Event Handling**: DOM event binding with Java callbacks
4. **Routing**: Client-side routing for single-page applications

## Example Usage

```java
// Create a simple component
Component app = new Component("div")
    .addChild(new Component("h1").setText("Hello, World!"))
    .addChild(new Component("p").setText("This is a Java TeaVM Framework example"))
    .addChild(new Component("button")
        .setText("Click me")
        .addEventListener("click", event -> {
            alert("Button clicked!");
        }));

// Render to DOM
app.render(document.getElementById("app"));
```

## Creating a Todo Application

See the `TodoApp.java` example for a complete todo application implementation.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
