package zone.vao.nexoAddon.classes.populators.orePopulator;

import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.List;

@Getter
public class Ore {

  public int maxLevel;
  public int minLevel;
  public CustomBlockMechanic nexoBlocks;
  public double chance;
  public List<Material> replace;
  public List<Material> placeOn;
  public List<World> worlds;
  public List<Biome> biomes;
  int iterations;

  public Ore(CustomBlockMechanic nexoBlocks, int minLevel, int maxLevel, double chance, List<Material> replace, List<Material> placeOn, List<World> worlds, List<Biome> biomes, int iterations) {
    this.nexoBlocks = nexoBlocks;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.chance = chance;
    this.replace = replace;
    this.worlds = worlds;
    this.biomes = biomes;
    this.placeOn = placeOn;
    this.iterations = iterations;
  }
}
