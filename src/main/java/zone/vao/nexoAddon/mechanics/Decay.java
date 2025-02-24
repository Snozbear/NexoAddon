package zone.vao.nexoAddon.mechanics;

import org.bukkit.Material;

import java.util.List;

public record Decay(int time, double chance, List<Material> base, List<String> nexoBase, int radius) {
}
