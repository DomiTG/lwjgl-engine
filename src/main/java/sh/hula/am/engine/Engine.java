package sh.hula.am.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Engine {

    private Window window;

    public Engine() {
        this.window = new Window(800, 600, "Hula Engine");
    }

    public void run() {
        if(this.window == null) {
            throw new IllegalStateException("Window is null");
        }
        this.window.createDisplay();
        if(this.window.getWindow() == 0) {
            throw new IllegalStateException("Window is null");
        }
        while(!this.window.shouldClose() && GLFW.glfwGetCurrentContext() != 0) {
            this.window.getFrameCounter().update();
            this.window.updateDisplay();
        }
        this.window.closeDisplay();
    }
}
