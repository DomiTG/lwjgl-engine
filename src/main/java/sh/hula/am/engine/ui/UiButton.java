package sh.hula.am.engine.ui;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import sh.hula.am.engine.ShaderProgram;
import sh.hula.am.engine.ShaderUtils;

public class UiButton extends UiComponent {

    private String text;
    private ShaderProgram shader;

    private int vao;
    private int vbo;
    private static final float[] VERTICES = {
            0.0f, 0.0f, // Bottom-left
            1.0f, 0.0f, // Bottom-right
            1.0f, 1.0f, // Top-right
            0.0f, 1.0f // Top-left
    };
    private static final int[] INDICES = {
            0, 1, 2,
            2, 3, 0
    };

    public UiButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
        initShader();
        initBuffers();
    }

    private void initShader() {
        try {
            shader = new ShaderProgram();
            shader.createVertexShader(ShaderUtils.loadResource("ui_vertex.glsl"));
            shader.createFragmentShader(ShaderUtils.loadResource("ui_fragment.glsl"));
            shader.link();

            // Create uniforms
            shader.createUniform("uiPosition");
            shader.createUniform("uiScale");
            shader.createUniform("color");
        } catch (Exception e) {
            throw new RuntimeException("Error initializing button shader", e);
        }
    }

    private void initBuffers() {
        // Create VAO
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Create VBO
        vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, VERTICES, GL30.GL_STATIC_DRAW);

        // Define vertex attributes
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
        GL30.glEnableVertexAttribArray(0);

        // Create EBO
        int ebo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, INDICES, GL30.GL_STATIC_DRAW);

        // Unbind
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void render() {
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        shader.bind();

        // Convert pixel coordinates to normalized device coordinates (-1 to 1)
        float normalizedX = (getX() / 400.0f) - 1.0f; // assuming window width 800
        float normalizedY = 1.0f - (getY() / 300.0f); // assuming window height 600
        float normalizedWidth = getWidth() / 400.0f; // scale width to NDC
        float normalizedHeight = getHeight() / 300.0f; // scale height to NDC

        // Set uniforms with normalized coordinates
        shader.setUniform("uiPosition", normalizedX, normalizedY);
        shader.setUniform("uiScale", normalizedWidth, normalizedHeight);
        if (isHovered(normalizedX, normalizedY, normalizedWidth, normalizedHeight)) {
            shader.setUniform("color", 0.0f, 1.0f, 0.0f, 1.0f); // Green color with full opacity
        } else {
            shader.setUniform("color", 1.0f, 0.0f, 0.0f, 1.0f); // Red color with full opacity
        }
        // Enable blending for transparency
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        // Render button
        GL30.glBindVertexArray(vao);
        GL30.glDrawElements(GL30.GL_TRIANGLES, INDICES.length, GL30.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);

        // Disable blending
        GL30.glDisable(GL30.GL_BLEND);
        GL30.glEnable(GL30.GL_DEPTH_TEST);

        shader.unbind();
    }

    private boolean isHovered(float normalisedX, float normalisedY, float normalisedWidth, float normalisedHeight) {
        long window = GLFW.glfwGetCurrentContext();
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
    
        // Get cursor position
        GLFW.glfwGetCursorPos(window, xBuffer, yBuffer);
        double mouseX = xBuffer.get(0);
        double mouseY = yBuffer.get(0);
    
        // Convert cursor position to normalized device coordinates
        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
        
        double normalizedMouseX = (mouseX / windowWidth[0]) * 2.0 - 1.0;
        double normalizedMouseY = 1.0 - (mouseY / windowHeight[0]) * 2.0;
    
        // Check if mouse is within button bounds
        return normalizedMouseX >= normalisedX && 
               normalizedMouseX <= (normalisedX + normalisedWidth) && 
               normalizedMouseY >= normalisedY && 
               normalizedMouseY <= (normalisedY + normalisedHeight);
    }

    @Override
    public void cleanup() {
        GL30.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
        shader.cleanup();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
