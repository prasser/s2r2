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
 * A chess texture for an XZ-plane
 * @author Fabian Prasser
 */
public class TexturePlaneXZChess implements Texture {

    /** Origin*/
    public double originX = 0f;
    /** Origin*/
    public double originZ = 0f;
    /** Size*/
    public double sizeX   = 0f;
    /** Size*/
    public double sizeZ   = 0f;
    /** Color*/
    private Color c1;
    /** Color*/
    private Color c2;

    /**
     * Creates a new texture
     * @param sizeX
     * @param sizeZ
     * @param originX
     * @param originZ
     * @param c1
     * @param c2
     */
    public TexturePlaneXZChess(double sizeX, double sizeZ, double originX, double originZ, Color c1, Color c2) {
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.originX = originX;
        this.originZ = originZ;
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public Color getColorAt(Point intersection) {
        double diffX = intersection.x - originX;
        double diffZ = intersection.z - originZ;
        int valX = (int) (diffX / sizeX) % 2;
        int valZ = (int) (diffZ / sizeZ) % 2;
        if (valX == 0 && valZ == 0) return c1;
        if ((valX & valZ) == 0) return c2;
        else return c1;
    }

    @Override
    public void initialize(Object object) {
        // Nothing to do
    }
}
