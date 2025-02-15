package sh.hula.am.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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

    public boolean isHovered(Camera camera) {
        // Get the current window context
        long window = GLFW.glfwGetCurrentContext();
        
        // Get cursor position
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        
        // Get window size
        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
        
        // Convert to normalized device coordinates (-1 to 1)
        float normalizedX = (float)(xpos[0] / windowWidth[0] * 2 - 1);
        float normalizedY = (float)(1 - ypos[0] / windowHeight[0] * 2);
        
        // Create ray from camera position
        Vector3f rayOrigin = new Vector3f(camera.getPosition());
        
        // Calculate ray direction using camera's view matrix
        Vector3f rayDirection = new Vector3f(normalizedX, normalizedY, -1);
        rayDirection.sub(rayOrigin).normalize();
        
        // Check intersection with block bounds
        float minX = position.x - scale/2;
        float maxX = position.x + scale/2;
        float minY = position.y - scale/2;
        float maxY = position.y + scale/2;
        float minZ = position.z - scale/2;
        float maxZ = position.z + scale/2;
        
        // Ray-AABB intersection test
        float tMin = Float.NEGATIVE_INFINITY;
        float tMax = Float.POSITIVE_INFINITY;
        
        // Check X planes
        if (Math.abs(rayDirection.x) > 1e-6) {
            float tx1 = (minX - rayOrigin.x) / rayDirection.x;
            float tx2 = (maxX - rayOrigin.x) / rayDirection.x;
            tMin = Math.max(tMin, Math.min(tx1, tx2));
            tMax = Math.min(tMax, Math.max(tx1, tx2));
        }
        
        // Check Y planes
        if (Math.abs(rayDirection.y) > 1e-6) {
            float ty1 = (minY - rayOrigin.y) / rayDirection.y;
            float ty2 = (maxY - rayOrigin.y) / rayDirection.y;
            tMin = Math.max(tMin, Math.min(ty1, ty2));
            tMax = Math.min(tMax, Math.max(ty1, ty2));
        }
        
        // Check Z planes
        if (Math.abs(rayDirection.z) > 1e-6) {
            float tz1 = (minZ - rayOrigin.z) / rayDirection.z;
            float tz2 = (maxZ - rayOrigin.z) / rayDirection.z;
            tMin = Math.max(tMin, Math.min(tz1, tz2));
            tMax = Math.min(tMax, Math.max(tz1, tz2));
        }
        
        return tMax >= tMin && tMax > 0;
    }

}
