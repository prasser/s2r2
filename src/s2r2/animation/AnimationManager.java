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

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all animations and delegates ticks from the main animation loop
 * @author Fabian Prasser
 */
public class AnimationManager {

    /** The singleton instance*/
    private static AnimationManager singleton = new AnimationManager();

    /** Returns the singleton*/
    public static AnimationManager get() {
        return singleton;
    }

    /** All registered animations*/
    private List<Animation> animations  = new ArrayList<Animation>();

    /** The timestamp of the last tick*/
    private long             timestamp = System.currentTimeMillis();

    /** Empty constructor*/
    private AnimationManager() {}

    /** 
     * Registers a new animation
     * 
     * @param r The animation to register
     */
    public void register(Animation r) {
        this.animations.add(r);
    }

    /**
     * Call this method to trigger all registered animations
     */
    public void tick() {

        long delta = System.currentTimeMillis() - timestamp;
        for (Animation a : animations) {
            a.tick(delta);
        }
        timestamp = System.currentTimeMillis();
    }
}