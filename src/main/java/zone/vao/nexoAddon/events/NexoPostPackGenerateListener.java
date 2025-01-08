package zone.vao.nexoAddon.events;

import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.handlers.RecipeManager;
import zone.vao.nexoAddon.utils.RecipesUtil;

public class NexoPostPackGenerateListener implements Listener {

  @EventHandler
  public void onPostPackGen(NexoPostPackGenerateEvent event) {

    RecipeManager.clearRegisteredRecipes();
    RecipesUtil.loadRecipes();
  }
}
