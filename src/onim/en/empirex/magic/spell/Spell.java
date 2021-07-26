package onim.en.empirex.magic.spell;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import onim.en.empirex.base.Identifiable;

public interface Spell extends Identifiable {

  public String id();

  public String getName();

  public String getDescription();

  public int getCost();

  /**
   *
   * @return tick of cooltime for this spell
   */
  public int getCooltime();

  /**
   *
   * @param player
   * @return 詠唱に成功したらTrueを返す
   */
  public boolean activate(Player player);

  public default ItemStack toItemStack() {
    return this.toItemStack(false);
  }

  public ItemStack toItemStack(boolean available);
}
