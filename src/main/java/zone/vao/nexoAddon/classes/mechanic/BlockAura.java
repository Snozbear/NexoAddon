package zone.vao.nexoAddon.classes.mechanic;

import org.bukkit.Particle;

public record BlockAura(Particle particle, double xOffset, double yOffset, double zOffset, int amount, double deltaX, double deltaY, double deltaZ, double speed) {
}
