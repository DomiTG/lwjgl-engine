package sh.hula.am.engine;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

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

    public Engine() {
        this.window = new Window(800, 600, "Hula Engine");
        this.timer = new Timer(60);
        this.frameCounter = new FrameCounter();
        this.camera = new Camera(0, 0, 1);
    }

    private void sampleWorld() {
        //green color

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
        if(this.window == null) {
            throw new IllegalStateException("Window is null");
        }
        this.window.createDisplay();
        if(this.window.getWindow() == 0) {
            throw new IllegalStateException("Window is null");
        }
        this.blockRenderer = new BlockRenderer();
        this.flower = new Rose(new Vector3f(0, 0.6f, 0));
        this.world = new World();
        while(!this.window.shouldClose()) {
            this.timer.update();
            this.handleMouseInput(this.camera);
            this.camera.update(this.window, this.timer.getDeltaTime());
            this.frameCounter.update();
            this.window.setTitle("Hula Engine | FPS: " + this.frameCounter.getFPS());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            int canSee = 0;
            int cannotSee = 0;
            for(Block block : world.getBlocks()) {
                if(this.camera.isVisible(block)) {
                    blockRenderer.renderWithOutline(camera, block);
                    canSee++;
                } else {
                    cannotSee++;
                }
            }
            System.out.println("Can see: " + canSee + " Cannot see: " + cannotSee);
            for (Block block : flower.getBlocks()) {
                blockRenderer.render(camera, block);
            }

            this.window.updateDisplay();
        }

        this.window.closeDisplay();
    }
}
