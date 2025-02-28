const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const cors = require('cors');
const fs = require('fs');
const app = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Ensure required directories exist
const requiredDirs = [
  'src/main/resources/static/js/jaffolding/core',
  'src/main/resources/static/js/jaffolding/ui',
  'src/main/resources/static/js/jaffolding/ui/layout',
  'src/main/resources/static/js/jaffolding/demos',
  'src/main/resources/static/teavm'
];

requiredDirs.forEach(dir => {
  const fullPath = path.join(__dirname, dir);
  if (!fs.existsSync(fullPath)) {
    console.log(`Creating directory: ${dir}`);
    fs.mkdirSync(fullPath, { recursive: true });
  }
});

// Serve static files
app.use(express.static(path.join(__dirname, 'src/main/resources/static')));
app.use('/node_modules', express.static(path.join(__dirname, 'node_modules')));

// Try to serve compiled TeaVM files if they exist
app.use('/teavm', express.static(path.join(__dirname, 'target/classes/static/teavm')));

// Mock API endpoints
const mockData = {
  salesData: [
    { id: 1, product: 'Laptop', category: 'Electronics', sales: 120, revenue: 120000, month: 'January' },
    { id: 2, product: 'Smartphone', category: 'Electronics', sales: 200, revenue: 100000, month: 'January' },
    { id: 3, product: 'Headphones', category: 'Accessories', sales: 150, revenue: 15000, month: 'January' },
    { id: 4, product: 'Monitor', category: 'Electronics', sales: 80, revenue: 24000, month: 'January' },
    { id: 5, product: 'Keyboard', category: 'Accessories', sales: 100, revenue: 5000, month: 'January' },
    { id: 6, product: 'Laptop', category: 'Electronics', sales: 130, revenue: 130000, month: 'February' },
    { id: 7, product: 'Smartphone', category: 'Electronics', sales: 180, revenue: 90000, month: 'February' },
    { id: 8, product: 'Headphones', category: 'Accessories', sales: 170, revenue: 17000, month: 'February' },
    { id: 9, product: 'Monitor', category: 'Electronics', sales: 85, revenue: 25500, month: 'February' },
    { id: 10, product: 'Keyboard', category: 'Accessories', sales: 110, revenue: 5500, month: 'February' }
  ],
  categories: ['Electronics', 'Accessories', 'Software', 'Services'],
  products: ['Laptop', 'Smartphone', 'Headphones', 'Monitor', 'Keyboard', 'Mouse', 'Tablet', 'Printer'],
  months: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
};

// API Routes
app.get('/api/sales', (req, res) => {
  res.json(mockData.salesData);
});

app.get('/api/categories', (req, res) => {
  res.json(mockData.categories);
});

app.get('/api/products', (req, res) => {
  res.json(mockData.products);
});

app.get('/api/months', (req, res) => {
  res.json(mockData.months);
});

app.post('/api/sales', (req, res) => {
  const newSale = {
    id: mockData.salesData.length + 1,
    ...req.body
  };
  mockData.salesData.push(newSale);
  res.status(201).json(newSale);
});

app.put('/api/sales/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const index = mockData.salesData.findIndex(item => item.id === id);
  
  if (index !== -1) {
    mockData.salesData[index] = { ...mockData.salesData[index], ...req.body };
    res.json(mockData.salesData[index]);
  } else {
    res.status(404).json({ error: 'Sale not found' });
  }
});

app.delete('/api/sales/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const index = mockData.salesData.findIndex(item => item.id === id);
  
  if (index !== -1) {
    const deleted = mockData.salesData.splice(index, 1);
    res.json(deleted[0]);
  } else {
    res.status(404).json({ error: 'Sale not found' });
  }
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'UP', mode: 'Node.js' });
});

// Serve index.html for all other routes (SPA support)
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'src/main/resources/static/index.html'));
});

app.listen(port, () => {
  console.log(`Jaffolding server running at http://localhost:${port}`);
  console.log(`Running in Node.js fallback mode`);
  console.log(`To use Spring Boot mode, run: npm run spring`);
});