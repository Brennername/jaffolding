const shell = require('shelljs');
const fs = require('fs');
const path = require('path');

console.log('Starting build process...');

// Check if Maven is available
const hasMaven = shell.which('mvn');

if (hasMaven) {
  console.log('Maven found, attempting to build with Spring Boot...');
  
  if (shell.exec('mvn clean package').code !== 0) {
    console.log('Maven build failed, falling back to Node.js build...');
    buildWithNode();
  } else {
    console.log('Maven build successful!');
    
    // Check if TeaVM output exists and copy it to the Node.js static directory
    const teavmDir = path.join('target', 'classes', 'static', 'teavm');
    if (fs.existsSync(teavmDir)) {
      console.log('TeaVM output found, making it available to Node.js server...');
      shell.mkdir('-p', 'src/main/resources/static/teavm');
      shell.cp('-R', path.join(teavmDir, '*'), 'src/main/resources/static/teavm/');
    }
  }
} else {
  console.log('Maven not found, falling back to Node.js build...');
  buildWithNode();
}

function buildWithNode() {
  console.log('Building with Node.js...');
  
  // Ensure static directories exist
  shell.mkdir('-p', 'src/main/resources/static/js');
  
  // Copy static assets
  shell.mkdir('-p', 'target/classes/static');
  shell.cp('-R', 'src/main/resources/static/*', 'target/classes/static/');
  
  console.log('Node.js build completed successfully!');
}