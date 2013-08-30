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

import java.util.ArrayList;
import java.util.List;

import s2r2.rendering.Camera;
import s2r2.rendering.Raster;

public class SpriteManager implements Sprite{
    
    private static SpriteManager singleton = new SpriteManager();
    
    public static SpriteManager get(){
        return singleton;
    }
    
    private List<Sprite> sprites = new ArrayList<Sprite>();

    private SpriteManager(){}
    
    public void register(Sprite sprite){
        this.sprites.add(sprite);
    }
    
    public void render(int[] mPixels, Camera camera, Raster raster){
        for (Sprite sprite : sprites){
            sprite.render(mPixels, camera, raster);
        }
    }
}
