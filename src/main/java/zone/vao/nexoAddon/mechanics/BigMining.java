package zone.vao.nexoAddon.mechanics;

import org.bukkit.Material;
import zone.vao.nexoAddon.NexoAddon;

import java.util.List;

public record BigMining(int radius, int depth, boolean switchable, List<Material> materials) {

  public static boolean isBigMiningTool(String toolId) {
    return toolId != null && NexoAddon.getInstance().getMechanics().containsKey(toolId) && NexoAddon.getInstance().getMechanics().get(toolId).getBigMining() != null;
  }
}
