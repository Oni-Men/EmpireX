package onim.en.empirex.gui.window;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.magic.Wand;
import onim.en.empirex.magic.WandManager;

public class SpellEditor extends InteractiveWindow {

  private final Wand wand;
  private final Inventory spellEditor;

  public SpellEditor(ItemStack wand) {
    this.wand = WandManager.getWand(wand);
    if (wand == null) {
      throw new IllegalArgumentException("given item is not wand.");
    }

    this.spellEditor = null;

//    Collection<NBTTagCompound> registeredSpells = MagicHandler.getSpellAllNBT(wand);
//
//    int size = registeredSpells.size();
//    this.spellEditor = Bukkit.createInventory(null, size + (9 - (size % 9)), Component.text(this.getTitle()));
//
//    for (NBTTagCompound nbt : registeredSpells) {
//      Spell spell = SpellRegistry.getSpellById(nbt.getString(Spell.SPELL_ID));
//      spellEditor.addItem(spell.toItemStack(nbt.getBoolean(Spell.SPELL_AVAILABLE)));
//    }

  }

  @Override
  public String getTitle() {
    return ChatColor.LIGHT_PURPLE + "Spell Editor";
  }

  @Override
  public String getId() {
    return "window.spell_editor";
  }

  @Override
  public Inventory getWindow() {
    return this.spellEditor;
  }

  @Override
  public void onClick(InventoryClickEvent event) {
    super.onClick(event);

    ItemStack stack = event.getCurrentItem();

    if (!this.isTopInventory(event.getRawSlot(), event.getView().getTopInventory().getSize()))
      return;

    if (stack == null)
      return;

    switch (stack.getType()) {
    case GREEN_STAINED_GLASS_PANE:
      stack.setType(Material.RED_STAINED_GLASS_PANE);
      break;
    case RED_STAINED_GLASS_PANE:
      stack.setType(Material.GREEN_STAINED_GLASS_PANE);
      break;
    default:
      break;
    }

    event.setCurrentItem(stack);
  }

  @Override
  public void onClose(InventoryCloseEvent event) {
    super.onClose(event);

    if (!(event.getPlayer() instanceof Player))
      return;

    // InventoryWandData data = new InventoryWandData((Player) event.getPlayer(), EquipmentSlot.HAND,
    // wand);

    for (ItemStack stack : this.spellEditor.getContents()) {
      if (stack == null)
        continue;

      String spellId = ItemUtil.getPluginItemId(stack);
      if (spellId.isEmpty())
        continue;

      // MagicHandler.setAvailable(SpellRegistry.getSpellById(spellId), data,
      // stack.getType() == Material.GREEN_STAINED_GLASS_PANE);

    }

  }
}
