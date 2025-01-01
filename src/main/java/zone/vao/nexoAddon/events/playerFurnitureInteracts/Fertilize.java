package zone.vao.nexoAddon.events.playerFurnitureInteracts;

import com.nexomc.nexo.NexoPlugin;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.component.Fertilizer;
import zone.vao.nexoAddon.utils.InventoryUtil;

public class Fertilize {

  private static final NamespacedKey EVOLUTION_KEY = new NamespacedKey(NexoPlugin.instance(), "evolution");

  public static void onFertilize(NexoFurnitureInteractEvent event){

    Player player = event.getPlayer();
    String furnitureId = NexoFurniture.furnitureMechanic(event.getBaseEntity()).getItemID();

    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    Fertilizer fertilizer = NexoAddon.getInstance().getComponents().get(itemId).getFertilizer();

    if(itemId == null
        || !NexoAddon.getInstance().getComponents().containsKey(itemId)
        || fertilizer == null
        || !fertilizer.getUsableOn().contains(furnitureId)
        || !event.getBaseEntity().getPersistentDataContainer().has(EVOLUTION_KEY, PersistentDataType.INTEGER)
    ) return;

    fertilizeFurniture(event.getBaseEntity(), player, fertilizer);
  }

  private static void fertilizeFurniture(ItemDisplay itemDisplay, Player player, Fertilizer fertilizer) {

    PersistentDataContainer container = itemDisplay.getPersistentDataContainer();

    int evolutionTime = container.get(EVOLUTION_KEY, PersistentDataType.INTEGER);

    evolutionTime += fertilizer.getGrowthSpeedup();

    container.set(EVOLUTION_KEY, PersistentDataType.INTEGER, evolutionTime);

    InventoryUtil.removePartialStack(player, player.getInventory().getItemInMainHand(), 1);
  }
}
