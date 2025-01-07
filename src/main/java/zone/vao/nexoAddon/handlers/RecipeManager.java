package zone.vao.nexoAddon.handlers;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import zone.vao.nexoAddon.NexoAddon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class RecipeManager {
    private final List<NamespacedKey> registeredRecipes = new ArrayList<>();
    private final Map<NamespacedKey, String> recipeConfigMap = new HashMap<>();
    private File recipeFile;
    private FileConfiguration recipeConfig;

    public RecipeManager() {
        loadRecipeFile();
    }

    private void loadRecipeFile() {
        File recipeFolder = new File(NexoAddon.getInstance().getDataFolder(), "recipes");
        if (!recipeFolder.exists()) {
            recipeFolder.mkdirs();
        }

        recipeFile = new File(recipeFolder, "smithing.yml");

        if (!recipeFile.exists()) {
            try {
                InputStream resourceStream = NexoAddon.getInstance().getResource("recipes/smithing.yml");
                if (resourceStream == null) {
                    return;
                }

                Files.copy(resourceStream, recipeFile.toPath());
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to generate smithing.yml: " + e.getMessage());
            }
        }

        recipeConfig = YamlConfiguration.loadConfiguration(recipeFile);
    }

    public void loadRecipes() {
        Bukkit.getScheduler().runTask(NexoAddon.getInstance(), () -> {
            clearRegisteredRecipes();

            if (recipeConfig == null) {
                Bukkit.getLogger().severe("Recipes file not found");
                return;
            }

            recipeConfig.getKeys(false).forEach(recipeId -> {
                try {
                    addSmithingTransformRecipe(recipeId, recipeConfig);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Failed to load recipe " + recipeId + ": " + e.getMessage());
                }
            });
        });
    }


    public Map<NamespacedKey, String> getRecipeConfigMap() {
        return recipeConfigMap;
    }

    public FileConfiguration getRecipeConfig() {
        return recipeConfig;
    }


    private void clearRegisteredRecipes() {
        registeredRecipes.forEach(key -> {
            Bukkit.removeRecipe(key);
            Bukkit.getLogger().info("Removed recipe: " + key);
        });
        registeredRecipes.clear();
        recipeConfigMap.clear();
    }

    private void addSmithingTransformRecipe(String recipeId, FileConfiguration config) {
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

    private ItemStack parseItem(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null) return Objects.requireNonNull(NexoItems.itemFromId(nexoItemId)).build();

        String materialName = config.getString(path + ".minecraft_item");
        assert materialName != null;
        Material material = Material.matchMaterial(materialName);
        return material != null ? new ItemStack(material) : null;
    }

    private RecipeChoice parseRecipeChoice(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null) return new RecipeChoice.ExactChoice(Objects.requireNonNull(NexoItems.itemFromId(nexoItemId)).build());

        String materialName = config.getString(path + ".minecraft_item");
        assert materialName != null;
        Material material = Material.matchMaterial(materialName);
        return material != null ? new RecipeChoice.MaterialChoice(material) : null;
    }
}
