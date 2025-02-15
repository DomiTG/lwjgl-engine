package sh.hula.am.engine;

import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class World {

    private List<Block> blocks = new ArrayList<>();

    public World() {
        for(int x = 0; x < 10; x++) {
            for(int z = 0; z < 10; z++) {
                    blocks.add(new Block(new Vector3f(x, 0, z), new Vector3f(0, 1, 0), 1));
            }
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}