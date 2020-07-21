package com.bytelaw.engine;

import com.bytelaw.engine.input.MouseInput;
import com.bytelaw.light.Material;
import com.bytelaw.light.PointLight;
import com.bytelaw.objects.Camera;
import com.bytelaw.objects.GameObject;
import com.bytelaw.render.Model;
import com.bytelaw.render.OBJLoader;
import com.bytelaw.render.Renderer;
import com.bytelaw.render.Texture;
import com.google.common.collect.Lists;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {
    private static final float CAMERA_POS_STEP = 0.05f, MOUSE_SENSITIVITY = 0.2f;
    private final Renderer renderer;
    private final List<GameObject> gameObjects = Lists.newArrayList();
    private final Camera camera;
    private final Vector3f cameraInc;
    private Vector3f ambientLight;
    private PointLight pointLight;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1f;
        //Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        //Material material = new Material(new Vector3f(0.2f, 0.5f, 0.5f), reflectance);

        Model model = OBJLoader.loadModel("/models/cube.obj");
        Texture texture = new Texture("/textures/grass_block.png");
        Material material = new Material(texture, reflectance);

        model.setMaterial(material);
        GameObject gameObject = new GameObject(model);
        gameObject.setScale(0.5f);
        gameObject.setPosition(0, 0, -2);
        gameObjects.add(gameObject);

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_G)) {
            window.toggleModelDebug();
        }
        float lightPos = pointLight.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.pointLight.getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.pointLight.getPosition().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjects, ambientLight, pointLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        gameObjects.forEach(gameObject -> gameObject.getModel().cleanup());
    }
}
