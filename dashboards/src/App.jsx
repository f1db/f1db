import React from 'react'
import { Routes, Route } from 'react-router-dom'
import DashboardLayout from './components/DashboardLayout'
import GeneralDashboard from './dashboards/GeneralDashboard'
import BestDriverDashboard from './dashboards/BestDriverDashboard'

function App() {
  return (
    <DashboardLayout>
      <Routes>
        <Route path="/" element={<GeneralDashboard />} />
        <Route path="/best-driver" element={<BestDriverDashboard />} />
      </Routes>
    </DashboardLayout>
  )
}

export default App
