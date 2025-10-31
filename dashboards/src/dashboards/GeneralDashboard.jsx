import React, { useState, useEffect } from 'react'
import {
  LineChart, Line, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts'
import StatCard from '../components/StatCard'
import currentSeasonData from '../data/current-season.json'
import driverChampionshipsData from '../data/driver-championships.json'
import constructorChampionshipsData from '../data/constructor-championships.json'
import driverStatsData from '../data/driver-stats.json'

const GeneralDashboard = () => {
  const [currentSeason, setCurrentSeason] = useState(null)
  const [driverChampionships, setDriverChampionships] = useState([])
  const [constructorChampionships, setConstructorChampionships] = useState([])
  const [driverStats, setDriverStats] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Load data directly from imports
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
  }, [])

  // Create a map of driver IDs to names for quick lookup
  const driverNameMap = React.useMemo(() => {
    return new Map(
      driverStats.map(d => [d.id, `${d.firstName} ${d.lastName}`])
    )
  }, [driverStats])

  // Get top 10 drivers by wins
  const topDrivers = React.useMemo(() => {
    return driverStats
      .filter(d => d && d.totalWins > 0 && d.firstName && d.lastName)
      .slice(0, 10)
      .map(d => ({
        name: `${d.firstName.charAt(0)}. ${d.lastName}`,
        wins: Number(d.totalWins) || 0,
        poles: Number(d.totalPoles) || 0,
        championships: Number(d.totalChampionships) || 0
      }))
  }, [driverStats])

  // Enhance driver standings with names
  const driverStandingsWithNames = React.useMemo(() => {
    return currentSeason?.driverStandings?.map(standing => ({
      ...standing,
      driverName: driverNameMap.get(standing.driverId) || standing.driverId
    })) || []
  }, [currentSeason, driverNameMap])

  // Get championships trend (last 20 years) with driver names
  const recentChampionships = React.useMemo(() => {
    return driverChampionships.slice(-20).map(c => ({
      year: c.year,
      points: c.points,
      driverName: driverNameMap.get(c.driverId) || c.driverId
    }))
  }, [driverChampionships, driverNameMap])

  // State for expandable cards
  const [expandedCards, setExpandedCards] = useState({
    podiums: false,
    dnf: false,
    timePenalties: false,
    gridPenalties: false
  })

  const toggleCard = (cardName) => {
    setExpandedCards(prev => ({
      ...prev,
      [cardName]: !prev[cardName]
    }))
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-f1-red border-solid mx-auto mb-4"></div>
          <p className="text-f1-lightGray">Loading F1 data...</p>
        </div>
      </div>
    )
  }

  // Color scheme
  const COLORS = ['#E10600', '#FF6B6B', '#FFA500', '#FFD700', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DFE6E9', '#B2BEC3']

  return (
    <div className="space-y-8">
      {/* Page Title */}
      <div className="dashboard-header">
        <h1 className="text-4xl font-bold">F1 GENERAL DASHBOARD</h1>
        <p className="text-f1-lightGray text-base mt-2">
          Comprehensive Formula 1 statistics and insights
        </p>
      </div>

      {/* Top Stats */}
      {currentSeason && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCard
            title="Current Season"
            value={currentSeason.season}
            subtitle="Races Completed"
            delay={0}
          />
          <StatCard
            title="Total Races"
            value={currentSeason.totalRaces || 0}
            subtitle="This season"
            delay={100}
          />
          <StatCard
            title="Drivers"
            value={currentSeason.uniqueDrivers || currentSeason.driverStandings?.length || 0}
            subtitle="Competing"
            delay={200}
          />
          <StatCard
            title="Constructors"
            value={currentSeason.constructorStandings?.length || 0}
            subtitle="Racing teams"
            delay={300}
          />
        </div>
      )}

      {/* Current Season Standings */}
      {currentSeason && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Driver Standings */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red">Driver Standings {currentSeason.season}</h2>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={driverStandingsWithNames.slice(0, 10)}>
                <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
                <XAxis
                  dataKey="driverName"
                  stroke="#949498"
                  tick={{ fill: '#949498', fontSize: 11 }}
                  angle={-45}
                  textAnchor="end"
                  height={80}
                />
                <YAxis stroke="#949498" tick={{ fill: '#949498' }} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px'
                  }}
                  labelStyle={{ color: '#fff' }}
                  formatter={(value, name) => [value, name === 'points' ? 'Points' : name]}
                />
                <Bar dataKey="points" fill="#E10600" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Constructor Standings */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red flex items-center gap-2">
              Constructor Standings {currentSeason.season}
              {currentSeason.constructorStandings?.[0]?.position === 1 && (
                <span className="text-yellow-400">🏆</span>
              )}
            </h2>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={currentSeason.constructorStandings?.slice(0, 10)}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ constructorId, position }) =>
                    `${position === 1 ? '🏆 ' : ''}${constructorId}`
                  }
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="points"
                >
                  {currentSeason.constructorStandings?.slice(0, 10).map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px',
                    color: '#FFFFFF'
                  }}
                  labelStyle={{ color: '#FFFFFF', marginBottom: '5px', fontWeight: 'bold' }}
                  itemStyle={{ color: '#FFFFFF' }}
                  formatter={(value, name) => [`${value} points`, 'Points']}
                  labelFormatter={(label, payload) => {
                    const data = payload?.[0]?.payload;
                    return data ? `${data.constructorId} - Position ${data.position}` : label;
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      )}

      {/* All-Time Stats */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Top Drivers by Wins */}
        <div className="dashboard-card">
          <h2 className="text-xl font-bold mb-4 text-f1-red">Top 10 Drivers - All Time Wins</h2>
          {topDrivers && topDrivers.length > 0 ? (
            <ResponsiveContainer width="100%" height={400}>
              <BarChart 
                data={[...topDrivers].reverse()}
                margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
              >
                <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
                <XAxis 
                  type="category"
                  dataKey="name"
                  stroke="#949498"
                  tick={{ fill: '#949498', fontSize: 11 }}
                  angle={-45}
                  textAnchor="end"
                  height={80}
                />
                <YAxis 
                  type="number"
                  stroke="#949498"
                  tick={{ fill: '#949498', fontSize: 12 }}
                  domain={[0, 'dataMax']}
                  allowDecimals={false}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px',
                    color: '#fff'
                  }}
                  labelStyle={{ color: '#fff', marginBottom: '5px' }}
                  formatter={(value, name) => {
                    if (name === 'wins') return [`${value} wins`, 'Wins'];
                    if (name === 'poles') return [`${value} poles`, 'Pole Positions'];
                    return [value, name];
                  }}
                />
                <Legend 
                  wrapperStyle={{ paddingTop: '20px' }}
                  iconType="rect"
                  formatter={(value) => {
                    if (value === 'wins') return 'Race Wins';
                    if (value === 'poles') return 'Pole Positions';
                    return value;
                  }}
                />
                <Bar 
                  dataKey="wins" 
                  fill="#E10600"
                  radius={[8, 8, 0, 0]}
                  name="wins"
                />
                <Bar 
                  dataKey="poles" 
                  fill="#FFA500"
                  radius={[8, 8, 0, 0]}
                  name="poles"
                />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="flex items-center justify-center h-96 text-f1-lightGray">
              No data available (Found {driverStats.length} drivers, {driverStats.filter(d => d?.totalWins > 0).length} with wins)
            </div>
          )}
        </div>

          {/* Championship Points Trend */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red">Championship Winning Points (Last 20 Years)</h2>
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={recentChampionships}>
                <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
                <XAxis
                  dataKey="year"
                  stroke="#949498"
                  tick={{ fill: '#949498', fontSize: 11 }}
                />
                <YAxis stroke="#949498" tick={{ fill: '#949498' }} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px'
                  }}
                  labelStyle={{ color: '#fff' }}
                  formatter={(value, name, props) => [
                    `${props.payload.driverName}: ${value} points`,
                    name === 'points' ? 'Points' : name
                  ]}
                />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="points"
                  stroke="#E10600"
                  strokeWidth={3}
                  dot={{ fill: '#E10600', r: 4 }}
                  activeDot={{ r: 6 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
      </div>

      {/* Current Season Detailed Stats */}
      {currentSeason && (
        <div className="space-y-6">
          <div className="dashboard-header">
            <h2 className="text-3xl font-bold">2025 Season Statistics</h2>
            <p className="text-f1-lightGray text-base mt-2">
              Detailed performance metrics for the current season
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Podium Statistics */}
            <div className="dashboard-card">
              <button
                onClick={() => toggleCard('podiums')}
                className="w-full flex items-center justify-between mb-4 text-left hover:opacity-80 transition-opacity"
              >
                <h3 className="text-xl font-bold text-f1-red">Podium Positions</h3>
                <svg
                  className={`w-5 h-5 text-f1-red transition-transform ${expandedCards.podiums ? 'rotate-180' : ''}`}
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              {currentSeason.podiumStats && currentSeason.podiumStats.length > 0 ? (
                <div className="space-y-4">
                  <div className="text-sm text-f1-lightGray mb-3">
                    Most podiums by position this season
                  </div>
                  {(expandedCards.podiums ? currentSeason.podiumStats : currentSeason.podiumStats.slice(0, 5)).map((driver, idx) => {
                    const driverName = driverNameMap.get(driver.driverId) || driver.driverId;
                    return (
                      <div key={idx} className="bg-f1-gray p-3 rounded-lg border border-f1-gray/50">
                        <div className="flex items-center justify-between mb-2">
                          <span className="font-bold text-white">{driverName}</span>
                          <span className="text-f1-red font-bold">{driver.total} podiums</span>
                        </div>
                        <div className="flex gap-4 text-sm">
                          <div className="flex items-center gap-1">
                            <span className="text-yellow-400">🥇</span>
                            <span className="text-f1-lightGray">{driver['1st']}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <span className="text-gray-300">🥈</span>
                            <span className="text-f1-lightGray">{driver['2nd']}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <span className="text-orange-400">🥉</span>
                            <span className="text-f1-lightGray">{driver['3rd']}</span>
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              ) : (
                <div className="text-f1-lightGray text-center py-8">No podium data available</div>
              )}
            </div>

            {/* DNF Statistics */}
            <div className="dashboard-card">
              <button
                onClick={() => toggleCard('dnf')}
                className="w-full flex items-center justify-between mb-4 text-left hover:opacity-80 transition-opacity"
              >
                <h3 className="text-xl font-bold text-f1-red">Did Not Finish (DNF)</h3>
                <svg
                  className={`w-5 h-5 text-f1-red transition-transform ${expandedCards.dnf ? 'rotate-180' : ''}`}
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              {currentSeason.dnfStats && currentSeason.dnfStats.length > 0 ? (
                <div className="space-y-4">
                  <div className="text-sm text-f1-lightGray mb-3">
                    Drivers with most retirements this season
                  </div>
                  {(expandedCards.dnf ? currentSeason.dnfStats : currentSeason.dnfStats.slice(0, 5)).map((driver, idx) => {
                    const driverName = driverNameMap.get(driver.driverId) || driver.driverId;
                    const races = driver.races || [];
                    return (
                      <div 
                        key={idx} 
                        className="bg-f1-gray p-3 rounded-lg border border-f1-gray/50 hover:border-f1-red/50 transition-colors relative group"
                      >
                        <div className="flex items-center justify-between">
                          <span className="font-bold text-white">{driverName}</span>
                          <span className="text-f1-red font-bold text-lg">{driver.count} DNFs</span>
                        </div>
                        {races.length > 0 && (
                          <div className="absolute bottom-full left-0 mb-2 w-96 p-4 bg-f1-darkGray border-2 border-white/30 rounded-lg shadow-2xl opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-20">
                            <div className="text-sm font-bold text-f1-red mb-2">Races DNF'd:</div>
                            <div className="text-xs text-white space-y-2">
                              {races.map((raceEntry, raceIdx) => {
                                const raceName = typeof raceEntry === 'object' ? raceEntry.race : raceEntry;
                                const circuitName = typeof raceEntry === 'object' ? raceEntry.circuit : null;
                                return (
                                  <div key={raceIdx} className="flex flex-col gap-0.5">
                                    <div className="text-white">• {raceName}</div>
                                    {circuitName && (
                                      <div className="ml-2 text-white/70 text-[10px]">📍 {circuitName}</div>
                                    )}
                                  </div>
                                );
                              })}
                            </div>
                          </div>
                        )}
                      </div>
                    );
                  })}
                  {currentSeason.dnfStats.length === 0 && (
                    <div className="text-f1-lightGray text-center py-8">
                      No DNFs recorded this season
                    </div>
                  )}
                </div>
              ) : (
                <div className="text-f1-lightGray text-center py-8">No DNF data available</div>
              )}
            </div>

            {/* Time Penalties */}
            <div className="dashboard-card">
              <button
                onClick={() => toggleCard('timePenalties')}
                className="w-full flex items-center justify-between mb-4 text-left hover:opacity-80 transition-opacity"
              >
                <h3 className="text-xl font-bold text-f1-red">Time Penalties</h3>
                <svg
                  className={`w-5 h-5 text-f1-red transition-transform ${expandedCards.timePenalties ? 'rotate-180' : ''}`}
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              {currentSeason.timePenaltyStats && currentSeason.timePenaltyStats.length > 0 ? (
                <div className="space-y-4">
                  <div className="text-sm text-f1-lightGray mb-3">
                    Drivers with most time penalties this season (sorted by total time)
                  </div>
                  {(expandedCards.timePenalties ? currentSeason.timePenaltyStats : currentSeason.timePenaltyStats.slice(0, 5)).map((driver, idx) => {
                    const driverName = driverNameMap.get(driver.driverId) || driver.driverId;
                    const minutes = Math.floor(driver.totalSeconds / 60);
                    const seconds = (driver.totalSeconds % 60).toFixed(0);
                    const penalties = driver.penalties || [];
                    return (
                      <div 
                        key={idx} 
                        className="bg-f1-gray p-3 rounded-lg border border-f1-gray/50 hover:border-f1-red/50 transition-colors relative group"
                      >
                        <div className="flex items-center justify-between mb-1">
                          <span className="font-bold text-white">{driverName}</span>
                          <span className="text-f1-red font-bold">{driver.count} penalties</span>
                        </div>
                        <div className="text-sm text-f1-lightGray">
                          Total: {minutes > 0 ? `${minutes}m ` : ''}{seconds}s
                        </div>
                        {penalties.length > 0 && (
                          <div className="absolute bottom-full left-0 mb-2 w-96 p-4 bg-f1-darkGray border-2 border-white/30 rounded-lg shadow-2xl opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-20">
                            <div className="text-sm font-bold text-f1-red mb-2">Time Penalties:</div>
                            <div className="text-xs text-white space-y-2">
                              {penalties.map((penalty, pIdx) => {
                                const penaltyAmount = typeof penalty === 'object' ? penalty.amount : penalty;
                                // Format penalty amount (e.g., "10.000" -> "+10s", "5.000" -> "+5s")
                                let formattedAmount = penaltyAmount;
                                if (typeof penaltyAmount === 'string' && /^\d+\.?\d*$/.test(penaltyAmount.replace('+', '').replace('s', ''))) {
                                  const num = parseFloat(penaltyAmount);
                                  formattedAmount = `+${num}s`;
                                }
                                // Get full race name and circuit
                                const raceName = typeof penalty === 'object' ? penalty.race : 'Unknown race';
                                const circuitName = typeof penalty === 'object' ? penalty.circuit : null;
                                return (
                                  <div key={pIdx} className="flex flex-col gap-0.5">
                                    <div className="flex items-start gap-2">
                                      <span className="text-f1-red font-semibold min-w-[50px]">{formattedAmount}</span>
                                      <span className="text-white flex-1">{raceName}</span>
                                    </div>
                                    {circuitName && (
                                      <div className="ml-[58px] text-white/70 text-[10px]">📍 {circuitName}</div>
                                    )}
                                  </div>
                                );
                              })}
                            </div>
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              ) : (
                <div className="text-f1-lightGray text-center py-8">No time penalty data available</div>
              )}
            </div>

            {/* Grid Penalties */}
            <div className="dashboard-card">
              <button
                onClick={() => toggleCard('gridPenalties')}
                className="w-full flex items-center justify-between mb-4 text-left hover:opacity-80 transition-opacity"
              >
                <h3 className="text-xl font-bold text-f1-red">Grid Penalties</h3>
                <svg
                  className={`w-5 h-5 text-f1-red transition-transform ${expandedCards.gridPenalties ? 'rotate-180' : ''}`}
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              {currentSeason.gridPenaltyStats && currentSeason.gridPenaltyStats.length > 0 ? (
                <div className="space-y-4">
                  <div className="text-sm text-f1-lightGray mb-3">
                    Drivers with most grid penalties this season (sorted by total places)
                  </div>
                  {(expandedCards.gridPenalties ? currentSeason.gridPenaltyStats : currentSeason.gridPenaltyStats.slice(0, 5)).map((driver, idx) => {
                    const driverName = driverNameMap.get(driver.driverId) || driver.driverId;
                    const penalties = driver.penalties || [];
                    return (
                      <div 
                        key={idx} 
                        className="bg-f1-gray p-3 rounded-lg border border-f1-gray/50 hover:border-f1-red/50 transition-colors relative group"
                      >
                        <div className="flex items-center justify-between mb-1">
                          <span className="font-bold text-white">{driverName}</span>
                          <span className="text-f1-red font-bold">{driver.count} penalties</span>
                        </div>
                        <div className="text-sm text-f1-lightGray">
                          Total: {driver.totalPlaces} grid places
                        </div>
                        {penalties.length > 0 && (
                          <div className="absolute bottom-full left-0 mb-2 w-96 p-4 bg-f1-darkGray border-2 border-white/30 rounded-lg shadow-2xl opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-20">
                            <div className="text-sm font-bold text-f1-red mb-2">Grid Penalties:</div>
                            <div className="text-xs text-white space-y-2">
                              {penalties.map((penalty, pIdx) => {
                                const penaltyAmount = typeof penalty === 'object' ? penalty.amount : penalty;
                                // Get full race name and circuit
                                const raceName = typeof penalty === 'object' ? penalty.race : 'Unknown race';
                                const circuitName = typeof penalty === 'object' ? penalty.circuit : null;
                                return (
                                  <div key={pIdx} className="flex flex-col gap-0.5">
                                    <div className="flex items-start gap-2">
                                      <span className="text-f1-red font-semibold min-w-[60px]">{penaltyAmount}</span>
                                      <span className="text-white flex-1">{raceName}</span>
                                    </div>
                                    {circuitName && (
                                      <div className="ml-[68px] text-white/70 text-[10px]">📍 {circuitName}</div>
                                    )}
                                  </div>
                                );
                              })}
                            </div>
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              ) : (
                <div className="text-f1-lightGray text-center py-8">No grid penalty data available</div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Fun Stats */}
      <div className="dashboard-card">
        <h2 className="text-xl font-bold mb-6 text-f1-red">All-Time Records</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Most Wins */}
          <div className="bg-f1-gray p-6 rounded-lg border border-f1-red/30">
            <div className="text-f1-lightGray text-sm mb-2">Most Wins</div>
            <div className="text-2xl font-bold text-white mb-2">
              {driverStats[0]?.firstName} {driverStats[0]?.lastName}
            </div>
            <div className="text-f1-red text-3xl font-bold">
              {driverStats[0]?.totalWins || 0}
            </div>
            <div className="text-f1-lightGray text-sm mt-1">race victories</div>
          </div>
          {/* Most Poles */}
          {(() => {
            const sortedByPoles = [...driverStats].sort((a, b) => b.totalPoles - a.totalPoles);
            const topPoles = sortedByPoles[0];
            return (
              <div className="bg-f1-gray p-6 rounded-lg border border-f1-red/30">
                <div className="text-f1-lightGray text-sm mb-2">Most Pole Positions</div>
                <div className="text-2xl font-bold text-white mb-2">
                  {topPoles?.firstName} {topPoles?.lastName}
                </div>
                <div className="text-f1-red text-3xl font-bold">
                  {topPoles?.totalPoles || 0}
                </div>
                <div className="text-f1-lightGray text-sm mt-1">pole positions</div>
              </div>
            );
          })()}
          {/* Most Championships */}
          {(() => {
            const sortedByChamps = [...driverStats].sort((a, b) => b.totalChampionships - a.totalChampionships);
            const topChamps = sortedByChamps[0];
            return (
              <div className="bg-f1-gray p-6 rounded-lg border border-f1-red/30">
                <div className="text-f1-lightGray text-sm mb-2">Most Championships</div>
                <div className="text-2xl font-bold text-white mb-2">
                  {topChamps?.firstName} {topChamps?.lastName}
                </div>
                <div className="text-f1-red text-3xl font-bold">
                  {topChamps?.totalChampionships || 0}
                </div>
                <div className="text-f1-lightGray text-sm mt-1">world titles</div>
              </div>
            );
          })()}
        </div>
      </div>
    </div>
  )
}

export default GeneralDashboard
