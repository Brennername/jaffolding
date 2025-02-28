/**
 * Script to fix common build issues in the Jaffolding project
 */
const fs = require('fs');
const path = require('path');
const shell = require('shelljs');

console.log('Starting build fix script...');

// Ensure required directories exist
const requiredDirs = [
  'src/main/java/com/danielremsburg/jaffolding/bridge',
  'src/main/resources/static/js/jaffolding/bridge',
  'src/main/resources/static/js/jaffolding/ui/kde'
];

requiredDirs.forEach(dir => {
  const fullPath = path.join(process.cwd(), dir);
  if (!fs.existsSync(fullPath)) {
    console.log(`Creating directory: ${dir}`);
    fs.mkdirSync(fullPath, { recursive: true });
  }
});

// Check if TeaVM dependencies are in pom.xml
const pomPath = path.join(process.cwd(), 'pom.xml');
if (fs.existsSync(pomPath)) {
  let pomContent = fs.readFileSync(pomPath, 'utf8');
  
  // Check for required TeaVM dependencies
  const requiredDeps = [
    'teavm-classlib',
    'teavm-platform',
    'teavm-interop'
  ];
  
  let missingDeps = [];
  requiredDeps.forEach(dep => {
    if (!pomContent.includes(dep)) {
      missingDeps.push(dep);
    }
  });
  
  if (missingDeps.length > 0) {
    console.log(`Missing TeaVM dependencies: ${missingDeps.join(', ')}`);
    console.log('Please add these dependencies to your pom.xml file.');
  } else {
    console.log('All required TeaVM dependencies found in pom.xml');
  }
  
  // Check for optimizationLevel in TeaVM plugin
  if (!pomContent.includes('<optimizationLevel>')) {
    console.log('TeaVM plugin is missing optimizationLevel configuration.');
    console.log('Please add <optimizationLevel>ADVANCED</optimizationLevel> to the TeaVM plugin configuration.');
  }
}

// Check for bridge classes
const bridgeFiles = [
  'src/main/java/com/danielremsburg/jaffolding/bridge/JSBridge.java',
  'src/main/java/com/danielremsburg/jaffolding/bridge/JSComponentWrapper.java',
  'src/main/java/com/danielremsburg/jaffolding/bridge/ComponentRegistry.java',
  'src/main/java/com/danielremsburg/jaffolding/bridge/ComponentFactory.java',
  'src/main/java/com/danielremsburg/jaffolding/bridge/HTMLTableElements.java'
];

let missingBridgeFiles = [];
bridgeFiles.forEach(file => {
  const fullPath = path.join(process.cwd(), file);
  if (!fs.existsSync(fullPath)) {
    missingBridgeFiles.push(file);
  }
});

if (missingBridgeFiles.length > 0) {
  console.log(`Missing bridge files: ${missingBridgeFiles.join(', ')}`);
  console.log('Please create these files to enable Java-JavaScript interoperability.');
} else {
  console.log('All required bridge files found.');
}

// Check for duplicate SpringBootApplication class
const springBootAppPath = path.join(process.cwd(), 'src/main/java/com/danielremsburg/jaffolding/SpringBootApplication.java');
if (fs.existsSync(springBootAppPath)) {
  console.log('Found duplicate SpringBootApplication class. This may cause compilation errors.');
  console.log('Consider renaming or removing this class.');
}

// Check for missing imports in Main.java
const mainPath = path.join(process.cwd(), 'src/main/java/com/danielremsburg/jaffolding/Main.java');
if (fs.existsSync(mainPath)) {
  let mainContent = fs.readFileSync(mainPath, 'utf8');
  
  const requiredImports = [
    'import java.util.function.Consumer;',
    'import org.teavm.jso.dom.events.Event;'
  ];
  
  let missingImports = [];
  requiredImports.forEach(imp => {
    if (!mainContent.includes(imp)) {
      missingImports.push(imp);
    }
  });
  
  if (missingImports.length > 0) {
    console.log(`Missing imports in Main.java: ${missingImports.join(', ')}`);
    console.log('Please add these imports to fix compilation errors.');
  } else {
    console.log('All required imports found in Main.java');
  }
}

console.log('Build fix script completed.');