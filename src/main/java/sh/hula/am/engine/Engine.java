package sh.hula.am.engine;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import sh.hula.am.engine.ui.UiButton;
import sh.hula.am.engine.ui.UiRenderer;

public class Engine {

    private Window window;
    private Timer timer;
    private FrameCounter frameCounter;
    private Camera camera;

    private BlockRenderer blockRenderer;
    private List<Block> blocks = new ArrayList<>();

    private Rose flower;
    private World world;

    private double lastX, lastY;
    private boolean firstMouse = true;

    private UiRenderer uiRenderer;

    public Engine() {
        this.window = new Window(800, 600, "Hula Engine");
        this.timer = new Timer(60);
        this.frameCounter = new FrameCounter();
        this.camera = new Camera(0, 0, 1);
        this.uiRenderer = new UiRenderer();
    }

    private void sampleWorld() {
        // green color

        blocks.add(new Block(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), 1));
    }

    public void handleMouseInput(Camera camera) {
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window.getWindow(), xpos, ypos);

        if (firstMouse) {
            lastX = xpos[0];
            lastY = ypos[0];
            firstMouse = false;
            return;
        }

        double xoffset = xpos[0] - lastX;
        double yoffset = lastY - ypos[0];

        lastX = xpos[0];
        lastY = ypos[0];

        camera.handleMouseInput(xoffset, yoffset);
    }

    public void run() {
        if (this.window == null) {
            throw new IllegalStateException("Window is null");
        }
        this.window.createDisplay();
        if (this.window.getWindow() == 0) {
            throw new IllegalStateException("Window is null");
        }
        this.blockRenderer = new BlockRenderer();
        this.flower = new Rose(new Vector3f(0, 0.6f, 0));
        this.world = new World();
        this.uiRenderer.addComponent(new UiButton(100, 100, 100, 50, "Click me!"));
        
        while (!this.window.shouldClose()) {
            this.timer.update();
            this.handleMouseInput(this.camera);
            this.camera.update(this.window, this.timer.getDeltaTime());
            this.frameCounter.update();
            this.window.setTitle("Hula Engine | FPS: " + this.frameCounter.getFPS());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor(135/255f, 206/255f, 235/255f, 1.0f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            for (Block block : world.getBlocks()) {
                if(block.isHovered(this.camera)) {
                    blockRenderer.renderWithOutline(camera, block);
                } else {
                    blockRenderer.render(camera, block);
                }
            }
            this.uiRenderer.render();
            this.window.updateDisplay();
        }

        this.window.closeDisplay();
    }
}
