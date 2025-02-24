package zone.vao.nexoAddon.mechanics;

import org.bukkit.Particle;

public record BlockAura(Particle particle, String xOffset, String yOffset, String zOffset, int amount, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
}