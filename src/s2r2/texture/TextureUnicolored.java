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

/**
 * A texture that colors an object
 * @author Fabian Prasser
 */
public class TextureUnicolored implements Texture {

    /** The color*/
    private final Color color;

    /**
     * Creates a new texture
     * @param color
     */
    public TextureUnicolored(Color color) {
        this.color = color;
    }

    @Override
    public Color getColorAt(Point intersection) {
        return color;
    }

    @Override
    public void initialize(Object object) {
        // Nothing to do
    }
}
