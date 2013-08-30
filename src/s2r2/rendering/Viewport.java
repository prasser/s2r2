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

import s2r2.ui.Settings;

/**
 * This class implements a viewport
 * @author Fabian Prasser
 */
public class Viewport {

    /** The singleton instance*/
    private static final Viewport singleton = new Viewport(Settings.VIEWPORT_WIDTH,
                                                           Settings.VIEWPORT_HEIGHT,
                                                           Settings.VIEWPORT_NEAR_CLIP,
                                                           Settings.VIEWPORT_FAR_CLIP,
                                                           Settings.VIEWPORT_ANGLE);
    
    /** Width in terms of pixels*/
    public final int    width;
    /** height in terms of pixels*/
    public final int    height;
    /** Horizontal resolution*/
    public final double resolutionX;
    /** Vertical resolution*/
    public final double resolutionY;
    /** Near clipping plane*/
    public final double nearClip;
    /** Far clipping plane*/
    public final double farClip;
    /** Ratio*/
    public final double aspectRatio;
    /** Angle*/
    public final double angle;

    /**
     * Creates a new viewport
     * @param width
     * @param height
     * @param near
     * @param far
     * @param angle
     */
    private Viewport(int width, int height, double near, double far, double angle) {

        this.width = width;
        this.height = height;
        this.nearClip = near;
        this.farClip = far;
        this.angle = angle;

        double near_height = this.nearClip * Math.tan(angle / 2.0f) * 2.0f;
        double near_width = (double) width / (double) height * near_height;

        this.resolutionX = near_width / width;
        this.resolutionY = near_height / height;

        this.aspectRatio = (double) width / (double) height;
    }
    
    /**
     * Returns the singleton instance
     * @return
     */
    public static Viewport get(){
        return singleton;
    }
}
