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

/**
 * This class implements general vector-math operations. In contrast to similar
 * methods in the classes <code>Vector</code> and <code>Point</code> the
 * contained methods always return new objects
 * 
 * @author Fabian Prasser
 */
public class VectorMath {

    /**
     * Vector addition applied to points
     * @param v1
     * @param v2
     * @return
     */
    public static final Point add(Point v1, Point v2) {
        return new Point(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    /**
     * Moves a point
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static final Point add(Point v1, Vector v2) {
        return new Point(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    /**
     * Returns the angle between two vectors
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static final double angle(Vector v1, Vector v2) {
        return Math.acos(scalarProduct(v1, v2) / (v1.getLength() * v2
                                 .getLength()));
    }

    /**
     * Returns the cross product of the given vectors/points
     * 
     * @param p1
     * @param p2
     * @return
     */
    public static final Vector crossProduct(Point p1, Vector p2) {
        double x = p1.y * p2.z - p1.z * p2.y;
        double y = p1.z * p2.x - p1.x * p2.z;
        double z = p1.x * p2.y - p1.y * p2.x;

        return new Vector(x, y, z);
    }

    /**
     * Returns the cross product of the given vectors
     * 
     * @param p1
     * @param p2
     * @return
     */
    public static final Vector crossProduct(Vector p1, Vector p2) {
        double x = p1.y * p2.z - p1.z * p2.y;
        double y = p1.z * p2.x - p1.x * p2.z;
        double z = p1.x * p2.y - p1.y * p2.x;

        return new Vector(x, y, z);
    }

    /**
     * Returns the normalized cross product
     * @param p1
     * @param p2
     * @return
     */
    public static Vector crossProductAndNormalize(Vector p1, Vector p2) {
        double x = p1.y * p2.z - p1.z * p2.y;
        double y = p1.z * p2.x - p1.x * p2.z;
        double z = p1.x * p2.y - p1.y * p2.x;
        double length = Math.sqrt(x * x + y * y + z * z);
        return new Vector(x / length, y / length, z / length);
    }

    /**
     * Returns the distance of the point from the plane
     * @param p
     * @param p1
     * @return
     */
    public static final double distance(Plane p, Point p1) {
        double x = p.equation_a * p1.x
                   + p.equation_b
                   * p1.y
                   + p.equation_c
                   * p1.z
                   + p.equation_d;
        return x;
    }

    /**
     * Returns the distance between two points
     * @param p1
     * @param p2
     * @return
     */
    public static final double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
                         + (p1.y - p2.y)
                         * (p1.y - p2.y)
                         + (p1.z - p2.z)
                         * (p1.z - p2.z));
    }

    /**
     * Returns the intersection point between the given line and given plane if
     * there is one. If the base-position of the line is on the plane, no
     * intersection is returned null otherwise.
     * @param r
     * @param s
     * @return
     */
    public static final Point getIntersectionPoint(Line r, Plane p) {
        double denominator = p.equation_a * r.direction.x
                             + p.equation_b
                             * r.direction.y
                             + p.equation_c
                             * r.direction.z;
        if (denominator == 0.0d) return null;
        double numerator = -p.equation_d - (p.equation_a * r.position.x
                                            + p.equation_b
                                            * r.position.y + p.equation_c * r.position.z);
        double mu = numerator / denominator;
        if (mu <= 0) return null;
        double x = r.position.x + r.direction.x * mu;
        double y = r.position.y + r.direction.y * mu;
        double z = r.position.z + r.direction.z * mu;
        return new Point(x, y, z);
    }

    /**
     * Computes the intersection between a given line and a given sphere, null if there is none
     * 
     * @param r
     * @param s
     * @return
     */
    public static final Point getIntersectionPoint(Line r, Sphere s) {
        double rpx_spx = r.position.x - s.position.x;
        double rpy_spy = r.position.y - s.position.y;
        double rpz_spz = r.position.z - s.position.z;

        double a1 = r.direction.x * r.direction.x
                    + r.direction.y
                    * r.direction.y
                    + r.direction.z
                    * r.direction.z;
        double b1 = 2.0f * (r.direction.x * (rpx_spx)
                            + r.direction.y
                            * (rpy_spy) + r.direction.z * (rpz_spz));
        double c1 = (rpx_spx) * (rpx_spx)
                    + (rpy_spy)
                    * (rpy_spy)
                    + (rpz_spz)
                    * (rpz_spz)
                    - (s.radius)
                    * (s.radius);

        double discriminant = b1 * b1 - 4.0f * a1 * c1;

        if (discriminant < 0) return null;

        a1 = a1 * 2.0f;
        b1 = -b1;

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (b1 + sqrtDiscriminant) / (a1);
        double t2 = (b1 - sqrtDiscriminant) / (a1);

        if (t1 < 0 && t2 < 0) return null;

        double use_t = 0.0f;
        if (t1 < 0) use_t = t2;
        else if (t2 < 0) use_t = t1;
        else if (t1 < t2) use_t = t1;
        else if (t2 <= t1) use_t = t2;

        double x = r.position.x + r.direction.x * use_t;
        double y = r.position.y + r.direction.y * use_t;
        double z = r.position.z + r.direction.z * use_t;

        return new Point(x, y, z);
    }

    /**
     * Returns the unnormalized normal at the given surface point
     * @param p
     * @return
     */
    public static final Vector getNormalAt(Plane p, Point p1) {
        // FIXME: Should return the correct normal depending on ray-direction
        return p.normal;
    }

    /**
     * Returns the unnormalized normal at the given surface point
     * @param p
     * @return
     */
    public static final Vector getNormalAt(Sphere s, Point p) {
        Vector res = VectorMath.vectorFromPoints(s.position, p);
        return res;
    }

    /**
     * Multiplies a vector with a constant
     * @param v
     * @param d
     * @return
     */
    public static final Vector multiply(Vector v, double d) {
        return new Vector(v.x * d, v.y * d, v.z * d);
    }

    /**
     * Rotates the vector by a given number of degrees around the given axis.
     * @param v
     * @param axis
     * @param phi
     * @return
     */
    public static final Vector rotate(Vector v, Vector axis, double phi) {
        double s = Math.sin(phi);
        double c = Math.cos(phi);
        double t = 1.0f - c;
        double rx = (t * axis.x * axis.x + c) * v.x
                    + (t * axis.x * axis.y - s * axis.z)
                    * v.y
                    + (t * axis.x * axis.z + s * axis.y)
                    * v.z;
        double ry = (t * axis.x * axis.y + s * axis.z) * v.x
                    + (t * axis.y * axis.y + c)
                    * v.y
                    + (t * axis.y * axis.z - s * axis.x)
                    * v.z;
        double rz = (t * axis.x * axis.z - s * axis.y) * v.x
                    + (t * axis.y * axis.z + s * axis.x)
                    * v.y
                    + (t * axis.z * axis.z + c)
                    * v.z;
        return new Vector(rx, ry, rz);
    }

    /**
     * Returns the scalar product of two given vectors
     * @param v1
     * @param v2
     * @return
     */
    public static final double scalarProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * Determines whether an object casts a shadow on itself
     * @param oPosition
     * @param oNormal
     * @param lPosition
     * @return
     */
    public static boolean selfShadow(Point oPosition,
                                     Vector oNormal,
                                     Point lPosition) {

        // FIXME: This is a bogus method
        
        double a = oNormal.x;
        double b = oNormal.y;
        double c = oNormal.z;
        double n = a * a + b * b + c * c;

        if (n == 0.0d) return false;

        double d = (a * oPosition.x + b * oPosition.y + c * oPosition.z);
        double z = d - (a * lPosition.x + b * lPosition.y + c * lPosition.z);

        if (z / n <= 0) return false;
        else return true;
    }

    /**
     * Returns v1-v2
     * @param v1
     * @param v2
     * @return
     */
    public static final Point sub(Point v1, Point v2) {
        return new Point(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Returns v1-v2
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static final Point sub(Point v1, Vector v2) {
        return new Point(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Returns a vector pointing from v2 to v1
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static final Vector vectorFromPoints(Point v2, Point v1) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Subtracts vectors
     * @param v1
     * @param v2
     * @return
     */
    public static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /** 
     * Normalizes vectors
     * @param w
     * @return
     */
    public static Vector normalize(Vector w) {
        double l = w.getLength();
        Vector v = new Vector(w.x/l, w.y/l, w.z/l);
        return v;
    }
}
