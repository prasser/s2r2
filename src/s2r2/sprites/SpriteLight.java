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

import s2r2.geometry.Line;
import s2r2.geometry.Plane;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.rendering.Camera;
import s2r2.rendering.Raster;
import s2r2.rendering.Viewport;
import s2r2.scene.SceneLight;

/**
 * This class implements a sprite for a light
 * @author Fabian Prasser
 */
public class SpriteLight implements Sprite{

    /** The light*/
    private SceneLight light    = null;
    /** The bitmap*/
    private Bitmap bitmap       = null;

    public SpriteLight(SceneLight light) {
        this.light = light;
        this.bitmap = new Bitmap("light.png");
    }

    @Override
    public void render(int[] mPixels, Camera camera, Raster raster) {

        if (light.distance == Double.NEGATIVE_INFINITY) return;

        // Project light position onto viewport
        Plane p = new Plane(raster.origin, camera.direction);
        Vector direction = new Vector(camera.position.x - light.position.x,
                                      camera.position.y - light.position.y,
                                      camera.position.z - light.position.z);
        Line l = new Line(light.position, direction);
        Point projected = VectorMath.getIntersectionPoint(l, p);

        if (projected != null) {

            // TODO: Can go wrong if raster-vectors have 0 entries
            double vX = projected.x - raster.origin.x;
            double vY = projected.y - raster.origin.y;
            double cX = (vY * raster.deltaY.x - vX * raster.deltaY.y) / 
                        (raster.deltaX.y * raster.deltaY.x - raster.deltaX.x * raster.deltaY.y);
            double cY = (vX - cX * raster.deltaX.x) / raster.deltaY.x;

            double scale = light.size * 15000d / light.distance / light.distance;
            int posX = (int) cX;
            int posY = (int) cY;
            int minX = (int) (-bitmap.width * scale / 2d);
            int maxX = (int) (Viewport.get().width + bitmap.width * scale / 2d);
            int minY = (int) (-bitmap.height * scale / 2d);
            int maxY = (int) (Viewport.get().height + bitmap.height * scale / 2d);

            // TODO: This prevents the above side effect
            if (minX == Integer.MIN_VALUE) return;

            // Clipping
            if (posX >= minX && posX <= maxX && posY >= minY && posY <= maxY) {

                posX = posX - (int) (bitmap.width * scale / 2d);
                posY = posY - (int) (bitmap.height * scale / 2d);
                int toX = (posX + (int) (bitmap.width * scale));
                int toY = (posY + (int) (bitmap.height * scale));
                bitmap.render(mPixels, posX, posY, toX, toY, scale);
               
            }
        }
    }
}
