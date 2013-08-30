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

/**
 * This class provides access to basic settings
 * @author Fabian Prasser
 */
public class Settings {

    public static final double  UI_MOUSE_SPEED           = 0.25d;

    public static final int     RT_MAX_BOUNCE            = 5;
    public static final int     RT_NUM_THREADS           = 32;
    public static final int     RT_SECOND_PASS_THRESHOLD = 30;

    public static final int     VIEWPORT_WIDTH           = 800;
    public static final int     VIEWPORT_HEIGHT          = 498;
    public static final double  VIEWPORT_NEAR_CLIP       = 1d;
    public static final double  VIEWPORT_FAR_CLIP        = 100d;
    public static final double  VIEWPORT_ANGLE           = 0.25d * Math.PI;
    
    public static final String  APP_KEY_FORWARD          = "w";
    public static final String  APP_KEY_BACKWARD         = "s";
    public static final String  APP_KEY_LEFT             = "a";
    public static final String  APP_KEY_RIGHT            = "d";
    
    public static final double CAMERA_INITIAL_POS_X      = 53.33298173782452500d;
    public static final double CAMERA_INITIAL_POS_Y      = 21.10241298952124000d;
    public static final double CAMERA_INITIAL_POS_Z      = 53.97534983438923000d;
    public static final double CAMERA_INITIAL_UP_X       = -0.14067419697688770d;
    public static final double CAMERA_INITIAL_UP_Y       = 0.979175599912774800d;
    public static final double CAMERA_INITIAL_UP_Z       = -0.14637593668484414d;
    public static final double CAMERA_INITIAL_DIR_X      = -0.67749993148345500d;
    public static final double CAMERA_INITIAL_DIR_Y      = -0.20271741303608093d;
    public static final double CAMERA_INITIAL_DIR_Z      = -0.70496003677989160d;
}
