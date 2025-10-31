import React, { useState, useEffect } from 'react'
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
  ResponsiveContainer, RadarChart, PolarGrid, PolarAngleAxis,
  PolarRadiusAxis, Radar, Cell
} from 'recharts'
import StatCard from '../components/StatCard'
import dgiData from '../data/driver-greatness-index.json'

// Custom Tooltip Component for DGI Chart (exported for reuse)
export const DGITooltip = ({ active, payload }) => {
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

// Custom Driver Select Component
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
              {/* Search Input */}
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
              
              {/* Driver List */}
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

const BestDriverDashboard = () => {
  const [dgi, setDgi] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedDriver, setSelectedDriver] = useState(null)

  useEffect(() => {
    try {
      setDgi(dgiData)
      if (dgiData && dgiData.length > 0) {
        setSelectedDriver(dgiData[0])
      }
    } catch (error) {
      console.error('Error loading DGI data:', error)
    } finally {
      setLoading(false)
    }
  }, [])

  // Prepare top 30 drivers for visualization
  const topDrivers = React.useMemo(() => {
    if (!dgi || dgi.length === 0) return []
    
    const drivers = dgi.slice(0, 30).map(driver => {
      const dgiValue = typeof driver.dgi === 'number' ? driver.dgi : parseFloat(driver.dgi) || 0
      const fullName = `${driver.firstName || ''} ${driver.lastName || ''}`.trim()
      return {
        name: fullName, // Use full name instead of abbreviated
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
    }).filter(d => d.dgi > 0) // Filter out drivers with invalid DGI
    
    return drivers.reverse() // Reverse for horizontal bar chart (lowest at top, highest at bottom)
  }, [dgi])

  // Prepare radar chart data for selected driver
  const radarData = React.useMemo(() => {
    if (!selectedDriver) return []
    
    const normalized = selectedDriver.normalizedMetrics || {}
    return [
      { metric: 'Teammate\nDominance', value: normalized.teammateDominance || 0, weight: '25%' },
      { metric: 'Podium %', value: normalized.podiumPercentage || 0, weight: '20%' },
      { metric: 'Non-Pole\nWins', value: normalized.nonPolePoints || 0, weight: '20%' },
      { metric: 'Pole\nPositions', value: normalized.poles || 0, weight: '15%' },
      { metric: 'Championships', value: normalized.championships || 0, weight: '10%' },
      { metric: 'Longevity', value: (normalized.careerLength + normalized.numConstructors) / 2 || 0, weight: '10%' },
    ]
  }, [selectedDriver])

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

  if (!dgi || dgi.length === 0) {
    return (
      <div className="space-y-8">
        <div className="dashboard-header">
          <h1 className="text-4xl font-bold">BEST DRIVER OF ALL TIME</h1>
          <p className="text-f1-lightGray text-base mt-2">
            Advanced analytics to determine the greatest F1 driver in history
          </p>
        </div>
        <div className="dashboard-card text-center py-20">
          <div className="text-6xl mb-6">🏆</div>
          <h2 className="text-3xl font-bold mb-4 text-f1-red">No Data Available</h2>
          <p className="text-f1-lightGray text-lg max-w-2xl mx-auto">
            Please run the data generation script to calculate Driver Greatness Index.
          </p>
        </div>
      </div>
    )
  }

  const topDriver = dgi[0]

  return (
    <div className="space-y-8">
      {/* Page Title */}
      <div className="dashboard-header">
        <h1 className="text-4xl font-bold">BEST DRIVER OF ALL TIME</h1>
        <p className="text-f1-lightGray text-base mt-2">
          Driver Greatness Index (DGI) - A composite metric combining multiple performance indicators
        </p>
      </div>

      {/* Driver Selection and Top Driver Highlight */}
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

      {/* Main Visualization: Top 30 Drivers */}
      <div className="dashboard-card">
        <h2 className="text-xl font-bold mb-4 text-f1-red">Top 30 Drivers by Driver Greatness Index</h2>
        <p className="text-f1-lightGray text-sm mb-6">
          DGI combines teammate dominance (25%), podium percentage (20%), wins from non-pole (20%), 
          pole positions (15%), championships (10%), and longevity (10%).
        </p>
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

      {/* DGI Methodology Explanation */}
      <div className="dashboard-card">
        <h2 className="text-xl font-bold mb-4 text-f1-red">DGI Methodology</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
          <div className="space-y-3">
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Teammate Dominance (25%)</div>
              <div className="text-f1-lightGray">
                Percentage of races where driver finished ahead of teammate(s) in the same car.
                This is the most direct measure of driver skill.
              </div>
            </div>
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Podium Percentage (20%)</div>
              <div className="text-f1-lightGray">
                Percentage of races finished in the top 3 positions. Measures consistency and top-tier performance.
              </div>
            </div>
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Wins from Non-Pole (20%)</div>
              <div className="text-f1-lightGray">
                Weighted wins starting from grid positions other than P1. Highlights ability to overcome grid disadvantages.
              </div>
            </div>
          </div>
          <div className="space-y-3">
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Pole Positions (15%)</div>
              <div className="text-f1-lightGray">
                Total number of pole positions. Reflects raw speed and qualifying mastery.
              </div>
            </div>
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Championship Wins (10%)</div>
              <div className="text-f1-lightGray">
                Total number of world championships. Ultimate measure of success in F1.
              </div>
            </div>
            <div className="bg-f1-gray p-3 rounded-lg">
              <div className="font-bold text-f1-red mb-1">Longevity & Versatility (10%)</div>
              <div className="text-f1-lightGray">
                Career length (seasons) and number of constructors driven for. Shows adaptability and sustained excellence.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default BestDriverDashboard
