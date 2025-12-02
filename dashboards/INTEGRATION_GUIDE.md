# F1 Dashboard Integration Guide

This guide explains how to use the F1 Dashboard components in another React-based website.

## Quick Integration (Easiest Method)

### Step 1: Copy Required Files

Copy these files/folders to your React project:

```
your-react-project/
  src/
    ├── dashboards/
    │   └── GeneralDashboard.jsx      # Main dashboard component (copy from dashboards/src/dashboards/)
    ├── components/
    │   └── StatCard.jsx              # Stat card component (copy from dashboards/src/components/)
    └── data/                         # JSON data files (optional - see Step 4 for alternatives)
        ├── current-season.json
        ├── driver-championships.json
        ├── constructor-championships.json
        └── driver-stats.json
```

**Important**: You also need to copy the CSS. Either:
- Copy `dashboards/src/index.css` and import it, OR
- Copy just the Tailwind theme colors to your existing Tailwind config (see Step 3)

### Step 2: Install Dependencies

In your React project, install the required dependencies:

```bash
npm install recharts
npm install -D tailwindcss postcss autoprefixer
```

If your project already uses Tailwind, you can skip the Tailwind installation.

### Step 3: Add Tailwind Configuration

If you're using Tailwind, ensure your `tailwind.config.js` includes the F1 theme colors:

```js
module.exports = {
  theme: {
    extend: {
      colors: {
        'f1-red': '#E10600',
        'f1-black': '#15151E',
        'f1-darkGray': '#1F1F27',
        'f1-gray': '#38383F',
        'f1-lightGray': '#949498',
      }
    }
  }
}
```

### Step 4: Modify GeneralDashboard.jsx to Accept Props (Simple Change)

The current `GeneralDashboard.jsx` imports JSON directly. To make it reusable, modify it to accept optional props:

**Change lines 7-10 from:**
```jsx
import currentSeasonData from '../data/current-season.json'
import driverChampionshipsData from '../data/driver-championships.json'
import constructorChampionshipsData from '../data/constructor-championships.json'
import driverStatsData from '../data/driver-stats.json'
```

**To:**
```jsx
// Optional: Keep imports as fallback
import currentSeasonData from '../data/current-season.json'
import driverChampionshipsData from '../data/driver-championships.json'
import constructorChampionshipsData from '../data/constructor-championships.json'
import driverStatsData from '../data/driver-stats.json'
```

**Change the component signature (line 12) from:**
```jsx
const GeneralDashboard = () => {
```

**To:**
```jsx
const GeneralDashboard = ({
  currentSeason: propCurrentSeason,
  driverChampionships: propDriverChampionships,
  constructorChampionships: propConstructorChampionships,
  driverStats: propDriverStats
}) => {
```

**Update the useEffect (lines 19-31) to:**
```jsx
useEffect(() => {
  // If props provided, use them; otherwise use imported data
  if (propCurrentSeason || propDriverChampionships || propConstructorChampionships || propDriverStats) {
    setCurrentSeason(propCurrentSeason || null)
    setDriverChampionships(propDriverChampionships || [])
    setConstructorChampionships(propConstructorChampionships || [])
    setDriverStats(propDriverStats || [])
    setLoading(false)
  } else {
    // Fallback to imported JSON data (for backward compatibility)
    try {
      setCurrentSeason(currentSeasonData)
      setDriverChampionships(driverChampionshipsData)
      setConstructorChampionships(constructorChampionshipsData)
      setDriverStats(driverStatsData)
    } catch (error) {
      console.error('Error loading data:', error)
    } finally {
      setLoading(false)
    }
  }
}, [propCurrentSeason, propDriverChampionships, propConstructorChampionships, propDriverStats])
```

### Step 5: Use in Your Project

#### Option A: With JSON files (if you copy the data folder)
```jsx
import GeneralDashboard from './dashboards/GeneralDashboard'

function App() {
  return <GeneralDashboard />
}
```

#### Option B: Fetch data from API
```jsx
import { useState, useEffect } from 'react'
import GeneralDashboard from './dashboards/GeneralDashboard'

function App() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch('/api/f1-data')  // Your API endpoint
      .then(res => res.json())
      .then(data => {
        setData(data)
        setLoading(false)
      })
  }, [])

  if (loading) return <div>Loading...</div>

  return (
    <GeneralDashboard
      currentSeason={data.currentSeason}
      driverChampionships={data.driverChampionships}
      constructorChampionships={data.constructorChampionships}
      driverStats={data.driverStats}
    />
  )
}
```

#### Option C: Pass data as props directly
```jsx
import GeneralDashboard from './dashboards/GeneralDashboard'

function App() {
  const myData = {
    currentSeason: { /* your data */ },
    driverChampionships: [ /* your data */ ],
    constructorChampionships: [ /* your data */ ],
    driverStats: [ /* your data */ ]
  }

  return <GeneralDashboard {...myData} />
}
```

## Method 2: Publish as npm Package (For Multiple Projects)

### Create a Library Build

Update `vite.config.js` to build as a library:

```js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: './src/index.js',
      name: 'F1Dashboard',
      fileName: 'f1-dashboard'
    },
    rollupOptions: {
      external: ['react', 'react-dom', 'recharts'],
      output: {
        globals: {
          react: 'React',
          'react-dom': 'ReactDOM',
          recharts: 'Recharts'
        }
      }
    }
  }
})
```

### Create Entry Point

Create `src/index.js`:

```js
export { default as GeneralDashboard } from './dashboards/GeneralDashboard'
export { default as StatCard } from './components/StatCard'
```

Then install in your other project:

```bash
npm install @your-username/f1-dashboard
```

## Method 3: Git Submodule or Copy Files

1. **Git Submodule**: Add this repo as a submodule in your project
2. **Direct Copy**: Simply copy the component files into your project

## Data Requirements

The dashboard expects data in this format:

```javascript
{
  currentSeason: {
    season: 2025,
    totalRaces: 20,
    uniqueDrivers: 21,
    driverStandings: [...],
    constructorStandings: [...],
    podiumStats: [...],
    dnfStats: [...],
    timePenaltyStats: [...],
    gridPenaltyStats: [...]
  },
  driverChampionships: [...],
  constructorChampionships: [...],
  driverStats: [...]
}
```

## Customization

- **Styling**: All styles use Tailwind classes. You can override them in your Tailwind config.
- **Colors**: The F1 theme colors are defined in the Tailwind config above.
- **Layout**: The component is self-contained and doesn't require the DashboardLayout wrapper.

## Dependencies

Required npm packages:
- `react` (^18.3.1)
- `react-dom` (^18.3.1)
- `recharts` (^2.12.7)
- `tailwindcss` (^3.4.10) - if not already using Tailwind

## Notes

- The dashboard uses direct JSON imports. For production, consider fetching data from an API.
- All tooltips and interactive elements work independently of any router.
- The component is fully responsive and works in any container size.

