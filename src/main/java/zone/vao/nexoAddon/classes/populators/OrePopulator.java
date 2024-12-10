package zone.vao.nexoAddon.classes.populators;

import com.nexomc.nexo.api.NexoBlocks;
import lombok.Getter;

@Getter
public class OrePopulator {

  public int maxLevel;
  public int minLevel;
  public NexoBlocks nexoBlocks;
  public double chance;

  public OrePopulator(NexoBlocks nexoBlocks, int minLevel, int maxLevel, double chance) {
    this.nexoBlocks = nexoBlocks;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.chance = chance;
  }
}
