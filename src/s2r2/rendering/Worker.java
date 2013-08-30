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

import s2r2.geometry.Vector;
import s2r2.ui.Settings;

/**
 * This worker is able to perform two basic operations
 * @author Fabian Prasser
 */
public class Worker implements Runnable {

    /** The workload*/
    public static enum Workload {
        RAYTRACE, INTERPOLATE
    }

    /** Is the worker currently active*/
    private boolean                      active      = false;
    /** Associated raytracer*/
    private final Raytracer              raytracer;
    /** Associated renderer*/
    private final Renderer               renderer;
    /** The id of the worker*/
    private final int                    id;
    /** The vertical offset for interleaved raytracing*/
    private final int                    verticalOffset;
    /** The current workload*/
    private Workload                     workload;
    /** Current raster*/
    private Raster                       raster;
    /** Current viewport*/
    private Viewport                     viewport;
    /** Current camera*/
    private Camera                       camera;
    /** Buffer to render to*/
    private int[]                        buffer;
    /** The current pass*/
    private int                          pass;
    /** The current mask*/
    private boolean[]                    mask;

    /** 
     * Creates a new worker
     * 
     * @param id
     * @param max
     * @param rayspecs
     * @param viewcanvas
     * @param tracer
     * @param raytracer
     */
    public Worker(int id, int max, Raster rayspecs, Viewport viewcanvas, Raytracer tracer, Renderer raytracer) {
        this.id = id;
        this.verticalOffset = max;
        this.raytracer = tracer;
        this.renderer = raytracer;
        this.raster = rayspecs;
        this.viewport = viewcanvas;
        this.mask = renderer.raymask;
    }

    /**
     * Creates a new worker
     * @param id
     * @param max
     * @param tracer
     * @param raytracer
     */
    public Worker(int id, int max, Raytracer tracer, Renderer raytracer) {
        this.id = id;
        this.verticalOffset = max;
        this.raytracer = tracer;
        this.renderer = raytracer;
    }

    @Override
    public void run() {
        
        // Main loop
        while (true) {
            
            // Work
            if (active) {
                
                switch (workload) {
                    // Raytracer
                    case RAYTRACE:
                        this.raytrace();
                        break;
                    // Or interpolate
                    case INTERPOLATE:
                        this.interpolate();
                        break;
                }

                // Callback
                synchronized (this) {
                    active = false;
                    renderer.done();
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            // Or sleep
            } else {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * Tells the worker to interpolate
     * @param buffer
     */
    public void workInterpolate(int[] buffer) {
        this.work(raster, viewport, camera, buffer, pass, mask, Workload.INTERPOLATE);
    }

    /**
     * Tells the worker to raytrace
     * @param camera
     * @param pass
     * @param buffer
     */
    public void workRaytrace(Camera camera, int pass, int[] buffer) {
        this.pass = pass;
        this.work(raster, viewport, camera, buffer, pass, mask, Workload.RAYTRACE);
    }

    /**
     * Creates an interpolated color for the given coordinate based on surrounding colors.
     * @param x
     * @param y
     * @param r
     * @param g
     * @param b
     * @return
     */
    private boolean harmonize(int x, int y, int[] r, int[] g, int[] b) {

        int index = (y * viewport.width + x);
        r[0] = buffer[index] >> 16 & 0xff;
        g[0] = buffer[index] >> 8 & 0xff;
        b[0] = buffer[index] & 0xff;
        index += 2;
        r[1] = buffer[index] >> 16 & 0xff;
        g[1] = buffer[index] >> 8 & 0xff;
        b[1] = buffer[index] & 0xff;
        index += (viewport.width * 2 - 2);
        r[2] = buffer[index] >> 16 & 0xff;
        g[2] = buffer[index] >> 8 & 0xff;
        b[2] = buffer[index] & 0xff;
        index += 2;
        r[3] = buffer[index] >> 16 & 0xff;
        g[3] = buffer[index] >> 8 & 0xff;
        b[3] = buffer[index] & 0xff;
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (Math.abs(r[i] - r[j]) > Settings.RT_SECOND_PASS_THRESHOLD || 
                    Math.abs(g[i] - g[j]) > Settings.RT_SECOND_PASS_THRESHOLD ||
                    Math.abs(b[i] - b[j]) > Settings.RT_SECOND_PASS_THRESHOLD) return false;
            }
        }
        r[0] += r[1] + r[2] + r[3];
        g[0] += g[1] + g[2] + g[3];
        b[0] += b[1] + b[2] + b[3];
        r[0] /= 4;
        g[0] /= 4;
        b[0] /= 4;
        return true;
    }

    /**
     * Scans all pixels and either interpolates their colors or marks them as targets for the
     * second phase
     */
    private void interpolate() {

        int[] hR = new int[4];
        int[] hG = new int[4];
        int[] hB = new int[4];
        int fromY = id * 16; // TODO
        int toY = fromY + 16; // TODO

        for (int x = 0; x < this.viewport.width - 2; x += 2) {
            for (int y = fromY; y < toY && y < viewport.height - 2; y += 2) {
                int index1 = (y * viewport.width + x + 1);
                int index2 = index1 + viewport.width - 1;
                int index3 = index2 + 1;
                int index4 = index2 + 2;
                int index5 = index4 + viewport.width - 1;

                if (harmonize(x, y, hR, hG, hB)) {

                    buffer[index1] = (hR[0] < 256 ? hR[0] << 16 : 255 << 16) | 
                                      (hG[0] < 256 ? hG[0] << 8 : 255 << 8) |
                                      (hB[0] < 256 ? (int) hB[0] : 255);
                    
                    buffer[index2] = buffer[index1];
                    buffer[index3] = buffer[index1];
                    buffer[index4] = buffer[index1];
                    buffer[index5] = buffer[index1];
                    mask[index1] = false;
                    mask[index2] = false;
                    mask[index3] = false;
                    mask[index4] = false;
                    mask[index5] = false;
                } else {
                    
                    mask[index1] = true;
                    mask[index2] = true;
                    mask[index3] = true;
                    mask[index4] = true;
                    mask[index5] = true;
                }
            }
        }
    }

    /**
     * Raytraces the scene both in first or second phase
     */
    private void raytrace() {
        
        // Init
        boolean firstPass = (pass == 0);
        int deltaY = verticalOffset;
        int deltaX = 1;
        int fromY = id;
        if (firstPass) {
            fromY *= 2;
            deltaY *= 2;
            deltaX = 2;
        }

        // Determine position based on raster
        double oX = raster.origin.x;
        double oY = raster.origin.y;
        double oZ = raster.origin.z;
        oX += raster.deltaY.x * fromY;
        oY += raster.deltaY.y * fromY;
        oZ += raster.deltaY.z * fromY;
        int index = 0;

        // Traverse the scene
        for (int y = fromY; y < viewport.height; y += deltaY) {
            for (int x = 0; x < viewport.width; x += deltaX) {

                // Trace from this pixel or not
                index = (y * viewport.width + x);
                if (firstPass || mask[index]) {
                    
                    // Call the raytracer
                    Vector rayDirection = new Vector(oX - camera.position.x,
                                                     oY - camera.position.y,
                                                     oZ - camera.position.z);
                    raytracer.last = null;
                    Color c = raytracer.trace(new Ray(camera.position, rayDirection, Settings.RT_MAX_BOUNCE));
                    
                    // Write to the buffer
                    buffer[index] = ((int) c.r < 256 ? (int) c.r << 16 : 255 << 16) | 
                                    ((int) c.g < 256 ? (int) c.g << 8 : 255 << 8) |
                                    ((int) c.b < 256 ? (int) c.b : 255);
                }

                // Adjust position from raster
                oX += raster.deltaX.x * deltaX;
                oY += raster.deltaX.y * deltaX;
                oZ += raster.deltaX.z * deltaX;
            }

            // Adjust position from raster
            oX += raster.deltaY.x * deltaY;
            oY += raster.deltaY.y * deltaY;
            oZ += raster.deltaY.z * deltaY;
            oX -= raster.resetX.x;
            oY -= raster.resetX.y;
            oZ -= raster.resetX.z;
        }
    }

    /**
     * Generic method that starts a workload
     * @param rayspecs
     * @param viewcanvas
     * @param viewport
     * @param mPixels
     * @param pass
     * @param bitmap
     * @param work
     */
    private void work(Raster rayspecs,
                      Viewport viewcanvas,
                      Camera viewport,
                      int[] mPixels,
                      int pass,
                      boolean[] bitmap,
                      Workload work) {
        synchronized (this) {
            this.mask = bitmap;
            this.pass = pass;
            this.raster = rayspecs;
            this.viewport = viewcanvas;
            this.camera = viewport;
            this.buffer = mPixels;
            this.workload = work;
            this.active = true;
            this.notifyAll();
        }
    }
}
