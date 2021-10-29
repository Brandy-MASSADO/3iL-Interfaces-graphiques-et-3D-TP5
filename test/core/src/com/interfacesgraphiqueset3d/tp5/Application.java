package com.interfacesgraphiqueset3d.tp5;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Application extends ApplicationAdapter {
    private FitViewport viewport;
    private PerspectiveCamera camera;
    private Pixmap pixels;
    private Texture textureWithPixels;
    private SpriteBatch spriteBatch;
    
    private Vector2 currentScreen;
    private Vector3 currentScene;
    private Vector3 tmpVector3;

    private Vector3 center;

    @Override
    public void create() {
        // Get screen dimensions, in pixels :
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        


        float radius = 2;
        //Create a sphere
        Vector3 center = new Vector3(0, 0, 0);
        Sphere sphere = new Sphere(center, radius);
        Vector3 color = new Vector3(1.f, 0.f, 0.f);


        // Create a camera with perspective view :
        camera = new PerspectiveCamera(50.0f, screenWidth, screenHeight);
        camera.position.set(0f, 0f, -10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 500f;
        camera.update();


        // Create a viewport to convert coords of screen space into coords of scene
        // space.
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        Vector3 position_pixel_ecran = new Vector3(50, 40, 0);
        Vector3 position_pixel_3d = camera.unproject(position_pixel_ecran);
        System.out.println(position_pixel_3d);

      


        // Create an array of pixels, initialized with grey color :
        pixels = Pixmap.createFromFrameBuffer(0, 0, screenWidth, screenHeight);
        for (int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {
                pixels.setColor(0.1f, 0.1f, 0.1f, 1f);
                pixels.drawPixel(x, y);
            }
        }

        for(int i = 0; i < screenWidth; i++)
        {
            for(int j = 0; j < screenHeight; j++)
            {
                //Créer un rayon
                Vector3 origin = camera.position;
                Vector3 coordonneePixel = new Vector3(i,j,0);
                Vector3 pixelCamera = camera.unproject(coordonneePixel);
                Vector3 direction = pixelCamera.sub(origin);
                Ray rayon = new Ray(origin, direction);

                // Calculer l'intersection entre la sphere1 et ray1

                Boolean isIntersected =  Intersector.intersectRaySphere(rayon, center, radius, null);
                if(isIntersected)
                {
                    pixels.setColor(color.x, color.y, color.z, 1.f);
                    pixels.drawPixel(i, j);
                }
            }
        }


        // Add pixels in a Texture in order to render them :
        spriteBatch = new SpriteBatch();
        textureWithPixels = new Texture(pixels);

        // Initialize coords of the first pixel, in screen space :
        currentScreen = new Vector2(0, 0);

        // Others initializations :
        currentScene = new Vector3();
        tmpVector3 = new Vector3();
    }

    @Override
    public void render() {
        // If "ctrl + s" is pressed :
        if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.S)) {
            // Save the pixels into a png file :
            savePixelsInPngFile();
        }

        // If "escape" is pressed :
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            // Close th application :
            Gdx.app.exit();
        }

        // Reset the screen buffer colors :
        ScreenUtils.clear(0, 0, 0, 1);

        // Process pixels color :
        processPixel();

        // Render the texture with pixels :
        spriteBatch.begin();
        spriteBatch.draw(textureWithPixels, 0, 0);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureWithPixels.dispose();
        pixels.dispose();
    }

    /**
     * Compute the color of each screen pixels and store the results in the pixels
     * map.
     */
    private boolean processPixel() {
        boolean isOk = true;

        // Get color of current pixel :
        Vector3 color = getColor((int) currentScreen.x, (int) currentScreen.y);

        // Save color into pixels map :
        pixels.setColor(color.x, color.y, color.z, 1f);
        pixels.drawPixel((int) currentScreen.x, (int) currentScreen.y);

        return isOk;
    }

    /**
     * Wrire pixels in the png file "core/assets/render.png". If the file already
     * exists it will be overrided.
     */
    private boolean savePixelsInPngFile() {
        boolean isOk = true;

        // Create file :
        FileHandle file = Gdx.files.local("render.png");

        // Write pixels in file :
        PixmapIO.writePNG(file, pixels);

        return isOk;
    }

    /**
     * Return the color processed with path tracing and Phong method for the given pixel.
     */
    private Vector3 getColor(int xScreen, int yScreen)
    {
        Vector3 color = new Vector3(1.f, 0f, 0f); 

        // Get coords of current pixel, in scene space :
        tmpVector3.set(xScreen, yScreen, 0);
        currentScene = viewport.unproject(tmpVector3);

        // To be continued ...

        return color;
    }
}
