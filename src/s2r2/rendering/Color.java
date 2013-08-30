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
package s2r2.rendering;

/**
 * This class implements a color in the RGB color model
 * @author Fabian Prasser
 */
public class Color {

    /** The color black*/
    public static final Color BLACK = new Color(0, 0, 0);

    /** The red component*/
    public double             r;
    /** The green component*/
    public double             g;
    /** The blue component*/
    public double             b;

    /** 
     * Creates a new color
     * @param r
     * @param g
     * @param b
     */
    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public Color clone(){
        return new Color(r,g,b);
    }
}
