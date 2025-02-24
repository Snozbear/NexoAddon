package zone.vao.nexoAddon.mechanics;

import org.bukkit.entity.EntityType;

import java.util.List;

public record Infested(List<EntityType> entities, List<String> mythicMobs, double probability, String selector, boolean particles, boolean drop) {

}
