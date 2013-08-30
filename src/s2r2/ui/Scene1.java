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

import s2r2.animation.AnimatePositionLight;
import s2r2.animation.AnimateTexturePlaneXZChess;
import s2r2.animation.AnimateTexturePlaneXZImage;
import s2r2.animation.AnimateTextureSphereStriped;
import s2r2.animation.AnimationManager;
import s2r2.geometry.AAB;
import s2r2.geometry.Point;
import s2r2.geometry.Vector;
import s2r2.rendering.Color;
import s2r2.scene.Scene;
import s2r2.scene.SceneLight;
import s2r2.scene.SceneObject;
import s2r2.scene.SceneRectangleXZ;
import s2r2.scene.SceneSphere;
import s2r2.sprites.SpriteLight;
import s2r2.sprites.SpriteManager;
import s2r2.texture.Texture;
import s2r2.texture.TexturePlaneXZBitmap;
import s2r2.texture.TexturePlaneXZChess;
import s2r2.texture.TextureSphereStriped;
import s2r2.texture.TextureUnicolored;

/**
 * This class implements a basic example scene
 * @author Fabian Prasser
 */
public class Scene1 {

    private static final double DIFFUSION   = 0.3d;
    private static final double SHININESS = 200d;
    private static final double SPECULAR  = 30d;

    public static Scene build() {

        // Create base scene
        Scene scene = new Scene(new Color(6, 89, 255), new Color(10, 10, 10));

        // Create and animate light
        SceneLight l1 = new SceneLight(new Point(0, 10, 20), 15d, 0.2d, new Color(255, 255, 255));
        scene.addLight(l1);
        AnimationManager.get().register(new AnimatePositionLight(l1, new Point(0, 0, 0), Math.PI / 3000d, 15));
        SpriteManager.get().register(new SpriteLight(l1));

        // Create sphere
        SceneSphere s1 = new SceneSphere(new Point(-20d, 0, 20),
                                         3,
                                         new TextureUnicolored(new Color(255, 30, 30)),
                                         1.0d,
                                         0.25d,
                                         1.0d,
                                         0.0d,
                                         DIFFUSION,
                                         SPECULAR,
                                         SHININESS,
                                         true,
                                         true,
                                         true);

        // Create sphere
        SceneSphere s2 = new SceneSphere(new Point(-10d, 0, 20),
                                         3,
                                         new TextureUnicolored(new Color(255, 255, 30)),
                                         1.0d,
                                         0.25d,
                                         1.0d,
                                         0.0d,
                                         DIFFUSION,
                                         SPECULAR,
                                         SHININESS,
                                         true,
                                         true,
                                         true);

        // Create and animate striped sphere
        TextureSphereStriped t1 = new TextureSphereStriped(30,
                                                           new Vector(0, 1, 1),
                                                           new Color(0, 0, 255),
                                                           new Color(255, 255, 255));

        SceneSphere s3 = new SceneSphere(new Point(0d, 0, 20),
                                         3,
                                         t1,
                                         1.0d,
                                         0.3d,
                                         1.0d,
                                         0.0d,
                                         DIFFUSION,
                                         SPECULAR,
                                         SHININESS,
                                         true,
                                         true,
                                         true);
        AnimationManager.get().register(new AnimateTextureSphereStriped(t1, Math.PI / 10000d));

        // Create sphere
        SceneSphere s4 = new SceneSphere(new Point(10d, 0, 20),
                                         3,
                                         new TextureUnicolored(new Color(255, 60, 255)),
                                         1.0d,
                                         0.25d,
                                         1.0d,
                                         0.0d,
                                         DIFFUSION,
                                         SPECULAR,
                                         SHININESS,
                                         true,
                                         true,
                                         true);

        // Create sphere
        SceneSphere s5 = new SceneSphere(new Point(20d, 0, 20),
                                         3,
                                         new TextureUnicolored(new Color(60, 255, 255)),
                                         1.0d,
                                         0.25d,
                                         1.0d,
                                         0.0d,
                                         DIFFUSION,
                                         SPECULAR,
                                         SHININESS,
                                         true,
                                         true,
                                         true);

        // Create plane with animated chess texture
        TexturePlaneXZChess t2 = new TexturePlaneXZChess(7, 7, 100000, 100000, new Color(60, 60, 60), new Color(160,
                                                                                                                160,
                                                                                                                160));
        AnimationManager.get().register(new AnimateTexturePlaneXZChess(t2, 0.005d));

        SceneRectangleXZ p1 = new SceneRectangleXZ(-10,
                                                   -50,
                                                   +50,
                                                   -50,
                                                   +50,
                                                   t2,
                                                   1.0d,
                                                   0.2d,
                                                   1.0d,
                                                   0.0d,
                                                   DIFFUSION,
                                                   SPECULAR,
                                                   SHININESS,
                                                   true,
                                                   true,
                                                   true);

        // Create sphere
        SceneSphere s21 = new SceneSphere(new Point(-20d, 0, -20),
                                          3,
                                          new TextureUnicolored(new Color(255, 20, 20)),
                                          1.0d,
                                          0.25d,
                                          1.0d,
                                          0.0d,
                                          DIFFUSION,
                                          SPECULAR,
                                          SHININESS,
                                          true,
                                          true,
                                          true);

        // Create sphere
        SceneSphere s22 = new SceneSphere(new Point(-10d, 0, -20),
                                          3,
                                          new TextureUnicolored(new Color(255, 255, 20)),
                                          1.0d,
                                          0.25d,
                                          1.0d,
                                          0.0d,
                                          DIFFUSION,
                                          SPECULAR,
                                          SHININESS,
                                          true,
                                          true,
                                          true);

        // Create striped sphere
        SceneSphere s23 = new SceneSphere(new Point(0d, 0, -20),
                                          3,
                                          new TextureSphereStriped(30,
                                                                   new Vector(1, 1, 0),
                                                                   new Color(0, 255, 0),
                                                                   new Color(255, 255, 255)),
                                          1.0d,
                                          0.3d,
                                          1.0d,
                                          0.0d,
                                          DIFFUSION,
                                          SPECULAR,
                                          SHININESS,
                                          true,
                                          true,
                                          true);

        // Create sphere
        SceneSphere s24 = new SceneSphere(new Point(10d, 0, -20),
                                          3,
                                          new TextureUnicolored(new Color(255, 20, 255)),
                                          1.0d,
                                          0.25d,
                                          1.0d,
                                          0.0d,
                                          DIFFUSION,
                                          SPECULAR,
                                          SHININESS,
                                          true,
                                          true,
                                          true);

        // Create sphere
        SceneSphere s25 = new SceneSphere(new Point(20d, 0, -20),
                                          3,
                                          new TextureUnicolored(new Color(20, 255, 255)),
                                          1.0d,
                                          0.25d,
                                          1.0d,
                                          0.0d,
                                          DIFFUSION,
                                          SPECULAR,
                                          SHININESS,
                                          true,
                                          true,
                                          true);

        // Create plane with animated sky texture
        Texture t3 = null;
        t3 = new TexturePlaneXZBitmap("sky.jpg", 1, 1, 0, 0, 1000, 2000, new Color(6, 89, 255));
        AnimationManager.get().register(new AnimateTexturePlaneXZImage((TexturePlaneXZBitmap) t3, 0.005d));

        SceneRectangleXZ p6 = new SceneRectangleXZ(+100,
                                                   -1000000,
                                                   +1000000,
                                                   -1000000,
                                                   +1000000,
                                                   t3,
                                                   1.0d,
                                                   0d,
                                                   1.0d,
                                                   0.0d,
                                                   DIFFUSION,
                                                   SPECULAR,
                                                   SHININESS,
                                                   false,
                                                   false,
                                                   false);

        // Pack the scene
        SceneObject[] objects1 = { s1, s2 };
        AAB b1 = new AAB(-23d, -7d, -3d, +3d, 17d, 23d, objects1);

        SceneObject[] objects2 = { s4, s5, s3 };
        AAB b2 = new AAB(-3d, +23d, -3d, +3d, 17d, 23d, objects2);

        SceneObject[] objects21 = { s21, s22 };
        AAB b21 = new AAB(-23d, -7d, -3d, +3d, -23d, -17d, objects21);

        SceneObject[] objects22 = { s23, s24, s25 };
        AAB b22 = new AAB(-3d, +23d, -3d, +3d, -23d, -17d, objects22);

        scene.addObject(b1);
        scene.addObject(b2);
        scene.addObject(b21);
        scene.addObject(b22);
        scene.addObject(p6);
        scene.addObject(p1);

        scene.pack();

        // Return
        return scene;
    }
}
