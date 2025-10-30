import React, { useState, useEffect } from 'react'
import {
  LineChart, Line, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts'
import StatCard from '../components/StatCard'

const GeneralDashboard = () => {
  const [currentSeason, setCurrentSeason] = useState(null)
  const [driverChampionships, setDriverChampionships] = useState([])
  const [constructorChampionships, setConstructorChampionships] = useState([])
  const [driverStats, setDriverStats] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Load data
    const loadData = async () => {
      try {
        const [seasonRes, driverChampRes, constructorChampRes, driverStatsRes] = await Promise.all([
          fetch('/src/data/current-season.json'),
          fetch('/src/data/driver-championships.json'),
          fetch('/src/data/constructor-championships.json'),
          fetch('/src/data/driver-stats.json')
        ])

        if (seasonRes.ok) setCurrentSeason(await seasonRes.json())
        if (driverChampRes.ok) setDriverChampionships(await driverChampRes.json())
        if (constructorChampRes.ok) setConstructorChampionships(await constructorChampRes.json())
        if (driverStatsRes.ok) setDriverStats(await driverStatsRes.json())
      } catch (error) {
        console.error('Error loading data:', error)
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [])

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

  // Get top 10 drivers by wins
  const topDrivers = driverStats
    .filter(d => d.totalWins > 0)
    .slice(0, 10)
    .map(d => ({
      name: `${d.firstName?.charAt(0)}. ${d.lastName}`,
      wins: d.totalWins,
      poles: d.totalPoles,
      championships: d.totalChampionships
    }))

  // Get championships trend (last 20 years)
  const recentChampionships = driverChampionships.slice(-20).map(c => ({
    year: c.year,
    points: c.points
  }))

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
            value={currentSeason.recentRaces?.length || 0}
            subtitle="This season"
            delay={100}
          />
          <StatCard
            title="Drivers"
            value={currentSeason.driverStandings?.length || 0}
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
              <BarChart data={currentSeason.driverStandings?.slice(0, 10)}>
                <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
                <XAxis
                  dataKey="driverId"
                  stroke="#949498"
                  tick={{ fill: '#949498', fontSize: 12 }}
                />
                <YAxis stroke="#949498" tick={{ fill: '#949498' }} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#1E1E28',
                    border: '1px solid #38383F',
                    borderRadius: '8px'
                  }}
                  labelStyle={{ color: '#fff' }}
                />
                <Bar dataKey="points" fill="#E10600" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Constructor Standings */}
          <div className="dashboard-card">
            <h2 className="text-xl font-bold mb-4 text-f1-red">Constructor Standings {currentSeason.season}</h2>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={currentSeason.constructorStandings?.slice(0, 10)}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ constructorId, percent }) =>
                    `${constructorId} ${(percent * 100).toFixed(0)}%`
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
                    borderRadius: '8px'
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
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={topDrivers} layout="horizontal">
              <CartesianGrid strokeDasharray="3 3" stroke="#38383F" />
              <XAxis type="number" stroke="#949498" tick={{ fill: '#949498' }} />
              <YAxis
                type="category"
                dataKey="name"
                stroke="#949498"
                tick={{ fill: '#949498', fontSize: 11 }}
                width={100}
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: '#1E1E28',
                  border: '1px solid #38383F',
                  borderRadius: '8px'
                }}
                labelStyle={{ color: '#fff' }}
              />
              <Legend />
              <Bar dataKey="wins" fill="#E10600" radius={[0, 8, 8, 0]} />
              <Bar dataKey="poles" fill="#FFA500" radius={[0, 8, 8, 0]} />
            </BarChart>
          </ResponsiveContainer>
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

      {/* Fun Stats */}
      <div className="dashboard-card">
        <h2 className="text-xl font-bold mb-6 text-f1-red">All-Time Records</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {driverStats.slice(0, 3).map((driver, idx) => (
            <div key={idx} className="bg-f1-gray p-4 rounded-lg border border-f1-red/30">
              <div className="text-f1-lightGray text-sm mb-1">
                {idx === 0 ? 'Most Wins' : idx === 1 ? 'Most Poles' : 'Most Championships'}
              </div>
              <div className="text-2xl font-bold text-white">
                {driver.firstName} {driver.lastName}
              </div>
              <div className="text-f1-red text-xl font-bold mt-2">
                {idx === 0 ? driver.totalWins : idx === 1 ? driver.totalPoles : driver.totalChampionships}
                {idx === 0 ? ' wins' : idx === 1 ? ' poles' : ' titles'}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default GeneralDashboard
