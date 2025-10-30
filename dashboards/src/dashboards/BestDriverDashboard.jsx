import React from 'react'

const BestDriverDashboard = () => {
  return (
    <div className="space-y-8">
      {/* Page Title */}
      <div className="dashboard-header">
        <h1 className="text-4xl font-bold">BEST DRIVER OF ALL TIME</h1>
        <p className="text-f1-lightGray text-base mt-2">
          Advanced analytics to determine the greatest F1 driver in history
        </p>
      </div>

      {/* Coming Soon */}
      <div className="dashboard-card text-center py-20">
        <div className="text-6xl mb-6">🏆</div>
        <h2 className="text-3xl font-bold mb-4 text-f1-red">Coming Soon</h2>
        <p className="text-f1-lightGray text-lg max-w-2xl mx-auto">
          This dashboard will feature your custom formula for analyzing and ranking
          the greatest Formula 1 drivers of all time based on multiple performance metrics,
          era adjustments, and competitive analysis.
        </p>
        <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-4 max-w-4xl mx-auto">
          <div className="stat-card">
            <div className="text-f1-lightGray text-sm mb-2">Planned Metrics</div>
            <div className="text-xl font-bold">Championship Performance</div>
          </div>
          <div className="stat-card">
            <div className="text-f1-lightGray text-sm mb-2">Analysis Type</div>
            <div className="text-xl font-bold">Era-Adjusted Scoring</div>
          </div>
          <div className="stat-card">
            <div className="text-f1-lightGray text-sm mb-2">Data Points</div>
            <div className="text-xl font-bold">Comprehensive Stats</div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default BestDriverDashboard
