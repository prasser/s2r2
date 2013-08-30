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

import s2r2.geometry.Point;
import s2r2.scene.SceneLight;

/**
 * This animation moves a light on a circular orbit around a given coordinate
 * 
 * @author Fabian Prasser
 */
public class AnimatePositionLight implements Animation {

    /** Current position */
    private double     position = 0;

    /** The radius */
    private double     radius   = 0;

    /** The speed */
    private double     speed    = 0;

    /** The light to move */
    private SceneLight light    = null;

    /** The origin*/
    private Point      origin   = null;

    /** 
     * Creates a new animation
     * 
     * @param light
     * @param origin
     * @param speed
     * @param radius
     */
    public AnimatePositionLight(SceneLight light,
                                Point origin,
                                double speed,
                                double radius) {
        this.speed = speed;
        this.radius = radius;
        this.light = light;
        this.origin = origin;

    }

    @Override
    public void tick(long millis) {
        position += millis * speed;
        if (position > 2 * Math.PI) position = 0;
        light.position = new Point(0 + origin.x,
                                   Math.sin(position) * radius + origin.y,
                                   Math.cos(position) * radius + origin.z);
        light.box.bounds[0] = new Point(light.position.x - light.size,
                                        light.position.y - light.size,
                                        light.position.z - light.size);
        light.box.bounds[1] = new Point(light.position.x + light.size,
                                        light.position.y + light.size,
                                        light.position.z + light.size);
    }
}
