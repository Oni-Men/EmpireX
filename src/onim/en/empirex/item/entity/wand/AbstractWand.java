package onim.en.empirex.item.entity.wand;

import java.util.Arrays;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.Droppable;
import onim.en.empirex.item.interfaces.LeftClickable;
import onim.en.empirex.magic.Wand;
import onim.en.empirex.magic.WandManager;
import onim.en.empirex.magic.spell.Spell;

public abstract class AbstractWand extends CustomItem implements LeftClickable, Droppable {

  public static final String MAGIC_WAND_ID = "magic_wand";

  public AbstractWand(String id, String name) {
    super(id, name, Arrays.asList("Left click to call spell", "Switch spell with drop item"));
  }

  @Override
  public boolean onDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();

    if (player.isSneaking()) {
      return false;
    }

    event.setCancelled(true);

    Item drop = event.getItemDrop();
    Wand wand = WandManager.getWand(drop.getItemStack());
    wand.nextSpell();
    return true;
  }

  @Override
  public void onLeftClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    event.setCancelled(true);

    if (player.getAttackCooldown() != 1F) {
      return;
    }

    Wand wand = WandManager.getWand(event.getItem());
    wand.castSpell(player);
  }

  public abstract double getCooldown();

  public abstract List<Spell> getDefaultSpells();

  @Override
  public boolean isGlowing() {
    return true;
  }

  @Override
  public ItemMeta modifyMeta(ItemMeta meta) {
    double attackSpeed = 1 / getCooldown();

    AttributeModifier modifier = new AttributeModifier("attack_speed", -4 + attackSpeed, Operation.ADD_NUMBER);
    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

    return meta;
  }

  @Override
  public ItemStack modifyItemStack(ItemStack stack) {
    Wand wand = WandManager.addWand(stack);
    wand.addSpells(getDefaultSpells());
    return stack;
  }
}
