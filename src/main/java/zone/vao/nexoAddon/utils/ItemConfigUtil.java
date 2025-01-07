package zone.vao.nexoAddon.utils;

import com.nexomc.nexo.NexoPlugin;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.classes.Mechanics;

import java.io.File;
import java.util.*;

public class ItemConfigUtil {

  private static Set<File> itemFiles = new HashSet<>();

  public static HashMap<File, LinkedHashMap<String, ItemBuilder>> getItemConfigs(){

    return NexoPlugin.instance().configsManager().parseItemConfig();
  }

  public static Set<File> getItemFiles(){
    itemFiles.clear();
    itemFiles = NexoPlugin.instance().configsManager().parseItemConfig().keySet();
    return itemFiles;
  }

  public static void loadComponents() {
    NexoAddon.getInstance().getComponents().clear();

    for (File itemFile : itemFiles) {
      YamlConfiguration file = YamlConfiguration.loadConfiguration(itemFile);

      for (String itemId : file.getKeys(false)) {
        if (!file.isConfigurationSection(itemId)) continue;

        ConfigurationSection itemSection = file.getConfigurationSection(itemId);
        if (itemSection != null && itemSection.contains("Components")) {

          if(!NexoAddon.getInstance().getComponents().containsKey(itemId))
            NexoAddon.getInstance().getComponents().put(itemId, new Components(itemId));
          Components component = NexoAddon.getInstance().getComponents().get(itemId);

          if(itemSection.contains("Components.equippable")) {
            try {
              EquipmentSlot equippableSlot = EquipmentSlot.valueOf(itemSection.getString("Components.equippable.slot", "HEAD").toUpperCase());
              component.setEquippable(equippableSlot);
            } catch (Exception ignored) {
              continue;
            }
          }

          if(itemSection.contains("Components.jukebox_playable") && itemSection.contains("Components.jukebox_playable.song_key")) {
            String songKey = itemSection.getString("Components.jukebox_playable.song_key");
            component.setPlayable(songKey);
          }

          if(itemSection.contains("Components.fertilizer.growth_speedup") && itemSection.contains("Components.fertilizer.usable_on")) {
            int growthSpeedup = itemSection.getInt("Components.fertilizer.growth_speedup", 1000);
            List<String> usableOn = itemSection.getStringList("Components.fertilizer.usable_on");
            component.setFertilizer(growthSpeedup, usableOn);
          }
        }
      }
    }
  }

  public static void loadMechanics() {
    NexoAddon.getInstance().getMechanics().clear();

    for (File itemFile : itemFiles) {
      YamlConfiguration file = YamlConfiguration.loadConfiguration(itemFile);

      for (String itemId : file.getKeys(false)) {
        if (!file.isConfigurationSection(itemId)) continue;

        ConfigurationSection itemSection = file.getConfigurationSection(itemId);
        if (itemSection != null && itemSection.contains("Mechanics")) {

          if(!NexoAddon.getInstance().getMechanics().containsKey(itemId))
            NexoAddon.getInstance().getMechanics().put(itemId, new Mechanics(itemId));
          Mechanics mechanic = NexoAddon.getInstance().getMechanics().get(itemId);

          if(itemSection.contains("Mechanics.repair.ratio") || itemSection.contains("Mechanics.repair.fixed_amount")){

            mechanic.setRepair(itemSection.getDouble("Mechanics.repair.ratio"), itemSection.getInt("Mechanics.repair.fixed_amount"));
          }

          if(itemSection.contains("Mechanics.bigmining.radius") && itemSection.contains("Mechanics.bigmining.depth")){

            mechanic.setBigMining(itemSection.getInt("Mechanics.bigmining.radius", 1), itemSection.getInt("Mechanics.bigmining.depth", 1));
          }

          if(itemSection.contains("Mechanics.bedrockbreak.hardness") && itemSection.contains("Mechanics.bedrockbreak.probability")){
            mechanic.setBedrockBreak(itemSection.getInt("Mechanics.bedrockbreak.hardness"), itemSection.getDouble("Mechanics.bedrockbreak.probability"), itemSection.getInt("Mechanics.bedrockbreak.durability_cost", 1), itemSection.getBoolean("Mechanics.bedrockbreak.disable_on_first_layer", true));
          }
        }
      }
    }
  }
}
