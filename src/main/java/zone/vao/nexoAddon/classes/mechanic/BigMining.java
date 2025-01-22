package zone.vao.nexoAddon.classes.mechanic;

import zone.vao.nexoAddon.NexoAddon;

public record BigMining(int radius, int depth, boolean switchable) {

  public static boolean isBigMiningTool(String toolId) {
    return toolId != null && NexoAddon.getInstance().getMechanics().containsKey(toolId) && NexoAddon.getInstance().getMechanics().get(toolId).getBigMining() != null;
  }
}
