package sh.hula.am.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private Vector3f position;
    private float pitch;  // rotation around X-axis
    private float yaw;    // rotation around Y-axis
    private float roll;   // rotation around Z-axis
    private float moveSpeed = 0.05f;
    private float mouseSensitivity = 0.1f;
    
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    
    public Camera(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.pitch = 0;
        this.yaw = 0;
        this.roll = 0;
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        createProjectionMatrix();
    }
    
    public void update(Window window) {
        // Handle keyboard input
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            moveForward();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            moveBackward();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            moveLeft();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            moveRight();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            moveUp();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            moveDown();
        }
    
        
        // Update view matrix
        updateViewMatrix();
    }
    
    private void updateViewMatrix() {
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0))
                 .rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0))
                 .rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1))
                 .translate(-position.x, -position.y, -position.z);
    }
    
    private void createProjectionMatrix() {
        float aspectRatio = 16.0f / 9.0f;  // Adjust based on your window
        float FOV = 70;  // Field of view in degrees
        float nearPlane = 0.1f;
        float farPlane = 1000f;
        
        projectionMatrix.identity();
        projectionMatrix.perspective((float) Math.toRadians(FOV), aspectRatio, nearPlane, farPlane);
    }
    
    private void moveForward() {
        float dx = -(float) Math.sin(Math.toRadians(yaw)) * moveSpeed;
        float dz = -(float) Math.cos(Math.toRadians(yaw)) * moveSpeed;
        position.add(dx, 0, dz);
    }
    
    private void moveBackward() {
        float dx = (float) Math.sin(Math.toRadians(yaw)) * moveSpeed;
        float dz = (float) Math.cos(Math.toRadians(yaw)) * moveSpeed;
        position.add(dx, 0, dz);
    }
    
    private void moveLeft() {
        float dx = -(float) Math.cos(Math.toRadians(yaw)) * moveSpeed;
        float dz = (float) Math.sin(Math.toRadians(yaw)) * moveSpeed;
        position.add(dx, 0, dz);
    }
    
    private void moveRight() {
        float dx = (float) Math.cos(Math.toRadians(yaw)) * moveSpeed;
        float dz = -(float) Math.sin(Math.toRadians(yaw)) * moveSpeed;
        position.add(dx, 0, dz);
    }
    
    private void moveUp() {
        position.add(0, moveSpeed, 0);
    }
    
    private void moveDown() {
        position.add(0, -moveSpeed, 0);
    }
    
    // Getters
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    // Setters for customization
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    
    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }
}