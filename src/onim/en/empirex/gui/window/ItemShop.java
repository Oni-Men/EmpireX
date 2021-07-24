package onim.en.empirex.gui.window;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import onim.en.empirex.EmpireX;
import onim.en.empirex.item.ItemUtil;

public class ItemShop extends InteractiveWindow {

  private static boolean hasItem(Inventory bottomInventory, String pluginItemId) {
    return Stream.of(bottomInventory.getContents())
        .anyMatch(stack -> ItemUtil.getPluginItemId(stack).contentEquals(pluginItemId));
  }

  @Override
  public String getTitle() {
    return ChatColor.BLACK + "Item Shop";
  }

  @Override
  public String getId() {
    return "window.item_shop";
  }

  @Override
  public Inventory getWindow() {
    List<ItemStack> stackList = EmpireX.itemFactory.toStackList();
    int size = ((stackList.size() / 9) + 1) * 9;

    Inventory itemShop = Bukkit.createInventory(null, size, Component.text(getTitle()));
    stackList.forEach(stack -> itemShop.addItem(stack));

    return itemShop;
  }

  @Override
  public void onClick(InventoryClickEvent event) {
    super.onClick(event);

    ItemStack currentItem = event.getCurrentItem();
    if (currentItem == null)
      return;

    //ショップ外のスロットをクリックしたときは何もしない
    if (!this.isTopInventory(event.getRawSlot(), event.getView().getTopInventory().getSize()))
      return;

    String pluginItemId = ItemUtil.getPluginItemId(currentItem);
    if (pluginItemId == null)
      return;

    //購入しようとしているアイテムが重複不可で、かつ既に所持しているとき
    if (ItemUtil.isUnique(currentItem) && ItemShop.hasItem(event.getView().getBottomInventory(), pluginItemId))
      return;

    event.getWhoClicked().getInventory().addItem(currentItem);

    //同期的にビューを閉じる
    Bukkit.getServer().getScheduler().runTask(EmpireX.instance, () -> {
      event.getView().close();
    });
  }
}
