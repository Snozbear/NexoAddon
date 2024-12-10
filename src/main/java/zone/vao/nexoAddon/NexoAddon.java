package zone.vao.nexoAddon;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.commands.NexoAddonCommand;
import zone.vao.nexoAddon.events.NexoFurnitureBreakListener;
import zone.vao.nexoAddon.events.NexoItemsLoadedListener;
import zone.vao.nexoAddon.events.PlayerInteractListener;
import zone.vao.nexoAddon.events.PlayerMovementListener;
import zone.vao.nexoAddon.utils.BossBarUtil;
import zone.vao.nexoAddon.utils.ItemConfigUtil;
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
  @Getter
  private static NexoAddon instance;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    saveDefaultConfig();
    this.globalConfig = getConfig();

    if(VersionUtil.isVersionLessThan("1.21.2")){
      this.componentSupport = true;
    }
    PaperCommandManager manager = new PaperCommandManager(this);
    manager.registerCommand(new NexoAddonCommand());

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


  public void reload(){
    this.reloadConfig();
    this.globalConfig = getConfig();

    this.getNexoFiles().clear();
    this.getNexoFiles().addAll(ItemConfigUtil.getItemFiles());

    if(this.isComponentSupport()){

      ItemConfigUtil.loadComponents();
    }
  }
}
