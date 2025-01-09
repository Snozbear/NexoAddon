package zone.vao.nexoAddon.events;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.handlers.RecipeManager;

public class PrepareRecipesListener implements Listener {

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void handlePrepareSmithing(PrepareSmithingEvent event) {
    SmithingInventory inventory = event.getInventory();

    ItemStack templateItem = inventory.getItem(0);
    ItemStack baseItem = inventory.getItem(1);
    ItemStack additionItem = inventory.getItem(2);

    if (templateItem == null || baseItem == null || additionItem == null) return;

    for (NamespacedKey key : RecipeManager.getRegisteredRecipes()) {
      SmithingTransformRecipe recipe = (SmithingTransformRecipe) NexoAddon.getInstance().getServer().getRecipe(key);
      if (recipe == null) continue;

      ItemStack copy = null;
      if(recipe.getBase() instanceof RecipeChoice.ExactChoice choice){
        copy = choice.getItemStack().clone();
        if(copy.getItemMeta() instanceof Damageable damageable){
          if(damageable.hasDamage() && damageable.getDamage() > 0) {
            damageable.setDamage(0);
            copy.setItemMeta(damageable);
          }
        }
      }
      if (recipe.getTemplate().test(templateItem)
          && (recipe.getBase().test(baseItem) || copy != null && recipe.getBase().test(copy))
          && recipe.getAddition().test(additionItem))
      {

        boolean copyTrim = RecipeManager.getRecipeConfig().getBoolean(key.getKey() + ".copy_trim", false);
        boolean copyEnchants = RecipeManager.getRecipeConfig().getBoolean(key.getKey() + ".copy_enchantments", true);
        boolean keepDurability = RecipeManager.getRecipeConfig().getBoolean(key.getKey() + ".keep_durability", true);

        ItemStack result = recipe.getResult();
        applyMetaTransformations(baseItem, result, copyEnchants, copyTrim, keepDurability);

        event.setResult(result);
      }
    }
  }

  private void applyMetaTransformations(ItemStack baseItem, ItemStack result, boolean copyEnchants, boolean copyTrim, boolean keepDurability) {
    ItemMeta baseMeta = baseItem.getItemMeta();
    ItemMeta resultMeta = result.getItemMeta();

    if (baseMeta == null || resultMeta == null) return;

    resultMeta.setDisplayName(baseMeta.hasDisplayName() ? baseMeta.getDisplayName() : resultMeta.getDisplayName());
    if (!resultMeta.hasLore() && baseMeta.hasLore()) {
      resultMeta.setLore(baseMeta.getLore());
    }

    if (copyEnchants) {
      baseMeta.getEnchants().forEach((enchant, level) -> resultMeta.addEnchant(enchant, level, true));
    }

    if (copyTrim && baseMeta instanceof ArmorMeta baseArmorMeta) {
      if (baseArmorMeta.hasTrim()) {
        ((ArmorMeta) resultMeta).setTrim(baseArmorMeta.getTrim());
      }
    }

    if(keepDurability && resultMeta instanceof Damageable damageable) {
      if(baseMeta instanceof Damageable baseDamageable) {
        damageable.setDamage(baseDamageable.getDamage());
      }
    }

    result.setItemMeta(resultMeta);
  }
}
