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
package s2r2.animation;

import s2r2.texture.TexturePlaneXZBitmap;

/**
 * This animation moves a texture for a plane in XZ
 * @author Fabian Prasser
 */
public class AnimateTexturePlaneXZImage implements Animation {

    /** The original origin*/
    private double       originX = 0f;
    
    /** The original origin*/
    private double       originZ = 0f;
    
    /** The texture to animate*/
    private TexturePlaneXZBitmap texture  = null;
    
    /** The speed*/
    private double       speed    = 0f;

    /**
     * Creates a new animation
     * @param texture
     * @param speed
     */
    public AnimateTexturePlaneXZImage(TexturePlaneXZBitmap texture, double speed) {
        this.originX = texture.originX;
        this.originZ = texture.originZ;
        this.speed = speed;
        this.texture = texture;
    }

    @Override
    public void tick(long millis) {
        texture.originX += millis * speed;
        texture.originZ += millis * speed;
        if (originX - texture.originX > texture.sizeX) texture.originX = originX;
        if (originZ - texture.originZ > texture.sizeZ) texture.originZ = originZ;
    }
}
