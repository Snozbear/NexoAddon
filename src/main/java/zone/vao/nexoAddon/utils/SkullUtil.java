package zone.vao.nexoAddon.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.nexomc.nexo.NexoPlugin;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class SkullUtil {

  public static String NEXO_HEAD_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY1ZTgzNWMxMTZlOGUyMDBlMmUwNmFhNTkzY2FiOGYxYTlmOGM0MGU3ZjAwNWE5Yzc2ZjEyZTI0ZjRjNjM3MCJ9fX0=";

  public static void applyTextures(){

    Map<String, Components> componentsList = new HashMap<>();
    NexoAddon.getInstance().getComponents().forEach((key, component) -> {
      if(component.getSkullValue() == null) return;
      componentsList.put(key, component);
    });

    componentsList.forEach((key, component) -> {

      NexoItems.items().stream().filter(a -> Objects.equals(NexoItems.idFromItem(a), key) && NexoItems.itemFromId(key).build().getType() == Material.PLAYER_HEAD).findFirst().ifPresent(item -> {

        ItemStack itemStack = item.build();

        NexoItems.itemMap().forEach((file, items) -> {

          if(!items.containsKey(key)) return;

          NexoItems.itemMap().get(file).remove(key);
          NexoPlugin.instance().configsManager().parseItemConfig$core().get(file).remove(key);
          final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

          PlayerProfile profile = getProfileBase64(component.getSkullValue().getValue(), key);
          if(profile == null) return;
          meta.setPlayerProfile(profile);

          itemStack.setItemMeta(meta);
          ItemBuilder itemBuilder =  new ItemBuilder(itemStack);

          NexoItems.itemMap().get(file).put(key, itemBuilder);
        });
      });
    });
  }

  private static PlayerProfile getProfileBase64(String base64, String itemId) {
    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
    PlayerTextures textures = profile.getTextures();
    URL urlObject;
    try {
      urlObject = getUrlFromBase64(base64);
    } catch (MalformedURLException exception) {
      NexoAddon.getInstance().getLogger().warning("Invalid base64: "+itemId);
      return null;
    }
    textures.setSkin(urlObject);
    profile.setTextures(textures);
    return profile;
  }

  public static URL getUrlFromBase64(String base64) throws MalformedURLException {
    try {
      String decoded = new String(Base64.getDecoder().decode(base64));
      return URI.create(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length())).toURL();
    } catch (Throwable t) {
      throw new MalformedURLException("Invalid base64 string: " + base64);
    }
  }
}
