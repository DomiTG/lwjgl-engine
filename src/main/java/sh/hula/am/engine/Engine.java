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

    private int CHUNK_SIZE = 16;
    private int WORLD_SIZE = 16;

    public Engine() {
        this.window = new Window(800, 600, "Hula Engine");
        this.timer = new Timer(60);
        this.frameCounter = new FrameCounter();
        this.camera = new Camera(0, 0, 1);
    }

    private void sampleWorld() {
        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int y = 0; y < WORLD_SIZE; y++) {
                for (int z = 0; z < WORLD_SIZE; z++) {
                    if (Math.random() < 0.5) {
                        //random color
                        float r = (float) Math.random();
                        float g = (float) Math.random();
                        float b = (float) Math.random();

                        blocks.add(new Block(new Vector3f(x, y, z), new Vector3f(r, g, b)));
                    }
                }
            }
        } 
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
        sampleWorld();
        while(!this.window.shouldClose()) {
            this.timer.update();
            this.camera.update(this.window);            
            this.frameCounter.update();
            this.window.setTitle("Hula Engine | FPS: " + this.frameCounter.getFPS());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            for (Block block : blocks) {
                blockRenderer.render(camera, block);
            }

            this.window.updateDisplay();
        }

        this.window.closeDisplay();
    }
}
