#!/usr/bin/env python3
"""
Generate PNG and ICO favicons from SVG
Requires: cairosvg or PIL/Pillow with svg2png
Install: pip install cairosvg pillow
"""

try:
    from PIL import Image, ImageDraw, ImageFont
    import os
except ImportError:
    print("Please install required packages:")
    print("  pip install pillow")
    exit(1)

def create_favicon_png(size, output_path):
    """Create a PNG favicon at the specified size"""
    # Create image with red background
    img = Image.new('RGB', (size, size), color='#E10600')
    draw = ImageDraw.Draw(img)
    
    # Draw rounded corners
    # For simplicity, we'll use a rectangle (rounded corners need more complex path)
    # But for small sizes like 16x16, it won't matter much
    
    # Draw checkered flag pattern in corner
    flag_size = int(size * 0.2)
    flag_x = int(size * 0.08)
    flag_y = int(size * 0.08)
    
    # White squares for checkered pattern
    square_size = flag_size // 2
    draw.rectangle([flag_x, flag_y, flag_x + square_size, flag_y + square_size], fill='white')
    draw.rectangle([flag_x + square_size, flag_y + square_size, flag_x + flag_size, flag_y + flag_size], fill='white')
    
    # Flag border
    draw.rectangle([flag_x, flag_y, flag_x + flag_size, flag_y + flag_size], outline='white', width=max(1, size//32))
    
    # Draw "F1" text
    try:
        # Try to use a bold font
        font_size = int(size * 0.52)
        # Try different font paths
        font = None
        font_paths = [
            '/System/Library/Fonts/Supplemental/Arial Bold.ttf',  # macOS
            '/usr/share/fonts/truetype/arial.ttf',  # Linux
            'arial.ttf',  # Windows
        ]
        
        for path in font_paths:
            try:
                font = ImageFont.truetype(path, font_size)
                break
            except:
                continue
        
        if font is None:
            # Fall back to default font
            font = ImageFont.load_default()
        
        # Calculate text position (centered)
        bbox = draw.textbbox((0, 0), "F1", font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]
        text_x = (size - text_width) // 2
        text_y = int(size * 0.68) - text_height // 2
        
        draw.text((text_x, text_y), "F1", fill='white', font=font)
    except Exception as e:
        print(f"Warning: Could not load font, using default: {e}")
        # Fallback: draw simple text
        draw.text((size//4, size//3), "F1", fill='white')
    
    # Save PNG
    img.save(output_path, 'PNG')
    print(f"✓ Generated {output_path} ({size}x{size})")

def create_ico(output_path, sizes=[16, 32]):
    """Create ICO file with multiple sizes"""
    try:
        from PIL import Image
        images = []
        for size in sizes:
            img = Image.new('RGB', (size, size), color='#E10600')
            # Reuse the same drawing logic
            draw = ImageDraw.Draw(img)
            
            # Checkered flag
            flag_size = int(size * 0.2)
            flag_x = int(size * 0.08)
            flag_y = int(size * 0.08)
            square_size = flag_size // 2
            draw.rectangle([flag_x, flag_y, flag_x + square_size, flag_y + square_size], fill='white')
            draw.rectangle([flag_x + square_size, flag_y + square_size, flag_x + flag_size, flag_y + flag_size], fill='white')
            draw.rectangle([flag_x, flag_y, flag_x + flag_size, flag_y + flag_size], outline='white', width=max(1, size//32))
            
            # F1 text
            try:
                font_size = int(size * 0.52)
                font = ImageFont.load_default()
                draw.text((size//4, size//3), "F1", fill='white')
            except:
                draw.text((size//4, size//3), "F1", fill='white')
            
            images.append(img)
        
        # Save as ICO
        images[0].save(output_path, format='ICO', sizes=[(img.size[0], img.size[1]) for img in images])
        print(f"✓ Generated {output_path}")
    except Exception as e:
        print(f"Could not create ICO file: {e}")
        print("ICO format may require additional libraries")

def main():
    """Generate all favicon formats"""
    script_dir = os.path.dirname(os.path.abspath(__file__))
    public_dir = os.path.join(script_dir, '..', 'public')
    
    if not os.path.exists(public_dir):
        os.makedirs(public_dir)
    
    print("Generating F1 favicons...")
    print("=" * 50)
    
    # Generate PNGs in various sizes
    sizes = [16, 32, 64, 180, 192, 512]
    for size in sizes:
        if size == 180:
            filename = 'apple-touch-icon.png'
        elif size == 192:
            filename = 'android-chrome-192x192.png'
        elif size == 512:
            filename = 'android-chrome-512x512.png'
        else:
            filename = f'favicon-{size}x{size}.png'
        
        output_path = os.path.join(public_dir, filename)
        create_favicon_png(size, output_path)
    
    # Generate ICO
    ico_path = os.path.join(public_dir, 'favicon.ico')
    create_ico(ico_path, sizes=[16, 32])
    
    print("\n" + "=" * 50)
    print("✓ Favicon generation complete!")
    print(f"  Output directory: {public_dir}")
    print("\nNext steps:")
    print("  1. The SVG favicon is already in public/f1-logo.svg")
    print("  2. PNG and ICO files have been generated")
    print("  3. The index.html has been updated to reference them")

if __name__ == '__main__':
    main()
