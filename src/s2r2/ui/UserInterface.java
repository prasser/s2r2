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
package s2r2.ui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import s2r2.animation.AnimationManager;
import s2r2.rendering.Renderer;

/**
 * This class provides a basic user interface
 * @author Fabian Prasser
 *
 */
@SuppressWarnings("serial")
public class UserInterface extends Frame implements Runnable{

    /** Basic settings*/
    private final int         width;
    /** Basic settings*/
    private final int         height;
    /** Basic settings*/
    private int               fps;

    /** Buffer*/
    private MemoryImageSource source;
    /** Buffer*/
    private Image             image;
    /** Buffer*/
    private Graphics          graphics;
    /** Buffer*/
    private int[]             buffer;
    
    /** Renderer*/
    private Renderer          renderer;

    /**
     * Creates a new user interface
     * @param renderer
     * @param width
     * @param height
     */
    public UserInterface(Renderer renderer, int width, int height) {

        // Store
        this.renderer = renderer;
        this.width = width;
        this.height = height;
        
        // Init
        setTitle("Simple Software Realtime Raytracer (S2R2)");
        setUndecorated(false);
        setSize(this.width, this.height);
        setVisible(true);

        // Create buffer
        this.buffer = new int[this.height * this.width];
        this.source = new MemoryImageSource(this.width,
                                            this.height,
                                            new DirectColorModel(24, 0xff0000, 0xff00, 0xff),
                                            this.buffer,
                                            0,
                                            this.width);
        this.source.setAnimated(true);
        this.image = createImage(source);
        this.graphics = getGraphics();

        // Add window listener
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void run() {
        
        // Init
        long time = System.currentTimeMillis();
        int frame = 0;
        int last = 0;

        // Main event loop
        while (true) {

            // Render
            this.renderer.render(this.buffer);
            this.source.newPixels();
            this.graphics.drawImage(this.image, 0, 0, null);

            // Animate
            AnimationManager.get().tick();

            // Compute FPS
            ++frame;
            long currentTime = System.currentTimeMillis();
            if (currentTime - time > 1000) {
                this.fps = (int) ((1000d * ((double) frame - (double) last)) / ((double) currentTime - (double) time));
                this.setTitle("Simple Software Realtime Raytracer (S2R2) - " + fps + " FPS");
                time = currentTime;
                last = frame;
            }
        }
    }
}
