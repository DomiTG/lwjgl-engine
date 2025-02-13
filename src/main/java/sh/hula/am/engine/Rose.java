package sh.hula.am.engine;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

public class Rose {
    private List<Block> blocks = new ArrayList<>();

    public Rose(Vector3f position) {
        // Stem (Green, with gradient)
        for (int i = 0; i < 10; i++) {
            float greenIntensity = 0.5f + (i * 0.02f);
            blocks.add(new Block(new Vector3f(position).add(0, i * 0.2f, 0), 
                               new Vector3f(0, greenIntensity, 0), 0.2f));
        }

        // Thorns (Dark Brown)
        blocks.add(new Block(new Vector3f(position).add(0.2f, 0.4f, 0), 
                           new Vector3f(0.2f, 0.1f, 0), 0.2f));
        blocks.add(new Block(new Vector3f(position).add(-0.2f, 0.8f, 0), 
                           new Vector3f(0.2f, 0.1f, 0), 0.2f));
        blocks.add(new Block(new Vector3f(position).add(0.2f, 1.2f, 0), 
                           new Vector3f(0.2f, 0.1f, 0), 0.2f));

        // Leaves (Green, with different shades)
        addLeaf(position, 0.6f, -1, 0.7f);
        addLeaf(position, 1.0f, 1, 0.65f);

        // Base petals (Darker Red)
        float baseHeight = 2.0f;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    blocks.add(new Block(new Vector3f(position).add(x * 0.2f, baseHeight, z * 0.2f), 
                                       new Vector3f(0.6f, 0, 0), 0.2f));
                }
            }
        }

        // Middle layer petals (Rich Red)
        float middleHeight = baseHeight + 0.2f;
        for (int i = 0; i < 8; i++) {
            float angle = (float) (i * Math.PI / 4);
            float x = (float) Math.cos(angle) * 0.2f;
            float z = (float) Math.sin(angle) * 0.2f;
            blocks.add(new Block(new Vector3f(position).add(x, middleHeight, z), 
                               new Vector3f(0.8f, 0, 0), 0.2f));
        }

        // Top layer petals (Light Red)
        float topHeight = middleHeight + 0.2f;
        blocks.add(new Block(new Vector3f(position).add(0, topHeight, 0), 
                           new Vector3f(0.9f, 0, 0), 0.2f));
        for (int i = 0; i < 4; i++) {
            float angle = (float) (i * Math.PI / 2);
            float x = (float) Math.cos(angle) * 0.2f;
            float z = (float) Math.sin(angle) * 0.2f;
            blocks.add(new Block(new Vector3f(position).add(x, topHeight - 0.1f, z), 
                               new Vector3f(0.85f, 0, 0), 0.2f));
        }
    }

    private void addLeaf(Vector3f position, float height, int direction, float green) {
        float x = direction * 0.2f;
        blocks.add(new Block(new Vector3f(position).add(x, height, 0), 
                           new Vector3f(0, green, 0), 0.2f));
        blocks.add(new Block(new Vector3f(position).add(x * 2, height, 0), 
                           new Vector3f(0, green - 0.1f, 0), 0.2f));
        blocks.add(new Block(new Vector3f(position).add(x * 1.5f, height + 0.2f, 0), 
                           new Vector3f(0, green - 0.05f, 0), 0.2f));
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}