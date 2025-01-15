package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.List;

@Getter
public class Infested {
    private final List<EntityType> entities;
    private final List<String> mythicMobs;
    private final double probability;
    private final String selector;
    private final boolean particles;
    private final boolean drop;

    public Infested(final List<EntityType> entities, final List<String> mythicMobs, final double probability, final String selector, final boolean particles, final boolean drop) {
        this.entities = entities;
        this.mythicMobs = mythicMobs;
        this.probability = probability;
        this.selector = selector;
        this.particles = particles;
        this.drop = drop;
    }
}
