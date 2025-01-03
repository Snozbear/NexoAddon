package zone.vao.nexoAddon;

import co.aikar.commands.PaperCommandManager;
import io.th0rgal.protectionlib.ProtectionLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
import zone.vao.nexoAddon.events.*;
import zone.vao.nexoAddon.metrics.Metrics;
import zone.vao.nexoAddon.utils.BossBarUtil;
import zone.vao.nexoAddon.utils.ItemConfigUtil;
import zone.vao.nexoAddon.utils.PopulatorsConfigUtil;

import java.io.File;
import java.util.*;

@Getter
public final class NexoAddon extends JavaPlugin {

  @Getter
  public static NexoAddon instance;
  public Set<File> nexoFiles = new HashSet<>();
  public Map<String, Components> components = new HashMap<>();
  public Map<UUID, BossBarUtil> bossBars = new HashMap<>();
  public FileConfiguration globalConfig;
  public PopulatorsConfigUtil populatorsConfig;
  public List<Ore> ores = new ArrayList<>();
  public List<CustomTree> trees = new ArrayList<>();
  public final OrePopulator orePopulator = new OrePopulator();
  public final TreePopulator treePopulator = new TreePopulator();
  public Map<String, List<BlockPopulator>> worldPopulators = new HashMap<>();
  public Map<String, String> jukeboxLocations = new HashMap<>();
  public Map<String, Integer> customBlockLights = new HashMap<>();

  @Override
  public void onEnable() {
    instance = this;
    ProtectionLib.init(this);
    saveDefaultConfig();
    globalConfig = getConfig();
    initializeCommandManager();

    new BukkitRunnable() {

      @Override
      public void run() {
        initializePopulators();
        registerEvents();
        initializeMetrics();
      }
    }.runTaskAsynchronously(this);
  }

  @Override
  public void onDisable() {
    bossBars.values().forEach(BossBarUtil::removeBar);
    clearPopulators();
  }

  @Override
  public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
    return new CustomChunkGenerator(orePopulator);
  }

  public void reload() {
    new BukkitRunnable() {

      @Override
      public void run() {
        reloadConfig();
        globalConfig = getConfig();
        clearPopulators();
        initializePopulators();
        reloadNexoFiles();
        loadComponentsIfSupported();
        bossBars.values().forEach(BossBarUtil::removeBar);
      }
    }.runTaskAsynchronously(this);
  }

  private void initializeCommandManager() {
    PaperCommandManager manager = new PaperCommandManager(this);
    manager.registerCommand(new NexoAddonCommand());
  }

  private void initializePopulators() {
    populatorsConfig = new PopulatorsConfigUtil(getDataFolder(), getClassLoader());
    initializeOres();
    initializeTrees();
  }

  private void initializeOres() {
    ores = populatorsConfig.loadOresFromConfig();
    orePopulator.clearOres();
    ores.forEach(orePopulator::addOre);
    orePopulator.getOres().forEach(ore -> {
      if(ore.getNexoFurniture() != null) return;
      for (World world : ore.getWorlds()) {

        CustomOrePopulator customOrePopulator = new CustomOrePopulator(orePopulator);
        if(!worldPopulators.containsKey(world.getName())) {
          worldPopulators.put(world.getName(), new ArrayList<>());
        }
        addPopulatorToWorld(world, customOrePopulator);
        worldPopulators.get(world.getName()).add(customOrePopulator);
        logPopulatorAdded("BlockPopulator", ore.getId(), world);
      }
    });
  }

  private void initializeTrees() {
    trees = populatorsConfig.loadTreesFromConfig();
    treePopulator.clearTrees();
    trees.forEach(treePopulator::addTree);
    treePopulator.getTrees().forEach(tree -> {
      for (World world : tree.getWorlds()) {
        CustomTreePopulator customTreePopulator = new CustomTreePopulator(treePopulator);
        if(!worldPopulators.containsKey(world.getName())) {
          worldPopulators.put(world.getName(), new ArrayList<>());
        }
        addPopulatorToWorld(world, customTreePopulator);
        worldPopulators.get(world.getName()).add(customTreePopulator);
        logPopulatorAdded("TreePopulator", tree.getId(), world);
      }
    });
  }

  private void registerEvents() {
    registerEvent(new NexoItemsLoadedListener());
    registerEvent(new PlayerInteractListener());
    registerEvent(new PlayerMovementListener());
    registerEvent(new NexoFurnitureBreakListener());
    registerEvent(new BlockBreakListener());
    registerEvent(new NexoFurnitureInteractListener());
    registerEvent(new ChunkLoadListener());
  }

  private void initializeMetrics() {
    new Metrics(this, 24168);
  }

  private void reloadNexoFiles() {
    nexoFiles.clear();
    nexoFiles.addAll(ItemConfigUtil.getItemFiles());
  }

  private void loadComponentsIfSupported() {
    ItemConfigUtil.loadComponents();
  }

  private void clearPopulators() {
    worldPopulators.forEach((worldName, populators) -> {
      World world = Bukkit.getWorld(worldName);
      if (world == null) {
        Bukkit.getLogger().warning("World '" + worldName + "' not found. Skipping populator removal.");
        return;
      }
      populators.forEach(populator -> {
        if (world.getPopulators().remove(populator)) {
          Bukkit.getLogger().info("Populator removed from world: " + worldName);
        }
      });
    });
    worldPopulators.clear();
  }


  public void addPopulatorToWorld(World world, BlockPopulator populator) {
    if (world == null) {
      Bukkit.getLogger().severe("World is null. Cannot add Populator.");
      return;
    }
    if (!world.getPopulators().contains(populator)) {
      world.getPopulators().add(populator);
    }
  }

  private void logPopulatorAdded(String type, String name, World world) {
    Bukkit.getLogger().info(type + " of "+name+" added to world: " + world.getName());
  }

  private void registerEvent(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
