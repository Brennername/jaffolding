/**
 * Script to ensure all required directories exist for the JS implementation
 */
const fs = require('fs');
const path = require('path');

// Define the directory structure
const directories = [
  'src/main/resources/static/js/jaffolding/core',
  'src/main/resources/static/js/jaffolding/ui',
  'src/main/resources/static/js/jaffolding/ui/layout',
  'src/main/resources/static/js/jaffolding/demos',
  'src/main/resources/static/teavm'
];

// Create directories if they don't exist
directories.forEach(dir => {
  const fullPath = path.join(process.cwd(), dir);
  if (!fs.existsSync(fullPath)) {
    console.log(`Creating directory: ${dir}`);
    fs.mkdirSync(fullPath, { recursive: true });
  }
});

console.log('Directory structure verified.');