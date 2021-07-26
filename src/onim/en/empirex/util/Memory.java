package onim.en.empirex.util;

import static org.bukkit.persistence.PersistentDataType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class Memory {

  private static final String TAG_KEY = "__tags__";
  private final PersistentDataContainer data;

  private final Runnable updater;

  public Memory(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();

    if (meta == null) {
      throw new NullPointerException("meta is null");
    }

    data = meta.getPersistentDataContainer();
    updater = () -> {
      stack.setItemMeta(meta);
    };
  }

  public Memory(Entity entity) {
    data = entity.getPersistentDataContainer();
    updater = null;
  }

  protected void update() {
    if (this.updater != null) {
      this.updater.run();
    }
  }

  public void addTag(String tag) {
    List<String> tags = this.deserializeTags();
    tags.add(tag);
    updateTags(tags);
  }

  public void removeTag(String tag) {
    List<String> tags = this.deserializeTags();
    tags.remove(tag);
    updateTags(tags);
  }

  public boolean hasTag(String tag) {
    return this.deserializeTags().contains(tag);
  }

  private List<String> deserializeTags() {
    String serialized = data.getOrDefault(KeyFactory.get(TAG_KEY), STRING, "");
    return new ArrayList<>(Arrays.asList(serialized.split(",")));
  }

  private void updateTags(List<String> tags) {
    String serialized = String.join(",", tags);
    data.set(KeyFactory.get(TAG_KEY), STRING, serialized);
    this.update();
  }

  public void setInt(String key, int i) {
    data.set(KeyFactory.get(key), INTEGER, i);
  }
}
