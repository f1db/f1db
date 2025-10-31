# F1 Dashboards

Modern, interactive Formula 1 analytics dashboards built with React, Recharts, Tailwind CSS, and anime.js.

## Features

- **General F1 Dashboard**: Comprehensive statistics including current season standings, historical data, and all-time records
- **Best Driver Analysis**: (Coming Soon) Advanced analytics to determine the greatest F1 driver using custom formulas
- **Animated UI**: Smooth animations powered by anime.js
- **Responsive Design**: Works beautifully on all screen sizes
- **F1-Themed**: Custom color scheme matching Formula 1 branding

## Tech Stack

- **React 18** - Modern UI library
- **Vite** - Lightning-fast build tool
- **Recharts** - Composable charting library
- **Tailwind CSS** - Utility-first CSS framework
- **anime.js** - JavaScript animation engine
- **React Router** - Client-side routing

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Python 3.7+ (for data generation)
- PyYAML package: `pip install pyyaml`

### Installation

1. **Navigate to the dashboards directory**:
   ```bash
   cd dashboards
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Generate data from F1DB**:
   ```bash
   npm run generate-data
   # or directly: python3 scripts/generate-data.py
   ```

4. **Start the development server**:
   ```bash
   npm run dev
   ```

5. **Open your browser**:
   The app will automatically open at `http://localhost:3000`

## Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run generate-data` | Generate JSON data from F1DB YAML files |

## Project Structure

```
dashboards/
├── src/
│   ├── components/         # Reusable React components
│   │   ├── DashboardLayout.jsx
│   │   └── StatCard.jsx
│   ├── dashboards/         # Dashboard pages
│   │   ├── GeneralDashboard.jsx
│   │   └── BestDriverDashboard.jsx
│   ├── data/              # Generated JSON data files
│   │   ├── current-season.json
│   │   ├── driver-championships.json
│   │   ├── constructor-championships.json
│   │   ├── driver-stats.json
│   │   └── circuits.json
│   ├── App.jsx            # Main app component with routing
│   ├── main.jsx           # App entry point
│   └── index.css          # Global styles with Tailwind
├── scripts/
│   └── generate-data.py   # Python script to extract data from F1DB
├── public/                # Static assets
├── index.html             # HTML template
├── package.json           # Dependencies and scripts
├── vite.config.js         # Vite configuration
├── tailwind.config.js     # Tailwind CSS configuration
└── README.md             # This file
```

## Data Generation

The `generate-data.py` script extracts data from the F1DB YAML files and creates optimized JSON files for the dashboards:

- **current-season.json**: Latest season standings and recent races
- **driver-championships.json**: Historical driver championship data
- **constructor-championships.json**: Historical constructor championship data
- **driver-stats.json**: Aggregated driver statistics (wins, poles, championships)
- **circuits.json**: Circuit information

### Customizing Data Extraction

Edit `scripts/generate-data.py` to:
- Add new data transformations
- Create custom aggregations for your Best Driver formula
- Export additional metrics

## Customization

### Adding New Dashboards

1. Create a new component in `src/dashboards/`
2. Add a route in `src/App.jsx`
3. Add navigation link in `src/components/DashboardLayout.jsx`

### Styling

The project uses a custom F1 color theme defined in `tailwind.config.js`:

```javascript
colors: {
  f1: {
    red: '#E10600',
    black: '#15151E',
    darkGray: '#1E1E28',
    gray: '#38383F',
    lightGray: '#949498',
    white: '#FFFFFF',
  }
}
```

Custom utility classes are available:
- `.dashboard-card` - Styled card container
- `.dashboard-header` - Gradient text header
- `.stat-card` - Animated statistics card

## Deploying

### Build for Production

```bash
npm run build
```

The optimized files will be in the `dist/` directory.

### Deployment Options

- **Vercel**: Connect your repo and deploy automatically
- **Netlify**: Drag and drop the `dist/` folder
- **GitHub Pages**: Use `gh-pages` package
- **Any static host**: Upload the `dist/` folder

## Portability

This dashboard setup is fully self-contained and can be easily moved to another repository:

1. Copy the entire `dashboards/` folder
2. Ensure the data generation script can access F1DB data (or use pre-generated JSON)
3. Run `npm install` and `npm run dev`

Alternatively, you can use F1DB's official releases which include pre-built JSON exports.

## Future Enhancements

- [ ] Best Driver of All Time dashboard with custom formula
- [ ] Race track visualizations using anime.js and SVG
- [ ] Real-time race data integration
- [ ] Driver comparison tools
- [ ] Historical era analysis
- [ ] Mobile-optimized views
- [ ] Dark/light theme toggle
- [ ] Export charts as images

## Contributing

Feel free to add your own dashboards, improve visualizations, or enhance the data extraction logic!

## License

This project uses data from F1DB (check F1DB license). Dashboard code is provided as-is.
