import { useLocation } from 'react-router-dom'

/**
 * Structured Data Component - Adds JSON-LD schema markup for AI models
 * This helps AI crawlers like Perplexity, ChatGPT, Claude understand your content
 */
const StructuredData = () => {
  const location = useLocation()
  const baseUrl = typeof window !== 'undefined' 
    ? `${window.location.protocol}//${window.location.host}`
    : 'https://f1db-dashboard.vercel.app'

  // Base website schema
  const websiteSchema = {
    "@context": "https://schema.org",
    "@type": "WebSite",
    "name": "F1 Analytics",
    "url": baseUrl,
    "description": "Comprehensive Formula 1 analytics including driver statistics, championship history, and the Driver Greatness Index (DGI) - a composite metric ranking the greatest F1 drivers of all time.",
    "author": {
      "@type": "Person",
      "name": "Jason Cozy",
      "url": "https://www.jasoncozy.com"
    },
    "publisher": {
      "@type": "Person",
      "name": "Jason Cozy"
    },
    "potentialAction": {
      "@type": "SearchAction",
      "target": {
        "@type": "EntryPoint",
        "urlTemplate": `${baseUrl}/?search={search_term_string}`
      },
      "query-input": "required name=search_term_string"
    }
  }

  // Page-specific schemas
  const getPageSchema = () => {
    switch (location.pathname) {
      case '/':
        return {
          "@context": "https://schema.org",
          "@type": "WebPage",
          "name": "F1 Analytics - General Dashboard",
          "url": `${baseUrl}/`,
          "description": "Explore current Formula 1 season statistics, championship standings, driver and constructor performance, and comprehensive race data for all F1 seasons.",
          "isPartOf": {
            "@type": "WebSite",
            "name": "F1 Analytics",
            "url": baseUrl
          }
        }
      
      case '/best-driver':
        return {
          "@context": "https://schema.org",
          "@type": "WebPage",
          "name": "Driver Greatness Index | Best F1 Drivers of All Time",
          "url": `${baseUrl}/best-driver`,
          "description": "Discover the greatest Formula 1 drivers ranked by the Driver Greatness Index (DGI), a composite metric analyzing teammate dominance, podium percentages, championships, pole positions, and longevity.",
          "isPartOf": {
            "@type": "WebSite",
            "name": "F1 Analytics",
            "url": baseUrl
          },
          "about": {
            "@type": "Thing",
            "name": "Formula 1 Driver Rankings",
            "description": "Ranking system for Formula 1 drivers based on comprehensive performance metrics"
          }
        }
      
      case '/about':
        return {
          "@context": "https://schema.org",
          "@type": "Article",
          "headline": "About F1 Analytics - Data Sources & DGI Methodology",
          "url": `${baseUrl}/about`,
          "description": "Learn about the F1 Analytics project, data sources from F1DB, and the detailed methodology behind the Driver Greatness Index (DGI) calculation.",
          "author": {
            "@type": "Person",
            "name": "Jason Cozy",
            "url": "https://www.jasoncozy.com"
          },
          "publisher": {
            "@type": "Person",
            "name": "Jason Cozy"
          },
          "datePublished": "2025-01-01",
          "dateModified": "2025-01-15",
          "mainEntityOfPage": {
            "@type": "WebPage",
            "@id": `${baseUrl}/about`
          },
          "about": [
            {
              "@type": "Thing",
              "name": "Formula 1 Statistics",
              "description": "Comprehensive Formula 1 racing statistics and analytics"
            },
            {
              "@type": "Thing",
              "name": "Driver Greatness Index",
              "alternateName": "DGI",
              "description": "A composite metric for ranking Formula 1 drivers based on multiple performance indicators"
            }
          ]
        }
      
      case '/settings':
        return {
          "@context": "https://schema.org",
          "@type": "WebPage",
          "name": "Customize DGI Weights | F1 Analytics",
          "url": `${baseUrl}/settings`,
          "description": "Customize the Driver Greatness Index by adjusting metric weights. Recalculate DGI scores based on your preferences.",
          "isPartOf": {
            "@type": "WebSite",
            "name": "F1 Analytics",
            "url": baseUrl
          },
          "about": {
            "@type": "SoftwareApplication",
            "name": "DGI Calculator",
            "description": "Interactive tool for customizing Driver Greatness Index weights"
          }
        }
      
      default:
        return null
    }
  }

  // Dataset schema for the data we're displaying
  const datasetSchema = {
    "@context": "https://schema.org",
    "@type": "Dataset",
    "name": "Formula 1 Racing Statistics",
    "description": "Comprehensive Formula 1 racing data including driver statistics, race results, championship standings, and driver performance metrics from 1950 to present.",
    "url": baseUrl,
    "keywords": "Formula 1, F1, racing statistics, driver statistics, F1 data",
    "license": "https://github.com/f1db/f1db",
    "creator": {
      "@type": "Organization",
      "name": "F1DB",
      "url": "https://github.com/f1db"
    },
    "temporalCoverage": "1950/..",
    "spatialCoverage": "Worldwide",
    "distribution": [
      {
        "@type": "DataDownload",
        "contentUrl": "https://github.com/f1db/f1db",
        "encodingFormat": "YAML",
        "description": "Source data from F1DB"
      }
    ]
  }

  const schemas = [
    websiteSchema,
    getPageSchema(),
    datasetSchema
  ].filter(Boolean) // Remove null values

  return (
    <>
      {schemas.map((schema, index) => (
        <script
          key={index}
          type="application/ld+json"
          dangerouslySetInnerHTML={{ __html: JSON.stringify(schema, null, 2) }}
        />
      ))}
    </>
  )
}

export default StructuredData

