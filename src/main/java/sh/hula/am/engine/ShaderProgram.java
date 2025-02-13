package sh.hula.am.engine;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        programId = GL30.glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    public void createVertexShader(String shaderCode) {
        vertexShaderId = createShader(shaderCode, GL30.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) {
        fragmentShaderId = createShader(shaderCode, GL30.GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderId = GL30.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        GL30.glShaderSource(shaderId, shaderCode);
        GL30.glCompileShader(shaderId);

        if (GL30.glGetShaderi(shaderId, GL30.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + GL30.glGetShaderInfoLog(shaderId, 1024));
        }

        GL30.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() {
        GL30.glLinkProgram(programId);
        if (GL30.glGetProgrami(programId, GL30.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + GL30.glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            GL30.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL30.glDetachShader(programId, fragmentShaderId);
        }

        GL30.glValidateProgram(programId);
        if (GL30.glGetProgrami(programId, GL30.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL30.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        GL30.glUseProgram(programId);
    }

    public void unbind() {
        GL30.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            GL30.glDeleteProgram(programId);
        }
    }

    public void createUniform(String uniformName) {
        int uniformLocation = GL30.glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            GL30.glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    //has to handle color
    public void setUniform(String uniformName, float r, float g, float b, float a) {
        GL30.glUniform4f(uniforms.get(uniformName), r, g, b, a);
    }

    public void setUniform(String uniformName, int value) {
        GL30.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        GL30.glUniform1f(uniforms.get(uniformName), value);
    }
}