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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.geometry.VectorMath;
import s2r2.rendering.Camera;

/**
 * This class provides the user interaction. It allows users to change the camera position and
 * directions with mouse and keyboard
 * 
 * @author Fabian Prasser
 */
public class UserInteraction {
    
    /** Initial camera position*/
    private volatile Camera     camera    = new Camera(new Point(0, 0, 50), new Vector(0, 0, 1), new Vector(0, 1, 0));
    /** The user interface*/
    private UserInterface       uinterface;
    /** Drag start position*/
    private int                 dragStartX;
    /** Drag start position*/
    private int                 dragStartY;
    /** Camera params*/
    private Vector              direction = camera.direction;
    /** Camera params*/
    private Vector              up        = camera.up;
    /** Camera params*/
    private Vector              zvector   = new Vector(0, 1, 0);
    /** Camera params*/
    private Point               position  = camera.position;

    /**
     * Returns the current camera
     * @return
     */
    public synchronized Camera getCamera() {
        return this.camera;
    }

    /**
     * Registers the user interface
     * @param uinterface
     */
    public void register(UserInterface uinterface) {
        
        this.uinterface = uinterface;
        
        // Add listeners
        uinterface.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                synchronized(this) {
                    handleKeyPressed(e);
                }
            }
        });
        
        // Add listeners
        uinterface.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                synchronized(this) {
                handleMousePressed(e);
                }
            }
        });
        
        // Add listeners
        uinterface.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
                synchronized(this) {
                handleMouseDragged(e);
                }
            }
        });
    }

    /**
     * Sets the camera
     * @param camera
     */
    private synchronized void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Handle key down
     * 
     * @param e
     */
    protected void handleKeyPressed(KeyEvent e) {
        String key = String.valueOf(e.getKeyChar());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (key.equals(Settings.APP_KEY_FORWARD)) {
            position = VectorMath.add(position, direction);
        } else if (key.equals(Settings.APP_KEY_BACKWARD)) {
            position = VectorMath.sub(position, direction);
        } else if (key.equals(Settings.APP_KEY_RIGHT)) {
            Vector rDirection = VectorMath.crossProductAndNormalize(direction,
                                                                    zvector);
            position = VectorMath.add(position, rDirection);
        } else if (key.equals(Settings.APP_KEY_LEFT)) {
            Vector lDirection = VectorMath.crossProductAndNormalize(direction,
                                                                    zvector);
            position = VectorMath.sub(position, lDirection);
        }
        setCamera(new Camera(position, direction, up));
    }

    /**
     * Handle mouse dragged
     */
    protected void handleMouseDragged(MouseEvent e) {

        // Create new view
        int width = uinterface.getWidth();
        int delta = e.getX() - dragStartX;
        double percentage = (double) delta / (double) width;
        double deltaX = percentage * 2f * Math.PI * Settings.UI_MOUSE_SPEED;

        // Create new view
        int height = uinterface.getHeight();
        delta = e.getY() - dragStartY;
        percentage = (double) delta / (double) height;
        double deltaY = percentage * 2f * Math.PI * Settings.UI_MOUSE_SPEED;

        // Store right-vector
        Vector rightVector = VectorMath.crossProduct(direction, zvector);

        // Apply deltaX -> Rotate around viewup-vector
        direction = VectorMath.rotate(direction, zvector, -deltaX);

        // Apply deltaY -> Rotate around right-vector
        direction = VectorMath.rotate(direction, rightVector, -deltaY);
        up = VectorMath.multiply(VectorMath
                .crossProductAndNormalize(direction, rightVector), -1);

        setCamera(new Camera(position, direction, up));

        // Store current drag start position
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    /**
     * Handle mouse pressed
     */
    protected void handleMousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
    }
}
