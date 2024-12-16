package zone.vao.nexoAddon;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.classes.populators.CustomChunkGenerator;
import zone.vao.nexoAddon.classes.populators.orePopulator.CustomOrePopulator;
import zone.vao.nexoAddon.classes.populators.orePopulator.Ore;
import zone.vao.nexoAddon.classes.populators.orePopulator.OrePopulator;
import zone.vao.nexoAddon.classes.populators.treePopulator.CustomTree;
import zone.vao.nexoAddon.classes.populators.treePopulator.CustomTreePopulator;
import zone.vao.nexoAddon.classes.populators.treePopulator.TreePopulator;
import zone.vao.nexoAddon.commands.NexoAddonCommand;
import zone.vao.nexoAddon.events.NexoFurnitureBreakListener;
import zone.vao.nexoAddon.events.NexoItemsLoadedListener;
import zone.vao.nexoAddon.events.PlayerInteractListener;
import zone.vao.nexoAddon.events.PlayerMovementListener;
import zone.vao.nexoAddon.utils.BossBarUtil;
import zone.vao.nexoAddon.utils.ItemConfigUtil;
import zone.vao.nexoAddon.utils.PopulatorsConfigUtil;
import zone.vao.nexoAddon.utils.VersionUtil;

import java.io.File;
import java.util.*;

@Getter
public final class NexoAddon extends JavaPlugin {

  public boolean componentSupport = false;
  Set<File> nexoFiles = new HashSet<>();
  Map<String, Components> components = new HashMap<>();
  Map<UUID, BossBarUtil> bossBars = new HashMap<>();
  FileConfiguration globalConfig;
  PopulatorsConfigUtil populatorsConfig;
  List<Ore> ores = new ArrayList<>();
  List<CustomTree> trees = new ArrayList<>();

  @Getter
  private static NexoAddon instance;
  private final OrePopulator orePopulator = new OrePopulator();
  private final TreePopulator treePopulator = new TreePopulator();

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    saveDefaultConfig();
    this.globalConfig = getConfig();

    this.populatorsConfig = new PopulatorsConfigUtil(getDataFolder(), getClassLoader());

    if(VersionUtil.isVersionLessThan("1.21.2")){
      this.componentSupport = true;
    }
    PaperCommandManager manager = new PaperCommandManager(this);
    manager.registerCommand(new NexoAddonCommand());

    ores = this.populatorsConfig.loadOresFromConfig();
    trees = this.populatorsConfig.loadTreesFromConfig();
    for (Ore ore : ores) {
      orePopulator.addOre(ore);
    }
    for (CustomTree tree : trees) {
      treePopulator.addTree(tree);
    }
    for (Ore ore : orePopulator.getOres()) {
      for (World world : ore.getWorlds()) {

        addPopulatorToWorld(world, new CustomOrePopulator(orePopulator));
        Bukkit.getLogger().info("BlockPopulator added to world: " + world.getName());
      }
    }
    for (CustomTree tree : treePopulator.getTrees()) {
      for (World world : tree.getWorlds()) {

        addPopulatorToWorld(world, new CustomTreePopulator(treePopulator));
        Bukkit.getLogger().info("TreePopulator added to world: " + world.getName());
      }
    }

    getServer().getPluginManager()
        .registerEvents(new NexoItemsLoadedListener(), this);
    getServer().getPluginManager()
        .registerEvents(new PlayerInteractListener(), this);
    getServer().getPluginManager()
        .registerEvents(new PlayerMovementListener(), this);
    getServer().getPluginManager()
        .registerEvents(new NexoFurnitureBreakListener(), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    getBossBars().forEach((uuid, bossBar) -> bossBar.removeBar());

  }

  @Override
  public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
    return new CustomChunkGenerator(orePopulator);
  }


  public void reload(){
    this.reloadConfig();
    this.globalConfig = getConfig();

    for (World world : Bukkit.getWorlds()) {
      world.getPopulators().clear();
    }

    this.populatorsConfig = new PopulatorsConfigUtil(getDataFolder(), getClassLoader());
    this.ores = populatorsConfig.loadOresFromConfig();
    orePopulator.clearOres();
    for (Ore ore : ores) {
      orePopulator.addOre(ore);
    }
    for (Ore ore : orePopulator.getOres()) {
      for (World world : ore.getWorlds()) {
        addPopulatorToWorld(world, new CustomOrePopulator(orePopulator));
        Bukkit.getLogger().info("BlockPopulator added to world: " + world.getName());
      }
    }
    this.trees = populatorsConfig.loadTreesFromConfig();
    treePopulator.clearTrees();
    for (CustomTree tree : trees) {
      treePopulator.addTree(tree);
    }
    for (CustomTree tree : treePopulator.getTrees()) {
      for (World world : tree.getWorlds()) {
        addPopulatorToWorld(world, new CustomTreePopulator(treePopulator));
        Bukkit.getLogger().info("TreePopulator added to world: " + world.getName());
      }
    }

    this.getNexoFiles().clear();
    this.getNexoFiles().addAll(ItemConfigUtil.getItemFiles());

    if(this.isComponentSupport()){

      ItemConfigUtil.loadComponents();
    }
  }

  public void addPopulatorToWorld(World world, BlockPopulator populator) {
    if (world == null) {
      Bukkit.getLogger().severe("World is null. Cannot add Populator.");
      return;
    }

    if(world.getPopulators().contains(populator)) return;
    world.getPopulators().add(populator);
  }
}
