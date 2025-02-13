package sh.hula.am.engine;

public class FrameCounter {
    private int fps;
    private int frameCount;
    private double lastTime;

    public FrameCounter() {
        lastTime = System.nanoTime() / 1_000_000_000.0;
        frameCount = 0;
        fps = 0;
    }

    public void update() {
        frameCount++;
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        if (currentTime - lastTime >= 1.0) {
            fps = frameCount;
            frameCount = 0;
            lastTime = currentTime;
        }
    }

    public int getFPS() {
        return fps;
    }
}