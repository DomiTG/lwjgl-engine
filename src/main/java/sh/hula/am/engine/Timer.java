package sh.hula.am.engine;

public class Timer {

    private double lastTime;
    private float deltaTime;
    private double frameTime; // Time per frame in seconds

    public Timer(int fps) {
        this.frameTime = 1.0 / fps;
        lastTime = getTime();
    }

    public void update() {
        float currentTime = (float) getTime();
        deltaTime = currentTime - (float) lastTime;

        // Enforce max FPS
        double sleepTime = frameTime - deltaTime;
        if (sleepTime > 0) {
            try {
                Thread.sleep((long) (sleepTime * 1000)); // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lastTime = getTime(); // Update lastTime after sleeping
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
