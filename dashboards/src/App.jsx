import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { Analytics } from '@vercel/analytics/react'
import DashboardLayout from './components/DashboardLayout'
import GeneralDashboard from './dashboards/GeneralDashboard'
import BestDriverDashboard from './dashboards/BestDriverDashboard'
import AboutDashboard from './dashboards/AboutDashboard'
import SettingsDashboard from './dashboards/SettingsDashboard'

function App() {
  return (
    <>
      <DashboardLayout>
        <Routes>
          <Route path="/" element={<GeneralDashboard />} />
          <Route path="/best-driver" element={<BestDriverDashboard />} />
          <Route path="/about" element={<AboutDashboard />} />
          <Route path="/settings" element={<SettingsDashboard />} />
        </Routes>
      </DashboardLayout>
      <Analytics />
    </>
  )
}

export default App
