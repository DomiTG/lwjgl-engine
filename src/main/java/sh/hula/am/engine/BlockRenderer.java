package sh.hula.am.engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class BlockRenderer {

    private int vaoId;
    private int vboId;
    private int eboId;
    private int vertexCount;
    private ShaderProgram shader;

    private List<Block> blocks = new ArrayList<>();
    
    // Vertex data for a cube
    private static final float[] VERTICES = {
        // Front face
        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, // Top left
         0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // Top right
         0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // Bottom right
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // Bottom left
        
        // Back face
        -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
        
        // Top face
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
        
        // Bottom face
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        
        // Right face
         0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
         0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        
        // Left face
        -0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,  1.0f, 0.0f
    };
    
    private static final int[] INDICES = {
        0,  1,  2,  2,  3,  0,  // Front
        4,  5,  6,  6,  7,  4,  // Back
        8,  9,  10, 10, 11, 8,  // Top
        12, 13, 14, 14, 15, 12, // Bottom
        16, 17, 18, 18, 19, 16, // Right
        20, 21, 22, 22, 23, 20  // Left
    };

    public BlockRenderer() {
        setupShader();
        setupMesh();
    }

    private void setupShader() {
        shader = new ShaderProgram();
        try {
            String vertexShader = ShaderUtils.loadResource("block_vertex.glsl");
            String fragmentShader = ShaderUtils.loadResource("block_fragment.glsl");
            shader.createVertexShader(vertexShader);
            shader.createFragmentShader(fragmentShader);
        } catch(Exception e) {
            e.printStackTrace();
        }
        shader.link();
        
        // Create uniforms for world and projection matrices
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("modelMatrix");
        shader.createUniform("color");
    }
    
    private void setupMesh() {
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            // Create VAO
            vaoId = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoId);
            
            // Create VBO
            vboId = GL30.glGenBuffers();
            verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.length);
            verticesBuffer.put(VERTICES).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verticesBuffer, GL30.GL_STATIC_DRAW);
            
            // Create EBO
            eboId = GL30.glGenBuffers();
            indicesBuffer = BufferUtils.createIntBuffer(INDICES.length);
            indicesBuffer.put(INDICES).flip();
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);
            
            // Position attribute
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * Float.BYTES, 0);
            GL30.glEnableVertexAttribArray(0);
            
            // Texture coordinate attribute
            GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
            GL30.glEnableVertexAttribArray(1);
            
            vertexCount = INDICES.length;
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    
    public void render(Camera camera, Block block) {
        shader.bind();
        
        // Bind texture
        //need to return Matrix4f
        shader.setUniform("color", block.getColor().x, block.getColor().y, block.getColor().z, 1.0f);
        // Update uniforms
        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        shader.setUniform("viewMatrix", camera.getViewMatrix());
        shader.setUniform("modelMatrix", block.getModelMatrix());
        
        // Bind VAO
        GL30.glBindVertexArray(vaoId);
        
        // Draw
        GL30.glDrawElements(GL30.GL_TRIANGLES, vertexCount, GL30.GL_UNSIGNED_INT, 0);
        
        // Restore state
        GL30.glBindVertexArray(0);
        shader.unbind();
    }
    
    public void cleanup() {
        shader.cleanup();
        GL30.glDeleteVertexArrays(vaoId);
        GL30.glDeleteBuffers(vboId);
        GL30.glDeleteBuffers(eboId);
    }
}