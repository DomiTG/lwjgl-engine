package sh.hula.am.engine.ui;

import java.util.ArrayList;
import java.util.List;

public class UiRenderer {

    private List<UiComponent> components;

    public UiRenderer() {
        this.components = new ArrayList<>();
    }

    public void addComponent(UiComponent component) {
        components.add(component);
    }

    public void render() {
        for(UiComponent component : components) {
            System.out.println("Rendering component " + component);
            component.render();
        }
    }
    
}
