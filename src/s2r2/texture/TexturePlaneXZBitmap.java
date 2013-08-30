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
package s2r2.texture;

import s2r2.geometry.Point;
import s2r2.rendering.Color;
import s2r2.sprites.Bitmap;

/**
 * A bitmap texture for an XZ-plane
 * @author Fabian Prasser
 */
public class TexturePlaneXZBitmap implements Texture {

    /** Origin*/
    public double       originX   = 0f;
    /** Origin*/
    public double       originZ   = 0f;
    /** Size*/
    public double       sizeX     = 0f;
    /** Size*/
    public double       sizeZ     = 0f;
    /** Bitmap*/
    protected Bitmap    bitmap    = null;
    /** Fade away*/
    protected Color     fadeColor = null;
    /** Fade away*/
    protected double    fadeFrom  = 0f;
    /** Fade away*/
    protected double    fadeTo    = 0f;
    /** Fade away*/
    protected boolean   fade      = false;

    /**
     * Creates a new texture
     * @param img
     * @param sizeX
     * @param sizeZ
     * @param originX
     * @param originZ
     * @param fadeFrom
     * @param fadeTo
     * @param fadeColor
     */
    public TexturePlaneXZBitmap(String img,
                                double sizeX,
                                double sizeZ,
                                double originX,
                                double originZ,
                                double fadeFrom,
                                double fadeTo,
                                Color fadeColor) {
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.originX = originX;
        this.originZ = originZ;
        this.bitmap = new Bitmap(img);

        if (fadeFrom != fadeTo) {
            this.fade = true;
            this.fadeFrom = fadeFrom * fadeFrom;
            this.fadeTo = fadeTo * fadeTo - fadeFrom;
            this.fadeColor = fadeColor;
        }
    }

    @Override
    public Color getColorAt(Point intersection) {
        
        // Init
        double diffX = intersection.x - originX;
        double diffZ = intersection.z - originZ;
        double distance = 0f;
        
        // Fade out
        if (fade) {
            distance = diffX * diffX + diffZ * diffZ;
            if (distance > fadeFrom) {
                distance = Math.sqrt((distance - fadeFrom) / fadeTo);
                if (distance > 1d) return fadeColor;
            } else {
                distance = 0f;
            }
        }
        
        // Find pixel
        int pX = (int) (diffX / sizeX - bitmap.width/2) % bitmap.width;
        int pZ = (int) (diffZ / sizeZ - bitmap.height/2) % bitmap.height;
        if (pX < 0) pX = bitmap.width + pX;
        if (pZ < 0) pZ = bitmap.height + pZ;
        Color c = bitmap.pixels[pX][pZ];
        
        // Fade out
        if (distance != 0f) {
            c = new Color((distance * fadeColor.r + c.r * (1f - distance)),
                          (distance * fadeColor.g + c.g * (1f - distance)),
                          (distance * fadeColor.b + c.b * (1f - distance)));
        }
        
        // Return
        return c;
    }

    @Override
    public void initialize(Object object) {
        // Nothing to do
    }
}
