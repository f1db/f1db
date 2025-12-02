/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        f1: {
          red: '#E10600',
          black: '#15151E',
          darkGray: '#1E1E28',
          gray: '#38383F',
          lightGray: '#949498',
          white: '#FFFFFF',
        }
      },
      fontFamily: {
        formula: ['Formula1', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
