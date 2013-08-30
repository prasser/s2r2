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

import s2r2.texture.TexturePlaneXZChess;

/**
 * This animation moves a chess texture for a plane in XZ
 * @author Fabian Prasser
 */
public class AnimateTexturePlaneXZChess implements Animation {

    /** The texture to animate*/
    private TexturePlaneXZChess texture = null;
    
    /** The original origin*/
    private double       originX;
    
    /** The original origin*/
    private double       originZ;
    
    /** The speed*/
    private double       speed;

    /**
     * Creates a new animation
     * @param texture
     * @param speed
     */
    public AnimateTexturePlaneXZChess(TexturePlaneXZChess texture, double speed) {
        this.texture = texture;
        this.originX = texture.originX;
        this.originZ = texture.originZ;
        this.speed = speed;
    }

    @Override
    public void tick(long delta) {
        texture.originX += delta * speed;
        texture.originZ += delta * speed;
        if (originX - texture.originX > texture.sizeX) texture.originX = originX;
        if (originZ - texture.originZ > texture.sizeZ) texture.originZ = originZ;
    }

}
