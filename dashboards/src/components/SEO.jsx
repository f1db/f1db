import { useEffect } from 'react'

/**
 * SEO Component - Updates meta tags dynamically for each page
 * Usage: <SEO title="Page Title" description="Page description" />
 */
const SEO = ({ 
  title = 'F1 Analytics - Formula 1 Driver Statistics & Driver Greatness Index',
  description = 'Explore comprehensive Formula 1 analytics including driver statistics, championship history, and the Driver Greatness Index (DGI) - a composite metric ranking the greatest F1 drivers of all time.',
  keywords = 'Formula 1, F1, F1 analytics, driver statistics, F1 statistics, Driver Greatness Index, DGI, Formula 1 drivers, F1 championships, F1 history',
  image = '/og-image.png',
  url = typeof window !== 'undefined' ? window.location.href : 'https://your-domain.com/',
  type = 'website'
}) => {
  useEffect(() => {
    // Update document title
    document.title = title
    
    // Update or create meta tags
    const updateMetaTag = (name, content, isProperty = false) => {
      const attribute = isProperty ? 'property' : 'name'
      let element = document.querySelector(`meta[${attribute}="${name}"]`)
      
      if (!element) {
        element = document.createElement('meta')
        element.setAttribute(attribute, name)
        document.getElementsByTagName('head')[0].appendChild(element)
      }
      
      element.setAttribute('content', content)
    }
    
    // Primary meta tags
    updateMetaTag('title', title)
    updateMetaTag('description', description)
    updateMetaTag('keywords', keywords)
    
    // Open Graph tags
    updateMetaTag('og:title', title, true)
    updateMetaTag('og:description', description, true)
    // Get base URL from current location or use default
    const baseUrl = typeof window !== 'undefined' 
      ? `${window.location.protocol}//${window.location.host}`
      : 'https://f1db-dashboard.vercel.app'
    
    const fullImageUrl = image.startsWith('http') ? image : `${baseUrl}${image}`
    
    updateMetaTag('og:image', fullImageUrl, true)
    updateMetaTag('og:url', url, true)
    updateMetaTag('og:type', type, true)
    
    // Twitter Card tags
    updateMetaTag('twitter:title', title)
    updateMetaTag('twitter:description', description)
    updateMetaTag('twitter:image', fullImageUrl)
    
    // Canonical URL
    let canonical = document.querySelector('link[rel="canonical"]')
    if (!canonical) {
      canonical = document.createElement('link')
      canonical.setAttribute('rel', 'canonical')
      document.getElementsByTagName('head')[0].appendChild(canonical)
    }
    canonical.setAttribute('href', url)
    
  }, [title, description, keywords, image, url, type])
  
  return null
}

export default SEO

