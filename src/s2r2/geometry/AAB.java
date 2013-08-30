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
package s2r2.geometry;

import s2r2.rendering.Ray;
import s2r2.scene.SceneObject;

/**
 * This class implements an axis-aligned bounding box for speeding up rendering
 * 
 * @author Fabian Prasser
 */
public class AAB extends SceneObject {

    /**
     * Merges a set of given AABs into a new AAB
     * @param boxes The AABs to merge
     * @return A new AAB that covers all input AABs
     */
    public static AAB merge(AAB... boxes) {

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        for (AAB b : boxes) {
            minX = Math.min(b.bounds[0].x, minX);
            minY = Math.min(b.bounds[0].y, minY);
            minZ = Math.min(b.bounds[0].z, minZ);
            maxX = Math.max(b.bounds[1].x, maxX);
            maxY = Math.max(b.bounds[1].y, maxY);
            maxZ = Math.max(b.bounds[1].z, maxZ);
        }

        AAB b = new AAB(minX, maxX, minY, maxY, minZ, maxZ, boxes);
        return b;
    }

    /** The boundary points of the AAB*/
    public Point[]      bounds = new Point[2];

    /** All contained objects*/
    public SceneObject[] objects;

    /** A unique ID utilized during rendering and set while packing the scene*/
    public int           uid;

    /**
     * The constructor
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param minZ
     * @param maxZ
     * @param objects
     */
    public AAB(double minX,
               double maxX,
               double minY,
               double maxY,
               double minZ,
               double maxZ,
               SceneObject[] objects) {
        super(null, Type.AAB, 0, 0, 0, 0, 0, 0, 0, false, false, false);
        bounds[0] = new Point(minX, minY, minZ);
        bounds[1] = new Point(maxX, maxY, maxZ);
        this.objects = objects;
    }

    @Override
    public Vector getNormalAt(Point p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point intersect(Ray r) {
        throw new UnsupportedOperationException();
    }
}
