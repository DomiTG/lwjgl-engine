package sh.hula.am.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Block {

    private Vector3f position;
    private Matrix4f modelMatrix;
    private Vector3f color;
    private float scale;

    public Block(Vector3f position, Vector3f color, float scale) {
        this.position = position;
        this.modelMatrix = new Matrix4f().translate(position).scale(scale);
        this.color = color;
        this.scale = scale;
        System.out.println(color.toString());
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }

}
