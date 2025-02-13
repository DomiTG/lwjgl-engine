package sh.hula.am.engine;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private float pitch;
    private float yaw;
    private float roll;
    private float moveSpeed = 5.0f; // Increased base speed
    private float mouseSensitivity = 0.1f;
    
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private FrustumIntersection frustum;
    
    public Camera(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.front = new Vector3f(0, 0, -1);
        this.up = new Vector3f(0, 1, 0);
        this.right = new Vector3f(1, 0, 0);
        this.pitch = 0;
        this.yaw = -90; // Makes camera face -Z by default
        this.roll = 0;
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.frustum = new FrustumIntersection();
        createProjectionMatrix();
    }
    
    public void update(Window window, float deltaTime) {
        // Handle keyboard input with delta time for smooth movement
        float actualSpeed = moveSpeed * deltaTime;
        
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            position.add(new Vector3f(front).mul(actualSpeed));
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            position.sub(new Vector3f(front).mul(actualSpeed));
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            position.sub(new Vector3f(right).mul(actualSpeed));
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            position.add(new Vector3f(right).mul(actualSpeed));
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            position.add(new Vector3f(up).mul(actualSpeed));
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            position.sub(new Vector3f(up).mul(actualSpeed));
        }
        
        updateViewMatrix();
    }
    
    public void handleMouseInput(double xoffset, double yoffset) {
        xoffset *= mouseSensitivity;
        yoffset *= mouseSensitivity;

        yaw += xoffset;
        pitch -= yoffset;

        // Constrain pitch to prevent camera flipping
        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;

        updateVectors();
    }
    
    private void updateVectors() {
        // Calculate new front vector
        float x = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
        float y = (float) Math.sin(Math.toRadians(pitch));
        float z = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
        front.set(x, y, z).normalize();
        
        // Recalculate right and up vectors
        right.set(front).cross(new Vector3f(0, 1, 0)).normalize();
        up.set(right).cross(front).normalize();
    }
    
    private void updateViewMatrix() {
        viewMatrix.identity()
                 .lookAt(position, 
                         new Vector3f(position).add(front), 
                         up);
        Matrix4f projectionViewMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix);
        frustum.set(projectionViewMatrix);
    }
    
    private void createProjectionMatrix() {
        float aspectRatio = 16.0f / 9.0f;
        float FOV = 70; 
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
    
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    
    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public boolean isVisible(Block block) {
        return frustum.testAab(block.getPosition().x, block.getPosition().y, block.getPosition().z, block.getPosition().x + block.getScale(), block.getPosition().y + block.getScale(), block.getPosition().z + block.getScale());
    }
}