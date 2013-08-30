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

import s2r2.scene.Scene;
import s2r2.scene.SceneLight;
import s2r2.sprites.SpriteManager;
import s2r2.ui.Settings;
import s2r2.ui.UserInteraction;

/**
 * Implements a multithreaded raytracer
 * 
 * @author Fabian Prasser
 */
public class Renderer {

    /** The UI*/
    private final UserInteraction   uinterface;
    /** The camera*/
    public Camera                   camera;
    /** The raster*/
    private final Raster            raster  = new Raster();
    /** The scene*/
    private final Scene             scene;
    /** The pass*/
    public int                      pass    = 0;
    /** The mask*/
    public final boolean[]          raymask;
    /** The raytracers*/
    private final Raytracer[]       tracers = new Raytracer[Settings.RT_NUM_THREADS];
    /** The threads*/
    private final Thread[]          threads = new Thread[Settings.RT_NUM_THREADS];
    /** The workers*/
    private final Worker[] workers = new Worker[Settings.RT_NUM_THREADS];
    /** The number of active workers*/
    private volatile int            active  = 0;

    /**
     * Create a new raytracer
     * 
     * @param ui
     * @param viewport
     * @param scene
     */
    public Renderer(UserInteraction ui, Scene scene) {
        
        // Init
        this.scene = scene;
        this.uinterface = ui;
        this.camera = ui.getCamera();
        this.raster.update(camera);
        
        // Create a boolean mask for adaptive rendering
        this.raymask = new boolean[Viewport.get().width * Viewport.get().height];

        // Create threads and their raytracers and workers
        for (int i = 0; i < tracers.length; i++) {
            this.tracers[i] = new Raytracer(this, scene);
        }
        for (int i = 0; i < workers.length; i++) {
            this.workers[i] = new Worker(i, workers.length, raster, Viewport.get(), tracers[i], this);
        }
        for (int i = 0; i < threads.length; i++) {
            this.threads[i] = new Thread(workers[i]);
            this.threads[i].start();
        }
    }

    /**
     * Called by a worker, when done
     */
    public void done() {
        synchronized (this) {
            active--;
            if (active == 0) {
                this.notifyAll();
            }
        }
    }

    /**
     * Renders the scene to the buffer
     * @param buffer
     */
    public void render(int[] buffer) {

        for (SceneLight l : scene.lights) {
            l.distance = Double.NEGATIVE_INFINITY;
        }

        // Check for viewport update
        if (!this.uinterface.getCamera().equals(this.camera)) {
            this.camera = this.uinterface.getCamera();
            this.raster.update(this.camera);
        }

        // First pass
        this.pass = 0;
        raytrace(buffer);
        
        // Interpolate
        interpolate(buffer);
        
        // Second pass
        this.pass = 1;
        raytrace(buffer);

        // Render all sprites
        SpriteManager.get().render(buffer, camera, raster);
    }

    /**
     * Perform interpolation
     * 
     * @param buffer
     */
    private void interpolate(int[] buffer) {

        synchronized (this) {
            try {
                for (int i = 0; i < Settings.RT_NUM_THREADS; i++) {
                    workers[i].workInterpolate(buffer);
                    this.active++;
                }
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Raytrace the scene
     * 
     * @param buffer
     */
    private void raytrace(int[] buffer) {

        synchronized (this) {
            try {
                for (int i = 0; i < Settings.RT_NUM_THREADS; i++) {
                    workers[i].workRaytrace(this.camera, this.pass, buffer);
                    this.active++;
                }
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}