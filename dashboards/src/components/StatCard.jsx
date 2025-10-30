import React, { useEffect, useRef } from 'react'
import anime from 'animejs'

const StatCard = ({ title, value, subtitle, icon, delay = 0 }) => {
  const cardRef = useRef(null)
  const valueRef = useRef(null)

  useEffect(() => {
    // Animate card entrance
    anime({
      targets: cardRef.current,
      translateY: [20, 0],
      opacity: [0, 1],
      duration: 800,
      delay: delay,
      easing: 'easeOutCubic'
    })

    // Animate value counter
    if (typeof value === 'number') {
      anime({
        targets: valueRef.current,
        innerHTML: [0, value],
        easing: 'easeOutExpo',
        duration: 1500,
        delay: delay + 200,
        round: 1
      })
    }
  }, [value, delay])

  return (
    <div ref={cardRef} className="stat-card opacity-0">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-f1-lightGray text-sm font-medium mb-2">{title}</p>
          <p ref={valueRef} className="text-3xl font-bold text-f1-white mb-1">
            {typeof value === 'number' ? 0 : value}
          </p>
          {subtitle && (
            <p className="text-f1-lightGray text-xs">{subtitle}</p>
          )}
        </div>
        {icon && (
          <div className="text-f1-red text-2xl ml-4">
            {icon}
          </div>
        )}
      </div>
    </div>
  )
}

export default StatCard
