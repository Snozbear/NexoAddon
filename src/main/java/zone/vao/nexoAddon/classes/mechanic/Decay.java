package zone.vao.nexoAddon.classes.mechanic;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class Decay {
  private final int time;
  private final double chance;
  private final List<Material> base;
  private final List<String> nexoBase;
  private final int radius;

  public Decay(final int time, final double chance, final List<Material> base, List<String> nexoBase, final int radius) {
    this.time = time;
    this.base = base;
    this.nexoBase = nexoBase;
    this.chance = chance;
    this.radius = radius;
  }
}
