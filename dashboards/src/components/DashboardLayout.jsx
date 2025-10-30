import React from 'react'
import { Link, useLocation } from 'react-router-dom'

const DashboardLayout = ({ children }) => {
  const location = useLocation()

  const navItems = [
    { path: '/', label: 'General Dashboard' },
    { path: '/best-driver', label: 'Best Driver Analysis' }
  ]

  return (
    <div className="min-h-screen bg-f1-black">
      {/* Header */}
      <header className="bg-f1-darkGray border-b border-f1-gray">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <h1 className="text-2xl font-bold bg-gradient-to-r from-f1-red to-red-700 bg-clip-text text-transparent">
                F1 ANALYTICS
              </h1>
            </div>
            <nav className="flex space-x-1">
              {navItems.map((item) => (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`px-4 py-2 rounded-lg transition-all duration-300 ${
                    location.pathname === item.path
                      ? 'bg-f1-red text-white'
                      : 'text-f1-lightGray hover:text-white hover:bg-f1-gray'
                  }`}
                >
                  {item.label}
                </Link>
              ))}
            </nav>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-6 py-8">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-f1-darkGray border-t border-f1-gray mt-12">
        <div className="container mx-auto px-6 py-4 text-center text-f1-lightGray text-sm">
          <p>Powered by F1DB • Data updated through 2025 Season</p>
        </div>
      </footer>
    </div>
  )
}

export default DashboardLayout
