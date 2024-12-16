package zone.vao.nexoAddon.classes.populators.treePopulator;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CustomTreePopulator extends BlockPopulator {

  private final TreePopulator treePopulator;

  public CustomTreePopulator(TreePopulator treePopulator) {
    this.treePopulator = treePopulator;
  }

  @Override
  public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
    for (CustomTree tree : treePopulator.getTrees()) {
      replaceTrees(worldInfo, random, chunkX, chunkZ, limitedRegion, tree);
    }
  }

  private void replaceTrees(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion, CustomTree tree) {
    if (random.nextDouble() > tree.getChance()) return;
    for (int x = (chunkX << 4); x < (chunkX << 4) + 16; x++) {
      for (int z = (chunkZ << 4); z < (chunkZ << 4) + 16; z++) {
        for (int y = tree.getMinLevel(); y <= tree.getMaxLevel(); y++) {
          if (!limitedRegion.isInRegion(x, y, z)) {
            continue;
          }
          if(!tree.biomes.contains(limitedRegion.getBiome(x, y, z))) continue;
          Material currentMaterial = limitedRegion.getType(x, y, z);
          if (currentMaterial.toString().endsWith("_LOG") || currentMaterial.toString().endsWith("_LEAVES")) {
            replaceTreeBlock(limitedRegion, x, y, z, tree);
          }
        }
      }
    }
  }

  private void replaceTreeBlock(LimitedRegion limitedRegion, int x, int y, int z, CustomTree tree) {
    Material currentMaterial = limitedRegion.getType(x, y, z);

    if (currentMaterial.toString().endsWith("_LOG")) {
      limitedRegion.setBlockData(x, y, z, tree.getLog().getBlockData());
    }
    else if (currentMaterial.toString().endsWith("_LEAVES")) {
      limitedRegion.setBlockData(x, y, z, tree.getLeaves().getBlockData());
    }
  }
}
