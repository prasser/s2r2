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
package s2r2.scene;

import java.util.ArrayList;
import java.util.List;

import s2r2.geometry.AAB;
import s2r2.rendering.Color;
import s2r2.scene.SceneObject.Type;

/**
 * This class represents a scene
 * @author Fabian Prasser
 */
public class Scene {

    /** Lights*/
    private List<SceneLight>  lLight   = new ArrayList<SceneLight>();
    /** Objects*/
    private List<SceneObject> lObject  = new ArrayList<SceneObject>();
    /** Lights*/
    public SceneLight[]       lights;
    /** Objects*/
    public SceneObject[]      objects;
    /** All objects*/
    public SceneObject[]      all;
    /** Back color*/
    public Color              color;
    /** Ambient light*/
    public Color              ambient;

    /**
     * Creates a new scene
     * @param color
     * @param ambient
     */
    public Scene(Color color, Color ambient) {
        this.color = color;
        this.ambient = ambient;
    }

    /**
     * Adds a light
     * @param l
     */
    public void addLight(SceneLight l) {
        lLight.add(l);
    }

    /** 
     * Adds an object
     * @param o
     */
    public void addObject(SceneObject o) {
        lObject.add(o);
    }

    /**
     * Packs the scene
     */
    public void pack() {
        lights = lLight.toArray(new SceneLight[0]);
        objects = lObject.toArray(new SceneObject[0]);
        List<SceneObject> lAll = new ArrayList<SceneObject>();
        lAll.addAll(lLight);
        lAll.addAll(lObject);
        all = lAll.toArray(new SceneObject[0]);
        lLight.clear();
        lObject.clear();
        int id = 0;
        for (SceneObject o : all) {
            if (o.type == Type.AAB) ((AAB) o).uid = id++;
        }
    }

    /**
     * Returns the ambient light for the given object
     * @param object
     * @return
     */
    public Color getAmbient(SceneObject object) {
        Color ambient = new Color(0,0,0);
        ambient.r = this.ambient.r * object.ambient;
        ambient.g = this.ambient.g * object.ambient;
        ambient.b = this.ambient.b * object.ambient;
        return ambient;
    }
}
