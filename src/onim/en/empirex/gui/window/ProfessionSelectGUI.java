package onim.en.empirex.gui.window;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Strings;

import net.kyori.adventure.text.Component;
import onim.en.empirex.EmpireX;
import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionManager;
import onim.en.empirex.profession.ProfessionType;

public class ProfessionSelectGUI extends InteractiveWindow {

  @Override
  public String getTitle() {
    return ChatColor.DARK_GREEN + "職業安定所";
  }

  @Override
  public String getId() {
    return "window.profession_select";
  }

  @Override
  public Inventory getWindow() {
    Inventory window = Bukkit.createInventory(null, 9 * 3, Component.text(getTitle()));

    for (Profession profession : ProfessionManager.getProfessions()) {

      ItemStack stack = new ItemStack(Material.SKULL_BANNER_PATTERN);
      ItemMeta meta = stack.getItemMeta();

      meta.displayName(Component.text(ChatColor.BLUE + profession.getType().getName()));
      meta.lore(profession.description());

      stack.setItemMeta(meta);
      ItemUtil.setPluginItemId(stack, profession.getType().name());
      window.addItem(stack);
    }

    return window;
  }

  @Override
  public void onClick(InventoryClickEvent event) {
    super.onClick(event);

    ItemStack currentItem = event.getCurrentItem();
    if (currentItem == null)
      return;

    // ショップ外のスロットをクリックしたときは何もしない
    if (!this.isTopInventory(event.getRawSlot(), event.getView().getTopInventory().getSize()))
      return;

    String pluginItemId = ItemUtil.getPluginItemId(currentItem);
    System.out.println(pluginItemId);
    if (Strings.isNullOrEmpty(pluginItemId))
      return;

    ProfessionType type = ProfessionType.valueOf(pluginItemId);

    if (type == null) {
      return;
    }

    EmpireX.civilianFactory.executeIfPresent((Player) event.getWhoClicked(), c -> {
      c.setProfessionType(type);
    });

    // 同期的にビューを閉じる
    Bukkit.getServer().getScheduler().runTask(EmpireX.instance, () -> {
      event.getView().close();
    });
  }

}
