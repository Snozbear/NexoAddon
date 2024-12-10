package zone.vao.nexoAddon;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.events.NexoItemsLoadedListener;
import zone.vao.nexoAddon.events.PlayerInteractListener;
import zone.vao.nexoAddon.utils.VersionUtil;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public final class NexoAddon extends JavaPlugin {

  public boolean componentSupport = false;
  Set<File> nexoFiles = new HashSet<>();
  Map<String, Components> components = new HashMap<>();
  @Getter
  private static NexoAddon instance;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    if(VersionUtil.isVersionLessThan("1.21.2")){
      this.componentSupport = true;
    }

    getServer().getPluginManager()
        .registerEvents(new NexoItemsLoadedListener(), this);
    getServer().getPluginManager()
        .registerEvents(new PlayerInteractListener(), this);

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

}
