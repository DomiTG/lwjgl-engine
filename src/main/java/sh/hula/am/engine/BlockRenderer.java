package sh.hula.am.engine;

import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BlockRenderer {
    private int vaoId;
    private int vboId;
    private int eboId;
    private int shaderProgram;

    public void init() {
        // Vertex data for a cube (positions only)
        float[] vertices = {
                // Front face
                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                // Back face
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f
        };

        // Index data
        int[] indices = {
                // Front
                0, 1, 2,
                2, 3, 0,
                // Right
                1, 5, 6,
                6, 2, 1,
                // Back
                5, 4, 7,
                7, 6, 5,
                // Left
                4, 0, 3,
                3, 7, 4,
                // Top
                3, 2, 6,
                6, 7, 3,
                // Bottom
                4, 5, 1,
                1, 0, 4
        };

        // Create and compile vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,
                "#version 410 core\n" +
                        "layout (location = 0) in vec3 position;\n" +
                        "uniform mat4 model;\n" +
                        "uniform mat4 view;\n" +
                        "uniform mat4 projection;\n" +
                        "void main() {\n" +
                        "    gl_Position = projection * view * model * vec4(position, 1.0);\n" +
                        "}"
        );
        glCompileShader(vertexShader);

        // Create and compile fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,
                "#version 410 core\n" +
                        "out vec4 FragColor;\n" +
                        "void main() {\n" +
                        "    FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" + // Green color
                        "}"
        );
        glCompileShader(fragmentShader);

        // Create shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        // Clean up shaders
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        // Create VAO, VBO, and EBO
        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        eboId = glGenBuffers();

        // Bind VAO
        glBindVertexArray(vaoId);

        // Put vertices in VBO
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Put indices in EBO
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Set vertex attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Unbind VAO
        glBindVertexArray(0);
    }

    public void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) {
        glUseProgram(shaderProgram);

        // Set uniforms
        int modelLoc = glGetUniformLocation(shaderProgram, "model");
        int viewLoc = glGetUniformLocation(shaderProgram, "view");
        int projLoc = glGetUniformLocation(shaderProgram, "projection");

        glUniformMatrix4fv(modelLoc, false, modelMatrix);
        glUniformMatrix4fv(viewLoc, false, viewMatrix);
        glUniformMatrix4fv(projLoc, false, projectionMatrix);

        // Draw cube
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteProgram(shaderProgram);
    }
}