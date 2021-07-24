package onim.en.empirex.item;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Strings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

@SuppressWarnings("deprecation")
public class ItemUtil {

  public static void setUndroppable(ItemStack stack, boolean undroppable) {
    if (stack == null)
      return;

    if (!stack.hasItemMeta())
      return;

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return;

    ItemUtil.removeText(lore, ItemTag.UNDROPPABLE.toString());

    // ドロップ不可能にしたい場合のみ追加。
    if (undroppable) {
      lore.add(ItemTag.UNDROPPABLE.toString());
    }

  }

  public static boolean isUndroppable(ItemStack stack) {
    if (stack == null)
      return false;

    if (!stack.hasItemMeta())
      return false;

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return false;

    return containsText(lore, ItemTag.UNDROPPABLE.toString());
  }

  public static void setUnclickable(ItemStack stack, boolean unclickable) {
    if (stack == null)
      return;

    if (!stack.hasItemMeta()) {
      return;
    }

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return;

    ItemUtil.removeText(lore, ItemTag.UNCLICKABLE.toString());

    // クリック不可能にしたい場合のみ追加。
    if (unclickable) {
      lore.add(ItemTag.UNCLICKABLE.toString());
    }

  }

  public static boolean isUnclickable(ItemStack stack) {
    if (stack == null)
      return false;

    if (!stack.hasItemMeta())
      return false;

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return false;

    return containsText(lore, ItemTag.UNCLICKABLE.toString());
  }

  public static void setUnique(ItemStack stack, boolean unique) {
    if (stack == null)
      return;

    if (!stack.hasItemMeta())
      return;

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return;

    ItemUtil.removeText(lore, ItemTag.UNIQUE.toString());

    if (unique) {
      lore.add(ItemTag.UNIQUE.toString());
    }
  }

  public static boolean isUnique(ItemStack stack) {
    if (stack == null)
      return false;

    if (!stack.hasItemMeta())
      return false;

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return false;

    return containsText(lore, ItemTag.UNIQUE.toString());
  }

  /**
   * id が null の場合 pluginId を削除します。
   * 
   * @param stack
   * @param id
   */
  public static void setPluginItemId(ItemStack stack, String id) {
    if (stack == null)
      return;

    if (!stack.hasItemMeta())
      return;

    List<Component> lore = stack.getItemMeta().lore();
    if (lore == null)
      return;

    Iterator<Component> iterator = lore.iterator();
    while (iterator.hasNext()) {
      Component next = iterator.next();

      if (!(next instanceof TextComponent)) {
        continue;
      }

      String content = ((TextComponent) next).content();

      if (content.startsWith(ChatColor.GRAY + "#")) {
        iterator.remove();
      }
    }

    if (!Strings.isNullOrEmpty(id)) {
      lore.add(Component.text(ChatColor.GRAY + "#" + id));
    }

    stack.lore(lore);
    return;
  }

  public static String getPluginItemId(ItemStack stack) {
    if (stack == null)
      return "";

    if (!stack.hasItemMeta())
      return "";

    List<String> lore = stack.getItemMeta().getLore();
    if (lore == null)
      return "";

    for (String str : lore) {
      if (str.startsWith(ChatColor.GRAY + "#")) {
        return str.replaceFirst(ChatColor.GRAY + "#", "");
      }
    }

    return "";
  }

  private static void removeText(List<String> lore, String text) {
    Iterator<String> iterator = lore.iterator();

    // いったん、全部消す。
    while (iterator.hasNext()) {
      String next = iterator.next();

      if (next.contentEquals(text)) {
        iterator.remove();
      }

    }
  }

  private static boolean containsText(List<String> lore, String str) {
    Iterator<String> iterator = lore.iterator();

    while (iterator.hasNext()) {
      String next = iterator.next();

      if (next.contentEquals(str))
        return true;
    }

    return false;
  }

}
