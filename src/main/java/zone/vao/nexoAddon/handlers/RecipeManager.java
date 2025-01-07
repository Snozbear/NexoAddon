package zone.vao.nexoAddon.handlers;

import com.nexomc.nexo.api.NexoItems;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.*;
import zone.vao.nexoAddon.NexoAddon;

import java.io.File;
import java.util.*;


public class RecipeManager {
    @Getter
    private static final List<NamespacedKey> registeredRecipes = new ArrayList<>();
    @Getter
    private static final Map<NamespacedKey, String> recipeConfigMap = new HashMap<>();
    @Getter
    @Setter
    private static File recipeFile;
    @Getter
    @Setter
    private static FileConfiguration recipeConfig;


    public static void addSmithingTransformRecipe(String recipeId, FileConfiguration config) {
        ItemStack resultTemplate = parseItem(config, recipeId + ".result");
        RecipeChoice template = parseRecipeChoice(config, recipeId + ".template");
        RecipeChoice base = parseRecipeChoice(config, recipeId + ".base");
        RecipeChoice addition = parseRecipeChoice(config, recipeId + ".addition");

        if (resultTemplate == null || template == null || base == null || addition == null) {
            throw new IllegalArgumentException("Invalid recipe configuration for " + recipeId);
        }

        NamespacedKey key = new NamespacedKey(NexoAddon.getInstance(), recipeId);

        if (Bukkit.getRecipe(key) == null) {
            SmithingTransformRecipe recipe = new SmithingTransformRecipe(key, resultTemplate, template, base, addition);
            Bukkit.addRecipe(recipe);
            registeredRecipes.add(key);
            recipeConfigMap.put(key, recipeId);
            Bukkit.getLogger().info("Registered smithing transform recipe: " + recipeId);
        } else {
            Bukkit.getLogger().info("Recipe " + recipeId + " already exists, skipping.");
        }
    }

    private static ItemStack parseItem(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null) return Objects.requireNonNull(NexoItems.itemFromId(nexoItemId)).build();

        String materialName = config.getString(path + ".minecraft_item");
        assert materialName != null;
        Material material = Material.matchMaterial(materialName);
        return material != null ? new ItemStack(material) : null;
    }

    private static RecipeChoice parseRecipeChoice(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null && NexoItems.itemFromId(nexoItemId) != null)
            return new RecipeChoice.ExactChoice(NexoItems.itemFromId(nexoItemId).build());

        String materialName = config.getString(path + ".minecraft_item");
        assert materialName != null;
        Material material = Material.matchMaterial(materialName);
        return material != null ? new RecipeChoice.MaterialChoice(material) : null;
    }
}
