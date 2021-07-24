package onim.en.empirex.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;
import onim.en.empirex.EmpireX;
import onim.en.empirex.base.Identifiable;

public abstract class CustomItem implements Identifiable {

  public static final List<ItemTag> UNIQUE_UNDROPPABLE =
      Arrays.asList(ItemTag.UNIQUE, ItemTag.UNDROPPABLE);

  public final String name;
  public final List<Component> description;
  public final String id;

  public CustomItem(String id, String name) {
    this(id, name, Collections.emptyList());
  }

  public CustomItem(String id, String name, List<String> description) {
    this.id = id;
    this.name = name;

    this.description = new ArrayList<>(description.size());
    for (String s : description) {
      this.description.add(Component.text(s));
    }
  }

  public String getName() {
    return this.name;
  };

  public List<Component> getDescription() {
    return this.description;
  };

  public String id() {
    return this.id;
  }

  public abstract ItemStack getItemStack();

  public List<ItemTag> getTags() {
    return UNIQUE_UNDROPPABLE;
  }

  public int getCooltime() {
    return 0;
  }

  public final boolean isValidItem(ItemStack stack) {
    String pluginItemId = ItemUtil.getPluginItemId(stack);
    if (pluginItemId == null)
      return false;

    return pluginItemId.contentEquals(this.id());
  }

  public final ItemStack getFormattedItem() {
    ItemStack stack = this.getItemStack();
    ItemMeta meta = stack.getItemMeta();

    meta.displayName(Component.text(getName()));

    List<Component> lore = meta.lore();
    if (lore == null) {
      lore = new ArrayList<>();
    }

    lore.addAll(this.getDescription());
    lore.add(Component.empty());

    List<ItemTag> tags = this.getTags();
    if (tags != null) {
      lore.addAll(tags.stream().map(tag -> tag.toComponent()).collect(Collectors.toList()));
    }

    lore.add(Component.empty());
    meta.lore(lore);
    stack.setItemMeta(meta);

    ItemUtil.setPluginItemId(stack, id());
    return stack;
  }

  public boolean isCooldown(ItemStack stack) {
    PersistentDataContainer data = stack.getItemMeta().getPersistentDataContainer();
    if (!data.has(new NamespacedKey(EmpireX.instance, "cooldown"), PersistentDataType.LONG)) {
      return false;
    }

    Long cooldown = data.get(new NamespacedKey(EmpireX.instance, "cooldown"), PersistentDataType.LONG);
    return System.currentTimeMillis() > cooldown;
  }

  public void setCooldown(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    PersistentDataContainer data = meta.getPersistentDataContainer();
    long cooldown = System.currentTimeMillis() + this.getCooltime() * 1000;
    data.set(new NamespacedKey(EmpireX.instance, "cooldown"), PersistentDataType.LONG, cooldown);
    stack.setItemMeta(meta);
  }
}
