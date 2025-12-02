# SEO Implementation Guide

This guide explains how to use the SEO component for dynamic meta tags on each page.

## Overview

The project includes:
- ✅ Comprehensive meta tags in `index.html` (static/fallback)
- ✅ `SEO.jsx` component for dynamic per-page updates
- ✅ `sitemap.xml` for search engine indexing
- ✅ `robots.txt` for crawler instructions
- ✅ Hreflang tags for international SEO

## Important: Replace Domain Placeholders

**Before deploying**, replace `your-domain.com` in:
1. `dashboards/index.html` - All meta tag URLs
2. `dashboards/public/sitemap.xml` - All URL locations
3. `dashboards/public/robots.txt` - Sitemap URL

## Using the SEO Component

The `SEO` component automatically updates meta tags for each page. Add it to your dashboard components:

### Example: GeneralDashboard.jsx

```jsx
import SEO from '../components/SEO'

const GeneralDashboard = () => {
  return (
    <>
      <SEO 
        title="F1 Analytics - General Dashboard | Formula 1 Statistics"
        description="Explore current Formula 1 season statistics, championship standings, and comprehensive race data for all drivers and constructors."
        keywords="F1 standings, Formula 1 2025, F1 current season, F1 statistics"
      />
      {/* Your dashboard content */}
    </>
  )
}
```

### Example: BestDriverDashboard.jsx

```jsx
import SEO from '../components/SEO'

const BestDriverDashboard = () => {
  return (
    <>
      <SEO 
        title="Driver Greatness Index | Best F1 Drivers of All Time"
        description="Discover the greatest Formula 1 drivers ranked by the Driver Greatness Index (DGI), a comprehensive metric analyzing teammate dominance, podium percentages, championships, and more."
        keywords="best F1 driver, greatest F1 driver, Driver Greatness Index, DGI ranking, F1 all-time rankings"
      />
      {/* Your dashboard content */}
    </>
  )
}
```

### Example: AboutDashboard.jsx

```jsx
import SEO from '../components/SEO'

const AboutDashboard = () => {
  return (
    <>
      <SEO 
        title="About F1 Analytics | Data Sources & Methodology"
        description="Learn about the F1 Analytics project, data sources from F1DB, and the detailed methodology behind the Driver Greatness Index (DGI) calculation."
        keywords="F1 analytics about, DGI methodology, F1 data sources, Formula 1 statistics methodology"
      />
      {/* Your dashboard content */}
    </>
  )
}
```

### Example: SettingsDashboard.jsx

```jsx
import SEO from '../components/SEO'

const SettingsDashboard = () => {
  return (
    <>
      <SEO 
        title="Customize DGI Weights | F1 Analytics"
        description="Customize the Driver Greatness Index by adjusting metric weights. Recalculate DGI scores based on your preferences for teammate dominance, championships, and other factors."
        keywords="customize DGI, DGI weights, customize Driver Greatness Index, F1 analytics customize"
        // Optionally exclude from search indexing:
        // robots="noindex, nofollow"
      />
      {/* Your dashboard content */}
    </>
  )
}
```

## SEO Component Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `title` | string | "F1 Analytics - Formula 1..." | Page title |
| `description` | string | "Explore comprehensive..." | Meta description |
| `keywords` | string | "Formula 1, F1..." | Meta keywords (comma-separated) |
| `image` | string | "/og-image.png" | Open Graph/Twitter image path |
| `url` | string | current URL | Canonical URL |
| `type` | string | "website" | Open Graph type (website/article) |

## Best Practices

### 1. Unique Titles & Descriptions
- Each page should have unique, descriptive titles (50-60 characters)
- Write compelling descriptions (150-160 characters)
- Include relevant keywords naturally

### 2. Images
- Create an `og-image.png` (1200x630px) for social sharing
- Place it in `dashboards/public/`
- Use different images per page if desired

### 3. Canonical URLs
- The component automatically uses the current page URL
- Ensures Google knows the preferred version of each page

### 4. Robots Meta Tag (Optional)
If you want to prevent indexing of certain pages (like `/settings`), you can modify the SEO component or add to individual pages:

```jsx
useEffect(() => {
  const metaRobots = document.querySelector('meta[name="robots"]')
  if (metaRobots) {
    metaRobots.setAttribute('content', 'noindex, nofollow')
  }
}, [])
```

## Testing Your SEO

### 1. Check Meta Tags
- View page source and verify meta tags are present
- Use browser DevTools → Elements → Check `<head>` section

### 2. Validate Open Graph
- Use [Facebook Sharing Debugger](https://developers.facebook.com/tools/debug/)
- Use [LinkedIn Post Inspector](https://www.linkedin.com/post-inspector/)

### 3. Validate Twitter Cards
- Use [Twitter Card Validator](https://cards-dev.twitter.com/validator)

### 4. Test Sitemap
- Visit `https://your-domain.com/sitemap.xml`
- Validate format using [XML Sitemap Validator](https://www.xml-sitemaps.com/validate-xml-sitemap.html)

### 5. Check robots.txt
- Visit `https://your-domain.com/robots.txt`
- Ensure it's accessible and properly formatted

## Updating Sitemap

When you add new pages:

1. Open `dashboards/public/sitemap.xml`
2. Add a new `<url>` entry:

```xml
<url>
  <loc>https://your-domain.com/new-page</loc>
  <lastmod>2025-01-15</lastmod>
  <changefreq>weekly</changefreq>
  <priority>0.8</priority>
  <xhtml:link rel="alternate" hreflang="en" href="https://your-domain.com/new-page" />
</url>
```

3. Update the `<lastmod>` date when you make significant changes
4. Resubmit in Google Search Console

## Performance Considerations

- The SEO component uses `useEffect` and only updates when props change
- Meta tag updates are lightweight and don't impact performance
- All SEO files are static and served quickly

## Troubleshooting

### Meta tags not updating
- Check that SEO component is imported and used
- Verify props are being passed correctly
- Check browser console for errors

### Sitemap not accessible
- Ensure `sitemap.xml` is in `public` folder
- Check Vercel deployment includes `public` directory
- Verify file permissions

### Images not showing in social previews
- Ensure image is at least 1200x630px
- Check image is accessible via URL
- Verify image path is correct (use absolute URL)

## Next Steps

1. ✅ Replace all `your-domain.com` placeholders
2. ✅ Add SEO component to all dashboard pages
3. ✅ Create `og-image.png` for social sharing
4. ✅ Deploy and verify all files are accessible
5. ✅ Submit to Google Search Console (see `GOOGLE_SEARCH_CONSOLE_SETUP.md`)

