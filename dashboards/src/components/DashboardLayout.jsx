import React, { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'

const DashboardLayout = ({ children }) => {
  const location = useLocation()
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)

  const navItems = [
    { path: '/', label: 'General Dashboard' },
    { path: '/best-driver', label: 'Best Driver Analysis' },
    { path: '/settings', label: 'Customize' },
    { path: '/about', label: 'About' }
  ]

  return (
    <div className="min-h-screen bg-f1-black">
      {/* Header */}
      <header className="bg-f1-darkGray border-b border-f1-gray">
        <div className="container mx-auto px-4 sm:px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <h1 className="text-xl sm:text-2xl font-bold bg-gradient-to-r from-f1-red to-red-700 bg-clip-text text-transparent">
                F1 ANALYTICS
              </h1>
            </div>
            
            {/* Desktop Navigation */}
            <nav className="hidden md:flex space-x-1">
              {navItems.map((item) => (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`px-3 py-2 rounded-lg transition-all duration-300 text-sm ${
                    location.pathname === item.path
                      ? 'bg-f1-red text-white'
                      : 'text-f1-lightGray hover:text-white hover:bg-f1-gray'
                  }`}
                >
                  {item.label}
                </Link>
              ))}
            </nav>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="md:hidden p-2 text-f1-lightGray hover:text-white hover:bg-f1-gray rounded-lg transition-all"
              aria-label="Toggle menu"
            >
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                {isMobileMenuOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>

          {/* Mobile Navigation Menu */}
          {isMobileMenuOpen && (
            <nav className="md:hidden mt-4 pb-4 border-t border-f1-gray/50 pt-4">
              <div className="flex flex-col space-y-2">
                {navItems.map((item) => (
                  <Link
                    key={item.path}
                    to={item.path}
                    onClick={() => setIsMobileMenuOpen(false)}
                    className={`px-4 py-3 rounded-lg transition-all duration-300 ${
                      location.pathname === item.path
                        ? 'bg-f1-red text-white'
                        : 'text-f1-lightGray hover:text-white hover:bg-f1-gray'
                    }`}
                  >
                    {item.label}
                  </Link>
                ))}
              </div>
            </nav>
          )}
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 sm:px-6 py-6 sm:py-8">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-f1-darkGray border-t border-f1-gray mt-8 sm:mt-12">
        <div className="container mx-auto px-4 sm:px-6 py-4 text-center text-f1-lightGray text-xs sm:text-sm">
          <p className="flex flex-col sm:flex-row sm:items-center sm:justify-center gap-1 sm:gap-0">
            <span>Created by{' '}
              <a
                href="https://www.jasoncozy.com"
                target="_blank"
                rel="noopener noreferrer"
                className="text-f1-red hover:text-red-400 transition-colors font-medium"
              >
                Jason Cozy
              </a>
            </span>
            <span className="hidden sm:inline">{' • '}</span>
            <span>Powered by{' '}
              <a
                href="https://github.com/f1db"
                target="_blank"
                rel="noopener noreferrer"
                className="text-f1-red hover:text-red-400 transition-colors font-medium"
              >
                F1DB
              </a>
            </span>
            <span className="hidden sm:inline">{' • '}</span>
            <span>Data updated through 2025 Season</span>
          </p>
        </div>
      </footer>
    </div>
  )
}

export default DashboardLayout
