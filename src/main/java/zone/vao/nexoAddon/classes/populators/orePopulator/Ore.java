package zone.vao.nexoAddon.classes.populators.orePopulator;

import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.List;

@Getter
public class Ore {

  public String id;
  public int maxLevel;
  public int minLevel;
  public CustomBlockMechanic nexoBlocks;
  public FurnitureMechanic nexoFurnitures;
  public Material vanillaMaterial;
  public double chance;
  public List<Material> replace;
  public List<Material> placeOn;
  public List<World> worlds;
  public List<Biome> biomes;
  int iterations;
  boolean tall;
  int veinSize;
  double clusterChance;

  public Ore(String id, Material vanillaMaterial, int minLevel, int maxLevel, double chance, List<Material> replace, List<Material> placeOn, List<World> worlds, List<Biome> biomes, int iterations, boolean tall, int veinSize, double clusterChance) {
    this.id = id;
    this.vanillaMaterial = vanillaMaterial;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.chance = chance;
    this.replace = replace;
    this.worlds = worlds;
    this.biomes = biomes;
    this.placeOn = placeOn;
    this.iterations = iterations;
    this.tall = tall;
    this.veinSize = veinSize;
    this.clusterChance = clusterChance;
  }

  public Ore(String id, FurnitureMechanic nexoFurnitures, int minLevel, int maxLevel, double chance, List<Material> replace, List<Material> placeOn, List<World> worlds, List<Biome> biomes, int iterations, boolean tall, int veinSize, double clusterChance) {
    this.id = id;
    this.nexoFurnitures = nexoFurnitures;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.chance = chance;
    this.replace = replace;
    this.worlds = worlds;
    this.biomes = biomes;
    this.placeOn = placeOn;
    this.iterations = iterations;
    this.tall = tall;
    this.veinSize = veinSize;
    this.clusterChance = clusterChance;
  }

  public Ore(String id, CustomBlockMechanic nexoBlocks, int minLevel, int maxLevel, double chance, List<Material> replace, List<Material> placeOn, List<World> worlds, List<Biome> biomes, int iterations, boolean tall, int veinSize, double clusterChance) {
    this.id = id;
    this.nexoBlocks = nexoBlocks;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.chance = chance;
    this.replace = replace;
    this.worlds = worlds;
    this.biomes = biomes;
    this.placeOn = placeOn;
    this.iterations = iterations;
    this.tall = tall;
    this.veinSize = veinSize;
    this.clusterChance = clusterChance;
  }
}
