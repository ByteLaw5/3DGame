package com.bytelaw.render;

import com.bytelaw.engine.Window;
import com.bytelaw.light.PointLight;
import com.bytelaw.objects.Camera;
import com.bytelaw.objects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private static final float FOV = (float)Math.toRadians(60.0F), Z_NEAR = 0.01F, Z_FAR = 1000.0F;
    private ShaderProgram shaderProgram;
    private final Transformation transformation;
    @SuppressWarnings("FieldMayBeFinal")
    private float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10F;
    }

    public void init(Window window) throws Exception {
        shaderProgram = ShaderProgram.create("/vertex.glsl", "/fragment.glsl");
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
    }

    public void render(Window window, Camera camera, Iterable<GameObject> gameItems, Vector3f ambientLight,
                       PointLight pointLight) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update Light Uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);
        // Get a copy of the light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currPointLight);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for (GameObject gameItem : gameItems) {
            Model model = gameItem.getModel();
            // Set model view matrix for this object
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            // Render the model for this game object
            shaderProgram.setUniform("material", model.getMaterial());
            model.render();
        }

        shaderProgram.unbind();
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if(shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
