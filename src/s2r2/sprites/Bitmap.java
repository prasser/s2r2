/*
 *  Simple Software Realtime Raytracer S2R2 - (c) 2012 Fabian Prasser
 *  
 *  This file is part of S2R2.
 * 
 *  S2R2 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  S2R2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with S2R2.  If not, see <http://www.gnu.org/licenses/>.
 */
package s2r2.sprites;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import s2r2.rendering.Color;
import s2r2.rendering.Viewport;

/**
 * A bitmap
 * @author Fabian Prasser
 */
public class Bitmap {

    /** Buffer*/
    public final Color[][]  pixels;
    /** Width*/
    public final int        width;
    /** Height*/
    public final int        height;
    
    /**
     * Creates a new bitmap from the file at the given path
     * @param path
     */
    public Bitmap(String path){

        BufferedImage buf = null;
        try {
            buf = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pixels = new Color[buf.getWidth()][buf.getHeight()];
        width = buf.getWidth();
        height = buf.getHeight();
        for (int x = 0; x < buf.getWidth(); x++) {
            for (int y = 0; y < buf.getHeight(); y++) {
                int clr = buf.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                pixels[x][y] = new Color(red, green, blue);
            }
        }
    }
    
    /**
     * Renders the bitmap to the buffer at the given location with the given scale
     * @param buffer
     * @param posX
     * @param posY
     * @param toX
     * @param toY
     * @param scale
     */
    public void render(int[] buffer, int posX, int posY, int toX, int toY, double scale){

        int startX = Math.max(0, posX);
        int endX = Math.min(Viewport.get().width, toX);
        int startY = Math.max(0, posY);
        int endY = Math.min(Viewport.get().height, toY);
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int tX = (int) ((x - posX) / scale);
                int tY = (int) ((y - posY) / scale);
                int index = (y * Viewport.get().width + x);
                int r = (buffer[index] >> 16) & 0xff;
                int g = (buffer[index] >> 8) & 0xff;
                int b = (buffer[index]) & 0xff;
                r += pixels[tX][tY].r;
                g += pixels[tX][tY].g;
                b += pixels[tX][tY].b;
                buffer[index] = (r < 256 ? r << 16 : 255 << 16) | (g < 256 ? g << 8 : 255 << 8) | (b < 256 ? b : 255);
            }
        }
    }
}
