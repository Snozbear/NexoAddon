package zone.vao.nexoAddon.utils;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.populators.orePopulator.Ore;
import zone.vao.nexoAddon.classes.populators.treePopulator.CustomTree;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopulatorsConfigUtil {

  private final File pluginDirectory;
  private final ClassLoader pluginClassLoader;

  public PopulatorsConfigUtil(File pluginDirectory, ClassLoader pluginClassLoader) {
    this.pluginDirectory = pluginDirectory;
    this.pluginClassLoader = pluginClassLoader;
    createPopulatorFiles();
  }

  private void createPopulatorFiles() {
    File populatorsDir = new File(pluginDirectory, "populators");

    if(populatorsDir.exists())
      return;
    else
      populatorsDir.mkdirs();

    File blockPopulatorFile = new File(populatorsDir, "block_populator.yml");
    File treePopulatorFile = new File(populatorsDir, "tree_populator.yml");
    if (!blockPopulatorFile.exists()) {
      try (InputStream inputStream = pluginClassLoader.getResourceAsStream("block_populator.yml");
           OutputStream outputStream = new FileOutputStream(blockPopulatorFile)) {
        if (inputStream == null) {
          return;
        }
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      } catch (IOException e) {
        NexoAddon.getInstance().getLogger().severe("Failed to copy block_populator.yml: " + e.getMessage());
      }
    }
    if (!treePopulatorFile.exists()) {
      try (InputStream inputStream = pluginClassLoader.getResourceAsStream("tree_populator.yml");
           OutputStream outputStream = new FileOutputStream(treePopulatorFile)) {
        if (inputStream == null) {
          return;
        }
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      } catch (IOException e) {
        NexoAddon.getInstance().getLogger().severe("Failed to copy block_populator.yml: " + e.getMessage());
      }
    }
  }

  public List<FileConfiguration> loadPopulatorConfigs() {
    List<FileConfiguration> configurations = new ArrayList<>();
    File populatorsDir = new File(pluginDirectory, "populators");

    if (!populatorsDir.exists()) {
      NexoAddon.getInstance().getLogger().severe("Populators directory does not exist.");
      return configurations;
    }

    File[] files = populatorsDir.listFiles((dir, name) -> name.endsWith(".yml"));
    if (files == null) {
      NexoAddon.getInstance().getLogger().severe("Failed to list files in the populators directory.");
      return configurations;
    }

    for (File file : files) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      configurations.add(config);
    }

    return configurations;
  }

  public List<Ore> loadOresFromConfig() {
    File file = new File(pluginDirectory, "populators/block_populator.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    List<Ore> ores = new ArrayList<>();

    for (String key : config.getKeys(false)) {
      ConfigurationSection section = config.getConfigurationSection(key);
      if (section == null) continue;

//      System.out.println("isCustomBlock: "+NexoBlocks.isCustomBlock(key));
      if (!NexoBlocks.isCustomBlock(key)) continue;
      int maxY = section.getInt("maxY");
      int minY = section.getInt("minY");
      maxY = Math.max(maxY, minY);
      minY = Math.min(maxY, minY);
      double chance = section.getDouble("chance", 0.1);
      int iterations = Math.abs(section.getInt("iterations", 50));
      List<String> worldRaw = section.getStringList("worlds");
      List<String> biomesRaw = section.getStringList("biomes");
//      System.out.println("worldRaw: "+worldRaw.toString());
      List<World> worlds = new ArrayList<>();
      for (String s : worldRaw) {
        s = s.trim();
        World world = Bukkit.getWorld(s);
        if(world == null) continue;
        worlds.add(world);
      }
      List<Biome> biomes = new ArrayList<>();
      for (String s : biomesRaw) {
        s = s.trim().toUpperCase();
        Biome biome = Biome.valueOf(s);
        biomes.add(biome);
      }
      if(biomes.isEmpty()) biomes = Arrays.stream(Biome.values()).toList();

      if(worlds.isEmpty()) continue;

      if(!section.contains("replace") && !section.contains("place_on")) continue;
      List<Material> replaceMaterialsFinal = new ArrayList<>();
      List<Material> placeOnMaterialsFinal = new ArrayList<>();
      if(section.contains("replace")) {
        List<String> replaceMaterials = section.getStringList("replace");
        if (replaceMaterials.isEmpty()) {
          replaceMaterials.add(Material.AIR.name());
        }

        replaceMaterialsFinal = new ArrayList<>();
        for (String replaceMaterial : replaceMaterials) {
          replaceMaterialsFinal.add(Material.getMaterial(replaceMaterial));
        }
      }
      else if(section.contains("place_on")) {
        List<String> placeOnMaterials = section.getStringList("place_on");
        if (placeOnMaterials.isEmpty()) continue;

        for (String replaceMaterial : placeOnMaterials) {
          placeOnMaterialsFinal.add(Material.getMaterial(replaceMaterial));
        }
      }

      if(replaceMaterialsFinal.isEmpty()) replaceMaterialsFinal = null;
      if(placeOnMaterialsFinal.isEmpty()) placeOnMaterialsFinal = null;

      System.out.println(replaceMaterialsFinal != null ? replaceMaterialsFinal.toString(): replaceMaterialsFinal);
      System.out.println(placeOnMaterialsFinal != null ? placeOnMaterialsFinal.toString(): placeOnMaterialsFinal);

      try {
        CustomBlockMechanic nexoBlock = NexoBlocks.customBlockMechanic(key);
        Ore ore = new Ore(nexoBlock, minY, maxY, chance, replaceMaterialsFinal, placeOnMaterialsFinal, worlds, biomes, iterations);
        ores.add(ore);
      } catch (IllegalArgumentException e) {
        NexoAddon.getInstance().getLogger().severe("Invalid custom block ID: " + key);
      }
    }
    return ores;
  }

  public List<CustomTree> loadTreesFromConfig() {
    File file = new File(pluginDirectory, "populators/tree_populator.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    List<CustomTree> trees = new ArrayList<>();

    for (String key : config.getKeys(false)) {
      ConfigurationSection section = config.getConfigurationSection(key);
      if (section == null) continue;

      if(!section.contains("logs") || !section.contains("leaves")) continue;
      String logs = section.getString("logs");
      String leaves = section.getString("leaves");

      if (!NexoBlocks.isCustomBlock(logs) || !NexoBlocks.isCustomBlock(leaves)) continue;
      int maxY = section.getInt("maxY");
      int minY = section.getInt("minY");
      maxY = Math.max(maxY, minY);
      minY = Math.min(maxY, minY);
      double chance = section.getDouble("chance", 0.1);
      List<String> worldRaw = section.getStringList("worlds");
      List<String> biomesRaw = section.getStringList("biomes");
      List<World> worlds = new ArrayList<>();
      for (String s : worldRaw) {
        s = s.trim();
        World world = Bukkit.getWorld(s);
        if(world == null) continue;
        worlds.add(world);
      }
      List<Biome> biomes = new ArrayList<>();
      for (String s : biomesRaw) {
        s = s.trim().toUpperCase();
        Biome biome = Biome.valueOf(s);
        biomes.add(biome);
      }
      if(biomes.isEmpty()) biomes = Arrays.stream(Biome.values()).toList();

      if(worlds.isEmpty()) continue;

      try {
        CustomBlockMechanic logsBlock = NexoBlocks.customBlockMechanic(logs);
        CustomBlockMechanic leavesBlock = NexoBlocks.customBlockMechanic(leaves);
        CustomTree tree = new CustomTree(logsBlock, leavesBlock, minY, maxY, chance, worlds, biomes);
        trees.add(tree);
      } catch (IllegalArgumentException e) {
        NexoAddon.getInstance().getLogger().severe("Invalid custom block ID: " + key);
      }
//      System.out.println(logs);
//      System.out.println(leaves);
//      System.out.println(maxY);
//      System.out.println(minY);
//      System.out.println(chance);
//      System.out.println(worlds);
//      System.out.println(biomes);
    }
    return trees;
  }
}
