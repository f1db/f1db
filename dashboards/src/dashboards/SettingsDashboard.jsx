import React, { useState, useEffect } from 'react'
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip,
  ResponsiveContainer, RadarChart, PolarGrid, PolarAngleAxis,
  PolarRadiusAxis, Radar, Cell
} from 'recharts'
import StatCard from '../components/StatCard'
import dgiData from '../data/driver-greatness-index.json'

// Custom Tooltip Component for DGI Chart
const DGITooltip = ({ active, payload }) => {
  if (!active || !payload || payload.length === 0) {
    return null
  }

  const driver = payload[0].payload

  return (
    <div
      style={{
        backgroundColor: '#1E1E28',
        border: '1px solid #38383F',
        borderRadius: '8px',
        padding: '12px',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.3)'
      }}
    >
      <div style={{ color: '#fff', fontWeight: 'bold', marginBottom: '8px', fontSize: '14px' }}>
        {driver.fullName}
      </div>
      <div style={{ color: '#E10600', fontSize: '16px', fontWeight: 'bold', marginBottom: '8px' }}>
        DGI: {driver.dgi.toFixed(4)}
      </div>
      <div style={{ color: '#949498', fontSize: '12px', marginBottom: '4px' }}>
        Wins: {driver.wins} • Championships: {driver.championships} • Poles: {driver.poles}
      </div>
      <div style={{ color: '#949498', fontSize: '12px' }}>
        Teammate Dominance: {driver.teammateDominance.toFixed(1)}%
      </div>
    </div>
  )
}

// Custom Driver Select Component (reused from BestDriverDashboard)
const CustomDriverSelect = ({ drivers, selectedDriver, onSelect }) => {
  const [isOpen, setIsOpen] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  
  const filteredDrivers = drivers.filter(driver => {
    const fullName = `${driver.firstName} ${driver.lastName}`.toLowerCase()
    return fullName.includes(searchTerm.toLowerCase())
  })

  const handleSelect = (driver) => {
    onSelect(driver)
    setIsOpen(false)
    setSearchTerm('')
  }

  return (
    <div className="relative mb-6">
      <label className="block text-f1-lightGray text-sm mb-2 font-medium">
        Select Driver (Top 30)
      </label>
      <div className="relative">
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="w-full md:w-80 px-4 py-3 bg-f1-gray border border-f1-gray/50 rounded-lg text-white hover:border-f1-red transition-all flex items-center justify-between cursor-pointer"
        >
          <span className={selectedDriver ? 'text-white' : 'text-f1-lightGray'}>
            {selectedDriver 
              ? `#${drivers.findIndex(d => d.id === selectedDriver.id) + 1} - ${selectedDriver.firstName} ${selectedDriver.lastName}`
              : 'Select a driver...'}
          </span>
          <svg
            className={`w-5 h-5 text-f1-red transition-transform ${isOpen ? 'rotate-180' : ''}`}
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
          </svg>
        </button>

        {isOpen && (
          <>
            <div 
              className="fixed inset-0 z-10" 
              onClick={() => setIsOpen(false)}
            />
            <div className="absolute z-20 w-full md:w-80 mt-2 bg-f1-darkGray border border-f1-gray/50 rounded-lg shadow-2xl max-h-96 overflow-hidden">
              <div className="p-3 border-b border-f1-gray/50">
                <input
                  type="text"
                  placeholder="Search drivers..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full px-3 py-2 bg-f1-gray border border-f1-gray/50 rounded text-white placeholder-f1-lightGray focus:outline-none focus:border-f1-red"
                  autoFocus
                />
              </div>
              <div className="overflow-y-auto max-h-80">
                {filteredDrivers.length > 0 ? (
                  filteredDrivers.map((driver) => {
                    const isSelected = selectedDriver?.id === driver.id
                    const originalIndex = drivers.findIndex(d => d.id === driver.id) + 1
                    return (
                      <button
                        key={driver.id}
                        onClick={() => handleSelect(driver)}
                        className={`w-full px-4 py-3 text-left hover:bg-f1-gray transition-colors border-b border-f1-gray/30 last:border-b-0 ${
                          isSelected ? 'bg-f1-red/20 border-l-4 border-l-f1-red' : ''
                        }`}
                      >
                        <div className="flex items-center justify-between">
                          <div>
                            <div className="text-white font-medium">
                              #{originalIndex} - {driver.firstName} {driver.lastName}
                            </div>
                            <div className="text-f1-lightGray text-sm mt-1">
                              DGI: {driver.dgi.toFixed(4)} • Wins: {driver.wins} • Championships: {driver.championships}
                            </div>
                          </div>
                          {isSelected && (
                            <svg className="w-5 h-5 text-f1-red ml-2" fill="currentColor" viewBox="0 0 20 20">
                              <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                            </svg>
                          )}
                        </div>
                      </button>
                    )
                  })
                ) : (
                  <div className="px-4 py-8 text-center text-f1-lightGray">
                    No drivers found
                  </div>
                )}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  )
}

const SettingsDashboard = () => {
  const [originalDgi, setOriginalDgi] = useState([])
  const [dgi, setDgi] = useState([])
  const [selectedDriver, setSelectedDriver] = useState(null)
  const [loading, setLoading] = useState(true)
  const [calculating, setCalculating] = useState(false)
  const [progress, setProgress] = useState(0)

  // Default weights
  const defaultWeights = {
    teammateDominance: 0.25,
    podiumPercentage: 0.20,
    nonPolePoints: 0.20,
    poles: 0.15,
    championships: 0.10,
    careerLength: 0.05,
    numConstructors: 0.05,
  }

  const [weights, setWeights] = useState(() => {
    // Load from localStorage or use defaults
    const saved = localStorage.getItem('dgiWeights')
    if (saved) {
      try {
        return JSON.parse(saved)
      } catch {
        return defaultWeights
      }
    }
    return defaultWeights
  })

  useEffect(() => {
    try {
      const data = dgiData
      setOriginalDgi([...data])
      // If we have custom weights, recalculate
      const saved = localStorage.getItem('dgiWeights')
      if (saved) {
        const customWeights = JSON.parse(saved)
        if (JSON.stringify(customWeights) !== JSON.stringify(defaultWeights)) {
          recalculateDGI(data, customWeights)
        } else {
          setDgi([...data])
        }
      } else {
        setDgi([...data])
      }
      if (data && data.length > 0) {
        setSelectedDriver(data[0])
      }
    } catch (error) {
      console.error('Error loading DGI data:', error)
    } finally {
      setLoading(false)
    }
  }, [])

  const handleWeightChange = (metric, value) => {
    const numValue = parseFloat(value) / 100 // Convert percentage to decimal
    if (isNaN(numValue) || numValue < 0 || numValue > 1) return
    
    setWeights(prev => ({
      ...prev,
      [metric]: numValue
    }))
  }

  const normalizeWeights = (inputWeights) => {
    // Normalize weights so they sum to 1.0
    const total = Object.values(inputWeights).reduce((sum, val) => sum + val, 0)
    if (total === 0) return inputWeights
    
    const normalized = {}
    for (const key in inputWeights) {
      normalized[key] = inputWeights[key] / total
    }
    return normalized
  }

  const recalculateDGI = async (data, customWeights = null) => {
    setCalculating(true)
    setProgress(0)

    const weightsToUse = customWeights || weights
    const normalizedWeights = normalizeWeights(weightsToUse)

    // Simulate progress with car animation
    const progressInterval = setInterval(() => {
      setProgress(prev => {
        if (prev >= 90) return prev
        return prev + Math.random() * 15
      })
    }, 50)

    // Use setTimeout to allow UI to update
    setTimeout(() => {
      // Find min/max for normalization
      const metricsData = data.map(driver => ({
        teammateDominance: driver.teammateDominance,
        podiumPercentage: driver.podiumPercentage,
        nonPolePoints: driver.nonPolePoints,
        poles: driver.poles,
        championships: driver.championships,
        careerLength: driver.careerLength,
        numConstructors: driver.numConstructors,
      }))

      const maxValues = {
        teammateDominance: Math.max(...metricsData.map(m => m.teammateDominance)),
        podiumPercentage: Math.max(...metricsData.map(m => m.podiumPercentage)),
        nonPolePoints: Math.max(...metricsData.map(m => m.nonPolePoints)),
        poles: Math.max(...metricsData.map(m => m.poles)),
        championships: Math.max(...metricsData.map(m => m.championships)),
        careerLength: Math.max(...metricsData.map(m => m.careerLength)),
        numConstructors: Math.max(...metricsData.map(m => m.numConstructors)),
      }

      const minValues = {
        teammateDominance: Math.min(...metricsData.map(m => m.teammateDominance)),
        podiumPercentage: Math.min(...metricsData.map(m => m.podiumPercentage)),
        nonPolePoints: Math.min(...metricsData.map(m => m.nonPolePoints)),
        poles: Math.min(...metricsData.map(m => m.poles)),
        championships: Math.min(...metricsData.map(m => m.championships)),
        careerLength: Math.min(...metricsData.map(m => m.careerLength)),
        numConstructors: Math.min(...metricsData.map(m => m.numConstructors)),
      }

      // Recalculate DGI for each driver
      const recalculated = data.map(driver => {
        const normalized = {}
        for (const metric of ['teammateDominance', 'podiumPercentage', 'nonPolePoints', 'poles', 'championships', 'careerLength', 'numConstructors']) {
          const value = driver[metric]
          const min = minValues[metric]
          const max = maxValues[metric]
          const range = max - min
          normalized[metric] = range > 0 ? (value - min) / range : 0
        }

        // Calculate DGI with custom weights
        const dgiScore = 
          normalized.teammateDominance * normalizedWeights.teammateDominance +
          normalized.podiumPercentage * normalizedWeights.podiumPercentage +
          normalized.nonPolePoints * normalizedWeights.nonPolePoints +
          normalized.poles * normalizedWeights.poles +
          normalized.championships * normalizedWeights.championships +
          normalized.careerLength * normalizedWeights.careerLength +
          normalized.numConstructors * normalizedWeights.numConstructors

        return {
          ...driver,
          normalizedMetrics: normalized,
          dgi: round(dgiScore, 4)
        }
      })

      // Sort by new DGI
      recalculated.sort((a, b) => b.dgi - a.dgi)

      clearInterval(progressInterval)
      setProgress(100)

      setTimeout(() => {
        setDgi(recalculated)
        if (selectedDriver) {
          const newSelected = recalculated.find(d => d.id === selectedDriver.id)
          if (newSelected) setSelectedDriver(newSelected)
        }
        setCalculating(false)
        setProgress(0)
      }, 200)
    }, 500)
  }

  const round = (num, decimals) => {
    return Math.round(num * Math.pow(10, decimals)) / Math.pow(10, decimals)
  }

  const handleRecalculate = () => {
    const normalizedWeights = normalizeWeights(weights)
    localStorage.setItem('dgiWeights', JSON.stringify(normalizedWeights))
    recalculateDGI(originalDgi, normalizedWeights)
  }

  const handleReset = () => {
    setWeights(defaultWeights)
    localStorage.removeItem('dgiWeights')
    recalculateDGI(originalDgi, defaultWeights)
  }

  // Prepare top 30 drivers for visualization
  const topDrivers = React.useMemo(() => {
    if (!dgi || dgi.length === 0) return []
    
    const drivers = dgi.slice(0, 30).map(driver => {
      const dgiValue = typeof driver.dgi === 'number' ? driver.dgi : parseFloat(driver.dgi) || 0
      const fullName = `${driver.firstName || ''} ${driver.lastName || ''}`.trim()
      return {
        name: fullName,
        fullName: fullName,
        dgi: dgiValue,
        wins: driver.wins || 0,
        championships: driver.championships || 0,
        poles: driver.poles || 0,
        podiums: driver.podiums || 0,
        teammateDominance: driver.teammateDominance || 0,
        podiumPercentage: driver.podiumPercentage || 0,
        driverId: driver.id
      }
    }).filter(d => d.dgi > 0)
    
    return drivers.reverse()
  }, [dgi])

  // Prepare radar chart data
  const radarData = React.useMemo(() => {
    if (!selectedDriver) return []
    
    const normalized = selectedDriver.normalizedMetrics || {}
    return [
      { metric: 'Teammate\nDominance', value: normalized.teammateDominance || 0, weight: `${(weights.teammateDominance * 100).toFixed(0)}%` },
      { metric: 'Podium %', value: normalized.podiumPercentage || 0, weight: `${(weights.podiumPercentage * 100).toFixed(0)}%` },
      { metric: 'Non-Pole\nWins', value: normalized.nonPolePoints || 0, weight: `${(weights.nonPolePoints * 100).toFixed(0)}%` },
      { metric: 'Pole\nPositions', value: normalized.poles || 0, weight: `${(weights.poles * 100).toFixed(0)}%` },
      { metric: 'Championships', value: normalized.championships || 0, weight: `${(weights.championships * 100).toFixed(0)}%` },
      { metric: 'Longevity', value: (normalized.careerLength + normalized.numConstructors) / 2 || 0, weight: `${((weights.careerLength + weights.numConstructors) * 100).toFixed(0)}%` },
    ]
  }, [selectedDriver, weights])

  const totalWeight = Object.values(weights).reduce((sum, val) => sum + val, 0)
  const weightError = Math.abs(totalWeight - 1.0) > 0.01

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-f1-red border-solid mx-auto mb-4"></div>
          <p className="text-f1-lightGray">Loading Driver Greatness Index...</p>
        </div>
      </div>
    )
  }

  const topDriver = dgi[0]

  return (
    <div className="space-y-8">
      {/* Page Title */}
      <div className="dashboard-header">
        <h1 className="text-4xl font-bold">⚙️ CUSTOMIZE DGI</h1>
        <p className="text-f1-lightGray text-base mt-2">
          Adjust the weights for each metric and recalculate Driver Greatness Index scores
        </p>
      </div>

      {/* Weight Configuration */}
      <div className="dashboard-card">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-f1-red">DGI Weight Configuration</h2>
          <div className="flex gap-3">
            <button
              onClick={handleReset}
              className="px-4 py-2 bg-f1-gray border border-f1-gray/50 rounded-lg text-white hover:border-f1-red transition-all"
            >
              Reset to Defaults
            </button>
            <button
              onClick={handleRecalculate}
              disabled={calculating || weightError}
              className={`px-6 py-2 rounded-lg font-medium transition-all ${
                calculating || weightError
                  ? 'bg-f1-gray/50 text-f1-lightGray cursor-not-allowed'
                  : 'bg-f1-red text-white hover:bg-red-700'
              }`}
            >
              {calculating ? 'Calculating...' : 'Re-calculate DGI'}
            </button>
          </div>
        </div>

        {weightError && (
          <div className="mb-4 p-3 bg-yellow-500/20 border border-yellow-500/50 rounded-lg text-yellow-300 text-sm">
            ⚠️ Weights must sum to 100% (Current: {(totalWeight * 100).toFixed(1)}%)
          </div>
        )}

        {/* Progress Indicator */}
        {calculating && (
          <div className="mb-6 bg-f1-gray p-6 rounded-lg border border-f1-red/30">
            <div className="flex flex-col gap-3">
              <div className="flex items-center justify-between">
                <span className="text-white font-medium">Recalculating DGI scores...</span>
                <span className="text-f1-red font-bold text-lg">{Math.round(progress)}%</span>
              </div>
              <div className="relative bg-f1-darkGray rounded-full h-4 overflow-hidden">
                <div 
                  className="h-full bg-gradient-to-r from-f1-red to-red-600 transition-all duration-300 relative"
                  style={{ width: `${progress}%` }}
                >
                  <div 
                    className="absolute right-0 top-1/2 -translate-y-1/2 text-2xl transition-all duration-300"
                    style={{ transform: `translateY(-50%)` }}
                  >
                    🏎️
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {/* Teammate Dominance */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Teammate Dominance
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={weights.teammateDominance * 100}
              onChange={(e) => handleWeightChange('teammateDominance', e.target.value)}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round(weights.teammateDominance * 100, 1)}
                onChange={(e) => handleWeightChange('teammateDominance', e.target.value)}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
          </div>

          {/* Podium Percentage */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Podium Percentage
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={weights.podiumPercentage * 100}
              onChange={(e) => handleWeightChange('podiumPercentage', e.target.value)}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round(weights.podiumPercentage * 100, 1)}
                onChange={(e) => handleWeightChange('podiumPercentage', e.target.value)}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
          </div>

          {/* Non-Pole Wins */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Wins from Non-Pole
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={weights.nonPolePoints * 100}
              onChange={(e) => handleWeightChange('nonPolePoints', e.target.value)}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round(weights.nonPolePoints * 100, 1)}
                onChange={(e) => handleWeightChange('nonPolePoints', e.target.value)}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
          </div>

          {/* Pole Positions */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Pole Positions
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={weights.poles * 100}
              onChange={(e) => handleWeightChange('poles', e.target.value)}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round(weights.poles * 100, 1)}
                onChange={(e) => handleWeightChange('poles', e.target.value)}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
          </div>

          {/* Championships */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Championships
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={weights.championships * 100}
              onChange={(e) => handleWeightChange('championships', e.target.value)}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round(weights.championships * 100, 1)}
                onChange={(e) => handleWeightChange('championships', e.target.value)}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
          </div>

          {/* Longevity */}
          <div className="bg-f1-gray p-4 rounded-lg">
            <label className="block text-white font-medium mb-3">
              Longevity & Versatility
            </label>
            <input
              type="range"
              min="0"
              max="100"
              value={(weights.careerLength + weights.numConstructors) * 100}
              onChange={(e) => {
                const total = parseFloat(e.target.value) / 100
                const split = total / 2
                setWeights(prev => ({
                  ...prev,
                  careerLength: split,
                  numConstructors: split
                }))
              }}
              className="w-full mb-2"
            />
            <div className="flex items-center justify-between">
              <input
                type="number"
                min="0"
                max="100"
                value={round((weights.careerLength + weights.numConstructors) * 100, 1)}
                onChange={(e) => {
                  const total = parseFloat(e.target.value) / 100
                  const split = total / 2
                  setWeights(prev => ({
                    ...prev,
                    careerLength: split,
                    numConstructors: split
                  }))
                }}
                className="w-20 px-2 py-1 bg-f1-darkGray border border-f1-gray/50 rounded text-white text-sm"
              />
              <span className="text-f1-lightGray text-sm">%</span>
            </div>
            <p className="text-f1-lightGray text-xs mt-2">
              (Career Length + Num Constructors)
            </p>
          </div>
        </div>

        <div className="mt-4 p-3 bg-f1-darkGray rounded-lg">
          <div className="flex items-center justify-between">
            <span className="text-f1-lightGray">Total Weight:</span>
            <span className={`font-bold ${weightError ? 'text-yellow-400' : 'text-green-400'}`}>
              {(totalWeight * 100).toFixed(1)}%
            </span>
          </div>
        </div>
      </div>

      {/* Key Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <StatCard
          title={selectedDriver ? "Rank" : "Drivers Analyzed"}
          value={selectedDriver ? `#${dgi.findIndex(d => d.id === selectedDriver.id) + 1}` : dgi.length}
          subtitle={selectedDriver ? "Current ranking" : "Total drivers with DGI scores"}
          delay={0}
        />
        <StatCard
          title={selectedDriver ? "DGI Score" : "Top DGI Score"}
          value={selectedDriver ? selectedDriver.dgi.toFixed(4) : (topDriver?.dgi.toFixed(4) || '0.0000')}
          subtitle={selectedDriver ? "Driver's DGI" : `${topDriver?.firstName} ${topDriver?.lastName}` || ''}
          delay={100}
        />
        <StatCard
          title={selectedDriver ? "Career Length" : "Avg Career Length"}
          value={selectedDriver ? selectedDriver.careerLength : (dgi.length > 0 ? Math.round(dgi.reduce((sum, d) => sum + d.careerLength, 0) / dgi.length) : 0)}
          subtitle={selectedDriver ? "Seasons active" : "Seasons per driver"}
          delay={200}
        />
        <StatCard
          title={selectedDriver ? "Championships" : "Total Championships"}
          value={selectedDriver ? selectedDriver.championships : dgi.reduce((sum, d) => sum + d.championships, 0)}
          subtitle={selectedDriver ? "World titles won" : "Combined titles"}
          delay={300}
        />
      </div>

      {/* Driver Selection */}
      <div className="dashboard-card">
        <CustomDriverSelect
          drivers={dgi.slice(0, 30)}
          selectedDriver={selectedDriver}
          onSelect={setSelectedDriver}
        />
        {selectedDriver && (
          <div className="bg-gradient-to-r from-f1-red/20 to-transparent border-2 border-f1-red rounded-lg p-6">
            <div className="flex items-center justify-between">
              <div className="flex-1">
                <div className="text-f1-lightGray text-sm mb-2">
                  #{dgi.findIndex(d => d.id === selectedDriver.id) + 1} DRIVER BY DGI
                </div>
                <h2 className="text-3xl font-bold text-white mb-2">
                  {selectedDriver.firstName} {selectedDriver.lastName}
                </h2>
                <div className="text-f1-red text-4xl font-bold mb-4">
                  DGI: {selectedDriver.dgi.toFixed(4)}
                </div>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
                  <div>
                    <div className="text-f1-lightGray text-xs">Wins</div>
                    <div className="text-white text-xl font-bold">{selectedDriver.wins}</div>
                  </div>
                  <div>
                    <div className="text-f1-lightGray text-xs">Championships</div>
                    <div className="text-white text-xl font-bold">{selectedDriver.championships}</div>
                  </div>
                  <div>
                    <div className="text-f1-lightGray text-xs">Poles</div>
                    <div className="text-white text-xl font-bold">{selectedDriver.poles}</div>
                  </div>
                  <div>
                    <div className="text-f1-lightGray text-xs">Teammate Dominance</div>
                    <div className="text-white text-xl font-bold">{selectedDriver.teammateDominance.toFixed(1)}%</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Top 30 Drivers Chart */}
      <div className="dashboard-card">
        <h2 className="text-xl font-bold mb-4 text-f1-red">Top 30 Drivers by Custom DGI</h2>
        {topDrivers && topDrivers.length > 0 ? (
          <ResponsiveContainer width="100%" height={600}>
            <BarChart 
              data={topDrivers} 
              layout="vertical"
              margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
              <XAxis
                type="number"
                domain={['dataMin', 'dataMax']}
                stroke="#949498"
                tick={{ fill: '#949498', fontSize: 12 }}
                allowDecimals={true}
              />
              <YAxis
                type="category"
                dataKey="name"
                stroke="#949498"
                tick={{ fill: '#949498', fontSize: 11 }}
                width={140}
                interval={0}
              />
              <Tooltip content={<DGITooltip />} />
              <Bar
                dataKey="dgi"
                radius={[0, 8, 8, 0]}
                minPointSize={2}
                onClick={(data) => {
                  const driver = dgi.find(d => d.id === data.driverId)
                  if (driver) setSelectedDriver(driver)
                }}
                style={{ cursor: 'pointer' }}
              >
                {topDrivers.map((entry, index) => {
                  const isSelected = selectedDriver?.id === entry.driverId
                  return (
                    <Cell 
                      key={`cell-${index}`} 
                      fill={isSelected ? '#FFD700' : '#E10600'} 
                    />
                  )
                })}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <div className="text-center py-20 text-f1-lightGray">
            No driver data available
          </div>
        )}
      </div>

      {/* Driver Detail View */}
      {selectedDriver && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Radar Chart */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red">
              {selectedDriver.firstName} {selectedDriver.lastName} - Metric Breakdown
            </h2>
            <ResponsiveContainer width="100%" height={400}>
              <RadarChart data={radarData}>
                <PolarGrid stroke="#38383F" />
                <PolarAngleAxis
                  dataKey="metric"
                  tick={{ fill: '#949498', fontSize: 11 }}
                />
                <PolarRadiusAxis
                  angle={90}
                  domain={[0, 1]}
                  tick={{ fill: '#949498', fontSize: 10 }}
                />
                <Radar
                  name="Normalized Score"
                  dataKey="value"
                  stroke="#E10600"
                  fill="#E10600"
                  fillOpacity={0.6}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px',
                    color: '#fff'
                  }}
                  formatter={(value, name, props) => {
                    const weight = props.payload.weight
                    return [`${(value * 100).toFixed(1)}% (Weight: ${weight})`, 'Normalized Score']
                  }}
                />
              </RadarChart>
            </ResponsiveContainer>
          </div>

          {/* Driver Stats */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red">Detailed Statistics</h2>
            <div className="space-y-4">
              <div className="bg-f1-gray p-4 rounded-lg">
                <div className="text-2xl font-bold text-f1-red mb-2">DGI Score: {selectedDriver.dgi.toFixed(4)}</div>
                <div className="text-f1-lightGray text-sm">Rank: #{dgi.findIndex(d => d.id === selectedDriver.id) + 1}</div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Total Races</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.totalRaces}</div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Wins</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.wins}</div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Podiums</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.podiums}</div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Pole Positions</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.poles}</div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Championships</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.championships}</div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Non-Pole Wins</div>
                  <div className="text-white text-2xl font-bold">{selectedDriver.nonPoleWins}</div>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Teammate Dominance</div>
                  <div className="text-white text-xl font-bold">{selectedDriver.teammateDominance.toFixed(1)}%</div>
                  <div className="text-f1-lightGray text-xs mt-1">
                    {selectedDriver.teammateRaces} races with teammate
                  </div>
                </div>
                <div className="bg-f1-gray p-4 rounded-lg">
                  <div className="text-f1-lightGray text-sm mb-1">Podium Percentage</div>
                  <div className="text-white text-xl font-bold">{selectedDriver.podiumPercentage.toFixed(1)}%</div>
                </div>
              </div>

              <div className="bg-f1-gray p-4 rounded-lg">
                <div className="text-f1-lightGray text-sm mb-2">Career Information</div>
                <div className="text-white">
                  <div>Career Length: {selectedDriver.careerLength} seasons</div>
                  <div>Years Active: {selectedDriver.firstSeason} - {selectedDriver.lastSeason}</div>
                  <div>Constructors: {selectedDriver.numConstructors} teams</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default SettingsDashboard
