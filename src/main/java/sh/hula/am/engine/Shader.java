package sh.hula.am.engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {

    private int program;
    private int vertexShader;
    private int fragmentShader;

    public Shader(String vertexSource, String fragmentSource) {
        this.program = GL20.glCreateProgram();
        this.vertexShader = compileShader(vertexSource, GL20.GL_VERTEX_SHADER);
        this.fragmentShader = compileShader(fragmentSource, GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(this.program, this.vertexShader);
        GL20.glAttachShader(this.program, this.fragmentShader);
        GL20.glLinkProgram(this.program);
        GL20.glValidateProgram(this.program);
    }

    private int compileShader(String source, int type) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Error: Could not compile shader.");
            System.err.println(GL20.glGetShaderInfoLog(shader));
            System.exit(1);
        }
        return shader;
    }

    public void bind() {
        GL20.glUseProgram(this.program);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        GL20.glDetachShader(this.program, this.vertexShader);
        GL20.glDetachShader(this.program, this.fragmentShader);
        GL20.glDeleteShader(this.vertexShader);
        GL20.glDeleteShader(this.fragmentShader);
        GL20.glDeleteProgram(this.program);
    }

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(this.program, name);
    }

    public void setUniform1i(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform1f(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public int getProgram() {
        return this.program;
    }

    public int getVertexShader() {
        return this.vertexShader;
    }

    public int getFragmentShader() {
        return this.fragmentShader;
    }
}
