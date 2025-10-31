import React from 'react'
import SEO from '../components/SEO'

const AboutDashboard = () => {
  return (
    <>
      <SEO 
        title="About F1 Analytics | Data Sources & DGI Methodology"
        description="Learn about the F1 Analytics project, data sources from F1DB, and the detailed methodology behind the Driver Greatness Index (DGI) calculation. Discover how we rank the greatest Formula 1 drivers of all time."
        keywords="F1 analytics about, DGI methodology, F1 data sources, Formula 1 statistics methodology, Driver Greatness Index methodology, F1DB data"
      />
      <div className="space-y-8">
      {/* Page Title */}
      <div className="dashboard-header">
        <h1 className="text-4xl font-bold">ABOUT</h1>
        <p className="text-f1-lightGray text-base mt-2">
          Learn about this project, data sources, and the Driver Greatness Index methodology
        </p>
      </div>

      {/* Project Story */}
      <div className="dashboard-card">
        <h2 className="text-2xl font-bold mb-4 text-f1-red">The Project</h2>
        <div className="space-y-4 text-f1-lightGray leading-relaxed">
          <p>
            This dashboard is a fun experiment that emerged from conversations with friends about 
            how to determine who the greatest Formula 1 driver of all time truly is. The question 
            sparked countless debates: Is it purely about championships? Total wins? Consistency? 
            Or something more nuanced?
          </p>
          <p>
            After jotting down some initial ideas, I worked with ChatGPT to develop Jupyter notebooks 
            that could transform and analyze the raw Formula 1 data. These notebooks helped establish 
            the methodology for calculating various driver metrics and ultimately led to the creation 
            of the Driver Greatness Index (DGI).
          </p>
          <p>
            Once the analytical foundation was set, I used Cursor to bring my vision to life, 
            building out interactive dashboards that allow you to explore driver statistics, 
            compare performances, and dive deep into what makes a driver truly great in Formula 1.
          </p>
          <p className="text-white font-medium">
            This project represents a blend of statistical analysis, data visualization, and a passion 
            for Formula 1 racing history.
          </p>
          
          <div className="pt-4 mt-4 border-t border-f1-gray/50">
            <p className="text-f1-lightGray text-sm">
              Created by <span className="text-white font-medium">Jason Cozy</span>. 
              See more projects at{' '}
              <a
                href="https://www.jasoncozy.com"
                target="_blank"
                rel="noopener noreferrer"
                className="text-f1-red hover:text-red-400 transition-colors font-medium"
              >
                www.jasoncozy.com
              </a>
            </p>
          </div>
        </div>
      </div>

      {/* Data Sources */}
      <div className="dashboard-card">
        <h2 className="text-2xl font-bold mb-4 text-f1-red">Data Sources</h2>
        <div className="space-y-4">
          <div className="bg-f1-gray p-4 rounded-lg">
            <h3 className="text-xl font-bold text-white mb-2">F1DB</h3>
            <p className="text-f1-lightGray mb-3">
              All race data, driver statistics, and historical records come from F1DB, the most 
              comprehensive free open-source database with all-time Formula 1 racing data and statistics.
            </p>
            <a
              href="https://github.com/f1db/f1db"
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 text-f1-red hover:text-red-400 transition-colors font-medium"
            >
              Visit F1DB on GitHub
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
              </svg>
            </a>
          </div>
          <div className="text-f1-lightGray text-sm pt-2">
            <p className="mb-2">F1DB provides data including:</p>
            <ul className="list-disc list-inside space-y-1 ml-2">
              <li>All drivers, constructors, and circuits</li>
              <li>Complete race results from 1950 to present</li>
              <li>Qualifying results and starting grid positions</li>
              <li>Championship standings and points</li>
              <li>Fastest laps, pit stops, and penalties</li>
            </ul>
          </div>
          
          <div className="mt-4 p-3 bg-yellow-500/20 border border-yellow-500/50 rounded-lg text-yellow-300 text-sm">
            ⚠️ Data is not live. Updates require manual regeneration via Python scripts, so there may be delays in reflecting the latest race results.
          </div>
        </div>
      </div>

      {/* DGI Methodology */}
      <div className="dashboard-card">
        <h2 className="text-2xl font-bold mb-4 text-f1-red">Driver Greatness Index (DGI) Methodology</h2>
        <p className="text-f1-lightGray mb-6">
          The Driver Greatness Index is a composite metric designed to provide a comprehensive 
          evaluation of driver performance by combining multiple performance indicators. Each 
          metric is normalized (0-1 scale) and weighted according to its importance in 
          determining driver greatness.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {/* Metric Cards */}
          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Teammate Dominance</h3>
              <span className="text-f1-red font-bold text-lg">25%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Percentage of races where a driver finished ahead of their teammate(s) in the same car.
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> F1 teammates drive identical machinery, 
              making this the most direct measure of pure driver skill. A high percentage indicates consistent 
              performance relative to direct competition.
            </p>
          </div>

          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Podium Percentage</h3>
              <span className="text-f1-red font-bold text-lg">20%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Percentage of races finished in the top 3 positions (P1, P2, or P3).
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> Measures consistency and top-tier 
              performance. Drivers who regularly finish on the podium demonstrate sustained excellence beyond 
              just race wins.
            </p>
          </div>

          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Wins from Non-Pole</h3>
              <span className="text-f1-red font-bold text-lg">20%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Weighted wins starting from grid positions other than P1. Wins from further back on the grid 
              receive higher weights (e.g., P2 = +1, P5 = +5).
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> Highlights a driver's ability to 
              overcome grid disadvantages through superior racecraft, strategy, and overtaking skills. 
              This rewards drivers who can win from challenging starting positions.
            </p>
          </div>

          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Pole Positions</h3>
              <span className="text-f1-red font-bold text-lg">15%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Total number of pole positions achieved throughout a driver's career.
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> Reflects raw speed and qualifying 
              mastery. Pole positions demonstrate a driver's ability to extract maximum performance in 
              single-lap qualifying sessions.
            </p>
          </div>

          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Championship Wins</h3>
              <span className="text-f1-red font-bold text-lg">10%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Total number of World Drivers' Championship titles won.
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> The ultimate measure of success 
              in Formula 1. While championships require both driver skill and team performance, they 
              represent sustained excellence over an entire season.
            </p>
          </div>

          <div className="bg-f1-gray p-5 rounded-lg border-l-4 border-f1-red">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-xl font-bold text-white">Longevity & Versatility</h3>
              <span className="text-f1-red font-bold text-lg">10%</span>
            </div>
            <p className="text-f1-lightGray mb-3">
              Combines career length (number of seasons) and number of different constructors driven for.
            </p>
            <p className="text-f1-lightGray text-sm">
              <strong className="text-white">Why it matters:</strong> Shows adaptability and sustained 
              excellence. Drivers who perform well across multiple teams and over many seasons demonstrate 
              true versatility and longevity in the sport.
            </p>
          </div>
        </div>

        {/* Calculation Process */}
        <div className="bg-f1-gray p-5 rounded-lg mt-6">
          <h3 className="text-xl font-bold text-white mb-4">Calculation Process</h3>
          <div className="space-y-3 text-f1-lightGray">
            <div className="flex gap-3">
              <span className="text-f1-red font-bold min-w-[30px]">1.</span>
              <p>
                <strong className="text-white">Data Collection:</strong> Process all race results, qualifying 
                sessions, and championship standings from the F1DB database.
              </p>
            </div>
            <div className="flex gap-3">
              <span className="text-f1-red font-bold min-w-[30px]">2.</span>
              <p>
                <strong className="text-white">Metric Calculation:</strong> Calculate each of the six metrics 
                for every driver based on their complete career statistics.
              </p>
            </div>
            <div className="flex gap-3">
              <span className="text-f1-red font-bold min-w-[30px]">3.</span>
              <p>
                <strong className="text-white">Normalization:</strong> Normalize all metrics to a 0-1 scale 
                using min-max normalization. This ensures fair comparison across different metrics.
              </p>
            </div>
            <div className="flex gap-3">
              <span className="text-f1-red font-bold min-w-[30px]">4.</span>
              <p>
                <strong className="text-white">Weighted Combination:</strong> Multiply each normalized metric 
                by its assigned weight and sum them together to create the final DGI score.
              </p>
            </div>
            <div className="flex gap-3">
              <span className="text-f1-red font-bold min-w-[30px]">5.</span>
              <p>
                <strong className="text-white">Ranking:</strong> Drivers are ranked by their final DGI score, 
                with higher scores indicating greater overall driver greatness.
              </p>
            </div>
          </div>
          <div className="mt-4 p-4 bg-f1-darkGray rounded border border-f1-gray/50">
            <p className="text-white font-medium mb-2">Formula:</p>
            <p className="text-f1-lightGray font-mono text-sm">
              DGI = (Teammate Dominance × 0.25) + (Podium % × 0.20) + (Non-Pole Wins × 0.20) + 
              (Poles × 0.15) + (Championships × 0.10) + (Longevity × 0.10)
            </p>
          </div>
        </div>

        {/* Notes */}
        <div className="bg-f1-gray p-5 rounded-lg mt-6 border border-f1-red/30">
          <h3 className="text-xl font-bold text-white mb-3">Important Notes</h3>
          <ul className="space-y-2 text-f1-lightGray">
            <li className="flex gap-2">
              <span className="text-f1-red">•</span>
              <span>
                DGI scores are relative and normalized, meaning they reflect performance relative to all 
                drivers in the dataset, not absolute values.
              </span>
            </li>
            <li className="flex gap-2">
              <span className="text-f1-red">•</span>
              <span>
                The weights are subjective and based on the assumption that teammate comparisons are 
                the purest measure of driver skill. You may disagree with these weights, and that's okay!
              </span>
            </li>
            <li className="flex gap-2">
              <span className="text-f1-red">•</span>
              <span>
                DGI does not account for era-specific factors, car reliability, or team strategies. 
                It focuses purely on quantifiable performance metrics.
              </span>
            </li>
            <li className="flex gap-2">
              <span className="text-f1-red">•</span>
              <span>
                This is a fun analytical exercise, not a definitive answer to "who is the greatest?" 
                The beauty of Formula 1 is that greatness can be measured in many ways.
              </span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    </>
  )
}

export default AboutDashboard
