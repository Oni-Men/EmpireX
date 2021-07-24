package onim.en.empirex.event.civilian.item;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import onim.en.empirex.event.civilian.CivilianEvent;
import onim.en.empirex.player.Civilian;

public class HeldEvent extends CivilianEvent {

  public enum HeldAction {
      START,
      STOP;
  }

  private static final HandlerList handlers = new HandlerList();
  private final HeldAction action;

  private final ItemStack newItem, oldItem;

  public HeldEvent(Civilian who, HeldAction action, ItemStack newItem, ItemStack oldItem) {
    super(who);
    this.action = action;
    this.newItem= newItem;
    this.oldItem= oldItem;
  }

  public HeldAction getAction() {
    return this.action;
  }

  public ItemStack getNewItemStack() {
    return this.newItem;
  }

  public ItemStack getOldItemStack() {
    return this.oldItem;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
