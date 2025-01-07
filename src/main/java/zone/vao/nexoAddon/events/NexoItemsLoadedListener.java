package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.events.NexoItemsLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.handlers.RecipeManager;
import zone.vao.nexoAddon.utils.ItemConfigUtil;

public class NexoItemsLoadedListener implements Listener {

  private final RecipeManager recipeManager;

  public NexoItemsLoadedListener(RecipeManager recipeManager) {
    this.recipeManager = recipeManager;
  }

  @EventHandler
  public void on(NexoItemsLoadedEvent event) {

    new BukkitRunnable() {

      @Override
      public void run() {
        NexoAddon.getInstance().getNexoFiles().clear();
        NexoAddon.getInstance().getNexoFiles().addAll(ItemConfigUtil.getItemFiles());

        ItemConfigUtil.loadComponents();
        ItemConfigUtil.loadMechanics();

        recipeManager.loadRecipes();

      }
    }.runTaskAsynchronously(NexoAddon.getInstance());
  }
}
