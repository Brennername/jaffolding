const shell = require('shelljs');
const fs = require('fs');
const path = require('path');

console.log('Starting comprehensive build process...');

// Check if Maven is available
const hasMaven = shell.which('mvn');

// Always build the Node.js part
console.log('Building Node.js components...');

// Ensure static directories exist
shell.mkdir('-p', 'src/main/resources/static/js');
shell.mkdir('-p', 'target/classes/static');

// Copy static assets
shell.cp('-R', 'src/main/resources/static/*', 'target/classes/static/');

// If Maven is available, also build the Spring Boot part
if (hasMaven) {
  console.log('Maven found, building Spring Boot application...');
  
  if (shell.exec('mvn clean package').code !== 0) {
    console.log('Maven build failed, but Node.js components are still available.');
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
  console.log('Maven not found, skipping Spring Boot build.');
}

console.log('Build process completed!');