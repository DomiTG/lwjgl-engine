package sh.hula.am.engine.ui;

public abstract class UiComponent {

    private int x;
    private int y;
    private int width;
    private int height;

    public UiComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render();
    public abstract void cleanup();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}
