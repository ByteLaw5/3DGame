package com.bytelaw.objects;

import com.bytelaw.render.Model;
import org.joml.Vector3f;

public class GameObject {
    private final Model model;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public GameObject(Model model) {
        this.model = model;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.set(x, y, z);
    }

    public Model getModel() {
        return model;
    }
}
