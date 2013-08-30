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

import s2r2.rendering.Renderer;
import s2r2.scene.Scene;

/**
 * Main entry point
 * @author Fabian Prasser
 */
public class Main {

    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {

        // Build the scene
        Scene s = Scene1.build();
        
        // Create user interaction
        UserInteraction h = new UserInteraction();
        
        // Create a renderer
        Renderer r = new Renderer(h, s);
        
        // Create a user interface
        UserInterface c = new UserInterface(r,
                                            Settings.VIEWPORT_WIDTH,
                                            Settings.VIEWPORT_HEIGHT);
        
        // Init and run
        h.register(c);
        c.run();
    }
}
