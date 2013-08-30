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

import s2r2.texture.TextureSphereStriped;

/**
 * This class animates a striped texture for spheres
 * @author Fabian Prasser
 */
public class AnimateTextureSphereStriped implements Animation {

    /** The speed*/
    private double        speed;
    
    /** The texture to animate*/
    private TextureSphereStriped texture;

    /**
     * Creates a new animation
     * @param texture
     * @param speed
     */
    public AnimateTextureSphereStriped(TextureSphereStriped texture, double speed) {
        this.texture = texture;
        this.speed = speed;
    }

    @Override
    public void tick(long millis) {
        texture.offset += millis * speed;
        if (texture.offset >= 2d * Math.PI) texture.offset = 0;

    }
}
