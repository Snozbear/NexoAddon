package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;
import org.bukkit.Particle;

public record Aura(Particle particle, String type, String formula) {
}
